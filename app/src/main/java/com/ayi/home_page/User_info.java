package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Debug_net;
import com.ayi.utils.FileService;
import com.ayi.utils.Show_toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/10/13.
 */
public class User_info extends Activity {
    private View progressBar1;
    //4个文本
    private TextView name;
    private TextView phone;
    private TextView address;
    private TextView door;
    //经纬度
    private String latitude = "";
    private String longitude = "";
    private String areaname = "";//区域名称
    private String adid = "";//地址id
    private View select_address;//点击可以进入悬着地址的界面
    private View button_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_view);
        init();
        init_back();
        init_select_address();//点击选择地址
        init_ok();
    }

    private void init_back() {
        findViewById(R.id.top).findViewById(R.id.logreg_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init_select_address() {
        select_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User_info.this, Map_search.class);
                KLog.e("latitude:" + latitude);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);

                startActivity(intent);
            }
        });
    }

    /*
    提交按钮点击
      */
    private void init_ok() {
        button_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                final String name_text = name.getText().toString();
                final String phone_text = phone.getText().toString();
                final String address_text = address.getText().toString();
                final String door_text = door.getText().toString();
                if (name_text.equals("")) {
                    Show_toast.showText(User_info.this, "用户名不能为空");
                    return;
                }
                String phone_zhenz = "^1\\d{10}$";
                if (phone_text.equals("")) {
                    Show_toast.showText(User_info.this, "手机号不能为空");
                    return;
                }
                if (!phone_text.matches(phone_zhenz)) {
                    Show_toast.showText(User_info.this, "手机号不正确");
                    return;
                }
                if (address_text.equals("")) {
                    Show_toast.showText(User_info.this, "地址不能为空");
                    return;
                }
                if (door_text.equals("")) {
                    Show_toast.showText(User_info.this, "门牌号不能为空");
                    return;
                }
                KLog.e("选择" + areaname + "," + AyiApplication.place);
                if (!getIntent().getStringExtra("flag_place").equals("voc")) {
                    if (!areaname.equals(AyiApplication.place)) {
                        Show_toast.showText(User_info.this, "请选择相应城市");
                        return;
                    }
                } else {
                    if (!areaname.equals(getIntent().getStringExtra("flag_place_dizhi"))) {
                        Show_toast.showText(User_info.this, "请选择相应城市");
                        return;
                    }
                }

                progressBar1.setVisibility(View.VISIBLE);
                if (!flag_shifouxiugai) {

                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    String url = RetrofitUtil.url_add_addresslist;
                    RequestParams requestParams = new RequestParams();
                    try {
                        requestParams.put("name", name_text);
                        requestParams.put("tel", phone_text);
                        requestParams.put("addr", address_text);
                        requestParams.put("door", door_text);
                        if (areaname != "") {
                            requestParams.put("areaname", areaname);
                        } else {
                            requestParams.put("areaname", AyiApplication.place);
                        }
                        requestParams.put("latitude", latitude);
                        requestParams.put("longitude", longitude);
                        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                        requestParams.put("token", AyiApplication.getInstance().accountService().token());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Debug_net.debug_tiaoshi("10086", e.getMessage(), e.toString() + "," + areaname + "," + AyiApplication.place + "," + latitude + "," + longitude, "新建地址参数的时候");
                    }
                    asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                System.out.println("url_add_addresslist" + response.toString());
                                if (response.getString("ret").equals("200") && response.getJSONObject("data").getString("status").equals("1")) {
                                    progressBar1.setVisibility(View.GONE);
                                    Intent intent = new Intent(User_info.this, User_info_before.class);
                                    startActivity(intent);
                                    Show_toast.showText(User_info.this, "添加地址成功");
                                    FileService service = new FileService(User_info.this);
                                    String info = "";
                                    if (areaname != "") {
                                        info = name_text + "~" + phone_text + "~" + address_text + "~" + door_text + "~" + latitude + "~" + longitude + "~" + areaname;
                                    } else {
                                        info = name_text + "~" + phone_text + "~" + address_text + "~" + door_text + "~" + latitude + "~" + longitude + "~" + AyiApplication.place;
                                    }
                                    try {
                                        service.saveToRom("user.txt", info);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        Show_toast.showText(User_info.this, "网络异常1000");
                                    }
                                } else {
                                    progressBar1.setVisibility(View.GONE);
                                    if (!response.getString("msg").equals("")) {
                                        Show_toast.showText(User_info.this, response.getString("msg"));
                                    } else {
                                        Show_toast.showText(User_info.this, "添加地址失败");
                                    }

                                }


                            } catch (JSONException e) {
                                progressBar1.setVisibility(View.GONE);
                                e.printStackTrace();
                                Debug_net.debug_tiaoshi("10087", e.toString(), response.toString() + "," + areaname, "url_add_addresslist接口访问中出错");
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            System.out.println(errorResponse.toString());
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            progressBar1.setVisibility(View.GONE);
                            Show_toast.showText(User_info.this, "网络繁忙，请重试。10013");
                        }
                    });


                } else {

                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    String url = RetrofitUtil.url_modify_addresslist;
                    RequestParams requestParams = new RequestParams();
                    try {

                        requestParams.put("adid", adid);
                        requestParams.put("name", name_text);
                        requestParams.put("tel", phone_text);
                        requestParams.put("addr", address_text);
                        requestParams.put("door", door_text);

                        if (areaname != "") {
                            requestParams.put("areaname", areaname);
                        } else {
                            requestParams.put("areaname", AyiApplication.place);
                        }
                        requestParams.put("latitude", latitude);
                        requestParams.put("longitude", longitude);
                        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                        requestParams.put("token", AyiApplication.getInstance().accountService().token());
                    } catch (Exception e) {
                        Debug_net.debug_tiaoshi("10088", e.toString(), areaname + "," + AyiApplication.place + "," + latitude + "," + longitude, "修改地址参数中出错");
                        e.printStackTrace();
                    }
                    asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                System.out.println("url_modif_addresslist" + response.toString());
                                if (response.getString("ret").equals("200") && response.getJSONObject("data").getString("status").equals("1")) {
                                    progressBar1.setVisibility(View.GONE);
                                    Intent intent = new Intent(User_info.this, User_info_before.class);
                                    startActivity(intent);
                                    FileService service = new FileService(User_info.this);
                                    String info = "";
                                    if (areaname != "") {
                                        info = name_text + "~" + phone_text + "~" + address_text + "~" + door_text + "~" + latitude + "~" + longitude + "~" + areaname;

                                    } else {
                                        info = name_text + "~" + phone_text + "~" + address_text + "~" + door_text + "~" + latitude + "~" + longitude + "~" + AyiApplication.place;

                                    }
                                    try {
                                        service.saveToRom("user.txt", info);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Show_toast.showText(User_info.this, "修改地址成功");
                                } else {
                                    progressBar1.setVisibility(View.GONE);
                                    if (!response.getString("msg").equals("")) {
                                        Show_toast.showText(User_info.this, response.getString("msg"));
                                    } else {
                                        Show_toast.showText(User_info.this, "修改地址失败");
                                    }
                                }


                            } catch (JSONException e) {
                                Debug_net.debug_tiaoshi("10089", e.toString(), areaname, "修改地址访问中出错");
                                progressBar1.setVisibility(View.GONE);
                                e.printStackTrace();
                            }


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            System.out.println(errorResponse.toString());
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            progressBar1.setVisibility(View.GONE);
                            Show_toast.showText(User_info.this, "网络繁忙，请重试。1016");
                        }
                    });

                }


            }
        });
    }


    /**
     * 再次进入这个界面，会调用只个方法
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        address.setText(intent.getStringExtra("place_name"));
        latitude = intent.getStringExtra("latitude");
        longitude = intent.getStringExtra("longitude");

        areaname = intent.getStringExtra("mibu_city");//修改后
        System.out.println("再次进入这个页面：" + intent.getStringExtra("place_name") + "," + intent.getStringExtra("latitude") + "," + intent.getStringExtra("longitude") + "," + areaname);
    }

    private boolean flag_shifouxiugai = false;

    private void init() {
        progressBar1 = findViewById(R.id.progressBar1);
        button_btn = findViewById(R.id.button_btn);
        select_address = findViewById(R.id.select_address);
        name = (TextView) findViewById(R.id.name);
        phone = (TextView) findViewById(R.id.phone);
        phone.setText(AyiApplication.getInstance().accountService().mobile());
        address = (TextView) findViewById(R.id.address);
        door = (TextView) findViewById(R.id.door);
        //要是由修改的界面进入，那么会传过来一些值，这些值在初始化的时候可以用来注入。
        if (getIntent().getStringExtra("name") != null) {
            adid = getIntent().getStringExtra("adid");
            name.setText(getIntent().getStringExtra("name"));
            phone.setText(getIntent().getStringExtra("phone"));
            address.setText(getIntent().getStringExtra("add1"));
            door.setText(getIntent().getStringExtra("add2"));
            latitude = getIntent().getStringExtra("lat");
            longitude = getIntent().getStringExtra("long");
            areaname = getIntent().getStringExtra("areaname");
            flag_shifouxiugai = true;
            System.out.println("修改进入这个页面" + latitude + "," + longitude + "," + areaname);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("添加地址这个");
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("添加地址这个");
        MobclickAgent.onResume(this);
    }
}
