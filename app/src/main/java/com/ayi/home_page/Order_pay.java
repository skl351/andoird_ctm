package com.ayi.home_page;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.actions.PayActionsCreator;
import com.ayi.activity.MainActivity;
import com.ayi.activity.TempletActivity;
import com.ayi.entity.PayResult;
import com.ayi.entity.WeixinInfo;
import com.ayi.entity.item_coupon;
import com.ayi.entity.mlist_pay;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.stores.MyInfoStore;
import com.ayi.utils.Show_toast;
import com.ayi.wxapi.WeiXin;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/8/29.
 */
public class Order_pay extends TempletActivity<MyInfoStore, PayActionsCreator> {
    private LinearLayout price_total_weihzi;
    private Button button_home_zdg_ok_btn;
    private View top;
    private View back;
    private List<mlist_pay> mlist;
    private View coupon;
    private ImageView coupon_to_right;
    private TextView coupon_price;
    private String areaid;
    private TextView order_text;
    private TextView tishi_code;
    private View progressBar1;
    private TextView yuesao_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_pay_view);
        progressBar1 = findViewById(R.id.progressBar1);
        order_text = (TextView) findViewById(R.id.order_text);
        init();
        init_back();
        init_now_pay();
        init_text();
        init_radio_click();
        init_coupon_click();//优惠券点击-待定
        init_yue();

    }

    private void init_yue() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_get_info;
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());

        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    System.out.println(jsonObject.toString());
                    text_yue2.setText(jsonObject.getJSONObject("data").getString("remainSum"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    int pay_way = 1;//1是余额，2微信，3支付宝

    private void init_radio_click() {
        click_yue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_zhifubao_img.setBackground(getResources().getDrawable(R.drawable.btn_unselect_round));
                pay_yue_img.setBackground(getResources().getDrawable(R.drawable.btn_select_round));
                pay_weix_img.setBackground(getResources().getDrawable(R.drawable.btn_unselect_round));
                pay_way = 1;
            }
        });
        click_zhifubao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_zhifubao_img.setBackground(getResources().getDrawable(R.drawable.btn_select_round));
                pay_yue_img.setBackground(getResources().getDrawable(R.drawable.btn_unselect_round));
                pay_weix_img.setBackground(getResources().getDrawable(R.drawable.btn_unselect_round));
                pay_way = 3;
            }
        });
        click_weix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay_zhifubao_img.setBackground(getResources().getDrawable(R.drawable.btn_unselect_round));
                pay_yue_img.setBackground(getResources().getDrawable(R.drawable.btn_unselect_round));
                pay_weix_img.setBackground(getResources().getDrawable(R.drawable.btn_select_round));
                pay_way = 2;
            }
        });
    }

    private void init_coupon_click() {
        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Order_pay.this, Coupon.class);
                intent.putExtra("type_num", getIntent().getStringExtra("type_num"));
                intent.putExtra("areaid", areaid);
                System.out.println("需要传的type_num:" + getIntent().getStringExtra("type_num"));
                intent.putExtra("flag", "1");
                startActivity(intent);
            }
        });
    }

    private int couponId = 0;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getSerializableExtra("coupon") != null) {
            coupon_to_right.setVisibility(View.GONE);
            coupon_price.setVisibility(View.VISIBLE);
            item_coupon item = (item_coupon) intent.getSerializableExtra("coupon");
            coupon_price.setText("￥" + item.getPrice());
            couponId = Integer.parseInt(item.getId());
            float total = Float.parseFloat(service_price.getText().toString());
            float youhuiq = Float.parseFloat(item.getPrice());
            DecimalFormat decimalFormat = new DecimalFormat("######0.00");
            String p = decimalFormat.format(total - youhuiq);
            KLog.e("实际金额");
            if (Float.parseFloat(p) < 0) {
                p = "0.00";
                pay_zhifubao_img.setBackground(getResources().getDrawable(R.drawable.btn_unselect_round));
                pay_yue_img.setBackground(getResources().getDrawable(R.drawable.btn_select_round));
                pay_weix_img.setBackground(getResources().getDrawable(R.drawable.btn_unselect_round));
                pay_way = 1;
                tishi_code.setVisibility(View.VISIBLE);
                click_zhifubao.setEnabled(false);
                click_weix.setEnabled(false);
            } else {
                tishi_code.setVisibility(View.GONE);
                click_zhifubao.setEnabled(true);
                click_weix.setEnabled(true);
            }
//            String noo=p.split("\\.")[0];
            servic_price_reality.setText(p);
        }
    }

    private void init_back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init_now_pay() {
        button_home_zdg_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_home_zdg_ok_btn.setEnabled(false);

                switch (pay_way) {
                    case 1://余额
                        yue_pay();
                        break;
                    case 2://微信
                        if (isWeixinAvilible(Order_pay.this)) {
                            weixin_pay();
                        } else {
                            Show_toast.showText(Order_pay.this, "检测到未安装微信");
                            button_home_zdg_ok_btn.setEnabled(true);
                        }
                        break;
                    case 3://支付宝


                        zhifubao_pay();
                        break;
                }


            }
        });
    }

    /**
     * 判断 用户是否安装微信客户端
     */
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

    public static boolean checkAliPayInstalled(Context context) {

        Uri uri = Uri.parse("alipays://platformapi/startApp");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        ComponentName componentName = intent.resolveActivity(context.getPackageManager());
        return componentName != null;
    }

    public String paynumber;
    private static final int SDK_PAY_FLAG = 1;

    /**
     * 支付宝
     *
     * @param string
     */
    public void aliPay(String string) {
        progressBar1.setVisibility(View.GONE);
        button_home_zdg_ok_btn.setEnabled(true);
        totalfee = string;
////        totalfee = "0.01";
//        paynumber = Alipay.getOutTradeNo();
//        String orderInfo = Alipay.getOrderInfo("支付宝支付：" + totalfee, "三个阿姨平台订单支付：" + totalfee, totalfee, paynumber);
//        String sign = Alipay.sign(orderInfo);
//        try {
//            sign = URLEncoder.encode(sign, "UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + Alipay.getSignType();
//        /*
//        这边修改为新的
//         */


        goodstag = "支付宝支付";
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_ALIpay;
        RequestParams requestParams = new RequestParams();
        requestParams.put("body", body);
        requestParams.put("totalfee", totalfee);
        requestParams.put("goodstag", goodstag);
        requestParams.put("source", source);
        requestParams.put("ordernum", getIntent().getStringExtra("ordernum"));
        String ch = "";
        if (getIntent().getStringExtra("childordernum2") != null) {
            ch = getIntent().getStringExtra("childordernum2");
        } else {
            ch = "-1";
        }
        requestParams.put("childordernum", ch);
        requestParams.put("type", "2");
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        System.out.println("ordernum:" + getIntent().getStringExtra("ordernum") + ",childordernum:" + ch);

        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                System.out.println(jsonObject);


                try {
                    final String orderInfo = jsonObject.getString("data");
                    Runnable payRunnable = new Runnable() {

                        @Override
                        public void run() {
//                            if (checkAliPayInstalled(Order_pay.this)){
                            PayTask alipay = new PayTask(Order_pay.this);

                            Map<String, String> result = alipay.payV2(orderInfo, true);
                            Log.i("payV2", result.toString());

                            Message msg = new Message();
                            msg.what = SDK_PAY_FLAG;
                            msg.obj = result;
                            mHandler1.sendMessage(msg);
//                            }else {
//
//                                Message msg = new Message();
//                                msg.what = 2;
//                                mHandler1.sendMessage(msg);
//
//                            }

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
    }

    //支付宝状态
    @SuppressLint("HandlerLeak")
    private Handler mHandler1 = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case SDK_PAY_FLAG: {
                    System.out.println("调用支付接口——支付宝在判断中" + msg.obj);
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    System.out.println("调用支付接口--经过");
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        System.out.println("调用支付接口——支付宝——成功");
                        AyiApplication.flag_tc_dg = "1";
                        Toast.makeText(Order_pay.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Order_pay.this, MainActivity.class);
                        intent.putExtra("for_refesh", "1");
                        intent.putExtra("tab", "1");
                        startActivity(intent);
//                        zhifubao();
//                        actionsCreator().customerPay(totalfee, paynumber, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), RechargeActivity.this);
                    } else {
                        // 判断resultStatus 为非"9000"则代表可能支付失败
                        // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(Order_pay.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else if (TextUtils.equals(resultStatus, "4000")) {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(Order_pay.this, "支付失败", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(Order_pay.this, "支付失败", Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case 2: {
                    Show_toast.showText(Order_pay.this, "检测到未安装支付宝");
                    button_home_zdg_ok_btn.setEnabled(true);
                }
                default:
                    break;
            }
            button_home_zdg_ok_btn.setEnabled(true);
        }

        ;
    };

    private void zhifubao() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_pay_order_success;
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("orderId", Integer.parseInt(getIntent().getStringExtra("orderid")));
        String childorder = "-1";
        if (getIntent().getStringExtra("childorder") != null) {
            childorder = getIntent().getStringExtra("childorder");
        }
        requestParams.put("childId", childorder);
        requestParams.put("payment", pay_way);
        requestParams.put("paynumber", "-1");
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
//                button_home_zdg_ok_btn.setEnabled(true);
                //成功
                try {
                    if (response.getJSONObject("data").getString("status").equals("1")) {
                        Toast.makeText(Order_pay.this, "支付成功", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(RetrofitUtil.APP_BORADCASTRECEIVER);
//                        sendBroadcast(intent);
////                        finish();
                        Intent intent = new Intent(Order_pay.this, MainActivity.class);
                        intent.putExtra("for_refesh", "1");
                        intent.putExtra("tab", "1");
                        startActivity(intent);


                    } else {
                        Toast.makeText(Order_pay.this, "调用支付宝失败，请重试", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }
        });

    }

    DecimalFormat df;

    private void weixin_pay() {
        progressBar1.setVisibility(View.VISIBLE);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_pay_order;
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("orderId", Integer.parseInt(getIntent().getStringExtra("orderid")));
        requestParams.put("couponId", couponId);
        requestParams.put("isGlod", 0);
        String childorder = "-1";
        if (getIntent().getStringExtra("childorder") != null) {
            childorder = getIntent().getStringExtra("childorder");
        }
        System.out.println("childorder:" + childorder);
        requestParams.put("childId", childorder);
        requestParams.put("payment", pay_way);
        requestParams.put("end", 3);
        System.out.println("2pay_way" + pay_way + "couponId" + couponId + "orderId" + Integer.parseInt(getIntent().getStringExtra("orderid")) + "=childorder:" + childorder);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {

                    System.out.println("url_pay_order" + jsonObject);

                    if (jsonObject.getString("ret").equals("200")) {
                        if (jsonObject.getJSONObject("data").getString("status").equals("1")) {
//                        String remain=jsonObject.getJSONObject("data").getString("remain");
                            diaoyong_weix(df.format(Double.parseDouble(jsonObject.getJSONObject("data").getString("endPrice"))));
//                        diaoyong_weix("0.01");
                        } else {
                            progressBar1.setVisibility(View.GONE);
                            button_home_zdg_ok_btn.setEnabled(true);
                            Toast.makeText(Order_pay.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressBar1.setVisibility(View.GONE);
                        button_home_zdg_ok_btn.setEnabled(true);
                        Toast.makeText(Order_pay.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressBar1.setVisibility(View.GONE);
                button_home_zdg_ok_btn.setEnabled(true);
            }
        });
    }

    private void zhifubao_pay() {
        progressBar1.setVisibility(View.VISIBLE);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_pay_order;
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("orderId", Integer.parseInt(getIntent().getStringExtra("orderid")));
        requestParams.put("couponId", couponId);
        requestParams.put("isGlod", 0);
        String childorder = "-1";
        if (getIntent().getStringExtra("childorder") != null) {
            childorder = getIntent().getStringExtra("childorder");
        }
        requestParams.put("childId", childorder);
        requestParams.put("payment", pay_way);
        requestParams.put("end", 3);
        System.out.println("3pay_way" + pay_way + "couponId" + couponId + "orderId" + Integer.parseInt(getIntent().getStringExtra("orderid")) + "=childorder:" + childorder);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {

                    System.out.println(jsonObject);
                    if (jsonObject.getString("ret").equals("200")) {
                        if (jsonObject.getJSONObject("data").getString("status").equals("1")) {
//                        String remain=jsonObject.getJSONObject("data").getString("remain");
                            aliPay(df.format(Double.parseDouble(jsonObject.getJSONObject("data").getString("endPrice"))));
//                        diaoyong_weix("0.01");
                        } else {
                            button_home_zdg_ok_btn.setEnabled(true);
                            progressBar1.setVisibility(View.GONE);
                            Toast.makeText(Order_pay.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        button_home_zdg_ok_btn.setEnabled(true);
                        progressBar1.setVisibility(View.GONE);
                        Toast.makeText(Order_pay.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                button_home_zdg_ok_btn.setEnabled(true);
                progressBar1.setVisibility(View.GONE);
            }
        });
    }

    public String totalfee = "0";
    public String body = "三个阿姨平台订单支付";
    public String goodstag;
    public String source = "APP";

    public void diaoyong_weix(String remain) {

        totalfee = String.valueOf((int) ((Double.valueOf(remain) * 100)));
        System.out.println("weix" + totalfee);
        goodstag = "微信支付";
        //一劳永逸，之后不会出现bug
//        actionsCreator().weixinPay(body, totalfee, goodstag, source, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), Order_pay.this);

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_weix;
        RequestParams requestParams = new RequestParams();
        requestParams.put("body", body);
        requestParams.put("totalfee", totalfee);
        requestParams.put("goodstag", goodstag);
        requestParams.put("source", source);
        requestParams.put("ordernum", getIntent().getStringExtra("ordernum"));
        String ch = "";
        if (getIntent().getStringExtra("childordernum2") != null) {
            ch = getIntent().getStringExtra("childordernum2");
        } else {
            ch = "-1";
        }
        requestParams.put("childordernum", ch);
        requestParams.put("type", "2");
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        System.out.println("ordernum:" + getIntent().getStringExtra("ordernum") + ",childordernum:" + ch);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);

                try {
                    System.out.println("url_weix" + jsonObject.toString());
                    progressBar1.setVisibility(View.GONE);
                    button_home_zdg_ok_btn.setEnabled(true);
                    if (jsonObject.getString("ret").equals("200")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        WeixinInfo weixinInfo = new WeixinInfo();
                        weixinInfo.setResult_code(jsonObject1.getString("result_code"));
                        weixinInfo.setMch_id(jsonObject1.getString("mch_id"));
                        weixinInfo.setReturn_msg(jsonObject1.getString("return_msg"));
                        weixinInfo.setPrepay_id(jsonObject1.getString("prepay_id"));
                        weixinInfo.setAppid(jsonObject1.getString("appid"));
                        weixinInfo.setOut_trade_no(jsonObject1.getString("out_trade_no"));
                        weixinInfo.setNonce_str(jsonObject1.getString("nonce_str"));
                        weixinInfo.setTrade_type(jsonObject1.getString("trade_type"));

                        //发送一个广播
                        Intent intent = new Intent(RetrofitUtil.APP_BORADCASTRECEIVER2);
                        Bundle mBundle = new Bundle();
                        mBundle.putSerializable("weixinInfo", weixinInfo);
                        intent.putExtras(mBundle);
                        sendBroadcast(intent);
                    } else {
                        Show_toast.showText(Order_pay.this, "支付失败");
                    }


                } catch (Exception e) {
                    Show_toast.showText(Order_pay.this, "支付金额异常");
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("微信错误" + responseString.toString());
                progressBar1.setVisibility(View.GONE);
                button_home_zdg_ok_btn.setEnabled(true);
                Show_toast.showText(Order_pay.this, "服务器繁忙，请重试");

            }
        });

    }

    public void weixinCallback(WeixinInfo weixinInfo) {

        actionsCreator().weixinCallback(weixinInfo.getOut_trade_no(), "APP", AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), Order_pay.this);

    }

    public void onStart() {
        super.onStart();
        registerBoradcastReceiver();
    }

    public WeixinInfo weixinInfo;
    //一个广播的人接收者
    public BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getAction();

            //微信发起支付请求
            if (str.equals(RetrofitUtil.APP_BORADCASTRECEIVER2)) {
                weixinInfo = (WeixinInfo) intent.getSerializableExtra("weixinInfo");
                WeiXin.createWXAPI(weixinInfo, Order_pay.this);//发起微信
                System.out.println("发起微信1");
                //微信充值回调接口
            } else if (str.equals(RetrofitUtil.APP_BORADCASTRECEIVER4)) {
                System.out.println("支付成功的");
                //支付成功--得不到
//                weixinCallback(weixinInfo);
                weix_call_back();
            }
        }

    };

    private void weix_call_back() {
        //这个是跳回订单页面
        Intent intent = new Intent(Order_pay.this, MainActivity.class);
        intent.putExtra("for_refesh", "1");
        intent.putExtra("tab", "1");
        startActivity(intent);
        AyiApplication.flag_tc_dg = "1";
//        Intent intent=new Intent(Order_pay.this, MainActivity.class);
//        intent.putExtra("for_refesh","1");
//        intent.putExtra("tab","1");
//        startActivity(intent);

//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.setTimeout(20000);
//        String url= RetrofitUtil.url_pay_order_success;
//        RequestParams requestParams=new RequestParams();
//        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
//        requestParams.put("token",AyiApplication.getInstance().accountService().token());
//        requestParams.put("orderId",Integer.parseInt(getIntent().getStringExtra("orderid")));
//        requestParams.put("childId",-1);
//        requestParams.put("payment",pay_way);
//        requestParams.put("paynumber","-1");
//        asyncHttpClient.post(url,requestParams,new JsonHttpResponseHandler(){
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
//                super.onSuccess(statusCode, headers, jsonObject);
//                try {
//                    System.out.println(jsonObject.toString());
//                    //成功
//                    if(jsonObject.getJSONObject("data").getString("status").equals("1")){
//
//
//
//                    }
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//            }
//        });
    }

    //注册只过滤相关的广播
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

    /**
     * 1先检查余额充足
     */
    private void yue_pay() {
        progressBar1.setVisibility(View.VISIBLE);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_pd_enough;
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("orderId", Integer.parseInt(getIntent().getStringExtra("orderid")));
        requestParams.put("couponId", couponId);
        requestParams.put("isGlod", 0);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    //充足
                    if (response.getJSONObject("data").getString("status").equals("1")) {
                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                        asyncHttpClient.setTimeout(20000);
                        String url = RetrofitUtil.url_pay_order;
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                        requestParams.put("token", AyiApplication.getInstance().accountService().token());
                        requestParams.put("orderId", Integer.parseInt(getIntent().getStringExtra("orderid")));
                        requestParams.put("couponId", couponId);
                        requestParams.put("isGlod", 0);
                        String childorder = "-1";
                        if (getIntent().getStringExtra("childorder") != null) {
                            childorder = getIntent().getStringExtra("childorder");
                        }
                        requestParams.put("childId", childorder);
                        requestParams.put("payment", pay_way);
                        requestParams.put("end", 3);
                        System.out.println("1pay_way" + pay_way + "couponId" + couponId + "orderId" + Integer.parseInt(getIntent().getStringExtra("orderid")) + "chaild" + childorder);
                        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                                super.onSuccess(statusCode, headers, jsonObject);
                                try {
                                    progressBar1.setVisibility(View.GONE);
                                    button_home_zdg_ok_btn.setEnabled(true);
                                    System.out.println("url_pay_order:" + jsonObject.toString());
                                    if (jsonObject.getString("ret").equals("200")) {
                                        //成功
                                        if (jsonObject.getJSONObject("data").getString("status").equals("1")) {
                                            Toast.makeText(Order_pay.this, "支付成功", Toast.LENGTH_SHORT).show();
//                                            Intent intent = new Intent(RetrofitUtil.APP_BORADCASTRECEIVER);
//                                            sendBroadcast(intent);
//                                            finish();
                                            Intent intent = new Intent(Order_pay.this, MainActivity.class);
                                            intent.putExtra("for_refesh", "1");
                                            intent.putExtra("tab", "1");
                                            startActivity(intent);
                                            AyiApplication.flag_tc_dg = "1";

                                        } else {
                                            progressBar1.setVisibility(View.GONE);
                                            button_home_zdg_ok_btn.setEnabled(true);
                                            Toast.makeText(Order_pay.this, "支付失败", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        progressBar1.setVisibility(View.GONE);
                                        button_home_zdg_ok_btn.setEnabled(true);
                                        Toast.makeText(Order_pay.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                progressBar1.setVisibility(View.GONE);
                                button_home_zdg_ok_btn.setEnabled(true);
                                System.out.println("responseString:" + responseString);
                                Toast.makeText(Order_pay.this, responseString, Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        button_home_zdg_ok_btn.setEnabled(true);
                        progressBar1.setVisibility(View.GONE);
                        Toast.makeText(Order_pay.this, "余额不足", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

                Toast.makeText(Order_pay.this, "支付失败", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void init_text() {
        service_content.setText(getIntent().getStringExtra("type"));
        String[] aa = getIntent().getStringExtra("time_start").split("\\|");
        if (aa.length > 2) {
            if (!aa[2].substring(0, 1).equals("0")) {
                service_time2.setVisibility(View.VISIBLE);
                service_time2.setText(aa[2]);
            }
            service_time.setText(aa[1]);
            service_time1.setText(aa[0]);
        } else if (aa.length > 1) {
            service_time.setText(aa[0]);
            service_time1.setText(aa[1]);
        } else if (aa.length > 0) {
            service_time.setText(getIntent().getStringExtra("time_start"));
            service_time1.setVisibility(View.GONE);
        }

        service_user.setText(getIntent().getStringExtra("user_name"));
        service_place.setText(getIntent().getStringExtra("place"));
        service_price.setText(getIntent().getStringExtra("price"));
//        serivce_price_2.setText("￥"+getIntent().getStringExtra("price"));
        servic_price_reality.setText(getIntent().getStringExtra("price"));
        System.out.println("mypri:" + getIntent().getStringExtra("price"));
        mlist = (List<mlist_pay>) getIntent().getSerializableExtra("list");
//        zanshi_one.setText(mlist.get(0).getProject());
        List<mlist_pay> list_item = (List<mlist_pay>) getIntent().getSerializableExtra("list");
        for (int i = 0; i < list_item.size(); i++) {
            View view = LayoutInflater.from(Order_pay.this).inflate(R.layout.pay_price_item_view, null);
            View center_num = view.findViewById(R.id.center_num);
            if (i == 0) {
                center_num.setVisibility(View.GONE);
            }
            TextView zanshi_one = (TextView) view.findViewById(R.id.zanshi_one);
            TextView serivce_price_2 = (TextView) view.findViewById(R.id.serivce_price_2);
            TextView num = (TextView) view.findViewById(R.id.num);
            serivce_price_2.setText("￥" + list_item.get(i).getPrice());
            zanshi_one.setText(list_item.get(i).getProject());
            num.setText(list_item.get(i).getQuantity());
            price_total_weihzi.addView(view);
        }
    }

    private TextView service_content;
    private TextView service_time;
    private TextView service_time1;
    private TextView service_time2;
    private TextView service_user;
    private TextView service_place;
    private TextView service_price;
    private TextView servic_price_reality;
    private TextView text_yue2;
    private ImageView pay_yue_img;
    private ImageView pay_zhifubao_img;
    private ImageView pay_weix_img;
    private View click_yue;
    private View click_zhifubao;
    private View click_weix;

    private void init() {
        tishi_code = (TextView) findViewById(R.id.tishi_code);
        yuesao_text = (TextView) findViewById(R.id.yuesao_text);
        if (getIntent().getStringExtra("type_num").equals("14")) {
            yuesao_text.setVisibility(View.VISIBLE);
            order_text.setText("单周总价");
        }
        if (getIntent().getStringExtra("type_num").equals("12") || getIntent().getStringExtra("type_num").equals("13") || getIntent().getStringExtra("type_num").equals("15")) {
            yuesao_text.setVisibility(View.VISIBLE);
            order_text.setText("单月总价");
        }
        df = new DecimalFormat("######0.00");
        areaid = getIntent().getStringExtra("areaid");
        service_time = (TextView) findViewById(R.id.service_time);
        service_time1 = (TextView) findViewById(R.id.service_time1);
        service_time2 = (TextView) findViewById(R.id.service_time2);
        price_total_weihzi = (LinearLayout) findViewById(R.id.price_total_weihzi);
        coupon_to_right = (ImageView) findViewById(R.id.coupon_to_right);
        coupon_price = (TextView) findViewById(R.id.coupon_price);
        click_zhifubao = findViewById(R.id.click_zhifubao);
        click_weix = findViewById(R.id.click_weix);
        click_yue = findViewById(R.id.click_yue);
        pay_yue_img = (ImageView) findViewById(R.id.pay_yue_img);
        pay_weix_img = (ImageView) findViewById(R.id.pay_weix_img);
        pay_zhifubao_img = (ImageView) findViewById(R.id.pay_zhifubao_img);
        coupon = findViewById(R.id.coupon);
        service_content = (TextView) findViewById(R.id.service_content);
        service_time = (TextView) findViewById(R.id.service_time);
        service_user = (TextView) findViewById(R.id.service_user);
        service_place = (TextView) findViewById(R.id.service_place);
        service_price = (TextView) findViewById(R.id.service_price);
        servic_price_reality = (TextView) findViewById(R.id.servic_price_reality);
        text_yue2 = (TextView) findViewById(R.id.text_yue2);
        top = findViewById(R.id.top);
        back = top.findViewById(R.id.logreg_left);
        button_home_zdg_ok_btn = (Button) findViewById(R.id.button_home_zdg_ok_btn);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onPageStart(getString(R.string.client_pay));
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPageEnd(getString(R.string.client_pay));
//        MobclickAgent.onPause(this);
    }
}
