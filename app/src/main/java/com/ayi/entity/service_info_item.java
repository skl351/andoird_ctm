package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/18.
 */
public class service_info_item implements Serializable{
    private String payed;
    private String date;
    private String price;
    private String status;
    private String childordernum;
    private String ordernum;

    public String getPayed() {
        return payed == null ? "" : payed;
    }

    public void setPayed(String payed) {
        this.payed = payed == null ? "" : payed;
    }

    public String getDate() {
        return date == null ? "" : date;
    }

    public void setDate(String date) {
        this.date = date == null ? "" : date;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price == null ? "" : price;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status == null ? "" : status;
    }

    public String getChildordernum() {
        return childordernum == null ? "" : childordernum;
    }

    public void setChildordernum(String childordernum) {
        this.childordernum = childordernum == null ? "" : childordernum;
    }

    public String getOrdernum() {
        return ordernum == null ? "" : ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum == null ? "" : ordernum;
    }

    @Override
    public String toString() {
        return "service_info_item{" +
                "payed='" + payed + '\'' +
                ", date='" + date + '\'' +
                ", price='" + price + '\'' +
                ", status='" + status + '\'' +
                ", childordernum='" + childordernum + '\'' +
                ", ordernum='" + ordernum + '\'' +
                '}';
    }
}
