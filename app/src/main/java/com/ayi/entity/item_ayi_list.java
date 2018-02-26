package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/2.
 */
public class item_ayi_list implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;
    private String id;
    private String name;
    private String img_head;
    private String sex;
    private String birthday;
    private String place;
    private String service_type;
    private String latitude;
    private String longitude;
    private String dis;
    private String times;
    private String old;
    //下三个只有在做保姆和月嫂的时候会出现
    private String in_price;
    private String out_price;
    private String servicecharge;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

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

    public String getImg_head() {
        return img_head == null ? "" : img_head;
    }

    public void setImg_head(String img_head) {
        this.img_head = img_head == null ? "" : img_head;
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

    public String getDis() {
        return dis == null ? "" : dis;
    }

    public void setDis(String dis) {
        this.dis = dis == null ? "" : dis;
    }

    public String getTimes() {
        return times == null ? "" : times;
    }

    public void setTimes(String times) {
        this.times = times == null ? "" : times;
    }

    public String getOld() {
        return old == null ? "" : old;
    }

    public void setOld(String old) {
        this.old = old == null ? "" : old;
    }

    public String getIn_price() {
        return in_price == null ? "" : in_price;
    }

    public void setIn_price(String in_price) {
        this.in_price = in_price == null ? "" : in_price;
    }

    public String getOut_price() {
        return out_price == null ? "" : out_price;
    }

    public void setOut_price(String out_price) {
        this.out_price = out_price == null ? "" : out_price;
    }

    public String getServicecharge() {
        return servicecharge == null ? "" : servicecharge;
    }

    public void setServicecharge(String servicecharge) {
        this.servicecharge = servicecharge == null ? "" : servicecharge;
    }

    @Override
    public String toString() {
        return "item_ayi_list{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", img_head='" + img_head + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", place='" + place + '\'' +
                ", service_type='" + service_type + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", dis='" + dis + '\'' +
                ", times='" + times + '\'' +
                ", old='" + old + '\'' +
                ", in_price='" + in_price + '\'' +
                ", out_price='" + out_price + '\'' +
                ", servicecharge='" + servicecharge + '\'' +
                '}';
    }
}
