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
import com.ayi.adapter.Order_two_adapter;
import com.ayi.entity.item_daipingjia;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.zidingyi_view.PullToRefreshLayout;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/8/29.
 */
public class Order_two extends Fragment {
    private View view;
    GridView gridView;
    PullToRefreshLayout.OnRefreshListener re;
    private Order_two_adapter adapter;
    List<item_daipingjia> list ;
    private View kongbai;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        System.out.println("出现2"+isVisibleToUser);
        if(isVisibleToUser){
            init_wangluo_init();
        }
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=  inflater.inflate(R.layout.order_two, container, false);
        gridView = (GridView) view.findViewById(R.id.content_view);
        kongbai=view.findViewById(R.id.kongbai);
        list=new ArrayList<item_daipingjia>();
        init_wangluo_init();
 /*
		 * 在布局中找到一个自定义了的控件，其实已经写好了，只要给他设置一个监听器实现两个功能。---3
		 */
        ((PullToRefreshLayout) view.findViewById(R.id.refresh_view))
                .setOnRefreshListener(getlin());
        return view;

    }

    int flag=1;
    private void init_wangluo_init() {
        flag=1;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url= RetrofitUtil.url_list_order;
        RequestParams requestParams=new RequestParams();
        requestParams.put("type", 3);
        requestParams.put("currentpage", flag);
        requestParams.put("pagesize", 10);
        requestParams.put("user_id",  AyiApplication.getInstance().accountService().id());
        requestParams.put("token",  AyiApplication.getInstance().accountService().token());
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    System.out.println("response"+response);
                    JSONArray jsonArray_data=response.getJSONArray("data");
                    if (jsonArray_data.length()==0){
                        kongbai.setVisibility(View.VISIBLE);
                    }else {
                        kongbai.setVisibility(View.GONE);
                    }
                    list.clear();
                    for(int i=0;i<jsonArray_data.length();i++){
                        item_daipingjia item_weiwancheng=new item_daipingjia();
                        item_weiwancheng.setPolicynum_customer(jsonArray_data.getJSONObject(i).getString("policynum_customer"));
                        item_weiwancheng.setService_type_id(jsonArray_data.getJSONObject(i).getString("service_type_id"));
                        item_weiwancheng.setTotal_money(jsonArray_data.getJSONObject(i).getString("pricetotal"));
                        item_weiwancheng.setSuishoudai_money(jsonArray_data.getJSONObject(i).getString("mtotal"));
                        item_weiwancheng.setGet_time(jsonArray_data.getJSONObject(i).getString("timestamp"));
                        item_weiwancheng.setPlace(jsonArray_data.getJSONObject(i).getString("contact_address"));
                        item_weiwancheng.setUser_name(jsonArray_data.getJSONObject(i).getString("contacts"));
                        item_weiwancheng.setService_content(jsonArray_data.getJSONObject(i).getString("service_type"));
                        item_weiwancheng.setService_time1(jsonArray_data.getJSONObject(i).getJSONObject("serviceShow").getString("time"));
                        item_weiwancheng.setOrderid(jsonArray_data.getJSONObject(i).getString("id"));
                        item_weiwancheng.setStatus(jsonArray_data.getJSONObject(i).getString("status"));
                        item_weiwancheng.setTrialorder(jsonArray_data.getJSONObject(i).getString("trialorder"));
                        item_weiwancheng.setAreaid(jsonArray_data.getJSONObject(i).getString("areaid"));
                        item_weiwancheng.setStatus2(jsonArray_data.getJSONObject(i).getString("status2"));
                        item_weiwancheng.setAyi_id(jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").getString("id"));
                        item_weiwancheng.setIsvalet(jsonArray_data.getJSONObject(i).getInt("isvalet"));
                        if (jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").length()>0){
                            item_weiwancheng.setCleaner_headimg(jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").getString("headimg"));
                            item_weiwancheng.setCleaner_name(jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").getString("name"));
                        }
                        list.add(item_weiwancheng);
                    }
                    adapter=new Order_two_adapter(getActivity(), list);
                    gridView.setAdapter(adapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }
        });

    }
    private void init_wangluo_init2(final PullToRefreshLayout pullToRefreshLayout) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url= RetrofitUtil.url_list_order;
        RequestParams requestParams=new RequestParams();
        requestParams.put("type", 3);
        flag++;
        requestParams.put("currentpage", flag);
        requestParams.put("pagesize", 10);
        requestParams.put("user_id",  AyiApplication.getInstance().accountService().id());
        requestParams.put("token",  AyiApplication.getInstance().accountService().token());
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray_data=response.getJSONArray("data");
                    for(int i=0;i<jsonArray_data.length();i++){
                        item_daipingjia item_weiwancheng=new item_daipingjia();
                        item_weiwancheng.setPolicynum_customer(jsonArray_data.getJSONObject(i).getString("policynum_customer"));
                        item_weiwancheng.setTotal_money(jsonArray_data.getJSONObject(i).getString("pricetotal"));
                        item_weiwancheng.setSuishoudai_money(jsonArray_data.getJSONObject(i).getString("mtotal"));
                        item_weiwancheng.setGet_time(jsonArray_data.getJSONObject(i).getString("timestamp"));
                        item_weiwancheng.setPlace(jsonArray_data.getJSONObject(i).getString("contact_address"));
                        item_weiwancheng.setUser_name(jsonArray_data.getJSONObject(i).getString("contacts"));
                        item_weiwancheng.setService_content(jsonArray_data.getJSONObject(i).getString("service_type"));
                        item_weiwancheng.setService_time1(jsonArray_data.getJSONObject(i).getJSONObject("serviceShow").getString("time"));
                        item_weiwancheng.setOrderid(jsonArray_data.getJSONObject(i).getString("id"));
                        item_weiwancheng.setStatus(jsonArray_data.getJSONObject(i).getString("status"));
                        item_weiwancheng.setTrialorder(jsonArray_data.getJSONObject(i).getString("trialorder"));
                        item_weiwancheng.setService_type_id(jsonArray_data.getJSONObject(i).getString("service_type_id"));
                        item_weiwancheng.setAreaid(jsonArray_data.getJSONObject(i).getString("areaid"));
                        item_weiwancheng.setStatus2(jsonArray_data.getJSONObject(i).getString("status2"));
                        item_weiwancheng.setAyi_id(jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").getString("id"));
                        item_weiwancheng.setIsvalet(jsonArray_data.getJSONObject(i).getInt("isvalet"));
                        if (jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").length()>0){
                            item_weiwancheng.setCleaner_headimg(jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").getString("headimg"));
                            item_weiwancheng.setCleaner_name(jsonArray_data.getJSONObject(i).getJSONObject("cleanerInfo").getString("name"));
                        }
                        list.add(item_weiwancheng);
                    }
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);

            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);

            }
        });

    }
    /**
     * 在其中实现上拉和下拉的功能-----4----最主要的地方
     * @return
     */
    private PullToRefreshLayout.OnRefreshListener getlin() {
        return re=new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(final PullToRefreshLayout pullToRefreshLayout) {
                new Handler()
                {
                    @Override
                    public void handleMessage(Message msg)
                    {
                        init_wangluo_init();
                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 1000);
            }
            @Override
            public void onLoadMore(final PullToRefreshLayout pullToRefreshLayout) {
                new Handler()
                {
                    @Override
                    public void handleMessage(Message msg)
                    {
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
