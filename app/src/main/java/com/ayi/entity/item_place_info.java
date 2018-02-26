package com.ayi.entity;

/**
 * Created by Administrator on 2016/9/7.
 */
public class item_place_info {
    private String id;
    private String name;
    private String phone;
    private String place;
    private String num_place;
    private String latitude;
    private String longitide;
    private String shiji_dizhi;
    private String door;
    private String areaname;
    private boolean flag_city;
    private boolean flag=false;

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

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? "" : phone;
    }

    public String getPlace() {
        return place == null ? "" : place;
    }

    public void setPlace(String place) {
        this.place = place == null ? "" : place;
    }

    public String getNum_place() {
        return num_place == null ? "" : num_place;
    }

    public void setNum_place(String num_place) {
        this.num_place = num_place == null ? "" : num_place;
    }

    public String getLatitude() {
        return latitude == null ? "" : latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? "" : latitude;
    }

    public String getLongitide() {
        return longitide == null ? "" : longitide;
    }

    public void setLongitide(String longitide) {
        this.longitide = longitide == null ? "" : longitide;
    }

    public String getShiji_dizhi() {
        return shiji_dizhi == null ? "" : shiji_dizhi;
    }

    public void setShiji_dizhi(String shiji_dizhi) {
        this.shiji_dizhi = shiji_dizhi == null ? "" : shiji_dizhi;
    }

    public String getDoor() {
        return door == null ? "" : door;
    }

    public void setDoor(String door) {
        this.door = door == null ? "" : door;
    }

    public String getAreaname() {
        return areaname == null ? "" : areaname;
    }

    public void setAreaname(String areaname) {
        this.areaname = areaname == null ? "" : areaname;
    }

    public boolean isFlag_city() {
        return flag_city;
    }

    public void setFlag_city(boolean flag_city) {
        this.flag_city = flag_city;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }


    @Override
    public String toString() {
        return "item_place_info{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", place='" + place + '\'' +
                ", num_place='" + num_place + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitide='" + longitide + '\'' +
                ", shiji_dizhi='" + shiji_dizhi + '\'' +
                ", door='" + door + '\'' +
                ", areaname='" + areaname + '\'' +
                ", flag_city=" + flag_city +
                ", flag=" + flag +
                '}';
    }
}
