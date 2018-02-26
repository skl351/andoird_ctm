package com.ayi.utils;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.ayi.R;

/**
 * Created by Administrator on 2016/10/18.
 */
public class Web_view extends Activity {

    private WebView web_view;
    private View top;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view_skl);
        init();
        init_back();
        web_view.loadUrl(getIntent().getStringExtra("web_url"));
        WebSettings setting = web_view.getSettings();
        setting.setJavaScriptEnabled(true);//支持js
        setting.setDefaultTextEncodingName("utf-8");//设置字符编码


        //覆盖默认打开网页的行为，使在webview中显示
        web_view.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //true 控制网页在webview中打开 false 在浏览器中打开
                view.loadUrl(url);
                return true;
            }
        });
        web_view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && web_view.canGoBack()) {
                        web_view.goBack();
                        return true;
                    }
                    onBackPressed();
                }
                return false;
            }
        });
    }

    private void init_back() {
        findViewById(R.id.top).findViewById(R.id.logreg_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (web_view.canGoBack()) {
                    web_view.goBack();
                } else {
                    onBackPressed();
                }

            }
        });
    }

    private void init() {
        title = (TextView) findViewById(R.id.logreg_center);
        title.setText(getIntent().getStringExtra("title"));
        top = findViewById(R.id.top);
        web_view = (WebView) findViewById(R.id.web_view);
    }
}
