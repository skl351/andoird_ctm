package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.LoginActivity;
import com.ayi.entity.item_ayi_list;
import com.ayi.entity.item_place_info;
import com.ayi.entity.mlist_pay;
import com.ayi.entity.suishoudai_guodu;
import com.ayi.retrofit.RetrofitUtil;
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

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/8/29.
 */
public class Home_zdg_ok extends Activity {
    private TextView text_title;
    private Button button_home_zdg_ok_btn;
    private View top;
    private View back;
    private View select_ayi_by_hand;
    private EditText zdg_beizhu;
    private View service_type;
    private TextView click_select_service_type;
    private View click_yuyue_time;
    private View suishoudai_big;
    private TextView click_select_suishoudai;
    private View delete_ayi;
    private View progressBar1;
    private boolean need_hidden = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.home_zdg_ok_view2);
        need_hidden = getIntent().getBooleanExtra("need_hidden", false);
        init();
        init_click_detailed_info_click();
        init_click_service_type();
        init_back();
        init_select_ayi_click();//选择阿姨的点击---ok
        init_time_click();//时间选择---ok
        init_isuishoudai();
        if (getIntent().getBooleanExtra("flag", false)) {
            init_ok2();
        } else {
            init_ok();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.client_home_zdg2));
        MobclickAgent.onResume(this);
        //显示阿姨与否
        if (!ayi_id.equals("-1")) {
            delete_ayi.setVisibility(View.VISIBLE);
        } else {
            delete_ayi.setVisibility(View.GONE);
        }

        KLog.e("need_hidden"+need_hidden);
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
                    Show_toast.showText(Home_zdg_ok.this, "请选择对应城市");
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
                    Show_toast.showText(Home_zdg_ok.this, "请选择对应城市");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void init_isuishoudai() {
        suishoudai_big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (click_select_service_type.getText().toString().equals("")) {
                    Show_toast.showText(Home_zdg_ok.this, "请选择服务类型");
                    return;
                }
                if (AyiApplication.getInstance().accountService().id().equals("")) {
                    button_home_zdg_ok_btn.setEnabled(true);
                    Intent intent = new Intent(Home_zdg_ok.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent in = new Intent(Home_zdg_ok.this, Suishoudai.class);
                in.putExtra("typeid", type_num);
                if (list_suishoudai != null) {

                    in.putExtra("suishou", (Serializable) list_suishoudai);
                }
                startActivity(in);
            }
        });
    }


    private void init_click_service_type() {
        service_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_zdg_ok.this, Service_type_modify_zdg.class);
                intent.putExtra("type_num", type_num);
                intent.putExtra("type", type);
                intent.putExtra("biaozhi1", biaozhi1);
                intent.putExtra("biaozhi2", biaozhi2);
                intent.putExtra("sale", sale);
                startActivity(intent);
            }
        });
    }

    //    private void init_click_detailed_info_click() {
//        click_detailed_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(Home_zdg_ok.this,User_info_.class);
//                intent.putExtra("flag_place","zdg");
//                startActivity(intent);
//            }
//        });
//    }
    private void init_click_detailed_info_click() {
        click_detailed_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home_zdg_ok.this, User_info_before.class);
                intent.putExtra("flag_place", "zdg");
                intent.putExtra("flag", getIntent().getBooleanExtra("flag", false));
                startActivity(intent);
            }
        });
    }

    private String ayi_id = "-1";
    private String type = "";
    private String type_num = "";
    private String price_total = "";
    private String num_total = "";
    private String size_total = "";
    private String meici = "";
    private String taishu_xiaoshi = "";
    private TextView ayi_name;
    private String json_arr = "";
    private int biaozhi1 = 0;
    private int biaozhi2 = 0;
    int lie1 = -1;
    int lie2 = -1;
    private String sale = "1";

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getIntExtra("user_info", 0) == 1) {
            ayi_id = "-1";
            ayi_name.setText("");
        }
        //预约时间
        if (intent.getStringExtra("yuyue_time_string") != null) {
            lie1 = intent.getIntExtra("lie1", -1);
            lie2 = intent.getIntExtra("lie2", -1);
            yuyue_time_string = intent.getStringExtra("yuyue_time_string");
            time_start_unix = intent.getStringExtra("time_start_unix");
            time_finish_unix = intent.getStringExtra("time_finish_unix");
            yuyue_time.setText(yuyue_time_string.split("\\)")[0] + ")" + "\n" + yuyue_time_string.split("\\)")[1]);
            ayi_id = "-1";
            ayi_name.setText("");
        }
        //原服务类型
        if (intent.getStringExtra("price_total") != null) {
            biaozhi1 = intent.getIntExtra("biaozhi1", 1);
            biaozhi2 = intent.getIntExtra("biaozhi2", 1);
            System.out.println(biaozhi1 + "----," + biaozhi2);
//            type = intent.getStringExtra("service_type");
//            type_num = intent.getStringExtra("service_type_id");
            price_total = intent.getStringExtra("price_total");
            num_total = intent.getStringExtra("num_total");
            size_total = intent.getStringExtra("size_total");
            taishu_xiaoshi = intent.getStringExtra("taishu_xiaoshi");
            meici = intent.getStringExtra("meici_time");
            sale = intent.getStringExtra("sale");
            ayi_id = "-1";
            ayi_name.setText("");
            json_arr = "";
            click_select_suishoudai.setText("");
            yuyue_time.setText("");
            list_suishoudai.clear();
            lie1 = -1;
            lie2 = -1;
            try {
                System.out.println("size_total:" + size_total + "-" + price_total + "-" + sale);
                if (type_num.equals("6")) {

                    click_select_service_type.setText(num_total + "台," + df.format(Double.parseDouble(price_total) * Double.parseDouble(sale)) + "元");
                } else {
                    click_select_service_type.setText(size_total + "," + df.format(Double.parseDouble(price_total) * Double.parseDouble(sale)) + "元");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //这是阿姨的ayi_name&ayi_id
        if (intent.getSerializableExtra("ayi_info") != null) {
            System.out.println("再次回来了" + intent.getSerializableExtra("ayi_info"));
            item_ayi_list item_ayi = (item_ayi_list) intent.getSerializableExtra("ayi_info");
            ayi_id = item_ayi.getId();
            ayi_name.setText(item_ayi.getName());
        }

        /*
        随手带--关键的 json_arr
         */
        if (intent.getStringExtra("flag_suishou") != null) {
            list_suishoudai = (List<suishoudai_guodu>) intent.getSerializableExtra("flag_suishoudai");
            String suishoudai_string = "";
            for (int i = 0; i < list_suishoudai.size(); i++) {
                suishoudai_string = suishoudai_string + list_suishoudai.get(i).getName();
                suishoudai_string = suishoudai_string + " * ";
                suishoudai_string = suishoudai_string + list_suishoudai.get(i).getNum();
                suishoudai_string = suishoudai_string + "    ";
            }
            click_select_suishoudai.setText(suishoudai_string);
            json_arr = intent.getStringExtra("flag_object");


        }
    }

    List<suishoudai_guodu> list_suishoudai = new ArrayList<>();
    String user_name = "";
    String user_phone = "";

    private void init_select_ayi_click() {
        select_ayi_by_hand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (yuyue_time.getText().toString().isEmpty()) {
                    Show_toast.showText(Home_zdg_ok.this, "请选择时间");
                    return;
                }
                if (latitude.equals("") || longitude.equals("")) {
                    Show_toast.showText(Home_zdg_ok.this, "请输入具体地址");
                    return;
                }
                Intent intent = new Intent(Home_zdg_ok.this, Ayi_list_new.class);
                intent.putExtra("flag", "zdg");
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                intent.putExtra("type_num", type_num);
                intent.putExtra("time_start", time_start_unix);
                intent.putExtra("time_finish", time_finish_unix);
                startActivity(intent);
            }
        });
    }

    private TextView yuyue_time;
    private String yuyue_time_string = "";//预约时间字符串
    private String time_start_unix = "";
    private String time_finish_unix = "";

    /**
     * 时间选择
     */
    private void init_time_click() {
        click_yuyue_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (place1.getText().toString().isEmpty()) {
                    Show_toast.showText(Home_zdg_ok.this, "请完善详细资料");
                    return;
                }
                if (click_select_service_type.getText().equals("")) {
                    Show_toast.showText(Home_zdg_ok.this, "请选择服务价格");
                    return;
                }
                Intent intent = new Intent(Home_zdg_ok.this, Zdg_time_select.class);
                if (lie1 != -1) {
                    intent.putExtra("lie1", lie1);
                    intent.putExtra("lie2", lie2);
                }
                intent.putExtra("typeid", type_num);
                String time_dur = "";
                if (type_num.equals("6")) {
                    time_dur = String.valueOf(Double.parseDouble(taishu_xiaoshi) * 3600);
                } else {
                    time_dur = String.valueOf(Double.parseDouble(meici) * 3600);
                }
                ;
                intent.putExtra("time_dur", time_dur);
                intent.putExtra("latitude", latitude);
                intent.putExtra("longitude", longitude);
                startActivity(intent);

            }
        });
    }

    private ArrayAdapter<String> adapter_spinner1;

    private void init_back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private TextView place1;
    private String latitude = "";
    private String longitude = "";
    DecimalFormat df;

    private void init_ok() {
        button_home_zdg_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button_home_zdg_ok_btn.setEnabled(false);
                //多一重保险
                if (AyiApplication.getInstance().accountService().id().equals("")) {
                    button_home_zdg_ok_btn.setEnabled(true);
                    Intent intent = new Intent(Home_zdg_ok.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                if (place1.getText().toString().equals("")) {
                    Show_toast.showText(Home_zdg_ok.this, "用户资料不完整，请点击填写");
                    button_home_zdg_ok_btn.setEnabled(true);
                    return;
                }
                if (yuyue_time.getText().toString().isEmpty()) {
                    Show_toast.showText(Home_zdg_ok.this, "预约时间不能为空");
                    button_home_zdg_ok_btn.setEnabled(true);
                    return;
                }

                progressBar1.setVisibility(View.VISIBLE);
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.setTimeout(20000);
                String url = RetrofitUtil.url_order_add;
                RequestParams requestParams = new RequestParams();
                requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                requestParams.put("isvalet", 0);
                requestParams.put("token", AyiApplication.getInstance().accountService().token());
                requestParams.put("contacts", user_name);//联系人
                requestParams.put("contact_phone", user_phone);//联系手机号
                requestParams.put("areaid", AyiApplication.area_id);
                requestParams.put("areaname", AyiApplication.place);
                requestParams.put("contact_address", place1.getText().toString());//两个地址相加
                requestParams.put("latitude", latitude);//维度得到的具体坐标
                requestParams.put("longitude", longitude);//经度
                requestParams.put("service_type_id", type_num);//服务类型id,参照服务类型表
                requestParams.put("service_type", type);//前者的中文名字
                requestParams.put("price", price_total);//总的钱
                requestParams.put("size", size_total);//总的尺寸
                requestParams.put("num", num_total);//油烟机  没有则-1
                requestParams.put("service_time", "-1");
                requestParams.put("sp_price_from", "");//心理价位
                requestParams.put("sp_price_to", "");//心理价位
                requestParams.put("time_start", time_start_unix);
                requestParams.put("time_finish", time_finish_unix);
                requestParams.put("day_fre", "-1");
                requestParams.put("day_start", "-1");
                requestParams.put("day_finish", "-1");
                requestParams.put("period", "-1");
                requestParams.put("end", "3");
                requestParams.put("cleaner_id", ayi_id.equals("-1") ? "0" : ayi_id);//选择清洁工0是没有选择
                requestParams.put("remark", zdg_beizhu.getText().toString());//备注
//                requestParams.put("payment","1");//0表示服务后付款1表示马上付款
//                requestParams.put("trialorder","1");//可以不传
                requestParams.put("sale", sale);

                if (!json_arr.equals("")) {
                    try {
                        requestParams.put("merchandise", new JSONArray(json_arr));//这个好像是随手带
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    requestParams.put("merchandise", "");
                }
                KLog.e("submit:", sale + "," + price_total);

                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        super.onSuccess(statusCode, headers, jsonObject);
                        try {
                            progressBar1.setVisibility(View.GONE);
                            button_home_zdg_ok_btn.setEnabled(true);
                            System.out.println(jsonObject.toString());
                            Intent intent = new Intent(Home_zdg_ok.this, Order_pay.class);//跳转去支付
                            intent.putExtra("type", type);//这是服务内容。
                            intent.putExtra("areaid", AyiApplication.area_id);//这是区域地址
                            intent.putExtra("type_num", type_num);
                            intent.putExtra("time_start", jsonObject.getJSONObject("data").getString("timeService"));//开始的预约时间
                            intent.putExtra("user_name", jsonObject.getJSONObject("data").getString("contacts"));
                            intent.putExtra("place", jsonObject.getJSONObject("data").getString("address"));//地方
                            intent.putExtra("price", jsonObject.getJSONObject("data").getString("pricetotal"));
//                            List<mlist_pay> list_item=new ArrayList<mlist_pay>();
//                            for (int i=0;i<jsonObject.getJSONObject("data").getJSONArray("mList").length();i++){
//                                mlist_pay item=new mlist_pay();
//                                item.setPrice(jsonObject.getJSONObject("data").getJSONArray("mList").getJSONObject(i).getString("price"));
//                                item.setProject(jsonObject.getJSONObject("data").getJSONArray("mList").getJSONObject(i).getString("project"));
//                                item.setQuantity(jsonObject.getJSONObject("data").getJSONArray("mList").getJSONObject(i).getString("quantity"));
//                                list_item.add(item);
//                            }
//                            intent.putExtra("list", (Serializable) list_item);
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
                            intent.putExtra("ordernum", jsonObject.getJSONObject("data").getString("ordernum"));
                            startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        System.out.println("responseString:" + responseString);
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
                //多一重保险
                if (AyiApplication.getInstance().accountService().id().equals("")) {
                    button_home_zdg_ok_btn.setEnabled(true);
                    Intent intent = new Intent(Home_zdg_ok.this, LoginActivity.class);
                    startActivity(intent);
                    return;
                }

                if (place1.getText().toString().equals("")) {
                    Show_toast.showText(Home_zdg_ok.this, "用户资料不完整，请点击填写");
                    button_home_zdg_ok_btn.setEnabled(true);
                    return;
                }
                if (yuyue_time.getText().toString().isEmpty()) {
                    Show_toast.showText(Home_zdg_ok.this, "预约时间不能为空");
                    button_home_zdg_ok_btn.setEnabled(true);
                    return;
                }
                progressBar1.setVisibility(View.VISIBLE);
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.setTimeout(20000);
                String url = RetrofitUtil.url_order_add;
                RequestParams requestParams = new RequestParams();
                requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                requestParams.put("isvalet", 1);
                requestParams.put("token", AyiApplication.getInstance().accountService().token());
                requestParams.put("contacts", user_name);//联系人
                requestParams.put("contact_phone", user_phone);//联系手机号
                requestParams.put("areaid", AyiApplication.area_id);
                requestParams.put("areaname", AyiApplication.place);
                requestParams.put("contact_address", place1.getText().toString());//两个地址相加
                requestParams.put("latitude", latitude);//维度得到的具体坐标
                requestParams.put("longitude", longitude);//经度
                requestParams.put("service_type_id", type_num);//服务类型id,参照服务类型表
                requestParams.put("service_type", type);//前者的中文名字
                requestParams.put("price", price_total);//总的钱
                requestParams.put("size", size_total);//总的尺寸
                requestParams.put("num", num_total);//油烟机  没有则-1
                requestParams.put("service_time", "-1");
                requestParams.put("sp_price_from", "");//心理价位
                requestParams.put("sp_price_to", "");//心理价位
                requestParams.put("time_start", time_start_unix);
                requestParams.put("time_finish", time_finish_unix);
                requestParams.put("day_fre", "-1");
                requestParams.put("day_start", "-1");
                requestParams.put("day_finish", "-1");
                requestParams.put("period", "-1");
                requestParams.put("end", "3");
                requestParams.put("cleaner_id", ayi_id.equals("-1") ? "0" : ayi_id);//选择清洁工0是没有选择
                requestParams.put("remark", zdg_beizhu.getText().toString());//备注
//                requestParams.put("payment","1");//0表示服务后付款1表示马上付款
//                requestParams.put("trialorder","1");//可以不传
                requestParams.put("sale", sale);

                if (!json_arr.equals("")) {
                    try {
                        requestParams.put("merchandise", new JSONArray(json_arr));//这个好像是随手带
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    requestParams.put("merchandise", "");
                }
                KLog.e("submit:", sale + "," + price_total);

                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        super.onSuccess(statusCode, headers, jsonObject);
                        try {
                            progressBar1.setVisibility(View.GONE);
                            button_home_zdg_ok_btn.setEnabled(true);
                            System.out.println(jsonObject.toString());
                            Intent intent = new Intent(Home_zdg_ok.this, Order_pay.class);//跳转去支付
                            intent.putExtra("type", type);//这是服务内容。
                            intent.putExtra("areaid", AyiApplication.area_id);//这是区域地址
                            intent.putExtra("type_num", type_num);
                            intent.putExtra("time_start", jsonObject.getJSONObject("data").getString("timeService"));//开始的预约时间
                            intent.putExtra("user_name", jsonObject.getJSONObject("data").getString("contacts"));
                            intent.putExtra("place", jsonObject.getJSONObject("data").getString("address"));//地方
                            intent.putExtra("price", jsonObject.getJSONObject("data").getString("pricetotal"));
//                            List<mlist_pay> list_item=new ArrayList<mlist_pay>();
//                            for (int i=0;i<jsonObject.getJSONObject("data").getJSONArray("mList").length();i++){
//                                mlist_pay item=new mlist_pay();
//                                item.setPrice(jsonObject.getJSONObject("data").getJSONArray("mList").getJSONObject(i).getString("price"));
//                                item.setProject(jsonObject.getJSONObject("data").getJSONArray("mList").getJSONObject(i).getString("project"));
//                                item.setQuantity(jsonObject.getJSONObject("data").getJSONArray("mList").getJSONObject(i).getString("quantity"));
//                                list_item.add(item);
//                            }
//                            intent.putExtra("list", (Serializable) list_item);
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
                            intent.putExtra("ordernum", jsonObject.getJSONObject("data").getString("ordernum"));
                            startActivity(intent);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        System.out.println("responseString:" + responseString);
                    }
                });


            }
        });
    }

    private View click_detailed_info;

    private void init() {


        progressBar1 = findViewById(R.id.progressBar1);
        delete_ayi = findViewById(R.id.delete_ayi);
        delete_ayi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ayi_id = "-1";
                ayi_name.setText("");
                v.setVisibility(View.GONE);
            }
        });
        df = new DecimalFormat("######0.00");
        click_select_suishoudai = (TextView) findViewById(R.id.click_select_suishoudai);
        suishoudai_big = findViewById(R.id.suishoudai_big);
        ayi_name = (TextView) findViewById(R.id.ayi_name);
        click_yuyue_time = findViewById(R.id.click_yuyue_time);
        service_type = findViewById(R.id.service_type);
        click_detailed_info = findViewById(R.id.click_detailed_info);
        click_select_service_type = (TextView) findViewById(R.id.click_select_service_type);
        zdg_beizhu = (EditText) findViewById(R.id.zdg_beizhu);
        select_ayi_by_hand = findViewById(R.id.select_ayi_by_hand);
        yuyue_time = (TextView) findViewById(R.id.yuyue_time);
        place1 = (TextView) findViewById(R.id.place1);
        top = findViewById(R.id.top);
        back = top.findViewById(R.id.logreg_left);
        text_title = (TextView) top.findViewById(R.id.logreg_center);
        text_title.setText(getIntent().getStringExtra("title"));
        button_home_zdg_ok_btn = (Button) findViewById(R.id.button_home_zdg_ok_btn);
        type = getIntent().getStringExtra("title");
        type_num = getIntent().getStringExtra("service_id");
//        click_select_service_type.setText(type);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.client_home_zdg2));
        MobclickAgent.onPause(this);
    }


}
