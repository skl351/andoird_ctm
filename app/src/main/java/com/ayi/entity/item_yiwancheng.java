package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/30.
 */
public class item_yiwancheng implements Serializable {

    private String total_money;
    private String suishoudai_money;
    private String service_content;
    private String service_time1;
    private String service_time2;
    private String get_time;
    private String orderid;
    private String status;
    private String cleaner_name;
    private String cleaner_headimg ;
    private String trialorder;
    private String service_type_id;
    private String user_name;
    private String place;

    public String getTotal_money() {
        return total_money == null ? "" : total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money == null ? "" : total_money;
    }

    public String getSuishoudai_money() {
        return suishoudai_money == null ? "" : suishoudai_money;
    }

    public void setSuishoudai_money(String suishoudai_money) {
        this.suishoudai_money = suishoudai_money == null ? "" : suishoudai_money;
    }

    public String getService_content() {
        return service_content == null ? "" : service_content;
    }

    public void setService_content(String service_content) {
        this.service_content = service_content == null ? "" : service_content;
    }

    public String getService_time1() {
        return service_time1 == null ? "" : service_time1;
    }

    public void setService_time1(String service_time1) {
        this.service_time1 = service_time1 == null ? "" : service_time1;
    }

    public String getService_time2() {
        return service_time2 == null ? "" : service_time2;
    }

    public void setService_time2(String service_time2) {
        this.service_time2 = service_time2 == null ? "" : service_time2;
    }

    public String getGet_time() {
        return get_time == null ? "" : get_time;
    }

    public void setGet_time(String get_time) {
        this.get_time = get_time == null ? "" : get_time;
    }

    public String getOrderid() {
        return orderid == null ? "" : orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? "" : orderid;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status == null ? "" : status;
    }

    public String getCleaner_name() {
        return cleaner_name == null ? "" : cleaner_name;
    }

    public void setCleaner_name(String cleaner_name) {
        this.cleaner_name = cleaner_name == null ? "" : cleaner_name;
    }

    public String getCleaner_headimg() {
        return cleaner_headimg == null ? "" : cleaner_headimg;
    }

    public void setCleaner_headimg(String cleaner_headimg) {
        this.cleaner_headimg = cleaner_headimg == null ? "" : cleaner_headimg;
    }

    public String getTrialorder() {
        return trialorder == null ? "" : trialorder;
    }

    public void setTrialorder(String trialorder) {
        this.trialorder = trialorder == null ? "" : trialorder;
    }

    public String getService_type_id() {
        return service_type_id == null ? "" : service_type_id;
    }

    public void setService_type_id(String service_type_id) {
        this.service_type_id = service_type_id == null ? "" : service_type_id;
    }

    public String getUser_name() {
        return user_name == null ? "" : user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name == null ? "" : user_name;
    }

    public String getPlace() {
        return place == null ? "" : place;
    }

    public void setPlace(String place) {
        this.place = place == null ? "" : place;
    }

    @Override
    public String toString() {
        return "item_yiwancheng{" +
                "total_money='" + total_money + '\'' +
                ", suishoudai_money='" + suishoudai_money + '\'' +
                ", service_content='" + service_content + '\'' +
                ", service_time1='" + service_time1 + '\'' +
                ", service_time2='" + service_time2 + '\'' +
                ", get_time='" + get_time + '\'' +
                ", orderid='" + orderid + '\'' +
                ", status='" + status + '\'' +
                ", cleaner_name='" + cleaner_name + '\'' +
                ", cleaner_headimg='" + cleaner_headimg + '\'' +
                ", trialorder='" + trialorder + '\'' +
                ", service_type_id='" + service_type_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", place='" + place + '\'' +
                '}';
    }
}
