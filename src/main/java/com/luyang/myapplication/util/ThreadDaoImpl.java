package com.luyang.myapplication.util;

import com.luyang.myapplication.vo.ThreadInfo;
import org.litepal.crud.DataSupport;
import java.util.List;

/**
 * Created by luyang on 2017/12/20.
 */

public class ThreadDaoImpl implements ThreadDao {
    @Override
    public void insertThread(ThreadInfo threadInfo) {
        threadInfo.save();
    }

    @Override
    public void deleteThread(String url, int id) {
        DataSupport.deleteAll(ThreadInfo.class, "url=? and id=?", url, String.valueOf(id));
    }

    @Override
    public void updateThread(String url, int id, long finished) {
        ThreadInfo ti = new ThreadInfo();
        ti.setFinished(finished);
        ti.updateAll("url = ? and id = ?", url, String.valueOf(id));
    }

    @Override
    public List<ThreadInfo> getAllThread(String url) {
        List<ThreadInfo> threadList = DataSupport.where("url = ?", url).find(ThreadInfo.class);
        return threadList;
    }

    @Override
    public boolean isExists(String url, int id) {
        ThreadInfo ti = DataSupport.where("url = ? and id = ?", url, String.valueOf(id)).findFirst(ThreadInfo.class);
        if (ti != null) {
            return true;
        }
        return false;
    }
}
