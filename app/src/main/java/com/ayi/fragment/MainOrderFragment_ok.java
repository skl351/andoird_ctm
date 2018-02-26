package com.ayi.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.LoginActivity;
import com.ayi.order_four.Order_four;
import com.ayi.order_four.Order_one;
import com.ayi.order_four.Order_two;
import com.ayi.retrofit.RetrofitUtil;
import com.example.zhouwei.library.CustomPopWindow;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * 一个好的方法解决了在fragement中用了v4 fragement的切换，导致被暂停后看不到第一张界面，需要循环点击才可以看到
 * Created by oceanzhang on 16/3/24.
 */
public class MainOrderFragment_ok extends BaseViewPagerFragment2_now {


    private Fragment fragment = null;
    private Fragment fragment1 = null;
    private Fragment fragment3 = null;

    /**
     * 处理弹出显示内容、点击事件等逻辑
     *
     * @param contentView
     */
    private void handleLogic(View contentView) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCustomPopWindow != null) {
                    mCustomPopWindow.dissmiss();
                }
                switch (v.getId()) {
                    case R.id.cgdd:
                        kongbai.setVisibility(View.GONE);
                        title.setText("普通订单");
                        flag_tc_dg = "1";
                        pager.setVisibility(View.VISIBLE);
                        main_tabs.setVisibility(View.VISIBLE);
                        adapter = new ViewPagerAdapter(getChildFragmentManager());
                        setupViewPager(adapter);
                        viewPager.setAdapter(adapter);
                        tabLayout.setupWithViewPager(viewPager);
                        demo_swiperefreshlayout.setVisibility(View.GONE);
                        demo_recycler.setAdapter(null);
                        break;
                    case R.id.tcdd:
                        kongbai.setVisibility(View.GONE);
                        title.setText("套餐订单");
                        flag_tc_dg = "2";
                        pager.setVisibility(View.GONE);
                        main_tabs.setVisibility(View.GONE);
                        viewPager.setAdapter(null);
                        demo_swiperefreshlayout.setVisibility(View.VISIBLE);
                        Do_network_tc(1, 10, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), AyiApplication.area_id, flag_tc_dg);

                        break;
                    case R.id.dgdd:
                        kongbai.setVisibility(View.GONE);
                        title.setText("企业清洁");
                        flag_tc_dg = "3";
                        pager.setVisibility(View.GONE);
                        main_tabs.setVisibility(View.GONE);
                        viewPager.setAdapter(null);
                        demo_swiperefreshlayout.setVisibility(View.VISIBLE);
                        Do_network(1, 10, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), AyiApplication.area_id, flag_tc_dg);

                        break;
                }
            }
        };
        contentView.findViewById(R.id.cgdd).setOnClickListener(listener);
        contentView.findViewById(R.id.tcdd).setOnClickListener(listener);
        contentView.findViewById(R.id.dgdd).setOnClickListener(listener);
    }

    @Override
    protected void seleclt_fun() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.pop_order, null);
        handleLogic(contentView);
        mCustomPopWindow = new CustomPopWindow.PopupWindowBuilder(getActivity())
                .setView(contentView)
                .enableBackgroundDark(true) //弹出popWindow时，背景是否变暗
                .setBgDarkAlpha(0.7f) // 控制亮度
                .setOnDissmissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Log.e("TAG", "onDismiss");
                    }
                })
                .create()
                .showAsDropDown(select_click, 0, 20);
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        KLog.d("main_order:init");
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

    private CustomPopWindow mCustomPopWindow;


    @Override
    public void onPause() {
        super.onPause();
        KLog.d("main_order:pause");
        MobclickAgent.onPageEnd(getString(R.string.client_order));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KLog.d("main_order:destroy");

    }

    @Override
    public void onResume() {
        super.onResume();
        KLog.d("main_order:resume");
        MobclickAgent.onPageStart(getString(R.string.client_order));
    }

    @Override
    protected void setupViewPager(ViewPagerAdapter adapter) {
        if (fragment == null) {
            fragment = new Order_one();
        }
        if (fragment1 == null) {
            fragment1 = new Order_two();
        }
        if (fragment3 == null) {
            fragment3 = new Order_four();
        }
        adapter.addFragment(fragment, "未完成");
        adapter.addFragment(fragment1, "已完成");
//        adapter.addFragment(fragment2,"已完成");
        adapter.addFragment(fragment3, "全部");
    }
}
