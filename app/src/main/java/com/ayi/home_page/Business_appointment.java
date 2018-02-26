package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.MainActivity;
import com.ayi.entity.Result;
import com.ayi.entity.item_place_info;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.FileService;
import com.ayi.utils.Show_toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/5/24.
 */

public class Business_appointment extends Activity {
    private TextView place1;
    private TextView place2;
    String user_name;
    String user_phone;
    String place_info;
    String place_info2;
    String latitude;
    String longitude;
    private View progressBar1;
    private WebView image_big;

    private View click_detailed_info;
    private View button_home_zdg_ok_btn;
    private EditText edit_hiht;
    private TextView centertext;

    private String getHtmlData(String bodyHTML) {
        String head = "<head>" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no\"> " +
                "<style>img{max-width: 100%; width:auto; height:auto;}</style>" +
                "</head>";
        return "<html>" + head + "<body><img src=\"" + bodyHTML + "\"   vspace=\"0\" border=\"0\"  width=\"100%\"/></body></html>";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.business_appoint_view);
        init_view();
        init_back();
        init_godelete();
        init_ok();
        if (getIntent().getStringExtra("remark") != null) {
            if (getIntent().getStringExtra("isimg").equals("0")) {
                image_big.setVisibility(View.VISIBLE);
                if (getIntent().getStringExtra("image") != null) {
                    if (!getIntent().getStringExtra("image").equals("")) {

                        String url_string = "<img src=\"" +
                                getIntent().getStringExtra("image")
                                + "\"   vspace=\"0\" border=\"0\"  width=\"100%\"/>";
//                        image_big.loadDataWithBaseURL(null, url_string, "text/html", "utf-8", null);
                        image_big.loadData(getHtmlData(getIntent().getStringExtra("image")), "text/html; charset=utf-8", "utf-8");

                    }
                }

            } else {
                ceshi_text.setVisibility(View.VISIBLE);
                ceshi_text.setText(getIntent().getStringExtra("text"));
            }
            edit_hiht.setEnabled(false);
            edit_hiht.setText(getIntent().getStringExtra("remark"));
            edit_hiht.setTextColor(getResources().getColor(R.color.default_textcolor));
            edit_hiht.setHint("");
            user_name = getIntent().getStringExtra("contacts");
            user_phone = getIntent().getStringExtra("contact_phone");
            place_info = getIntent().getStringExtra("contact_addr");
            place_info2 = getIntent().getStringExtra("contact_door");
            place1.setText(user_name + "    " + user_phone);
            place2.setText(place_info + "," + place_info2);
            button_home_zdg_ok_btn.setVisibility(View.GONE);
        } else {
            init_wangluo();
        }
    }

    String dg_id = "";

    private void init_wangluo() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_duig_data;//测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("areaid", AyiApplication.area_id);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    KLog.e("jsonObject" + jsonObject.toString());
                    dg_id = jsonObject.getJSONObject("data").getString("id");
                    if (jsonObject.getJSONObject("data").getString("isimg").equals("0")) {
                        image_big.setVisibility(View.VISIBLE);
                        if (jsonObject.getJSONObject("data").getString("content_img") != null) {
                            if (!jsonObject.getJSONObject("data").getString("content_img").equals("")) {
                                String url_string = "<img src=\"" +
                                        jsonObject.getJSONObject("data").getString("content_img")
                                        + "\"   vspace=\"0\" border=\"0\"  width=\"100%\"/>";
                                image_big.loadData(getHtmlData(jsonObject.getJSONObject("data").getString("content_img")), "text/html; charset=utf-8", "utf-8");

                            }
                        }
                    } else {
                        ceshi_text.setVisibility(View.VISIBLE);
                        ceshi_text.setText(jsonObject.getJSONObject("data").getString("content_txt"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressBar1.setVisibility(View.GONE);
                Show_toast.showText(Business_appointment.this, "网络繁忙，请重试.");

            }
        });
    }

    /**
     * 对公业务下单
     */
    public void Corporateadd(String areaid, String contacts, String contact_phone, String contact_addr, String contact_door, String content, String token, String userid, String dg_id) {

        RetrofitUtil.getService().Corporateadd(areaid, contacts, contact_phone, contact_addr, contact_door, content, token, userid, dg_id, AyiApplication.m).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Show_toast.showText(Business_appointment.this, "网络繁忙，请重试");
                        progressBar1.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(Result userInfoResult) {
                        KLog.e("userInfoResult" + userInfoResult);
                        progressBar1.setVisibility(View.GONE);

                        //暂时位置
                        if (userInfoResult.getRet() == 200) {
                            Show_toast.showText(Business_appointment.this, "预约成功");
                            AyiApplication.flag_tc_dg = "3";
                            Intent intent = new Intent(Business_appointment.this, MainActivity.class);
                            intent.putExtra("for_refesh", "1");
                            intent.putExtra("tab", "1");
                            startActivity(intent);
                        }
//                        onError(new Exception("error"));
                    }
                });
    }

    private void init_ok() {
        button_home_zdg_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (place1.getText().toString().equals("")) {
                    Show_toast.showText(Business_appointment.this, "请点击填写服务地址");
                    return;
                }
                Corporateadd(AyiApplication.area_id,
                        user_name, user_phone, place_info, place_info2, edit_hiht.getText().toString(), AyiApplication.getInstance().token(), AyiApplication.getInstance().userId(), dg_id);

            }
        });
    }

    private void init_godelete() {
        if (getIntent().getStringExtra("remark") == null) {
            click_detailed_info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Business_appointment.this, User_info_before.class);
                    intent.putExtra("flag_place", "duigong");
                    startActivity(intent);
                }
            });
        }

    }

    private void init_back() {
        findViewById(R.id.top).findViewById(R.id.logreg_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private TextView ceshi_text;

    private void init_view() {
        centertext = (TextView) findViewById(R.id.top).findViewById(R.id.logreg_center);
        centertext.setText("企业清洁");
        edit_hiht = (EditText) findViewById(R.id.edit_hiht);
        image_big = (WebView) findViewById(R.id.image_big);
        ceshi_text = (TextView) findViewById(R.id.ceshi_text);
        progressBar1 = findViewById(R.id.progressBar1);
        button_home_zdg_ok_btn = findViewById(R.id.button_home_zdg_ok_btn);
        place1 = (TextView) findViewById(R.id.place1);
        place2 = (TextView) findViewById(R.id.place2);
        click_detailed_info = findViewById(R.id.click_detailed_info);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getStringExtra("remark") == null) {
            FileService fileService = new FileService(this);
            try {
                item_place_info user_info = fileService.read("user.txt");
                if (user_info.getShiji_dizhi().equals(AyiApplication.place)) {
                    latitude = user_info.getLatitude();
                    longitude = user_info.getLongitide();
                    place2.setVisibility(View.VISIBLE);
                    if (!user_info.getPlace().equals("")) {
                        place1.setText(user_info.getName() + "    " + user_info.getPhone());
                        place2.setText(user_info.getPlace() + "," + user_info.getNum_place());

                    } else {
                        place1.setText("");
                        place2.setText("");
                        place2.setVisibility(View.GONE);
                    }
                    user_name = user_info.getName();
                    user_phone = user_info.getPhone();
                    place_info = user_info.getPlace();
                    place_info2 = user_info.getNum_place();
                } else {
                    place1.setText("");
                    place2.setText("");
                    place2.setVisibility(View.GONE);
                    if (!user_info.getShiji_dizhi().equals(""))
                        Show_toast.showText(Business_appointment.this, "请选择对应城市");
                }
            } catch (Exception e) {
                place1.setText("");
                place2.setText("");
                place2.setVisibility(View.GONE);
                e.printStackTrace();
            }
        } else {
            place2.setVisibility(View.VISIBLE);
        }


    }
}
