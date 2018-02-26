package com.ayi.order_four;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.MainActivity;
import com.ayi.adapter.Order_one_adapter;
import com.ayi.entity.item_weiwancheng;
import com.ayi.entity.mlist_pay;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.zidingyi_view.PullToRefreshLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 52ebef7c
 * Created by Administrator on 2016/8/29.
 */
public class Order_one extends Fragment {
    private View view;
    GridView gridView;
    PullToRefreshLayout.OnRefreshListener re;
    private Order_one_adapter adapter;
    List<item_weiwancheng> list;
    private boolean flag_show = false;
    private View kongbai;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        KLog.d("one_order:出现1" + isVisibleToUser);
        if (isVisibleToUser) {
            System.out.println("又一次出现了setUserVisibleHint");
            init_wangluo_init();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        registerBoradcastReceiver();
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getAction();
            if (str.equals(RetrofitUtil.APP_BORADCASTRECEIVER)) {
                System.out.println("又一次出现了APP_BORADCASTRECEIVER");
                init_wangluo_init();
            }
        }
    };

    public void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RetrofitUtil.APP_BORADCASTRECEIVER);
        getActivity().registerReceiver(this.mBroadcastReceiver, intentFilter);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (MainActivity.for_refesh.equals("1")) {
            System.out.println("又一次出现了for_refesh");
            init_wangluo_init();
            MainActivity.for_refesh = "0";
        }
        KLog.d("one_order:resume" + this);
    }

    @Override
    public void onPause() {
        super.onPause();
        KLog.d("one_order:pause");
        KLog.e("one_order:pause" + this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KLog.d("one_order:ondestroy");
        getActivity().unregisterReceiver(this.mBroadcastReceiver);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        KLog.d("one_order:onViewCreated");
    }

    private View progressBar1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_one, container, false);
        KLog.d("one_order:onViewCreated");
        progressBar1 = view.findViewById(R.id.progressBar1);
        gridView = (GridView) view.findViewById(R.id.content_view);
        kongbai = view.findViewById(R.id.kongbai);
        System.out.println("又一次出现了create");
        init_wangluo_init();
        /*
         * 在布局中找到一个自定义了的控件，其实已经写好了，只要给他设置一个监听器实现两个功能。---3
		 */
        ((PullToRefreshLayout) view.findViewById(R.id.refresh_view))
                .setOnRefreshListener(getlin());
        return view;

    }

    int flag = 1;


    /**
     * 这个对订单列表的未完成订单表的请求接口
     */
    private void init_wangluo_init() {
        if (progressBar1 != null) {
            progressBar1.setVisibility(View.VISIBLE);
        }
        flag = 1;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_list_order;
        RequestParams requestParams = new RequestParams();
        requestParams.put("type", 1);//0表示全部订单，1表示未完成订单，2表示待评价，3表示已完成
        requestParams.put("currentpage", flag);
        requestParams.put("pagesize", 10);
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    KLog.json("kai", response.toString());

                    JSONArray jsonArray_data = response.getJSONArray("data");
                    if (jsonArray_data.length() == 0) {
                        kongbai.setVisibility(View.VISIBLE);
                    } else {
                        kongbai.setVisibility(View.GONE);
                    }
                    list = new ArrayList<item_weiwancheng>();
                    for (int i = 0; i < jsonArray_data.length(); i++) {

                        item_weiwancheng item_weiwancheng = new item_weiwancheng();
                        item_weiwancheng.setAreaid(jsonArray_data.getJSONObject(i).getString("areaid"));
                        item_weiwancheng.setPolicynum_customer(jsonArray_data.getJSONObject(i).getString("policynum_customer"));
                        item_weiwancheng.setService_type_id(jsonArray_data.getJSONObject(i).getString("service_type_id"));
                        item_weiwancheng.setTotal_money(jsonArray_data.getJSONObject(i).getString("pricetotal"));
                        item_weiwancheng.setSuishoudai_money(jsonArray_data.getJSONObject(i).getString("mtotal"));
                        item_weiwancheng.setGet_time(jsonArray_data.getJSONObject(i).getString("timestamp"));
                        item_weiwancheng.setService_content(jsonArray_data.getJSONObject(i).getString("service_type"));
                        item_weiwancheng.setService_time1(jsonArray_data.getJSONObject(i).getJSONObject("serviceShow").getString("time"));
                        item_weiwancheng.setOrderid(jsonArray_data.getJSONObject(i).getString("id"));
                        item_weiwancheng.setStatus(jsonArray_data.getJSONObject(i).getString("status"));
                        item_weiwancheng.setPayed(jsonArray_data.getJSONObject(i).getString("payed"));
                        item_weiwancheng.setPlace(jsonArray_data.getJSONObject(i).getString("contact_address"));
                        item_weiwancheng.setUser_name(jsonArray_data.getJSONObject(i).getString("contacts"));
                        item_weiwancheng.setTrialorder(jsonArray_data.getJSONObject(i).getString("trialorder"));
                        item_weiwancheng.setStatus2(jsonArray_data.getJSONObject(i).getString("status2"));
                        item_weiwancheng.setPhone(jsonArray_data.getJSONObject(i).getString("contact_phone"));
                        item_weiwancheng.setOrdernum(jsonArray_data.getJSONObject(i).getString("ordernum"));
                        item_weiwancheng.setIsvalet(jsonArray_data.getJSONObject(i).getInt("isvalet"));
                        item_weiwancheng.setStatus3(jsonArray_data.getJSONObject(i).getString("status3"));
                        item_weiwancheng.setParentoid(jsonArray_data.getJSONObject(i).getString("parentoid"));
                        if (jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").has("id")) {
                            item_weiwancheng.setAyi_id(jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").getString("id"));
                        }
                        List<mlist_pay> list_item = new ArrayList<mlist_pay>();
                        for (int j = 0; j < jsonArray_data.getJSONObject(i).getJSONArray("detail").length(); j++) {
                           mlist_pay item = new mlist_pay();
                            item.setPrice(jsonArray_data.getJSONObject(i).getJSONArray("detail").getJSONObject(j).getString("price"));
                            item.setProject(jsonArray_data.getJSONObject(i).getJSONArray("detail").getJSONObject(j).getString("name"));
                            item.setQuantity(jsonArray_data.getJSONObject(i).getJSONArray("detail").getJSONObject(j).getString("quantity"));
                            list_item.add(item);
                        }
                        if (jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").length() > 0) {
                            item_weiwancheng.setCleaner_headimg(jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").getString("headimg"));
                            item_weiwancheng.setCleaner_name(jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").getString("name"));
                        }
                        item_weiwancheng.setList_item(list_item);
                        list.add(item_weiwancheng);
                    }
                    adapter = new Order_one_adapter(getActivity(), list);

                    gridView.setAdapter(adapter);

                    if (progressBar1!=null){
                        progressBar1.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    if (progressBar1!=null){
                        progressBar1.setVisibility(View.GONE);
                    }
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (progressBar1!=null){
                    progressBar1.setVisibility(View.GONE);
                }
            }
        });

    }

    private void init_wangluo_init2(final PullToRefreshLayout pullToRefreshLayout) {
        if (progressBar1!=null){
            progressBar1.setVisibility(View.VISIBLE);
        }
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_list_order;
        RequestParams requestParams = new RequestParams();
        requestParams.put("type", 1);
        flag++;
        requestParams.put("currentpage", flag);
        requestParams.put("pagesize", 10);
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray_data = response.getJSONArray("data");
                    for (int i = 0; i < jsonArray_data.length(); i++) {
                        item_weiwancheng item_weiwancheng = new item_weiwancheng();
                        item_weiwancheng.setAreaid(jsonArray_data.getJSONObject(i).getString("areaid"));
                        item_weiwancheng.setPolicynum_customer(jsonArray_data.getJSONObject(i).getString("policynum_customer"));
                        item_weiwancheng.setService_type_id(jsonArray_data.getJSONObject(i).getString("service_type_id"));
                        item_weiwancheng.setTotal_money(jsonArray_data.getJSONObject(i).getString("pricetotal"));
                        item_weiwancheng.setSuishoudai_money(jsonArray_data.getJSONObject(i).getString("mtotal"));
                        item_weiwancheng.setGet_time(jsonArray_data.getJSONObject(i).getString("timestamp"));
                        item_weiwancheng.setService_content(jsonArray_data.getJSONObject(i).getString("service_type"));
                        item_weiwancheng.setService_time1(jsonArray_data.getJSONObject(i).getJSONObject("serviceShow").getString("time"));
                        item_weiwancheng.setOrderid(jsonArray_data.getJSONObject(i).getString("id"));
                        item_weiwancheng.setStatus(jsonArray_data.getJSONObject(i).getString("status"));
                        item_weiwancheng.setPayed(jsonArray_data.getJSONObject(i).getString("payed"));
                        item_weiwancheng.setPlace(jsonArray_data.getJSONObject(i).getString("contact_address"));
                        item_weiwancheng.setUser_name(jsonArray_data.getJSONObject(i).getString("contacts"));
                        item_weiwancheng.setStatus2(jsonArray_data.getJSONObject(i).getString("status2"));
                        item_weiwancheng.setTrialorder(jsonArray_data.getJSONObject(i).getString("trialorder"));
                        item_weiwancheng.setPhone(jsonArray_data.getJSONObject(i).getString("contact_phone"));
                        item_weiwancheng.setStatus3(jsonArray_data.getJSONObject(i).getString("status3"));
                        item_weiwancheng.setParentoid(jsonArray_data.getJSONObject(i).getString("parentoid"));
                        item_weiwancheng.setOrdernum(jsonArray_data.getJSONObject(i).getString("ordernum"));
                        item_weiwancheng.setIsvalet(jsonArray_data.getJSONObject(i).getInt("isvalet"));
                        if (jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").has("id")) {
                            item_weiwancheng.setAyi_id(jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").getString("id"));
                        }
                        List<mlist_pay> list_item = new ArrayList<mlist_pay>();
                        for (int j = 0; j < jsonArray_data.getJSONObject(i).getJSONArray("detail").length(); j++) {
                            mlist_pay item = new mlist_pay();
                            item.setPrice(jsonArray_data.getJSONObject(i).getJSONArray("detail").getJSONObject(j).getString("price"));
                            item.setProject(jsonArray_data.getJSONObject(i).getJSONArray("detail").getJSONObject(j).getString("name"));
                            item.setQuantity(jsonArray_data.getJSONObject(i).getJSONArray("detail").getJSONObject(j).getString("quantity"));
                            list_item.add(item);
                        }
                        item_weiwancheng.setList_item(list_item);
                        if (jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").length() > 0) {
                            item_weiwancheng.setCleaner_headimg(jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").getString("headimg"));
                            item_weiwancheng.setCleaner_name(jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").getString("name"));
                        }

                        list.add(item_weiwancheng);
                    }
                    adapter.notifyDataSetChanged();
                    if (progressBar1!=null){
                        progressBar1.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    if (progressBar1!=null){
                        progressBar1.setVisibility(View.GONE);
                    }
                    e.printStackTrace();
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (progressBar1!=null){
                    progressBar1.setVisibility(View.GONE);
                }
            }
        });

    }

    /**
     * 在其中实现上拉和下拉的功能-----4----最主要的地方
     *
     * @return
     */
    private PullToRefreshLayout.OnRefreshListener getlin() {
        return re = new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        System.out.println("又一次出现了下拉");
                        init_wangluo_init();
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        System.out.println("有没有1");
                    }
                }.sendEmptyMessageDelayed(0, 1000);
            }

            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        System.out.println("上拉加载");
                        // 千万别忘了告诉控件加载完毕了哦！
                        init_wangluo_init2(pullToRefreshLayout);
                        System.out.println("有没有2");
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 1000);

            }
        };

    }


}
