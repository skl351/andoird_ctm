package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.adapter.Coupon_adapter;
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
public class Coupon extends Activity {
    private View top;
    private View back;
    GridView gridView;
    PullToRefreshLayout.OnRefreshListener re;
    List<item_coupon> list;
    Coupon_adapter adapter;
    private String type_num;
    private View empey_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_view);
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

    private String areaid;
    private void init() {
        areaid=getIntent().getStringExtra("areaid");
        empey_view=findViewById(R.id.empey_view);
        list=new ArrayList<>();
        top=findViewById(R.id.top);
        back=top.findViewById(R.id.logreg_left);
        gridView = (GridView)findViewById(R.id.content_view);
        if(getIntent().getStringExtra("flag").equals("1")){
            type_num= getIntent().getStringExtra("type_num");
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (list.get(position).getStatus()){
                        case "0":
                            Toast.makeText(Coupon.this,"已使用",Toast.LENGTH_SHORT).show();
                            break;
                        case "1":
                            Intent intent=new Intent(Coupon.this,Order_pay.class);
                            intent.putExtra("coupon",list.get(position));
                            startActivity(intent);
                            Coupon.this.finish();
                            break;
                        case "2": Toast.makeText(Coupon.this,"已过期",Toast.LENGTH_SHORT).show();break;
                    }
                }
            });
        }else{
            type_num="-1";
        }
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
        if(type_num!=null&&(!type_num.equals("-1"))){
            requestParams.put("typeid",type_num);
        }
        requestParams.put("currentpage",flag);
        requestParams.put("pagesize","10");
        requestParams.put("areaid", areaid);
        System.out.println("areaid:"+areaid);
        KLog.d("coupon_service",requestParams);

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
                        item.setPlace(jsonArray_data.getJSONObject(i).getString("name"));
//                        item.setRole(jsonArray_data.getJSONObject(i).getString("role"));
                        item.setId(jsonArray_data.getJSONObject(i).getString("id"));
                        item.setPrice(jsonArray_data.getJSONObject(i).getString("price"));
                        item.setTime_finish(jsonArray_data.getJSONObject(i).getString("time_finish"));
                        item.setTime_start(jsonArray_data.getJSONObject(i).getString("time_start"));
                        String zuihou_time=jsonArray_data.getJSONObject(i).getString("time_finish");
                        item.setStatus(jsonArray_data.getJSONObject(i).getString("status"));
//                        item.setTypeids(jsonArray_data.getJSONObject(i).getString("typeids"));
                        item.setTypenames(jsonArray_data.getJSONObject(i).getString("typenames"));
                        if(!Data_time_cuo.comp_time_cuo(zuihou_time,System.currentTimeMillis()/1000)){//过期
                            item.setStatus("2");
                        }
                        list.add(item);
                    }
                    adapter=new Coupon_adapter(Coupon.this, list);
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
        if(!type_num.equals("-1")){
            requestParams.put("typeid",type_num);
        }
        requestParams.put("currentpage",flag);
        requestParams.put("pagesize","10");
        requestParams.put("areaid", areaid);
        KLog.d("areaid:",type_num+","+areaid);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    System.out.println(response);
                    JSONArray jsonArray_data=response.getJSONObject("data").getJSONArray("list");
                    for(int i=0;i<jsonArray_data.length();i++){
                        item_coupon item=new item_coupon();
                        item.setPlace(jsonArray_data.getJSONObject(i).getString("name"));
//                        item.setRole(jsonArray_data.getJSONObject(i).getString("role"));
                        item.setId(jsonArray_data.getJSONObject(i).getString("id"));
                        item.setPrice(jsonArray_data.getJSONObject(i).getString("price"));
                        item.setTime_finish(jsonArray_data.getJSONObject(i).getString("time_finish"));
                        item.setTime_start(jsonArray_data.getJSONObject(i).getString("time_start"));
                        String zuihou_time=jsonArray_data.getJSONObject(i).getString("time_finish");
                        item.setStatus(jsonArray_data.getJSONObject(i).getString("status"));
//                        item.setTypeids(jsonArray_data.getJSONObject(i).getString("typeids"));
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
