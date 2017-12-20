package com.luyang.myapplication.service;

import android.content.Context;
import android.content.Intent;

import com.luyang.myapplication.util.ThreadDaoImpl;
import com.luyang.myapplication.vo.FileInfo;
import com.luyang.myapplication.vo.ThreadInfo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by luyang on 2017/12/20.
 */

public class DownloadTask {

    private FileInfo fileInfo;
    private Context context;
    private ThreadDaoImpl dao;
    private long progress;
    public boolean isPaused;
    public boolean isCanceld;

    public DownloadTask(FileInfo fileInfo, Context context) {
        this.fileInfo = fileInfo;
        this.context = context;
        dao = new ThreadDaoImpl();
    }


    public void download(){
        //读取数据的线程信息
        List<ThreadInfo> list = dao.getAllThread(fileInfo.getUrl());
        ThreadInfo ti = null;
        if(list.size() == 0){
            ti = new ThreadInfo(0,fileInfo.getUrl(),0,fileInfo.getLength(),0);

        }else {
            ti =list.get(0);
        }
        new DownloadThread(ti).start();
    }

    class DownloadThread extends Thread {
        private ThreadInfo threadInfo;
        private RandomAccessFile raf;
        InputStream is;

        public DownloadThread(ThreadInfo threadInfo) {
            this.threadInfo = threadInfo;
        }

        @Override
        public void run() {
            super.run();
            //向数据库插入线程信息
            if (!dao.isExists(threadInfo.getUrl(), threadInfo.getId())) {
                dao.insertThread(threadInfo);
            }
            //设置下载的开始位置
            long start = threadInfo.getStart() + threadInfo.getFinished();

            //找到开始写入的位置
            try {
                File file = new File(DownloadService.FILEPATH, fileInfo.getFileName());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(start);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //开始下载
            try {
                //读文件，写本地
                OkHttpClient client = new OkHttpClient();
                Request req = new Request.Builder().addHeader("RANGE", "bytes=" + start + "-").url(fileInfo.getUrl()).build();
                Response res = client.newCall(req).execute();
                //定义进度通知的广播

                Intent intent = new Intent(DownloadService.ACTION_UPDATE);
                progress += threadInfo.getFinished();
                byte b[] = new byte[1024];
                int len = 0;
                is = res.body().byteStream();
                long curTime = System.currentTimeMillis();
                while ((len = is.read(b)) != -1) {
                    progress += len;
                    raf.write(b, 0, len);
                    if (System.currentTimeMillis() - curTime > 500) {
                        //下载进度广播给Activity
                        curTime = System.currentTimeMillis();
                        intent.putExtra("finished", progress * 100 / fileInfo.getLength());
                        context.sendBroadcast(intent);
                    }

                    //下载暂停时，保存进度
                    if (isPaused) {
                        dao.updateThread(threadInfo.getUrl(), threadInfo.getId(), progress);
                        return;
                    }

                }

                //下载成功删除ID
                dao.deleteThread(threadInfo.getUrl(), threadInfo.getId());
                res.body().close();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                try {
                    raf.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
