package com.ayi.stores;

import com.ayi.AyiApplication;
import com.ayi.entity.UserInfo;
import com.milk.flux.annotation.BindAction;
import com.milk.flux.dispatcher.Dispatcher;
import com.milk.flux.stores.Store;

import java.util.HashMap;

/**
 * Created by oceanzhang on 16/3/24.
 */
public class MyInfoStore extends Store {
    public MyInfoStore(Dispatcher dispatcher) {
        super(dispatcher);
    }
    private UserInfo userInfo;
    @BindAction("getVerifyCodeSuccess")
    public void getVerifyCodeSuccess(HashMap<String, Object> data){
        emitStoreChange(new StoreChangeEvent(1, false, "get verifycode success."));
    }
    @BindAction("getVerifyCodeFailed")
    public void getVerifyCodeFailed(HashMap<String, Object> data){
        emitStoreChange(new StoreChangeEvent(1,true,"error get verifyCode"));
    }
    @BindAction("loginAction")
    public void loginAction(HashMap<String,Object> data){
        if(data.get("error") == null){
             userInfo = (UserInfo) data.get("data");
            emitStoreChange(new StoreChangeEvent(2,false,(String)data.get("error")));
            AyiApplication.getInstance().accountService().update(userInfo);
        }else{
            emitStoreChange(new StoreChangeEvent(2,true,(String)data.get("error")));
        }
    }

    @BindAction("updateUserInfo")
    public void updateUserInfo(HashMap<String,Object> data){
        if(data.get("error") == null){
            emitStoreChange(new StoreChangeEvent(4,false,"update userinfo success!"));
        }else{
            emitStoreChange(new StoreChangeEvent(4,true,"update userinfo failed!"));
        }
    }

    @BindAction("loadAyiProfile")
    public void loadAyiProfile(HashMap<String,Object> data){
        if(data.get("error") == null){
            userInfo = (UserInfo) data.get("data");
            emitStoreChange(new StoreChangeEvent(5,false,"login success."));
        }else{
            emitStoreChange(new StoreChangeEvent(5,true,"login failed."));
        }
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

}
