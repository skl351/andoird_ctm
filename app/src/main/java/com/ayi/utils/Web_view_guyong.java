package com.ayi.utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ayi.R;

/**
 * Created by Administrator on 2016/10/18.
 */
public class Web_view_guyong extends Activity {

    private WebView web_view;

    private View top;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_skl);
        init();
        init_back();
        web_view.loadUrl("http://doc.sangeayi.com/employment.html");
        WebSettings setting = web_view.getSettings();
        setting.setJavaScriptEnabled(true);//支持js
        setting.setDefaultTextEncodingName("utf-8");//设置字符编码


        //覆盖默认打开网页的行为，使在webview中显示
        web_view.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //true 控制网页在webview中打开 false 在浏览器中打开
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void init_back() {
        findViewById(R.id.top).findViewById(R.id.logreg_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        title= (TextView) findViewById(R.id.logreg_center);
        title.setText("雇用合同模板");
        top= findViewById(R.id.top);
        web_view= (WebView) findViewById(R.id.web_view);
    }
}
