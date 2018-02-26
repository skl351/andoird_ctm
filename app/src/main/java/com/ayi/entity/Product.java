package com.ayi.entity;

/**
 * Created by oceanzhang on 16/3/25.
 */
public class Product {
    private String id;

    private double price;

    private String name;

    private int quantity;

    private String mid;

    public String getMid() {
        return mid == null ? "" : mid;
    }

    public void setMid(String mid) {
        this.mid = mid == null ? "" : mid;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name == null ? "" : name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", price=" + price +
                ", name='" + name + '\'' +
                ", quantity=" + quantity +
                ", mid='" + mid + '\'' +
                '}';
    }
}
