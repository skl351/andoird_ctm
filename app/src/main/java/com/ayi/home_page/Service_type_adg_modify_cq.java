package com.ayi.home_page;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.entity.Item_sale;
import com.ayi.entity.daily_net_info;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Data_time_cuo;
import com.ayi.utils.Show_toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

import static com.ayi.utils.Data_time_cuo.gettime2;

/**
 * Created by Administrator on 2016/10/14.
 */
public class Service_type_adg_modify_cq extends Activity {
    private ImageView image_coupon_z;
    private ImageView image_coupon_zhong;
    private ImageView image_coupon_w;
    private Spinner taox_spinner;
    private TextView taox_money;
    private View taox_view;
    private View taishu_view;
    private Spinner taishu_num;
    private TextView taishu_money;
    private TextView taishu_xiaoshi;
    private String type = "日常保洁（长期）";
    private String type_num = "9";
    private String price_total = "";
    private String size_total = "-1";
    private TextView breakfast_text;
    private TextView lunch_text;
    private TextView dinner_text;
    private TextView meici_time;
    private View danci;
    private View bushidanci;
    private View danci2;
    private View bushidanci2;
    private Spinner spin_shangwu;
    private List<daily_net_info> list_daily_net_info;
    private List<daily_net_info> list_daily_net_info1;
    private List<daily_net_info> list_daily_net_info2;
    private List<daily_net_info> list_daily_net_info3;
    private View make_food;
    private String service_time = "";
    boolean flag_wangluo = false;
    private View progressBar1;
    private View tiaofu_6_01;
    private View tiaofu_6_02;
    private View slae_vis;
    private TextView taox_money2_z;
    private TextView taox_money2_zhong;
    private TextView taox_money2_w;
    private View button_btn;

    private View center_content_cqbj;
    private View center_content_zf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_type_zdg_cq_view);
        init();
        button_btn.setEnabled(false);
        init_ok();
        init_food();

        switch (type_num) {

            case "9":
                network_request(type_num, getIntent().getIntExtra("biaozhi1", 0));
                taox_view.setVisibility(View.VISIBLE);
                taishu_view.setVisibility(View.GONE);
                danci.setVisibility(View.GONE);
                bushidanci.setVisibility(View.VISIBLE);
                danci2.setVisibility(View.GONE);
                bushidanci2.setVisibility(View.VISIBLE);
                make_food.setVisibility(View.GONE);
                shijianduan.setVisibility(View.GONE);
                center_content_cqbj.setVisibility(View.VISIBLE);
                break;
            case "10":
                network_request2(type_num, getIntent().getStringExtra("biaozhi3"));
                taox_view.setVisibility(View.GONE);
                taishu_view.setVisibility(View.VISIBLE);
                make_food.setVisibility(View.VISIBLE);
                shijianduan.setVisibility(View.GONE);
                center_content_zf.setVisibility(View.VISIBLE);
                break;
            case "11":
                network_request3(type_num, getIntent().getIntExtra("biaozhi1", 0), getIntent().getIntExtra("biaozhi2", 0));
                taox_view.setVisibility(View.VISIBLE);
                taishu_view.setVisibility(View.GONE);
                danci.setVisibility(View.VISIBLE);
                bushidanci.setVisibility(View.GONE);
                danci2.setVisibility(View.VISIBLE);
                bushidanci2.setVisibility(View.GONE);
                make_food.setVisibility(View.GONE);
                shijianduan.setVisibility(View.VISIBLE);
                break;
        }
        init_back();
    }

    private void init_back() {
        findViewById(R.id.top).findViewById(R.id.logreg_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init_food() {
        for (int i = 0; i < list_food.size(); i++) {
            list_food.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.isSelected()) {
                        v.setSelected(false);
                    } else {
                        v.setSelected(true);
                    }

                }
            });
        }
    }

    String sale_price = "1";

    private void init_ok() {

        button_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!flag_wangluo) {
                    return;
                }

                if (type_num.equals("10")) {
                    //做饭
                    size_total = "-1";
                    double pr = 0;
                    String service_time2 = "";
                    if (flag_sale) {
                        sale_price = item_sale.getSale();
                        for (int i = 0; i < list_food.size(); i++) {
                            if (list_food.get(i).isSelected()) {
                                service_time2 = service_time2 + i + "|";
                                if (i == 0) {
                                    pr = pr + Double.parseDouble(taox_money2_z.getText().toString());
                                } else if (i == 1) {
                                    pr = pr + Double.parseDouble(taox_money2_zhong.getText().toString());
                                } else {
                                    pr = pr + Double.parseDouble(taox_money2_w.getText().toString());
                                }
                            }
                        }
                    } else {
                        for (int i = 0; i < list_food.size(); i++) {
                            if (list_food.get(i).isSelected()) {
                                service_time2 = service_time2 + i + "|";

                                if (i == 0) {
                                    pr = pr + Double.parseDouble(breakfast_text.getText().toString());
                                } else if (i == 1) {
                                    pr = pr + Double.parseDouble(lunch_text.getText().toString());
                                } else {
                                    pr = pr + Double.parseDouble(dinner_text.getText().toString());
                                }
                            }
                        }
                    }

                    if (service_time2.equals("")) {
                        Toast.makeText(Service_type_adg_modify_cq.this, "请选择时间段", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        price_total = "" + pr;
                        service_time = service_time2.substring(0, service_time2.length() - 1);
                    }

                } else if (type_num.equals("9")) {
                    if (flag_sale) {
                        sale_price = item_sale.getSale();
                        price_total = taox_money2.getText().toString();
                    } else {
                        price_total = taox_money.getText().toString();
                    }

                    service_time = "-1";
                    size_total = list_daily_net_info.get(taox_spinner.getSelectedItemPosition()).getSize();
                } else {//11
                    service_time = "" + spin_shangwu.getSelectedItemPosition();
                    switch (shijianduan_status) {
                        case "0":
                            size_total = list_daily_net_info1.get(taox_spinner.getSelectedItemPosition()).getSize();
                            break;
                        case "1":
                            size_total = list_daily_net_info2.get(taox_spinner.getSelectedItemPosition()).getSize();
                            break;
                        case "2":
                            size_total = list_daily_net_info3.get(taox_spinner.getSelectedItemPosition()).getSize();
                            break;
                    }
                    if (flag_sale) {
                        sale_price = item_sale.getSale();
                        price_total = taox_money2.getText().toString();
                    } else {
                        price_total = taox_money.getText().toString();
                    }

                }


                Intent intent = new Intent(Service_type_adg_modify_cq.this, Home_zdg_cq_ok.class);
                System.out.println("biaozhi" + taox_spinner.getSelectedItemPosition() + "," + spin_shangwu.getSelectedItemPosition() + service_time);
                intent.putExtra("biaozhi1", taox_spinner.getSelectedItemPosition());
                intent.putExtra("biaozhi2", spin_shangwu.getSelectedItemPosition());
                intent.putExtra("biaozhi3", service_time);//早中晚的
                intent.putExtra("price_total", price_total);
                intent.putExtra("size_total", size_total);
                intent.putExtra("sale", sale_price);
                intent.putExtra("service_time", service_time);
                intent.putExtra("meici_time", meici_time.getText().toString().split("\\.")[0]);
                KLog.d("service_type", type_num + "," + type + "," + price_total + "," + size_total + "," + service_time + "-" + sale_price);
                startActivity(intent);

            }
        });
    }


    /*
    以下8个是类型点击的视图
     */
    /*
    以下8个是类型点击的视图
     */
    private View select1;
    private View select2;
    private View select3;
    private View breakfast1;
    private View lunch1;
    private View dinner1;
    private TextView meici_time2;

    private List<View> list_food;
    private View shijianduan;

    private void init() {
        center_content_cqbj = findViewById(R.id.center_content_cqbj);
        center_content_zf = findViewById(R.id.center_content_zf);
        type = getIntent().getStringExtra("type");
        type_num = getIntent().getStringExtra("type_num");

        slae_vis_zao = findViewById(R.id.slae_vis_zao);
        slae_vis_zhong = findViewById(R.id.slae_vis_zhong);
        slae_vis_wan = findViewById(R.id.slae_vis_wan);
        button_btn = findViewById(R.id.button_btn);
        image_coupon_z = (ImageView) findViewById(R.id.image_coupon_z);
        image_coupon_zhong = (ImageView) findViewById(R.id.image_coupon_zhong);
        image_coupon_w = (ImageView) findViewById(R.id.image_coupon_w);
        taox_money2_z = (TextView) findViewById(R.id.taox_money2_z);
        taox_money2_z.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        taox_money2_zhong = (TextView) findViewById(R.id.taox_money2_zhong);
        taox_money2_zhong.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        taox_money2_w = (TextView) findViewById(R.id.taox_money2_w);
        taox_money2_w.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        meici_time2 = (TextView) findViewById(R.id.meici_time2);
        image_coupon = findViewById(R.id.image_coupon);
//        image_coupon2= findViewById(R.id.image_coupon2);
        df = new DecimalFormat("######0.00");

        slae_vis = findViewById(R.id.slae_vis);
        tiaofu_6_02 = findViewById(R.id.tiaofu_6_02);
        tiaofu_6_01 = findViewById(R.id.tiaofu_6_01);
        if (Build.VERSION.SDK_INT >= 23) {
            tiaofu_6_02.setVisibility(View.VISIBLE);
            tiaofu_6_01.setVisibility(View.VISIBLE);
        }
        progressBar1 = findViewById(R.id.progressBar1);
        shijianduan = findViewById(R.id.shijianduan);
        spin_shangwu = (Spinner) findViewById(R.id.spin_shangwu);
        list_food = new ArrayList<>();
        breakfast1 = findViewById(R.id.breakfast1);
        lunch1 = findViewById(R.id.lunch2);
        dinner1 = findViewById(R.id.dinner2);
        list_food.add(breakfast1);
        list_food.add(lunch1);
        list_food.add(dinner1);
        make_food = findViewById(R.id.make_food);
        breakfast_text = (TextView) findViewById(R.id.breakfast_text);
        lunch_text = (TextView) findViewById(R.id.lunch_text);
        dinner_text = (TextView) findViewById(R.id.dinner_text);
        danci = findViewById(R.id.danci);
        bushidanci = findViewById(R.id.bushidanci);
        danci2 = findViewById(R.id.danci2);
        bushidanci2 = findViewById(R.id.bushidanci2);
        meici_time = (TextView) findViewById(R.id.meici_time);
        taishu_num = (Spinner) findViewById(R.id.taishu_num);
        taishu_money = (TextView) findViewById(R.id.taishu_money);
        taishu_xiaoshi = (TextView) findViewById(R.id.taishu_xiaoshi);
        taox_view = findViewById(R.id.taox_view);
        taishu_view = findViewById(R.id.taishu_view);
        taox_money = (TextView) findViewById(R.id.taox_money);
        taox_money2 = (TextView) findViewById(R.id.taox_money2);
        taox_money2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        taox_spinner = (Spinner) findViewById(R.id.taox_spinner);
        list_daily_net_info = new ArrayList<>();
        list_daily_net_info1 = new ArrayList<>();
        list_daily_net_info2 = new ArrayList<>();
        list_daily_net_info3 = new ArrayList<>();


    }

    private ArrayAdapter<String> adapter_spinner1;

    /*
    按下第一个的时候--日常保洁
     */
    private void network_request(String typeid, final int biaozhi1) {
        progressBar1.setVisibility(View.VISIBLE);
        System.out.println("biaozhi1--" + biaozhi1);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_yuyue_info;
        RequestParams requestParams = new RequestParams();
        requestParams.put("typeid", typeid);
        requestParams.put("areaid", AyiApplication.area_id);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    progressBar1.setVisibility(View.GONE);
                    button_btn.setEnabled(true);
                    flag_wangluo = true;
                    System.out.println("这个是zfg_cq" + jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("price");
                    list_daily_net_info.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        daily_net_info item = new daily_net_info();
                        item.setSize(jsonArray.getJSONObject(i).getString("size"));
                        item.setPrice(jsonArray.getJSONObject(i).getString("price"));
                        item.setDur(jsonArray.getJSONObject(i).getString("dur"));
                        list_daily_net_info.add(item);
                    }
                    String[] m = new String[list_daily_net_info.size()];
                    for (int i = 0; i < list_daily_net_info.size(); i++) {
                        m[i] = list_daily_net_info.get(i).getSize();
                    }
                    adapter_spinner1 = new ArrayAdapter<String>(Service_type_adg_modify_cq.this, android.R.layout.simple_spinner_item, m);
                    adapter_spinner1.setDropDownViewResource(android.R.layout.simple_spinner_item);
                    taox_spinner.setAdapter(adapter_spinner1);

                    final String flag = jsonObject.getJSONObject("data").getString("Is_sale");
                    if (flag.equals("1")) {
                        flag_sale = true;
                        JSONObject jv = jsonObject.getJSONObject("data").getJSONArray("sale").getJSONObject(0);//价钱相关
                        item_sale.setTitle(jv.getString("title"));
                        item_sale.setRemark(jv.getString("remark"));
                        item_sale.setData_end(gettime2(jv.getString("data_end")));
                        item_sale.setData_start(gettime2(jv.getString("data_start")));
                        item_sale.setSale(jv.getString("sale"));
                        sale_price = item_sale.getSale();
                        slae_vis.setVisibility(View.VISIBLE);
                        taox_money.setText("" + df.format(Double.parseDouble(list_daily_net_info.get(0).getPrice()) * Double.parseDouble(item_sale.getSale())));
                        taox_money2.setText(list_daily_net_info.get(0).getPrice());

                        if (Double.parseDouble(jv.getString("sale")) > 1) {
                            init_zhang1();
                        } else if (Double.parseDouble(jv.getString("sale")) < 1) {
                            init_hui1();
                        }


                    } else {
                        flag_sale = false;
                        slae_vis.setVisibility(View.GONE);
                        taox_money.setText(list_daily_net_info.get(0).getPrice());

                    }

                    meici_time.setText(list_daily_net_info.get(0).getDur());
                    meici_time2.setText(list_daily_net_info.get(0).getDur());
                    taox_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (flag.equals("1")) {
                                taox_money.setText("" + df.format(Double.parseDouble(list_daily_net_info.get(position).getPrice()) * Double.parseDouble(item_sale.getSale())));
                                taox_money2.setText(list_daily_net_info.get(position).getPrice());
                            } else {
                                taox_money.setText(list_daily_net_info.get(position).getPrice());
                            }

                            meici_time.setText(list_daily_net_info.get(position).getDur());
                            meici_time2.setText(list_daily_net_info.get(position).getDur());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    if (biaozhi1 != -1) {
                        taox_spinner.setSelection(biaozhi1);
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

    private View slae_vis_zao;
    private View slae_vis_zhong;
    private View slae_vis_wan;

    /*
   按下第二个的时候--做饭
    */
    private void network_request2(String typeid, final String biaozhi3) {
        progressBar1.setVisibility(View.VISIBLE);
        System.out.println("biaozhi3--" + biaozhi3);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_yuyue_info;
        RequestParams requestParams = new RequestParams();
        requestParams.put("typeid", typeid);
        requestParams.put("areaid", AyiApplication.area_id);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    progressBar1.setVisibility(View.GONE);
                    button_btn.setEnabled(true);
                    flag_wangluo = true;
                    System.out.println(jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("price");

                    final String flag = jsonObject.getJSONObject("data").getString("Is_sale");
                    if (flag.equals("1")) {
                        slae_vis_zao.setVisibility(View.VISIBLE);
                        slae_vis_zhong.setVisibility(View.VISIBLE);
                        slae_vis_wan.setVisibility(View.VISIBLE);
                        flag_sale = true;
                        JSONObject jv = jsonObject.getJSONObject("data").getJSONArray("sale").getJSONObject(0);//价钱相关
                        item_sale.setTitle(jv.getString("title"));
                        item_sale.setRemark(jv.getString("remark"));
                        item_sale.setData_end(gettime2(jv.getString("data_end")));
                        item_sale.setData_start(gettime2(jv.getString("data_start")));
                        item_sale.setSale(jv.getString("sale"));
                        sale_price = item_sale.getSale();
                        if (Double.parseDouble(jv.getString("sale")) > 1) {
                            image_coupon_z.setBackgroundResource(R.mipmap.zhangjia_icon);
                            image_coupon_z.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println("点击了z11一下");
                                    //点击后出现一些方法。待续
                                    AlertDialog alert;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Service_type_adg_modify_cq.this);
                                    alert = builder.create();
                                    View view = LayoutInflater.from(Service_type_adg_modify_cq.this).inflate(R.layout.zhang_saying, null);
                                    alert.setView(view);
                                    TextView time = (TextView) view.findViewById(R.id.coupon_time);
                                    TextView title = (TextView) view.findViewById(R.id.coupon_title);
                                    TextView saying = (TextView) view.findViewById(R.id.coupon_saying);
                                    TextView sale = (TextView) view.findViewById(R.id.coupon_zhekou);
                                    time.setText(item_sale.getData_start() + "～" + item_sale.getData_end());
                                    title.setText(item_sale.getTitle());
                                    saying.setText(item_sale.getRemark());
                                    sale.setText(String.valueOf(Double.parseDouble(df.format((Double.parseDouble(item_sale.getSale()) - 1))) * 100) + "%");
                                    alert.show();
                                }
                            });
                            image_coupon_w.setBackgroundResource(R.mipmap.zhangjia_icon);
                            image_coupon_w.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println("点击了z11一下");
                                    //点击后出现一些方法。待续
                                    AlertDialog alert;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Service_type_adg_modify_cq.this);
                                    alert = builder.create();
                                    View view = LayoutInflater.from(Service_type_adg_modify_cq.this).inflate(R.layout.zhang_saying, null);
                                    alert.setView(view);
                                    TextView time = (TextView) view.findViewById(R.id.coupon_time);
                                    TextView title = (TextView) view.findViewById(R.id.coupon_title);
                                    TextView saying = (TextView) view.findViewById(R.id.coupon_saying);
                                    TextView sale = (TextView) view.findViewById(R.id.coupon_zhekou);
                                    time.setText(item_sale.getData_start() + "～" + item_sale.getData_end());
                                    title.setText(item_sale.getTitle());
                                    saying.setText(item_sale.getRemark());
                                    sale.setText(String.valueOf(Double.parseDouble(df.format((Double.parseDouble(item_sale.getSale()) - 1))) * 100) + "%");
                                    alert.show();
                                }
                            });
                            image_coupon_zhong.setBackgroundResource(R.mipmap.zhangjia_icon);
                            image_coupon_zhong.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println("点击了z11一下");
                                    //点击后出现一些方法。待续
                                    AlertDialog alert;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Service_type_adg_modify_cq.this);
                                    alert = builder.create();
                                    View view = LayoutInflater.from(Service_type_adg_modify_cq.this).inflate(R.layout.zhang_saying, null);
                                    alert.setView(view);
                                    TextView time = (TextView) view.findViewById(R.id.coupon_time);
                                    TextView title = (TextView) view.findViewById(R.id.coupon_title);
                                    TextView saying = (TextView) view.findViewById(R.id.coupon_saying);
                                    TextView sale = (TextView) view.findViewById(R.id.coupon_zhekou);
                                    time.setText(item_sale.getData_start() + "～" + item_sale.getData_end());
                                    title.setText(item_sale.getTitle());
                                    saying.setText(item_sale.getRemark());
                                    sale.setText(String.valueOf(Double.parseDouble(df.format((Double.parseDouble(item_sale.getSale()) - 1))) * 100) + "%");
                                    alert.show();
                                }
                            });

                        } else {
                            image_coupon_z.setBackgroundResource(R.mipmap.youhuide_icon);
                            image_coupon_z.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println("点击了h11一下");
                                    //点击后出现一些方法。待续
                                    AlertDialog alert;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Service_type_adg_modify_cq.this);
                                    alert = builder.create();
                                    View view = LayoutInflater.from(Service_type_adg_modify_cq.this).inflate(R.layout.coupon_saying, null);
                                    alert.setView(view);
                                    TextView time = (TextView) view.findViewById(R.id.coupon_time);
                                    TextView title = (TextView) view.findViewById(R.id.coupon_title);
                                    TextView saying = (TextView) view.findViewById(R.id.coupon_saying);
                                    TextView sale = (TextView) view.findViewById(R.id.coupon_zhekou);
                                    time.setText(item_sale.getData_start() + "～" + item_sale.getData_end());
                                    title.setText(item_sale.getTitle());
                                    saying.setText(item_sale.getRemark());
                                    sale.setText((Double.parseDouble(item_sale.getSale()) * 10) + "折");
                                    alert.show();

                                }
                            });
                            image_coupon_w.setBackgroundResource(R.mipmap.youhuide_icon);
                            image_coupon_w.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println("点击了h11一下");
                                    //点击后出现一些方法。待续
                                    AlertDialog alert;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Service_type_adg_modify_cq.this);
                                    alert = builder.create();
                                    View view = LayoutInflater.from(Service_type_adg_modify_cq.this).inflate(R.layout.coupon_saying, null);
                                    alert.setView(view);
                                    TextView time = (TextView) view.findViewById(R.id.coupon_time);
                                    TextView title = (TextView) view.findViewById(R.id.coupon_title);
                                    TextView saying = (TextView) view.findViewById(R.id.coupon_saying);
                                    TextView sale = (TextView) view.findViewById(R.id.coupon_zhekou);
                                    time.setText(item_sale.getData_start() + "～" + item_sale.getData_end());
                                    title.setText(item_sale.getTitle());
                                    saying.setText(item_sale.getRemark());
                                    sale.setText((Double.parseDouble(item_sale.getSale()) * 10) + "折");
                                    alert.show();

                                }
                            });
                            image_coupon_zhong.setBackgroundResource(R.mipmap.youhuide_icon);
                            image_coupon_zhong.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println("点击了h11一下");
                                    //点击后出现一些方法。待续
                                    AlertDialog alert;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Service_type_adg_modify_cq.this);
                                    alert = builder.create();
                                    View view = LayoutInflater.from(Service_type_adg_modify_cq.this).inflate(R.layout.coupon_saying, null);
                                    alert.setView(view);
                                    TextView time = (TextView) view.findViewById(R.id.coupon_time);
                                    TextView title = (TextView) view.findViewById(R.id.coupon_title);
                                    TextView saying = (TextView) view.findViewById(R.id.coupon_saying);
                                    TextView sale = (TextView) view.findViewById(R.id.coupon_zhekou);
                                    time.setText(item_sale.getData_start() + "～" + item_sale.getData_end());
                                    title.setText(item_sale.getTitle());
                                    saying.setText(item_sale.getRemark());
                                    sale.setText((Double.parseDouble(item_sale.getSale()) * 10) + "折");
                                    alert.show();

                                }
                            });
                        }

                        List<String> list_str=new ArrayList<String>();
                        list_str.add("");
                        list_str.add("");
                        list_str.add("");
                        for (int i=0;i<jsonArray.length();i++){
                            if (jsonArray.getJSONObject(i).getInt("service_time")==0){
                                if (list_str.get(0).equals("")){
                                    breakfast_text.setText("" + df.format(Double.parseDouble(jsonArray.getJSONObject(i).getString("price")) * Double.parseDouble(item_sale.getSale())));
                                    taox_money2_z.setText(jsonArray.getJSONObject(i).getString("price"));
                                    list_str.set(0,"1");
                                }

                            }
                            if (jsonArray.getJSONObject(i).getInt("service_time")==1){
                                if (list_str.get(1).equals("")){
                                    lunch_text.setText("" + df.format(Double.parseDouble(jsonArray.getJSONObject(i).getString("price")) * Double.parseDouble(item_sale.getSale())));
                                    taox_money2_zhong.setText(jsonArray.getJSONObject(i).getString("price"));
                                    list_str.set(1,"1");
                                }
                            }
                            if (jsonArray.getJSONObject(i).getInt("service_time")==2){
                                if (list_str.get(2).equals("")){
                                    dinner_text.setText("" + df.format(Double.parseDouble(jsonArray.getJSONObject(i).getString("price")) * Double.parseDouble(item_sale.getSale())));
                                    taox_money2_w.setText(jsonArray.getJSONObject(i).getString("price"));
                                    list_str.set(2,"1");
                                }


                            }
                        }
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            switch (jsonArray.getJSONObject(i).getInt("service_time")) {
//                                case 0:
//                                    breakfast_text.setText("" + df.format(Double.parseDouble(jsonArray.getJSONObject(i).getString("price")) * Double.parseDouble(item_sale.getSale())));
//                                    taox_money2_z.setText(jsonArray.getJSONObject(i).getString("price"));
//                                    break;
//                                case 1:
//                                    lunch_text.setText("" + df.format(Double.parseDouble(jsonArray.getJSONObject(i).getString("price")) * Double.parseDouble(item_sale.getSale())));
//                                    taox_money2_zhong.setText(jsonArray.getJSONObject(i).getString("price"));
//                                    break;
//                                case 2:
//                                    dinner_text.setText("" + df.format(Double.parseDouble(jsonArray.getJSONObject(i).getString("price")) * Double.parseDouble(item_sale.getSale())));
//                                    taox_money2_w.setText(jsonArray.getJSONObject(i).getString("price"));
//                                    break;
//                            }
//
//                        }

                    } else {
                        slae_vis_zao.setVisibility(View.GONE);
                        slae_vis_zhong.setVisibility(View.GONE);
                        slae_vis_wan.setVisibility(View.GONE);

//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            switch (jsonArray.getJSONObject(i).getInt("service_time")) {
//                                case 0:
//                                    breakfast_text.setText(jsonArray.getJSONObject(i).getString("price"));
//                                    break;
//                                case 1:
//                                    lunch_text.setText(jsonArray.getJSONObject(i).getString("price"));
//                                    break;
//                                case 2:
//                                    dinner_text.setText(jsonArray.getJSONObject(i).getString("price"));
//                                    break;
//                            }
//
//                        }
                        List<String> list_str=new ArrayList<String>();
                        list_str.add("");
                        list_str.add("");
                        list_str.add("");
                        for (int i=0;i<jsonArray.length();i++){
                            if (jsonArray.getJSONObject(i).getInt("service_time")==0){
                                if (list_str.get(0).equals("")){
                                    breakfast_text.setText(jsonArray.getJSONObject(i).getString("price"));
                                    list_str.set(0,"1");
                                }
                            }
                            if (jsonArray.getJSONObject(i).getInt("service_time")==1){
                                if (list_str.get(1).equals("")){
                                    lunch_text.setText(jsonArray.getJSONObject(i).getString("price"));
                                    list_str.set(1,"1");
                                }
                            }
                            if (jsonArray.getJSONObject(i).getInt("service_time")==2){
                                if (list_str.get(2).equals("")){
                                    dinner_text.setText(jsonArray.getJSONObject(i).getString("price"));
                                    list_str.set(2,"1");
                                }
                            }
                        }
                    }

                    if (!biaozhi3.equals("-1")) {
                        String[] xx = biaozhi3.split("\\|");
                        for (int i = 0; i < xx.length; i++) {
                            switch (xx[i]) {
                                case "0":
                                    list_food.get(0).setSelected(true);
                                    break;
                                case "1":
                                    list_food.get(1).setSelected(true);
                                    break;
                                case "2":
                                    list_food.get(2).setSelected(true);
                                    break;
                            }
                        }
                    }
                    price_total = taox_money.getText().toString();
                    size_total = "-1";

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

    String shijianduan_status = "0";

    /*
    按下第一个的时候-保姆+做饭
     */
    private void network_request3(String typeid, final int biaozhi1, final int biaozhi2) {
        progressBar1.setVisibility(View.VISIBLE);
        System.out.println("biaozhi12--" + biaozhi1 + "," + biaozhi2);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_yuyue_info;
        RequestParams requestParams = new RequestParams();
        requestParams.put("typeid", typeid);
        requestParams.put("areaid", AyiApplication.area_id);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    progressBar1.setVisibility(View.GONE);
                    button_btn.setEnabled(true);
                    flag_wangluo = true;
                    System.out.println("这个是zfg_cq" + jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("price");
                    list_daily_net_info1.clear();
                    list_daily_net_info2.clear();
                    list_daily_net_info3.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        daily_net_info item = new daily_net_info();
                        item.setSize(jsonArray.getJSONObject(i).getString("size"));
                        item.setPrice(jsonArray.getJSONObject(i).getString("price"));
                        item.setDur(jsonArray.getJSONObject(i).getString("dur"));//0 1 2   3 4 5   6 7 8   9 10 11  12 13 14
                        switch (i % 3) {
                            case 0:
                                list_daily_net_info1.add(item);
                                break;
                            case 1:
                                list_daily_net_info2.add(item);
                                break;
                            case 2:
                                list_daily_net_info3.add(item);
                                break;
                        }
                    }
                    String[] m = new String[list_daily_net_info1.size()];
                    for (int i = 0; i < list_daily_net_info1.size(); i++) {
                        m[i] = list_daily_net_info1.get(i).getSize();
                    }
                    adapter_spinner1 = new ArrayAdapter<String>(Service_type_adg_modify_cq.this, android.R.layout.simple_spinner_item, m);
                    adapter_spinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    taox_spinner.setAdapter(adapter_spinner1);
                    final String flag = jsonObject.getJSONObject("data").getString("Is_sale");
                    if (flag.equals("1")) {
                        flag_sale = true;
                        JSONObject jv = jsonObject.getJSONObject("data").getJSONArray("sale").getJSONObject(0);//价钱相关
                        item_sale.setTitle(jv.getString("title"));
                        item_sale.setRemark(jv.getString("remark"));
                        item_sale.setData_end(gettime2(jv.getString("data_end")));
                        item_sale.setData_start(gettime2(jv.getString("data_start")));
                        item_sale.setSale(jv.getString("sale"));
                        sale_price = item_sale.getSale();
                        slae_vis.setVisibility(View.VISIBLE);
                        taox_money.setText("" + df.format(Double.parseDouble(list_daily_net_info1.get(0).getPrice()) * Double.parseDouble(item_sale.getSale())));
                        taox_money2.setText(list_daily_net_info1.get(0).getPrice());

                        if (Double.parseDouble(jv.getString("sale")) > 1) {
                            init_zhang1();
                        } else if (Double.parseDouble(jv.getString("sale")) < 1) {
                            init_hui1();
                        }


                    } else {
                        flag_sale = false;
                        slae_vis.setVisibility(View.GONE);
                        taox_money.setText(list_daily_net_info1.get(0).getPrice());

                    }

                    meici_time.setText(list_daily_net_info1.get(0).getDur());
                    meici_time2.setText(list_daily_net_info1.get(0).getDur());
                    taox_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (flag.equals("1")) {
                                taox_money.setText("" + df.format(Double.parseDouble(list_daily_net_info1.get(position).getPrice()) * Double.parseDouble(item_sale.getSale())));
                                taox_money2.setText(list_daily_net_info1.get(position).getPrice());
                            } else {
                                taox_money.setText(list_daily_net_info1.get(position).getPrice());
                            }

                            meici_time.setText(list_daily_net_info1.get(position).getDur());
                            meici_time2.setText(list_daily_net_info1.get(position).getDur());
                            spin_shangwu.setSelection(0);
                            shijianduan_status = "0";
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    if (biaozhi1 != -1) {
                        taox_spinner.setSelection(biaozhi1);
                    }
                    String[] m2 = new String[3];
                    m2[0] = "上午";
                    m2[1] = "下午";
                    m2[2] = "全天";
                    adapter_spinner1 = new ArrayAdapter<String>(Service_type_adg_modify_cq.this, android.R.layout.simple_spinner_item, m2);
                    adapter_spinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin_shangwu.setAdapter(adapter_spinner1);
                    spin_shangwu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if (flag.equals("1")) {
                                switch (position) {
                                    case 0:
                                        shijianduan_status = "0";
                                        taox_money.setText("" + df.format(Double.parseDouble(list_daily_net_info1.get(taox_spinner.getSelectedItemPosition()).getPrice()) * Double.parseDouble(item_sale.getSale())));
                                        taox_money2.setText(list_daily_net_info1.get(taox_spinner.getSelectedItemPosition()).getPrice());

                                        break;
                                    case 1:
                                        shijianduan_status = "1";
                                        taox_money.setText("" + df.format(Double.parseDouble(list_daily_net_info2.get(taox_spinner.getSelectedItemPosition()).getPrice()) * Double.parseDouble(item_sale.getSale())));

                                        taox_money2.setText(list_daily_net_info2.get(taox_spinner.getSelectedItemPosition()).getPrice());
                                        break;
                                    case 2:
                                        shijianduan_status = "2";
                                        taox_money.setText("" + df.format(Double.parseDouble(list_daily_net_info3.get(taox_spinner.getSelectedItemPosition()).getPrice()) * Double.parseDouble(item_sale.getSale())));

                                        taox_money2.setText(list_daily_net_info3.get(taox_spinner.getSelectedItemPosition()).getPrice());
                                        break;
                                }
                            } else {
                                switch (position) {
                                    case 0:
                                        shijianduan_status = "0";
                                        taox_money.setText(list_daily_net_info1.get(taox_spinner.getSelectedItemPosition()).getPrice());

                                        break;
                                    case 1:
                                        shijianduan_status = "1";
                                        taox_money.setText(list_daily_net_info2.get(taox_spinner.getSelectedItemPosition()).getPrice());
                                        break;
                                    case 2:
                                        shijianduan_status = "2";
                                        taox_money.setText(list_daily_net_info3.get(taox_spinner.getSelectedItemPosition()).getPrice());
                                        break;
                                }
                            }


                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    if (biaozhi2 != -1) {
                        spin_shangwu.setSelection(biaozhi2);
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

    Item_sale item_sale = new Item_sale();
    DecimalFormat df;
    private boolean flag_sale = false;
    private View image_coupon;
    private View image_coupon2;
    private TextView taox_money2;

    private void init_hui1() {
        image_coupon.setBackgroundResource(R.mipmap.youhuide_icon);
        image_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击了h11一下");
                //点击后出现一些方法。待续
                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(Service_type_adg_modify_cq.this);
                alert = builder.create();
                View view = LayoutInflater.from(Service_type_adg_modify_cq.this).inflate(R.layout.coupon_saying, null);
                alert.setView(view);
                TextView time = (TextView) view.findViewById(R.id.coupon_time);
                TextView title = (TextView) view.findViewById(R.id.coupon_title);
                TextView saying = (TextView) view.findViewById(R.id.coupon_saying);
                TextView sale = (TextView) view.findViewById(R.id.coupon_zhekou);
                time.setText(item_sale.getData_start() + "～" + item_sale.getData_end());
                title.setText(item_sale.getTitle());
                saying.setText(item_sale.getRemark());
                sale.setText((Double.parseDouble(item_sale.getSale()) * 10) + "折");
                alert.show();

            }
        });
    }


    private void init_zhang1() {
        image_coupon.setBackgroundResource(R.mipmap.zhangjia_icon);
        image_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击了z11一下");
                //点击后出现一些方法。待续
                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(Service_type_adg_modify_cq.this);
                alert = builder.create();
                View view = LayoutInflater.from(Service_type_adg_modify_cq.this).inflate(R.layout.zhang_saying, null);
                alert.setView(view);
                TextView time = (TextView) view.findViewById(R.id.coupon_time);
                TextView title = (TextView) view.findViewById(R.id.coupon_title);
                TextView saying = (TextView) view.findViewById(R.id.coupon_saying);
                TextView sale = (TextView) view.findViewById(R.id.coupon_zhekou);
                time.setText(item_sale.getData_start() + "～" + item_sale.getData_end());
                title.setText(item_sale.getTitle());
                saying.setText(item_sale.getRemark());
                sale.setText(String.valueOf(Double.parseDouble(df.format((Double.parseDouble(item_sale.getSale()) - 1))) * 100) + "%");
                alert.show();
            }
        });
    }
}
