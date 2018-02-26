package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/5.
 */
public class item_coupon implements Serializable {
    private String id;
    private String price;
    private String time_start;
    private String time_finish;
    private String status;
    private String typeids;
    private String place;
    private String typenames;

    public String getTypenames() {
        return typenames == null ? "" : typenames;
    }

    public void setTypenames(String typenames) {
        this.typenames = typenames == null ? "" : typenames;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price == null ? "" : price;
    }

    public String getTime_start() {
        return time_start == null ? "" : time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start == null ? "" : time_start;
    }

    public String getTime_finish() {
        return time_finish == null ? "" : time_finish;
    }

    public void setTime_finish(String time_finish) {
        this.time_finish = time_finish == null ? "" : time_finish;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status == null ? "" : status;
    }

    public String getTypeids() {
        return typeids == null ? "" : typeids;
    }

    public void setTypeids(String typeids) {
        this.typeids = typeids == null ? "" : typeids;
    }

    public String getPlace() {
        return place == null ? "" : place;
    }

    public void setPlace(String place) {
        this.place = place == null ? "" : place;
    }
}
