package com.communication.printer;

import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @author Wilber
 */
public class MainActivity extends AppCompatActivity {
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = this.findViewById(R.id.textTest);
        Button btnBluetooth = (Button) this.findViewById(R.id.btnBluetooth);
        Button btnTCP = (Button) this.findViewById(R.id.btnTCP);
        btnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("*/*");
                    intent.setClassName("com.android.bluetooth" , "com.android.bluetooth.opp.BluetoothOppLauncherActivity");
                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File("/sdcard/image.jpg")));
                    startActivity(intent);
                    textView.setText("蓝牙是否开启="+ UUID.randomUUID().toString());
                }catch (Exception e){
                    textView.setText("蓝牙是否开启="+e.getMessage());
                }

            }
        });
        btnTCP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        textView.setText("我是客户端，服务器说："+info+",uuid="+uuid);
                    }
                    //4.关闭资源
                    br.close();
                    is.close();
                    pw.close();
                    os.close();
                    socket.close();
                } catch (Exception e) {
                    textView.setText("发送TCP异常=" + e.getLocalizedMessage());
                }
            }
        });
    }
}
