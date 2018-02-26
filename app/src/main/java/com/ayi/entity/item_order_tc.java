package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/25.
 */

public class item_order_tc implements Serializable{
private String contact_addr;
    private String contact;
    private String content;
    private String contact_phone;
    private String contact_door;
    private String ordernum;
    private String time;
    private String id;
    private String status;
    private String title;
    private String payed;
    private String price;
    private int isvalet;

    public int getIsvalet() {
        return isvalet;
    }

    public void setIsvalet(int isvalet) {
        this.isvalet = isvalet;
    }

    public String getPrice() {
        return price == null ? "" : price;
    }

    public void setPrice(String price) {
        this.price = price == null ? "" : price;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status == null ? "" : status;
    }

    public String getContact_addr() {
        return contact_addr == null ? "" : contact_addr;
    }

    public void setContact_addr(String contact_addr) {
        this.contact_addr = contact_addr == null ? "" : contact_addr;
    }

    public String getContact() {
        return contact == null ? "" : contact;
    }

    public void setContact(String contact) {
        this.contact = contact == null ? "" : contact;
    }

    public String getContent() {
        return content == null ? "" : content;
    }

    public void setContent(String content) {
        this.content = content == null ? "" : content;
    }

    public String getContact_phone() {
        return contact_phone == null ? "" : contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone == null ? "" : contact_phone;
    }

    public String getContact_door() {
        return contact_door == null ? "" : contact_door;
    }

    public void setContact_door(String contact_door) {
        this.contact_door = contact_door == null ? "" : contact_door;
    }

    public String getOrdernum() {
        return ordernum == null ? "" : ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum == null ? "" : ordernum;
    }

    public String getTime() {
        return time == null ? "" : time;
    }

    public void setTime(String time) {
        this.time = time == null ? "" : time;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title == null ? "" : title;
    }

    public String getPayed() {
        return payed == null ? "" : payed;
    }

    public void setPayed(String payed) {
        this.payed = payed == null ? "" : payed;
    }

    @Override
    public String toString() {
        return "item_order_tc{" +
                "contact_addr='" + contact_addr + '\'' +
                ", contact='" + contact + '\'' +
                ", content='" + content + '\'' +
                ", contact_phone='" + contact_phone + '\'' +
                ", contact_door='" + contact_door + '\'' +
                ", ordernum='" + ordernum + '\'' +
                ", time='" + time + '\'' +
                ", id='" + id + '\'' +
                ", status='" + status + '\'' +
                ", title='" + title + '\'' +
                ", payed='" + payed + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
