package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/25.
 */

public class item_combo implements Serializable{
    private String areaid;
    private String content_img;
    private String simple_img;
    private String price;
    private String title;
    private String time;
    private String isimg;
    private String content_txt;
    private String ccsp_id;
    private String cornertitle;
    private String type_id;

    public String getType_id() {
        return type_id == null ? "" : type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id == null ? "" : type_id;
    }

    public String getCornertitle() {
        return cornertitle == null ? "" : cornertitle;
    }

    public void setCornertitle(String cornertitle) {
        this.cornertitle = cornertitle == null ? "" : cornertitle;
    }

    public String getCcsp_id() {
        return ccsp_id == null ? "" : ccsp_id;
    }

    public void setCcsp_id(String ccsp_id) {
        this.ccsp_id = ccsp_id == null ? "" : ccsp_id;
    }

    public String getIsimg() {
        return isimg == null ? "" : isimg;
    }

    public void setIsimg(String isimg) {
        this.isimg = isimg == null ? "" : isimg;
    }

    public String getContent_txt() {
        return content_txt == null ? "" : content_txt;
    }

    public void setContent_txt(String content_txt) {
        this.content_txt = content_txt == null ? "" : content_txt;
    }

    public String getAreaid() {
        return areaid == null ? "" : areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid == null ? "" : areaid;
    }

    public String getContent_img() {
        return content_img == null ? "" : content_img;
    }

    public void setContent_img(String content_img) {
        this.content_img = content_img == null ? "" : content_img;
    }

    public String getSimple_img() {
        return simple_img == null ? "" : simple_img;
    }

    public void setSimple_img(String simple_img) {
        this.simple_img = simple_img == null ? "" : simple_img;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price == null ? "" : price;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time == null ? "" : time;
    }

    @Override
    public String toString() {
        return "item_combo{" +
                "areaid='" + areaid + '\'' +
                ", content_img='" + content_img + '\'' +
                ", simple_img='" + simple_img + '\'' +
                ", price='" + price + '\'' +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", isimg='" + isimg + '\'' +
                ", content_txt='" + content_txt + '\'' +
                ", ccsp_id='" + ccsp_id + '\'' +
                '}';
    }
}
