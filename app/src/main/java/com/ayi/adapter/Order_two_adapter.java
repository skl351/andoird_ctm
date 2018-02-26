package com.ayi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayi.R;
import com.ayi.entity.item_daipingjia;
import com.ayi.entity.mlist_pay;
import com.ayi.order_four.Order_one_detail;
import com.ayi.order_four.Pingjia_jiemian;
import com.ayi.order_four.service_info;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Data_time_cuo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


/**
 * Created by Administrator on 2016/8/30.
 */
public class Order_two_adapter extends BaseAdapter {
    AlertDialog alert;
    private List<item_daipingjia> list;
    Context context;
    public Order_two_adapter(Context context, List<item_daipingjia> list){
        this.list=list;
        this.context=context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private View view;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view= LayoutInflater.from(context).inflate(R.layout.order_daipingjia, null);
        ImageView zeren= (ImageView) view.findViewById(R.id.zeren);
        if (!list.get(position).getPolicynum_customer().equals("")){
            zeren.setVisibility(View.VISIBLE);
        }
        View daikexiadan_id=view.findViewById(R.id.daikexiadan_id);

        if (list.get(position).getIsvalet()==1){
            daikexiadan_id.setVisibility(View.VISIBLE);
        }else {
            daikexiadan_id.setVisibility(View.GONE);
        }
        TextView service_time3= (TextView) view.findViewById(R.id.service_time3);
      TextView total_money= (TextView) view.findViewById(R.id.total_money);
      TextView suishoudai_money= (TextView) view.findViewById(R.id.suishoudai_money);
      TextView service_content= (TextView) view.findViewById(R.id.service_content);
      TextView service_time1= (TextView) view.findViewById(R.id.service_time1);
      TextView service_time2= (TextView) view.findViewById(R.id.service_time2);
      TextView get_time= (TextView) view.findViewById(R.id.get_time);
        TextView get_time2= (TextView) view.findViewById(R.id.get_time2);
        final TextView status= (TextView) view.findViewById(R.id.status);
        TextView cancel_btn= (TextView) view.findViewById(R.id.cancel_btn);
        TextView go_pay= (TextView) view.findViewById(R.id.go_pay);
        TextView shanweijiedan= (TextView) view.findViewById(R.id.shanweijiedan);
        ImageView headimg= (ImageView) view.findViewById(R.id.headimg);
        View zongjia=view.findViewById(R.id.zongjia);
        TextView status_2 = (TextView) view.findViewById(R.id.status_2);
        if(list.get(position).getCleaner_headimg()==null||list.get(position).getCleaner_headimg().equals("")){
            headimg.setImageResource(R.mipmap.zhanweifu);
        }else {
            ImageLoader.getInstance().displayImage(list.get(position).getCleaner_headimg(),headimg);
        }
        String status_flag=list.get(position).getStatus();
        switch (status_flag){
            case "0": status.setText("待接单");
                status.setTextColor(context.getResources().getColor(R.color.yishouli));
                cancel_btn.setText("取消订单");
                break;
            case "1": status.setText("已接单");
                status.setTextColor(context.getResources().getColor(R.color.yishouli));
                cancel_btn.setText("取消订单");
                shanweijiedan.setVisibility(View.INVISIBLE);
                break;
            case "2": status.setText("工作中");
                status.setTextColor(context.getResources().getColor(R.color.yishouli));
                cancel_btn.setVisibility(View.INVISIBLE);
                shanweijiedan.setVisibility(View.INVISIBLE);
                break;
            case "3": status.setText("已完成");
                status.setTextColor(context.getResources().getColor(R.color.daishouli));
                cancel_btn.setText("评价");
                cancel_btn.setTextColor(context.getResources().getColor(R.color.main_green));
                cancel_btn.setBackground(context.getResources().getDrawable(R.drawable.biankuang_ne_gre));
                shanweijiedan.setVisibility(View.INVISIBLE);
                break;
            case "4": status.setText("已评价");
                status.setTextColor(context.getResources().getColor(R.color.daishouli));
                cancel_btn.setVisibility(View.VISIBLE);
                cancel_btn.setText("评价");
                cancel_btn.setTextColor(context.getResources().getColor(R.color.main_green));
                cancel_btn.setBackground(context.getResources().getDrawable(R.drawable.biankuang_ne_gre));
                shanweijiedan.setVisibility(View.INVISIBLE);
                break;
            case "5": status.setText("已取消");
                status.setTextColor(context.getResources().getColor(R.color.order_gra));
                cancel_btn.setVisibility(View.INVISIBLE);
                shanweijiedan.setVisibility(View.INVISIBLE);
                break;
            case "6": status.setText("已退款");
                status.setTextColor(context.getResources().getColor(R.color.order_gra));
                cancel_btn.setVisibility(View.INVISIBLE);
                shanweijiedan.setVisibility(View.INVISIBLE);           break;
            case "7": status.setText("待结算");
                status.setTextColor(context.getResources().getColor(R.color.daishouli));
                cancel_btn.setVisibility(View.GONE);
                shanweijiedan.setVisibility(View.INVISIBLE);
                break;
            default:     status.setText("默认");
                cancel_btn.setVisibility(View.GONE);
                shanweijiedan.setVisibility(View.INVISIBLE);

        }
        total_money.setText(list.get(position).getTotal_money());
        suishoudai_money.setText(list.get(position).getSuishoudai_money());
        service_content.setText(list.get(position).getService_content());
        String[] str=list.get(position).getService_time1().split("\\|");
        get_time.setText(Data_time_cuo.gettime2(list.get(position).getGet_time()));
        get_time2.setText(Data_time_cuo.gettime4(list.get(position).getGet_time()));
        if(str.length>2){
            if(!str[2].substring(0,1).equals("0")){
                service_time3.setText(str[2]);
                service_time3.setVisibility(View.VISIBLE);
            }
            service_time1.setText(str[0]);
            service_time2.setText(str[1]);
        }else {
            service_time2.setText(str[0]);
            service_time1.setText(str[1]);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context.getApplicationContext(), Order_one_detail.class);
                intent.putExtra("id",list.get(position).getOrderid());
                intent.putExtra("status",status.getText().toString());
                intent.putExtra("trialorder",list.get(position).getTrialorder());
                intent.putExtra("service_type_id",list.get(position).getService_type_id());

                intent.putExtra("type_num",list.get(position).getService_type_id());
                intent.putExtra("orderid",list.get(position).getOrderid());
                intent.putExtra("type",list.get(position).getService_content());
                intent.putExtra("time_start",list.get(position).getService_time1());
                intent.putExtra("user_name",list.get(position).getUser_name());
                intent.putExtra("place",list.get(position).getPlace());
                intent.putExtra("price",list.get(position).getTotal_money());
                List<mlist_pay> list_item=new ArrayList<mlist_pay>();
                mlist_pay item=new mlist_pay();
                item.setPrice(list.get(position).getTotal_money());
                item.setProject(list.get(position).getService_content());
                list_item.add(item);
                intent.putExtra("list", (Serializable) list_item);

                context.startActivity(intent);
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context,Pingjia_jiemian.class);
                intent.putExtra("orderid",list.get(position).getOrderid());
                context.startActivity(intent);

            }
        });
        //试用订单
        if (list.get(position).getTrialorder().equals("1")) {

            if (!(list.get(position).getStatus2().equals("0") || list.get(position).getStatus2().equals("-1"))) {
                go_pay.setVisibility(View.GONE);
                cancel_btn.setVisibility(View.GONE);
                status_2.setVisibility(View.VISIBLE);
                switch (list.get(position).getStatus2()) {
                    case "1":
                        status_2.setText("客户申请取消中");
//                        status.setText("客户申请取消中");
                        break;
                    case "2":
                        status_2.setText("阿姨申请取消中");
//                        status.setText("阿姨申请取消");
                        break;
                    case "3":
                        status_2.setText("客户同意取消");
//                        status.setText("已结束试用");
                        break;
                    case "4":
                        status_2.setText("阿姨同意取消");
//                        status.setText("已结束试用");
                        break;
                    case "5":
                        status_2.setText("客户拒绝取消");
//                        status.setText("客户拒绝取消");
                        break;
                    case "6":
                        status_2.setText("阿姨拒绝取消");
//                        status.setText("阿姨拒绝取消");
                        break;
                    case "7":
                        status_2.setText("用户申请转正式订单");
//                        status.setText("申请中");
                        break;
                    case "8":
                        status_2.setText("已正式签约");
//                        status.setText("签约成功");
                        break;
                    case "9":
                        status_2.setText("阿姨拒绝转正");
//                        status.setText("签约失败");
                        break;
                }

            }

            if (list.get(position).getStatus2().equals("0") || list.get(position).getStatus2().equals("3") || list.get(position).getStatus2().equals("4") || list.get(position).getStatus2().equals("5") || list.get(position).getStatus2().equals("6") || list.get(position).getStatus2().equals("1")) {
                cancel_btn.setVisibility(View.VISIBLE);
                go_pay.setVisibility(View.VISIBLE);
                go_pay.setText("正式签约");
                go_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //这里是需要即将跳转正式签约的地方转口
                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                        String url = RetrofitUtil.url_ayi_info;
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("cleaner_id", list.get(position).getAyi_id());
                        System.out.println("ayi_ud:" + list.get(position).getAyi_id());
                        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                try {
                                    //成功说明取到了阿姨的钱
                                    System.out.println(response);
                                    JSONObject data = response.getJSONObject("data");
                                    JSONArray jsonArray = data.getJSONArray("cPrice");
                                    String typeid = list.get(position).getService_type_id();
                                    String price = "";
                                    String yonglaipanduan = "0";
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        if (jsonArray.getJSONObject(i).getString("type_id").equals(typeid)) {
                                            price = jsonArray.getJSONObject(i).getString("out_price");

                                            if (typeid.equals("14")) {
                                                String p1 = price.split("\\.")[0];
                                                Double p2 = Double.parseDouble(p1) / 30 * 7;
                                                DecimalFormat df = new DecimalFormat("######0.00");
                                                yonglaipanduan = "14";
                                                price = String.valueOf(df.format(p2));
                                            }

                                            break;
                                        }
                                    }
                                    Intent intent = new Intent(context, service_info.class);
                                    intent.putExtra("type", list.get(position).getService_content());
                                    intent.putExtra("user_name", list.get(position).getUser_name());
                                    intent.putExtra("phone", list.get(position).getPhone());
                                    intent.putExtra("place", list.get(position).getPlace());
                                    intent.putExtra("price", price);
                                    intent.putExtra("ayi_name", data.getString("name"));
                                    intent.putExtra("phone2", data.getString("phone"));
                                    intent.putExtra("yonglaipanduan", yonglaipanduan);
                                    intent.putExtra("orderId", list.get(position).getOrderid());
                                    context.startActivity(intent);

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

            }
        }

//        if (list.get(position).getTrialorder().equals("1")) {{
//            go_pay.setVisibility(View.VISIBLE);
//            go_pay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    //这里是需要即将跳转正式签约的地方转口
//                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
//                    String url= RetrofitUtil.url_ayi_info;
//                    RequestParams requestParams=new RequestParams();
//                    requestParams.put("cleaner_id", list.get(position).getAyi_id());
//                    System.out.println("ayi_ud:"+list.get(position).getAyi_id());
//                    asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            super.onSuccess(statusCode, headers, response);
//                            try {
//                                //成功说明取到了阿姨的钱
//                                System.out.println(response);
//                                JSONObject data= response.getJSONObject("data");
//                                JSONArray jsonArray=data.getJSONArray("cPrice");
//                                String typeid=list.get(position).getService_type_id();
//                                String price="";
//                                String yonglaipanduan="0";
//                                for (int i=0;i<jsonArray.length();i++){
//                                    if(jsonArray.getJSONObject(i).getString("type_id").equals(typeid)){
//                                        price=jsonArray.getJSONObject(i).getString("out_price");
//
//                                        if(typeid.equals("14")){
//                                            String p1=price.split("\\.")[0];
//                                            Double p2=Double.parseDouble(p1)/30*7;
//                                            DecimalFormat df   = new DecimalFormat("######0.00");
//                                            yonglaipanduan="14";
//                                            price= String.valueOf(df.format(p2));
//                                        }
//
//                                        break;
//                                    }
//                                }
//                                Intent intent=new Intent(context,service_info.class);
//                                intent.putExtra("type",list.get(position).getService_content());
//                                intent.putExtra("user_name",list.get(position).getUser_name());
//                                intent.putExtra("phone",list.get(position).getPhone());
//                                intent.putExtra("place",list.get(position).getPlace());
//                                intent.putExtra("price",price);
//                                intent.putExtra("ayi_name",data.getString("name"));
//                                intent.putExtra("phone2",data.getString("phone"));
//                                intent.putExtra("yonglaipanduan",yonglaipanduan);
//                                intent.putExtra("orderId",list.get(position).getOrderid());
//                                context.startActivity(intent);
//
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//
//
//                        }
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
//                            super.onFailure(statusCode, headers, throwable, errorResponse);
//
//                        }
//                    });
//
//
//                }
//            });
//        }}

        if (!list.get(position).getTrialorder().equals("1")){
            if(Integer.parseInt(list.get(position).getService_type_id())>8) {
                zongjia.setVisibility(View.INVISIBLE);
            }
        }

        return view;
    }

}
