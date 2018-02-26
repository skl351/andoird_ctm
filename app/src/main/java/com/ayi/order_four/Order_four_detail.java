package com.ayi.order_four;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Data_time_cuo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/8/30.
 */
public class Order_four_detail extends Activity {
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
    private View view_clear_ingo;
    private View order_serivce_gone;
    private TextView service_time3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_four_detail);
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

    private void init() {
        order_serivce_gone = findViewById(R.id.order_serivce_gone);
        service_time3 = (TextView) findViewById(R.id.service_time3);
        view_clear_ingo = findViewById(R.id.view_clear_ingo);
        headimg = (ImageView) findViewById(R.id.headimg);
        clear_name = (TextView) findViewById(R.id.clear_name);
        clear_service_num = (TextView) findViewById(R.id.clear_service_num);
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
    }

    private TextView clear_name;
    private TextView clear_service_num;
    private ImageView headimg;

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
                    JSONObject jsonArray_data = response.getJSONObject("data");
                    orderid.setText(jsonArray_data.getString("ordernum"));
                    service_content.setText(jsonArray_data.getJSONObject("serviceShow").getString("content"));
                    String[] str = jsonArray_data.getJSONObject("serviceShow").getString("time").split("\\|");
                    service_time1.setText(str[0]);
                    service_time2.setText(str[1]);
                    if (str.length > 2) {
                        service_time3.setText(str[2]);
                        order_serivce_gone.setVisibility(View.VISIBLE);
                    }
                    service_object.setText(jsonArray_data.getString("contacts"));
                    service_place.setText(jsonArray_data.getString("contact_address"));
                    total_money.setText(jsonArray_data.getString("pricetotal"));
                    baojie_money.setText(jsonArray_data.getString("pricetotal"));
                    if (jsonArray_data.getJSONObject("cleanerInfo").length() > 0) {
                        view_clear_ingo.setVisibility(View.VISIBLE);
                        ImageLoader.getInstance().displayImage(jsonArray_data.getJSONObject("cleanerInfo").getString("headimg"), headimg);
                        clear_name.setText(jsonArray_data.getJSONObject("cleanerInfo").getString("name") + "(" + jsonArray_data.getJSONObject("cleanerInfo").getString("old") + "岁" + jsonArray_data.getJSONObject("cleanerInfo").getString("place") + ")");
                        clear_service_num.setText("服务（" + jsonArray_data.getJSONObject("cleanerInfo").getString("times") + ")次");
                    }
                    for (int i = 0; i < jsonArray_data.getJSONArray("progress").length(); i++) {
                        View view = LayoutInflater.from(Order_four_detail.this).inflate(R.layout.item_progress, null);
                        TextView textView = (TextView) view.findViewById(R.id.time);
                        TextView text_bz = (TextView) view.findViewById(R.id.text_bz);
                        textView.setText(Data_time_cuo.gettime(jsonArray_data.getJSONArray("progress").getJSONObject(i).getString("timestamp")));
                        String bz = "";
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
                                bz = "已退款到雇主余额账户";
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
}
