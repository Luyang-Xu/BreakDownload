package com.luyang.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.luyang.myapplication.service.DownloadService;
import com.luyang.myapplication.vo.FileInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView title;

    private ProgressBar download_bar;

    private Button start;

    private Button stop;

    private Button cancel;

    private FileInfo fileInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        //注册广播接收器
        IntentFilter filter = new IntentFilter();
        filter.addAction(DownloadService.ACTION_UPDATE);
        registerReceiver(receiver,filter);
    }

    public void init(){
        title = findViewById(R.id.title);
        download_bar = findViewById(R.id.download_bar);
        download_bar.setMax(100);
        start = findViewById(R.id.start_button);
        stop = findViewById(R.id.stop_button);
        cancel = findViewById(R.id.cancel_button);
        fileInfo = new FileInfo();
        String url= "https://gss0.baidu.com/5r1ZsjOhKgQFm2e88IuM_a/srf/mac/baiduinput_mac_v5.0.0.23_1000e.dmg";
        fileInfo.setUrl(url);
        fileInfo.setFileName("百度输入法");
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(MainActivity.this,DownloadService.class);
        switch (v.getId()){
            case R.id.start_button:
                intent.setAction(DownloadService.ACTION_START);
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);
                break;
            case R.id.stop_button:
                intent.setAction(DownloadService.ACTION_STOP);
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);
                break;
            case R.id.cancel_button:
                intent.setAction(DownloadService.ACTION_CANCEL);
                intent.putExtra("fileInfo",fileInfo);
                startService(intent);
                break;
            default:
        }
    }


    //广播接收器，Activity接收Service中来的数据
    /**
     * 更新UI的广播数据
     */
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(DownloadService.ACTION_UPDATE)){
                int progress = intent.getIntExtra("finished",0);
                download_bar.setProgress(progress);
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
