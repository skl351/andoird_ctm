package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/12.
 */

public class item_info implements Serializable {

    private String id;
    private String timestamp;
    private String title;
    private String content;
    private boolean flag_review;
    private String url;

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url == null ? "" : url;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getTimestamp() {
        return timestamp == null ? "" : timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp == null ? "" : timestamp;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content == null ? "" : content;
    }

    public boolean isFlag_review() {
        return flag_review;
    }

    public void setFlag_review(boolean flag_review) {
        this.flag_review = flag_review;
    }

    @Override
    public String toString() {
        return "item_info{" +
                "id='" + id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", flag_review=" + flag_review +
                '}';
    }
}
