package com.ayi.entity;

import java.util.Arrays;
import java.util.List;

/**
 * Created by oceanzhang on 16/3/25.
 */
public class MyOrder {

    private UserInfo cleanerInfo;

    private String workTime;

    private String day_fre;

    private Product[] detail;

    private int status;

    private String service_type;

    private String contact_phone;

    private String time_start;

    private String time_dur;

    private int eService;

    private String contacts;

    private int service_type_id;

    private long timestamp;

    private double mtotal;

    private String id;

    private String service_time;

    private String day_start;

    private double pricetotal;

    private String day_finish;

    private String contact_address;

    private String time_finish;

    private ServiceShow serviceShow;
    private double price;

    private int payment;

    private int payed;

    private int evaluate;

    private String oid;

    private String ordernum;

    private int gold_num;

    private List<OrderProgress> progress;

    public UserInfo getCleanerInfo() {
        return cleanerInfo;
    }

    public void setCleanerInfo(UserInfo cleanerInfo) {
        this.cleanerInfo = cleanerInfo;
    }

    public String getWorkTime() {
        return workTime == null ? "" : workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime == null ? "" : workTime;
    }

    public String getDay_fre() {
        return day_fre == null ? "" : day_fre;
    }

    public void setDay_fre(String day_fre) {
        this.day_fre = day_fre == null ? "" : day_fre;
    }

    public Product[] getDetail() {
        return detail;
    }

    public void setDetail(Product[] detail) {
        this.detail = detail;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getService_type() {
        return service_type == null ? "" : service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type == null ? "" : service_type;
    }

    public String getContact_phone() {
        return contact_phone == null ? "" : contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone == null ? "" : contact_phone;
    }

    public String getTime_start() {
        return time_start == null ? "" : time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start == null ? "" : time_start;
    }

    public String getTime_dur() {
        return time_dur == null ? "" : time_dur;
    }

    public void setTime_dur(String time_dur) {
        this.time_dur = time_dur == null ? "" : time_dur;
    }

    public int geteService() {
        return eService;
    }

    public void seteService(int eService) {
        this.eService = eService;
    }

    public String getContacts() {
        return contacts == null ? "" : contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts == null ? "" : contacts;
    }

    public int getService_type_id() {
        return service_type_id;
    }

    public void setService_type_id(int service_type_id) {
        this.service_type_id = service_type_id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getMtotal() {
        return mtotal;
    }

    public void setMtotal(double mtotal) {
        this.mtotal = mtotal;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getService_time() {
        return service_time == null ? "" : service_time;
    }

    public void setService_time(String service_time) {
        this.service_time = service_time == null ? "" : service_time;
    }

    public String getDay_start() {
        return day_start == null ? "" : day_start;
    }

    public void setDay_start(String day_start) {
        this.day_start = day_start == null ? "" : day_start;
    }

    public double getPricetotal() {
        return pricetotal;
    }

    public void setPricetotal(double pricetotal) {
        this.pricetotal = pricetotal;
    }

    public String getDay_finish() {
        return day_finish == null ? "" : day_finish;
    }

    public void setDay_finish(String day_finish) {
        this.day_finish = day_finish == null ? "" : day_finish;
    }

    public String getContact_address() {
        return contact_address == null ? "" : contact_address;
    }

    public void setContact_address(String contact_address) {
        this.contact_address = contact_address == null ? "" : contact_address;
    }

    public String getTime_finish() {
        return time_finish == null ? "" : time_finish;
    }

    public void setTime_finish(String time_finish) {
        this.time_finish = time_finish == null ? "" : time_finish;
    }

    public ServiceShow getServiceShow() {
        return serviceShow;
    }

    public void setServiceShow(ServiceShow serviceShow) {
        this.serviceShow = serviceShow;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPayment() {
        return payment;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getPayed() {
        return payed;
    }

    public void setPayed(int payed) {
        this.payed = payed;
    }

    public int getEvaluate() {
        return evaluate;
    }

    public void setEvaluate(int evaluate) {
        this.evaluate = evaluate;
    }

    public String getOid() {
        return oid == null ? "" : oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? "" : oid;
    }

    public String getOrdernum() {
        return ordernum == null ? "" : ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum == null ? "" : ordernum;
    }

    public int getGold_num() {
        return gold_num;
    }

    public void setGold_num(int gold_num) {
        this.gold_num = gold_num;
    }

    public List<OrderProgress> getProgress() {
        return progress;
    }

    public void setProgress(List<OrderProgress> progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "MyOrder{" +
                "cleanerInfo=" + cleanerInfo +
                ", workTime='" + workTime + '\'' +
                ", day_fre='" + day_fre + '\'' +
                ", detail=" + Arrays.toString(detail) +
                ", status=" + status +
                ", service_type='" + service_type + '\'' +
                ", contact_phone='" + contact_phone + '\'' +
                ", time_start='" + time_start + '\'' +
                ", time_dur='" + time_dur + '\'' +
                ", eService=" + eService +
                ", contacts='" + contacts + '\'' +
                ", service_type_id=" + service_type_id +
                ", timestamp=" + timestamp +
                ", mtotal=" + mtotal +
                ", id='" + id + '\'' +
                ", service_time='" + service_time + '\'' +
                ", day_start='" + day_start + '\'' +
                ", pricetotal=" + pricetotal +
                ", day_finish='" + day_finish + '\'' +
                ", contact_address='" + contact_address + '\'' +
                ", time_finish='" + time_finish + '\'' +
                ", serviceShow=" + serviceShow +
                ", price=" + price +
                ", payment=" + payment +
                ", payed=" + payed +
                ", evaluate=" + evaluate +
                ", oid='" + oid + '\'' +
                ", ordernum='" + ordernum + '\'' +
                ", gold_num=" + gold_num +
                ", progress=" + progress +
                '}';
    }
}
