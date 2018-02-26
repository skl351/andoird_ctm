package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/17.
 */
public class item_suishoudai implements Serializable {
    private String spec ;
    private String id ;
    private String detail ;
    private String price ;
    private String flagtext ;
    private String big_img ;
    private String name ;
    private String quality_time ;
    private String small_img ;
    private String num="0";

    public String getSpec() {
        return spec == null ? "" : spec;
    }

    public void setSpec(String spec) {
        this.spec = spec == null ? "" : spec;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getDetail() {
        return detail == null ? "" : detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? "" : detail;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price == null ? "" : price;
    }

    public String getFlagtext() {
        return flagtext == null ? "" : flagtext;
    }

    public void setFlagtext(String flagtext) {
        this.flagtext = flagtext == null ? "" : flagtext;
    }

    public String getBig_img() {
        return big_img == null ? "" : big_img;
    }

    public void setBig_img(String big_img) {
        this.big_img = big_img == null ? "" : big_img;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getQuality_time() {
        return quality_time == null ? "" : quality_time;
    }

    public void setQuality_time(String quality_time) {
        this.quality_time = quality_time == null ? "" : quality_time;
    }

    public String getSmall_img() {
        return small_img == null ? "" : small_img;
    }

    public void setSmall_img(String small_img) {
        this.small_img = small_img == null ? "" : small_img;
    }

    public String getNum() {
        return num == null ? "" : num;
    }

    public void setNum(String num) {
        this.num = num == null ? "" : num;
    }

    @Override
    public String toString() {
        return "item_suishoudai{" +
                "spec='" + spec + '\'' +
                ", id='" + id + '\'' +
                ", detail='" + detail + '\'' +
                ", price='" + price + '\'' +
                ", flagtext='" + flagtext + '\'' +
                ", big_img='" + big_img + '\'' +
                ", name='" + name + '\'' +
                ", quality_time='" + quality_time + '\'' +
                ", small_img='" + small_img + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
