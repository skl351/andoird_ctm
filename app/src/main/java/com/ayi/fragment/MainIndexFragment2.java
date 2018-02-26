package com.ayi.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.LoginActivity;
import com.ayi.activity.MainActivity;
import com.ayi.activity.RechargeActivity;
import com.ayi.adapter.position_tc__adapter;
import com.ayi.entity.Home_certem_entiy;
import com.ayi.entity.Place_item;
import com.ayi.entity.UserInfo;
import com.ayi.entity.item_news;
import com.ayi.entity.item_position_tc;
import com.ayi.home_page.Business_appointment;
import com.ayi.home_page.Combo;
import com.ayi.home_page.Home_baomu_ok;
import com.ayi.home_page.Home_daike_guodu;
import com.ayi.home_page.Home_yuesao_ok;
import com.ayi.home_page.Home_zdg_cq_ok;
import com.ayi.home_page.Home_zdg_ok;
import com.ayi.home_page.More_project;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Debug_net;
import com.ayi.utils.DensityUtil;
import com.ayi.utils.Show_toast;
import com.ayi.utils.Utils_loadimage;
import com.ayi.utils.Web_view;
import com.ayi.widget.MyNewyearDialog;
import com.ayi.zidingyi_view.MyLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.milk.base.BaseFragment;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
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
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;




/**
 * 1518624000-2018-2-15
 * 1518883200-2018-2-18
 * Created by oceanzhang on 16/3/24.
 */
public class MainIndexFragment2 extends BaseFragment {
    /**
     * 新年
     */


    public boolean alert_newyears() {

        long time = getTime();
        if (time < AyiApplication.time2 && time >= AyiApplication.time1) {
             final MyNewyearDialog dialog=new MyNewyearDialog(getActivity());
            dialog.viewold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            Window dialogWindow = dialog.getWindow();
            WindowManager m = getActivity().getWindowManager();
            DisplayMetrics dm = new DisplayMetrics();
            m.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            p.height = (int) (dm.heightPixels * 0.9); // 高度设置为屏幕的0.6，根据实际情况调整
            p.width = (int) (dm.widthPixels * 0.9); // 宽度设置为屏幕的0.65，根据实际情况调整
            dialogWindow.setAttributes(p);
            return true;
        }
        return false;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 101) {
                main_page_4.setVisibility(View.GONE);
                no_use_image.setVisibility(View.VISIBLE);
                main_text_zezao.setVisibility(View.VISIBLE);
                main_text_zezao.setText("抱歉，" + AyiApplication.place + "尚未开通三个阿姨O2O服务");
                alert_newyears();//新年弹窗
            }
            if (msg.arg1 == 102) {
                //这里在登录
                UserInfo userInfo = AyiApplication.getInstance().accountService().profile();
                //不登录
                if (userInfo == null || userInfo.getVerifyCode() == null || userInfo.getVerifyCode().equals("")) {
                    //正常
                    place_select.setText(AyiApplication.place);
                    loadData();
//                load_bottom_image();
                    init_get_center_icon();//1
                    init_get_positionrecommend();

                    progressBar1.setVisibility(View.GONE);
//                loadcenter_info();

                    mLocationClient.stop();
                    MainActivity m = (MainActivity) getActivity();
                    m.do_math(10);
                    alert_newyears();//新年弹窗
                } else {
                    //登录
                    get_5info();//bak
                }


            }


        }
    };
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
        app_version = getAppVersionName(getActivity());

//检查网络连接
        ConnectivityManager mConnectivity = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();

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
                    all_info = net_ip + "|" + phone_model + "|" + sdk_version + "|" + android_version + "|" + app_version + "|" + data_info + "|" + date1;
                    handler_5ingo.sendEmptyMessage(0);
                }
            }).start();

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

    Handler handler_5ingo = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            try {
                UserInfo userInfo = AyiApplication.getInstance().accountService().profile();
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                asyncHttpClient.setTimeout(20000);
                String url = RetrofitUtil.url_zidongdenglu;//测试数据--得到的数据
                RequestParams requestParams = new RequestParams();
                requestParams.put("username", userInfo.getMobile());
                requestParams.put("password", userInfo.getVerifyCode());
                requestParams.put("latitude", AyiApplication.lat);
                requestParams.put("longitude", AyiApplication.logna);
                requestParams.put("bak", all_info);
                KLog.e("-----+----------" + requestParams);
                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                        super.onSuccess(statusCode, headers, jsonObject);
                        try {
                            KLog.e("jsonObjec---t" + jsonObject);
                            AyiApplication.getInstance().setCanvalet(jsonObject.getJSONObject("data").getInt("canvalet"));
                            //登录成功
                            //正常
                            place_select.setText(AyiApplication.place);
                            loadData();
                            init_get_center_icon();//1
                            init_get_positionrecommend();
                            progressBar1.setVisibility(View.GONE);
                            mLocationClient.stop();
                            MainActivity m = (MainActivity) getActivity();
                            m.do_math(10);
                            alert_newyears();//新年弹窗

                        } catch (Exception e) {
                            //有错
                            e.printStackTrace();
                            //正常
                            place_select.setText(AyiApplication.place);
                            loadData();
                            init_get_center_icon();//1
                            init_get_positionrecommend();
                            progressBar1.setVisibility(View.GONE);
                            mLocationClient.stop();
                            MainActivity m = (MainActivity) getActivity();
                            m.do_math(10);
                            alert_newyears();//新年弹窗
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        super.onFailure(statusCode, headers, responseString, throwable);
                        //正常
                        place_select.setText(AyiApplication.place);
                        loadData();
//                load_bottom_image();
                        init_get_center_icon();//1
                        init_get_positionrecommend();

                        progressBar1.setVisibility(View.GONE);
//                loadcenter_info();

                        mLocationClient.stop();
                        MainActivity m = (MainActivity) getActivity();
                        m.do_math(10);
                        alert_newyears();//新年弹窗
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                //正常
                place_select.setText(AyiApplication.place);
                loadData();
//                load_bottom_image();
                init_get_center_icon();//1
                init_get_positionrecommend();

                progressBar1.setVisibility(View.GONE);
//                loadcenter_info();

                mLocationClient.stop();
                MainActivity m = (MainActivity) getActivity();
                m.do_math(10);
                alert_newyears();//新年弹窗
            }
        }
    };

    private void init_get_positionrecommend() {
        System.out.println("AyiApplication.area_id:" + AyiApplication.area_id);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_position_recomend;//测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("areaid", AyiApplication.area_id);
        requestParams.put("currentpage", 1);
        requestParams.put("pagesize", 10);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    KLog.e("jsonObject.toString()" + jsonObject.toString());
                    System.out.println("RetrofitUtil.url_position_recomend;" + jsonObject.toString());
                    JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("financialList");
                    if ("200".equals(jsonObject.getString("ret"))) {
                        List<item_position_tc> list = new ArrayList<item_position_tc>();

                        for (int i = 0; i < jsonArray.length(); i++) {
                            item_position_tc item = new item_position_tc();
                            item.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            item.setSimple_img(jsonArray.getJSONObject(i).getString("simple_img"));
                            item.setCornertitle(jsonArray.getJSONObject(i).getString("cornertitle"));
                            item.setAreaid(jsonArray.getJSONObject(i).getString("areaid"));
                            item.setCode(jsonArray.getJSONObject(i).getString("code"));
                            item.setPrice(jsonArray.getJSONObject(i).getString("price"));
//                            item.setType_id(tc_type_id);
                            item.setId(jsonArray.getJSONObject(i).getString("dyid"));
                            list.add(item);
                        }
                        position_tc__adapter adapter = new position_tc__adapter(getActivity(), list);
                        list_tc.setAdapter(adapter);
                        setListViewHeightBasedOnChildren(list_tc);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressBar1.setVisibility(View.GONE);
                Show_toast.showText(getActivity(), "网络繁忙，请重试.");

            }
        });
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        position_tc__adapter listAdapter = (position_tc__adapter) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            System.out.println("几个view:");
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        System.out.println("totalHeight:" + totalHeight);
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + 30;
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    public LocationClient mLocationClient = null;

    public BDLocationListener myListener = new MyLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            KLog.e("location.getLocType():error", location.getLocType());
            KLog.e("进来一次");
            if (location.getLocType() != 161 && location.getLocType() != 61) {
                AyiApplication.lat = 0;
                AyiApplication.logna = 0;
                Show_toast.showText(getActivity(), "请打开app定位功能");
                mLocationClient.stop();
            } else {
                AyiApplication.lat = location.getLatitude();
                AyiApplication.logna = location.getLongitude();
            }
            String city = location.getCity();
//            String city="慈溪";
            boolean flag = false;
            if (city == null) {
                KLog.d("location:", city);
                city = "长春市";
                AyiApplication.area_id = "2201";
                AyiApplication.place = "长春";
            }
            for (int i = 0; i < list.size(); i++) {
                KLog.e("suanll", city + "," + list.get(i).getArea_name());
                if (list.get(i).getArea_name().equals(city.split("市")[0])) {
                    AyiApplication.area_id = list.get(i).getArea_id();
                    AyiApplication.place = list.get(i).getArea_name();
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                //说明没有匹配到
                AyiApplication.area_id = "0";
                AyiApplication.place = "城市";
                Message message = new Message();
                message.arg1 = 101;
                handler.sendMessage(message);
                return;
            }
            Message message = new Message();
            message.arg1 = 102;
            handler.sendMessage(message);

        }
    };

    @Bind(R.id.fragment_statistics_webview)
    FrameLayout frameLayout;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getAction();
            if (str.equals(RetrofitUtil.APP_BORADCASTRECEIVER)) {
                initView(getView());
            } else if (str.equals(RetrofitUtil.APP_BORADCASTRECEIVER2)) {
//                initView(getView());
            }
        }

    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_index;
    }

    private View main_view;
    private TimerTask mTimerTask;//定时器
    private boolean mIsUserTouched = false;
    private int mBannerPosition = 0;//当前位置
    private final int FAKE_BANNER_SIZE = 100;
    private ViewPager mViewPager;
    private int DEFAULT_BANNER_SIZE;//可以设动态
    private Timer mTimer;
    //    private ImageView[] mIndicator;
    private BannerAdapter bannerAdapter;
    public double screenWidth;
    private TextView place_select;
    private View place_select_wai;
    private View no_use_image;
    private TextView main_text_zezao;
    private View main_page_4;
    private View progressBar1;
    private LinearLayout item_outer1;
    private LinearLayout item_outer2;

    private ListView list_tc;

    @Override
    public void initView(View view) {
        super.initView(view);
        //得到频宽；
        DisplayMetrics outMetrics = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        frameLayout.removeAllViews();
        main_view = LayoutInflater.from(getContext()).inflate(R.layout.one_main_page, null);
        initView();
//        init_center_project();
        init_select_place();
        initEvent();
        System.out.println("每次出现");
        if (AyiApplication.getInstance().isFlag_first() && isNetworkConnected(getActivity())) {
            System.out.println("出现一次的");
            AyiApplication.getInstance().setFlag_first(false);
            init_net_place();
        } else {
            if (!isNetworkConnected(getActivity())) {
                progressBar1.setVisibility(View.VISIBLE);
            } else {
                boolean flag = false;
                try {
                    if (list != null && (!list.isEmpty())) {
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).getArea_name().equals(AyiApplication.place)) {
                                AyiApplication.area_id = list.get(i).getArea_id();
                                flag = true;
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (!flag) {
                        KLog.e(AyiApplication.area_id + "," + AyiApplication.place);
                        //说明没有匹配到
                        main_page_4.setVisibility(View.GONE);
                        no_use_image.setVisibility(View.VISIBLE);
                        main_text_zezao.setVisibility(View.VISIBLE);
                        main_text_zezao.setText("抱歉，" + AyiApplication.place + "尚未开通三个阿姨O2O服务");
                    } else {
                        place_select.setText(AyiApplication.place);
                        main_page_4.setVisibility(View.VISIBLE);
                        no_use_image.setVisibility(View.GONE);
                        main_text_zezao.setVisibility(View.GONE);
                    }
                    place_select.setText(AyiApplication.place);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                loadData();
//                load_bottom_image();
                init_get_center_icon();//2
                init_get_positionrecommend();
                MainActivity m = (MainActivity) getActivity();
                m.do_math(10);
            }


        }

        frameLayout.addView(main_view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);

    }


    public boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


    private List<Place_item> list;

    private void init_net_place() {
        progressBar1.setVisibility(View.VISIBLE);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_place_list;
        asyncHttpClient.post(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    System.out.println("jsonObject:" + jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    list = new ArrayList<Place_item>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        Place_item item = new Place_item();
                        item.setArea_id(jsonArray.getJSONObject(i).getString("area_id"));
                        item.setArea_name(jsonArray.getJSONObject(i).getString("area_name"));
                        list.add(item);
                    }

                    mLocationClient = new LocationClient(getActivity());     //声明LocationClient类
                    initLocation();
                    mLocationClient.registerLocationListener(myListener);    //注册监听函数
                    mLocationClient.start();


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

    List<View> list_view_place;
    AlertDialog alert;
    AlertDialog alert_wait;

    private void init_select_place() {
        list_view_place = new ArrayList<>();
        place_select_wai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_view_place.clear();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                alert = builder.create();
                View view = LayoutInflater.from(getContext()).inflate(R.layout.place_view, null);
                LinearLayout linearLayout1 = (LinearLayout) view.findViewById(R.id.place_line1);
                LinearLayout linearLayout2 = (LinearLayout) view.findViewById(R.id.place_line2);
                LinearLayout linearLayout3 = (LinearLayout) view.findViewById(R.id.place_line3);
                LinearLayout linearLayout4 = (LinearLayout) view.findViewById(R.id.place_line4);
                LinearLayout linearLayout5 = (LinearLayout) view.findViewById(R.id.place_line5);
                for (int i = 0; i < list.size(); i++) {
                    if (i < 5) {
                        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.place_item, null);
                        TextView text = (TextView) view1.findViewById(R.id.text);
                        text.setText(list.get(i).getArea_name());
                        linearLayout1.addView(view1);
                        list_view_place.add(view1);
                        continue;
                    }
                    if (i < 10) {
                        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.place_item, null);
                        TextView text = (TextView) view1.findViewById(R.id.text);
                        text.setText(list.get(i).getArea_name());
                        linearLayout2.addView(view1);
                        list_view_place.add(view1);
                        continue;
                    }
                    if (i < 15) {
                        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.place_item, null);
                        TextView text = (TextView) view1.findViewById(R.id.text);
                        text.setText(list.get(i).getArea_name());
                        linearLayout3.addView(view1);
                        list_view_place.add(view1);
                        continue;
                    }
                    if (i < 20) {
                        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.place_item, null);
                        TextView text = (TextView) view1.findViewById(R.id.text);
                        text.setText(list.get(i).getArea_name());
                        linearLayout4.addView(view1);
                        list_view_place.add(view1);
                        continue;
                    }
                    if (i < 25) {
                        View view1 = LayoutInflater.from(getContext()).inflate(R.layout.place_item, null);
                        TextView text = (TextView) view1.findViewById(R.id.text);
                        text.setText(list.get(i).getArea_name());
                        linearLayout5.addView(view1);
                        list_view_place.add(view1);
                        continue;
                    }

                }
                for (int i = 0; i < list_view_place.size(); i++) {
                    final int a = i;
                    list_view_place.get(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String city = "";
                            city = list.get(a).getArea_name();
                            main_page_4.setVisibility(View.VISIBLE);
                            no_use_image.setVisibility(View.GONE);

                            main_text_zezao.setVisibility(View.GONE);
                            AyiApplication.place = city;
                            AyiApplication.area_id = list.get(a).getArea_id();
                            System.out.println("改变了：" + AyiApplication.place + "," + AyiApplication.area_id);
                            place_select.setText(city);
                            alert.dismiss();
                            loadData();
//                            load_bottom_image();
                            init_get_center_icon();//选取地址//3
                            init_get_positionrecommend();

                        }
                    });
                }
                alert.setView(view);
                alert.show();

            }
        });
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    private int ScreenWidth;
    private int whith_rct;//一个按钮的宽
    //    private ImageView bottom_image;
    private ImageView bottom_image1;
    private ImageView bottom_image2;
    List<Home_certem_entiy> list_certen_entiy;//这个是预计的数据
    List<View> list_center_view = new ArrayList<>();

    private void initView() {
        list_tc = (ListView) main_view.findViewById(R.id.list_tc);
        //得到频宽；
        DisplayMetrics outMetrics = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        ScreenWidth = outMetrics.widthPixels;
        whith_rct = ScreenWidth / 5;
        list_certen_entiy = new ArrayList<>();
        progressBar1 = main_view.findViewById(R.id.progressBar1);
        no_use_image = main_view.findViewById(R.id.no_use_image);
        main_text_zezao = (TextView) main_view.findViewById(R.id.main_text_zezao);
        main_page_4 = main_view.findViewById(R.id.main_page_4);
//        bottom_image = (ImageView) main_view.findViewById(R.id.bottom_image);
        bottom_image1 = (ImageView) main_view.findViewById(R.id.bottom_image1);
        bottom_image1.getLayoutParams().width = (ScreenWidth - DensityUtil.dip2px(getActivity(), 15)) / 2;
        bottom_image1.getLayoutParams().height = (ScreenWidth - DensityUtil.dip2px(getActivity(), 15)) / 2 * 3 / 7;
        bottom_image2 = (ImageView) main_view.findViewById(R.id.bottom_image2);
        bottom_image2.getLayoutParams().width = (ScreenWidth - DensityUtil.dip2px(getActivity(), 15)) / 2;
        bottom_image2.getLayoutParams().height = (ScreenWidth - DensityUtil.dip2px(getActivity(), 15)) / 2 * 3 / 7;
        place_select = (TextView) main_view.findViewById(R.id.place_select);
        place_select_wai = main_view.findViewById(R.id.place_select_wai);

        item_outer1 = (LinearLayout) main_view.findViewById(R.id.item_outer1);//一个长条
        item_outer2 = (LinearLayout) main_view.findViewById(R.id.item_outer2);//二个长条

//        init_get_center_icon();
        init_bootom_click();


    }

    private void init_bootom_click() {
        bottom_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新年
                if (alert_newyears()) {
                    return;
                }
                if (AyiApplication.getInstance().accountService().id().isEmpty() && AyiApplication.getInstance().accountService().token().isEmpty()) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    return;
                } else {
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    String url = RetrofitUtil.url_get_info;
                    asyncHttpClient.setTimeout(20000);
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                    requestParams.put("token", AyiApplication.getInstance().accountService().token());
                    asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                KLog.e("jsonObjec---t" + response);
                                String ret = response.getString("ret");
                                if (!ret.equals("200")) {
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent;
                                    intent = new Intent(getActivity(), Combo.class);
                                    intent.putExtra("title", "套餐");
                                    startActivity(intent);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                }

            }
        });
        bottom_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (AyiApplication.getInstance().accountService().id().isEmpty() && AyiApplication.getInstance().accountService().token().isEmpty()) {
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                    return;
                } else {
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    String url = RetrofitUtil.url_get_info;
                    asyncHttpClient.setTimeout(20000);
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                    requestParams.put("token", AyiApplication.getInstance().accountService().token());
                    asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                String ret = response.getString("ret");
                                if (!ret.equals("200")) {
                                    Intent intent = new Intent(getContext(), LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(getActivity(), RechargeActivity.class);
                                    startActivity(intent);
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Intent intent = new Intent(getContext(), LoginActivity.class);
                            startActivity(intent);
                        }
                    });

                }


//                Intent intent = new Intent(getActivity(), Web_view.class);
//                intent.putExtra("title", "惠民商城");
//                intent.putExtra("web_url", "http://12435shop.web08.com.cn/");
//                startActivity(intent);
            }
        });
    }

    private void init_get_center_icon() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_center_icon;//测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("areaid", AyiApplication.area_id);
        requestParams.put("isshow", 1);
        KLog.e("areaid", AyiApplication.area_id);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    System.out.println("RetrofitUtil.url_center_icon;" + jsonObject.toString());
                    if ("200".equals(jsonObject.getString("ret"))) {
                        list_certen_entiy.clear();
                        JSONArray js = jsonObject.getJSONArray("data");
                        for (int i = 0; i < js.length(); i++) {
                            if (js.getJSONObject(i).getString("isshow").equals("1")) {
                                Home_certem_entiy home_certem_entiy = new Home_certem_entiy();
                                home_certem_entiy.setImg_url(js.getJSONObject(i).getString("type_icon"));
                                home_certem_entiy.setType_id(js.getJSONObject(i).getString("service_type_id"));
                                home_certem_entiy.setTitle(js.getJSONObject(i).getString("title"));
                                home_certem_entiy.setSortby(js.getJSONObject(i).getString("sortby"));
                                home_certem_entiy.setType_code(js.getJSONObject(i).getString("type_code"));
                                home_certem_entiy.setIndex_icon(js.getJSONObject(i).getString("index_icon"));
                                list_certen_entiy.add(home_certem_entiy);
                            }
                        }
                        if (list_certen_entiy.size() < 6) {
                            item_outer2.setVisibility(View.GONE);
                        } else {
                            item_outer2.setVisibility(View.VISIBLE);
                        }
                        //在查阅一些只知识后，发现android没有提供软键盘监听，于是我找了一个KeyboardChangeListener
                        init_center_item();
                        init_center_onclick();
                    }
                } catch (Exception e) {
                    Debug_net.debug_tiaoshi("mainindex101", e.getMessage(), e.toString(), "url_center_icon");
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressBar1.setVisibility(View.GONE);
                Show_toast.showText(getActivity(), "网络繁忙，请重试.");

            }
        });
    }

    //    String tc_type_id="";
    private void init_center_onclick() {

        for (int i = 0; i < list_center_view.size(); i++) {
            final int a = i;
            list_center_view.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //新年
                    if (alert_newyears()) {
                        return;
                    }
                    if (AyiApplication.getInstance().accountService().id().isEmpty() && AyiApplication.getInstance().accountService().token().isEmpty()) {
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);
                        return;
                    } else {
                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                        String url = RetrofitUtil.url_get_info;
                        asyncHttpClient.setTimeout(20000);
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                        requestParams.put("token", AyiApplication.getInstance().accountService().token());
                        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    KLog.e("jsonObjec---t" + response);
                                    String ret = response.getString("ret");
                                    if (!ret.equals("200")) {
                                        Intent intent = new Intent(getContext(), LoginActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Intent intent;
                                        if (list_certen_entiy.get(a).getType_code().equals("dg")) {
//                            对公业务

                                            intent = new Intent(getActivity(), Business_appointment.class);
                                            startActivity(intent);
                                        } else if (list_certen_entiy.get(a).getType_code().equals("tc")) {
                                            intent = new Intent(getActivity(), Combo.class);
                                            intent.putExtra("title", list_certen_entiy.get(a).getTitle());
//                                            intent.putExtra("type_id", list_certen_entiy.get(a).getType_id());
//                                            System.out.println("1type_id" + list_certen_entiy.get(a).getType_id());
                                            startActivity(intent);
                                        }
                                        switch (list_certen_entiy.get(a).getType_id()) {

                                            case "5":
                                            case "6":
                                            case "7":
                                            case "8":
                                                if (AyiApplication.getInstance().getCanvalet() == 1) {
                                                    intent = new Intent(getActivity(), Home_daike_guodu.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                } else {
                                                    intent = new Intent(getActivity(), Home_zdg_ok.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                }
                                                break;
                                            case "9":
                                            case "10":
                                            case "11":
                                                if (AyiApplication.getInstance().getCanvalet() == 1) {
                                                    intent = new Intent(getActivity(), Home_daike_guodu.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                } else {
                                                    intent = new Intent(getActivity(), Home_zdg_cq_ok.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                }

                                                break;
                                            case "12":
                                            case "13":
                                                if (AyiApplication.getInstance().getCanvalet() == 1) {
                                                    intent = new Intent(getActivity(), Home_daike_guodu.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                } else {
                                                    intent = new Intent(getActivity(), Home_baomu_ok.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                }

                                                break;
                                            case "14":
                                            case "15":
                                                if (AyiApplication.getInstance().getCanvalet() == 1) {
                                                    intent = new Intent(getActivity(), Home_daike_guodu.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                } else {
                                                    intent = new Intent(getActivity(), Home_yuesao_ok.class);
                                                    intent.putExtra("service_id", list_certen_entiy.get(a).getType_id());
                                                    intent.putExtra("title", list_certen_entiy.get(a).getTitle());
                                                    startActivity(intent);
                                                }

                                                break;
//                            case "dg":
//                                //对公业务
//                                intent = new Intent(getActivity(), Business_appointment.class);
//                                startActivity(intent);
//                                break;
//                            case "tc":
//                                //对公业务
//                                intent = new Intent(getActivity(), Combo.class);
//                                startActivity(intent);
//                                break;
//                                //预留之后的
//                                default:
//                                    //对公业务
//                                    intent = new Intent(getActivity(), Combo.class);
//                                    startActivity(intent);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                super.onFailure(statusCode, headers, throwable, errorResponse);
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                }
            });
        }
    }

    public void setimage_icon(String id, ImageView img, String type) {
        switch (id) {
            case "5":
                img.setBackgroundResource(R.mipmap.center_rcbj);
                break;
            case "6":
                img.setBackgroundResource(R.mipmap.center_qxyyj);
                break;
            case "7":
                img.setBackgroundResource(R.mipmap.center_cbl);
                break;
            case "8":
                img.setBackgroundResource(R.mipmap.center_kh);
                break;
            case "9":
                img.setBackgroundResource(R.mipmap.center_cpzdg);
                break;
            case "10":
                img.setBackgroundResource(R.mipmap.center_zf);
                break;
            case "11":
                img.setBackgroundResource(R.mipmap.center_bjjzf);
                break;
            case "12":
                img.setBackgroundResource(R.mipmap.center_shqj);
                break;
            case "13":
                img.setBackgroundResource(R.mipmap.center_zglr);
                break;
            case "14":
                img.setBackgroundResource(R.mipmap.center_ys);
                break;
            case "15":
                img.setBackgroundResource(R.mipmap.center_yes);
                break;
            default:
                if (type.equals("dg")) {
                    img.setBackgroundResource(R.mipmap.center_dg);
                } else if (type.equals("tc")) {
                    img.setBackgroundResource(R.mipmap.center_tc);
                }
                break;

        }
    }

    //轮询得到数据view
    private void init_center_item() {
        System.out.println("修改了4--------");
        list_center_view.clear();
        item_outer1.removeAllViews();
        item_outer2.removeAllViews();
        for (int i = 0; i < list_certen_entiy.size(); i++) {
            if (i < 5) {
                View item_view = LayoutInflater.from(getContext()).inflate(R.layout.center_view, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(whith_rct, LinearLayout.LayoutParams.WRAP_CONTENT);
                item_view.setLayoutParams(lp);
                TextView title = (TextView) item_view.findViewById(R.id.titlecenter);
                ImageView img = (ImageView) item_view.findViewById(R.id.imgcenter);
                ImageView icon_small = (ImageView) item_view.findViewById(R.id.icon_small);
                title.setText(list_certen_entiy.get(i).getTitle());
                //图片为空
                if (!list_certen_entiy.get(i).getIndex_icon().equals("")) {
                    setimage_icon_small(list_certen_entiy.get(i).getIndex_icon(), icon_small);
                }
                //图片为空
                if (list_certen_entiy.get(i).getImg_url().equals("")) {
                    setimage_icon(list_certen_entiy.get(i).getType_id(), img, list_certen_entiy.get(i).getType_code());
                } else {
                    ImageLoader.getInstance().displayImage(list_certen_entiy.get(i).getImg_url(), img);
                }
                item_outer1.addView(item_view);
                list_center_view.add(item_view);

                continue;
            }
            if (i < 9) {
                View item_view = LayoutInflater.from(getContext()).inflate(R.layout.center_view, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(whith_rct, LinearLayout.LayoutParams.WRAP_CONTENT);
                item_view.setLayoutParams(lp);
                TextView title = (TextView) item_view.findViewById(R.id.titlecenter);
                ImageView img = (ImageView) item_view.findViewById(R.id.imgcenter);
                ImageView icon_small = (ImageView) item_view.findViewById(R.id.icon_small);
                //图片为空
                if (!list_certen_entiy.get(i).getIndex_icon().equals("")) {
                    setimage_icon_small(list_certen_entiy.get(i).getIndex_icon(), icon_small);
                }
                title.setText(list_certen_entiy.get(i).getTitle());
                if (list_certen_entiy.get(i).getImg_url().equals("")) {
                    setimage_icon(list_certen_entiy.get(i).getType_id(), img, list_certen_entiy.get(i).getType_code());
                } else {
                    ImageLoader.getInstance().displayImage(list_certen_entiy.get(i).getImg_url(), img);
                }
                item_outer2.addView(item_view);
                list_center_view.add(item_view);
                continue;
            }
//            if (i==list_certen_entiy.size()-1){
//
//                break;
//            }

        }
        if (list_certen_entiy.size() > 9) {
            View item_view = LayoutInflater.from(getContext()).inflate(R.layout.center_view, null);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(whith_rct, LinearLayout.LayoutParams.WRAP_CONTENT);
            item_view.setLayoutParams(lp);
            TextView title = (TextView) item_view.findViewById(R.id.titlecenter);
            ImageView img = (ImageView) item_view.findViewById(R.id.imgcenter);
            title.setText("更多");
            img.setBackgroundResource(R.drawable.more_center);
            item_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //新年
                    if (alert_newyears()) {
                        return;
                    }
                    Intent intent = new Intent(getActivity(), More_project.class);
                    intent.putExtra("whith_rct", whith_rct);
                    startActivity(intent);
                }
            });
            item_outer2.addView(item_view);
        }


    }

//    private void load_bottom_image() {
//        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//        asyncHttpClient.setTimeout(20000);
//        String url = RetrofitUtil.url_banan;//测试数据--得到的数据
//        RequestParams requestParams = new RequestParams();
////        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
////        requestParams.put("token",AyiApplication.getInstance().accountService().token());
//        requestParams.put("areaid", AyiApplication.area_id);
//        requestParams.put("type", "2");
//        KLog.e("areaid", AyiApplication.area_id);
//        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
//                super.onSuccess(statusCode, headers, jsonObject);
//                try {
//                    System.out.println(jsonObject.toString() + "banner图2片");
//                    if ("200".equals(jsonObject.getString("ret"))) {
//
//                        JSONArray js = jsonObject.getJSONArray("data");
//                        ImageLoader.getInstance().displayImage(js.getJSONObject(0).getString("url"), bottom_image);
//                        bottom_image.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Intent intent=new Intent(getActivity(),Web_view.class);
//                                intent.putExtra("web_url","http://12435shop.web08.com.cn/");
//                                intent.putExtra("title","测试用的");
//                                getActivity().startActivity(intent);
//                            }
//                        });
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//            }
//        });
//    }

    List<item_news> list_new = new ArrayList<item_news>();
    ;

    private void loadData() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_banan;//测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
//        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
//        requestParams.put("token",AyiApplication.getInstance().accountService().token());
        requestParams.put("areaid", AyiApplication.area_id);
        requestParams.put("type", "1");
        KLog.e("areaid", AyiApplication.area_id);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    list_new.clear();
                    System.out.println(jsonObject.toString() + "banner图片");
                    if ("200".equals(jsonObject.getString("ret"))) {

                        JSONArray js = jsonObject.getJSONArray("data");
                        for (int i = 0; i < js.length(); i++) {
                            item_news it1 = new item_news();
                            it1.setLink(js.getJSONObject(i).getString("link"));
                            it1.setTpdz(js.getJSONObject(i).getString("url"));
                            it1.setTitle(js.getJSONObject(i).getString("title"));
                            list_new.add(it1);
                        }
                        DEFAULT_BANNER_SIZE = list_new.size();//设置一个网络得到的尺寸，用来
                        bannerAdapter = new BannerAdapter(getActivity(), list_new);
                        mViewPager.setAdapter(bannerAdapter);
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
    }


    private class BannerAdapter extends PagerAdapter {

        private Context context;
        private List<item_news> newsList;

        public BannerAdapter(Context context, List<item_news> list) {
            this.context = context;
            this.newsList = list;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            position %= DEFAULT_BANNER_SIZE;
            View view = LayoutInflater.from(context).inflate(R.layout.item_image, container, false);
            ImageView image = (ImageView) view.findViewById(R.id.image);
//            image.setBackgroundResource(R.mipmap.ic_launcher);
            Utils_loadimage.loadImage(newsList.get(position).getTpdz(), image);
            final String link = newsList.get(position).getLink();
            final String title = newsList.get(position).getTitle();
            System.out.println("link" + link);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!link.equals("")) {
                        Intent intent = new Intent(getActivity(), Web_view.class);
                        intent.putExtra("web_url", link);
                        intent.putExtra("title", title);
                        startActivity(intent);
                    }

//                    Toast.makeText(context,"-->可以点击"+pos,Toast.LENGTH_SHORT).show();//点击跳转
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return FAKE_BANNER_SIZE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void finishUpdate(ViewGroup container) {

            //这个有点懵逼..
            int position = mViewPager.getCurrentItem();
            if (position == 0) {
                position = DEFAULT_BANNER_SIZE;
                mViewPager.setCurrentItem(position, false);
            } else if (position == FAKE_BANNER_SIZE - 1) {
                position = DEFAULT_BANNER_SIZE - 1;
                mViewPager.setCurrentItem(position, false);
            }
        }
    }

    private void initEvent() {
        mViewPager = (ViewPager) main_view.findViewById(R.id.view_pager);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                    mIsUserTouched = true;
                } else if (action == MotionEvent.ACTION_UP) {
                    mIsUserTouched = false;
                }
                return false;
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mBannerPosition = position;
//                setIndicator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResources().getString(R.string.client_home));
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (!mIsUserTouched) {
                    mBannerPosition = (mBannerPosition + 1) % FAKE_BANNER_SIZE;
                    /**
                     * Android在子线程更新UI的几种方法
                     * Handler，AsyncTask,view.post,runOnUiThread
                     */
                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mBannerPosition == FAKE_BANNER_SIZE - 1) {
                                    mViewPager.setCurrentItem(DEFAULT_BANNER_SIZE - 1, false);
                                } else {
                                    mViewPager.setCurrentItem(mBannerPosition);
                                }
                            }
                        });
                    }

                }
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 5000, 5000);
    }

    public void onStart() {
        super.onStart();
        registerBoradcastReceiver();
    }

    public void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RetrofitUtil.APP_BORADCASTRECEIVER);
        intentFilter.addAction(RetrofitUtil.APP_BORADCASTRECEIVER2);
        getActivity().registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResources().getString(R.string.client_home));
        System.out.println("暂停");
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(this.mBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setimage_icon_small(String id, ImageView img) {
        switch (id) {
            case "1":
                img.setBackgroundResource(R.mipmap.a_01);
                break;
            case "2":
                img.setBackgroundResource(R.mipmap.a_02);
                break;
            case "3":
                img.setBackgroundResource(R.mipmap.a_03);
                break;
            case "4":
                img.setBackgroundResource(R.mipmap.a_04);
                break;
            case "5":
                img.setBackgroundResource(R.mipmap.a_05);
                break;
            case "6":
                img.setBackgroundResource(R.mipmap.a_06);
                break;
            case "7":
                img.setBackgroundResource(R.mipmap.a_07);
                break;
            case "8":
                img.setBackgroundResource(R.mipmap.a_08);
                break;
            case "9":
                img.setBackgroundResource(R.mipmap.b_01);
                break;
            case "10":
                img.setBackgroundResource(R.mipmap.b_02);
                break;
            case "11":
                img.setBackgroundResource(R.mipmap.b_03);
                break;
            case "12":
                img.setBackgroundResource(R.mipmap.b_04);
                break;
            case "13":
                img.setBackgroundResource(R.mipmap.b_05);
                break;
            case "14":
                img.setBackgroundResource(R.mipmap.b_06);
                break;
            case "15":
                img.setBackgroundResource(R.mipmap.b_07);
                break;
            case "16":
                img.setBackgroundResource(R.mipmap.b_08);
                break;

        }
    }

    public long getTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        return time;
    }
}
