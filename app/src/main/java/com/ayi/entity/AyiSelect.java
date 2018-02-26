package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by oceanzhang on 16/3/29.
 */
public class AyiSelect implements Serializable{
    private String id;

    private String name;

    private String headimg;

    private String sex;

    private String birthday;

    private String place;

    private String service_type;

    private String latitude;

    private String longitude;

    private double dis;

    private int old;

    private String times;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getHeadimg() {
        return headimg == null ? "" : headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg == null ? "" : headimg;
    }

    public String getSex() {
        return sex == null ? "" : sex;
    }

    public void setSex(String sex) {
        this.sex = sex == null ? "" : sex;
    }

    public String getBirthday() {
        return birthday == null ? "" : birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday == null ? "" : birthday;
    }

    public String getPlace() {
        return place == null ? "" : place;
    }

    public void setPlace(String place) {
        this.place = place == null ? "" : place;
    }

    public String getService_type() {
        return service_type == null ? "" : service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type == null ? "" : service_type;
    }

    public String getLatitude() {
        return latitude == null ? "" : latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? "" : latitude;
    }

    public String getLongitude() {
        return longitude == null ? "" : longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? "" : longitude;
    }

    public double getDis() {
        return dis;
    }

    public void setDis(double dis) {
        this.dis = dis;
    }

    public int getOld() {
        return old;
    }

    public void setOld(int old) {
        this.old = old;
    }

    public String getTimes() {
        return times == null ? "" : times;
    }

    public void setTimes(String times) {
        this.times = times == null ? "" : times;
    }

    @Override
    public String toString() {
        return "AyiSelect{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", headimg='" + headimg + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", place='" + place + '\'' +
                ", service_type='" + service_type + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", dis=" + dis +
                ", old=" + old +
                ", times='" + times + '\'' +
                '}';
    }
}
