package com.ayi.zidingyi_view;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.MainActivity;
import com.ayi.activity.StartActivity;
import com.ayi.entity.UserInfo;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Show_toast;
import com.ayi.utils.User_flag;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

/**
 * Created by android on 2016/4/20.
 */
public class laun_activity extends Activity {

    public static final int REQUEST_PERMISSION_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        User_flag.is_first = true;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.laun_activity);
        Handler x = new Handler();
        x.postDelayed(new splashhandler(), 2000);
//        if (!User.is_first){
//
//        }else {
//            Intent intent=new Intent(laun_activity.this,MainActivity.class);
//            startActivity(intent);
//            laun_activity.this.finish();
//        }

    }

    class splashhandler implements Runnable {
        public void run() {
            flag_network();
        }
    }

    private void do_some() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int checkCode = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCode != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE);
                return;
            }
            yun();
        }
        yun();
//        UserInfo userInfo = AyiApplication.getInstance().accountService().profile();
//        if (userInfo == null || userInfo.getVerifyCode() == null || userInfo.getVerifyCode().equals("")) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                int checkCode = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
//                if (checkCode != PackageManager.PERMISSION_GRANTED) {
//                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_CODE);
//                    return;
//                }
//                yun();
//            }
//            yun();
//        } else {
//            get_5info();//bak
//        }
    }

    //客户端不管定位与否都进入app
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        try {
            switch (requestCode) {
                case REQUEST_PERMISSION_CODE:
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        KLog.e("error", "进来这里6");

                        yun();
                    } else {
                        yun();
                    }
                    break;
                default:
                    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            }
        } catch (Exception e) {
            yun();
        }
    }

    public void yun() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        if (getIntent().getIntExtra("status", 0) == 1) {
            intent.putExtra("status", 1);
        }
        startActivity(intent);
        laun_activity.this.finish();
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
//                Show_toast.showText(laun_activity.this, "无网络连接");
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
                            laun_activity.this.finish();
                        }
                    }).show();
            ad.setCancelable(false);

        } else {
            System.out.println("有网络");
            do_some();
        }
    }

}