package com.luyang.myapplication.util;

import com.luyang.myapplication.vo.ThreadInfo;

import java.util.List;

/**
 * Created by luyang on 2017/12/20.
 */

public interface ThreadDao {

    public void insertThread(ThreadInfo threadInfo);

    public void deleteThread(String url , int id);

    public void updateThread(String url , int id , long finished);

    /**
     * 查询文件的所有线程信息
     * @param url
     * @return
     */
    public List<ThreadInfo> getAllThread(String url);

    /**
     * 判断线程是否存在
     * @param url
     * @param id
     * @return
     */
    public boolean isExists(String url ,int id);




}
