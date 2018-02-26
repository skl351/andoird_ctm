package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/7.
 */

public class item_position_tc implements Serializable {
    private String areaid;
    private String code;
    private String cornertitle;
    private String simple_img;
    private String title;
    private String price;
    private String id;
    private String type_id;

    public String getType_id() {
        return type_id == null ? "" : type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id == null ? "" : type_id;
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

    public String getAreaid() {
        return areaid == null ? "" : areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid == null ? "" : areaid;
    }

    public String getCode() {
        return code == null ? "" : code;
    }

    public void setCode(String code) {
        this.code = code == null ? "" : code;
    }

    public String getCornertitle() {
        return cornertitle == null ? "" : cornertitle;
    }

    public void setCornertitle(String cornertitle) {
        this.cornertitle = cornertitle == null ? "" : cornertitle;
    }

    public String getSimple_img() {
        return simple_img == null ? "" : simple_img;
    }

    public void setSimple_img(String simple_img) {
        this.simple_img = simple_img == null ? "" : simple_img;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    @Override
    public String toString() {
        return "item_position_tc{" +
                "areaid='" + areaid + '\'' +
                ", code='" + code + '\'' +
                ", cornertitle='" + cornertitle + '\'' +
                ", simple_img='" + simple_img + '\'' +
                ", title='" + title + '\'' +
                '}';
    }
}
