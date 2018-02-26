package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.entity.Result;
import com.ayi.entity.item_place_info;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.FileService;
import com.ayi.utils.Show_toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/5/24.
 */

public class Business_appointment_tc extends Activity {
    private TextView xinagmuitem;
    private TextView place1;
    private TextView place2;
    String user_name;
    String user_phone;
    String place_info;
    String place_info2;
    String latitude;
    String longitude;
    private View progressBar1;
    private WebView image_big;

    private View click_detailed_info;
    private Button button_home_zdg_ok_btn;
    private TextView center_text;
    private EditText edit_hiht;

    String orderid;
    String payed;
    String status;
    String payment;
    private TextView status_text2;
    private View yige_status;
    private View yige_zhifufangshi;

    private TextView pay_way;

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body><img src=\"" + bodyHTML + "\"   vspace=\"0\" border=\"0\"  width=\"100%\"/></body></html>";
    }


    private View daikexiadan_id;
    private boolean need_hidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_appoint_view);
        need_hidden = getIntent().getBooleanExtra("need_hidden", false);
        init_view();
        init_back();
        init_godelete();
        if (getIntent().getBooleanExtra("flag", false)) {
            init_ok2();
        } else {
            init_ok();
        }
        if (getIntent().getStringExtra("remark") != null) {
            if (getIntent().getIntExtra("isvalet", 0) == 1) {
                daikexiadan_id.setVisibility(View.VISIBLE);
            } else {
                daikexiadan_id.setVisibility(View.GONE);
            }

            xinagmuitem.setText(getIntent().getStringExtra("title"));
            center_text.setText("订单详情");
            KLog.e("-------------" + getIntent().getStringExtra("isimg") + "," + getIntent().getStringExtra("text"));
            if (getIntent().getStringExtra("isimg").equals("0")) {
                image_big.setVisibility(View.VISIBLE);
//                String url_string = "<p><img src=\"" +
//                        getIntent().getStringExtra("image")
//                        + "\"   vspace=\"0\" border=\"0\"  width=\"100%\"/></p>";
//                image_big.loadDataWithBaseURL(null, getHtmlData(getIntent().getStringExtra("image")), "text/html", "utf-8", null);
                image_big.loadData(getHtmlData(getIntent().getStringExtra("image")), "text/html; charset=utf-8", "utf-8");
            } else {
                ceshi_text.setVisibility(View.VISIBLE);
                ceshi_text.setText(getIntent().getStringExtra("text"));
            }
            edit_hiht.setEnabled(false);
            tc_price_big.setVisibility(View.VISIBLE);
            first_price.setText(getIntent().getStringExtra("price"));
            yige_status.setVisibility(View.VISIBLE);
            yige_zhifufangshi.setVisibility(View.VISIBLE);
            edit_hiht.setText(getIntent().getStringExtra("remark"));
            edit_hiht.setTextColor(getResources().getColor(R.color.default_textcolor));
            edit_hiht.setHint("");
            orderid = getIntent().getStringExtra("ccsp_id");
            payed = getIntent().getStringExtra("payed");
            status = getIntent().getStringExtra("status");
            payment = getIntent().getStringExtra("payment");
            button_home_zdg_ok_btn.setVisibility(View.GONE);
            user_name = getIntent().getStringExtra("contacts");
            user_phone = getIntent().getStringExtra("contact_phone");
            place_info = getIntent().getStringExtra("contact_addr");
            place_info2 = getIntent().getStringExtra("contact_door");
            place1.setText(user_name + "    " + user_phone);

            place2.setText(place_info + "," + place_info2);
            if (payment.equals("0")) {
                pay_way.setText("尚未支付");
            }
            if (payment.equals("1")) {
                pay_way.setText("余额支付");
            }
            if (payment.equals("2")) {
                pay_way.setText("微信支付");
            }
            if (payment.equals("3")) {
                pay_way.setText("支付宝支付");
            }
            switch (status) {
                case "-2":
                    status_text2.setText("已退款");
                    status_text2.setTextColor(getResources().getColor(R.color.order_gra));
                    break;
                case "-1":
                    status_text2.setText("已取消");
                    status_text2.setTextColor(getResources().getColor(R.color.order_gra));
                    break;
                case "0":
                    if (payed.equals("0")) {
                        status_text2.setText("未支付");
                        status_text2.setTextColor(getResources().getColor(R.color.yishouli));
                        break;
                    } else {
                        status_text2.setText("待受理");
                        status_text2.setTextColor(getResources().getColor(R.color.yishouli));
                        break;
                    }
                case "1":
                    status_text2.setText("已受理");
                    status_text2.setTextColor(getResources().getColor(R.color.daishouli));
                    break;
            }
        } else {
            init_wangluo();
        }
    }


    private void init_wangluo() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_tc_deltail;//测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("id", getIntent().getStringExtra("ccsp_id"));
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    KLog.e("jsonObject" + jsonObject.toString());
                    center_text.setText(jsonObject.getJSONObject("data").getString("title"));
                    xinagmuitem.setText(jsonObject.getJSONObject("data").getString("title"));
                    if (jsonObject.getJSONObject("data").getString("isimg").equals("0")) {
                        image_big.setVisibility(View.VISIBLE);
                        String url_string = "<p><img src=\"" +
                                jsonObject.getJSONObject("data").getString("content_img")
                                + "\"   vspace=\"0\" border=\"0\"  width=\"100%\" /></p>";
//                        image_big.loadDataWithBaseURL(null, getHtmlData(jsonObject.getJSONObject("data").getString("content_img")), "text/html", "utf-8", null);
                        image_big.loadData(getHtmlData(jsonObject.getJSONObject("data").getString("content_img")), "text/html; charset=utf-8", "utf-8");


                    } else {
                        ceshi_text.setVisibility(View.VISIBLE);
                        ceshi_text.setText(jsonObject.getJSONObject("data").getString("content_txt"));

                    }
                    tc_price_big.setVisibility(View.VISIBLE);
                    first_price.setText(jsonObject.getJSONObject("data").getString("price"));
//                    edit_hiht.setText(getIntent().getStringExtra("remark"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressBar1.setVisibility(View.GONE);
                Show_toast.showText(Business_appointment_tc.this, "网络繁忙，请重试.");

            }
        });
    }

    /**
     * 套餐下单
     */
    public void ServicePackageadd(String areaid, String contacts, String contact_phone, String contact_addr, String contact_door, String remark, String ccsp_id, String token, String userid, int isvalet) {
        RetrofitUtil.getService().ServicePackageadd(areaid, contacts, contact_phone, contact_addr, contact_door, remark, ccsp_id, token, userid, isvalet, AyiApplication.m).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                        progressBar1.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Show_toast.showText(Business_appointment_tc.this, "网络繁忙，请重试");
                        progressBar1.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Result serpack) {
                        KLog.e("serpack" + serpack);
                        if (serpack.getRet() != 200) {
                            Show_toast.showText(Business_appointment_tc.this, "网络繁忙，请重试");
                            progressBar1.setVisibility(View.GONE);
                            return;
                        }
                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                        asyncHttpClient.setTimeout(20000);
                        String url = RetrofitUtil.url_tc_dowmload;//得到套餐订单列表
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                        requestParams.put("token", AyiApplication.getInstance().accountService().token());
                        requestParams.put("id", serpack.getData());
                        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                                super.onSuccess(statusCode, headers, jsonObject);
                                try {
                                    System.out.println("url_tc_dowmload" + jsonObject.toString());
                                    if ("200".equals(jsonObject.getString("ret"))) {
                                        progressBar1.setVisibility(View.GONE);
                                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                        Intent intent = new Intent(Business_appointment_tc.this, Order_pay_tc.class);
                                        intent.putExtra("contacts", jsonObject1.getString("contacts"));
                                        intent.putExtra("contact_phone", jsonObject1.getString("contact_phone"));
                                        intent.putExtra("contact_addr", jsonObject1.getString("contact_addr"));
                                        intent.putExtra("contact_door", jsonObject1.getString("contact_door"));
                                        intent.putExtra("price", jsonObject1.getString("price"));
                                        intent.putExtra("ccsp_title", jsonObject1.getString("ccsp_title"));
                                        intent.putExtra("ordernum", jsonObject1.getString("ordernum"));
                                        intent.putExtra("orderid", jsonObject1.getString("id"));
                                        intent.putExtra("type_id", jsonObject1.getString("service_type_id"));
                                        KLog.e("intent：" + intent);
                                        startActivity(intent);
                                    }
                                } catch (Exception e) {
                                    progressBar1.setVisibility(View.GONE);
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                                super.onFailure(statusCode, headers, responseString, throwable);
                                progressBar1.setVisibility(View.GONE);
                            }
                        });


                    }
                });
    }

    private void init_ok() {
        button_home_zdg_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place1.getText().toString().equals("")) {
                    Show_toast.showText(Business_appointment_tc.this, "请点击填写服务地址");
                    return;
                }
                progressBar1.setVisibility(View.VISIBLE);
                if (getIntent().getStringExtra("remark") == null) {
                    ServicePackageadd(AyiApplication.area_id, user_name, user_phone, place_info, place_info2, edit_hiht.getText().toString(), getIntent().getStringExtra("ccsp_id"), AyiApplication.getInstance().accountService().token(), AyiApplication.getInstance().accountService().id(), 0);
                }

            }
        });
    }

    private void init_ok2() {
        button_home_zdg_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place1.getText().toString().equals("")) {
                    Show_toast.showText(Business_appointment_tc.this, "请点击填写服务地址");
                    return;
                }
                progressBar1.setVisibility(View.VISIBLE);
                if (getIntent().getStringExtra("remark") == null) {
                    ServicePackageadd(AyiApplication.area_id, user_name, user_phone, place_info, place_info2, edit_hiht.getText().toString(), getIntent().getStringExtra("ccsp_id"), AyiApplication.getInstance().accountService().token(), AyiApplication.getInstance().accountService().id(), 1);
                }
            }
        });
    }

    private void init_godelete() {
        if (getIntent().getStringExtra("remark") == null) {
            click_detailed_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Business_appointment_tc.this, User_info_before.class);
                    intent.putExtra("flag_place", "tc");
                    intent.putExtra("flag", getIntent().getBooleanExtra("flag", false));
                    startActivity(intent);
                }
            });
        }

    }

    private void init_back() {
        findViewById(R.id.top).findViewById(R.id.logreg_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private TextView ceshi_text;
    private TextView first_price;
    private View tc_price_big;

    private void init_view() {
        daikexiadan_id = findViewById(R.id.daikexiadan_id);
        xinagmuitem = (TextView) findViewById(R.id.xinagmuitem);
        pay_way = (TextView) findViewById(R.id.pay_way);
        yige_status = findViewById(R.id.yige_status);
        yige_zhifufangshi = findViewById(R.id.yige_zhifufangshi);
        status_text2 = (TextView) findViewById(R.id.status_text2);
        edit_hiht = (EditText) findViewById(R.id.edit_hiht);
        tc_price_big = findViewById(R.id.tc_price_big);
        first_price = (TextView) findViewById(R.id.first_price);
        image_big = (WebView) findViewById(R.id.image_big);
        image_big.getSettings().setUseWideViewPort(true);
        System.out.println("出现了一个问题");
        image_big.getSettings().setLoadWithOverviewMode(true);
//        image_big.getSettings().setJavaScriptEnabled(true);
//// 设置可以支持缩放
//        image_big.getSettings().setSupportZoom(true);
////自适应屏幕
//        image_big.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        image_big.getSettings().setLoadWithOverviewMode(true);
        ceshi_text = (TextView) findViewById(R.id.ceshi_text);
        progressBar1 = findViewById(R.id.progressBar1);
        button_home_zdg_ok_btn = (Button) findViewById(R.id.button_home_zdg_ok_btn);

        place1 = (TextView) findViewById(R.id.place1);
        place2 = (TextView) findViewById(R.id.place2);
        click_detailed_info = findViewById(R.id.click_detailed_info);
        center_text = (TextView) findViewById(R.id.top).findViewById(R.id.logreg_center);

    }


    @Override
    protected void onResume() {
        super.onResume();
        KLog.e("need_hidden", need_hidden);
        if (!need_hidden) {
            if (getIntent().getStringExtra("remark") == null) {
                FileService fileService = new FileService(this);
                try {
                    item_place_info user_info = fileService.read("user.txt");
                    System.out.println("user_info" + user_info);
                    if (user_info.getShiji_dizhi().equals(AyiApplication.place)) {
                        latitude = user_info.getLatitude();
                        longitude = user_info.getLongitide();
                        place2.setVisibility(View.VISIBLE);
                        if (!user_info.getPlace().equals("")) {
                            place1.setText(user_info.getName() + "    " + user_info.getPhone());
                            place2.setText(user_info.getPlace() + "," + user_info.getNum_place());

                        } else {
                            place1.setText("");
                            place2.setText("");
                            place2.setVisibility(View.GONE);

                        }
                        user_name = user_info.getName();
                        user_phone = user_info.getPhone();
                        place_info = user_info.getPlace();
                        place_info2 = user_info.getNum_place();
                    } else {
                        place1.setText("");
                        place2.setText("");
                        place2.setVisibility(View.GONE);
                        if (!user_info.getShiji_dizhi().equals(""))
                        Show_toast.showText(Business_appointment_tc.this, "请选择对应城市");
                    }

                } catch (Exception e) {
                    place1.setText("");
                    place2.setText("");
                    place2.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }else {
                place2.setVisibility(View.VISIBLE);
            }
        } else {
            if (getIntent().getStringExtra("remark") == null) {
                FileService fileService = new FileService(this);
                try {
                    item_place_info user_info = fileService.read("user2.txt");
                    System.out.println("user_info" + user_info);
                    if (user_info.getShiji_dizhi().equals(AyiApplication.place)) {
                        latitude = user_info.getLatitude();
                        longitude = user_info.getLongitide();
                        place2.setVisibility(View.VISIBLE);
                        if (!user_info.getPlace().equals("")) {
                            place1.setText(user_info.getName() + "    " + user_info.getPhone());
                            place2.setText(user_info.getPlace() + "," + user_info.getNum_place());

                        } else {
                            place1.setText("");
                            place2.setText("");
                            place2.setVisibility(View.GONE);

                        }
                        user_name = user_info.getName();
                        user_phone = user_info.getPhone();
                        place_info = user_info.getPlace();
                        place_info2 = user_info.getNum_place();
                    } else {
                        place1.setText("");
                        place2.setText("");
                        place2.setVisibility(View.GONE);
                        if (!user_info.getShiji_dizhi().equals(""))
                        Show_toast.showText(Business_appointment_tc.this, "请选择对应城市");
                    }

                } catch (Exception e) {
                    place1.setText("");
                    place2.setText("");
                    place2.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }else {
                place2.setVisibility(View.VISIBLE);
            }
        }


    }
}
