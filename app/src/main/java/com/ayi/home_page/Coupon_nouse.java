package com.ayi.home_page;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.adapter.Coupon_mimi_adapter;
import com.ayi.entity.item_coupon;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Data_time_cuo;
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
 * Created by Administrator on 2016/9/5.
 */
public class Coupon_nouse extends Activity {
    private View top;
    private View back;
    GridView gridView;
    PullToRefreshLayout.OnRefreshListener re;
    List<item_coupon> list;
    Coupon_mimi_adapter adapter;
    private String type_num;

    private View empey_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_view_nouse);
        init();
        init_back();
        init_wangluo_init();
    }


    private void init_back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        empey_view=findViewById(R.id.empey_view);
        list=new ArrayList<>();
        top=findViewById(R.id.top);
        back=top.findViewById(R.id.logreg_left);
        gridView = (GridView)findViewById(R.id.content_view);

        /*
		 * 在布局中找到一个自定义了的控件，其实已经写好了，只要给他设置一个监听器实现两个功能。---3
		 */
        ((PullToRefreshLayout) findViewById(R.id.refresh_view))
                .setOnRefreshListener(getlin());
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
//                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 1000);
            }
        };

    }

    int flag=1;
    private void init_wangluo_init() {
        flag=1;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url= RetrofitUtil.url_get_coupon;
        RequestParams requestParams=new RequestParams();
        requestParams.put("user_id",  AyiApplication.getInstance().accountService().id());
        requestParams.put("token",  AyiApplication.getInstance().accountService().token());
        requestParams.put("currentpage",flag);
        requestParams.put("pagesize","10");
        requestParams.put("type","2");
        KLog.d("coupon_service_id",type_num);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    KLog.json(response.toString());
                    JSONArray jsonArray_data=response.getJSONObject("data").getJSONArray("list");

                    if(jsonArray_data.length()==0){
                        empey_view.setVisibility(View.VISIBLE);
                    }else {
                        empey_view.setVisibility(View.GONE);
                    }
                    list.clear();
                    for(int i=0;i<jsonArray_data.length();i++){
                      item_coupon item=new item_coupon();
                        item.setId(jsonArray_data.getJSONObject(i).getString("id"));
                        item.setPlace(jsonArray_data.getJSONObject(i).getString("name"));
                        item.setPrice(jsonArray_data.getJSONObject(i).getString("price"));
                        item.setTime_finish(jsonArray_data.getJSONObject(i).getString("time_finish"));
                        item.setTime_start(jsonArray_data.getJSONObject(i).getString("time_start"));
                        String zuihou_time=jsonArray_data.getJSONObject(i).getString("time_finish");
                        item.setStatus(jsonArray_data.getJSONObject(i).getString("status"));
                        item.setTypenames(jsonArray_data.getJSONObject(i).getString("typenames"));
                        if(!Data_time_cuo.comp_time_cuo(zuihou_time,System.currentTimeMillis()/1000)){//过期
                            item.setStatus("2");
                        }
                        list.add(item);
                    }
                    adapter=new Coupon_mimi_adapter(Coupon_nouse.this, list);
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
        String url= RetrofitUtil.url_get_coupon;
        RequestParams requestParams=new RequestParams();
        flag++;
        requestParams.put("user_id",  AyiApplication.getInstance().accountService().id());
        requestParams.put("token",  AyiApplication.getInstance().accountService().token());

        requestParams.put("currentpage",flag);
        requestParams.put("pagesize","10");
        requestParams.put("type","2");
        KLog.d("coupon_service_id",type_num);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    System.out.println(response);
                    JSONArray jsonArray_data=response.getJSONObject("data").getJSONArray("list");
                    for(int i=0;i<jsonArray_data.length();i++){
                        item_coupon item=new item_coupon();
                        item.setId(jsonArray_data.getJSONObject(i).getString("id"));
                        item.setPlace(jsonArray_data.getJSONObject(i).getString("name"));
                        item.setPrice(jsonArray_data.getJSONObject(i).getString("price"));
                        item.setTime_finish(jsonArray_data.getJSONObject(i).getString("time_finish"));
                        item.setTime_start(jsonArray_data.getJSONObject(i).getString("time_start"));
                        String zuihou_time=jsonArray_data.getJSONObject(i).getString("time_finish");
                        item.setStatus(jsonArray_data.getJSONObject(i).getString("status"));
                        item.setTypenames(jsonArray_data.getJSONObject(i).getString("typenames"));
                        if(!Data_time_cuo.comp_time_cuo(zuihou_time,System.currentTimeMillis()/1000)){//过期
                            item.setStatus("2");
                        }
                        list.add(item);

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


}
