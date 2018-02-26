package com.ayi.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.adapter.custom_spinner_adapter;
import com.ayi.adapter.custom_spinner_adapter2;
import com.ayi.adapter.more_city_adapter;
import com.ayi.entity.Place_item;
import com.ayi.entity.daily_net_info;
import com.ayi.entity.item_place_info;
import com.ayi.home_page.User_info_before;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.FileService;
import com.ayi.utils.Show_toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import simplezxing.activity.CaptureActivity;

/**
 * Created by Administrator on 2017/8/14.
 */

public class Voice_setting extends Activity {
    private static final int REQ_CODE_PERMISSION = 1;
    private View back;
    private View iwantsao;
    private EditText text_code;
    private View lianxidizhi;
    private View city_select;
    private TextView select_city_text;
    private TextView more_city;
    private String city_id = AyiApplication.area_id;
    private String city_name = AyiApplication.place;
    private String city_old_id = "";
    private View modfiy_but;//编辑but
    //    private boolean compile_flag = false;//是否编辑
    private View bootom_liner;
    private Button ok_btn;
    private TextView lianxidizhi_text;
    private TextView richangbaojie_text;
    private TextView qxyyj_text;
    private TextView cbl_text;
    private TextView kaihuang_text;
    private TextView city_select_text;
    private TextView robot_text;
    private ArrayAdapter<String> adapter_spinner1;
    private Spinner taox_spinner;
    private Spinner taox_spinner2;
    private Spinner taox_spinner3;
    private Spinner taox_spinner4;
    //    private Spinner taox_spinner5;
    private TextView pay_top_id;
    private String place_id;
    private String place_id_old;
    private String place_name_old;
    private List<daily_net_info> list_1;
    private List<daily_net_info> list_2;
    private List<daily_net_info> list_3;
    private List<daily_net_info> list_4;

    private String flag1;
    private String flag2;
    private String flag3;
    private String flag4;
    private View pay_way_big;
    private View yyj_big;
    private View cbl_big;
    private View rcbj_big;
    private View kh_big;
    private View progressBar1;
    private View calcel_but;
    private boolean flag_first = false;
    private View saomiao_img_id;
    private View img4;
    private View img1;
    private View img2;
    private View img3;
    private View img10;
    private View img11;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_setting_view);
        init();
        progressBar1.setVisibility(View.VISIBLE);
        init_modfiy();
        init_back();
        init_select_city();
        init_saomiao();
        init_dizhi();
        init_wangluo();
        init_bottom_but();
    }

    private boolean flag_1;
    private boolean flag_2;
    private boolean flag_3;
    private boolean flag_4;

    private void init_richangbaojie() {
        flag_1 = false;
        flag_2 = false;
        flag_3 = false;
        flag_4 = false;
        network_request("5");
        network_request("6");
        network_request("7");
        network_request("8");
    }

    private void init_bottom_but() {
        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String robot_sn = text_code.getText().toString();
                String area = select_city_text.getText().toString();
                String place = more_city.getText().toString();
                if (robot_sn.equals("")) {
                    Show_toast.showText(Voice_setting.this, "请输入序列号或扫描条形码");
                    return;
                }
                if (area.equals("")) {
                    Show_toast.showText(Voice_setting.this, "请选择城市");
                    return;
                }
                if (place.equals("")) {
                    Show_toast.showText(Voice_setting.this, "请选择联系地址");
                    return;
                }
                go_setting();
            }
        });
        calcel_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag_first) {
                    flag_first = false;
                    finish();
                } else {
                    init_wangluo();
//                    saomiao_img_id.setVisibility(View.GONE);
//                    modfiy_but.setVisibility(View.VISIBLE);
//                    bootom_liner.setVisibility(View.GONE);
//                    //修改颜色
//                    init_dandan();
//                    //使无用
//                    init_nouse();
                }
//                compile_flag = false;

            }
        });
    }

    private void init_nouse() {
        text_code.setEnabled(false);
        iwantsao.setEnabled(false);
        city_select.setEnabled(false);
        lianxidizhi.setEnabled(false);
        taox_spinner.setEnabled(false);
        taox_spinner2.setEnabled(false);
        taox_spinner3.setEnabled(false);
        taox_spinner4.setEnabled(false);
        pay_way_big.setEnabled(false);
    }

    private void init_use() {
        text_code.setEnabled(true);
        iwantsao.setEnabled(true);
        city_select.setEnabled(true);
        lianxidizhi.setEnabled(true);
        taox_spinner.setEnabled(true);
        taox_spinner2.setEnabled(true);
        taox_spinner3.setEnabled(true);
        taox_spinner4.setEnabled(true);
        pay_way_big.setEnabled(true);
    }

    public void init_dandan() {
        lianxidizhi_text.setTextColor(getResources().getColor(R.color.gray2));
        richangbaojie_text.setTextColor(getResources().getColor(R.color.gray2));
        qxyyj_text.setTextColor(getResources().getColor(R.color.gray2));
        cbl_text.setTextColor(getResources().getColor(R.color.gray2));
        robot_text.setTextColor(getResources().getColor(R.color.gray2));
        pay_top_id.setTextColor(getResources().getColor(R.color.gray2));
        kaihuang_text.setTextColor(getResources().getColor(R.color.gray2));
        city_select_text.setTextColor(getResources().getColor(R.color.gray2));
        select_city_text.setTextColor(getResources().getColor(R.color.gray2));
        more_city.setTextColor(getResources().getColor(R.color.gray2));

    }

    private void init_modfiy() {
        modfiy_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                compile_flag = true;
                modfiy_but.setVisibility(View.GONE);
                bootom_liner.setVisibility(View.VISIBLE);
                saomiao_img_id.setVisibility(View.VISIBLE);
                img11.setVisibility(View.VISIBLE);
                img4.setVisibility(View.VISIBLE);
                img1.setVisibility(View.VISIBLE);
                img2.setVisibility(View.VISIBLE);
                img3.setVisibility(View.VISIBLE);
                img10.setVisibility(View.VISIBLE);
                //修改颜色
                init_nongnong();
                init_use();
            }
        });
    }

    private void init_nongnong() {
        lianxidizhi_text.setTextColor(getResources().getColor(R.color.wenben_defalut));
        richangbaojie_text.setTextColor(getResources().getColor(R.color.wenben_defalut));
        qxyyj_text.setTextColor(getResources().getColor(R.color.wenben_defalut));
        cbl_text.setTextColor(getResources().getColor(R.color.wenben_defalut));
        robot_text.setTextColor(getResources().getColor(R.color.wenben_defalut));
        city_select_text.setTextColor(getResources().getColor(R.color.wenben_defalut));
        pay_top_id.setTextColor(getResources().getColor(R.color.wenben_defalut));
        kaihuang_text.setTextColor(getResources().getColor(R.color.wenben_defalut));
        city_select_text.setTextColor(getResources().getColor(R.color.wenben_defalut));
        select_city_text.setTextColor(getResources().getColor(R.color.wenben_defalut));
        more_city.setTextColor(getResources().getColor(R.color.wenben_defalut));
    }

    private void init() {
        saomiao_img_id = findViewById(R.id.saomiao_img_id);
        img11 = findViewById(R.id.img11);
        img4 = findViewById(R.id.img4);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img10 = findViewById(R.id.img10);
        calcel_but = findViewById(R.id.calcel_but);
        progressBar1 = findViewById(R.id.progressBar1);
        yyj_big = findViewById(R.id.yyj_big);
        cbl_big = findViewById(R.id.cbl_big);
        rcbj_big = findViewById(R.id.rcbj_big);
        kh_big = findViewById(R.id.kh_big);
        pay_way_big = findViewById(R.id.pay_way_big);
        kaihuang_text = (TextView) findViewById(R.id.kaihuang_text);
        pay_top_id = (TextView) findViewById(R.id.pay_top_id);
        taox_spinner = (Spinner) findViewById(R.id.taox_spinner);

        taox_spinner2 = (Spinner) findViewById(R.id.taox_spinner2);
        taox_spinner3 = (Spinner) findViewById(R.id.taox_spinner3);
        taox_spinner4 = (Spinner) findViewById(R.id.taox_spinner4);
//        taox_spinner5 = (Spinner) findViewById(R.id.taox_spinner5);
        robot_text = (TextView) findViewById(R.id.robot_text);
        city_select_text = (TextView) findViewById(R.id.city_select_text);
        richangbaojie_text = (TextView) findViewById(R.id.richangbaojie_text);
        qxyyj_text = (TextView) findViewById(R.id.qxyyj_text);
        cbl_text = (TextView) findViewById(R.id.cbl_text);
        lianxidizhi_text = (TextView) findViewById(R.id.lianxidizhi_text);
        ok_btn = (Button) findViewById(R.id.ok_btn);
        bootom_liner = findViewById(R.id.bootom_liner);
        modfiy_but = findViewById(R.id.modfiy_but);
        back = findViewById(R.id.top).findViewById(R.id.logreg_left);
        iwantsao = findViewById(R.id.iwantsao);
        text_code = (EditText) findViewById(R.id.text_code);
        text_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text_code.setSelection(s.length());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        city_select = findViewById(R.id.city_select);
        select_city_text = (TextView) findViewById(R.id.select_city_text);
        more_city = (TextView) findViewById(R.id.more_city);
        lianxidizhi = findViewById(R.id.lianxidizhi);
    }

    //日常保洁
    private void network_request(final String typeid) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_yuyue_info;
        RequestParams requestParams = new RequestParams();
        requestParams.put("typeid", typeid);
        requestParams.put("areaid", city_id);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    System.out.println("jsonObject" + jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("price");
                    List<daily_net_info> a = new ArrayList<>();
                    if (!typeid.equals("6")) {
                        try {
                            daily_net_info item1 = new daily_net_info();
                            item1.setSize("");
                            item1.setPrice("请点击选择");
                            item1.setDur("");
                            item1.setId("");

                            a.add(item1);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                daily_net_info item = new daily_net_info();
                                item.setSize(jsonArray.getJSONObject(i).getString("size"));
                                item.setPrice(jsonArray.getJSONObject(i).getString("price"));
                                item.setDur(jsonArray.getJSONObject(i).getString("dur"));
                                item.setId(jsonArray.getJSONObject(i).getString("id"));
//                        m[i]= String.valueOf(Html.fromHtml("每<font color=\"#e38918\">"+jsonArray.getJSONObject(i).getString("size")+"</font>为您提供服务"));
                                a.add(item);
                            }
                            if (typeid.equals("5")) {
                                if (jsonArray.length() == 0) {
                                    rcbj_big.setVisibility(View.GONE);
                                } else {
                                    rcbj_big.setVisibility(View.VISIBLE);
                                }
                            }
                            if (typeid.equals("7")) {
                                if (jsonArray.length() == 0) {
                                    cbl_big.setVisibility(View.GONE);
                                } else {
                                    cbl_big.setVisibility(View.VISIBLE);
                                }

                            }
                            if (typeid.equals("8")) {
                                if (jsonArray.length() == 0) {
                                    kh_big.setVisibility(View.GONE);
                                } else {
                                    kh_big.setVisibility(View.VISIBLE);
                                }

                            }
                        } catch (Exception e) {
                            if (typeid.equals("5")) {
                                rcbj_big.setVisibility(View.GONE);
                            }
                            if (typeid.equals("7")) {
                                cbl_big.setVisibility(View.GONE);
                            }
                            if (typeid.equals("8")) {
                                kh_big.setVisibility(View.GONE);
                            }
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            daily_net_info item1 = new daily_net_info();
                            item1.setSize("");
                            item1.setPrice("请点击选择");
                            item1.setDur("");
                            item1.setId("");

                            a.add(item1);
                            for (int i = 0; i < 5; i++) {
                                daily_net_info item = new daily_net_info();
                                item.setSize(String.valueOf(i + 1) + "台");
                                item.setPrice(String.valueOf(jsonArray.getJSONObject(0).getDouble("price") * (i + 1)));
                                item.setDur(String.valueOf(jsonArray.getJSONObject(0).getDouble("dur") * (i + 1)));
                                item.setId(jsonArray.getJSONObject(0).getString("id"));
                                a.add(item);
                            }
                            yyj_big.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            yyj_big.setVisibility(View.GONE);
                            e.printStackTrace();
                        }
                    }
                    init_4fun(typeid, a);


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

    private void init_4fun(String typeid, List<daily_net_info> a) {
        if (typeid.equals("5")) {
            list_1 = a;
            adapter_spinner1 = new custom_spinner_adapter(Voice_setting.this, android.R.layout.simple_spinner_item, a);
//            adapter_spinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            taox_spinner.setAdapter(adapter_spinner1);
            if (!flag_deault)
                try {
                    for (int i = 0; i < a.size(); i++) {
                        if (flag1.equals(a.get(i).getSize())) {
                            taox_spinner.setSelection(i);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    rcbj_big.setVisibility(View.GONE);
                }
            flag_1 = true;
            if (flag_1 && flag_2 && flag_3 && flag_4) {
                progressBar1.setVisibility(View.GONE);
            }
        }
        if (typeid.equals("6")) {
            list_2 = a;
            adapter_spinner1 = new custom_spinner_adapter2(Voice_setting.this, android.R.layout.simple_spinner_item, a);
//            adapter_spinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            taox_spinner4.setAdapter(adapter_spinner1);
            if (!flag_deault)
                try {
                    if (!flag2.equals("")) {
                        taox_spinner4.setSelection(Integer.parseInt(flag2));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    yyj_big.setVisibility(View.GONE);
                }
            flag_2 = true;
            if (flag_1 && flag_2 && flag_3 && flag_4) {
                progressBar1.setVisibility(View.GONE);
            }
        }
        if (typeid.equals("7")) {
            list_3 = a;
            adapter_spinner1 = new custom_spinner_adapter(Voice_setting.this, R.layout.xialakuang, a);
//            adapter_spinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//            adapter_spinner1.setDropDownViewResource(R.layout.xialakuang);

            taox_spinner2.setAdapter(adapter_spinner1);
            if (!flag_deault)
                try {
                    for (int i = 0; i < a.size(); i++) {
                        if (flag3.equals(a.get(i).getSize())) {
                            taox_spinner2.setSelection(i);

                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    cbl_big.setVisibility(View.GONE);
                }
            flag_3 = true;
            if (flag_1 && flag_2 && flag_3 && flag_4) {
                progressBar1.setVisibility(View.GONE);
            }
        }
        if (typeid.equals("8")) {
            list_4 = a;
            adapter_spinner1 = new custom_spinner_adapter(Voice_setting.this, android.R.layout.simple_spinner_item, a);
//            adapter_spinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            taox_spinner3.setAdapter(adapter_spinner1);
            if (!flag_deault)
                try {
                    for (int i = 0; i < a.size(); i++) {
                        if (flag4.equals(a.get(i).getSize())) {
                            taox_spinner3.setSelection(i);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
//                    kh_big.setVisibility(View.GONE);
                }
            flag_4 = true;
            if (flag_1 && flag_2 && flag_3 && flag_4) {
                progressBar1.setVisibility(View.GONE);
            }
        }
    }

    private void init_select_city() {
        city_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                go_city();
            }
        });
    }

    private List<Place_item> list;

    public void go_setting() {
        progressBar1.setVisibility(View.VISIBLE);
        JSONArray jsonArray = new JSONArray();
        try {

            if (rcbj_big.getVisibility() == View.VISIBLE)
                if (taox_spinner.getSelectedItemPosition() > 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("service_type_id", "5");
                    jsonObject.put("default_value", list_1.get(taox_spinner.getSelectedItemPosition()).getId());
                    jsonArray.put(jsonObject);
                }
            if (yyj_big.getVisibility() == View.VISIBLE)
                if (taox_spinner2.getSelectedItemPosition() > 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("service_type_id", "7");
                    jsonObject.put("default_value", list_3.get(taox_spinner2.getSelectedItemPosition()).getId());
                    jsonArray.put(jsonObject);
                }
            if (cbl_big.getVisibility() == View.VISIBLE)
                if (taox_spinner3.getSelectedItemPosition() > 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("service_type_id", "8");
                    jsonObject.put("default_value", list_4.get(taox_spinner3.getSelectedItemPosition()).getId());
                    jsonArray.put(jsonObject);
                }
            if (kh_big.getVisibility() == View.VISIBLE)
                if (taox_spinner4.getSelectedItemPosition() > 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("service_type_id", "6");
                    jsonObject.put("default_value", taox_spinner4.getSelectedItemPosition());
                    jsonArray.put(jsonObject);
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_setxiadanmoren;//测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("robot_sn", text_code.getText().toString());
        requestParams.put("areaid", city_id);
        requestParams.put("pay_type", "2");
        requestParams.put("addr_id", place_id);
        requestParams.put("defaultsets", jsonArray);
        KLog.e("requestParams" + requestParams);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                KLog.e(jsonObject.toString());
                try {
                    if (jsonObject.getInt("ret") == 200) {
                        //留意一下
                        flag1 = String.valueOf(taox_spinner.getSelectedItemPosition());
                        flag2 = String.valueOf(taox_spinner4.getSelectedItemPosition());
                        flag3 = String.valueOf(taox_spinner2.getSelectedItemPosition());
                        flag4 = String.valueOf(taox_spinner3.getSelectedItemPosition());
                        city_old_id = city_id;
                        place_name_old = more_city.getText().toString();
                        place_id_old = place_id;
                        Show_toast.showText(Voice_setting.this, "绑定成功");
//                        compile_flag = false;
                        modfiy_but.setVisibility(View.VISIBLE);
                        bootom_liner.setVisibility(View.GONE);
                        //修改颜色
                        init_dandan();
                        //使无用
                        init_nouse();
                    } else {
                        Show_toast.showText(Voice_setting.this, jsonObject.getString("msg"));
                    }
                    saomiao_img_id.setVisibility(View.GONE);
                    img11.setVisibility(View.GONE);
                    img4.setVisibility(View.GONE);
                    img1.setVisibility(View.GONE);
                    img2.setVisibility(View.GONE);
                    img3.setVisibility(View.GONE);
                    img10.setVisibility(View.GONE);
                    progressBar1.setVisibility(View.GONE);
                } catch (JSONException e) {
                    progressBar1.setVisibility(View.GONE);
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                KLog.e(throwable.toString());
                progressBar1.setVisibility(View.GONE);
            }
        });
    }

    public void go_city() {
        progressBar1.setVisibility(View.VISIBLE);
        if (list == null) {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.setTimeout(20000);
            String url = RetrofitUtil.url_place_list;
            asyncHttpClient.post(url, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                    super.onSuccess(statusCode, headers, jsonObject);
                    try {
                        System.out.println("jsonObject:" + jsonObject);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        list = new ArrayList<Place_item>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Place_item item = new Place_item();
                            item.setArea_id(jsonArray.getJSONObject(i).getString("area_id"));
                            item.setArea_name(jsonArray.getJSONObject(i).getString("area_name"));
                            list.add(item);
                        }
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Voice_setting.this);
                        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                progressBar1.setVisibility(View.GONE);
                            }
                        });
                        final android.app.AlertDialog alert = builder.create();
                        View view_city = LayoutInflater.from(Voice_setting.this).inflate(R.layout.more_xiadan_city, null);
                        final GridView my_list = (GridView) view_city.findViewById(R.id.my_list);
                        more_city_adapter adapter = new more_city_adapter(Voice_setting.this, list);
                        my_list.setAdapter(adapter);
                        my_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                city_id = list.get(position).getArea_id();
                                city_name = list.get(position).getArea_name();
                                select_city_text.setText(city_name);
                                if (!list.get(position).getArea_id().equals(city_old_id)) {
                                    flag_deault = true;
                                    init_mod_deault();
                                } else {
                                    flag_deault = false;
                                    init_mod_deault2();
                                }

                                alert.dismiss();
                            }
                        });
                        alert.setView(view_city);
                        alert.show();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                }
            });
        } else {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    progressBar1.setVisibility(View.GONE);
                }
            });
            final android.app.AlertDialog alert = builder.create();
            View view_city = LayoutInflater.from(this).inflate(R.layout.more_xiadan_city, null);
            GridView my_list = (GridView) view_city.findViewById(R.id.my_list);
            more_city_adapter adapter = new more_city_adapter(this, list);
            my_list.setAdapter(adapter);
            my_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    city_id = list.get(position).getArea_id();
                    city_name = list.get(position).getArea_name();
                    select_city_text.setText(city_name);
                    if (!list.get(position).getArea_id().equals(city_old_id)) {
                        flag_deault = true;
                        init_mod_deault();
                    } else {
                        flag_deault = false;
                        init_mod_deault2();
                    }

                    alert.dismiss();
                }
            });
            alert.setView(view_city);
            alert.show();

        }

    }

    private boolean flag_deault = false;

    public void init_mod_deault() {

        more_city.setText("");
        place_id = "";
        init_richangbaojie();
    }

    public void init_mod_deault2() {

        more_city.setText(place_name_old);
        place_id = place_id_old;
        init_richangbaojie();
    }

    private void init_wangluo() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_xiadanmoren;//
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                KLog.e(jsonObject.toString());
                try {

                    if (jsonObject.getInt("ret") == 200) {
                        saomiao_img_id.setVisibility(View.GONE);
                        img11.setVisibility(View.GONE);
                        img4.setVisibility(View.GONE);
                        img1.setVisibility(View.GONE);
                        img2.setVisibility(View.GONE);
                        img3.setVisibility(View.GONE);
                        img10.setVisibility(View.GONE);
                        flag_deault = false;
                        init_nouse();
                        init_dandan();
                        bootom_liner.setVisibility(View.GONE);
                        modfiy_but.setVisibility(View.VISIBLE);
                        text_code.setText(jsonObject.getJSONObject("data").getString("robot_sn"));
                        select_city_text.setText(jsonObject.getJSONObject("data").getString("areaname"));
                        city_name = jsonObject.getJSONObject("data").getString("areaname");
                        city_id = jsonObject.getJSONObject("data").getString("areaid");
                        city_old_id = jsonObject.getJSONObject("data").getString("areaid");
                        more_city.setText(jsonObject.getJSONObject("data").getString("addr"));

                        place_id = jsonObject.getJSONObject("data").getString("addr_id");
                        place_name_old = jsonObject.getJSONObject("data").getString("addr");
                        place_id_old = jsonObject.getJSONObject("data").getString("addr_id");
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("defaultsets");
                        if (jsonArray.length() == 0) {
                            flag_deault = true;
                        }
                        for (int i = 0; i < jsonArray.length(); i++) {
                            switch (jsonArray.getJSONObject(i).getString("service_type_id")) {
                                case "5":
                                    flag1 = jsonArray.getJSONObject(i).getString("size");
                                    break;
                                case "6":
                                    flag2 = jsonArray.getJSONObject(i).getString("default_value");
                                    break;
                                case "7":
                                    flag3 = jsonArray.getJSONObject(i).getString("size");
                                    break;
                                case "8":
                                    flag4 = jsonArray.getJSONObject(i).getString("size");
                                    break;
                            }
                        }
                        init_richangbaojie();
                    } else {

                        select_city_text.setText(city_name);
                        saomiao_img_id.setVisibility(View.VISIBLE);
                        img11.setVisibility(View.VISIBLE);
                        img4.setVisibility(View.VISIBLE);
                        img1.setVisibility(View.VISIBLE);
                        img2.setVisibility(View.VISIBLE);
                        img3.setVisibility(View.VISIBLE);
                        img10.setVisibility(View.VISIBLE);
                        flag_first = true;
                        flag_deault = true;
                        init_use();
                        init_nongnong();
                        bootom_liner.setVisibility(View.VISIBLE);
                        modfiy_but.setVisibility(View.GONE);
                        Show_toast.showText(Voice_setting.this, jsonObject.getString("msg"));
                        init_richangbaojie();
                    }
                } catch (JSONException e) {
                    finish();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                KLog.e(throwable.toString());
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getIntExtra("user_info", 0) == 1) {
            try {
                FileService fileService = new FileService(this);
                item_place_info user_info = fileService.read("user.txt");
                more_city.setText(user_info.getPlace() + user_info.getNum_place());
                place_id = intent.getStringExtra("id");

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    //选地址
    private void init_dizhi() {
        lianxidizhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Voice_setting.this, User_info_before.class);
                intent.putExtra("flag_place", "voc");
                intent.putExtra("flag", getIntent().getBooleanExtra("flag", false));
                intent.putExtra("flag_place_dizhi", city_name);
                startActivity(intent);
            }
        });
    }

    private void init_saomiao() {
        iwantsao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Android 6.0权限判断
                if (ContextCompat.checkSelfPermission(Voice_setting.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Voice_setting.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
                } else {
                    //跳转至扫描页面
                    startCaptureActivityForResult();
                }
            }
        });
    }

    /**
     * 跳转至扫描页面
     */
    private void startCaptureActivityForResult() {
        Intent intent = new Intent(Voice_setting.this, CaptureActivity.class);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

    private void init_back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //权限开启成功 跳转至扫描二维码页面
                    startCaptureActivityForResult();
                } else {
                    //权限开启失败 显示提示信息
                    showMissingPermissionDialog();
                }
            }
            break;
        }
    }

    /**
     * 显示提示信息
     */
    private void showMissingPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("当前应用缺少必要权限。请点击\"设置\"-\"权限\"-打开所需权限。");

        // 拒绝, 退出应用
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        finish();
                    }
                });

        builder.setPositiveButton("设置",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转至当前应用的权限设置页面
                        startAppSettings();
                    }
                });

        builder.setCancelable(false);

        builder.show();
    }

    /**
     * 启动应用的设置
     */
    private void startAppSettings() {
        Intent intent = new Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        text_code.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));  //or do sth
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            text_code.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        }
                        break;
                }
                break;
        }
    }
}
