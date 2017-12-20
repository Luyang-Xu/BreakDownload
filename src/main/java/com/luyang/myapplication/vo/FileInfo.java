package com.luyang.myapplication.vo;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by luyang on 2017/12/19.
 */

public class FileInfo extends DataSupport implements Serializable{
    private int id;
    private String url;
    private String fileName;
    private long length;
    private long finished;

    public FileInfo() {
    }

    public FileInfo(int id, String url, String fileName, long length, long finished) {
        this.id = id;
        this.url = url;
        this.fileName = fileName;
        this.length = length;
        this.finished = finished;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setLength(long length) {
        this.length = length;
    }

    public void setFinished(long finished) {
        this.finished = finished;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getFileName() {
        return fileName;
    }

    public long getLength() {
        return length;
    }

    public long getFinished() {
        return finished;
    }
}
