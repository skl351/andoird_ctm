package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/5.
 */
public class mlist_pay implements Serializable{
    private String project;
    private String quantity;
    private String price;

    public String getProject() {
        return project == null ? "" : project;
    }

    public void setProject(String project) {
        this.project = project == null ? "" : project;
    }

    public String getQuantity() {
        return quantity == null ? "" : quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity == null ? "" : quantity;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price == null ? "" : price;
    }

    @Override
    public String toString() {
        return "mlist_pay{" +
                "project='" + project + '\'' +
                ", quantity='" + quantity + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
