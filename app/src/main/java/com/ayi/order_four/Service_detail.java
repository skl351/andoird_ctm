package com.ayi.order_four;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.entity.cancel_reason;
import com.ayi.entity.mlist_pay;
import com.ayi.home_page.Order_pay;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Show_toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 子订单列表
 * Created by Administrator on 2016/9/12.
 */
public class Service_detail extends Activity {
    private ArrayAdapter<String> adapter_spinner1;
    private Spinner spinner;
    AlertDialog alert;
    List<cancel_reason> list_cancel_reason;
    private String orderid;
    private LinearLayout service_info_all_view;
    private View logreg_left_nei;
    List<View> list_view=new ArrayList<>();
    private Button cancal_order;
    List<Boolean> list_xuanz=new ArrayList<>();//判断是否选择了
    private  List<String> list_child=new ArrayList<>();

    private View progressBar1;
    private View yes_but;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_derail_view);
        init();
        init_back();
        orderid=getIntent().getStringExtra("orderid");
        init_wangluo();
        init_quan_delete();

    }

    private void init_quan_delete() {
        cancal_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancal_order.setEnabled(false);
                progressBar1.setVisibility(View.VISIBLE);
                int z=0;
                for (int i=0;i<list_xuanz.size();i++){
                    if (list_xuanz.get(i)){
                        z=i;
                    }
                }
                for (int i=0;i<list_xuanz.size();i++){
                  final   int a=i;
                    if (list_xuanz.get(i)){
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    String url= RetrofitUtil.url_cancel_submit;
                    RequestParams requestParams=new RequestParams();
                    requestParams.put("orderid", orderid);
                    requestParams.put("childid", list_child.get(a));
                    requestParams.put("msg", "批量删除");
                    requestParams.put("user_id",  AyiApplication.getInstance().accountService().id());
                    requestParams.put("token",  AyiApplication.getInstance().accountService().token());

                        final int finalZ = z;
                        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                System.out.println("response"+response);
                                if (response.getString("ret").equals("200")&&response.getJSONObject("data").getString("status").equals("1")){
                                    if (a== finalZ){
                                        Show_toast.showText(Service_detail.this,"选中订单删除成功");
                                        cancal_order.setText("选中订单取消");
                                        cancal_order.setBackgroundResource(R.drawable.button_style_gr2);
                                        init_wangluo();
                                    }

//                                    AlertDialog.Builder builder;
//                                    builder = new AlertDialog.Builder(Service_detail.this);
//                                    alert = builder.create();
//                                    View view = LayoutInflater.from(Service_detail.this).inflate(R.layout.calcel_rejectokok, null);
//                                    TextView title= (TextView) view.findViewById(R.id.textname);
//                                    view.findViewById(R.id.cancel_btn).setVisibility(View.GONE);
//                                    view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            alert.dismiss();
//                                            init_wangluo();
//                                        }
//                                    });
//                                    title.setText(response.getJSONObject("data").getString("msg").toString());
//                                    alert.setView(view);
//                                    alert.show();
                                }else {
                                    Toast.makeText(Service_detail.this,"取消失败",Toast.LENGTH_SHORT).show();

                                   finish();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                finish();
                            }


                        }
                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            finish();
                            Toast.makeText(Service_detail.this,"网络繁忙，请重试",Toast.LENGTH_SHORT).show();
                            progressBar1.setVisibility(View.GONE);
                        }
                    });
                }
                }

            }
        });
    }

    private void init_single() {
        for (int i=0;i<list_view.size();i++){
            final int a=i;
            list_view.get(a).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancal_order.setEnabled(true);
                    if (list_xuanz.get(a)){
                        list_view.get(a).findViewById(R.id.yes_but).setVisibility(View.GONE);
                        list_xuanz.set(a,false);
                        int z=0;
                        for (int i=0;i<list_xuanz.size();i++){
                            if (list_xuanz.get(i)){
                                z++;
                            }
                        }
                        if (z>0){
                            cancal_order.setText("选中订单取消"+"("+z+")");
                            cancal_order.setBackgroundResource(R.drawable.button_style_old);
                        }else {
                            cancal_order.setEnabled(false);
                            cancal_order.setText("选中订单取消");
                            cancal_order.setBackgroundResource(R.drawable.button_style_gr2);
                        }


                    }else {
                        list_view.get(a).findViewById(R.id.yes_but).setVisibility(View.VISIBLE);
                        list_xuanz.set(a,true);
                        int z=0;
                        for (int i=0;i<list_xuanz.size();i++){
                            if (list_xuanz.get(i)){
                                z++;
                            }
                        }
                        if (z>0){
                            cancal_order.setText("选中订单取消"+"("+z+")");
                            cancal_order.setBackgroundResource(R.drawable.button_style_old);
                        }
                    }

                }
            });

        }
    }



    private void init_back() {
        logreg_left_nei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void init() {
        yes_but=findViewById(R.id.yes_but);
        progressBar1=findViewById(R.id.progressBar1);
        cancal_order= (Button) findViewById(R.id.cancal_order);
        cancal_order.setEnabled(false);
        logreg_left_nei=findViewById(R.id.logreg_left_nei);
        service_info_all_view= (LinearLayout) findViewById(R.id.service_info_all_view);
    }

    private void init_wangluo() {
        progressBar1.setVisibility(View.VISIBLE);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url= RetrofitUtil.url_child_order;
        RequestParams requestParams=new RequestParams();
        requestParams.put("orderid", orderid);
        requestParams.put("user_id",  AyiApplication.getInstance().accountService().id());
        requestParams.put("token",  AyiApplication.getInstance().accountService().token());
         requestParams.put("group_id","3");
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                    System.out.println("test:::::"+response.toString());

                try {
                    final JSONArray jsonArray= response.getJSONArray("data");
                    service_info_all_view.removeAllViews();
                    list_view.clear();
                    list_child.clear();
                    list_xuanz.clear();
                    for (int i=0;i<jsonArray.length();i++){

                        View view= LayoutInflater.from(Service_detail.this).inflate(R.layout.service_info_item,null);
                       View baoxian_img= view.findViewById(R.id.baoxian_img);
                        if (!jsonArray.getJSONObject(i).getString("policynum_customer").equals("")){
                            baoxian_img.setVisibility(View.VISIBLE);
                        }
                        TextView date= (TextView) view.findViewById(R.id.jutishijian);
                        final TextView order_status= (TextView) view.findViewById(R.id.order_status);
                        TextView pay_baozj= (TextView) view.findViewById(R.id.pay_baozj);
                        TextView pay_status= (TextView) view.findViewById(R.id.pay_status);
                        ImageView gaizhang= (ImageView) view.findViewById(R.id.gaizhang);
                        Button go_pay= (Button) view.findViewById(R.id.go_pay);
                        Button cancel_pay= (Button) view.findViewById(R.id.cancel_pay);
                        if(jsonArray.getJSONObject(i).getJSONObject("serviceShow").getString("time").split("\\|").length>1){
                            date.setText(jsonArray.getJSONObject(i).getJSONObject("serviceShow").getString("time").split("\\|")[0]+"\n"+
                                    jsonArray.getJSONObject(i).getJSONObject("serviceShow").getString("time").split("\\|")[1]);
                        }else {
                            if(jsonArray.getJSONObject(i).getJSONObject("serviceShow").getString("time").equals("保证金")){
                                cancel_pay.setVisibility(View.GONE);
                                order_status.setVisibility(View.GONE);
                            }
                            date.setText(jsonArray.getJSONObject(i).getJSONObject("serviceShow").getString("time"));
                        }

                        boolean list_flag=false;
                        switch (jsonArray.getJSONObject(i).getString("status")){
                            case "0": order_status.setText("待接单");
                                break;
                            case "1": order_status.setText("已接单");
                                break;
                            case "2": order_status.setText("工作中");
                                list_flag=true;
                                cancel_pay.setVisibility(View.INVISIBLE);
                                break;
                            case "3": order_status.setText("已完成");
                                list_flag=true;
                                cancel_pay.setVisibility(View.INVISIBLE);
                                gaizhang.setVisibility(View.VISIBLE);
                                gaizhang.setImageResource(R.mipmap.yiwanchenggaizhang);
                                break;
                            case "4": order_status.setText("已评价");
                                list_flag=true;
                                break;
                            case "5": order_status.setText("已取消");
                                list_flag=true;
                                cancel_pay.setVisibility(View.INVISIBLE);
                                go_pay.setVisibility(View.INVISIBLE);
                                gaizhang.setVisibility(View.VISIBLE);
                                gaizhang.setImageResource(R.mipmap.yiquxiaogaizhang);
                                break;
                            case "6": order_status.setText("已退款");
                                list_flag=true;
                                cancel_pay.setVisibility(View.INVISIBLE);
                                go_pay.setVisibility(View.INVISIBLE);          break;
                            case "7": order_status.setText("待结算");
                                cancel_pay.setVisibility(View.INVISIBLE);
                                go_pay.setVisibility(View.INVISIBLE);
                                break;
                            default:     order_status.setText("默认");
                                cancel_pay.setVisibility(View.INVISIBLE);
                                go_pay.setVisibility(View.INVISIBLE);
                                break;
                        }
                        if(jsonArray.getJSONObject(i).getString("payed").equals("1")){
                            pay_status.setText("(已付)");
                            go_pay.setVisibility(View.INVISIBLE);
                        }else if(jsonArray.getJSONObject(i).getString("payed").equals("0")){
                            pay_status.setText("(未付)");
                            pay_status.setTextColor(getResources().getColor(R.color.red));
                        }
                        pay_baozj.setText(jsonArray.getJSONObject(i).getString("price"));
                        final String price=jsonArray.getJSONObject(i).getString("price");
                                final String childordernum=jsonArray.getJSONObject(i).getString("id");
                                final String ordernum=jsonArray.getJSONObject(i).getString("ordernum");
                                final String childordernum2=jsonArray.getJSONObject(i).getString("childordernum");
                        go_pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Toast.makeText(Service_detail.this,"去支付",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(Service_detail.this, Order_pay.class);
                                intent.putExtra("type_num",getIntent().getStringExtra("type_num"));
                                intent.putExtra("type",getIntent().getStringExtra("type"));//这是服务内容。
                                intent.putExtra("time_start",getIntent().getStringExtra("time_start"));//开始的预约时间
                                intent.putExtra("user_name",getIntent().getStringExtra("user_name"));
                                intent.putExtra("place",getIntent().getStringExtra("place"));//地方
                                intent.putExtra("price",price);
                                System.out.println("mypri:"+price);
                                List<mlist_pay> list_item=new ArrayList<mlist_pay>();
                                mlist_pay item=new mlist_pay();
                                item.setPrice(price);
                                item.setProject(getIntent().getStringExtra("type"));
                                item.setQuantity("1");
                                list_item.add(item);
                                intent.putExtra("list", (Serializable) list_item);
                                intent.putExtra("orderid",getIntent().getStringExtra("orderid"));
                                intent.putExtra("ordernum",ordernum);
                                intent.putExtra("childorder",childordernum);
                                intent.putExtra("childordernum2",childordernum2);
                                intent.putExtra("areaid",getIntent().getStringExtra("areaid"));
                                startActivity(intent);
                            }
                        });
                        cancel_pay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                Toast.makeText(Service_detail.this,"去取消",Toast.LENGTH_SHORT).show();
                                list_cancel_reason=new ArrayList<cancel_reason>();
                                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                String url= RetrofitUtil.url_cancelreason;
                                RequestParams requestParams=new RequestParams();
                                requestParams.put("group", "3");//3表示客户端，4表示阿姨端
                                requestParams.put("user_id",  AyiApplication.getInstance().accountService().id());
                                requestParams.put("token",  AyiApplication.getInstance().accountService().token());
                                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        System.out.println(response);
                                        try {
                                            JSONArray data= response.getJSONArray("data");
                                            for (int i=0;i<data.length();i++){
                                                cancel_reason item=new cancel_reason();
                                                item.setId(data.getJSONObject(i).getString("id"));
                                                item.setGroup(data.getJSONObject(i).getString("group"));
                                                item.setReason(data.getJSONObject(i).getString("reason"));
                                                list_cancel_reason.add(item);
                                            }
                                            String[] strings=new String[list_cancel_reason.size()];
                                            for (int i=0;i<list_cancel_reason.size();i++){
                                                strings[i]=list_cancel_reason.get(i).getReason();
                                            }

                                            AlertDialog.Builder builder;
                                            builder = new AlertDialog.Builder(Service_detail.this);
                                            alert = builder.create();
                                            View view = LayoutInflater.from(Service_detail.this).inflate(R.layout.calcel_reason, null);
                                            spinner= (Spinner) view.findViewById(R.id.spinner);
                                            adapter_spinner1=new ArrayAdapter<String>(Service_detail.this,android.R.layout.simple_spinner_item,strings);
                                            adapter_spinner1.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                            spinner.setAdapter(adapter_spinner1);
                                            //确定
                                            view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    progressBar1.setVisibility(View.VISIBLE);
                                                    alert.dismiss();
                                                    v.setEnabled(false);
                                                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                                    String url= RetrofitUtil.url_cancel_submit;
                                                    RequestParams requestParams=new RequestParams();
                                                    requestParams.put("orderid", orderid);
                                                    requestParams.put("childid", childordernum);
                                                    requestParams.put("msg", list_cancel_reason.get(spinner.getSelectedItemPosition()).getReason());
                                                    requestParams.put("user_id",  AyiApplication.getInstance().accountService().id());
                                                    requestParams.put("token",  AyiApplication.getInstance().accountService().token());
                                                    asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                                        @Override
                                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                            super.onSuccess(statusCode, headers, response);

                                                            try {
                                                                System.out.println("response"+response);
                                                                if (response.getString("ret").equals("200")&&response.getJSONObject("data").getString("status").equals("1")){

                                                                    AlertDialog.Builder builder;
                                                                    builder = new AlertDialog.Builder(Service_detail.this);
                                                                    alert = builder.create();
                                                                    View view = LayoutInflater.from(Service_detail.this).inflate(R.layout.calcel_rejectokok, null);
                                                                    TextView title= (TextView) view.findViewById(R.id.textname);
                                                                    view.findViewById(R.id.cancel_btn).setVisibility(View.GONE);
                                                                    view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(View v) {
                                                                            alert.dismiss();
                                                                            cancal_order.setText("选中订单取消");
                                                                            cancal_order.setBackgroundResource(R.drawable.button_style_gr2);
                                                                            init_wangluo();
                                                                        }
                                                                    });
                                                                    title.setText(response.getJSONObject("data").getString("msg").toString());
                                                                    alert.setView(view);
                                                                    alert.show();
                                                                }else {
                                                                    Toast.makeText(Service_detail.this,"取消失败",Toast.LENGTH_SHORT).show();
                                                                }

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
                                            //取消
                                            view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    alert.dismiss();
                                                }
                                            });


                                            alert.setView(view);
                                            alert.show();



                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }


                                    }
                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                        System.out.println(errorResponse);
                                        super.onFailure(statusCode, headers, throwable, errorResponse);

                                    }
                                });

                            }
                        });
                        service_info_all_view.addView(view);
                        if (!list_flag){
                            list_view.add(view);
                            list_child.add(childordernum);
                            list_xuanz.add(false);
                        }

                    }
                    init_single();
                    progressBar1.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressBar1.setVisibility(View.GONE);
                }



            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                progressBar1.setVisibility(View.GONE);
            }
        });

    }
}
