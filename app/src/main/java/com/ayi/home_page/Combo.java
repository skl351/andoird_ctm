package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.adapter.RefreshFootAdapter;
import com.ayi.adapter.RefreshcomboAdapter;
import com.ayi.entity.item_combo;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Data_time_cuo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.milk.widget.RecycleViewDivider;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2017/5/23.
 */

public class Combo extends Activity {

    private SwipeRefreshLayout demo_swiperefreshlayout;
    private RecyclerView demo_recycler;
    private RefreshcomboAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;

    private List<item_combo> list = new ArrayList<>();
    int flaf = 1;

    private View progressBar1;
    private View kongbai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.combo_view);
        init_view();
        Do_network(1, 10);
        init_back();
    }

    private void init_back() {
        findViewById(R.id.top).findViewById(R.id.logreg_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init_view() {
        kongbai = findViewById(R.id.kongbai);
        progressBar1 = findViewById(R.id.progressBar1);
        flaf = 1;
        demo_swiperefreshlayout = (SwipeRefreshLayout) findViewById(R.id.demo_swiperefreshlayout);
        demo_recycler = (RecyclerView) findViewById(R.id.demo_recycler);
        demo_recycler.addItemDecoration(new RecycleViewDivider(
                Combo.this, LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.white)));
        //设置刷新时动画的颜色，可以设置4个
        demo_swiperefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);//设置下拉刷新时的进度圈的底部颜色，和旋转的4种颜色。这个不是设置时间而是设置期间的颜色变化
        demo_swiperefreshlayout.setColorSchemeResources(android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_green_light);
        /*
        第一个参数是true表示支持缩放
        第二个参数是负数，表示向下移动多少距离
        第三个参数，表示end的尺寸，
         */
        demo_swiperefreshlayout.setProgressViewOffset(
                false,
                -100,
                (int) TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        24,
                        getResources().getDisplayMetrics())
        );


        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);//表示list的布置方向
        demo_recycler.setLayoutManager(linearLayoutManager); //建立一个linearLayoutManager用来设置给内圈的view

        //添加分隔线


        //外部用来刷新，不会动用内部
        demo_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                KLog.d("zttjiangqq", "invoke onRefresh...");
                flaf = 1;
                Do_network(1, 10);
            }
        });
        //RecyclerView滑动监听
        demo_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                KLog.e("加载更多2" + lastVisibleItem + "," + adapter.getItemCount());
                if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1) == adapter.getItemCount()) {
                    adapter.changeMoreStatus(RefreshFootAdapter.LOADING_MORE);
                    KLog.e("加载更多");
                    flaf++;
                    System.out.println("flaf：" + flaf + ",10");
                    Do_network2(flaf, 10);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }


    private void Do_network2(int flaf, int j) {
        progressBar1.setVisibility(View.VISIBLE);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_taocanlist;//测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("areaid", AyiApplication.area_id);
        requestParams.put("currentpage", flaf);
        requestParams.put("pagesize", j);
        requestParams.put("rowcount", 0);
        KLog.e("requestParams" + requestParams);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                progressBar1.setVisibility(View.GONE);
                try {
                    System.out.println("url_return_list+增加" + jsonObject.toString());
                    if ("200".equals(jsonObject.getString("ret"))) {
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("financialList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            item_combo item_combo = new item_combo();
                            item_combo.setIsimg(jsonArray.getJSONObject(i).getString("isimg"));
                            item_combo.setContent_txt(jsonArray.getJSONObject(i).getString("content_txt"));
                            item_combo.setTime(Data_time_cuo.gettime(jsonArray.getJSONObject(i).getString("timestamp")));
                            item_combo.setAreaid(jsonArray.getJSONObject(i).getString("areaid"));
                            item_combo.setContent_img(jsonArray.getJSONObject(i).getString("content_img"));
                            item_combo.setSimple_img(jsonArray.getJSONObject(i).getString("simple_img"));
                            item_combo.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            item_combo.setPrice(jsonArray.getJSONObject(i).getString("price"));
                            item_combo.setCcsp_id(jsonArray.getJSONObject(i).getString("id"));
                            item_combo.setCornertitle(jsonArray.getJSONObject(i).getString("cornertitle"));
                            list.add(item_combo);
                        }
                        if (list.size() > 0) {
                            kongbai.setVisibility(View.GONE);
                        } else {
                            kongbai.setVisibility(View.VISIBLE);
                        }
                        //适配器添加在内部的view
                        if (adapter == null) {
                            adapter = new RefreshcomboAdapter(Combo.this, list);
                            adapter.setOnItemClickListener(yItemClickListener);
                            demo_recycler.setAdapter(adapter);

                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        demo_swiperefreshlayout.setRefreshing(false);
//

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressBar1.setVisibility(View.GONE);
            }
        });

    }

    private void Do_network(int flaf, int j) {
        progressBar1.setVisibility(View.VISIBLE);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_taocanlist;//测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("areaid", AyiApplication.area_id);
//        requestParams.put("group_id", "3");
        requestParams.put("currentpage", flaf);
        requestParams.put("pagesize", j);
        requestParams.put("rowcount", 0);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                progressBar1.setVisibility(View.GONE);
                try {
                    System.out.println("url_return_list" + jsonObject.toString());
                    if ("200".equals(jsonObject.getString("ret"))) {
                        list.clear();
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("financialList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            item_combo item_combo = new item_combo();
                            item_combo.setIsimg(jsonArray.getJSONObject(i).getString("isimg"));
                            item_combo.setContent_txt(jsonArray.getJSONObject(i).getString("content_txt"));
                            item_combo.setTime(Data_time_cuo.gettime(jsonArray.getJSONObject(i).getString("timestamp")));
                            item_combo.setAreaid(jsonArray.getJSONObject(i).getString("areaid"));
                            item_combo.setContent_img(jsonArray.getJSONObject(i).getString("content_img"));
                            item_combo.setSimple_img(jsonArray.getJSONObject(i).getString("simple_img"));
                            item_combo.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            item_combo.setPrice(jsonArray.getJSONObject(i).getString("price"));
                            item_combo.setCcsp_id(jsonArray.getJSONObject(i).getString("id"));
                            item_combo.setCornertitle(jsonArray.getJSONObject(i).getString("cornertitle"));
//                            item_combo.setType_id(getIntent().getStringExtra("type_id"));
                            list.add(item_combo);
                        }
                        if (list.size() > 0) {
                            kongbai.setVisibility(View.GONE);
                        } else {
                            kongbai.setVisibility(View.VISIBLE);
                        }
                        adapter = new RefreshcomboAdapter(Combo.this, list);
                        adapter.setOnItemClickListener(yItemClickListener);
                        demo_recycler.setAdapter(adapter);
                        demo_swiperefreshlayout.setRefreshing(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressBar1.setVisibility(View.GONE);
            }
        });

    }


    //
    RefreshcomboAdapter.MyItemClickListener yItemClickListener = new RefreshcomboAdapter.MyItemClickListener() {

        @Override
        public void onItemClick(View view, int postion) {
            KLog.e("item_点击1");
            if (AyiApplication.getInstance().getCanvalet() == 1) {
                Intent intent = new Intent(Combo.this, Home_daike_guodu2.class);
                intent.putExtra("ccsp_id", list.get(postion).getCcsp_id());
                startActivity(intent);
            } else {
                Intent intent = new Intent(Combo.this, Business_appointment_tc.class);
                intent.putExtra("ccsp_id", list.get(postion).getCcsp_id());
                startActivity(intent);
            }
        }
    };
}
