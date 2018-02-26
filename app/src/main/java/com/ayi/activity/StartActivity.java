package com.ayi.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.entity.UserInfo;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Show_toast;
import com.ayi.widget.BTViewPager;
import com.ayi.zidingyi_view.laun_activity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 启动页面
 */
public class StartActivity extends Activity implements OnPageChangeListener {
    private BTViewPager start_g;
    private ViewPageAdapter adapter;
    private int[] num = {R.drawable.guidance_new1, R.drawable.guidance_new2, R.drawable.guidance_new3, R.drawable.guidance_new4};
    private LayoutInflater inflater;
    private ArrayList<View> views = new ArrayList<View>();
    private LinearLayout point_layout;
    private AyiApplication app;
    private float x1 = 0;
    private float x2 = 0;
    private float y1 = 0;
    private float y2 = 0;


    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.tab_start);

        app = AyiApplication.getInstance();
        start_g = (BTViewPager) findViewById(R.id.start_g);
        point_layout = (LinearLayout) findViewById(R.id.point_layout);
        adapter = new ViewPageAdapter(StartActivity.this);
        inflater = LayoutInflater.from(this);
        for (int i = 0; i < num.length; i++) {
            View view = inflater.inflate(R.layout.start_gallery_item, null);
            ImageView image = (ImageView) view.findViewById(R.id.image);
            View lijituyan=view.findViewById(R.id.lijituyan);
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inPreferredConfig = Bitmap.Config.RGB_565;
            opt.inPurgeable = true;
            opt.inInputShareable = true;
            // 获取资源图片
            InputStream is = getResources().openRawResource(num[i]);
            Bitmap btp = BitmapFactory.decodeStream(is, null, opt);
            image.setImageBitmap(btp);
            // image.setBackgroundResource(num[i]);
            // image.setAdjustViewBounds(true);
            if (i == num.length - 1) {
                lijituyan.setVisibility(View.VISIBLE);
                lijituyan.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        v.setEnabled(false);
                        app.setFirst_start_flag("1");
                        flag_network();
                    }
                });
            }else {
                lijituyan.setVisibility(View.GONE);
            }
            ImageView image2 = new ImageView(this);
            if (i == 0) {
                image2.setBackgroundResource(R.drawable.point_on);
            } else {
                image2.setBackgroundResource(R.drawable.point_off);
            }
            LinearLayout.LayoutParams params = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(8, 8, 8, 8);
            image2.setLayoutParams(params);
            point_layout.addView(image2);
            views.add(view);
        }
        start_g.setOnPageChangeListener(this);
        adapter.setArrayList(views);
        start_g.setAdapter(adapter);
        start_g.setAnimation(AnimationUtils.loadAnimation(StartActivity.this, R.anim.push_left_in));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ad!=null){
//            RelativeLayout dateTimeLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.networkflag_page, null);
//            TextView te= (TextView) dateTimeLayout.findViewById(R.id.text1);
//            te.setText("网络连接不可用,是否重试？");
//            ad = new AlertDialog.Builder(this)
//                    .setTitle("网络设置")
//                    .setView(dateTimeLayout)  //TODO
//                    .setPositiveButton("重试", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            flag_network();
//                        }
//                    })
//                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int whichButton) {
//                            StartActivity.this.finish();
//                        }
//                    }).show();
//            ad.setCancelable(false);
            flag_network();
        }
    }

    AlertDialog ad;
    public void flag_network(){
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
                            StartActivity.this.finish();
                        }
                    }).show();
            ad.setCancelable(false);

        } else {
            System.out.println("有网络");
            do_some();
        }
    }
    public void do_some(){
        if (Build.VERSION.SDK_INT >= 23) {
            KLog.e("error", "进来这里2");
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                KLog.e("error", "进来这里3");
                ActivityCompat.requestPermissions(StartActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                return;
            } else {
                KLog.e("error", "进来这里4");
                //上面已经写好的拨号方法
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                StartActivity.this.startActivity(intent);
                StartActivity.this.finish();
            }
        } else {
            KLog.e("error", "进来这里5");
            //上面已经写好的拨号方法
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            StartActivity.this.startActivity(intent);
            StartActivity.this.finish();
        }
    }
    //客户端不管定位与否都进入app
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 123:
                try{
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        KLog.e("error", "进来这里6");

                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        StartActivity.this.startActivity(intent);
                        StartActivity.this.finish();
                    } else {
                        KLog.e("error", "进来这里7");
                        Intent intent = new Intent(StartActivity.this, MainActivity.class);
                        StartActivity.this.startActivity(intent);
                        StartActivity.this.finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    public class ViewPageAdapter extends PagerAdapter {
        private ArrayList<View> arrayList;
        private Context context;

        public ViewPageAdapter(Context context) {
            this.context = context;
        }

        public int getCount() {
            return arrayList.size();
        }

        public boolean isViewFromObject(View arg0, Object arg1) {
            return (arg0 == arg1);
        }

        public void destroyItem(View container, int position, Object object) {
            ((ViewPager) container).removeView((View) arrayList.get(position));
        }

        public ArrayList<View> getArrayList() {
            return arrayList;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public void setArrayList(ArrayList<View> arrayList) {
            this.arrayList = arrayList;
        }

        public Object instantiateItem(View container, int position) {

            ((ViewPager) container).addView(arrayList.get(position));
            return arrayList.get(position);

        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onPageSelected(int position) {
        // TODO Auto-generated method stub

        for (int i = 0; i < point_layout.getChildCount(); i++) {
            if (i == position) {
                ImageView image = (ImageView) point_layout.getChildAt(i);
                image.setBackgroundResource(R.drawable.point_on);
            } else {
                ImageView image = (ImageView) point_layout.getChildAt(i);
                image.setBackgroundResource(R.drawable.point_off);
            }
        }
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

                String str=retJSON.toString().split("\\{")[1];
                str="{"+str;
                KLog.e("jsonstring:"+str);
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
            Show_toast.showText(StartActivity.this, "无网络连接");
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
                    all_info = net_ip + "|" + phone_model + "|" + sdk_version + "|" + android_version + "|" + app_version + "|" + data_info+"|"+date1;
                    handler_5ingo.sendEmptyMessage(0);
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
                    all_info = net_ip + "|" + phone_model + "|" + sdk_version + "|" + android_version + "|" + app_version + "|" + data_info+"|"+date1;
                    handler_5ingo.sendEmptyMessage(0);
                }
            }).start();

        }


    }
    Handler handler_5ingo = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            try{
                UserInfo userInfo = AyiApplication.getInstance().accountService().profile();
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.setTimeout(20000);
                String url = RetrofitUtil.url_zidongdenglu;//测试数据--得到的数据
                RequestParams requestParams = new RequestParams();
                requestParams.put("username", userInfo.getMobile());
                requestParams.put("password", userInfo.getVerifyCode());
                requestParams.put("latitude", AyiApplication.lat);
                requestParams.put("longitude",AyiApplication.logna);
                requestParams.put("bak",all_info);

                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        super.onSuccess(statusCode, headers, jsonObject);
                        try {
                            KLog.e("jsonObjec---t"+jsonObject);
                            AyiApplication.getInstance().setCanvalet(jsonObject.getJSONObject("data").getInt("canvalet"));
                            if (Build.VERSION.SDK_INT >= 23) {
                                KLog.e("error", "进来这里2");
                                int checkCallPhonePermission = ContextCompat.checkSelfPermission(StartActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                                    KLog.e("error", "进来这里3");
                                    ActivityCompat.requestPermissions(StartActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 123);
                                    return;
                                } else {
                                    KLog.e("error", "进来这里4");
                                    //上面已经写好的拨号方法
                                    Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                    StartActivity.this.startActivity(intent);
                                    StartActivity.this.finish();
                                }
                            } else {
                                KLog.e("error", "进来这里5");
                                //上面已经写好的拨号方法
                                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                                StartActivity.this.startActivity(intent);
                                StartActivity.this.finish();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    };
}