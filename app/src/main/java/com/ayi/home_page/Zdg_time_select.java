package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.adapter.select_view_adapter;
import com.ayi.entity.time_select_item;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Data_time_cuo;
import com.ayi.utils.Show_toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/10/14.
 */
public class Zdg_time_select extends Activity {

    List<View> list_view;
    private View btn_ok;
    public static String yuyue_time_string = "";
    public static String time_start_unix = "";
    public static String time_finish_unix = "";
    private View progressBar1;
    private TextView title_text;
    //标号，一个记住星期，和 日期
    int lie1 = 0;
    int lie2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zdg_time_select);
        init();
        init_ok();
        init_wangluo();
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

    private void init_wangluo() {
        progressBar1.setVisibility(View.VISIBLE);
        /*
                先网络请求时间，在弹出对话框
                 */
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url = RetrofitUtil.url_time_select;
        RequestParams requestParams = new RequestParams();
        requestParams.put("typeid", getIntent().getStringExtra("typeid"));
        requestParams.put("areaid", AyiApplication.area_id);
        requestParams.put("time_dur", getIntent().getStringExtra("time_dur"));
        requestParams.put("lat", getIntent().getStringExtra("latitude"));
        requestParams.put("lng", getIntent().getStringExtra("longitude"));
        KLog.e("详细", getIntent().getStringExtra("typeid") + "," + AyiApplication.area_id + "," + getIntent().getStringExtra("time_dur") + getIntent().getStringExtra("latitude")
                + getIntent().getStringExtra("longitude"));
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    progressBar1.setVisibility(View.GONE);
                    KLog.e(jsonObject.toString());
                    List<List<time_select_item>> list_time_select = new ArrayList<>();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        //每一周
                        JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                        List<time_select_item> list_item = new ArrayList<time_select_item>();
                        for (int j = 0; j < jsonArray1.length(); j++) {
                            //20个细节
                            JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                            time_select_item item = new time_select_item();
                            //新年
                            try {
                                Long l = Long.parseLong(jsonObject1.getString("start_unix_time"));
                                if (l >= AyiApplication.time1 && l < AyiApplication.time2) {
                                    item.setFull("1");
                                } else {
                                    item.setFull(jsonObject1.getString("full"));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            item.setStart_unix_time(jsonObject1.getString("start_unix_time"));
                            item.setFinish_unix_time(jsonObject1.getString("finish_unix_time"));
                            item.setWeekday(jsonObject1.getString("weekday"));
                            item.setTime(jsonObject1.getString("time"));
                            list_item.add(item);
                        }
                        list_time_select.add(list_item);
                    }
                    init_add(list_time_select);
                } catch (Exception e) {
                    Toast.makeText(Zdg_time_select.this, "选择时间过长", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });

    }

    private select_view_adapter adapter;
    private List<time_select_item> data;

    private void init_add(final List<List<time_select_item>> list_time_select) {
        LinearLayout week_line = (LinearLayout) findViewById(R.id.week_line);
        //修订-----1.0
        title_text.setText(Data_time_cuo.gettime2(list_time_select.get(getIntent().getIntExtra("lie1", 0)).get(0)
                .getStart_unix_time()) + "(" + formatweek(list_time_select.get(getIntent().getIntExtra("lie1", 0)).get(0).getWeekday()) + ")");
        data = list_time_select.get(getIntent().getIntExtra("lie1", 0));
        //7周的数据
        for (int i = 0; i < list_time_select.size(); i++) {
            View view1 = LayoutInflater.from(Zdg_time_select.this).inflate(R.layout.item_week, null);
            TextView name = (TextView) view1.findViewById(R.id.week_name);
            TextView date = (TextView) view1.findViewById(R.id.week_date);
            if (i == 0) {
                name.setText("今天");
            } else if (i == 1) {
                name.setText("明天");
            } else if (i == 2) {
                name.setText("后天");
            } else {
                name.setText(formatweek(list_time_select.get(i).get(0).getWeekday()));
            }

            date.setText(Data_time_cuo.gettime5(list_time_select.get(i).get(0).getStart_unix_time()));
            view1.setClickable(true);
            view1.setLongClickable(false);
            if (getIntent().getIntExtra("lie1", 0) == i) {
                view1.setBackgroundResource(R.mipmap.time_select_ed_back);
                System.out.println("-确定日期--" + i);
            }
            list_view.add(view1);
            week_line.addView(view1);
//            setwandh(view1);
        }
        //左边
        for (int i = 0; i < list_view.size(); i++) {
            final int a = i;
            list_view.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Zdg_time_select.yuyue_time_string = "";
                    setnnull(list_view);
                    title_text.setText(Data_time_cuo.gettime2(list_time_select.get(a).get(0).getStart_unix_time()) + "(" + formatweek(list_time_select.get(a).get(0).getWeekday()) + ")");
                    v.setBackgroundResource(R.mipmap.time_select_ed_back);
                    lie1 = a;
                    data = list_time_select.get(a);
                    adapter = new select_view_adapter(data, Zdg_time_select.this);
                    week_grid.setAdapter(adapter);
                    week_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            adapter.clearselect(position);
                            adapter.notifyDataSetChanged();
                            lie2 = position;
                        }
                    });
                }
            });
        }
        adapter = new select_view_adapter(data, this);
        if (getIntent().getIntExtra("lie2", -1) != -1) {
            adapter.clearselect(getIntent().getIntExtra("lie2", -1));
            adapter.notifyDataSetChanged();
        }

        week_grid.setAdapter(adapter);
        week_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.clearselect(position);
                adapter.notifyDataSetChanged();
                lie2 = position;

            }
        });

    }


    private void init_ok() {
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                b.setEnabled(false);
                if (yuyue_time_string.equals("")) {
                    Show_toast.showText(Zdg_time_select.this, "请选择时间");
                    b.setEnabled(true);
                    return;
                } else {
                    Intent intent = new Intent(Zdg_time_select.this, Home_zdg_ok.class);
                    System.out.println("lie" + lie1 + "," + lie2);
                    intent.putExtra("lie1", lie1);
                    intent.putExtra("lie2", lie2);
                    intent.putExtra("time_start_unix", time_start_unix);
                    intent.putExtra("time_finish_unix", time_finish_unix);
                    intent.putExtra("yuyue_time_string", yuyue_time_string);
                    KLog.d("time_select", time_start_unix + "," + time_finish_unix + "," + yuyue_time_string);
                    startActivity(intent);
                }
                b.setEnabled(true);

            }
        });
    }

    private String formatweek(String weekday) {
        String str = "";
        switch (weekday) {
            case "0":
                str = "星期日";
                break;
            case "1":
                str = "星期一";
                break;
            case "2":
                str = "星期二";
                break;
            case "3":
                str = "星期三";
                break;
            case "4":
                str = "星期四";
                break;
            case "5":
                str = "星期五";
                break;
            case "6":
                str = "星期六";
                break;

        }
        return str;
    }

    private GridView week_grid;

    private void init() {
        title_text = (TextView) findViewById(R.id.title_text);
        list_view = new ArrayList<View>();
        week_grid = (GridView) findViewById(R.id.week_grid);
        progressBar1 = findViewById(R.id.progressBar1);
        if (getIntent().getIntExtra("lie1", -1) != -1) {
            lie1 = getIntent().getIntExtra("lie1", -1);
            lie2 = getIntent().getIntExtra("lie2", -1);
        }

        yuyue_time_string = "";
        time_start_unix = "";
        time_finish_unix = "";
        btn_ok = findViewById(R.id.ok);

    }

    public void setnnull(List<View> list_view) {
        for (int i = 0; i < list_view.size(); i++) {
            list_view.get(i).setBackgroundResource(R.drawable.all_week_pink);
        }

    }

}
