package com.ayi.wxapi;

import android.content.Context;

import com.ayi.entity.WeixinInfo;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.util.LinkedList;
import java.util.List;

import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.message.BasicNameValuePair;

public class WeiXin {
    private IWXAPI api;

    public WeiXin() {

    }

    public static void createWXAPI(WeixinInfo weixinInfo, Context context) {
        String date = String.valueOf(genTimeStamp());
        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", weixinInfo.getAppid()));
        signParams.add(new BasicNameValuePair("noncestr", weixinInfo.getNonce_str()));
        signParams.add(new BasicNameValuePair("package", "Sign=WXPay"));
        signParams.add(new BasicNameValuePair("partnerid", weixinInfo.getMch_id()));
        signParams.add(new BasicNameValuePair("prepayid", weixinInfo.getPrepay_id()));
        signParams.add(new BasicNameValuePair("timestamp", date));

        IWXAPI localIWXAPI = WXAPIFactory.createWXAPI(context, weixinInfo.getAppid());
        PayReq localPayReq = new PayReq();
        localPayReq.appId = weixinInfo.getAppid();
        localPayReq.partnerId = weixinInfo.getMch_id();
        localPayReq.prepayId = weixinInfo.getPrepay_id();
        localPayReq.nonceStr = weixinInfo.getNonce_str();
        localPayReq.timeStamp = date;
        localPayReq.packageValue = "Sign=WXPay";
        localPayReq.sign = genPackageSign(signParams);
//        localPayReq.sign = weixinInfo.getSign();

        localIWXAPI.registerApp(weixinInfo.getAppid());
        localIWXAPI.sendReq(localPayReq);//这里是发起的源头
    }

    public static String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=0ed42c2a9eb4638eb00dc7e270fc7331");//API秘钥
        String sign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        System.out.println("sign"+sign);
        return sign;
    }

    public static long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }
}
