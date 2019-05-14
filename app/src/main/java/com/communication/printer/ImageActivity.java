package com.communication.printer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.communication.printer.utils.HttpUtils;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.UUID;


/**
 * @author Wilber
 */
public class ImageActivity extends AppCompatActivity {
    private String data ;
    private ImageView ivInternet;
    private Handler handler;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.send_bluetooth:

                    try {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("*/*");
                        intent.setClassName("com.android.bluetooth" , "com.android.bluetooth.opp.BluetoothOppLauncherActivity");
                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/sdcard/image.jpg")));
                        startActivity(intent);
                        Log.i("info",data+","+"蓝牙是否开启="+ UUID.randomUUID().toString());
                    }catch (Exception e){
                        Log.i("info",data+","+"蓝牙是否开启异常="+e.getMessage());
                    }
                    return true;
                case R.id.send_tcp:
                    try {

                        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());

                        //1.创建客户端Socket，指定服务器地址和端口
                        Socket socket=new Socket("192.168.1.100", 8888);
                        //2.获取输出流，向服务器端发送信息,字节输出流
                        OutputStream os=socket.getOutputStream();
                        //将输出流包装为打印流
                        PrintWriter pw=new PrintWriter(os);
                        String uuid = UUID.randomUUID().toString();
                        pw.write(uuid);
                        pw.flush();
                        socket.shutdownOutput();//关闭输出流
                        //3.获取输入流，并读取服务器端的响应信息
                        InputStream is=socket.getInputStream();
                        BufferedReader br=new BufferedReader(new InputStreamReader(is));
                        String info=null;
                        while((info=br.readLine())!=null){
                            Log.i("info",data+","+"我是客户端，服务器说："+info+",uuid="+uuid);
                        }
                        //4.关闭资源
                        br.close();
                        is.close();
                        pw.close();
                        os.close();
                        socket.close();
                    } catch (Exception e) {
                        Log.i("info",data+","+"发送TCP异常=" + e.getLocalizedMessage());
                    }

                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        Intent intent = getIntent();
        data = intent.getStringExtra("extra_data");
        setTitle(getResources().getText(R.string.title_activity_model)+" - "+data);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ivInternet = (ImageView) findViewById(R.id.ivInternet);

        // 定义一个handler，用于接收消息
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                Bitmap bmp = null;
                // 通过消息码确定使用什么方式传递图片信息
                if (msg.what == 0) {
                    bmp = (Bitmap) msg.obj;
                    Log.i("info","使用obj传递数据");
                } else {
                    Bundle ble = msg.getData();
                    bmp = (Bitmap) ble.get("bmp");
                    Log.i("info","使用Bundle传递数据");
                }
                // 设置图片到ImageView中
                ivInternet.setImageBitmap(bmp);
            }
        };


        ivInternet.setImageBitmap(null);
        //定义一个线程类
        new Thread() {
            @Override
            public void run() {
                try {
                    //获取网络图片
                    InputStream inputStream = HttpUtils.getImageViewInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Message msg = new Message();
                    Random rd = new Random();
                    int ird = rd.nextInt(10);
                    //通过一个随机数，随机选择通过什么方式传递图片信息到消息中
                    if (ird / 2 == 0) {
                        msg.what = 0;
                        msg.obj = bitmap;
                    } else {
                        Bundle bun = new Bundle();
                        bun.putParcelable("bmp", bitmap);
                        msg.what = 1;
                        msg.setData(bun);
                    }
                    //发送消息
                    handler.sendMessage(msg);
                } catch (Exception e) {

                }
            }
        }.start();

    }

}
