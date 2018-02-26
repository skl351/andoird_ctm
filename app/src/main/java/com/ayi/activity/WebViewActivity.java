package com.ayi.activity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.ayi.R;
import com.milk.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by oceanzhang on 16/3/29.
 */
public class WebViewActivity extends TempletActivity {
    @Bind(R.id.fragment_statistics_progressbar)
    ProgressBar progressBar;
    @Bind(R.id.fragment_statistics_webview)
    FrameLayout frameLayout;
    private WebView webView;

    @Override
    public void initView() {
        super.initView();
        String title = getIntent().getStringExtra("title");
        setTitle(title);
        setView(R.layout.activity_webview);
        webView = new WebView(this);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
                        webView.goBack();
                        return true;
                    }
                }
                return false;
            }
        });
        frameLayout.addView(webView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        webView.requestFocus();
        String url = getIntent().getStringExtra("url");
//        String url = getQueryParameter("url");
        if (!TextUtils.isEmpty(url)) {
            url = url.startsWith("http://") ? url : "http://" + url;
            webView.loadUrl(url);
        }
    }

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (progressBar != null) {
                progressBar.setProgress(newProgress);
                if (newProgress >= 100)
                    progressBar.setVisibility(View.GONE);
                else
                    progressBar.setVisibility(View.VISIBLE);
            }
            super.onProgressChanged(view, newProgress);
        }
    };

    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            super.onUnhandledKeyEvent(view, event);
            System.out.println(event.toString());
        }
    };

    public static void showWebViewActivity(BaseActivity activity, String name, String url) {
        activity.startActivity("ayiadmin://schooldetail?name=" + name + "&url=" + url);
    }

    @Override
    protected void onTitleLeftBtnClick(View v) {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            onBackPressed();
        }
    }

}
