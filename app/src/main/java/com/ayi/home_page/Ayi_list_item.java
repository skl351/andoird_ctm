package com.ayi.home_page;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayi.R;
import com.ayi.retrofit.RetrofitUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/9/2.
 */
public class Ayi_list_item extends Activity {
    private View top;
    private View back;
    String id = "";
    private ImageView ayi_headimg;
    private TextView ayi_name;
    private TextView ayi_sex;
    private TextView ayi_birthday;
    private TextView ayi_sfz;
    private TextView ayi_place;
    private TextView ayi_wedding;
    private TextView ayi_culture;
    private TextView ayi_phone;
    private TextView ayi_language;
    private TextView ayi_abillity;
    private TextView ayi_jazhengguishudi;
    private View ayi_sfrz;
    private View ayi_jnrz;
    private View ayi_jkrz;
    private TextView ayi_type;
    private View this_phone;
    private View button_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayi_list_item_view);
        id = getIntent().getStringExtra("cleaner_id");
        init();
        if (getIntent().getStringExtra("buxianshi") != null) {
            button_btn.setVisibility(View.GONE);
        }
        init_back();
        init_wanguo();
        init_ok();

    }

    private void init_ok() {
        button_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent();
                in.putExtra("result", id);
                setResult(RESULT_OK, in);
                finish();
            }
        });
    }

    String phone_num;

    private void init_wanguo() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_ayi_info;
        RequestParams requestParams = new RequestParams();
        requestParams.put("cleaner_id", id);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    System.out.println(response);
                    JSONObject data = response.getJSONObject("data");
                    ImageLoader.getInstance().displayImage(data.getString("headimg"), ayi_headimg);
                    ayi_name.setText(data.getString("name"));
                    ayi_sex.setText(data.getString("sex").equals("0") ? "♂" : "♀");
                    if (ayi_sex.getText().toString().equals("♂")) {
                        ayi_sex.setTextColor(getResources().getColor(R.color.xiaobule));
                    }
                    ayi_birthday.setText(data.getString("birthday"));
                    ayi_sfz.setText(data.getString("identitycard"));
                    ayi_place.setText(data.getString("place"));
                    ayi_wedding.setText(data.getString("marriage").equals("0") ? "已婚" : "未婚");
                    ayi_culture.setText(data.getString("culture").equals("0") ? "初中" : (data.getString("culture").equals("1") ? "高中" : "大学"));
                    ayi_phone.setText("拨打电话");
                    ayi_phone.setTextColor(Color.RED);
                    phone_num = data.getString("phone");
                    ayi_language.setText(data.getString("language"));
                    ayi_abillity.setText(data.getString("speciality"));
                    ayi_jazhengguishudi.setText(data.getString("company"));
                    ayi_type.setText(data.getString("type"));
                    if (data.getString("identity_auth").equals("0")) {
                        ayi_sfrz.setVisibility(View.GONE);
                    }
                    if (data.getString("skill_auth").equals("0")) {
                        ayi_jnrz.setVisibility(View.GONE);
                    }
                    if (data.getString("health_auth").equals("0")) {
                        ayi_jkrz.setVisibility(View.GONE);
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

    private void init_back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;

    private void init() {
        button_btn = findViewById(R.id.button_btn);
        ayi_type = (TextView) findViewById(R.id.ayi_type);
        top = findViewById(R.id.top);
        back = top.findViewById(R.id.logreg_left);
        ayi_headimg = (ImageView) findViewById(R.id.ayi_headimg);
        ayi_name = (TextView) findViewById(R.id.ayi_name);
        ayi_sex = (TextView) findViewById(R.id.ayi_sex);
        ayi_birthday = (TextView) findViewById(R.id.ayi_birthday);
        ayi_sfz = (TextView) findViewById(R.id.ayi_sfz);
        ayi_place = (TextView) findViewById(R.id.ayi_place);
        ayi_wedding = (TextView) findViewById(R.id.ayi_wedding);
        ayi_culture = (TextView) findViewById(R.id.ayi_culture);
        ayi_phone = (TextView) findViewById(R.id.ayi_phone);
        ayi_language = (TextView) findViewById(R.id.ayi_language);
        ayi_abillity = (TextView) findViewById(R.id.ayi_abillity);
        ayi_jazhengguishudi = (TextView) findViewById(R.id.ayi_jazhengguishudi);
        ayi_sfrz = findViewById(R.id.ayi_sfrz);
        ayi_jnrz = findViewById(R.id.ayi_jnrz);
        ayi_jkrz = findViewById(R.id.ayi_jkrz);
        this_phone = findViewById(R.id.this_phone);
        if (getIntent().getStringExtra("flag_ord") != null) {
            if (getIntent().getStringExtra("flag_ord").equals("1") || getIntent().getStringExtra("flag_ord").equals("2") || getIntent().getStringExtra("flag_ord").equals("3")) {
                this_phone.setVisibility(View.VISIBLE);
                this_phone.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(Ayi_list_item.this);
                        final AlertDialog alert = builder.create();
                        View view = LayoutInflater.from(Ayi_list_item.this).inflate(R.layout.calcel_rejectok, null);
                        TextView textView = (TextView) view.findViewById(R.id.title);
                        textView.setText(phone_num);
                        View calcel = view.findViewById(R.id.cancel_btn);
                        Button ok_bt = (Button) view.findViewById(R.id.ok_btn);
                        ok_bt.setText("呼叫");
                        calcel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();

                            }
                        });
                        ok_bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Uri url = Uri.parse("tel:" + phone_num);
                                Intent intent = new Intent(Intent.ACTION_DIAL, url);
                                startActivity(intent);
                                alert.dismiss();
                            }
                        });
                        alert.setView(view);
                        alert.show();

//                        if (Build.VERSION.SDK_INT >= 23) {
//                            KLog.e("error", "进来这里2");
//                            int checkCallPhonePermission = ContextCompat.checkSelfPermission(Ayi_list_item.this, Manifest.permission.CALL_PHONE);
//                            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
//                                KLog.e("error", "进来这里3");
//                                ActivityCompat.requestPermissions(Ayi_list_item.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE_ASK_CALL_PHONE);
//                                return;
//                            } else {
//                                KLog.e("error", "进来这里4");
//                                //上面已经写好的拨号方法
//                                Uri url = Uri.parse("tel:"+phone_num);
//                                Intent intent = new Intent(Intent.ACTION_DIAL, url);
//                                startActivity(intent);
//                            }
//                        } else {
//                            KLog.e("error", "进来这里5");
//                            //上面已经写好的拨号方法
//                            Uri url = Uri.parse("tel:"+phone_num);
//                            Intent intent = new Intent(Intent.ACTION_DIAL, url);
//                            startActivity(intent);
//                        }


                    }
                });
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    KLog.e("error", "进来这里6");
                    // Permission Granted
                    Uri url = Uri.parse("tel:" + phone_num);
                    Intent intent = new Intent(Intent.ACTION_DIAL, url);
                    startActivity(intent);
                } else {
                    KLog.e("error", "进来这里7");
                    // Permission Denied
                    Toast.makeText(Ayi_list_item.this, "电话拨号被禁止", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}

