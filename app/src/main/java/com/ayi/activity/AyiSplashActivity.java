package com.ayi.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.retrofit.ApiService;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.DataCleanManager;
import com.ayi.utils.Show_toast;
import com.ayi.zidingyi_view.laun_activity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.socks.library.KLog;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.ayi.AyiApplication.sysInitSharedPreferences;


/**
 * 得到这个基础的
 * 在这里开始，看你是否第一次安装
 * 第一次安装就进入启动轮播页面
 * 否则就直接进入主界面
 */
public class AyiSplashActivity extends Activity {
    private AyiApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        app = AyiApplication.getInstance();
        WindowManager wm = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        if (width != 0) {
            AyiApplication.widthPixels = width;
        }
        flag_network();


    }

    private void redirectTo() {
        //判断是否第一次  通过SharedPreferences来操作存储。
        if (app.getFirst_start_flag().equals("0")) {
            //第一次进入界面
            Intent intent = new Intent(AyiSplashActivity.this, StartActivity.class);
            startActivity(intent);
        } else if (app.getFirst_start_flag().equals("1")) {
            //第二次进入界面
            Intent intent = new Intent(AyiSplashActivity.this, laun_activity.class);
            intent.putExtra("status", 1);
            startActivity(intent);
        }
        finish();
    }

    public static double getAppVersionName(Context context) {
        double versionName = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = Double.valueOf(pi.versionName);
        } catch (Exception e) {

        }
        return versionName;
    }

    AlertDialog ad;

    @Override
    protected void onResume() {
        super.onResume();
        if (ad != null) {
            flag_network();
        }
    }

    public void flag_network() {
        ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            RelativeLayout dateTimeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.networkflag_page, null);
            ad = new AlertDialog.Builder(this)
                    .setTitle("网络设置")
                    .setView(dateTimeLayout)  //TODO
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            AyiSplashActivity.this.finish();
                        }
                    }).show();
            ad.setCancelable(false);

        } else {
            System.out.println("有网络");
            do_some();
        }
    }

    private void do_some() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(10000);
        String url = "http://geturl.sangeayi.com/getWebsite.php";//测试数据--得到的数据
        asyncHttpClient.post(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    KLog.e("jsonObject" + jsonObject);
//                    AyiApplication.URL="http://api.sangeayi.com/sangeayi/public/sangeayi/";
                    AyiApplication.URL = "http://" + jsonObject.getString(getResources().getString(R.string.first)) + "." +
                            jsonObject.getString(getResources().getString(R.string.content)) + "." + jsonObject.getString("last")
                            + "/sangeayi/public/sangeayi/" + "?secret_key=" + jsonObject.getString("m");
                    AyiApplication.m = jsonObject.getString("m");
                    System.out.println("AyiApplication.URL" + AyiApplication.URL);
                    AyiApplication.getInstance().setURL(AyiApplication.URL);
                    AyiApplication.getInstance().setURL_m(AyiApplication.m);
                    RetrofitUtil.init(AyiSplashActivity.this);
                    AyiApplication.version_sam = sysInitSharedPreferences.getString("version_sam", "3.9");
                    try {
                        double str = getAppVersionName(AyiSplashActivity.this);//当前版本
                        if (Double.parseDouble(AyiApplication.getInstance().getversion_sam()) < str) {
                            app.setFirst_start_flag("0");
                        }
                        DataCleanManager.cleanFiles_user(AyiSplashActivity.this);
                        AyiApplication.getInstance().setversion_sam(String.valueOf(str));
                        AyiApplication.first_start_flag = sysInitSharedPreferences.getString("first_start_flag", "0");
                        System.out.println("AyiApplication.first_start_flag");
                        redirectTo();
                    } catch (Exception e) {
                        e.printStackTrace();
                        AyiApplication.first_start_flag = sysInitSharedPreferences.getString("first_start_flag", "0");
                        redirectTo();
                    }

                } catch (Exception e) {
                    Show_toast.showText(AyiSplashActivity.this, "网络繁忙，请重试");
                    finish();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                KLog.e("网络繁忙，请重试");
                Show_toast.showText(AyiSplashActivity.this, "网络繁忙，请重试");
                finish();
            }
        });
    }
}
