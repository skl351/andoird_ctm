package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/12.
 */

public class Home_certem_entiy implements Serializable{
    private String img_url;
    private String title;
    private String type_id;
    private String sortby;
    private String type_code;
    private String index_icon;

    public String getIndex_icon() {
        return index_icon == null ? "" : index_icon;
    }

    public void setIndex_icon(String index_icon) {
        this.index_icon = index_icon == null ? "" : index_icon;
    }

    public String getType_code() {
        return type_code == null ? "" : type_code;
    }

    public void setType_code(String type_code) {
        this.type_code = type_code == null ? "" : type_code;
    }

    public String getSortby() {
        return sortby == null ? "" : sortby;
    }

    public void setSortby(String sortby) {
        this.sortby = sortby == null ? "" : sortby;
    }

    public String getType_id() {
        return type_id == null ? "" : type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id == null ? "" : type_id;
    }

    public String getImg_url() {
        return img_url == null ? "" : img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url == null ? "" : img_url;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    @Override
    public String toString() {
        return "Home_certem_entiy{" +
                "img_url='" + img_url + '\'' +
                ", title='" + title + '\'' +
                ", type_id='" + type_id + '\'' +
                ", sortby='" + sortby + '\'' +
                ", type_code='" + type_code + '\'' +
                ", index_icon='" + index_icon + '\'' +
                '}';
    }
}
