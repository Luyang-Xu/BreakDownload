package com.luyang.myapplication.service;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import com.luyang.myapplication.util.HttpUtil;
import com.luyang.myapplication.vo.FileInfo;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;


/**
 * Created by luyang on 2017/12/19.
 */

public class DownloadService extends Service {

    public static final String ACTION_START = "ACTION_START";
    public static final String ACTION_STOP = "ACTION_STOP";
    public static final String ACTION_CANCEL = "ACTION_CANCEL";
    public static final String ACTION_UPDATE = "ACTION_UPDATE";

    private DownloadTask task;

    //handler通信判断
    public static final int MSG_INIT = 0;

    //下载路径
    public static final String FILEPATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FileInfo file;
        switch (intent.getAction()) {
            case ACTION_START:
               file = (FileInfo) intent.getSerializableExtra("fileInfo");
                new InitThread(file).start();
                break;
            case ACTION_STOP:
                file = (FileInfo) intent.getSerializableExtra("fileInfo");
                if(task!=null){
                    task.isPaused = true;
                }
                break;
            case ACTION_CANCEL:
                break;
            default:
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 子线程和service的通信
     */
    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_INIT:
                    FileInfo f =(FileInfo) msg.obj;
                    Log.d("HELLO","HELLO:"+f.getLength());
                    task = new DownloadTask(f,DownloadService.this);
                    task.download();
                    break;
            }
        }
    };

//在子线程中完成下载的操作
    class InitThread extends Thread{

        private FileInfo file = null;

        public InitThread(FileInfo f){
            file= f;
        }

        @Override
        public void run() {
            RandomAccessFile raf =null;
            try {
                long fileLength = HttpUtil.getResourceLength(file.getUrl());
                //设置问价下载到本地后的路径有以及名称
                File dir = new File(FILEPATH);
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File newFile = new File(dir, file.getFileName());
                raf = new RandomAccessFile(newFile, "rwd");
                raf.setLength(fileLength);
                file.setLength(fileLength);
                handler.obtainMessage(MSG_INIT,file).sendToTarget();

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
