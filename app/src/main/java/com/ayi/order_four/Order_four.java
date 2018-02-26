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
import com.ayi.adapter.Order_four_adapter;
import com.ayi.entity.item_quanbu;
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
 * Created by Administrator on 2016/8/29.
 */
public class Order_four extends Fragment {
    private View view;
    GridView gridView;
    PullToRefreshLayout.OnRefreshListener re;
    private Order_four_adapter adapter;
    List<item_quanbu> list;
    private View kongbai;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("出现4" + isVisibleToUser);

        if (isVisibleToUser) {
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
                init_wangluo_init();
            }
        }
    };

    public void registerBoradcastReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RetrofitUtil.APP_BORADCASTRECEIVER);
        getActivity().registerReceiver(this.mBroadcastReceiver, intentFilter);
    }

    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(this.mBroadcastReceiver);
    }

    private View progressBar1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_four, container, false);
        progressBar1 = view.findViewById(R.id.progressBar1);
        gridView = (GridView) view.findViewById(R.id.content_view);
        kongbai = view.findViewById(R.id.kongbai);
        list = new ArrayList<item_quanbu>();
        init_wangluo_init();
/*
         * 在布局中找到一个自定义了的控件，其实已经写好了，只要给他设置一个监听器实现两个功能。---3
		 */
        ((PullToRefreshLayout) view.findViewById(R.id.refresh_view))
                .setOnRefreshListener(getlin());

        return view;

    }

    int flag = 1;

    private void init_wangluo_init() {
        if (progressBar1 != null) {
            progressBar1.setVisibility(View.VISIBLE);
        }
        flag = 1;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_list_order;
        RequestParams requestParams = new RequestParams();
        requestParams.put("type", 0);
        requestParams.put("currentpage", flag);
        requestParams.put("pagesize", 10);
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        KLog.e(AyiApplication.getInstance().accountService().id() + "," + AyiApplication.getInstance().accountService().token());
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    System.out.println(response.toString());
                    JSONArray jsonArray_data = response.getJSONArray("data");
                    if (jsonArray_data.length() == 0) {
                        kongbai.setVisibility(View.VISIBLE);
                    } else {
                        kongbai.setVisibility(View.GONE);
                    }
                    list.clear();
                    for (int i = 0; i < jsonArray_data.length(); i++) {
                        item_quanbu item_weiwancheng = new item_quanbu();
                        item_weiwancheng.setPolicynum_customer(jsonArray_data.getJSONObject(i).getString("policynum_customer"));
                        item_weiwancheng.setAreaid(jsonArray_data.getJSONObject(i).getString("areaid"));
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
                        item_weiwancheng.setStatus3(jsonArray_data.getJSONObject(i).getString("status3"));
                        item_weiwancheng.setParentoid(jsonArray_data.getJSONObject(i).getString("parentoid"));
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
                    adapter = new Order_four_adapter(getActivity(), list);
                    gridView.setAdapter(adapter);
                    if (progressBar1 != null) {
                        progressBar1.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    if (progressBar1 != null) {
                        progressBar1.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (progressBar1 != null) {
                    progressBar1.setVisibility(View.GONE);
                }
            }
        });

    }

    private void init_wangluo_init2(final PullToRefreshLayout pullToRefreshLayout) {
        if (progressBar1 != null) {
            progressBar1.setVisibility(View.VISIBLE);
        }
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_list_order;
        RequestParams requestParams = new RequestParams();
        requestParams.put("type", 0);
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
                        item_quanbu item_weiwancheng = new item_quanbu();
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
                        item_weiwancheng.setOrdernum(jsonArray_data.getJSONObject(i).getString("ordernum"));
                        item_weiwancheng.setStatus3(jsonArray_data.getJSONObject(i).getString("status3"));
                        item_weiwancheng.setParentoid(jsonArray_data.getJSONObject(i).getString("parentoid"));
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
                    if (progressBar1 != null) {
                        progressBar1.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    if (progressBar1 != null) {
                        progressBar1.setVisibility(View.GONE);
                    }
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                if (progressBar1 != null) {
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
                        init_wangluo_init();
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
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
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 1000);

            }
        };

    }

    @Override
    public void onResume() {
        super.onResume();
//        if(MainActivity.for_refesh.equals("1")){
//            init_wangluo_init();
//            MainActivity.for_refesh="0";
//        }
        init_wangluo_init();
        System.out.println("出现谁快");


    }
}
