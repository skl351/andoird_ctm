package com.ayi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.adapter.orderRefresh_duigongAdapter;
import com.ayi.adapter.orderRefresh_tcAdapter;
import com.ayi.entity.item_order_dg;
import com.ayi.entity.item_order_tc;
import com.ayi.home_page.Business_appointment;
import com.ayi.home_page.Business_appointment_tc;
import com.ayi.retrofit.RetrofitUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.milk.base.BaseFragment;
import com.milk.utils.Data_time_cuo;
import com.milk.widget.RecycleViewDivider;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by oceanzhang on 16/2/17.
 */
public abstract class BaseViewPagerFragment2_now extends BaseFragment {
    public static String flag_tc_dg = "1";//1-订单 2-套餐 3-对公业务

    protected SwipeRefreshLayout demo_swiperefreshlayout;
    protected RecyclerView demo_recycler;
    protected orderRefresh_duigongAdapter adapter_duigong;
    protected orderRefresh_tcAdapter adapter_tc;
    protected LinearLayoutManager linearLayoutManager;
    protected int lastVisibleItem;
    protected List<item_order_dg> list = new ArrayList<>();
    protected List<item_order_tc> list_tc = new ArrayList<>();
    protected int flaf = 1;
    protected TabLayout tabLayout;
    protected ViewPager viewPager;
    protected ViewPagerAdapter adapter;
    protected View select_click;
    protected View main_tabs;
    protected View pager;
    protected TextView title;
    private View progressBar1;
    public View kongbai;


    @Override
    protected int getLayoutId() {
        return com.milk.base.R.layout.base_viewpage;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        adapter = new ViewPagerAdapter(getChildFragmentManager());
//        setupViewPager(adapter);
    }

    private void init_view() {
        flaf = 1;
        demo_swiperefreshlayout = (SwipeRefreshLayout) view.findViewById(com.milk.base.R.id.demo_swiperefreshlayout);
        demo_recycler = (RecyclerView) view.findViewById(com.milk.base.R.id.demo_recycler);
        demo_recycler.addItemDecoration(new RecycleViewDivider(
                getActivity(), LinearLayoutManager.VERTICAL, 10, getResources().getColor(R.color.white)));
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

        //添加分隔线


        //外部用来刷新，不会动用内部
        demo_swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                KLog.d("zttjiangqq", "invoke onRefresh...");
                flaf = 1;
                if (flag_tc_dg.equals("3")) {
                    Do_network(1, 10, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), AyiApplication.getInstance().area_id, flag_tc_dg);//
                }
                if (flag_tc_dg.equals("2")) {
                    Do_network_tc(1, 10, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), AyiApplication.getInstance().area_id, flag_tc_dg);//
                }
            }
        });
        //RecyclerView滑动监听
        demo_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                try {
                    if (flag_tc_dg.equals("3")) {
                        KLog.e("加载更多2" + lastVisibleItem + "," + adapter_duigong.getItemCount());
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1) == adapter_duigong.getItemCount()) {
                            adapter_duigong.changeMoreStatus(orderRefresh_duigongAdapter.LOADING_MORE);
                            KLog.e("加载更多");
                            flaf++;
                            Do_network2(flaf, 10, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), AyiApplication.getInstance().area_id);
                        }
                    }
                    if (flag_tc_dg.equals("2")) {
                        KLog.e("加载更多2" + lastVisibleItem + "," + adapter_tc.getItemCount());
                        if (newState == RecyclerView.SCROLL_STATE_IDLE && (lastVisibleItem + 1) == adapter_tc.getItemCount()) {
                            adapter_tc.changeMoreStatus(orderRefresh_tcAdapter.LOADING_MORE);
                            KLog.e("加载更多");
                            flaf++;
                            Do_network2_tc(flaf, 10, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), AyiApplication.getInstance().area_id);
                        }
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
        if (AyiApplication.flag_tc_dg.equals("2")) {
            title.setText("套餐订单");
            flag_tc_dg = "2";
            pager.setVisibility(View.GONE);
            main_tabs.setVisibility(View.GONE);
            viewPager.setAdapter(null);
            demo_swiperefreshlayout.setVisibility(View.VISIBLE);
            Do_network_tc(1, 10, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), AyiApplication.area_id, flag_tc_dg);
        }

        if (AyiApplication.flag_tc_dg.equals("3")) {
            title.setText("企业清洁");
            flag_tc_dg = "3";
            pager.setVisibility(View.GONE);
            main_tabs.setVisibility(View.GONE);
            viewPager.setAdapter(null);
            demo_swiperefreshlayout.setVisibility(View.VISIBLE);
            Do_network(1, 10, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), AyiApplication.area_id, flag_tc_dg);

        }

    }

    protected abstract void seleclt_fun();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void initView(View view) {
        progressBar1 = view.findViewById(R.id.progressBar1);
        title = (TextView) view.findViewById(R.id.title);
        kongbai = view.findViewById(R.id.kongbai);
        main_tabs = view.findViewById(com.milk.base.R.id.main_tabs);
        pager = view.findViewById(com.milk.base.R.id.pager);
        select_click = view.findViewById(com.milk.base.R.id.select_click);
        select_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seleclt_fun();
            }
        });
        tabLayout = (TabLayout) view.findViewById(com.milk.base.R.id.main_tabs);
        viewPager = (ViewPager) view.findViewById(com.milk.base.R.id.pager);
        if (AyiApplication.flag_tc_dg.equals("1")) {
            kongbai.setVisibility(View.GONE);
            flag_tc_dg = "1";
            title.setText("普通订单");
            adapter = new ViewPagerAdapter(getChildFragmentManager());
            setupViewPager(adapter);
            viewPager.setAdapter(adapter);
            tabLayout.setupWithViewPager(viewPager);
        }
        init_view();
    }

    protected abstract void setupViewPager(ViewPagerAdapter adapter);

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    protected void Do_network(int flaf, int j, String userid, String token, String areaid, String flag_tc) {
        progressBar1.setVisibility(View.VISIBLE);
        AyiApplication.flag_xuyaoshuax = "false";
        flaf = 1;
        flag_tc_dg = flag_tc;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_duigong_list;//测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", userid);
        requestParams.put("token", token);
        requestParams.put("areaid", areaid);
        requestParams.put("group_id", "3");
        requestParams.put("currentpage", flaf);
        requestParams.put("pagesize", j);
        System.out.println("requestParams:" + requestParams);
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
                            KLog.e("运行了得到数据");
                            item_order_dg item = new item_order_dg();
                            item.setOrdernum(jsonArray.getJSONObject(i).getString("ordernum"));
                            item.setContact(jsonArray.getJSONObject(i).getString("content"));
                            item.setContact_phone(jsonArray.getJSONObject(i).getString("contact_phone"));
                            item.setContact_addr(jsonArray.getJSONObject(i).getString("contact_addr"));
                            item.setContact_door(jsonArray.getJSONObject(i).getString("contact_door"));
                            item.setTime(Data_time_cuo.gettime(jsonArray.getJSONObject(i).getString("timestamp")));
                            item.setId(jsonArray.getJSONObject(i).getString("id"));
                            item.setStatus(jsonArray.getJSONObject(i).getString("status"));
                            list.add(item);
                        }
                        if (list.size() > 0) {
                            kongbai.setVisibility(View.GONE);
                        } else {
                            kongbai.setVisibility(View.VISIBLE);
                        }
                        adapter_duigong = new orderRefresh_duigongAdapter(getActivity(), list);
                        adapter_duigong.setOnItemClickListener(yItemClickListener2);


                        demo_recycler.setAdapter(adapter_duigong);
                        demo_swiperefreshlayout.setRefreshing(false);
//                        Toast.makeText(getActivity(), "更新了对应数据...", Toast.LENGTH_SHORT).show();
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

    protected void Do_network2(int flaf, int j, String userid, String token, String areaid) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_duigong_list;
        //测试数据--得到的数据
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", userid);
        requestParams.put("token", token);
        requestParams.put("areaid", areaid);
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
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("financialList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            KLog.e("运行了得到数据");
                            item_order_dg item = new item_order_dg();
                            item.setOrdernum(jsonArray.getJSONObject(i).getString("ordernum"));
                            item.setContact(jsonArray.getJSONObject(i).getString("content"));
                            item.setContact_phone(jsonArray.getJSONObject(i).getString("contact_phone"));
                            item.setContact_addr(jsonArray.getJSONObject(i).getString("contact_addr"));
                            item.setContact_door(jsonArray.getJSONObject(i).getString("contact_door"));
                            item.setTime(Data_time_cuo.gettime(jsonArray.getJSONObject(i).getString("timestamp")));
                            item.setStatus(jsonArray.getJSONObject(i).getString("status"));
                            item.setId(jsonArray.getJSONObject(i).getString("id"));
                            list.add(item);
                        }
                        if (list.size() > 0) {
                            kongbai.setVisibility(View.GONE);
                        } else {
                            kongbai.setVisibility(View.VISIBLE);
                        }
                        //适配器添加在内部的view
                        if (adapter_duigong == null) {
                            adapter_duigong = new orderRefresh_duigongAdapter(getActivity(), list);
                            adapter_duigong.setOnItemClickListener(yItemClickListener2);
                            demo_recycler.setAdapter(adapter_duigong);
                        } else {
                            adapter_duigong.notifyDataSetChanged();
                        }
                        demo_swiperefreshlayout.setRefreshing(false);
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

    protected void Do_network_tc(int flaf, int j, String userid, String token, String areaid, String flag_tc) {
        progressBar1.setVisibility(View.VISIBLE);
        AyiApplication.flag_xuyaoshuax = "false";
        System.out.println("AyiApplication.flag_xuyaoshuax" + AyiApplication.flag_xuyaoshuax);
        flaf = 1;
        flag_tc_dg = flag_tc;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_tc_order_list;//得到套餐订单列表
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", userid);
        requestParams.put("token", token);
        requestParams.put("areaid", areaid);
        requestParams.put("group_id", "3");
        requestParams.put("currentpage", flaf);
        requestParams.put("pagesize", j);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                progressBar1.setVisibility(View.GONE);
                try {
                    System.out.println("OrderServicePackage.getList" + jsonObject.toString());
                    if ("200".equals(jsonObject.getString("ret"))) {
                        list_tc.clear();
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("financialList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            KLog.e("运行了得到数据");
                            item_order_tc item = new item_order_tc();
                            item.setPrice(jsonArray.getJSONObject(i).getString("price"));
                            item.setOrdernum(jsonArray.getJSONObject(i).getString("ordernum"));
                            item.setContact(jsonArray.getJSONObject(i).getString("contacts"));
                            item.setContact_phone(jsonArray.getJSONObject(i).getString("contact_phone"));
                            item.setContact_addr(jsonArray.getJSONObject(i).getString("contact_addr"));
                            item.setContact_door(jsonArray.getJSONObject(i).getString("contact_door"));
                            item.setTime(Data_time_cuo.gettime(jsonArray.getJSONObject(i).getString("timestamp")));
                            item.setStatus(jsonArray.getJSONObject(i).getString("status"));
                            item.setPayed(jsonArray.getJSONObject(i).getString("payed"));
                            item.setTitle(jsonArray.getJSONObject(i).getString("ccsp_title"));
                            item.setId(jsonArray.getJSONObject(i).getString("id"));
                            item.setIsvalet(jsonArray.getJSONObject(i).getInt("isvalet"));
                            list_tc.add(item);
                        }
                        if (list_tc.size() > 0) {
                            kongbai.setVisibility(View.GONE);
                        } else {
                            kongbai.setVisibility(View.VISIBLE);
                        }
                        adapter_tc = new orderRefresh_tcAdapter(getActivity(), list_tc);
                        adapter_tc.setOnItemClickListener(yItemClickListener);
                        demo_recycler.setAdapter(adapter_tc);
                        demo_swiperefreshlayout.setRefreshing(false);
//                        Toast.makeText(getActivity(), "更新了对应数据...", Toast.LENGTH_SHORT).show();
//
                    }
                } catch (Exception e) {
                    progressBar1.setVisibility(View.GONE);
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
    public void onResume() {
        super.onResume();
        System.out.println("AyiApplication.flag_xuyaoshuax" + AyiApplication.flag_xuyaoshuax);
        if (AyiApplication.flag_xuyaoshuax.equals("true")) {
            if (AyiApplication.flag_tc_dg.equals("3")) {
                Do_network(1, 10, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), AyiApplication.area_id, flag_tc_dg);
            }

            if (AyiApplication.flag_tc_dg.equals("2")) {
                Do_network_tc(1, 10, AyiApplication.getInstance().accountService().id(), AyiApplication.getInstance().accountService().token(), AyiApplication.area_id, flag_tc_dg);
            }
        }
    }


    protected void Do_network2_tc(int flaf, int j, String userid, String token, String areaid) {
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_tc_order_list;//得到套餐订单列表
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", userid);
        requestParams.put("token", token);
        requestParams.put("areaid", areaid);
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
                        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("financialList");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            KLog.e("运行了得到数据");
                            item_order_tc item = new item_order_tc();
                            item.setPrice(jsonArray.getJSONObject(i).getString("price"));
                            item.setContact(jsonArray.getJSONObject(i).getString("contacts"));
                            item.setOrdernum(jsonArray.getJSONObject(i).getString("ordernum"));
                            item.setContact_phone(jsonArray.getJSONObject(i).getString("contact_phone"));
                            item.setContact_addr(jsonArray.getJSONObject(i).getString("contact_addr"));
                            item.setContact_door(jsonArray.getJSONObject(i).getString("contact_door"));
                            item.setTime(Data_time_cuo.gettime(jsonArray.getJSONObject(i).getString("timestamp")));
                            item.setStatus(jsonArray.getJSONObject(i).getString("status"));
                            item.setPayed(jsonArray.getJSONObject(i).getString("payed"));
                            item.setTitle(jsonArray.getJSONObject(i).getString("ccsp_title"));
                            item.setId(jsonArray.getJSONObject(i).getString("id"));
                            item.setIsvalet(jsonArray.getJSONObject(i).getInt("isvalet"));
                            list_tc.add(item);
                        }
                        if (list_tc.size() > 0) {
                            kongbai.setVisibility(View.GONE);
                        } else {
                            kongbai.setVisibility(View.VISIBLE);
                        }
                        //适配器添加在内部的view
                        if (adapter_tc == null) {
                            adapter_tc = new orderRefresh_tcAdapter(getActivity(), list_tc);
                            adapter_tc.setOnItemClickListener(yItemClickListener);
                            demo_recycler.setAdapter(adapter_tc);
                        } else {
                            adapter_tc.notifyDataSetChanged();
                        }
                        demo_swiperefreshlayout.setRefreshing(false);
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

    orderRefresh_tcAdapter.MyItemClickListener yItemClickListener = new orderRefresh_tcAdapter.MyItemClickListener() {

        @Override
        public void onItemClick(View view, int postion) {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.setTimeout(20000);
            String url = RetrofitUtil.url_tc_dowmload;//得到套餐订单列表
            RequestParams requestParams = new RequestParams();
            requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
            requestParams.put("token", AyiApplication.getInstance().accountService().token());
            requestParams.put("id", list_tc.get(postion).getId());
            requestParams.put("ordernum", list_tc.get(postion).getOrdernum());
            asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                    super.onSuccess(statusCode, headers, jsonObject);
                    try {
                        System.out.println("OrderServicePackage.getList" + jsonObject.toString());
                        if ("200".equals(jsonObject.getString("ret"))) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            Intent intent = new Intent(getActivity(), Business_appointment_tc.class);
                            intent.putExtra("isimg", jsonObject1.getString("ccsp_isimg"));
                            intent.putExtra("image", jsonObject1.getString("ccsp_content"));
                            intent.putExtra("text", jsonObject1.getString("ccsp_content"));
                            intent.putExtra("title", jsonObject1.getString("ccsp_title"));
//                                intent.putExtra("title","订单详情");
                            intent.putExtra("price", jsonObject1.getString("price"));
                            intent.putExtra("payment", jsonObject1.getString("payment"));
                            intent.putExtra("ccsp_id", jsonObject1.getString("id"));
                            intent.putExtra("remark", jsonObject1.getString("remark"));
                            intent.putExtra("ordernum", jsonObject1.getString("ordernum"));
                            intent.putExtra("status", jsonObject1.getString("status"));
                            intent.putExtra("payed", jsonObject1.getString("payed"));
                            intent.putExtra("contacts", jsonObject1.getString("contacts"));
                            intent.putExtra("contact_phone", jsonObject1.getString("contact_phone"));
                            intent.putExtra("contact_addr", jsonObject1.getString("contact_addr"));
                            intent.putExtra("contact_door", jsonObject1.getString("contact_door"));
                            intent.putExtra("type_id", jsonObject1.getString("service_type_id"));
                            intent.putExtra("isvalet", jsonObject1.getInt("isvalet"));
                            KLog.e("标1志" + intent.toString());
                            startActivity(intent);
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
    };

    orderRefresh_duigongAdapter.MyItemClickListener yItemClickListener2 = new orderRefresh_duigongAdapter.MyItemClickListener() {

        @Override
        public void onItemClick(View view, int postion) {
            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
            asyncHttpClient.setTimeout(20000);
            String url = RetrofitUtil.url_duig_order_delere;//得到套餐订单列表
            RequestParams requestParams = new RequestParams();
            requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
            requestParams.put("token", AyiApplication.getInstance().accountService().token());
            requestParams.put("id", list.get(postion).getId());

            asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                    super.onSuccess(statusCode, headers, jsonObject);
                    try {
                        System.out.println("OrderServicePackage.getList" + jsonObject.toString());
                        if ("200".equals(jsonObject.getString("ret"))) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            Intent intent = new Intent(getActivity(), Business_appointment.class);
                            intent.putExtra("isimg", jsonObject1.getString("dg_isimg"));
                            intent.putExtra("image", jsonObject1.getString("dg_content"));
                            intent.putExtra("text", jsonObject1.getString("dg_content"));
//                            intent.putExtra("title",jsonObject1.getString("dg_title"));
                            intent.putExtra("title", "订单详情");
                            intent.putExtra("ccsp_id", jsonObject1.getString("id"));
                            intent.putExtra("remark", jsonObject1.getString("content"));
                            intent.putExtra("status", jsonObject1.getString("status"));
                            intent.putExtra("contacts", jsonObject1.getString("contacts"));
                            intent.putExtra("contact_phone", jsonObject1.getString("contact_phone"));
                            intent.putExtra("contact_addr", jsonObject1.getString("contact_addr"));
                            intent.putExtra("contact_door", jsonObject1.getString("contact_door"));
                            startActivity(intent);
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
    };

}
