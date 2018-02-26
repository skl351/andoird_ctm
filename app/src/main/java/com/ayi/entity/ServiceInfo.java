package com.ayi.entity;

/**
 * Created by oceanzhang on 16/3/24.
 */
public class ServiceInfo {
    private Price[] price;

    private Merchandise[] merchandise;

    public SPPrice[] spPrice; //期望价格

    public Period[] period;

    public SPPrice[] getSpPrice() {
        return spPrice;
    }

    public void setSpPrice(SPPrice[] spPrice) {
        this.spPrice = spPrice;
    }

    public Period[] getPeriod() {
        return period;
    }

    public void setPeriod(Period[] period) {
        this.period = period;
    }

    public Price[] getPrice() {
        return price;
    }

    public void setPrice(Price[] price) {
        this.price = price;
    }

    public Merchandise[] getMerchandise() {
        return merchandise;
    }

    public void setMerchandise(Merchandise[] merchandise) {
        this.merchandise = merchandise;
    }

    @Override
    public String toString() {
        return "ClassPojo [price = " + price + ", merchandise = " + merchandise + "]";
    }


    public static class Price {
        private String service_time;

        private String id;

        private String area_id;

        private String num;

        private double price;

        private double dur;

        private String seq;

        private String st_id;

        private String size;

        public String getService_time() {
            return service_time;
        }

        public void setService_time(String service_time) {
            this.service_time = service_time;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getDur() {
            return dur;
        }

        public void setDur(double dur) {
            this.dur = dur;
        }

        public String getSeq() {
            return seq;
        }

        public void setSeq(String seq) {
            this.seq = seq;
        }

        public String getSt_id() {
            return st_id;
        }

        public void setSt_id(String st_id) {
            this.st_id = st_id;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        @Override
        public String toString() {
            return "ClassPojo [service_time = " + service_time + ", id = " + id + ", area_id = " + area_id + ", num = " + num + ", price = " + price + ", dur = " + dur + ", seq = " + seq + ", st_id = " + st_id + ", size = " + size + "]";
        }
    }

    public static class SPPrice{
        private String id;

        private String st_id;

        private String sp_price_from;

        private String sp_price_to;

        private String area_id;

        private String seq;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSt_id() {
            return st_id;
        }

        public void setSt_id(String st_id) {
            this.st_id = st_id;
        }

        public String getSp_price_from() {
            return sp_price_from;
        }

        public void setSp_price_from(String sp_price_from) {
            this.sp_price_from = sp_price_from;
        }

        public String getSp_price_to() {
            return sp_price_to;
        }

        public void setSp_price_to(String sp_price_to) {
            this.sp_price_to = sp_price_to;
        }

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getSeq() {
            return seq;
        }

        public void setSeq(String seq) {
            this.seq = seq;
        }
    }
    //时间段
    public static class Period{
        private String id;

        private String st_id;

        private String num;

        private String area_id;

        private String seq;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSt_id() {
            return st_id;
        }

        public void setSt_id(String st_id) {
            this.st_id = st_id;
        }

        public String getNum() {
            return num;
        }

        public void setNum(String num) {
            this.num = num;
        }

        public String getArea_id() {
            return area_id;
        }

        public void setArea_id(String area_id) {
            this.area_id = area_id;
        }

        public String getSeq() {
            return seq;
        }

        public void setSeq(String seq) {
            this.seq = seq;
        }
    }
}
