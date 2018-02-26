package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.retrofit.RetrofitUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.library.KLog;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/10/17.
 */
public class Suishoudai_thing extends Activity {
    private double screenWidth;
    private Button ok;
    private View edit_num;
    private View jianqu;
    private View add;
    private EditText num_text;
    private TextView shop_name;
    private TextView shop_guige;
    private TextView shop_year;
    private TextView shop_price;
    private ImageView image;
    private TextView shop_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suishoudai_thing_view);
        //得到频宽；
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        init();
        init_yunsuan();
        init_ok();
        init_wangluo();
init_back();
    }
    private void init_back() {
        findViewById(R.id.top). findViewById(R.id.logreg_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void init_wangluo() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url= RetrofitUtil.url_suishoudai_item_info;
        RequestParams requestParams=new RequestParams();
        requestParams.put("typeid",getIntent().getStringExtra("typeid"));
        requestParams.put("areaid", AyiApplication.area_id);
        requestParams.put("mid",getIntent().getStringExtra("flag"));
        asyncHttpClient.post(url,requestParams,new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    KLog.d("随手带",jsonObject.toString());
                    JSONObject jsonObject_data=  jsonObject.getJSONObject("data");
                     shop_name.setText(jsonObject_data.getString("name"));
                    shop_guige.setText(jsonObject_data.getString("spec"));
                    shop_price.setText(jsonObject_data.getString("price"));
                    shop_year.setText(jsonObject_data.getString("quality_time"));
                    shop_info.setText(jsonObject_data.getString("detail"));
                    ImageLoader.getInstance().displayImage(jsonObject_data.getString("big_img"),image);
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

    private void init_ok() {
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Suishoudai_thing.this,Suishoudai.class);
                intent.putExtra("num",getIntent().getStringExtra("num"));
                intent.putExtra("duoshao",num_text.getText().toString());
                startActivity(intent);

            }
        });
    }

    private void init_yunsuan() {
        jianqu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(num_text.getText().toString())-1>=0){
                    num_text.setText(""+(Integer.valueOf(num_text.getText().toString())-1));
                    num_text.setSelection(num_text.getText().toString().length());
                }

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.valueOf(num_text.getText().toString())+1<=99){
                    num_text.setText(""+(Integer.valueOf(num_text.getText().toString())+1));
                    num_text.setSelection(num_text.getText().toString().length());
                }



            }
        });
    }

    private void init() {
        shop_info= (TextView) findViewById(R.id.shop_info);
        image= (ImageView) findViewById(R.id.image);
        shop_name= (TextView) findViewById(R.id.shop_name);
        shop_guige= (TextView) findViewById(R.id.shop_guige);
        shop_year= (TextView) findViewById(R.id.shop_year);
        shop_price= (TextView) findViewById(R.id.shop_price);

        num_text= (EditText) findViewById(R.id.num_text);
        jianqu=findViewById(R.id.jianqu);
        add=findViewById(R.id.add);
        ok= (Button) findViewById(R.id.ok);
        edit_num=findViewById(R.id.edit_num);
        ok.getLayoutParams().width= (int) (screenWidth/2);
        edit_num.getLayoutParams().width= (int) (screenWidth/2);
        num_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("0")){
                    jianqu.setBackgroundResource(R.mipmap.jianhao);
                    add.setBackgroundResource(R.mipmap.jiahao_lvse);
                }else if(s.toString().equals("99")){
                    jianqu.setBackgroundResource(R.mipmap.jianhao_lvse);
                    add.setBackgroundResource(R.mipmap.jiahao);
                }else {
                    jianqu.setBackgroundResource(R.mipmap.jianhao_lvse);
                    add.setBackgroundResource(R.mipmap.jiahao_lvse);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String c="";
                if (s.toString().equals("")){
                    c="0";
                }else {
                    c=s.toString();
                }
                if(s.toString().length()>1){
                    String ss=  s.toString().substring(0,1);
                    if(ss.equals("0")){
                        c=s.toString().substring(1,2);
                    }
                }
                if(!c.equals(s.toString())){
                    num_text.setText(c);
                    num_text.setSelection(num_text.getText().toString().length());
                }
            }
        });
        if (getIntent().getStringExtra("biaohao")!=null){
            num_text.setText(getIntent().getStringExtra("biaohao"));

        }
        num_text.setSelection(num_text.getText().toString().length());

    }
}
