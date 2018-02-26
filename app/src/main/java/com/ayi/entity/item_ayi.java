package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/2/19.
 */

public class item_ayi implements Serializable {

    @Override
    public String toString() {
        return "item_ayi{" +
                "id='" + id + '\'' +
                ", bitthday='" + bitthday + '\'' +
                ", sex='" + sex + '\'' +
                ", times='" + times + '\'' +
                ", dis='" + dis + '\'' +
                ", headimg='" + headimg + '\'' +
                ", service='" + service + '\'' +
                ", name='" + name + '\'' +
                ", longitude='" + longitude + '\'' +
                ", latitude='" + latitude + '\'' +
                ", place='" + place + '\'' +
                '}';
    }

    private String id;
    private String bitthday;
    private String sex;
    private String times;
    private String dis;
    private String headimg;
    private String service;
    private String name;
    private String longitude;
    private String latitude;
    private String place;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getBitthday() {
        return bitthday == null ? "" : bitthday;
    }

    public void setBitthday(String bitthday) {
        this.bitthday = bitthday == null ? "" : bitthday;
    }

    public String getSex() {
        return sex == null ? "" : sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? "" : sex;
    }

    public String getTimes() {
        return times == null ? "" : times;
    }

    public void setTimes(String times) {
        this.times = times == null ? "" : times;
    }

    public String getDis() {
        return dis == null ? "" : dis;
    }

    public void setDis(String dis) {
        this.dis = dis == null ? "" : dis;
    }

    public String getHeadimg() {
        return headimg == null ? "" : headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? "" : headimg;
    }

    public String getService() {
        return service == null ? "" : service;
    }

    public void setService(String service) {
        this.service = service == null ? "" : service;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getLongitude() {
        return longitude == null ? "" : longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? "" : longitude;
    }

    public String getLatitude() {
        return latitude == null ? "" : latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? "" : latitude;
    }

    public String getPlace() {
        return place == null ? "" : place;
    }

    public void setPlace(String place) {
        this.place = place == null ? "" : place;
    }
}
