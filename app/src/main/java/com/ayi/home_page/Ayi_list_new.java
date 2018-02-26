package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.adapter.Ayi_list_adapter;
import com.ayi.entity.item_ayi_list;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Show_toast;
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
 * Created by Administrator on 2016/9/2.
 */
public class Ayi_list_new extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    public static int index_flag = -1;
    private View top;
    private View back;
    ListView gridView;
    List<item_ayi_list> list;
    String type_num = "";
    Ayi_list_adapter adapter;

    private void init_back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private View right_submit;
    private String time_start;
    private String time_finish;
    private String longitude;
    private String latitude;
    private SwipeRefreshLayout mSwipeLayout;
    private Button button_btn;
    private View progressBar1;
    private View find_ayi;
    private TextView edit_ayi_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ayi_list_view_new);
        init_in();
        find_ayi_click();
        init_right_submit_click();
        init_wangluo_init();
        init_back();
        init_huanyipi();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler_dingshi.removeMessages(0);
    }

    int time_flag = 5;
    private Handler handler_dingshi = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    limian_text.setText("换一批(" + time_flag + ")");
                    limian_text.setTextColor(getResources().getColor(R.color.touming_color3));
                    right_submit.setEnabled(false);
                    time_flag--;
                    if (time_flag == -1) {
                        time_flag = 5;
                        limian_text.setText("换一批");
                        limian_text.setTextColor(getResources().getColor(R.color.main_green));
                        right_submit.setEnabled(true);
                        break;
                    }
                    handler_dingshi.sendEmptyMessageDelayed(0, 1000);
                    break;

                case 1:
                    // 直接移除，定时器停止
                    break;

                default:
                    break;
            }
        }

        ;
    };

    private void find_ayi_click() {
        find_ayi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                init_wangluo_init_sou();
            }
        });
    }

    private final static int REQUEST_CODE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                String str = bundle.getString("result");
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId().equals(str)) {
                        index_flag = i;
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }
            }
        }
    }

    private void init_huanyipi() {
        limian_text.setText("换一批");

    }

    private TextView limian_text;

    private void init_in() {
        edit_ayi_name = (TextView) findViewById(R.id.edit_ayi_name);
        find_ayi = findViewById(R.id.find_ayi);
        progressBar1 = findViewById(R.id.progressBar1);
        index_flag = -1;
        top = findViewById(R.id.top);
        back = top.findViewById(R.id.logreg_left);
        right_submit = top.findViewById(R.id.logreg_right);
        limian_text = (TextView) right_submit.findViewById(R.id.limian_text);
        type_num = getIntent().getStringExtra("type_num");
        time_start = getIntent().getStringExtra("time_start");
        time_finish = getIntent().getStringExtra("time_finish");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        gridView = (ListView) findViewById(R.id.listview);
        list = new ArrayList<item_ayi_list>();
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_ly);
        //设置监听
        mSwipeLayout.setOnRefreshListener(this);
        button_btn = (Button) findViewById(R.id.button_btn);
    }

    private void init_right_submit_click() {
        button_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index_flag != -1) {
                    if (getIntent().getStringExtra("flag").equals("zdg")) {
                        Intent intent = new Intent(Ayi_list_new.this, Home_zdg_ok.class);
                        intent.putExtra("ayi_info", list.get(index_flag));
                        startActivity(intent);
                        Ayi_list_new.this.finish();
                        System.out.println("返回钟点工");
                    }
                    if (getIntent().getStringExtra("flag").equals("zdg_cq")) {
                        Intent intent = new Intent(Ayi_list_new.this, Home_zdg_cq_ok.class);
                        intent.putExtra("ayi_info", list.get(index_flag));
                        startActivity(intent);
                        Ayi_list_new.this.finish();
                        System.out.println("返回钟点工长期");
                    }
                    if (getIntent().getStringExtra("flag").equals("bm")) {
                        Intent intent = new Intent(Ayi_list_new.this, Home_baomu_ok.class);
                        intent.putExtra("ayi_info", list.get(index_flag));
                        startActivity(intent);
                        Ayi_list_new.this.finish();
                        System.out.println("返回保姆");
                    }
                    if (getIntent().getStringExtra("flag").equals("yuesao")) {
                        Intent intent = new Intent(Ayi_list_new.this, Home_yuesao_ok.class);
                        intent.putExtra("ayi_info", list.get(index_flag));
                        startActivity(intent);
                        Ayi_list_new.this.finish();
                        System.out.println("返回月嫂");
                    }
                } else {
                    Toast.makeText(Ayi_list_new.this, "请选择阿姨", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    int flag = 1;
    long time_cuo = 0;

    /**
     * 这个对订单列表的未完成订单表的请求接口
     */
    private void init_wangluo_init() {

        right_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler_dingshi.sendEmptyMessage(0);
                init_wangluo_init();
            }
        });
        progressBar1.setVisibility(View.VISIBLE);
        flag = 1;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_ayi_select;
        asyncHttpClient.setTimeout(20000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("typeid", type_num);
        requestParams.put("areaid", AyiApplication.area_id);
        requestParams.put("time_start", time_start);
        requestParams.put("time_finish", time_finish);
        requestParams.put("lat", latitude);
        requestParams.put("lng", longitude);
        KLog.e("参数：", type_num + "," + AyiApplication.area_id + "," + time_start + "," + time_finish + "," + latitude + "," + longitude);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonObject_data = response.getJSONArray("data");
                    System.out.println(response);
                    list.clear();
                    int size = 0;
                    if (jsonObject_data.length() <= 10) {
                        size = jsonObject_data.length();
                    } else {
                        size = 10;
                    }
                    for (int i = 0; i < size; i++) {
                        item_ayi_list item = new item_ayi_list();
                        item.setImg_head(jsonObject_data.getJSONObject(i).getString("headimg"));
                        item.setName(jsonObject_data.getJSONObject(i).getString("name"));
                        item.setTimes(jsonObject_data.getJSONObject(i).getString("times"));
                        item.setPlace(jsonObject_data.getJSONObject(i).getString("place"));
                        item.setId(jsonObject_data.getJSONObject(i).getString("id"));
                        item.setOld(jsonObject_data.getJSONObject(i).getString("old"));
                        if (jsonObject_data.getJSONObject(i).has("in_price")) {
                            item.setIn_price(jsonObject_data.getJSONObject(i).getString("in_price"));
                            item.setServicecharge(jsonObject_data.getJSONObject(i).getString("servercharge"));
                        } else {
                            item.setIn_price("");
                            item.setServicecharge("");
                        }
                        list.add(item);
                    }
                    adapter = new Ayi_list_adapter(Ayi_list_new.this, list);
                    gridView.setAdapter(adapter);
//                    suijishu();
//                    Show_toast.showText(Ayi_list_new.this,"这是部分阿姨，下拉刷新随机获得阿姨列表");
                    progressBar1.setVisibility(View.GONE);

                } catch (JSONException e) {
                    progressBar1.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressBar1.setVisibility(View.GONE);
            }
        });

    }

    private void init_wangluo_init_sou() {
        right_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler_dingshi.sendEmptyMessage(0);
                init_wangluo_init_sou();

            }
        });
        progressBar1.setVisibility(View.VISIBLE);
        flag = 1;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_ayi_select;
        asyncHttpClient.setTimeout(20000);
        RequestParams requestParams = new RequestParams();
        requestParams.put("typeid", type_num);
        requestParams.put("areaid", AyiApplication.area_id);
        requestParams.put("time_start", time_start);
        requestParams.put("time_finish", time_finish);
        requestParams.put("lat", latitude);
        requestParams.put("lng", longitude);
        requestParams.put("cleanername", edit_ayi_name.getText().toString());

        KLog.e("参数：", type_num + "," + AyiApplication.area_id + "," + time_start + "," + time_finish + "," + latitude + "," + longitude + "," + edit_ayi_name.getText().toString());
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonObject_data = response.getJSONArray("data");
                    System.out.println(response);
                    list.clear();
                    int size = 0;
                    if (jsonObject_data.length() <= 10) {
                        size = jsonObject_data.length();
                    } else {
                        size = 10;
                    }
                    for (int i = 0; i < size; i++) {
                        item_ayi_list item = new item_ayi_list();
                        item.setImg_head(jsonObject_data.getJSONObject(i).getString("headimg"));
                        item.setName(jsonObject_data.getJSONObject(i).getString("name"));
                        item.setTimes(jsonObject_data.getJSONObject(i).getString("times"));
                        item.setPlace(jsonObject_data.getJSONObject(i).getString("place"));
                        item.setId(jsonObject_data.getJSONObject(i).getString("id"));
                        item.setOld(jsonObject_data.getJSONObject(i).getString("old"));
                        if (jsonObject_data.getJSONObject(i).has("in_price")) {
                            item.setIn_price(jsonObject_data.getJSONObject(i).getString("in_price"));
                            item.setServicecharge(jsonObject_data.getJSONObject(i).getString("servercharge"));
                        } else {
                            item.setIn_price("");
                            item.setServicecharge("");
                        }
                        list.add(item);
                    }

                    suijishu2();
//                    Show_toast.showText(Ayi_list_new.this,"这是部分阿姨，下拉刷新随机获得阿姨列表");


                } catch (JSONException e) {
                    progressBar1.setVisibility(View.GONE);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressBar1.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onRefresh() {
        mHandler.sendEmptyMessageDelayed(REFRESH_COMPLETE, 1000);

    }

    private static final int REFRESH_COMPLETE = 0X110;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case REFRESH_COMPLETE:

                    init_wangluo_init();
                    mSwipeLayout.setRefreshing(false);
                    break;

            }
        }

        ;
    };


    public void suijishu2() {
        if (list.size() <= 0) {
            adapter.notifyDataSetChanged();
            progressBar1.setVisibility(View.GONE);
            Show_toast.showText(Ayi_list_new.this, "无搜索阿姨");
            index_flag = -1;
            return;
        }

        adapter = new Ayi_list_adapter(Ayi_list_new.this, list);
        gridView.setAdapter(adapter);
        index_flag = -1;
        progressBar1.setVisibility(View.GONE);

    }
}
