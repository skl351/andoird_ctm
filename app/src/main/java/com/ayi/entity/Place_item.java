package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/6.
 */
public class Place_item implements Serializable {
    private String id;
    private String area_id;
    private String area_name;
    private String capitalize;
    private String open_time;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getArea_id() {
        return area_id == null ? "" : area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id == null ? "" : area_id;
    }

    public String getArea_name() {
        return area_name == null ? "" : area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name == null ? "" : area_name;
    }

    public String getCapitalize() {
        return capitalize == null ? "" : capitalize;
    }

    public void setCapitalize(String capitalize) {
        this.capitalize = capitalize == null ? "" : capitalize;
    }

    public String getOpen_time() {
        return open_time == null ? "" : open_time;
    }

    public void setOpen_time(String open_time) {
        this.open_time = open_time == null ? "" : open_time;
    }

    @Override
    public String toString() {
        return "Place_item{" +
                "id='" + id + '\'' +
                ", area_id='" + area_id + '\'' +
                ", area_name='" + area_name + '\'' +
                ", capitalize='" + capitalize + '\'' +
                ", open_time='" + open_time + '\'' +
                '}';
    }
}
