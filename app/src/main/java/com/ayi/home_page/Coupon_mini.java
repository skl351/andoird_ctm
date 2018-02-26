package com.ayi.home_page;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

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
import simplezxing.activity.CaptureActivity;

/**
 * Created by Administrator on 2016/9/5.
 */
public class Coupon_mini extends Activity {
    private View top;
    private View back;
    GridView gridView;
    PullToRefreshLayout.OnRefreshListener re;
    List<item_coupon> list;
    Coupon_mimi_adapter adapter;
    private String type_num;
    private View click_lishi;
    private View yhq_duihuan;
    private EditText yhq_str;
    private View empey_view;
    private View saomiaoerweim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_view_mini);
        init();
        init_back();
        init_wangluo_init();
        init_click_lishi();//点击历史
        init_yhq_duihuan();
        init_erweima();//二维码
    }

    /**
     * 扫描结果处理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        String str = data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT);
                        if (str.substring(0, str.length() - 6).equals("http://wx.sangeayi.com/coupon/index.html?cgcode=")) {
                            yhq_str.setText(str.substring(str.length() - 6, str.length()));
                        } else {
                            Toast.makeText(Coupon_mini.this, "二维码不正确", Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            String str1 = data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT);
                            if (str1.substring(0, str1.length() - 6).equals("http://wx.sangeayi.com/coupon/index.html?cgcode=")) {
                                yhq_str.setText(str1.substring(str1.length() - 6, str1.length()));
                            } else {
                                Toast.makeText(Coupon_mini.this, "二维码不正确", Toast.LENGTH_SHORT).show();
                            }
                        }
                        break;
                }
                break;
        }

    }

    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;

    private void init_erweima() {
        saomiaoerweim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.e("error", "进来这里1");
                if (Build.VERSION.SDK_INT >= 23) {
                    KLog.e("error", "进来这里2");
                    int checkCallPhonePermission = ContextCompat.checkSelfPermission(Coupon_mini.this, Manifest.permission.CAMERA);
                    if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                        KLog.e("error", "进来这里3");
                        ActivityCompat.requestPermissions(Coupon_mini.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CALL_PHONE);
                        return;
                    } else {
                        KLog.e("error", "进来这里4");
                        //上面已经写好的拨号方法
                        creame();
                    }
                } else {
                    KLog.e("error", "进来这里5");
                    //上面已经写好的拨号方法
                    creame();
                }

            }
        });
    }

    private void creame() {
        Intent intent = new Intent(Coupon_mini.this,
                CaptureActivity.class);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    KLog.e("error", "进来这里6");
                    // Permission Granted
                    creame();
                } else {
                    KLog.e("error", "进来这里7");
                    // Permission Denied
                    Toast.makeText(Coupon_mini.this, "相机功能被禁止", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void init_yhq_duihuan() {
        yhq_duihuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(Coupon_mini.this);
                final AlertDialog alert = builder.create();
                View view = LayoutInflater.from(Coupon_mini.this).inflate(R.layout.queding_duihuan, null);
                view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (yhq_str.getText().toString().equals("")) {
                            Toast.makeText(Coupon_mini.this, "输入为空", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                        String url = RetrofitUtil.url_duihuanyhq;
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                        requestParams.put("token", AyiApplication.getInstance().accountService().token());
                        requestParams.put("cgcode", yhq_str.getText().toString());
                        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    KLog.json(response.toString());
                                    if (response.getString("ret").equals("200") && response.getJSONObject("data").getString("status").equals("1")) {
                                        Toast.makeText(Coupon_mini.this, "兑换成功", Toast.LENGTH_SHORT).show();
                                        init_wangluo_init();
                                    } else {
                                        Toast.makeText(Coupon_mini.this, response.getString("msg"), Toast.LENGTH_SHORT).show();

                                    }
                                    alert.dismiss();

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
                });
                view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alert.dismiss();
                    }
                });

                alert.setView(view, 0, 0, 0, -10);
                alert.show();
            }
        });
    }

    private void init_click_lishi() {
        click_lishi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Coupon_mini.this, Coupon_nouse.class);

                startActivity(intent);
            }
        });
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
        saomiaoerweim = findViewById(R.id.saomiaoerweim);
        empey_view = findViewById(R.id.empey_view);
        yhq_str = (EditText) findViewById(R.id.yhq_str);
        yhq_duihuan = findViewById(R.id.yhq_duihuan);
        click_lishi = findViewById(R.id.click_lishi);
        list = new ArrayList<>();
        top = findViewById(R.id.top);
        back = top.findViewById(R.id.logreg_left);
        gridView = (GridView) findViewById(R.id.content_view);
        if (getIntent().getStringExtra("flag").equals("1")) {
            type_num = getIntent().getStringExtra("type_num");
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    switch (list.get(position).getStatus()) {
                        case "0":
                            Toast.makeText(Coupon_mini.this, "已使用", Toast.LENGTH_SHORT).show();
                            break;
                        case "1":
                            Intent intent = new Intent(Coupon_mini.this, Order_pay.class);
                            intent.putExtra("coupon", list.get(position));
                            startActivity(intent);
                            Coupon_mini.this.finish();
                            break;
                        case "2":
                            Toast.makeText(Coupon_mini.this, "已过期", Toast.LENGTH_SHORT).show();
                            break;

                    }

                }
            });

        } else {
            type_num = "-1";
        }

        /*
         * 在布局中找到一个自定义了的控件，其实已经写好了，只要给他设置一个监听器实现两个功能。---3
		 */
        ((PullToRefreshLayout) findViewById(R.id.refresh_view))
                .setOnRefreshListener(getlin());
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
//                        pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    }
                }.sendEmptyMessageDelayed(0, 1000);
            }
        };

    }

    int flag = 1;

    private void init_wangluo_init() {
        flag = 1;
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_get_coupon;
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("currentpage", flag);
        requestParams.put("pagesize", "10");
        requestParams.put("type", "1");
        KLog.d("coupon_service_id", type_num);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    KLog.json(response.toString());
                    JSONArray jsonArray_data = response.getJSONObject("data").getJSONArray("list");
                    list.clear();
                    if (jsonArray_data.length() == 0) {
                        empey_view.setVisibility(View.VISIBLE);
                    } else {
                        empey_view.setVisibility(View.GONE);
                    }
                    for (int i = 0; i < jsonArray_data.length(); i++) {
                        item_coupon item = new item_coupon();
                        item.setId(jsonArray_data.getJSONObject(i).getString("id"));
                        item.setPlace(jsonArray_data.getJSONObject(i).getString("name"));
                        item.setPrice(jsonArray_data.getJSONObject(i).getString("price"));
                        item.setTime_finish(jsonArray_data.getJSONObject(i).getString("time_finish"));
                        item.setTime_start(jsonArray_data.getJSONObject(i).getString("time_start"));
                        String zuihou_time = jsonArray_data.getJSONObject(i).getString("time_finish");
                        item.setStatus(jsonArray_data.getJSONObject(i).getString("status"));
                        item.setTypenames(jsonArray_data.getJSONObject(i).getString("typenames"));
                        if (!Data_time_cuo.comp_time_cuo(zuihou_time, System.currentTimeMillis() / 1000)) {//过期
                            item.setStatus("2");
                        }
                        list.add(item);
                    }
                    adapter = new Coupon_mimi_adapter(Coupon_mini.this, list);
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
        String url = RetrofitUtil.url_get_coupon;
        RequestParams requestParams = new RequestParams();
        flag++;
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("currentpage", flag);
        requestParams.put("pagesize", "10");
        requestParams.put("type", "1");
        KLog.d("coupon_service_id", type_num);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    System.out.println(response);
                    JSONArray jsonArray_data = response.getJSONObject("data").getJSONArray("list");

                    for (int i = 0; i < jsonArray_data.length(); i++) {
                        item_coupon item = new item_coupon();
                        item.setId(jsonArray_data.getJSONObject(i).getString("id"));
                        item.setPlace(jsonArray_data.getJSONObject(i).getString("name"));
                        item.setPrice(jsonArray_data.getJSONObject(i).getString("price"));
                        item.setTime_finish(jsonArray_data.getJSONObject(i).getString("time_finish"));
                        item.setTime_start(jsonArray_data.getJSONObject(i).getString("time_start"));
                        String zuihou_time = jsonArray_data.getJSONObject(i).getString("time_finish");
                        item.setStatus(jsonArray_data.getJSONObject(i).getString("status"));
                        item.setTypenames(jsonArray_data.getJSONObject(i).getString("typenames"));
                        if (!Data_time_cuo.comp_time_cuo(zuihou_time, System.currentTimeMillis() / 1000)) {//过期
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
