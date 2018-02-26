package com.ayi.home_page;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.Voice_setting;
import com.ayi.adapter.addresslist_adapter;
import com.ayi.entity.item_place_info;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.FileService;
import com.ayi.zidingyi_view.Forhuitiao;
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

import static com.ayi.R.id.spinner;

/**
 * Created by Administrator on 2016/12/26.
 */

public class User_info_before extends Activity implements SwipeRefreshLayout.OnRefreshListener, Forhuitiao {
    private addresslist_adapter adapter;
    private ListView listview;
    List<item_place_info> list;
    public static View add_address_click;
    private View back;
    public static TextView guanli;
    public static boolean flag_guanli = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_view_before);
        init();
        init_click();//点击新增服务地址
        init_back();
        init_guanli();
    }

    @Override
    protected void onResume() {
        super.onResume();
        init_wangluo();
    }

    private void init_guanli() {

        guanli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView xx = (TextView) v;
                if (!flag_guanli) {
                    add_address_click.setVisibility(View.GONE);
                    listview.setOnItemClickListener(null);
                    xx.setText("完成");
                    for (int i = 0; i < list.size(); i++) {

                        list.get(i).setFlag(true);
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    add_address_click.setVisibility(View.VISIBLE);
                    doonejiekou();
                    xx.setText("管理");
                    for (int i = 0; i < list.size(); i++) {
                        list.get(i).setFlag(false);
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                    }
                }
                flag_guanli = !flag_guanli;
            }
        });
    }

    @Override
    public void doonejiekou() {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Intent intent = null;
                switch (getIntent().getStringExtra("flag_place")) {
                    case "zdg":
                        intent = new Intent(User_info_before.this, Home_zdg_ok.class);
                        break;
                    case "zdg_cq":
                        intent = new Intent(User_info_before.this, Home_zdg_cq_ok.class);
                        break;
                    case "bm":
                        intent = new Intent(User_info_before.this, Home_baomu_ok.class);
                        break;
                    case "yuesao":
                        intent = new Intent(User_info_before.this, Home_yuesao_ok.class);
                        break;
                    case "duigong":
                        intent = new Intent(User_info_before.this, Business_appointment.class);
                        break;
                    case "tc":
                        intent = new Intent(User_info_before.this, Business_appointment_tc.class);
                        break;
                    case "voc":
                        intent = new Intent(User_info_before.this, Voice_setting.class);
                        intent.putExtra("id", list.get(position).getId());
                        break;
                }

                if (getIntent().getBooleanExtra("flag", false)) {

                    if (list.get(position).getPhone().equals(AyiApplication.getInstance().accountService().mobile())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(User_info_before.this);
                        final AlertDialog alert = builder.create();
                        View view_daik = LayoutInflater.from(User_info_before.this).inflate(R.layout.calcel_daike, null);
                        //确定
                        final Intent finalIntent = intent;
                        view_daik.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                FileService service = new FileService(User_info_before.this);
                                String info = list.get(position).getName() + "~" + list.get(position).getPhone() + "~" + list.get(position).getPlace() + "~" + list.get(position).getDoor() + "~" + list.get(position).getLatitude() + "~" + list.get(position).getLongitide() + "~" + list.get(position).getAreaname();
                                try {
                                    System.out.println("saveToRom1" + list.get(position).getAreaname());
                                    service.saveToRom("user2.txt", info);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                KLog.d("service_type", info);
                                KLog.d("list_conntent", list.toString());
                                if (list.get(position).isFlag_city()) {
                                    finalIntent.putExtra("user_info", 1);
                                    startActivity(finalIntent);
                                }
                                alert.dismiss();
                            }
                        });
                        view_daik.findViewById(R.id.chonx).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                            }
                        });
                        alert.setView(view_daik);
                        alert.show();
                    } else {
                        FileService service = new FileService(User_info_before.this);
                        String info = list.get(position).getName() + "~" + list.get(position).getPhone() + "~" + list.get(position).getPlace() + "~" + list.get(position).getDoor() + "~" + list.get(position).getLatitude() + "~" + list.get(position).getLongitide() + "~" + list.get(position).getAreaname();
                        try {
                            System.out.println("saveToRom1" + list.get(position).getAreaname());
                            service.saveToRom("user2.txt", info);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        KLog.d("service_type", info);
                        KLog.d("list_conntent", list.toString());
                        if (list.get(position).isFlag_city()) {
                            intent.putExtra("user_info", 1);
                            startActivity(intent);
                        }
                    }

                } else {
                    FileService service = new FileService(User_info_before.this);
                    String info = list.get(position).getName() + "~" + list.get(position).getPhone() + "~" + list.get(position).getPlace() + "~" + list.get(position).getDoor() + "~" + list.get(position).getLatitude() + "~" + list.get(position).getLongitide() + "~" + list.get(position).getAreaname();
                    try {
                        System.out.println("saveToRom1" + list.get(position).getAreaname());
                        service.saveToRom("user.txt", info);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    KLog.d("service_type", info);
                    KLog.d("list_conntent", list.toString());
                    if (list.get(position).isFlag_city()) {
                        intent.putExtra("user_info", 1);
                        startActivity(intent);
                    }
                }

            }
        });
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
    public void onBackPressed() {

        try {

            if (flag_guanli) {
                add_address_click.setVisibility(View.VISIBLE);
                doonejiekou();
                guanli.setText("管理");
                for (int i = 0; i < list.size(); i++) {
                    list.get(i).setFlag(false);
                }
                adapter.notifyDataSetChanged();
                flag_guanli = !flag_guanli;
            } else {
                super.onBackPressed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init_click() {
        KLog.e("tiaoshi", "出现1");
        add_address_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.e("tiaoshi", "出现2");
                Intent intent = new Intent(User_info_before.this, User_info.class);
                intent.putExtra("flag_place", getIntent().getStringExtra("flag_place"));
                intent.putExtra("flag_place_dizhi", getIntent().getStringExtra("flag_place_dizhi"));
                startActivity(intent);
            }
        });
    }

    private void init() {
        guanli = (TextView) findViewById(R.id.guanli);
        back = findViewById(R.id.top).findViewById(R.id.logreg_left);
        list = new ArrayList<>();
        listview = (ListView) findViewById(R.id.listview);
        add_address_click = findViewById(R.id.add_address_click);

        doonejiekou();
    }

    //网络请求有的地址
    private void init_wangluo() {

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_addresslist;
        RequestParams requestParams = new RequestParams();
        requestParams.put("pagesize", 100);
        requestParams.put("currentpage", 1);
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        System.out.println("user_id" + AyiApplication.getInstance().accountService().id() + "token" + AyiApplication.getInstance().accountService().token());
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    System.out.println("citylist:" + response.toString() + "mainplace:" + AyiApplication.place);
                    if (response.getString("ret").equals("200")) {
                        list.clear();
                        JSONObject jsonObject = response.getJSONObject("data");
                        JSONArray jsonArray = jsonObject.getJSONArray("list");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            item_place_info item = new item_place_info();
                            item.setName(jsonArray.getJSONObject(i).getString("name"));
                            item.setPhone(jsonArray.getJSONObject(i).getString("tel"));
                            item.setPlace(jsonArray.getJSONObject(i).getString("addr"));
                            item.setDoor(jsonArray.getJSONObject(i).getString("door"));
                            item.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                            item.setLongitide(jsonArray.getJSONObject(i).getString("longitude"));
                            item.setId(jsonArray.getJSONObject(i).getString("id"));
                            if (jsonArray.getJSONObject(i).getString("areaname") != null && (!jsonArray.getJSONObject(i).getString("areaname").equals(""))) {
                                item.setAreaname(jsonArray.getJSONObject(i).getString("areaname"));
                            } else {
                                item.setAreaname(AyiApplication.place);
                            }
                            KLog.e("getIntent().getStringExtra(\"flag_place\")"+getIntent().getStringExtra("flag_place")+","+getIntent().getStringExtra("flag_place_dizhi"));
                            //加一个在小一机器人中使用
                            if (!getIntent().getStringExtra("flag_place").equals("voc")) {
                                //判断是否当前城市
                                if (!item.getAreaname().equals(AyiApplication.place)) {
                                    item.setFlag_city(false);
                                } else {
                                    item.setFlag_city(true);
                                }
                            } else {
                                //判断是否当前城市
                                if (!item.getAreaname().equals(getIntent().getStringExtra("flag_place_dizhi"))) {
                                    item.setFlag_city(false);
                                } else {
                                    item.setFlag_city(true);
                                }
                            }

                            list.add(item);
                        }

                        if (guanli.getText().toString().equals("完成")) {
                            for (int i = 0; i < list.size(); i++) {
                                list.get(i).setFlag(true);
                            }
                        }
                        if (list.size() > 0) {
                            List<item_place_info> list_zilei = new ArrayList<item_place_info>();
                            //正反排序，用来显示当前城市在先
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).isFlag_city()) {
                                    list_zilei.add(list.get(i));
                                }
                            }
                            for (int i = 0; i < list.size(); i++) {
                                if (!list.get(i).isFlag_city()) {
                                    list_zilei.add(list.get(i));
                                }
                            }
                            list.clear();
                            list = list_zilei;
                            adapter = new addresslist_adapter(User_info_before.this, list);
                            listview.setAdapter(adapter);
                            guanli.setEnabled(true);
                            //默认选首个
//                            FileService service = new FileService(User_info_before.this);
//                            String info = list.get(0).getName() + "~" + list.get(0).getPhone() + "~" + list.get(0).getPlace() + "~" + list.get(0).getDoor() + "~" + list.get(0).getLatitude() + "~" + list.get(0).getLongitide() + "~" + list.get(0).getAreaname();
//                            try {
//                                System.out.println("saveToRom2" + list.get(0).getAreaname());
//                                service.saveToRom("user.txt", info);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
                        } else {
                            FileService service = new FileService(User_info_before.this);
                            try {
                                service.saveToRom("user.txt", "");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            guanli.setEnabled(false);
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
    public void onRefresh() {
        //从一群中取出多少的方法
        find_ten();
    }

    private void find_ten() {

    }


}
