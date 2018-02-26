package com.ayi.entity;

/**
 * Created by oceanzhang on 16/3/25.
 */
public class ServiceShow {
    private String time;
    private String address;
    private String content;

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time == null ? "" : time;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address == null ? "" : address;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content == null ? "" : content;
    }

    @Override
    public String toString() {
        return "ServiceShow{" +
                "time='" + time + '\'' +
                ", address='" + address + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
