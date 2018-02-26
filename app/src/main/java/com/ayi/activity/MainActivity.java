package com.ayi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.datadao.ContactInfoDao;
import com.ayi.interf.OnTabReselectListener;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.widget.MainTab;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;


/**
 * 首页  ayi://index
 */
public class MainActivity extends TempletActivity implements TabHost.OnTabChangeListener, View.OnTouchListener {

    @Bind(android.R.id.tabhost)
    public FragmentTabHost mTabHost;
    public AyiApplication ayiApp;

    private View xiajiemiangao;
    public double screenWidth;

    @Override
    public void initView() {
        super.initView();
        System.out.println("main_init");
        setTitle("首页");
        setView(R.layout.activity_main);
        xiajiemiangao = findViewById(R.id.xiajiemiangao);
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        xiajiemiangao.measure(w, h);
//        System.out.println("多高"+xiajiemiangao.getMeasuredHeight());
        ayiApp = AyiApplication.getInstance();
        int status = getIntent().getIntExtra("status", 0);
        //从splashactivity中跳转过来的
//        System.out.println("按断更新"+getIntent().getIntExtra("status", 0)+getIntent().getClass());
        if (status == 1) {
            Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
            startActivity(intent);
        }
        int index = getIntent().getIntExtra("index", 0);//设置首页--可以修改设置页面
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        if (android.os.Build.VERSION.SDK_INT > 10) {
            mTabHost.getTabWidget().setShowDividers(0);
        }

        initTabs();
        hideTitleBar();
        mTabHost.setCurrentTab(index);
        mTabHost.setOnTabChangedListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.client_entry));
        MobclickAgent.onResume(MainActivity.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //作为
        MobclickAgent.setDebugMode(true);
        MobclickAgent.openActivityDurationTrack(false);
        MobclickAgent.setScenarioType(MainActivity.this, MobclickAgent.EScenarioType.E_UM_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.client_entry));
        MobclickAgent.onPause(MainActivity.this);
    }

    @Override
    protected boolean hasBackBtn() {
        return false;
    }


    public void do_math(int a) {
        System.out.println("调用了这个方法");
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_return_list;//测试数据--得到的数据
        System.out.println("skl_url" + url);
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("areaid", AyiApplication.area_id);
        requestParams.put("group_id", "3");
        requestParams.put("currentpage", 1);
        requestParams.put("pagesize", 1000);
        requestParams.put("getid", "id");

        System.out.println("AyiApplication.area_id" + AyiApplication.area_id);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    System.out.println("url_return_list" + jsonObject.toString());
                    if ("200".equals(jsonObject.getString("ret"))) {
                        int flag = 0;
                        ContactInfoDao dao = new ContactInfoDao(MainActivity.this);
                        int index = jsonObject.getJSONObject("data").getJSONObject("financialList").getInt("count");
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONObject("financialList")
                                .getJSONArray("financialList");
                        try {
                            int size_data = dao.getPhoneNumbersize(AyiApplication.getInstance().accountService().id());
                            List<String> list_str = dao.getPhoneNumberby_userid(AyiApplication.getInstance().accountService().id());
                            KLog.e("测试瞩目：" + size_data + "," + index);
                            for (int i = 0; i < size_data; i++) {
                                boolean flag_state = false;
                                String chuanruid = "";
                                chuanruid = list_str.get(i);
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    if (chuanruid.equals(jsonArray.getJSONObject(j).getString("id"))) {
                                        //找到了
                                        flag_state = true;
                                        break;
                                    }
                                }
                                if (!flag_state) {
                                    dao.delete(chuanruid);
                                }
                            }

                            flag = index - dao.getPhoneNumbersize(AyiApplication.getInstance().accountService().id());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        View bootom_red_view = list.get(0).findViewById(R.id.bootom_red_view);
                        if (flag != 0) {
                            if (flag <= 99) {
                                TextView bootom_red_view_text = (TextView) bootom_red_view.findViewById(R.id.bootom_red_view_text);
                                bootom_red_view_text.setText("" + flag);
                                bootom_red_view.setVisibility(View.VISIBLE);
                            } else {
                                TextView bootom_red_view_text = (TextView) bootom_red_view.findViewById(R.id.bootom_red_view_text);
                                bootom_red_view_text.setText("99+");
                                bootom_red_view.setVisibility(View.VISIBLE);
                            }
                        } else {
                            bootom_red_view.setVisibility(View.GONE);
                        }

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

    List<View> list = new ArrayList<>();

    //初始化每张页面下面的4个tab
    public void initTabs() {
        MainTab[] tabs = MainTab.values();
        final int size = tabs.length;//tab的size
        for (int i = 0; i < size; i++) {
            MainTab mainTab = tabs[i];
            TabHost.TabSpec tab = mTabHost.newTabSpec(getString(mainTab.getResName()));
            View indicator = View.inflate(this, R.layout.tab_indicator, null);
            if (i == 2) {
                list.clear();
                list.add(indicator);
//                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//                asyncHttpClient.setTimeout(20000);
//                String url = RetrofitUtil.url_return_list;//测试数据--得到的数据
//                RequestParams requestParams = new RequestParams();
//                requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
//                requestParams.put("token",AyiApplication.getInstance().accountService().token());
//                requestParams.put("areaid", AyiApplication.area_id);
//                requestParams.put("group_id", "3");
//                requestParams.put("currentpage", 1);
//                requestParams.put("pagesize", 10);
//                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
//                        super.onSuccess(statusCode, headers, jsonObject);
//                        try {
//                            System.out.println(jsonObject.toString() + "banner图2片");
//                            if ("200".equals(jsonObject.getString("ret"))) {
//
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                        super.onFailure(statusCode, headers, responseString, throwable);
//                    }
//                });

            }
            TextView title = (TextView) indicator.findViewById(R.id.tab_title);
            ImageView iv = (ImageView) indicator.findViewById(R.id.tab_img);
            iv.setBackgroundResource(mainTab.getResIcon());
            title.setText(getString(mainTab.getResName()));
            tab.setIndicator(indicator);
            tab.setContent(new TabHost.TabContentFactory() {

                @Override
                public View createTabContent(String tag) {
                    return new View(MainActivity.this);
                }
            });
            mTabHost.addTab(tab, mainTab.getClz(), null);
            mTabHost.getTabWidget().getChildAt(i).setOnTouchListener(this);
        }
    }

    //4个按钮按下的改变
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouchEvent(event);
        boolean consumed = false;
        // use getTabHost().getCurrentTabView to decide if the current tab is
        // touched again
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && v.equals(mTabHost.getCurrentTabView())) {
            // use getTabHost().getCurrentView() to get a handle to the view
            // which is displayed in the tab - and to get this views context
            Fragment currentFragment = getCurrentFragment();
            if (currentFragment != null
                    && currentFragment instanceof OnTabReselectListener) {
                OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
                listener.onTabReselect();
                consumed = true;
            }
        }
        return consumed;
    }

    //得到对于的tab数字的对应页面
    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentByTag(
                mTabHost.getCurrentTabTag());
    }

    //在tab改变的时候
    @Override
    public void onTabChanged(String tabId) {
        if (tabId.equals(getString(R.string.main_tab_title_index))) {
            AyiApplication.flag_tc_dg = "1";
            setTitle("首页");
            hideTitleBar();
        } else if (tabId.equals(getString(R.string.main_tab_title_order))) {
            setTitle("订单");
            setRightBtnTxt("");
            setLeftBtnTxt("");
            hideTitleBar();
        } else if (tabId.equals(getString(R.string.main_tab_title_info))) {
            AyiApplication.flag_tc_dg = "1";
            setTitle("信息");
            setRightBtnTxt("");
            setLeftBtnTxt("");
            hideTitleBar();
        } else if (tabId.equals(getString(R.string.main_tab_title_kefu))) {
            AyiApplication.flag_tc_dg = "1";
            setTitle("客服");
            setRightBtnTxt("");
            setLeftBtnTxt("");
            showTitleBar();
        } else if (tabId.equals(getString(R.string.main_tab_title__my))) {
            AyiApplication.flag_tc_dg = "1";
            setTitle("我的");
            setLeftBtnTxt("");
            showTitleBar();
        }
    }


    public static String for_refesh = "0";

    //用来跳转到指定页面的方法。
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.hasExtra("for_refesh")) {
            for_refesh = intent.getStringExtra("for_refesh");
        }
        int tab = 0;
        try {
            if (intent.getStringExtra("tab") != null) {
                tab = Integer.parseInt(intent.getStringExtra("tab"));
            } else {
                tab = 0;
            }

        } catch (Exception e) {

        }

        mTabHost.setCurrentTab(tab);//修改页码

    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MobclickAgent.onKillProcess(MainActivity.this);
                finish();
                System.exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
