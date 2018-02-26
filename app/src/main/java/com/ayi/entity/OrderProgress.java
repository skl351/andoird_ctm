package com.ayi.entity;

/**
 * Created by oceanzhang on 16/3/26.
 * 订单进度
 */
public class OrderProgress {
    private String id;

    private String oid;

    private int status;

    private long timestamp;

    private String remark;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getOid() {
        return oid == null ? "" : oid;
    }

    public void setOid(String oid) {
        this.oid = oid == null ? "" : oid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getRemark() {
        return remark == null ? "" : remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? "" : remark;
    }

    @Override
    public String toString() {
        return "OrderProgress{" +
                "id='" + id + '\'' +
                ", oid='" + oid + '\'' +
                ", status=" + status +
                ", timestamp=" + timestamp +
                ", remark='" + remark + '\'' +
                '}';
    }
}
