package com.ayi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/9/1.
 */
public class cancel_reason implements Serializable {
    private String id;
    private String reason;
    private String group;

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id == null ? "" : id;
    }

    public String getReason() {
        return reason == null ? "" : reason;
    }

    public void setReason(String reason) {
        this.reason = reason == null ? "" : reason;
    }

    public String getGroup() {
        return group == null ? "" : group;
    }

    public void setGroup(String group) {
        this.group = group == null ? "" : group;
    }

    @Override
    public String toString() {
        return "cancel_reason{" +
                "id='" + id + '\'' +
                ", reason='" + reason + '\'' +
                ", group='" + group + '\'' +
                '}';
    }
}
