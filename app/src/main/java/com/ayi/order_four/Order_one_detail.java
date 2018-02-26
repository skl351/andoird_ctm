package com.ayi.order_four;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.home_page.Ayi_list_item;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Data_time_cuo;
import com.ayi.zidingyi_view.StarBarView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/8/30.
 */
public class Order_one_detail extends Activity {
    private View daikexiadan_id;
    private TextView orderid;
    private TextView service_content;
    private TextView service_time1;
    private TextView service_time2;
    private TextView service_object;
    private TextView service_place;
    private TextView total_money;
    private TextView baojie_money;
    private View top;
    private View back;
    private LinearLayout progress_id;
    private ImageView headimg;
    private View view_clear_info;
    private View order_serivce_gone;
    private TextView service_time3;
    String status;
    private TextView status_text;
    private TextView pay_way;
    private TextView service_content_like;
    private String areaid;
    private View baoxianview;
    private View gongzuojindu;
    private View yigeflag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_one_detail);
        status = getIntent().getStringExtra("status");
        areaid = getIntent().getStringExtra("areaid");
        init();
        init_back();
        init_wangluo();
    }

    private void init_back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    AlertDialog alert;
    private LinearLayout suishoudai_view_show;
    private TextView youhuidikou_money;
    private View youhuidikou_money_big;
    private View kenengxuyao_cangqilai;
    private View jiaqian_text;
    private View jiaqian_text2;

    private void init() {
        yigeflag = findViewById(R.id.yigeflag);
        gongzuojindu = findViewById(R.id.gongzuojindu);
        baoxianview = findViewById(R.id.baoxianview);
        daikexiadan_id = findViewById(R.id.daikexiadan_id);
        jiaqian_text2 = findViewById(R.id.jiaqian_text2);
        jiaqian_text = findViewById(R.id.jiaqian_text);
        kenengxuyao_cangqilai = findViewById(R.id.kenengxuyao_cangqilai);
        youhuidikou_money_big = findViewById(R.id.youhuidikou_money_big);
        youhuidikou_money = (TextView) findViewById(R.id.youhuidikou_money);
        suishoudai_view_show = (LinearLayout) findViewById(R.id.suishoudai_view_show);
        service_content_like = (TextView) findViewById(R.id.service_content_like);
        view_big_clear_info = findViewById(R.id.view_big_clear_info);
        pay_way = (TextView) findViewById(R.id.pay_way);
        status_text = (TextView) findViewById(R.id.status);
        status_text.setText(status);
        if (status.equals("已评价")) {
            status_text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    String url = RetrofitUtil.url_list_getEvaluate;
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("orderid", getIntent().getStringExtra("id"));
                    requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                    requestParams.put("token", AyiApplication.getInstance().accountService().token());
                    asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(Order_one_detail.this);
                                alert = builder.create();
                                View view = LayoutInflater.from(Order_one_detail.this).inflate(R.layout.pingjia_reason, null);
                                final StarBarView ratingBar = (StarBarView) view.findViewById(R.id.ratingBar);
                                ratingBar.setStarRating(Integer.parseInt(response.getJSONObject("data").getString("eService")));
                                ratingBar.setIsIndicator(true);
                                final EditText edt = (EditText) view.findViewById(R.id.pingjia_edt);
                                edt.setVisibility(View.GONE);
                                TextView pinglun_text = (TextView) view.findViewById(R.id.pinglun_text);
                                pinglun_text.setText(response.getJSONObject("data").getString("remark"));
                                edt.setEnabled(false);
                                view.findViewById(R.id.double_button).setVisibility(View.GONE);
                                ImageView b = (ImageView) view.findViewById(R.id.delete_button);
                                b.setVisibility(View.VISIBLE);
                                b.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alert.dismiss();
                                    }
                                });
                                alert.setView(view);
                                alert.show();
                            } catch (Exception e) {

                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                        }
                    });
                }
            });
        }
        service_time3 = (TextView) findViewById(R.id.service_time3);
        order_serivce_gone = findViewById(R.id.order_serivce_gone);
        view_clear_info = findViewById(R.id.view_clear_info);
        clear_name = (TextView) findViewById(R.id.clear_name);
        headimg = (ImageView) findViewById(R.id.headimg);
        progress_id = (LinearLayout) findViewById(R.id.progress_id);
        top = findViewById(R.id.top);
        back = top.findViewById(R.id.logreg_left);
        orderid = (TextView) findViewById(R.id.orderid);
        service_content = (TextView) findViewById(R.id.service_content);
        service_time1 = (TextView) findViewById(R.id.service_time1);
        service_time2 = (TextView) findViewById(R.id.service_time2);
        service_object = (TextView) findViewById(R.id.service_object);
        service_place = (TextView) findViewById(R.id.service_place);
        service_place.setSelected(true);
        total_money = (TextView) findViewById(R.id.total_money);
        baojie_money = (TextView) findViewById(R.id.baojie_money);
        if (!getIntent().getStringExtra("trialorder").equals("1")) {
            if (Integer.parseInt(getIntent().getStringExtra("service_type_id")) > 8) {
                jiaqian_text.setVisibility(View.GONE);
                kenengxuyao_cangqilai.setVisibility(View.GONE);
                jiaqian_text2.setVisibility(View.VISIBLE);
                jiaqian_text2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Order_one_detail.this, Service_detail.class);
                        intent.putExtra("type_num", getIntent().getStringExtra("type_num"));
                        intent.putExtra("orderid", getIntent().getStringExtra("orderid"));
                        intent.putExtra("type", getIntent().getStringExtra("type"));
                        intent.putExtra("time_start", getIntent().getStringExtra("time_start"));
                        intent.putExtra("user_name", getIntent().getStringExtra("user_name"));
                        intent.putExtra("place", getIntent().getStringExtra("place"));
                        intent.putExtra("price", getIntent().getStringExtra("price"));
                        intent.putExtra("list", getIntent().getSerializableExtra("list"));
                        intent.putExtra("areaid", areaid);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    String ss = "0";
    private TextView clear_name;
    private View view_big_clear_info;

    private void init_wangluo() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_list_order_detail;
        RequestParams requestParams = new RequestParams();
        requestParams.put("orderid", getIntent().getStringExtra("id"));
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    System.out.println(response);
                    JSONObject jsonArray_data = response.getJSONObject("data");
                    ss = jsonArray_data.getString("status");
                    JSONArray jsonArray_detail = jsonArray_data.getJSONArray("detail");
                    orderid.setText(jsonArray_data.getString("ordernum"));
                    service_content.setText(jsonArray_data.getJSONObject("serviceShow").getString("content"));
                    service_content_like.setText(jsonArray_data.getJSONObject("serviceShow").getString("content"));
                    String[] str = jsonArray_data.getJSONObject("serviceShow").getString("time").split("\\|");
                    String payment = jsonArray_data.getString("payment");
                    String payed = jsonArray_data.getString("payed");
                    if (jsonArray_data.getInt("service_type_id") > 8) {
                        yigeflag.setVisibility(View.GONE);
                        if (!jsonArray_data.getString("policynum_customer").equals("")) {
                            gongzuojindu.setVisibility(View.VISIBLE);
                            gongzuojindu.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Order_one_detail.this, Service_detail.class);
                                    intent.putExtra("type_num", getIntent().getStringExtra("type_num"));
                                    intent.putExtra("orderid", getIntent().getStringExtra("orderid"));
                                    intent.putExtra("type", getIntent().getStringExtra("type"));
                                    intent.putExtra("time_start", getIntent().getStringExtra("time_start"));
                                    intent.putExtra("user_name", getIntent().getStringExtra("user_name"));
                                    intent.putExtra("place", getIntent().getStringExtra("place"));
                                    intent.putExtra("price", getIntent().getStringExtra("price"));
                                    intent.putExtra("list", getIntent().getSerializableExtra("list"));
                                    intent.putExtra("areaid", areaid);
                                    startActivity(intent);
                                }
                            });
                        }

                    }
                    if (!jsonArray_data.getString("policynum_customer").equals("")) {
                        baoxianview.setVisibility(View.VISIBLE);
                    }
                    if (jsonArray_data.getInt("isvalet") == 1) {
                        daikexiadan_id.setVisibility(View.VISIBLE);
                    } else {
                        daikexiadan_id.setVisibility(View.GONE);
                    }
                    if ("0".equals(payed)) {
                        pay_way.setText("未支付");
                    } else {
                        switch (payment) {
                            case "0":
                                pay_way.setText("服务后付款");
                                break;
                            case "1":
                                pay_way.setText("余额支付");
                                break;
                            case "2":
                                pay_way.setText("微信支付");
                                break;
                            case "3":
                                pay_way.setText("支付宝支付");
                                break;
                        }
                    }

                    if (str.length > 2) {
                        if (!str[2].substring(0, 1).equals("0")) {
                            service_time3.setText(str[2]);
                            service_time3.setVisibility(View.VISIBLE);
                        }
                        service_time2.setText(str[0]);
                        service_time1.setText(str[1]);

                    } else {
                        service_time1.setText(str[0]);
                        service_time2.setText(str[1]);
                    }
                    service_object.setText(jsonArray_data.getString("contacts"));
                    service_place.setText(jsonArray_data.getString("contact_address"));
                    for (int i = 0; i < jsonArray_detail.length(); i++) {
                        View view = LayoutInflater.from(Order_one_detail.this).inflate(R.layout.pay_price_item_view, null);
                        TextView name = (TextView) view.findViewById(R.id.zanshi_one);
                        TextView num = (TextView) view.findViewById(R.id.num);
                        TextView serivce_price_2 = (TextView) view.findViewById(R.id.serivce_price_2);
                        name.setText(jsonArray_detail.getJSONObject(i).getString("name"));
                        num.setText(jsonArray_detail.getJSONObject(i).getString("quantity"));
                        String price = jsonArray_detail.getJSONObject(i).getString("price").split("\\.")[0];
                        serivce_price_2.setText("￥" + Integer.valueOf(jsonArray_detail.getJSONObject(i).getString("quantity")) * Integer.valueOf(price));
                        suishoudai_view_show.addView(view);
                    }
                    total_money.setText(jsonArray_data.getString("pricetotal"));
                    baojie_money.setText(jsonArray_data.getString("price"));
                    if (!jsonArray_data.getString("coupon").split("\\.")[0].equals("0")) {
                        youhuidikou_money_big.setVisibility(View.VISIBLE);
                        youhuidikou_money.setText(jsonArray_data.getString("coupon"));
                    }
                    if (jsonArray_data.getJSONObject("cleanerInfo").length() > 0) {
                        view_big_clear_info.setVisibility(View.VISIBLE);
                        view_clear_info.setVisibility(View.VISIBLE);
                        clear_name.setText(jsonArray_data.getJSONObject("cleanerInfo").getString("name"));
                        final String id = jsonArray_data.getJSONObject("cleanerInfo").getString("id");
                        view_clear_info.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Order_one_detail.this, Ayi_list_item.class);
                                intent.putExtra("cleaner_id", id);
                                intent.putExtra("buxianshi", "false");
                                intent.putExtra("flag_ord", ss);
                                startActivity(intent);
                            }
                        });
                    }
                    for (int i = 0; i < jsonArray_data.getJSONArray("progress").length(); i++) {
                        View view = LayoutInflater.from(Order_one_detail.this).inflate(R.layout.item_progress, null);
                        TextView textView = (TextView) view.findViewById(R.id.time);
                        TextView text_bz = (TextView) view.findViewById(R.id.text_bz);
                        textView.setText(Data_time_cuo.gettime(jsonArray_data.getJSONArray("progress").getJSONObject(i).getString("timestamp")));
                        String bz = "";
                        ImageView line_shang = (ImageView) view.findViewById(R.id.dot_shang_line);
                        ImageView line_xia = (ImageView) view.findViewById(R.id.dot_xia_line);
                        ImageView cent = (ImageView) view.findViewById(R.id.dot_cent);
                        if (jsonArray_data.getJSONArray("progress").length() != 1) {
                            if (i == 0) {
                                line_shang.setVisibility(View.INVISIBLE);
                                line_xia.setVisibility(View.VISIBLE);
                            }
                            if (i == jsonArray_data.getJSONArray("progress").length() - 1) {
                                line_shang.setVisibility(View.VISIBLE);
                                line_xia.setVisibility(View.INVISIBLE);
                                cent.setBackgroundResource(R.drawable.icon_shijianzhou_dot);
                            }
                        } else {
                            line_shang.setVisibility(View.INVISIBLE);
                            line_xia.setVisibility(View.INVISIBLE);
                            cent.setBackgroundResource(R.drawable.icon_shijianzhou_dot);
                        }
                        switch (jsonArray_data.getJSONArray("progress").getJSONObject(i).getString("status")) {
                            case "0":
                                bz = "客户下单";
                                break;
                            case "1":
                                bz = "阿姨接单";
                                break;
                            case "2":
                                bz = "开始工作";
                                break;
                            case "3":
                                bz = "完成工作";
                                break;
                            case "4":
                                bz = "客户评价";
                                break;
                            case "5":
                                bz = "订单取消";
                                break;
                            case "6":
                                bz = "null";
                                break;
                            case "7":
                                bz = "已支付";
                                break;
                            case "8":
                                bz = "雇主申请取消试用订单";
                                break;
                            case "9":
                                bz = "阿姨申请取消试用订单";
                                break;
                            case "10":
                                bz = "雇主同意阿姨取消申请试用订单";
                                break;
                            case "11":
                                bz = "阿姨同意雇主取消申请试用订单";
                                break;
                            case "12":
                                bz = "雇主拒绝阿姨取消申请试用订单";
                                break;
                            case "13":
                                bz = "阿姨拒绝雇主取消申请试用订单";
                                break;
                            case "14":
                                bz = "雇主申请正式签约";
                                break;
                            case "15":
                                bz = "阿姨同意雇主申请正式签约";
                                break;
                            case "16":
                                bz = "阿姨拒绝雇主申请正式签约";
                                break;
                            case "17":
                                bz = jsonArray_data.getJSONArray("progress").getJSONObject(i).getString("remark");
                                break;
                            case "18":
                                bz = "线下退款";
                                break;
                        }
                        if (!bz.equals("null")) {
                            text_bz.setText(bz);
                            progress_id.addView(view);
                        }
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


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.client_order_detail));
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.client_order_detail));
        MobclickAgent.onPause(this);
    }
}