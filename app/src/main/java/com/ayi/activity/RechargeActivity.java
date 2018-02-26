package com.ayi.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.actions.PayActionsCreator;
import com.ayi.entity.PayResult;
import com.ayi.entity.WeixinInfo;
import com.ayi.home_page.Order_pay;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.stores.MyInfoStore;
import com.ayi.utils.Show_toast;
import com.ayi.wxapi.WeiXin;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.milk.flux.stores.Store;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

/**
 * 支付类别选择
 * Created by oceanzhang on 16/3/24.
 */
public class RechargeActivity extends TempletActivity<MyInfoStore, PayActionsCreator> {

    @Bind(R.id.act_recharge_money)
    EditText recharge_money;
    @Bind(R.id.progressBar1)
    View progressBar1;
    @Bind(R.id.act_recharge_btn_submit)
    Button btn_submit;
    @Bind(R.id.btn_alipay)
    Button btn_alipay;
    @Bind(R.id.btn_weixin)
    Button btn_weixin;
    @Bind(R.id.btn_recharge_card)
    Button btn_recharge_card;
    @Bind(R.id.act_recharge_card)
    EditText act_recharge_card;
    @Bind(R.id.relativeLayout2)
    RelativeLayout relativeLayout2;
    @Bind(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    public int payment = 1;
    public String totalfee = "0";
    public String body = "三个阿姨平台余额充值";
    public String goodstag;
    public String source = "APP";
    public WeixinInfo weixinInfo;
    public String paynumber;
    public String card;
    private static final int SDK_PAY_FLAG = 1;


    public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getAction();

            //微信发起支付请求
            if (str.equals(RetrofitUtil.APP_BORADCASTRECEIVER2)) {
                weixinInfo = (WeixinInfo) intent.getSerializableExtra("weixinInfo");
                WeiXin.createWXAPI(weixinInfo, RechargeActivity.this);
                System.out.println("发起微信1");
                //微信充值回调接口
            } else if (str.equals(RetrofitUtil.APP_BORADCASTRECEIVER4)) {
                System.out.println("充值成功的");
                weix_call_back();
            }
        }

    };

    private void weix_call_back() {
        Intent intent = new Intent(RetrofitUtil.APP_BORADCASTRECEIVER);
        sendBroadcast(intent);
        finish();
    }

    //支付宝状态
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SDK_PAY_FLAG: {
                    System.out.println("调用支付接口——支付宝在判断中" + msg.obj);
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档

                    if (TextUtils.equals(resultStatus, "9000")) {

                        System.out.println("调用支付接口——支付宝——成功");
                        Toast.makeText(RechargeActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RetrofitUtil.APP_BORADCASTRECEIVER);
                        sendBroadcast(intent);
                        finish();
//                        zhifubao(totalfee, paynumber, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token());
//                        actionsCreator().customerPay(totalfee, paynumber, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), RechargeActivity.this);
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(RechargeActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            progressBar1.setVisibility(View.GONE);
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(RechargeActivity.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };


    @Override
    public void initView() {
        super.initView();
        setView(R.layout.activity_recharge);
        setTitle("充值");
        recharge_money.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (s != null) {
                        //线判断小数点前
                        String te = s.toString();
                        String first = te.substring(0, 1);
                        if (first.equals(".")) {
                            recharge_money.setText("0.");
                            recharge_money.setSelection(te.length() + 1);
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @OnClick({R.id.act_recharge_btn_submit, R.id.btn_alipay, R.id.btn_weixin, R.id.btn_recharge_card})
    @Override
    public void onClick(View v) {
        super.onClick(v);

        switch (v.getId()) {
            //
            case R.id.act_recharge_btn_submit:
                try {
                    String te = recharge_money.getText().toString();
                    System.out.println("te" + te.split("//."));
                    if (te.split("\\.")[0].length() >= 7) {
                        Show_toast.showText(RechargeActivity.this, "请输入正确的充值金额");
                        return;
                    }
                    if (te.split("\\.")[1].length() > 2) {
                        Show_toast.showText(RechargeActivity.this, "请输入正确的充值金额");
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


                progressBar1.setVisibility(View.VISIBLE);
                if (payment == 0) {
                    if (isWeixinAvilible(RechargeActivity.this)) {
                        diaoyong_weix();
                    } else {
                        Show_toast.showText(RechargeActivity.this, "检测到未安装微信");
                        progressBar1.setVisibility(View.GONE);
                    }
                } else if (payment == 1) {
                    aliPay();//支付宝
                } else {
                    rechargeCard();//卡
                }
                break;
            case R.id.btn_alipay:
                payment = 1;
                btn_weixin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_unselect_round));
                btn_alipay.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_select_round));
                btn_recharge_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_unselect_round));
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout2.setVisibility(View.GONE);
                break;
            case R.id.btn_weixin:
                payment = 0;
                btn_alipay.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_unselect_round));
                btn_weixin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_select_round));
                btn_recharge_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_unselect_round));
                relativeLayout.setVisibility(View.VISIBLE);
                relativeLayout2.setVisibility(View.GONE);
                break;
            case R.id.btn_recharge_card:
                payment = 2;
                recharge_money.setText("");
                btn_alipay.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_unselect_round));
                btn_weixin.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_unselect_round));
                btn_recharge_card.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_select_round));
                relativeLayout2.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);
                break;
        }
    }


    public void rechargeCard() {
        progressBar1.setVisibility(View.GONE);
        card = act_recharge_card.getText().toString();
        if (TextUtils.isEmpty(card)) {
            Toast.makeText(RechargeActivity.this, "请输入充值卡卡密", Toast.LENGTH_LONG).show();

            return;
        }

        actionsCreator().rechargePay(card, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), RechargeActivity.this);
    }


    public void diaoyong_weix() {

        totalfee = recharge_money.getText().toString();
        if (TextUtils.isEmpty(totalfee)) {//有bug---
            Toast.makeText(RechargeActivity.this, "请输入充值金额", Toast.LENGTH_LONG).show();
            progressBar1.setVisibility(View.GONE);
            return;
        }

        int a = (int) (Double.valueOf(totalfee) * 100);
        totalfee = "" + a;
        System.out.println("totalfee:" + totalfee);
        //一劳永逸，之后不会出现bug
//        actionsCreator().weixinPay(body, totalfee, goodstag, source, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), Order_pay.this);
        goodstag = "微信充值";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_weix;
        RequestParams requestParams = new RequestParams();
        requestParams.put("body", body);
        requestParams.put("totalfee", totalfee);
        requestParams.put("goodstag", goodstag);
        requestParams.put("source", source);
        requestParams.put("type", "1");
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);

                try {
                    System.out.println("url_weix" + jsonObject.toString());
                    progressBar1.setVisibility(View.GONE);
                    if (jsonObject.getString("ret").equals("200")) {

                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        WeixinInfo weixinInfo = new WeixinInfo();
                        weixinInfo.setSign(jsonObject1.getString("sign"));
                        weixinInfo.setResult_code(jsonObject1.getString("result_code"));
                        weixinInfo.setMch_id(jsonObject1.getString("mch_id"));
                        weixinInfo.setReturn_msg(jsonObject1.getString("return_msg"));
                        weixinInfo.setPrepay_id(jsonObject1.getString("prepay_id"));
                        weixinInfo.setAppid(jsonObject1.getString("appid"));
                        weixinInfo.setOut_trade_no(jsonObject1.getString("return_code"));
                        weixinInfo.setNonce_str(jsonObject1.getString("nonce_str"));
                        weixinInfo.setTrade_type(jsonObject1.getString("trade_type"));

                        Intent intent = new Intent(RetrofitUtil.APP_BORADCASTRECEIVER2);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("weixinInfo", weixinInfo);
                        intent.putExtras(mBundle);
                        sendBroadcast(intent);
                    } else {

                        Show_toast.showText(RechargeActivity.this, "充值失败");
                    }


                } catch (Exception e) {
                    progressBar1.setVisibility(View.GONE);
                    Show_toast.showText(RechargeActivity.this, getResources().getString(R.string.zhengqueje));
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressBar1.setVisibility(View.GONE);
                Show_toast.showText(RechargeActivity.this, "服务器繁忙，请重试");

            }
        });

    }


    /**
     * 支付宝
     */
    public void aliPay() {

        totalfee = recharge_money.getText().toString();
        if (TextUtils.isEmpty(totalfee)) {
            Toast.makeText(RechargeActivity.this, "请填写金额", Toast.LENGTH_LONG).show();
            progressBar1.setVisibility(View.GONE);
            return;
        }

//        paynumber = Alipay.getOutTradeNo();//商户订单号
//        String orderInfo = Alipay.getOrderInfo("支付宝充值", "三个阿姨平台余额充值", totalfee, paynumber);
//        String sign = Alipay.sign(orderInfo);
//        try {
//            sign = URLEncoder.encode(sign, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            progressBar1.setVisibility(View.GONE);
//        }
//        /*
//        这边修改为新的
//         */
//
//        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + Alipay.getSignType();

        goodstag = "支付宝支付";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_ALIpay;
        RequestParams requestParams = new RequestParams();
        requestParams.put("body", body);
        requestParams.put("totalfee", totalfee);
        requestParams.put("goodstag", goodstag);
        requestParams.put("source", source);
//        requestParams.put("ordernum", AyiApplication.getInstance().accountService().mobile());
        String ch = "";
        if (getIntent().getStringExtra("childordernum2") != null) {
            ch = getIntent().getStringExtra("childordernum2");
        } else {
            ch = "-1";
        }
        requestParams.put("childordernum", ch);
        requestParams.put("type", "1");
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());

        System.out.println("requestParams"+requestParams);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                System.out.println(jsonObject);


                try {
                    final String orderInfo = jsonObject.getString("data");
                    Log.i("msp", orderInfo);
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(RechargeActivity.this);
                            Map<String, String> result = alipay.payV2(orderInfo, true);
                            Log.i("msp", result.toString());

                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };

                    Thread payThread = new Thread(payRunnable);
                    payThread.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);

            }
        });

//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        String url = RetrofitUtil.url_pay_order_success;//需要修改 一个支付宝的预支付-会返回一些需要的key
//        RequestParams requestParams = new RequestParams();
//        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
//        requestParams.put("token",AyiApplication.getInstance().accountService().token());
//        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//
//
//
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                super.onFailure(statusCode, headers, throwable, errorResponse);
//
//            }
//        });


    }

    public void weixinCallback(WeixinInfo weixinInfo) {

        actionsCreator().weixinCallback(weixinInfo.getOut_trade_no(), "APP", AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), RechargeActivity.this);
    }

    @Override
    protected boolean flux() {
        return true;
    }

    @Override
    protected void updateView(Store.StoreChangeEvent event) {
        super.updateView(event);

    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);
    }

    public void onStart() {
        super.onStart();
        registerBoradcastReceiver();
    }

    public void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RetrofitUtil.APP_BORADCASTRECEIVER2);
        intentFilter.addAction(RetrofitUtil.APP_BORADCASTRECEIVER4);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.mBroadcastReceiver);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onStop() {
        super.onStop();

    }

    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }
}
