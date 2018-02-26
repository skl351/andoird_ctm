package com.ayi.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class item_quanbu implements Serializable {
    private String policynum_aunts;
    private String policynum_customer;
    private String areaid;
    private String parentoid;
    private String status3;
    private String ordernum;
    private String total_money;
    private String suishoudai_money;
    private String service_content;
    private String service_time1;
    private String service_time2;
    private String get_time;
    private String orderid;
    private String status;
    private String payed;
    private String user_name;
    private String place;
    private String cleaner_name;
    private String cleaner_headimg ;
    private String trialorder;
   private String Status2;
    private String service_type_id;
    private String ayi_id;
    private String phone;
    private int isvalet;
    List<mlist_pay> list_item;

    public String getPolicynum_aunts() {
        return policynum_aunts == null ? "" : policynum_aunts;
    }

    public void setPolicynum_aunts(String policynum_aunts) {
        this.policynum_aunts = policynum_aunts == null ? "" : policynum_aunts;
    }

    public String getPolicynum_customer() {
        return policynum_customer == null ? "" : policynum_customer;
    }

    public void setPolicynum_customer(String policynum_customer) {
        this.policynum_customer = policynum_customer == null ? "" : policynum_customer;
    }

    public int getIsvalet() {
        return isvalet;
    }

    public void setIsvalet(int isvalet) {
        this.isvalet = isvalet;
    }

    public String getAreaid() {
        return areaid == null ? "" : areaid;
    }

    public void setAreaid(String areaid) {
        this.areaid = areaid == null ? "" : areaid;
    }

    public String getParentoid() {
        return parentoid == null ? "" : parentoid;
    }

    public void setParentoid(String parentoid) {
        this.parentoid = parentoid == null ? "" : parentoid;
    }

    public String getStatus3() {
        return status3 == null ? "" : status3;
    }

    public void setStatus3(String status3) {
        this.status3 = status3 == null ? "" : status3;
    }

    public String getOrdernum() {
        return ordernum == null ? "" : ordernum;
    }

    public void setOrdernum(String ordernum) {
        this.ordernum = ordernum == null ? "" : ordernum;
    }

    public String getTotal_money() {
        return total_money == null ? "" : total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money == null ? "" : total_money;
    }

    public String getSuishoudai_money() {
        return suishoudai_money == null ? "" : suishoudai_money;
    }

    public void setSuishoudai_money(String suishoudai_money) {
        this.suishoudai_money = suishoudai_money == null ? "" : suishoudai_money;
    }

    public String getService_content() {
        return service_content == null ? "" : service_content;
    }

    public void setService_content(String service_content) {
        this.service_content = service_content == null ? "" : service_content;
    }

    public String getService_time1() {
        return service_time1 == null ? "" : service_time1;
    }

    public void setService_time1(String service_time1) {
        this.service_time1 = service_time1 == null ? "" : service_time1;
    }

    public String getService_time2() {
        return service_time2 == null ? "" : service_time2;
    }

    public void setService_time2(String service_time2) {
        this.service_time2 = service_time2 == null ? "" : service_time2;
    }

    public String getGet_time() {
        return get_time == null ? "" : get_time;
    }

    public void setGet_time(String get_time) {
        this.get_time = get_time == null ? "" : get_time;
    }

    public String getOrderid() {
        return orderid == null ? "" : orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? "" : orderid;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status == null ? "" : status;
    }

    public String getPayed() {
        return payed == null ? "" : payed;
    }

    public void setPayed(String payed) {
        this.payed = payed == null ? "" : payed;
    }

    public String getUser_name() {
        return user_name == null ? "" : user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name == null ? "" : user_name;
    }

    public String getPlace() {
        return place == null ? "" : place;
    }

    public void setPlace(String place) {
        this.place = place == null ? "" : place;
    }

    public String getCleaner_name() {
        return cleaner_name == null ? "" : cleaner_name;
    }

    public void setCleaner_name(String cleaner_name) {
        this.cleaner_name = cleaner_name == null ? "" : cleaner_name;
    }

    public String getCleaner_headimg() {
        return cleaner_headimg == null ? "" : cleaner_headimg;
    }

    public void setCleaner_headimg(String cleaner_headimg) {
        this.cleaner_headimg = cleaner_headimg == null ? "" : cleaner_headimg;
    }

    public String getTrialorder() {
        return trialorder == null ? "" : trialorder;
    }

    public void setTrialorder(String trialorder) {
        this.trialorder = trialorder == null ? "" : trialorder;
    }

    public String getStatus2() {
        return Status2 == null ? "" : Status2;
    }

    public void setStatus2(String status2) {
        Status2 = status2 == null ? "" : status2;
    }

    public String getService_type_id() {
        return service_type_id == null ? "" : service_type_id;
    }

    public void setService_type_id(String service_type_id) {
        this.service_type_id = service_type_id == null ? "" : service_type_id;
    }

    public String getAyi_id() {
        return ayi_id == null ? "" : ayi_id;
    }

    public void setAyi_id(String ayi_id) {
        this.ayi_id = ayi_id == null ? "" : ayi_id;
    }

    public String getPhone() {
        return phone == null ? "" : phone;
    }

    public void setPhone(String phone) {
        this.phone = phone == null ? "" : phone;
    }

    public List<mlist_pay> getList_item() {
        return list_item;
    }

    public void setList_item(List<mlist_pay> list_item) {
        this.list_item = list_item;
    }

    @Override
    public String toString() {
        return "item_quanbu{" +
                "policynum_aunts='" + policynum_aunts + '\'' +
                ", policynum_customer='" + policynum_customer + '\'' +
                ", areaid='" + areaid + '\'' +
                ", parentoid='" + parentoid + '\'' +
                ", status3='" + status3 + '\'' +
                ", ordernum='" + ordernum + '\'' +
                ", total_money='" + total_money + '\'' +
                ", suishoudai_money='" + suishoudai_money + '\'' +
                ", service_content='" + service_content + '\'' +
                ", service_time1='" + service_time1 + '\'' +
                ", service_time2='" + service_time2 + '\'' +
                ", get_time='" + get_time + '\'' +
                ", orderid='" + orderid + '\'' +
                ", status='" + status + '\'' +
                ", payed='" + payed + '\'' +
                ", user_name='" + user_name + '\'' +
                ", place='" + place + '\'' +
                ", cleaner_name='" + cleaner_name + '\'' +
                ", cleaner_headimg='" + cleaner_headimg + '\'' +
                ", trialorder='" + trialorder + '\'' +
                ", Status2='" + Status2 + '\'' +
                ", service_type_id='" + service_type_id + '\'' +
                ", ayi_id='" + ayi_id + '\'' +
                ", phone='" + phone + '\'' +
                ", isvalet=" + isvalet +
                ", list_item=" + list_item +
                '}';
    }
}
