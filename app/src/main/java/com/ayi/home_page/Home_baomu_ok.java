package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.LoginActivity;
import com.ayi.activity.WebViewActivity;
import com.ayi.entity.item_ayi_list;
import com.ayi.entity.item_place_info;
import com.ayi.entity.mlist_pay;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.FileService;
import com.ayi.utils.Show_toast;
import com.ayi.utils.Web_view_guyong;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/8/29.
 */
public class Home_baomu_ok extends Activity {
    private Button button_home_zdg_ok_btn;
    private View top;
    private View back;
    private View select_ayi_by_hand;
    private EditText zdg_beizhu;
    private View click_detailed_info;
    private View service_type;
    //    private View guyong_view;
    private View delete_ayi;
    private View guyonghetong;
    private View progressBar1;
    private boolean need_hidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.home_baomu_ok_view2);
        need_hidden = getIntent().getBooleanExtra("need_hidden", false);
        init();
        init_click_detailed_info_click();//用户信息
//        init_click_service_type();//服务类型——确认
        init_time_select_click();//预约时间选择
        init_select_ayi_click();//选择阿姨的点击-ok
        init_back();
        int_guyonghetong();
        if (getIntent().getBooleanExtra("flag", false)) {
            init_ok2();
        } else {
            init_ok();
        }
    }

    private void int_guyonghetong() {
        guyonghetong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_baomu_ok.this, WebViewActivity.class);
                intent.putExtra("url", "http://doc.sangeayi.com/contracts.html");
                intent.putExtra("title", "合同下载");
                startActivity(intent);
            }
        });
    }

    private String service_time;
    private TextView yuyue_time;
    private String yuyue_time_string = "";//预约时间字符串
    private String time_start_unix = "";
    private String ayi_id = "-1";
    private String price_total = "";
    private String num_total = "-1";
    private String size_total = "-1";
    private TextView ayi_name;
    private TextView click_select_service_type;
    private String meici_time = "1";
    private String period;
    private String ayi_fei;
    String biaozhi1 = "";
    int biaozhi2 = 0;
    private TextView yuyue_time2;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getIntExtra("user_info", 0) == 1) {
            ayi_id = "-1";
            ayi_name.setText("");
//            guyong_view.setVisibility(View.GONE);
        }
        if (intent.getStringExtra("yuyue_time_string") != null) {
            biaozhi1 = intent.getStringExtra("biaozhi1");
            biaozhi2 = intent.getIntExtra("biaozhi2", 0);
            System.out.println("接收" + biaozhi1 + "," + biaozhi2);
            yuyue_time_string = intent.getStringExtra("yuyue_time_string");
            time_start_unix = intent.getStringExtra("time_start");
            period = intent.getStringExtra("period");
            yuyue_time.setText(yuyue_time_string);
//            guyong_view.setVisibility(View.GONE);
            ayi_id = "-1";
            ayi_name.setText("");
            yuyue_time2.setVisibility(View.VISIBLE);
            yuyue_time2.setText("聘用" + (biaozhi2) + (type_num.equals("14") ? "周" : "月"));
        }
        if (intent.getStringExtra("service_type_id") != null) {
            type = intent.getStringExtra("service_type");
            type_num = intent.getStringExtra("service_type_id");
            size_total = intent.getStringExtra("size_total");
            service_time = intent.getStringExtra("service_time");
            click_select_service_type.setText(type);
            ayi_id = "-1";
            ayi_name.setText("");
//            guyong_view.setVisibility(View.GONE);
            yuyue_time.setText("");
            yuyue_time2.setVisibility(View.GONE);
            biaozhi1 = "";
        }
        //这是阿姨的
        if (intent.getSerializableExtra("ayi_info") != null) {
            System.out.println("再次回来了" + intent.getSerializableExtra("ayi_info"));
            item_ayi_list item_ayi = (item_ayi_list) intent.getSerializableExtra("ayi_info");
            ayi_id = item_ayi.getId();

            if (!item_ayi.getOut_price().equals("")) {
//                guyong_view.setVisibility(View.VISIBLE);
//                ayiyue_fee.setText(item_ayi.getOut_price());
                ayi_fei = item_ayi.getOut_price();
                ayi_name.setText(item_ayi.getName() + " (" + ayi_fei + "元/月)");
//                DecimalFormat df   = new DecimalFormat("######0.00");
//                service_pay.setText(item_ayi.getServicecharge()+".00");
//                test_pay.setText(""+df.format( Integer.valueOf(period) *new Double(ayi_fei)/30));
            }
        }


    }

    String user_name = "";
    String user_phone = "";


    private void init_back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private EditText name;
    private EditText phone;
    private String type = "";
    private String type_num = "";
    private String latitude = "";
    private String longitude = "";
    private TextView place1;

    private void init_ok() {
        button_home_zdg_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button_home_zdg_ok_btn.setEnabled(false);
                if (AyiApplication.getInstance().accountService().id().equals("")) {
                    button_home_zdg_ok_btn.setEnabled(true);
                    Intent intent = new Intent(Home_baomu_ok.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if (place1.getText().toString().isEmpty()) {
                    Show_toast.showText(Home_baomu_ok.this, "用户资料不完整，请点击填写");
                    button_home_zdg_ok_btn.setEnabled(true);
                    return;
                }
                if (yuyue_time.getText().toString().isEmpty()) {
                    Show_toast.showText(Home_baomu_ok.this, "预约时间不能为空");
                    button_home_zdg_ok_btn.setEnabled(true);
                    return;
                }
                if (ayi_id.equals("-1")) {
                    Show_toast.showText(Home_baomu_ok.this, "请选择阿姨");
                    button_home_zdg_ok_btn.setEnabled(true);
                    return;
                }
                progressBar1.setVisibility(View.VISIBLE);
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.setTimeout(20000);
                String url = RetrofitUtil.url_order_add;
                RequestParams requestParams = new RequestParams();
                requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                requestParams.put("token", AyiApplication.getInstance().accountService().token());
                requestParams.put("isvalet", 0);
                requestParams.put("contacts", user_name);//联系人
                requestParams.put("contact_phone", user_phone);//联系手机号
                requestParams.put("areaid", AyiApplication.area_id);
                requestParams.put("areaname", AyiApplication.place);
                requestParams.put("contact_address", place1.getText().toString());//两个地址相加
                requestParams.put("latitude", latitude);//维度得到的具体坐标
                requestParams.put("longitude", longitude);//经度
                requestParams.put("service_type_id", type_num);//服务类型id,参照服务类型表
                requestParams.put("service_type", type);//前者的中文名字
                requestParams.put("size", "-1");
                requestParams.put("num", "-1");//油烟机  没有则-1
                requestParams.put("service_time", "-1");
                requestParams.put("sp_price_from", "");//心理价位
                requestParams.put("sp_price_to", "");//心理价位
                requestParams.put("time_start", "-1");
                requestParams.put("time_finish", "-1");
                requestParams.put("day_fre", "-1");
                requestParams.put("day_start", time_start_unix);
                requestParams.put("day_finish", "-1");
                requestParams.put("period", period);
                requestParams.put("end", "3");
//                requestParams.put("price",ayiyue_fee.getText().toString());
                requestParams.put("price", ayi_fei);
                requestParams.put("cleaner_id", ayi_id.equals("-1") ? "0" : ayi_id);//选择清洁工0是没有选择
                requestParams.put("remark", zdg_beizhu.getText().toString());//备注
//                requestParams.put("payment","1");//0表示服务后付款1表示马上付款
                requestParams.put("trialorder", "0");//可以不传----1表示试用订单
                requestParams.put("merchandise", "");//这个好像是随手带
                System.out.println("requestParams.toString()" + requestParams.toString());
                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        super.onSuccess(statusCode, headers, jsonObject);
                        try {
                            progressBar1.setVisibility(View.GONE);
                            button_home_zdg_ok_btn.setEnabled(true);
                            System.out.println(jsonObject.toString());
                            Intent intent = new Intent(Home_baomu_ok.this, Order_pay.class);//跳转去支付
                            intent.putExtra("areaid", AyiApplication.area_id);//这是区域地址
                            intent.putExtra("type", type);//这是服务内容。
                            intent.putExtra("type_num", type_num);
                            intent.putExtra("time_start", jsonObject.getJSONObject("data").getString("timeService"));//开始的预约时间
                            intent.putExtra("user_name", jsonObject.getJSONObject("data").getString("contacts"));
                            intent.putExtra("place", jsonObject.getJSONObject("data").getString("address"));//地方
                            intent.putExtra("price", jsonObject.getJSONObject("data").getString("pricetotal"));
                            List<mlist_pay> list_item = new ArrayList<mlist_pay>();
                            for (int i = 0; i < jsonObject.getJSONObject("data").getJSONArray("mList").length(); i++) {
                                mlist_pay item = new mlist_pay();
                                item.setPrice(jsonObject.getJSONObject("data").getJSONArray("mList").getJSONObject(i).getString("price"));
                                item.setProject(jsonObject.getJSONObject("data").getJSONArray("mList").getJSONObject(i).getString("project"));
                                item.setQuantity(jsonObject.getJSONObject("data").getJSONArray("mList").getJSONObject(i).getString("quantity"));
                                list_item.add(item);
                            }
                            intent.putExtra("list", (Serializable) list_item);
                            intent.putExtra("orderid", jsonObject.getJSONObject("data").getString("orderId"));
                            intent.putExtra("childorder", jsonObject.getJSONObject("data").getString("childid"));
                            intent.putExtra("ordernum", jsonObject.getJSONObject("data").getString("ordernum"));
                            startActivity(intent);

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
        });
    }

    private void init_ok2() {
        button_home_zdg_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                button_home_zdg_ok_btn.setEnabled(false);
                if (AyiApplication.getInstance().accountService().id().equals("")) {
                    button_home_zdg_ok_btn.setEnabled(true);
                    Intent intent = new Intent(Home_baomu_ok.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if (place1.getText().toString().isEmpty()) {
                    Show_toast.showText(Home_baomu_ok.this, "用户资料不完整，请点击填写");
                    button_home_zdg_ok_btn.setEnabled(true);
                    return;
                }
                if (yuyue_time.getText().toString().isEmpty()) {
                    Show_toast.showText(Home_baomu_ok.this, "预约时间不能为空");
                    button_home_zdg_ok_btn.setEnabled(true);
                    return;
                }
                if (ayi_id.equals("-1")) {
                    Show_toast.showText(Home_baomu_ok.this, "请选择阿姨");
                    button_home_zdg_ok_btn.setEnabled(true);
                    return;
                }
                progressBar1.setVisibility(View.VISIBLE);
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.setTimeout(20000);
                String url = RetrofitUtil.url_order_add;
                RequestParams requestParams = new RequestParams();
                requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                requestParams.put("token", AyiApplication.getInstance().accountService().token());
                requestParams.put("isvalet", 1);
                requestParams.put("contacts", user_name);//联系人
                requestParams.put("contact_phone", user_phone);//联系手机号
                requestParams.put("areaid", AyiApplication.area_id);
                requestParams.put("areaname", AyiApplication.place);
                requestParams.put("contact_address", place1.getText().toString());//两个地址相加
                requestParams.put("latitude", latitude);//维度得到的具体坐标
                requestParams.put("longitude", longitude);//经度
                requestParams.put("service_type_id", type_num);//服务类型id,参照服务类型表
                requestParams.put("service_type", type);//前者的中文名字
                requestParams.put("size", "-1");
                requestParams.put("num", "-1");//油烟机  没有则-1
                requestParams.put("service_time", "-1");
                requestParams.put("sp_price_from", "");//心理价位
                requestParams.put("sp_price_to", "");//心理价位
                requestParams.put("time_start", "-1");
                requestParams.put("time_finish", "-1");
                requestParams.put("day_fre", "-1");
                requestParams.put("day_start", time_start_unix);
                requestParams.put("day_finish", "-1");
                requestParams.put("period", period);
                requestParams.put("end", "3");
//                requestParams.put("price",ayiyue_fee.getText().toString());
                requestParams.put("price", ayi_fei);
                requestParams.put("cleaner_id", ayi_id.equals("-1") ? "0" : ayi_id);//选择清洁工0是没有选择
                requestParams.put("remark", zdg_beizhu.getText().toString());//备注
//                requestParams.put("payment","1");//0表示服务后付款1表示马上付款
                requestParams.put("trialorder", "0");//可以不传----1表示试用订单
                requestParams.put("merchandise", "");//这个好像是随手带
                System.out.println("requestParams.toString()" + requestParams.toString());
                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        super.onSuccess(statusCode, headers, jsonObject);
                        try {
                            progressBar1.setVisibility(View.GONE);
                            button_home_zdg_ok_btn.setEnabled(true);
                            System.out.println(jsonObject.toString());
                            Intent intent = new Intent(Home_baomu_ok.this, Order_pay.class);//跳转去支付
                            intent.putExtra("areaid", AyiApplication.area_id);//这是区域地址
                            intent.putExtra("type", type);//这是服务内容。
                            intent.putExtra("type_num", type_num);
                            intent.putExtra("time_start", jsonObject.getJSONObject("data").getString("timeService"));//开始的预约时间
                            intent.putExtra("user_name", jsonObject.getJSONObject("data").getString("contacts"));
                            intent.putExtra("place", jsonObject.getJSONObject("data").getString("address"));//地方
                            intent.putExtra("price", jsonObject.getJSONObject("data").getString("pricetotal"));
                            List<mlist_pay> list_item = new ArrayList<mlist_pay>();
                            for (int i = 0; i < jsonObject.getJSONObject("data").getJSONArray("mList").length(); i++) {
                                mlist_pay item = new mlist_pay();
                                item.setPrice(jsonObject.getJSONObject("data").getJSONArray("mList").getJSONObject(i).getString("price"));
                                item.setProject(jsonObject.getJSONObject("data").getJSONArray("mList").getJSONObject(i).getString("project"));
                                item.setQuantity(jsonObject.getJSONObject("data").getJSONArray("mList").getJSONObject(i).getString("quantity"));
                                list_item.add(item);
                            }
                            intent.putExtra("list", (Serializable) list_item);
                            intent.putExtra("orderid", jsonObject.getJSONObject("data").getString("orderId"));
                            intent.putExtra("childorder", jsonObject.getJSONObject("data").getString("childid"));
                            intent.putExtra("ordernum", jsonObject.getJSONObject("data").getString("ordernum"));
                            startActivity(intent);

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
        });
    }

    private View click_yuyue_time;

    /*
    时间点击
     */
    private void init_time_select_click() {

        click_yuyue_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place1.getText().toString().isEmpty()) {
                    Show_toast.showText(Home_baomu_ok.this, "请完善详细资料");
                    return;
                }
                Intent intent = new Intent(Home_baomu_ok.this, Other_last2_time_select.class);
                intent.putExtra("flag", "bm");
                intent.putExtra("type_num", type_num);
                if (!biaozhi1.equals("")) {
                    intent.putExtra("biaozhi1", biaozhi1);
                    intent.putExtra("biaozhi2", biaozhi2);
                }

                System.out.println("发送" + biaozhi1 + "," + biaozhi2);
                startActivity(intent);
            }
        });


    }

    /*
      用户信息点击
       */
    private void init_click_detailed_info_click() {
        click_detailed_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_baomu_ok.this, User_info_before.class);
                intent.putExtra("flag_place", "bm");
                intent.putExtra("flag", getIntent().getBooleanExtra("flag", false));
                startActivity(intent);
            }
        });
    }

    private void init_select_ayi_click() {
        select_ayi_by_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (yuyue_time_string.equals("")) {
                    Show_toast.showText(Home_baomu_ok.this, "请选择时间");
                    return;
                }
                if (latitude.equals("") || longitude.equals("")) {
                    Show_toast.showText(Home_baomu_ok.this, "请输入具体地址");
                    return;
                }
                Intent intent = new Intent(Home_baomu_ok.this, Ayi_list_2_new.class);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("flag", "bm");
                intent.putExtra("type_num", type_num);
                //添加两个参数
                intent.putExtra("time_start", time_start_unix);
                intent.putExtra("time_finish", "");
                startActivity(intent);
            }
        });
    }

    //    private TextView service_pay;
//    private TextView test_pay;
    private String ayi_fee;
    private TextView text_title;

    private void init() {
        progressBar1 = findViewById(R.id.progressBar1);
        guyonghetong = findViewById(R.id.guyonghetong);
        delete_ayi = findViewById(R.id.delete_ayi);
        delete_ayi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ayi_id = "-1";
                ayi_name.setText("");
                v.setVisibility(View.GONE);
//                guyong_view.setVisibility(View.GONE);
            }
        });
        yuyue_time2 = (TextView) findViewById(R.id.yuyue_time2);
//        service_pay= (TextView) findViewById(R.id.service_pay);
//        test_pay= (TextView) findViewById(R.id.test_pay);
//        guyong_view=findViewById(R.id.guyong_view);
        ayi_name = (TextView) findViewById(R.id.ayi_name);
        yuyue_time = (TextView) findViewById(R.id.yuyue_time);
        click_yuyue_time = findViewById(R.id.click_yuyue_time);
        service_type = findViewById(R.id.service_type);
        click_select_service_type = (TextView) findViewById(R.id.click_select_service_type);
        click_detailed_info = findViewById(R.id.click_detailed_info);
        zdg_beizhu = (EditText) findViewById(R.id.zdg_beizhu);
        select_ayi_by_hand = findViewById(R.id.select_ayi_by_hand);
        place1 = (TextView) findViewById(R.id.place1);

        top = findViewById(R.id.top);
        text_title = (TextView) top.findViewById(R.id.logreg_center);
        text_title.setText(getIntent().getStringExtra("title"));
        back = top.findViewById(R.id.logreg_left);
        button_home_zdg_ok_btn = (Button) findViewById(R.id.button_home_zdg_ok_btn);
        type = getIntent().getStringExtra("title");
        type_num = getIntent().getStringExtra("service_id");


    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.client_home_baomu));
        MobclickAgent.onResume(this);
        if (!ayi_id.equals("-1")) {
            delete_ayi.setVisibility(View.VISIBLE);
        } else {
            delete_ayi.setVisibility(View.GONE);
        }

        if (!need_hidden) {
            FileService fileService = new FileService(this);
            try {
                item_place_info user_info = fileService.read("user.txt");
                if (user_info.getShiji_dizhi().equals(AyiApplication.place)) {
                    latitude = user_info.getLatitude();
                    longitude = user_info.getLongitide();
                    if (!user_info.getPlace().equals("")) {
                        place1.setText(user_info.getPlace() + "," + user_info.getNum_place());
                    } else {
                        place1.setText("");
                    }
                    user_name = user_info.getName();
                    user_phone = user_info.getPhone();
                } else {
                    place1.setText("");
                    if (!user_info.getShiji_dizhi().equals(""))
                    Show_toast.showText(Home_baomu_ok.this, "请选择对应城市");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            FileService fileService = new FileService(this);
            try {
                item_place_info user_info = fileService.read("user2.txt");
                if (user_info.getShiji_dizhi().equals(AyiApplication.place)) {
                    latitude = user_info.getLatitude();
                    longitude = user_info.getLongitide();
                    if (!user_info.getPlace().equals("")) {
                        place1.setText(user_info.getPlace() + "," + user_info.getNum_place());
                    } else {
                        place1.setText("");
                    }
                    user_name = user_info.getName();
                    user_phone = user_info.getPhone();
                } else {
                    place1.setText("");
                    if (!user_info.getShiji_dizhi().equals(""))
                    Show_toast.showText(Home_baomu_ok.this, "请选择对应城市");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.client_home_baomu));
        MobclickAgent.onPause(this);
    }
}
