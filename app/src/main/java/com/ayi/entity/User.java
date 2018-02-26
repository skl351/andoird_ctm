package com.ayi.entity;

/**
 * Created by oceanzhang on 16/3/24.
 */
public class User {
    private String user_id;
    private int is_new;
    private String token;
    private String status;

    public String getUser_id() {
        return user_id == null ? "" : user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id == null ? "" : user_id;
    }

    public int getIs_new() {
        return is_new;
    }

    public void setIs_new(int is_new) {
        this.is_new = is_new;
    }

    public String getToken() {
        return token == null ? "" : token;
    }

    public void setToken(String token) {
        this.token = token == null ? "" : token;
    }

    public String getStatus() {
        return status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status == null ? "" : status;
    }
}
