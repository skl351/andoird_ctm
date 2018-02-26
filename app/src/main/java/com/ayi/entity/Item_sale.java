package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/12/30.
 */

public class Item_sale implements Serializable {
    private String title;
    private String remark;
    private String data_end;
    private String data_start;
    private String sale;

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? "" : remark;
    }

    public String getData_end() {
        return data_end == null ? "" : data_end;
    }

    public void setData_end(String data_end) {
        this.data_end = data_end == null ? "" : data_end;
    }

    public String getData_start() {
        return data_start == null ? "" : data_start;
    }

    public void setData_start(String data_start) {
        this.data_start = data_start == null ? "" : data_start;
    }

    public String getSale() {
        return sale == null ? "" : sale;
    }

    public void setSale(String sale) {
        this.sale = sale == null ? "" : sale;
    }
}
