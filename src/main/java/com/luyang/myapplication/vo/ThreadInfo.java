package com.luyang.myapplication.vo;

import org.litepal.crud.DataSupport;

/**
 * Created by luyang on 2017/12/19.
 */

public class ThreadInfo extends DataSupport{
    private int id;
    private String url;
    private long start;
    private long end;
    private long finished;

    public ThreadInfo() {
    }

    public ThreadInfo(int id, String url, long start, long end, long finished) {
        this.id = id;
        this.url = url;
        this.start = start;
        this.end = end;
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public long getFinished() {
        return finished;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }
}
