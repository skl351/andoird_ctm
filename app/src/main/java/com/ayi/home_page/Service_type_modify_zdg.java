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
import android.widget.Spinner;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.entity.Item_sale;
import com.ayi.entity.daily_net_info;
import com.ayi.retrofit.RetrofitUtil;
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
public class Service_type_modify_zdg extends Activity {
    private List<daily_net_info> list_daily_net_info;
    private ArrayAdapter<String> adapter_spinner1;
    private Spinner taox_spinner;
    private TextView taox_money;
    private View taox_view;
    private View taishu_view;
    private Spinner taishu_num;
    private TextView taishu_money;
    private TextView taishu_money2;
    private TextView taishu_xiaoshi;
    private TextView taishu_xiaoshi2;
    private TextView meici_time;
    private String type = "日常保洁";
    private String type_num = "5";
    private String price_total = "";
    private String num_total = "-1";
    private String size_total = "-1";
    private Object _info;
    boolean flag_wangluo = false;
    private View progressBar1;
    private View tiaofu_6_01;
    private View tiaofu_6_02;
    Item_sale item_sale = new Item_sale();
    //    if (Build.VERSION.SDK_INT >= 23) {
    private TextView taox_money_be;
    private TextView taox_money2;
    private TextView meici_time2;
    DecimalFormat df;
    private boolean flag_sale = false;
    private View image_coupon;
    private View image_coupon2;
    private View button_btn;

    private View center_content_rcbj;
    private View center_content_qxyyj;
    private View center_content_kh;
    private View center_content_cbl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_type_view);
        type=getIntent().getStringExtra("type");
        type_num=getIntent().getStringExtra("type_num");
        init();
        button_btn.setEnabled(false);
        init_ok();

        switch (getIntent().getStringExtra("type_num")) {
            case "5":
//                list_4img.get(0).setVisibility(View.VISIBLE);//用来确定那个被按下了
//                type = "日常保洁";
//                type_num = "5";
                network_request("5", getIntent().getIntExtra("biaozhi1", -1));
                taox_view.setVisibility(View.VISIBLE);
                taishu_view.setVisibility(View.GONE);
                center_content_rcbj.setVisibility(View.VISIBLE);
                break;
            case "6":
//                System.out.println("这边经过");
//                list_4img.get(1).setVisibility(View.VISIBLE);//用来确定那个被按下了
//                type_num = "6";
//                type = "清洗油烟机";
                network_request2("6", getIntent().getIntExtra("biaozhi2", -1));
                taox_view.setVisibility(View.GONE);
                taishu_view.setVisibility(View.VISIBLE);
                center_content_qxyyj.setVisibility(View.VISIBLE);
                break;
            case "7":
//                list_4img.get(2).setVisibility(View.VISIBLE);//用来确定那个被按下了
//                type_num = "7";
//                type = "擦玻璃";
                network_request("7", getIntent().getIntExtra("biaozhi1", -1));
                taox_view.setVisibility(View.VISIBLE);
                taishu_view.setVisibility(View.GONE);
                center_content_cbl.setVisibility(View.VISIBLE);
                break;
            case "8":
//                list_4img.get(3).setVisibility(View.VISIBLE);//用来确定那个被按下了
//                type_num = "8";
//                type = "开荒";
                network_request("8", getIntent().getIntExtra("biaozhi1", -1));
                taox_view.setVisibility(View.VISIBLE);
                taishu_view.setVisibility(View.GONE);
                center_content_kh.setVisibility(View.VISIBLE);
                break;
        }

//        init_4object_click();
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

    private void init_ok() {
        button_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!flag_wangluo) {
                    return;
                }
                get_info();

                Intent intent = new Intent(Service_type_modify_zdg.this, Home_zdg_ok.class);
                intent.putExtra("biaozhi1", taox_spinner.getSelectedItemPosition());//
                KLog.e("biaozhi1+biaozhi2", taox_spinner.getSelectedItemPosition() + "," + taishu_num.getSelectedItemPosition());
                intent.putExtra("biaozhi2", taishu_num.getSelectedItemPosition());

                intent.putExtra("service_type_id", type_num);
                intent.putExtra("service_type", type);
                intent.putExtra("price_total", price_total);
                intent.putExtra("num_total", num_total);
                intent.putExtra("size_total", size_total);
                intent.putExtra("sale", sale_price);
                intent.putExtra("taishu_xiaoshi", taishu_xiaoshi.getText().toString());
                intent.putExtra("meici_time", meici_time.getText().toString());
                KLog.d("service_type", type_num + "," + type + "," + price_total + "," + num_total + "," + size_total + "," + taishu_xiaoshi.getText().toString() + "," + meici_time.getText().toString());
                startActivity(intent);

            }
        });
    }


    /*
    以下8个是类型点击的视图
     */
    private View zdg_daily;
    private View zdg_clear_cooker;
    private View zdg_clear_glass;
    private View zdg_open_westeload;
    private View select1;
    private View select2;
    private View select3;
    private View select4;

    private View slae_vis;
    private View slae_vis2;

    private void init() {
        center_content_rcbj=findViewById(R.id.center_content_rcbj);
        center_content_qxyyj=findViewById(R.id.center_content_qxyyj);
        center_content_kh=findViewById(R.id.center_content_kh);
        center_content_cbl=findViewById(R.id.center_content_cbl);
        button_btn=findViewById(R.id.button_btn);
        image_coupon = findViewById(R.id.image_coupon);
        image_coupon2= findViewById(R.id.image_coupon2);
        df = new DecimalFormat("######0.00");
        slae_vis = findViewById(R.id.slae_vis);
        slae_vis2 = findViewById(R.id.slae_vis2);
        taox_money_be = (TextView) findViewById(R.id.taox_money_be);
        tiaofu_6_02 = findViewById(R.id.tiaofu_6_02);
        tiaofu_6_01 = findViewById(R.id.tiaofu_6_01);
        if (Build.VERSION.SDK_INT >= 23) {
            tiaofu_6_02.setVisibility(View.VISIBLE);
            tiaofu_6_01.setVisibility(View.VISIBLE);
        }
        progressBar1 = findViewById(R.id.progressBar1);
        meici_time = (TextView) findViewById(R.id.meici_time);
        meici_time2 = (TextView) findViewById(R.id.meici_time2);
        taishu_num = (Spinner) findViewById(R.id.taishu_num);
        taishu_money = (TextView) findViewById(R.id.taishu_money);
        taishu_money2 = (TextView) findViewById(R.id.taishu_money2);
        taishu_money2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        taishu_xiaoshi = (TextView) findViewById(R.id.taishu_xiaoshi);
        taishu_xiaoshi2 = (TextView) findViewById(R.id.taishu_xiaoshi2);
        taox_view = findViewById(R.id.taox_view);
        taishu_view = findViewById(R.id.taishu_view);
        taox_money = (TextView) findViewById(R.id.taox_money);
        taox_money2 = (TextView) findViewById(R.id.taox_money2);
        taox_money2.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);  // 设置中划线并加清晰
        taox_spinner = (Spinner) findViewById(R.id.taox_spinner);
        list_daily_net_info = new ArrayList<>();

    }

    /*
   按下第一个的时候--日常保洁
    */
    private void network_request(String typeid, final int biaozhi) {
        progressBar1.setVisibility(View.VISIBLE);
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
                    System.out.println("jsonObject" + jsonObject);
                    progressBar1.setVisibility(View.GONE);
                    button_btn.setEnabled(true);
                    flag_wangluo = true;
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
                    adapter_spinner1 = new ArrayAdapter<String>(Service_type_modify_zdg.this, android.R.layout.simple_spinner_item, m);

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
                        slae_vis.setVisibility(View.VISIBLE);
                        taox_money.setText("" + df.format(Double.parseDouble(list_daily_net_info.get(0).getPrice()) * Double.parseDouble(item_sale.getSale())));
                        taox_money2.setText(list_daily_net_info.get(0).getPrice());

                        if(Double.parseDouble(jv.getString("sale"))>1){
                            init_zhang1();
                        }else if(Double.parseDouble(jv.getString("sale"))<1){
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
                    if (biaozhi != -1) {

                        System.out.println("biaozhi1---" + biaozhi);
                        taox_spinner.setSelection(biaozhi);
                    }
//                    price_total=taox_money.getText().toString();
//                    num_total="-1";
//                    size_total=list_daily_net_info.get(taox_spinner.getSelectedItemPosition()).getSize();
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

    /*
   按下第二个的时候--清洗油烟机
    */
    private void network_request2(String typeid, final int biaozhi) {
        progressBar1.setVisibility(View.VISIBLE);
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
                    System.out.println("jsonObject:" + jsonObject);
                    progressBar1.setVisibility(View.GONE);
                    button_btn.setEnabled(true);
                    flag_wangluo = true;
                    final String price_z = jsonObject.getJSONObject("data").getJSONArray("price").getJSONObject(0).getString("price");

                    final String time_z = jsonObject.getJSONObject("data").getJSONArray("price").getJSONObject(0).getString("dur");
                    String num = jsonObject.getJSONObject("data").getJSONArray("price").getJSONObject(0).getString("num");
//                    final String time_z = time.split("\\.")[0];
//                    final String price_z = price.split("\\.")[0];

                    String[] m = new String[5];
                    for (int i = 0; i < 5; i++) {
                        m[i] = "" + (i + 1);
                    }
                    adapter_spinner1 = new ArrayAdapter<String>(Service_type_modify_zdg.this, android.R.layout.simple_spinner_item, m);
                    adapter_spinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    taishu_num.setAdapter(adapter_spinner1);

                    final String flag = jsonObject.getJSONObject("data").getString("Is_sale");
                    if (flag.equals("1")) {
                        flag_sale = true;
                        JSONObject jv = jsonObject.getJSONObject("data").getJSONArray("sale").getJSONObject(0);//价钱相关
                        item_sale.setTitle(jv.getString("title"));
                        item_sale.setRemark(jv.getString("remark"));
                        item_sale.setData_end(gettime2(jv.getString("data_end")));
                        item_sale.setData_start(gettime2(jv.getString("data_start")));
                        item_sale.setSale(jv.getString("sale"));
                        slae_vis2.setVisibility(View.VISIBLE);
                        taishu_money.setText("" + df.format(Double.parseDouble(price_z) * Double.parseDouble(item_sale.getSale())));
                        taishu_money2.setText(price_z);
                        if(Double.parseDouble(jv.getString("sale"))>1){
                            image_coupon2.setBackgroundResource(R.mipmap.zhangjia_icon);
                            image_coupon2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println("点击了z2一下");
                                    //点击后出现一些方法。待续
                                    AlertDialog alert;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Service_type_modify_zdg.this);
                                    alert = builder.create();
                                    View view=LayoutInflater.from(Service_type_modify_zdg.this).inflate(R.layout.zhang_saying,null);
                                    alert.setView(view);
                                    TextView time= (TextView) view.findViewById(R.id.coupon_time);
                                    TextView title= (TextView) view.findViewById(R.id.coupon_title);
                                    TextView saying= (TextView) view.findViewById(R.id.coupon_saying);
                                    TextView sale= (TextView) view.findViewById(R.id.coupon_zhekou);
                                    time.setText(item_sale.getData_start()+"～"+item_sale.getData_end());
                                    title.setText(item_sale.getTitle());
                                    saying.setText(item_sale.getRemark());
                                    sale.setText(String.valueOf(Double.parseDouble(df.format((Double.parseDouble(item_sale.getSale())-1)))*100)+"%");
                                    alert.show();


                                }
                            });
                        }else if(Double.parseDouble(jv.getString("sale"))<1){
                            image_coupon2.setBackgroundResource(R.mipmap.youhuide_icon);
                            image_coupon2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    System.out.println("点击了h2一下");
                                    //点击后出现一些方法。待续
                                    AlertDialog alert;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Service_type_modify_zdg.this);
                                    alert = builder.create();
                                    View view=LayoutInflater.from(Service_type_modify_zdg.this).inflate(R.layout.coupon_saying,null);
                                    alert.setView(view);
                                    TextView time= (TextView) view.findViewById(R.id.coupon_time);
                                    TextView title= (TextView) view.findViewById(R.id.coupon_title);
                                    TextView saying= (TextView) view.findViewById(R.id.coupon_saying);
                                    TextView sale= (TextView) view.findViewById(R.id.coupon_zhekou);
                                    time.setText(item_sale.getData_start()+"～"+item_sale.getData_end());
                                    title.setText(item_sale.getTitle());
                                    saying.setText(item_sale.getRemark());
                                    sale.setText(Integer.parseInt(""+(Double.parseDouble(item_sale.getSale())*10))+"折");
                                    alert.show();
                                }
                            });
                        }
                    } else {
                        flag_sale = false;
                        slae_vis2.setVisibility(View.GONE);
                        taishu_money.setText(price_z);
                    }
                    taishu_xiaoshi.setText(time_z);
                    taishu_xiaoshi2.setText(time_z);
                    taishu_num.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            if (flag.equals("1")) {
                                taishu_money.setText("" + df.format(Double.parseDouble(price_z) * (position + 1) * Double.parseDouble(item_sale.getSale())));
                                taishu_money2.setText(String.valueOf(Double.parseDouble(price_z) * (position + 1)));
                            } else {
                                taishu_money.setText(String.valueOf(Double.parseDouble(price_z) * (position + 1)));
                            }
//                            taishu_xiaoshi.setText(String.valueOf(Integer.parseInt(time_z) * (position + 1)));
//                            taishu_xiaoshi2.setText(String.valueOf(Integer.parseInt(time_z) * (position + 1)));
                            taishu_xiaoshi.setText(String.valueOf(Double.parseDouble(time_z) * (position + 1)));
                            taishu_xiaoshi2.setText(String.valueOf(Double.parseDouble(time_z) * (position + 1)));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    if (biaozhi != -1) {
                        System.out.println("biaozhi2---" + biaozhi);
                        taishu_num.setSelection(biaozhi);
                    }
//                    price_total=taishu_money.getText().toString();
//                    num_total=""+taishu_num.getSelectedItemPosition();
//                    size_total="-1";
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

    /**
     * 初始化的时候默认选项
     *
     * @param typeid
     */
    private void network_request_init(String typeid) {
        progressBar1.setVisibility(View.VISIBLE);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_yuyue_info;
        RequestParams requestParams = new RequestParams();
        requestParams.put("typeid", typeid);
        requestParams.put("areaid", AyiApplication.area_id);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    progressBar1.setVisibility(View.GONE);
                    button_btn.setEnabled(true);
                    flag_wangluo = true;
                    System.out.println("zdg:" + jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("price");//价钱相关


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
                    adapter_spinner1 = new ArrayAdapter<String>(Service_type_modify_zdg.this, android.R.layout.simple_spinner_item, m);
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
                        slae_vis.setVisibility(View.VISIBLE);
                        taox_money.setText("" + df.format(Double.parseDouble(list_daily_net_info.get(0).getPrice()) * Double.parseDouble(item_sale.getSale())));
                        taox_money2.setText(list_daily_net_info.get(0).getPrice());

                        if(Double.parseDouble(jv.getString("sale"))>1){
                            init_zhang1();
                        }else if(Double.parseDouble(jv.getString("sale"))<1){
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
//                    price_total=taox_money.getText().toString();
//                    num_total="-1";
//                    size_total=list_daily_net_info.get(taox_spinner.getSelectedItemPosition()).getSize();
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

    private void init_hui1() {
        image_coupon.setBackgroundResource(R.mipmap.youhuide_icon);
        image_coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("点击了h11一下");
                //点击后出现一些方法。待续
                AlertDialog alert;
                AlertDialog.Builder builder = new AlertDialog.Builder(Service_type_modify_zdg.this);
                alert = builder.create();
                View view=LayoutInflater.from(Service_type_modify_zdg.this).inflate(R.layout.coupon_saying,null);
                alert.setView(view);
                TextView time= (TextView) view.findViewById(R.id.coupon_time);
                TextView title= (TextView) view.findViewById(R.id.coupon_title);
                TextView saying= (TextView) view.findViewById(R.id.coupon_saying);
                TextView sale= (TextView) view.findViewById(R.id.coupon_zhekou);
                time.setText(item_sale.getData_start()+"～"+item_sale.getData_end());
                title.setText(item_sale.getTitle());
                saying.setText(item_sale.getRemark());
                sale.setText((Double.parseDouble(item_sale.getSale())*10)+"折");
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Service_type_modify_zdg.this);
                alert = builder.create();
                View view=LayoutInflater.from(Service_type_modify_zdg.this).inflate(R.layout.zhang_saying,null);
                alert.setView(view);
                TextView time= (TextView) view.findViewById(R.id.coupon_time);
                TextView title= (TextView) view.findViewById(R.id.coupon_title);
                TextView saying= (TextView) view.findViewById(R.id.coupon_saying);
                TextView sale= (TextView) view.findViewById(R.id.coupon_zhekou);
                time.setText(item_sale.getData_start()+"～"+item_sale.getData_end());
                title.setText(item_sale.getTitle());
                saying.setText(item_sale.getRemark());
                sale.setText(String.valueOf(Double.parseDouble(df.format((Double.parseDouble(item_sale.getSale())-1)))*100)+"%");
                alert.show();
            }
        });
    }

    String sale_price = "1";

    public void get_info() {

        if (type_num.equals("6")) {
            //油烟机
            if (flag_sale) {
                price_total = taishu_money2.getText().toString();
                sale_price = item_sale.getSale();
            } else {
                price_total = taishu_money.getText().toString();
            }

            num_total = String.valueOf(taishu_num.getSelectedItemPosition() + 1);
            size_total = "-1";
        } else {
            if (flag_sale) {
                price_total = taox_money2.getText().toString();
                sale_price = item_sale.getSale();
            } else {
                price_total = taox_money.getText().toString();
            }
            num_total = "-1";
            size_total = list_daily_net_info.get(taox_spinner.getSelectedItemPosition()).getSize();
        }
    }
}
