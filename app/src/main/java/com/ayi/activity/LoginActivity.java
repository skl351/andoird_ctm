package com.ayi.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.actions.MyInfoActionsCreator;
import com.ayi.entity.UserInfo;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.stores.MyInfoStore;
import com.ayi.utils.Show_toast;
import com.ayi.zidingyi_view.VerificationCodeView;
import com.milk.base.BaseActivity;
import com.milk.flux.stores.Store;
import com.socks.library.KLog;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 没登录就先登录
 * Created by oceanzhang on 16/3/24.
 */
public class LoginActivity extends TempletActivity<MyInfoStore, MyInfoActionsCreator> {
    @Bind(R.id.act_login_input_phone)
    EditText etInputPhone;
    @Bind(R.id.act_login_input_verify_code)
    EditText etInputVerifyCode;
    @Bind(R.id.act_login_agreement_checkbox)
    CheckBox agreement_checkbox;
    @Bind(R.id.act_login_agreement_link)
    TextView agreement_link;
    @Bind(R.id.act_login_btn_verify_code)
    TextView but_yzm;
    private String backUrl;
    private int index;
    private ProgressDialog dialog;

//    private VerificationCodeView act_login_btn_verify_code2;
//    private EditText act_login_input_verify_code2;

    @Override
    public void initView() {
        super.initView();
        hideTitleBar();
        get_5info();//获取所有信息；
        UserInfo userInfo = AyiApplication.getInstance().accountService().profile();
        System.out.println("进入2这里");
        if (userInfo != null) {
            System.out.println("进入3这里");
            if (userInfo.getMobile() != null && userInfo.getVerifyCode() != null) {
                System.out.println("进入4这里");
                AyiApplication.getInstance().accountService().logout();
//                return;
            }
        }
        setView(R.layout.activity_login);
//        act_login_btn_verify_code2= (VerificationCodeView) findViewById(R.id.act_login_btn_verify_code2);
//        act_login_btn_verify_code2.setPointInterfere(false);
//        act_login_input_verify_code2= (EditText) findViewById(R.id.act_login_input_verify_code2);
        agreement_link.setText((Html.fromHtml("同意<font color='blue'>《用户协议》</font>")));
        etInputPhone.setLongClickable(false);
        etInputPhone.setTextIsSelectable(false);
        etInputVerifyCode.setLongClickable(false);
        etInputVerifyCode.setTextIsSelectable(false);
        String phone = getSharedPreferences("account", MODE_PRIVATE).getString("phone", "");
        if (!TextUtils.isEmpty(phone)) {
            etInputPhone.setText(phone);
        }
        backUrl = getIntent().getStringExtra("backurl");
        index = getIntent().getIntExtra("index", 0);
        int status = getIntent().getIntExtra("status", 0);
        if (status == 1) {
            Intent intent = new Intent(LoginActivity.this, UpdateActivity.class);
            startActivity(intent);
        }
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    @Override
    protected boolean flux() {
        return true;
    }

    @Override
    protected void updateView(Store.StoreChangeEvent event) {
        super.updateView(event);
        if (event.code == 1) { // get verifyCode
            if (event.error) {
                showToast("获取手机验证码失败.");
            } else {
                showToast("获取手机验证码成功.");
            }
        } else if (event.code == 2) { //login
            if (event.error) {
                showToast("验证码错误.");
            } else {

                getSharedPreferences("account", MODE_PRIVATE).edit().putString("phone", etInputPhone.getText().toString()).apply();
                showToast("登录成功.");
                //这个针对个人中心
                Intent mIntent = new Intent(RetrofitUtil.APP_BORADCASTRECEIVER);
                sendBroadcast(mIntent);
                //这个针对首页中4个界面
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
    }

    android.os.Handler handler = new android.os.Handler();
    private int falg2 = 60;
    Runnable runnable;

    @OnClick({R.id.act_login_btn_verify_code, R.id.act_login_btn_submit_login, R.id.act_login_agreement_link})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        String phone;
        switch (v.getId()) {
            case R.id.act_login_btn_verify_code:

                phone = etInputPhone.getText().toString();
                String phone_zhenz = "^1\\d{10}$";
                if (!phone.matches(phone_zhenz)) {
                    Toast.makeText(LoginActivity.this, "手机号码输入有误", Toast.LENGTH_SHORT).show();
                    return;
                }
                long ss = Long.parseLong(getTime()) - Long.parseLong(AyiApplication.getInstance().getcodetime());
                if (ss < 60) {
                    showToast("请再等待" + (60 - ss) + "秒");
                    break;
                }
                AyiApplication.getInstance().setcodetime(getTime());
                but_yzm.setEnabled(false);
                but_yzm.setText("" + falg2 + "秒");
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (falg2 <= 1) {
                                but_yzm.setEnabled(true);
                                but_yzm.setText("获取验证码");
                                falg2 = 60;
                                return;
                            }
                            but_yzm.setText(Integer.toString(--falg2) + "秒");
                            handler.postDelayed(this, 1000);
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                };
                handler.postDelayed(runnable, 0); //每隔1s执行
                if (!TextUtils.isEmpty(phone)) {
                    actionsCreator().getVerifyCode(phone);
                } else {
                    showToast("请输入手机号");
                }
                break;
            case R.id.act_login_btn_submit_login:
                if (!agreement_checkbox.isChecked()) {
                    showToast("请先同意《用户协议》");
                    return;
                }
                phone = etInputPhone.getText().toString();

                String verifyCode = etInputVerifyCode.getText().toString();
                if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(verifyCode)) {
                    String phone_zhenz2 = "^1\\d{10}$";
                    if (!phone.matches(phone_zhenz2)) {
                        Toast.makeText(LoginActivity.this, "手机号码输入有误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    KLog.e("location:" + AyiApplication.lat + "," + AyiApplication.logna + "," + all_info);
                    actionsCreator().login(phone, verifyCode, AyiApplication.lat, AyiApplication.logna, "登陆" + all_info);
                } else {
                    showToast("请输入手机号和验证码.");
                }
                break;
            case R.id.act_login_agreement_link:
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://doc.sangeayi.com/usercontract.html");
                startActivity(intent);
                break;
        }

    }

    public static void showLoginActivity(BaseActivity activity, String backUrl) {
        activity.startActivity("ayi://login?backUrl=" + backUrl);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //在这个地方修改了1变成2
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("tab", "0");
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static String getAppVersionName(Context context) {
        double versionName = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = Double.valueOf(pi.versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "" + versionName;
    }

    String net_ip = "";
    String phone_model = "";
    String sdk_version = "";
    String android_version = "";
    String app_version = "";
    String all_info = "";
    String data_info = "";

    public void get_5info() {
        phone_model = android.os.Build.MODEL;
        sdk_version = "" + Build.VERSION.SDK_INT;
        android_version = Build.VERSION.RELEASE;
        app_version = getAppVersionName(this);

        ConnectivityManager mConnectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
//检查网络连接
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();
        if (info == null || !mConnectivity.getBackgroundDataSetting()) {
            Show_toast.showText(LoginActivity.this, "无网络连接");
            return;
        } else {
            System.out.println("有网络");
        }
        int netType = info.getType();
        int netSubtype = info.getSubtype();

        if (netType == ConnectivityManager.TYPE_WIFI) {  //WIFI
            data_info = "wifi";
            new Thread(new Runnable() {
                @Override
                public void run() {
                    net_ip = GetNetIp();
//                    System.out.println("GetNetIp" + GetNetIp());
                    Date currentTime = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date1 = formatter.format(currentTime);
                    all_info = net_ip + "|" + phone_model + "|" + sdk_version + "|" + android_version + "|" + app_version + "|" + data_info + "|" + date1;
                }
            }).start();

        } else {

            switch (netSubtype) {
//如果是2g类型
                case TelephonyManager.NETWORK_TYPE_GPRS: // 联通2g
                    data_info = "联通2g";
                    break;
                case TelephonyManager.NETWORK_TYPE_CDMA: // 电信2g
                    data_info = "电信2g";
                    break;
                case TelephonyManager.NETWORK_TYPE_EDGE: // 移动2g
                    data_info = "移动2g";
                    break;
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    data_info = "2g";
                    break;
//如果是3g类型
                case TelephonyManager.NETWORK_TYPE_EVDO_A: // 电信3g
                    data_info = "电信3g";
                    break;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    data_info = "3g";
                    break;
//如果是4g类型
                case TelephonyManager.NETWORK_TYPE_LTE:
                    data_info = "4g";
                    break;
                default:
                    data_info = "默认G";
                    break;
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    net_ip = "" + GetNetIp();
                    Date currentTime = new Date();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date1 = formatter.format(currentTime);
                    all_info = net_ip + "|" + phone_model + "|" + sdk_version + "|" + android_version + "|" + app_version + "|" + data_info + "|" + date1;
                }
            }).start();

        }


    }

    public static String GetNetIp() {
        try {
            String address = "http://pv.sohu.com/cityjson?ie=utf-8";
            KLog.e("获取外网地址：http://pv.sohu.com/cityjson?ie=utf-8");
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setUseCaches(false);

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();

// 将流转化为字符串
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(in));

                String tmpString = "";
                StringBuilder retJSON = new StringBuilder();
                while ((tmpString = reader.readLine()) != null) {
                    retJSON.append(tmpString + "\n");
                }

                String str = retJSON.toString().split("\\{")[1];
                str = "{" + str;
                KLog.e("jsonstring:" + str);
                JSONObject jsonObject = new JSONObject(str);
                String cip = jsonObject.getString("cip");
                System.out.println("cip:" + cip);
                return cip;
            } else {
                Log.e("提示", "网络连接异常，无法获取IP地址！");
            }
        } catch (Exception e) {
            Log.e("提示", "获取IP地址时出现异常，异常信息是：" + e.toString());
        }
        return "";
    }

    public String getTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        return str;
    }
}
