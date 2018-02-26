package com.ayi.actions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.entity.Result;
import com.ayi.entity.WeixinInfo;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Show_toast;
import com.milk.flux.actions.ActionsCreator;
import com.milk.flux.dispatcher.Dispatcher;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by oceanzhang on 16/3/24.
 */
public class PayActionsCreator extends ActionsCreator {
    public PayActionsCreator(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public void weixinPay(final String body, String totalfee, String goodstag, String source, String user_id, String token, final Context context) {
        RetrofitUtil.getService().weixinPay(body, totalfee, goodstag, source, user_id, token, AyiApplication.m).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<WeixinInfo>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        Show_toast.showText(context,"服务器繁忙，请重试");
//                        dispatcher.dispatch("weixinInfo", "error", true);
                    }
                    @Override
                    public void onNext(Result<WeixinInfo> result) {
                        System.out.println("weix"+"出现在地上");
                        Intent intent = new Intent(RetrofitUtil.APP_BORADCASTRECEIVER2);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("weixinInfo", ((WeixinInfo) result.getData()));
                        intent.putExtras(mBundle);
                        context.sendBroadcast(intent);
                        System.out.println("weix"+"出现在地下");
                    }
                });
    }

    //微信回调
    public void weixinCallback(String out_trade_no, String source, String user_id, String token, final Context context) {
        RetrofitUtil.getService().weixinCallback(out_trade_no, source, user_id, token, AyiApplication.m).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<WeixinInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dispatcher.dispatch("weixinCallback", "error", true);
                    }

                    @Override
                    public void onNext(Result<WeixinInfo> result) {

                        if (result.getRet() == 200) {
                            Intent intent = new Intent(RetrofitUtil.APP_BORADCASTRECEIVER);
                            context.sendBroadcast(intent);
                            ((Activity) context).finish();
                        }
                    }
                });
    }
    //支付宝回调
    public void customerPay(String money, String out_trade_no, String user_id, String token, final Context context) {
        RetrofitUtil.getService().customerPay(money, out_trade_no, user_id, token, AyiApplication.m).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                        dispatcher.dispatch("customerPay", "error", true);
                    }
                    @Override
                    public void onNext(Result result) {
                        if (result.getRet() == 200) {

                            System.out.println("支付宝有没有道理————啊啊");
//                            Intent intent = new Intent(RetrofitUtil.APP_BORADCASTRECEIVER);
//                            context.sendBroadcast(intent);
//                            ((Activity) context).finish();
                        }
                    }
                });
    }

    public void rechargePay(String rechargeid, String user_id, String token, final Context context) {
        RetrofitUtil.getService().rechargePay(rechargeid, user_id, token, AyiApplication.m).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context,"充值失败",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(Result result) {
                        System.out.println(result);
                        if (result.getRet()==200){
                            Toast.makeText(context,"充值成功",Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(RetrofitUtil.APP_BORADCASTRECEIVER);
//                            context.sendBroadcast(intent);
                            ((Activity) context).finish();
                        }else{
                            Toast.makeText(context,result.getMsg(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
