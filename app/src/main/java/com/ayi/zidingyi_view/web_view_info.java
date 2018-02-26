package com.ayi.zidingyi_view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Data_time_cuo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2017/5/31.
 */

public class web_view_info extends Activity {
    //    private  TextView text_cent;
    private WebView webview;
    private View back;
    private TextView title;
    private TextView time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_info_view);
//        text_cent= (TextView) findViewById(R.id.top).findViewById(R.id.logreg_center);
        webview = (WebView) findViewById(R.id.webview);
        back = findViewById(R.id.top).findViewById(R.id.logreg_left);
        time = (TextView) findViewById(R.id.time);
        title = (TextView) findViewById(R.id.title);
        init_wangluo();
        init_back();
    }

    private void init_back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init_wangluo() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_return_getinfo;//测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("id", getIntent().getStringExtra("id"));

        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                System.out.println(jsonObject.toString());
                try {
                    if (jsonObject.getString("ret").equals("200")) {
                        title.setText(jsonObject.getJSONObject("data").getString("title"));
                        time.setText(Data_time_cuo.gettime2(jsonObject.getJSONObject("data").getString("timestamp")));
//                        text_cent.setText(jsonObject.getJSONObject("data").getString("title"));
                        webview.loadDataWithBaseURL(null, jsonObject.getJSONObject("data").getString("content"), "text/html", "utf-8", null);
                    }
//                    text_content.setText();
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
}
