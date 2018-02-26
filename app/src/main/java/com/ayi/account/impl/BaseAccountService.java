package com.ayi.account.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.ayi.account.AccountService;
import com.ayi.entity.UserInfo;


/**
 * Created by oceanzhang on 16/3/10.
 */
public abstract class BaseAccountService implements AccountService {
    protected Context context;
    protected SharedPreferences preferences;

    public BaseAccountService(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences("account",Context.MODE_PRIVATE);
    }

    @Override
    public UserInfo profile() {
        String jsonStr = preferences.getString("profile",null);
        if(jsonStr == null)return null;
        UserInfo userInfo = JSON.parseObject(jsonStr, UserInfo.class);
        return userInfo;
    }

    @Override
    public String id() {
        return preferences.getString("userId","");
    }

    @Override
    public String token() {
        return profile() != null ? profile().getToken() : "";
    }

    @Override
    public String verifyCode() {
        return profile() != null ? profile().getVerifyCode() : "";
    }
    @Override
    public String mobile() {
        return profile() != null ? profile().getMobile() : "";
    }

    @Override
    public void logout() {
        String userId = id();
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("userId").remove("token").remove("profile").apply();
        if(!TextUtils.isEmpty(userId)){
            dispatchAccountChanged();
        }
    }

    @Override
    public void update(UserInfo profile) {
        if(profile == null)return;
        String newUserId = profile.getUser_id();
        if(!newUserId.equals(id())){
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("userId",newUserId).putString("profile",JSON.toJSONString(profile)).apply();
            dispatchAccountChanged();
            return;
        }

        profile.setUser_id(id());
        profile.setToken(token());
        preferences.edit().putString("profile",JSON.toJSONString(profile)).apply();
        dispatchProfileChanged();

    }

    protected abstract void dispatchAccountChanged();

    protected abstract void dispatchProfileChanged();
}
