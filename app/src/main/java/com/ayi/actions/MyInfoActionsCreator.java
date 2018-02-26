package com.ayi.actions;


import com.ayi.AyiApplication;
import com.ayi.entity.Result;
import com.ayi.entity.UserInfo;
import com.ayi.retrofit.RetrofitUtil;
import com.milk.flux.actions.ActionsCreator;
import com.milk.flux.dispatcher.Dispatcher;
import com.socks.library.KLog;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by oceanzhang on 16/3/24.
 */
public class MyInfoActionsCreator extends ActionsCreator {
    public MyInfoActionsCreator(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public void login(final String phone,final String verifyCode,double lat,double longa,String versionname){
        RetrofitUtil.getService().login(phone, verifyCode,lat,longa,versionname, AyiApplication.m).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Subscriber<Result<UserInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        KLog.e("login:","自动登录失败，尝试人工登录，弹出登录框");
                        AyiApplication.getInstance().accountService().logout();
                        dispatcher.dispatch("loginAction", "error", e.getMessage());
                    }

                    @Override
                    public void onNext(Result<UserInfo> userInfoResult) {
                        KLog.e("userInfoResult",userInfoResult.getData());
                        if (userInfoResult.getRet() == 200) {
                            UserInfo userInfo = userInfoResult.getData();
                            userInfo.setMobile(phone);
                            userInfo.setVerifyCode(verifyCode);
                            AyiApplication.getInstance().accountService().update(userInfo);
                            AyiApplication.getInstance().setCanvalet(userInfoResult.getData().getCanvalet());
                            loadUserInfo(userInfoResult.getData().getUser_id(),phone,verifyCode);

                            return;
                        }else{
                            dispatcher.dispatch("loginAction", "error", userInfoResult.getMsg() );
                        }
                    }
                });
    }

    //导入的是阿姨的信息
    private void loadUserInfo(final String userId,final String phone,final String verifyCode){
        RetrofitUtil.getService().getUserInfo(userId,"", "", AyiApplication.m).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<UserInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dispatcher.dispatch("loginAction", "error", true);
                    }

                    @Override
                    public void onNext(Result<UserInfo> userInfoResult) {
                        if(userInfoResult.getRet() == 200){
                            userInfoResult.getData().setUser_id(userId);
                            userInfoResult.getData().setMobile(phone);
                            userInfoResult.getData().setVerifyCode(verifyCode);
                            AyiApplication.getInstance().accountService().update(userInfoResult.getData());
                            dispatcher.dispatch("loginAction", "data", userInfoResult.getData());
                        }else{
                            dispatcher.dispatch("loginAction", "error", userInfoResult.getMsg());
                        }
                    }
                });
    }

    /**
     * 得到阿姨信息
     * @param userId
     * @param cleanerId
     */
    public void loadAyiProfile(final String userId,String cleanerId){
        RetrofitUtil.getService().getUserInfo(userId,cleanerId, "", AyiApplication.m).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<UserInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dispatcher.dispatch("loadAyiProfile", "error", true);
                    }

                    @Override
                    public void onNext(Result<UserInfo> userInfoResult) {
                        if(userInfoResult.getRet() == 200){
                            dispatcher.dispatch("loadAyiProfile", "data", userInfoResult.getData());
                        }else{
                            dispatcher.dispatch("loadAyiProfile", "error", userInfoResult.getMsg() );
                        }
                    }
                });
    }

//    public void updateUserInfo(final String userId,HashMap<String,String> params){
//        RetrofitUtil.getService().changeUserInfo(params).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Result<JSONObject>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        dispatcher.dispatch("updateUserInfo","error",e.getMessage());
//                    }
//
//                    @Override
//                    public void onNext(Result<JSONObject> result) {
//                        if(result.getRet() == 200 && result.getData().getBoolean("status")){
//                            dispatcher.dispatch("updateUserInfo");
//                            loadUserInfo(userId); //同步本地用户信息
//                            return;
//                        }
//                        onError(new Exception(result.getMsg()));
//                    }
//                });
//    }

    /**
     * 获取短信认证
     * @param phone
     */
    public void getVerifyCode(String phone){
        RetrofitUtil.getService().getVerifyCode(phone, AyiApplication.m).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dispatcher.dispatch("getVerifyCodeFailed","error_message",e.getMessage());
                    }

                    @Override
                    public void onNext(Result userInfoResult) {
                        System.out.println("userInfoResult"+userInfoResult);
                        if(userInfoResult.getRet() == 200){
                            dispatcher.dispatch("getVerifyCodeSuccess");
                            return;
                        }
                        onError(new Exception("error"));
                    }
                });
    }



}
