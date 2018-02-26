package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/18.
 */
public class suishoudai_guodu  implements Serializable{
private String name;
    private String num;
    private String id;

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public String getNum() {
        return num == null ? "" : num;
    }

    public void setNum(String num) {
        this.num = num == null ? "" : num;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    @Override
    public String toString() {
        return "suishoudai_guodu{" +
                "name='" + name + '\'' +
                ", num='" + num + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
