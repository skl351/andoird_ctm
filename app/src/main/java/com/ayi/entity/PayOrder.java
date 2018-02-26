package com.ayi.entity;

import java.util.List;

/**
 * Created by oceanzhang on 16/5/9.
 * 订单支付
 */
public class PayOrder {
    private String orderId;

    private String service_type;

    private String contacts;

    private String contact_phone;

    private String address;

    private double pricetotal;

    private List<Project> mList ;

    private double glod;

    private String timeService;

    public String getOrderId() {
        return orderId == null ? "" : orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? "" : orderId;
    }

    public String getService_type() {
        return service_type == null ? "" : service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type == null ? "" : service_type;
    }

    public String getContacts() {
        return contacts == null ? "" : contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts == null ? "" : contacts;
    }

    public String getContact_phone() {
        return contact_phone == null ? "" : contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone == null ? "" : contact_phone;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address == null ? "" : address;
    }

    public double getPricetotal() {
        return pricetotal;
    }

    public void setPricetotal(double pricetotal) {
        this.pricetotal = pricetotal;
    }

    public List<Project> getmList() {
        return mList;
    }

    public void setmList(List<Project> mList) {
        this.mList = mList;
    }

    public double getGlod() {
        return glod;
    }

    public void setGlod(double glod) {
        this.glod = glod;
    }

    public String getTimeService() {
        return timeService == null ? "" : timeService;
    }

    public void setTimeService(String timeService) {
        this.timeService = timeService == null ? "" : timeService;
    }

    public static class Project{
        public Project(String project, int quantity, double price) {
            this.project = project;
            this.quantity = quantity;
            this.price = price;
        }

        public Project() {
        }

        private String project;

        private int quantity;

        private double price;

        public String getProject() {
            return project;
        }

        public void setProject(String project) {
            this.project = project;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }
    }

}
