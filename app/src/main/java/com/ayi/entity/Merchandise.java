package com.ayi.entity;

/**
 * Created by oceanzhang on 16/3/29.
 */
public class Merchandise {
    private String spec;

    private String id;

    private String area_id;

    private String detail;

    private double price;

    private String big_img;

    private String name;

    private String quality_time;

    private String url;

    private String small_img;

    private int quantity;

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

    public String getArea_id() {
        return area_id == null ? "" : area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id == null ? "" : area_id;
    }

    public String getDetail() {
        return detail == null ? "" : detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? "" : detail;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public String getUrl() {
        return url == null ? "" : url;
    }

    public void setUrl(String url) {
        this.url = url == null ? "" : url;
    }

    public String getSmall_img() {
        return small_img == null ? "" : small_img;
    }

    public void setSmall_img(String small_img) {
        this.small_img = small_img == null ? "" : small_img;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Merchandise{" +
                "spec='" + spec + '\'' +
                ", id='" + id + '\'' +
                ", area_id='" + area_id + '\'' +
                ", detail='" + detail + '\'' +
                ", price=" + price +
                ", big_img='" + big_img + '\'' +
                ", name='" + name + '\'' +
                ", quality_time='" + quality_time + '\'' +
                ", url='" + url + '\'' +
                ", small_img='" + small_img + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
