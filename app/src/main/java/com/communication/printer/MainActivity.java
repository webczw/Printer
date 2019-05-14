package com.communication.printer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
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
        Button sendBt = (Button) this.findViewById(R.id.buttonTest);

        sendBt.setOnClickListener(new View.OnClickListener() {

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
    }
}
