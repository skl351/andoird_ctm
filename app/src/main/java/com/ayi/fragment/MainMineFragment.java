package com.ayi.fragment;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.LoginActivity;
import com.ayi.activity.MainActivity;
import com.ayi.activity.RechargeActivity;
import com.ayi.activity.ShareActivity;
import com.ayi.activity.Voice_setting;
import com.ayi.activity.WebViewActivity;
import com.ayi.entity.Result;
import com.ayi.home_page.Coupon_mini;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Web_view;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.milk.base.BaseLoadWithRefreshFragment;
import com.milk.flux.actions.BaseLoadDataActionCreator;
import com.milk.flux.stores.BaseLoadDataStore;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;

import butterknife.Bind;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;
import rx.Observable;

import static com.umeng.analytics.MobclickAgent.onProfileSignOff;

/**
 * Created by oceanzhang on 16/3/24.
 */
public class MainMineFragment extends BaseLoadWithRefreshFragment<JSONObject, BaseLoadDataStore<JSONObject>, BaseLoadDataActionCreator<JSONObject>> {

    @Bind(R.id.frag_main_mine_tv_glod_num)
    TextView tvGlodNum;
    @Bind(R.id.frag_main_mine_tv_remain)
    TextView tvRemain;
    @Bind(R.id.fragment_main_goto_myinfo)
    RelativeLayout fragment_main_goto_myinfo;
    @Bind(R.id.fragment_voice_setting)
    RelativeLayout fragment_voice_setting;
    @Bind(R.id.fragment_main_goto_wodexiajia)
    RelativeLayout fragment_main_goto_wodexiajia;
    @Bind(R.id.fragment_main_goto_common_problem)
    RelativeLayout fragment_main_goto_common_problem;
    @Bind(R.id.fragment_main_goto_about)
    RelativeLayout fragment_main_goto_about;
    @Bind(R.id.frag_main_mine_tv_name)
    TextView tvName;
    @Bind(R.id.textView18)
    TextView textView18;
    @Bind(R.id.act_ayi_version)
    TextView ayi_version;
    @Bind(R.id.congzhi_view)
    View congzhi_view;
    @Bind(R.id.yue_view)
    View yue_view;

    public int screenWidth;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //得到频宽；
        DisplayMetrics outMetrics = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
    }

    public void setwandh(View view) {
        LinearLayout.LayoutParams lp_user_guanyuwomende = (LinearLayout.LayoutParams) view
                .getLayoutParams();
        lp_user_guanyuwomende.width = (screenWidth / 2);
        view.setLayoutParams(lp_user_guanyuwomende);

    }

    @Override
    public void initData() {
        super.initData();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getAction();
            if (str.equals(RetrofitUtil.APP_BORADCASTRECEIVER)) {
                onRefresh();
            }
        }
    };


    @Override
    protected Observable<JSONObject> createRequest() {
        System.out.println("进入1这里");
        Observable.Transformer<Result<JSONObject>, JSONObject> transformer = RetrofitUtil.applySchedulers();

        return RetrofitUtil.getService().customerDetail(AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), AyiApplication.m)
                .compose(transformer);
//        return null;
    }

    @Override
    public void loadError() {
        super.loadError();
        System.out.println("这里是");
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
    }

    @Override
    protected void bindView(JSONObject jsonObject) {
        super.bindView(jsonObject);
        tvGlodNum.setText("我的金币:  " + jsonObject.getString("glodSum"));
        tvGlodNum.setVisibility(View.GONE);
        tvRemain.setText("￥" + jsonObject.getString("remainSum"));
        String phone = AyiApplication.getInstance().accountService().profile().getMobile();
        String name = phone.length() > 4 ? phone.substring(0, 3) + "****" + phone.substring(7) : phone;
        tvName.setText(name);
        ayi_version.setText("版本号:" + getAppVersionName(getContext()));
        setwandh(yue_view);
        setwandh(congzhi_view);
    }

    //得到版本号
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

    @OnClick({R.id.frag_main_mine_btn_logout, R.id.fragment_main_goto_myinfo, R.id.fragment_main_goto_wodexiajia, R.id.fragment_main_goto_common_problem, R.id.fragment_main_goto_about, R.id.textView18, R.id.fragment_voice_setting, R.id.fragment_main_goto_hetongxiazai})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.frag_main_mine_btn_logout:
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(getActivity());
                final AlertDialog alert = builder.create();
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.calcel_rejectokok, null);
                TextView textname = (TextView) view.findViewById(R.id.textname);
                textname.setText("确定要退出吗？");
                view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                        init_loginout();
                    }
                });
                view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });
                alert.setView(view);
                alert.show();

                break;
            case R.id.fragment_main_goto_myinfo:
                intent = new Intent(getActivity(), Coupon_mini.class);
                intent.putExtra("flag", "2");
                startActivity(intent);
                break;
            case R.id.fragment_main_goto_wodexiajia:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://doc.sangeayi.com/refund_notice.html");
                intent.putExtra("title", "退款说明");
                startActivity(intent);
                break;
            case R.id.fragment_main_goto_common_problem:
                intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://doc.sangeayi.com/about_us.html");
                intent.putExtra("title", "关于");
                startActivity(intent);
                break;
            case R.id.fragment_main_goto_about:
                intent = new Intent(getActivity(), ShareActivity.class);
                startActivity(intent);
                break;
            case R.id.textView18:
                intent = new Intent(getActivity(), RechargeActivity.class);
                startActivity(intent);
                break;
            case R.id.fragment_voice_setting:
                intent = new Intent(getActivity(), Voice_setting.class);
                startActivity(intent);
                break;
            case R.id.fragment_main_goto_hetongxiazai:
                 intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("url", "http://doc.sangeayi.com/contracts.html");
                intent.putExtra("title", "合同下载");
                startActivity(intent);
                break;
        }
    }

    private void init_loginout() {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_loginout;
        RequestParams requestParams = new RequestParams();
        requestParams.put("group", "3");
        requestParams.put("client", "2");
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                KLog.e("response", response);
                AyiApplication.getInstance().accountService().logout();
                AyiApplication.getInstance().setWebview_url("");
                AyiApplication.getInstance().setWebview_url2("");
//                LoginActivity.showLoginActivity((BaseActivity) getActivity(), "");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("tab", "0");
                getActivity().startActivity(intent);
                onProfileSignOff();//umeng的方法

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }
        });
    }


    @Override
    protected int getRefreshContentLayoutId() {
        return R.layout.fragment_main_mine2;
    }


    public void onStart() {
        super.onStart();
        registerBoradcastReceiver();
    }

    public void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RetrofitUtil.APP_BORADCASTRECEIVER);
        getActivity().registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    public void onDestroy() {
        super.onDestroy();
        try {
            getActivity().unregisterReceiver(this.mBroadcastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.client_userpage));
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.client_userpage));
    }
}
