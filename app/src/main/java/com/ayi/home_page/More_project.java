package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.LoginActivity;
import com.ayi.entity.Home_certem_entiy;
import com.ayi.retrofit.RetrofitUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2017/5/12.
 */

public class More_project extends Activity {
    private View top;
    private View back;
    private LinearLayout item_outer1;
    private LinearLayout item_outer2;
    private LinearLayout item_outer3;
    private LinearLayout item_outer4;
    List<Home_certem_entiy> list_certen_entiy = new ArrayList<>();//这个是预计的数据
    List<View> list_center_view = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.more_priject_view);
        init_get_center_icon();
        init_view();
        init_back();
    }

    private void init_center_onclick() {
        for (int i = 0; i < list_center_view.size(); i++) {
            final int a = i;
            list_center_view.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AyiApplication.getInstance().accountService().id().isEmpty() && AyiApplication.getInstance().accountService().token().isEmpty()) {
                        Intent intent = new Intent(More_project.this, LoginActivity.class);
                        startActivity(intent);
                        return;
                    } else {
                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                        String url = RetrofitUtil.url_get_info;
                        asyncHttpClient.setTimeout(20000);
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                        requestParams.put("token", AyiApplication.getInstance().accountService().token());
                        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    String ret = response.getString("ret");
                                    if (!ret.equals("200")) {
                                        Intent intent = new Intent(More_project.this, LoginActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Intent intent;
                                        if (list_certen_entiy.get(a).getType_code().equals("dg")) {
//                                              对公业务
                                            intent = new Intent(More_project.this, Business_appointment.class);
                                            startActivity(intent);
                                        } else if (list_certen_entiy.get(a).getType_code().equals("tc")) {
                                            intent = new Intent(More_project.this, Combo.class);
                                            intent.putExtra("title", list_certen_entiy.get(a).getTitle());
//                                            intent.putExtra("type_id", list_certen_entiy.get(a).getType_id());
                                            startActivity(intent);
                                        }
                                        switch (list_certen_entiy.get(a).getType_id()) {
                                            case "5":
                                            case "6":
                                            case "7":
                                            case "8":
                                                if (AyiApplication.getInstance().getCanvalet() == 1) {
                                                    intent = new Intent(More_project.this, Home_daike_guodu.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                } else {
                                                    intent = new Intent(More_project.this, Home_zdg_ok.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                }

                                                break;
                                            case "9":
                                            case "10":
                                            case "11":
                                                if (AyiApplication.getInstance().getCanvalet() == 1) {
                                                    intent = new Intent(More_project.this, Home_daike_guodu.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                } else {
                                                    intent = new Intent(More_project.this, Home_zdg_cq_ok.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                }

                                                break;
                                            case "12":
                                            case "13":
                                                if (AyiApplication.getInstance().getCanvalet() == 1) {
                                                    intent = new Intent(More_project.this, Home_daike_guodu.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                } else {
                                                    intent = new Intent(More_project.this, Home_baomu_ok.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                }

                                                break;
                                            case "14":
                                            case "15":
                                                if (AyiApplication.getInstance().getCanvalet() == 1) {
                                                    intent = new Intent(More_project.this, Home_daike_guodu.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                } else {
                                                    intent = new Intent(More_project.this, Home_yuesao_ok.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                }

                                                break;
                                            default:
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Intent intent = new Intent(More_project.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            });
        }

    }

    private void init_get_center_icon() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_center_icon;//测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("areaid", AyiApplication.area_id);
        requestParams.put("isshow", 1);
        KLog.e("areaid", AyiApplication.area_id);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    System.out.println(jsonObject.toString() + "中心图标");
                    if ("200".equals(jsonObject.getString("ret"))) {
                        JSONArray js = jsonObject.getJSONArray("data");
                        for (int i = 0; i < js.length(); i++) {
                            Home_certem_entiy home_certem_entiy = new Home_certem_entiy();
                            home_certem_entiy.setImg_url(js.getJSONObject(i).getString("type_icon"));
                            home_certem_entiy.setType_id(js.getJSONObject(i).getString("service_type_id"));
                            home_certem_entiy.setTitle(js.getJSONObject(i).getString("title"));
                            home_certem_entiy.setSortby(js.getJSONObject(i).getString("sortby"));
                            home_certem_entiy.setType_code(js.getJSONObject(i).getString("type_code"));
                            home_certem_entiy.setIndex_icon(js.getJSONObject(i).getString("index_icon"));
                            list_certen_entiy.add(home_certem_entiy);
                        }
                        init_layout();
                        init_center_onclick();
                    }
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

    private void init_layout() {
        for (int i = 0; i < list_certen_entiy.size(); i++) {
            if (i < 5) {
                View item_view = LayoutInflater.from(this).inflate(R.layout.center_view, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getIntent().getIntExtra("whith_rct", 0), LinearLayout.LayoutParams.WRAP_CONTENT);

                item_view.setLayoutParams(lp);

                TextView title = (TextView) item_view.findViewById(R.id.titlecenter);
                ImageView img = (ImageView) item_view.findViewById(R.id.imgcenter);
                ImageView icon_small = (ImageView) item_view.findViewById(R.id.icon_small);
                //图片为空
                if (!list_certen_entiy.get(i).getIndex_icon().equals("")) {
                    setimage_icon_small(list_certen_entiy.get(i).getIndex_icon(), icon_small);
                }
                title.setText(list_certen_entiy.get(i).getTitle());
                //图片为空
                if (list_certen_entiy.get(i).getImg_url().equals("")) {
                    setimage_icon(list_certen_entiy.get(i).getType_id(), img, list_certen_entiy.get(i).getType_code());

                } else {
                    ImageLoader.getInstance().displayImage(list_certen_entiy.get(i).getImg_url(), img);
                }
                item_outer1.addView(item_view);
                list_center_view.add(item_view);
                continue;
            }
            if (i < 10) {
                View item_view = LayoutInflater.from(this).inflate(R.layout.center_view, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getIntent().getIntExtra("whith_rct", 0), LinearLayout.LayoutParams.WRAP_CONTENT);

                item_view.setLayoutParams(lp);

                TextView title = (TextView) item_view.findViewById(R.id.titlecenter);
                ImageView img = (ImageView) item_view.findViewById(R.id.imgcenter);
                ImageView icon_small = (ImageView) item_view.findViewById(R.id.icon_small);
                title.setText(list_certen_entiy.get(i).getTitle());
                //图片为空
                if (!list_certen_entiy.get(i).getIndex_icon().equals("")) {
                    setimage_icon_small(list_certen_entiy.get(i).getIndex_icon(), icon_small);
                }
                //图片为空
                if (list_certen_entiy.get(i).getImg_url().equals("")) {
                    setimage_icon(list_certen_entiy.get(i).getType_id(), img, list_certen_entiy.get(i).getType_code());
                } else {
                    ImageLoader.getInstance().displayImage(list_certen_entiy.get(i).getImg_url(), img);
                }
                item_outer2.addView(item_view);
                list_center_view.add(item_view);
                continue;
            }
            if (i < 15) {
                View item_view = LayoutInflater.from(this).inflate(R.layout.center_view, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getIntent().getIntExtra("whith_rct", 0), LinearLayout.LayoutParams.WRAP_CONTENT);

                item_view.setLayoutParams(lp);

                TextView title = (TextView) item_view.findViewById(R.id.titlecenter);
                ImageView img = (ImageView) item_view.findViewById(R.id.imgcenter);
                ImageView icon_small = (ImageView) item_view.findViewById(R.id.icon_small);
                //图片为空
                if (!list_certen_entiy.get(i).getIndex_icon().equals("")) {
                    setimage_icon_small(list_certen_entiy.get(i).getIndex_icon(), icon_small);
                }
                title.setText(list_certen_entiy.get(i).getTitle());
                //图片为空
                if (list_certen_entiy.get(i).getImg_url().equals("")) {
                    setimage_icon(list_certen_entiy.get(i).getType_id(), img, list_certen_entiy.get(i).getType_code());
                } else {
                    ImageLoader.getInstance().displayImage(list_certen_entiy.get(i).getImg_url(), img);
                }
                item_outer3.addView(item_view);
                list_center_view.add(item_view);
                continue;
            }
            if (i < 20) {
                View item_view = LayoutInflater.from(this).inflate(R.layout.center_view, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(getIntent().getIntExtra("whith_rct", 0), LinearLayout.LayoutParams.WRAP_CONTENT);

                item_view.setLayoutParams(lp);

                TextView title = (TextView) item_view.findViewById(R.id.titlecenter);
                ImageView img = (ImageView) item_view.findViewById(R.id.imgcenter);
                ImageView icon_small = (ImageView) item_view.findViewById(R.id.icon_small);
                //图片为空
                if (!list_certen_entiy.get(i).getIndex_icon().equals("")) {
                    setimage_icon_small(list_certen_entiy.get(i).getIndex_icon(), icon_small);
                }
                title.setText(list_certen_entiy.get(i).getTitle());
                //图片为空
                if (list_certen_entiy.get(i).getImg_url().equals("")) {
                    setimage_icon(list_certen_entiy.get(i).getType_id(), img, list_certen_entiy.get(i).getType_code());

                } else {
                    ImageLoader.getInstance().displayImage(list_certen_entiy.get(i).getImg_url(), img);
                }
                item_outer4.addView(item_view);
                list_center_view.add(item_view);
                continue;
            }


        }
    }

    private void init_back() {
        back = top.findViewById(R.id.logreg_left);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init_view() {
        top = findViewById(R.id.top);
        item_outer1 = (LinearLayout) findViewById(R.id.item_outer1);//一个长条
        item_outer2 = (LinearLayout) findViewById(R.id.item_outer2);//二个长条
        item_outer3 = (LinearLayout) findViewById(R.id.item_outer3);//二个长条
        item_outer4 = (LinearLayout) findViewById(R.id.item_outer4);//二个长条
    }

    public void setimage_icon(String id, ImageView img, String type) {
        switch (id) {
            case "5":
                img.setBackgroundResource(R.mipmap.center_rcbj);
                break;
            case "6":
                img.setBackgroundResource(R.mipmap.center_qxyyj);
                break;
            case "7":
                img.setBackgroundResource(R.mipmap.center_cbl);
                break;
            case "8":
                img.setBackgroundResource(R.mipmap.center_kh);
                break;
            case "9":
                img.setBackgroundResource(R.mipmap.center_cpzdg);
                break;
            case "10":
                img.setBackgroundResource(R.mipmap.center_zf);
                break;
            case "11":
                img.setBackgroundResource(R.mipmap.center_bjjzf);
                break;
            case "12":
                img.setBackgroundResource(R.mipmap.center_shqj);
                break;
            case "13":
                img.setBackgroundResource(R.mipmap.center_zglr);
                break;
            case "14":
                img.setBackgroundResource(R.mipmap.center_ys);
                break;
            case "15":
                img.setBackgroundResource(R.mipmap.center_yes);
                break;
            default:
                if (type.equals("dg")) {
                    img.setBackgroundResource(R.mipmap.center_dg);
                } else if (type.equals("tc")) {
                    img.setBackgroundResource(R.mipmap.center_tc);
                }
                break;
        }
    }

    public void setimage_icon_small(String id, ImageView img) {
        switch (id) {
            case "1":
                img.setBackgroundResource(R.mipmap.a_01);
                break;
            case "2":
                img.setBackgroundResource(R.mipmap.a_02);
                break;
            case "3":
                img.setBackgroundResource(R.mipmap.a_03);
                break;
            case "4":
                img.setBackgroundResource(R.mipmap.a_04);
                break;
            case "5":
                img.setBackgroundResource(R.mipmap.a_05);
                break;
            case "6":
                img.setBackgroundResource(R.mipmap.a_06);
                break;
            case "7":
                img.setBackgroundResource(R.mipmap.a_07);
                break;
            case "8":
                img.setBackgroundResource(R.mipmap.a_08);
                break;
            case "9":
                img.setBackgroundResource(R.mipmap.b_01);
                break;
            case "10":
                img.setBackgroundResource(R.mipmap.b_02);
                break;
            case "11":
                img.setBackgroundResource(R.mipmap.b_03);
                break;
            case "12":
                img.setBackgroundResource(R.mipmap.b_04);
                break;
            case "13":
                img.setBackgroundResource(R.mipmap.b_05);
                break;
            case "14":
                img.setBackgroundResource(R.mipmap.b_06);
                break;
            case "15":
                img.setBackgroundResource(R.mipmap.b_07);
                break;
            case "16":
                img.setBackgroundResource(R.mipmap.b_08);
                break;
        }
    }
}
