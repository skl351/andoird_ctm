package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/31.
 */
public class daily_net_info implements Serializable {
    private String size;
    private String price;
    private String dur;
    private String id;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getSize() {
        return size == null ? "" : size;
    }

    public void setSize(String size) {
        this.size = size == null ? "" : size;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price == null ? "" : price;
    }

    public String getDur() {
        return dur == null ? "" : dur;
    }

    public void setDur(String dur) {
        this.dur = dur == null ? "" : dur;
    }

    @Override
    public String toString() {
        return "daily_net_info{" +
                "size='" + size + '\'' +
                ", price='" + price + '\'' +
                ", dur='" + dur + '\'' +
                '}';
    }
}
