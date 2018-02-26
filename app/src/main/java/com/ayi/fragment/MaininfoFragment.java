package com.ayi.fragment;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.LoginActivity;
import com.ayi.activity.MainActivity;
import com.ayi.adapter.RefreshFootAdapter;
import com.ayi.datadao.ContactInfoDao;
import com.ayi.entity.item_info;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Data_time_cuo;
import com.ayi.zidingyi_view.web_view_info;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.milk.base.BaseFragment;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2017/5/11.
 */

public class MaininfoFragment extends BaseFragment {
    private View kongbai;
    private SwipeRefreshLayout demo_swiperefreshlayout;
    private RecyclerView demo_recycler;
    private RefreshFootAdapter adapter;
    private LinearLayoutManager linearLayoutManager;
    private int lastVisibleItem;
    private List<item_info> list = new ArrayList<>();
    int flaf = 1;
    private ContactInfoDao dao;
    private View main_view;
    @Bind(R.id.fragment_statistics_webview)
    FrameLayout frameLayout;

    @Override
    public void initView(View view) {
        super.initView(view);
        main_view = LayoutInflater.from(getContext()).inflate(R.layout.maininfofragment_view, null);
        //用来测试的文件下载
//        main_view.findViewById(R.id.logreg_left).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(),ceshi_download.class);
//                getActivity().startActivity(intent);
//            }
//        });
        //之间是一定的测试代码
        kongbai = main_view.findViewById(R.id.kongbai);
        frameLayout.addView(main_view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        dao = new ContactInfoDao(getActivity());
        init_view();
        if (AyiApplication.getInstance().accountService().id().isEmpty() && AyiApplication.getInstance().accountService().token().isEmpty()) {
            Intent intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        Do_network(1, 10);//进来就刷新
    }


    private void init_view() {
        flaf = 1;
        demo_swiperefreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.demo_swiperefreshlayout);
        demo_recycler = (RecyclerView) view.findViewById(R.id.demo_recycler);
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


        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(OrientationHelper.VERTICAL);//表示list的布置方向
        demo_recycler.setLayoutManager(linearLayoutManager); //建立一个linearLayoutManager用来设置给内圈的view

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
                try {
                    KLog.e("加载更多2" + lastVisibleItem + "," + adapter.getItemCount());
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1) == adapter.getItemCount()) {
                        adapter.changeMoreStatus(RefreshFootAdapter.LOADING_MORE);

                        KLog.e("加载更多");
                        flaf++;
                        Do_network2(flaf, 10);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            }
        });
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//
//    }

    RefreshFootAdapter.MyItemClickListener yItemClickListener = new RefreshFootAdapter.MyItemClickListener() {

        @Override
        public void onItemClick(View view, int postion) {
            KLog.e("item_点击1");
            try {
                KLog.e("item_点击2");
                if (dao.getPhoneNumber(list.get(postion).getId(), AyiApplication.getInstance().accountService().id()) == null) {
                    KLog.e("item_点击3");
                    dao.add(list.get(postion).getId(), AyiApplication.getInstance().accountService().id(), "1");
                    list.get(postion).setFlag_review(true);
                    adapter.notifyDataSetChanged();
                    MainActivity m = (MainActivity) getActivity();
                    m.do_math(10);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            Intent intent = new Intent(getActivity(), web_view_info.class);
//            intent.putExtra("web_url",list.get(postion).getUrl());
//            System.out.println("list.get(postion).getUrl()"+list.get(postion).getUrl());
//            intent.putExtra("title",list.get(postion).getTitle());
            intent.putExtra("id", list.get(postion).getId());
            getActivity().startActivity(intent);

        }
    };

    private void Do_network2(final int flaf, final int j) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_return_list;//测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("areaid", AyiApplication.area_id);
        requestParams.put("group_id", "3");
        requestParams.put("currentpage", flaf);
        requestParams.put("pagesize", j);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    System.out.println("url_return_list+增加" + jsonObject.toString());
                    if ("200".equals(jsonObject.getString("ret"))) {
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONObject("financialList").getJSONArray("financialList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            item_info item_info = new item_info();
                            String url = jsonObject.getJSONObject("data").getString("info_url") + "?user_id=" + AyiApplication.getInstance().accountService().id() + "&token=" + AyiApplication.getInstance().accountService().token() + "&id=" + jsonArray.getJSONObject(i).getString("id");
                            item_info.setUrl(url);
                            item_info.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            item_info.setId(jsonArray.getJSONObject(i).getString("id"));
//                            item_info.setContent("这个是内容");
                            item_info.setTimestamp(Data_time_cuo.gettime2(jsonArray.getJSONObject(i).getString("timestamp")));
                            try {
                                if (dao.getPhoneNumber(jsonArray.getJSONObject(i).getString("id"), AyiApplication.getInstance().accountService().id()).equals("1")) {
                                    item_info.setFlag_review(true);
                                } else {
                                    item_info.setFlag_review(false);
                                }
                            } catch (Exception e) {
                                item_info.setFlag_review(false);
                            }

                            list.add(item_info);
                        }
                        if (list.size() > 0) {
                            kongbai.setVisibility(View.GONE);
                        } else {
                            kongbai.setVisibility(View.VISIBLE);
                        }
                        //适配器添加在内部的view
                        if (adapter == null) {
                            adapter = new RefreshFootAdapter(getActivity(), list);
                            adapter.setOnItemClickListener(yItemClickListener);
                            demo_recycler.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }
                        demo_swiperefreshlayout.setRefreshing(false);
//                        Toast.makeText(getActivity(), "新增了对应数据...", Toast.LENGTH_SHORT).show();
                        MainActivity m = (MainActivity) getActivity();
                        m.do_math(10);

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

    private void Do_network(int flaf, int j) {

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_return_list;//测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("areaid", AyiApplication.area_id);
        requestParams.put("group_id", "3");
        requestParams.put("currentpage", flaf);
        requestParams.put("pagesize", j);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    System.out.println("url_return_list" + jsonObject.toString());
                    if ("200".equals(jsonObject.getString("ret"))) {
                        list.clear();
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONObject("financialList").getJSONArray("financialList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            item_info item_info = new item_info();
                            String url = jsonObject.getJSONObject("data").getString("info_url") + "?user_id=" + AyiApplication.getInstance().accountService().id() + "&token=" + AyiApplication.getInstance().accountService().token() + "&id=" + jsonArray.getJSONObject(i).getString("id");
                            item_info.setUrl(url);
                            item_info.setTitle(jsonArray.getJSONObject(i).getString("title"));
                            item_info.setId(jsonArray.getJSONObject(i).getString("id"));
//                            item_info.setContent("这个是内容");
                            item_info.setTimestamp(Data_time_cuo.gettime2(jsonArray.getJSONObject(i).getString("timestamp")));
                            try {
                                if (dao.getPhoneNumber(jsonArray.getJSONObject(i).getString("id"), AyiApplication.getInstance().accountService().id()).equals("1")) {

                                    item_info.setFlag_review(true);
                                } else {

                                    item_info.setFlag_review(false);
                                }
                            } catch (Exception e) {
                                item_info.setFlag_review(false);
                            }
                            list.add(item_info);
                        }
                        if (list.size() > 0) {
                            kongbai.setVisibility(View.GONE);
                        } else {
                            kongbai.setVisibility(View.VISIBLE);
                        }
                        adapter = new RefreshFootAdapter(getActivity(), list);
                        adapter.setOnItemClickListener(yItemClickListener);
                        demo_recycler.setAdapter(adapter);
                        demo_swiperefreshlayout.setRefreshing(false);
//                        Toast.makeText(getActivity(), "更新了对应数据...", Toast.LENGTH_SHORT).show();
                        MainActivity m = (MainActivity) getActivity();
                        m.do_math(10);
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

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main_index;
    }
}
