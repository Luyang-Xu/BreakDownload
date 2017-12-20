package com.luyang.myapplication.util;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by luyang on 2017/12/19.
 */

public class HttpUtil {

    public static void sendHttpRequest(){


    }

    /**
     * 计算记载文件长度
     * @param address
     * @return
     * @throws IOException
     */
    public static long getResourceLength(String address) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request req = new Request.Builder().url(address).build();
        Response res = client.newCall(req).execute();
        if(res != null && res.isSuccessful()){
            long contentLength = res.body().contentLength();
            res.body().close();
            return contentLength;
        }
        return 0;
    }
}
