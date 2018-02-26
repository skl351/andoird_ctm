package com.ayi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.entity.cancel_reason;
import com.ayi.entity.item_weiwancheng;
import com.ayi.entity.mlist_pay;
import com.ayi.home_page.Order_pay;
import com.ayi.order_four.Order_one_detail;
import com.ayi.order_four.Service_detail;
import com.ayi.order_four.service_info;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Data_time_cuo;
import com.ayi.utils.Show_toast;
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
public class Order_one_adapter extends BaseAdapter {
    AlertDialog alert;
    private List<item_weiwancheng> list;
    Context context;
    List<cancel_reason> list_cancel_reason;
    private Spinner spinner;
    private ArrayAdapter<String> adapter_spinner1;

    public Order_one_adapter(Context context, List<item_weiwancheng> list) {
        this.list = list;
        this.context = context;
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
    boolean flag_complete = false;
    boolean flag_reject = false;
    boolean flag_meiyoukaishi = false;//还么有开始
    boolean overtime_flag = false;//加时
    boolean overtime_flag2 = false;//取消
    boolean daijiesuan_flag = false;//取消

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.order_weiwancheng, null);
        ImageView zeren= (ImageView) view.findViewById(R.id.zeren);
        if (!list.get(position).getPolicynum_customer().equals("")){
            zeren.setVisibility(View.VISIBLE);
        }
        View daikexiadan_id = view.findViewById(R.id.daikexiadan_id);
        if (list.get(position).getIsvalet() == 1) {
            daikexiadan_id.setVisibility(View.VISIBLE);
        } else {
            daikexiadan_id.setVisibility(View.GONE);
        }
        TextView total_money = (TextView) view.findViewById(R.id.total_money);
        TextView suishoudai_money = (TextView) view.findViewById(R.id.suishoudai_money);
        TextView service_content = (TextView) view.findViewById(R.id.service_content);
        TextView service_time1 = (TextView) view.findViewById(R.id.service_time1);//星期
        TextView service_time2 = (TextView) view.findViewById(R.id.service_time2);//年月
        TextView service_time3 = (TextView) view.findViewById(R.id.service_time3);//时间
        TextView get_time = (TextView) view.findViewById(R.id.get_time);
        TextView get_time2 = (TextView) view.findViewById(R.id.get_time2);
        final TextView status = (TextView) view.findViewById(R.id.status);
        Button cancel_btn = (Button) view.findViewById(R.id.cancel_btn);
        cancel_btn.setTextColor(context.getResources().getColor(R.color.light_gray));
        cancel_btn.setBackground(context.getResources().getDrawable(R.drawable.biankuang_ne_gr));
        TextView shanweijiedan = (TextView) view.findViewById(R.id.shanweijiedan);
        Button go_pay = (Button) view.findViewById(R.id.go_pay);
        String status_flag = list.get(position).getStatus();
        ImageView headimg = (ImageView) view.findViewById(R.id.headimg);
        TextView status_2 = (TextView) view.findViewById(R.id.status_2);
        View zongjia = view.findViewById(R.id.zongjia);
        if (list.get(position).getCleaner_headimg() == null || list.get(position).getCleaner_headimg().equals("")) {
            headimg.setImageResource(R.mipmap.zhanweifu);
        } else {
            ImageLoader.getInstance().displayImage(list.get(position).getCleaner_headimg(), headimg);
        }
        flag_reject = false;
        flag_complete = false;
        flag_meiyoukaishi = false;
        overtime_flag2 = false;
        overtime_flag = false;
        switch (status_flag) {
            case "0":
                status.setText("待接单");
                status.setTextColor(context.getResources().getColor(R.color.yishouli));
                cancel_btn.setText("取消订单");
                flag_meiyoukaishi = true;
                break;
            case "1":
                status.setText("已接单");
                status.setTextColor(context.getResources().getColor(R.color.yishouli));
                if (Integer.parseInt(list.get(position).getService_type_id()) <= 8) {
//                    overtime_flag=true;
                }
                cancel_btn.setText("取消订单");
                flag_meiyoukaishi = true;
                shanweijiedan.setVisibility(View.INVISIBLE);
                break;
            case "2":
                status.setText("工作中");
                status.setTextColor(context.getResources().getColor(R.color.yishouli));
                if ((Integer.parseInt(list.get(position).getService_type_id()) <= 8)
                        && !(list.get(position).getStatus3().equals("1") && list.get(position).getStatus3().equals("2"))) {
                    overtime_flag = true;
                    System.out.println("正在工作中");

                }
                if ((Integer.parseInt(list.get(position).getService_type_id()) <= 8) && list.get(position).getStatus3().equals("1")) {
                    overtime_flag2 = true;
                }

                cancel_btn.setVisibility(View.GONE);
                shanweijiedan.setVisibility(View.INVISIBLE);
                flag_complete = true;
                flag_meiyoukaishi = false;
                break;
            case "3":
                status.setText("已完成");

                status.setTextColor(context.getResources().getColor(R.color.daishouli));
                flag_complete = true;
                cancel_btn.setText("评价");
                shanweijiedan.setVisibility(View.INVISIBLE);
                break;
            case "4":
                status.setText("已评价");
                status.setTextColor(context.getResources().getColor(R.color.daishouli));
                cancel_btn.setVisibility(View.GONE);
                shanweijiedan.setVisibility(View.INVISIBLE);
                break;
            case "5":
                status.setText("已取消");
                status.setTextColor(context.getResources().getColor(R.color.order_gra));
                cancel_btn.setVisibility(View.GONE);
                shanweijiedan.setVisibility(View.INVISIBLE);
                break;
            case "6":
                status.setText("已退款");
                status.setTextColor(context.getResources().getColor(R.color.order_gra));
                cancel_btn.setVisibility(View.GONE);
                shanweijiedan.setVisibility(View.INVISIBLE);
                break;
            case "7":
                status.setText("待结算");
                status.setTextColor(context.getResources().getColor(R.color.daishouli));
                daijiesuan_flag = true;
                flag_complete = true;
                cancel_btn.setVisibility(View.GONE);
                shanweijiedan.setVisibility(View.INVISIBLE);
                break;
            default:
                status.setText("默认");
                cancel_btn.setVisibility(View.GONE);
                shanweijiedan.setVisibility(View.INVISIBLE);
        }
        //判断支付与否
        if (list.get(position).getPayed().equals("0")) {
            status.setText("未支付");
            status.setTextColor(context.getResources().getColor(R.color.yishouli));
            go_pay.setVisibility(View.VISIBLE);
            go_pay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Order_pay.class);
                    intent.putExtra("areaid", list.get(position).getAreaid());
                    intent.putExtra("type_num", list.get(position).getService_type_id());
                    intent.putExtra("type", list.get(position).getService_content());//这是服务内容。
                    intent.putExtra("time_start", list.get(position).getService_time1());//开始的预约时间
                    intent.putExtra("user_name", list.get(position).getUser_name());
                    intent.putExtra("place", list.get(position).getPlace());//地方
                    intent.putExtra("price", list.get(position).getTotal_money());
                    List<mlist_pay> list_item = new ArrayList<mlist_pay>();
                    mlist_pay item = new mlist_pay();
                    item.setPrice("" + (Double.valueOf(list.get(position).getTotal_money()) - Double.valueOf(list.get(position).getSuishoudai_money())));
                    item.setProject(list.get(position).getService_content());
                    item.setQuantity("1");
                    list_item.add(item);
                    for (int i = 0; i < list.get(position).getList_item().size(); i++) {
                        mlist_pay item1 = new mlist_pay();
                        item1.setPrice(Integer.valueOf(list.get(position).getList_item().get(i).getPrice().split("\\.")[0]) * Integer.valueOf(list.get(position).getList_item().get(i).getQuantity()) + ".00");
                        item1.setQuantity(list.get(position).getList_item().get(i).getQuantity());
                        item1.setProject(list.get(position).getList_item().get(i).getProject());
                        list_item.add(item1);
                    }
                    intent.putExtra("list", (Serializable) list_item);
                    intent.putExtra("orderid", list.get(position).getOrderid());
                    intent.putExtra("ordernum", list.get(position).getOrdernum());
                    context.startActivity(intent);
                }
            });
        } else {
            go_pay.setVisibility(View.GONE);
        }


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
                        flag_reject = true;
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
                        flag_complete = true;
                        break;
                    case "6":
                        flag_complete = true;
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
            if (flag_reject) {
                cancel_btn.setVisibility(View.VISIBLE);
                go_pay.setVisibility(View.VISIBLE);
                go_pay.setText("拒绝取消");
                cancel_btn.setText("同意取消");
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(context);
                        alert = builder.create();
                        View view = LayoutInflater.from(context).inflate(R.layout.calcel_rejectokok, null);
                        view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(final View v) {
                                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                String url = RetrofitUtil.url_cancel_shiyong;
                                RequestParams requestParams = new RequestParams();
                                requestParams.put("orderid", list.get(position).getOrderid());
                                requestParams.put("val", 3);
                                requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                                requestParams.put("token", AyiApplication.getInstance().accountService().token());
                                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        try {
                                            System.out.println("resp1onse" + response);
                                            if (response.getString("ret").equals("200") && response.getJSONObject("data").getString("status").equals("1")) {

                                                list.get(position).setStatus2("3");
                                                list.get(position).setStatus("7");
                                                notifyDataSetChanged();
                                            } else {
                                                Toast.makeText(context, "取消失败", Toast.LENGTH_SHORT).show();
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
                        //取消
                        view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                            }
                        });
                        alert.setView(view);
                        alert.show();
                    }
                });

                go_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(context);
                        alert = builder.create();
                        View view = LayoutInflater.from(context).inflate(R.layout.calcel_rejectok, null);
                        view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                String url = RetrofitUtil.url_cancel_shiyong;
                                RequestParams requestParams = new RequestParams();
                                requestParams.put("orderid", list.get(position).getOrderid());
                                requestParams.put("val", 5);
                                requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                                requestParams.put("token", AyiApplication.getInstance().accountService().token());
                                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        System.out.println("resp2onse" + response);
                                        try {
                                            if (response.getString("ret").equals("200") && response.getJSONObject("data").getString("status").equals("1")) {
                                                list.get(position).setStatus("2");
                                                list.get(position).setStatus2("5");
                                                notifyDataSetChanged();
                                                if (response.getString("msg").equals("")) {
                                                    Toast.makeText(context, "取消成功", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(context, response.getString("msg").toString(), Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(context, "取消失败", Toast.LENGTH_SHORT).show();
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

                        //取消
                        view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                            }
                        });


                        alert.setView(view);
                        alert.show();
                    }
                });

            } else {
                System.out.println("kanyixai---1---");
                if (flag_complete) {
                    System.out.println("kanyixai---2---");
                    if (list.get(position).getStatus2().equals("0") || list.get(position).getStatus2().equals("3") || list.get(position).getStatus2().equals("4") || list.get(position).getStatus2().equals("5") || list.get(position).getStatus2().equals("6") || list.get(position).getStatus2().equals("9")) {
                        System.out.println("kanyixai---3---");
                        cancel_btn.setVisibility(View.VISIBLE);
                        go_pay.setVisibility(View.VISIBLE);
                        if (daijiesuan_flag) {
                            status.setText("待结算");
                            cancel_btn.setVisibility(View.GONE);
                        }
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
                                            System.out.println("resp3onse" + response);
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
                if (flag_meiyoukaishi) {
                    cancel_btn.setText("取消订单");
                    //取消订单
                    cancel_btn.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            list_cancel_reason = new ArrayList<cancel_reason>();
                            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                            String url = RetrofitUtil.url_cancelreason;
                            RequestParams requestParams = new RequestParams();
                            requestParams.put("group", "3");//3表示客户端，4表示阿姨端
                            requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                            requestParams.put("token", AyiApplication.getInstance().accountService().token());
                            asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    System.out.println("resp4onse" + response);
                                    try {
                                        JSONArray data = response.getJSONArray("data");
                                        for (int i = 0; i < data.length(); i++) {
                                            cancel_reason item = new cancel_reason();
                                            item.setId(data.getJSONObject(i).getString("id"));
                                            item.setGroup(data.getJSONObject(i).getString("group"));
                                            item.setReason(data.getJSONObject(i).getString("reason"));
                                            list_cancel_reason.add(item);
                                        }
                                        String[] strings = new String[list_cancel_reason.size()];
                                        for (int i = 0; i < list_cancel_reason.size(); i++) {
                                            strings[i] = list_cancel_reason.get(i).getReason();
                                        }

                                        AlertDialog.Builder builder;
                                        builder = new AlertDialog.Builder(context);
                                        alert = builder.create();
                                        View view = LayoutInflater.from(context).inflate(R.layout.calcel_reason, null);
                                        spinner = (Spinner) view.findViewById(R.id.spinner);

                                        adapter_spinner1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, strings);
                                        adapter_spinner1.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                        spinner.setAdapter(adapter_spinner1);
                                        //确定
                                        view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                                String url = RetrofitUtil.url_cancel_submit;
                                                RequestParams requestParams = new RequestParams();
                                                requestParams.put("orderid", list.get(position).getOrderid());
                                                requestParams.put("childid", "-1");
                                                requestParams.put("msg", list_cancel_reason.get(spinner.getSelectedItemPosition()).getReason());
                                                requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                                                requestParams.put("token", AyiApplication.getInstance().accountService().token());
                                                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                                    @Override
                                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                        super.onSuccess(statusCode, headers, response);
                                                        try {
                                                            alert.dismiss();
                                                            System.out.println("resp5onse" + response.toString());
                                                            if (response.getString("ret").equals("200") && response.getJSONObject("data").getString("status").equals("1")) {

                                                                AlertDialog.Builder builder;
                                                                builder = new AlertDialog.Builder(context);
                                                                alert = builder.create();
                                                                View view = LayoutInflater.from(context).inflate(R.layout.calcel_rejectokok, null);
                                                                TextView title = (TextView) view.findViewById(R.id.textname);
                                                                title.setText(response.getJSONObject("data").getString("msg").toString());
                                                                view.findViewById(R.id.cancel_btn).setVisibility(View.GONE);
                                                                view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(View v) {
                                                                        alert.dismiss();
                                                                        list.remove(position);
                                                                        notifyDataSetChanged();
                                                                    }
                                                                });

                                                                alert.setView(view);
                                                                alert.show();
//                                                                if(response.getString("msg").equals("")){
//                                                                    Toast.makeText(context,"取消成功",Toast.LENGTH_SHORT).show();
//                                                                }else {
//                                                                    Toast.makeText(context,response.getString("msg").toString(),Toast.LENGTH_SHORT).show();
//                                                                }
                                                            } else {
                                                                Toast.makeText(context, "取消失败", Toast.LENGTH_SHORT).show();
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
                } else {
                    cancel_btn.setText("结束试用");
                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder builder;
                            builder = new AlertDialog.Builder(context);
                            alert = builder.create();
                            View view = LayoutInflater.from(context).inflate(R.layout.calcel_queding, null);
                            view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                    String url = RetrofitUtil.url_cancel_shiyong;
                                    RequestParams requestParams = new RequestParams();
                                    requestParams.put("orderid", list.get(position).getOrderid());
                                    requestParams.put("val", 1);
                                    requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                                    requestParams.put("token", AyiApplication.getInstance().accountService().token());
                                    asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            super.onSuccess(statusCode, headers, response);
                                            System.out.println("resp6onse" + response);
                                            try {
                                                if (response.getString("ret").equals("200") && response.getJSONObject("data").getString("status").equals("1")) {
                                                    list.get(position).setStatus2("1");
                                                    notifyDataSetChanged();
                                                    if (response.getString("msg").equals("")) {
                                                        Toast.makeText(context, "结束试用成功", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(context, response.getString("msg").toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(context, "结束试用失败", Toast.LENGTH_SHORT).show();
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

                            //取消
                            view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alert.dismiss();
                                }
                            });
                            alert.setView(view);
                            alert.show();
                        }
                    });
                }
            }
        } else {
            //不是试用订单
            //有子订单
            if (Integer.parseInt(list.get(position).getService_type_id()) > 8) {

                cancel_btn.setVisibility(View.VISIBLE);
                go_pay.setVisibility(View.VISIBLE);
                cancel_btn.setText("取消订单");
                go_pay.setText("查看日程");
                go_pay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Service_detail.class);
                        intent.putExtra("type_num", list.get(position).getService_type_id());
                        intent.putExtra("orderid", list.get(position).getOrderid());
                        intent.putExtra("type", list.get(position).getService_content());
                        intent.putExtra("time_start", list.get(position).getService_time1());
                        intent.putExtra("user_name", list.get(position).getUser_name());
                        intent.putExtra("place", list.get(position).getPlace());
                        intent.putExtra("price", list.get(position).getTotal_money());
                        intent.putExtra("areaid", list.get(position).getAreaid());
                        List<mlist_pay> list_item = new ArrayList<mlist_pay>();
                        mlist_pay item = new mlist_pay();
                        item.setPrice(list.get(position).getTotal_money());
                        item.setProject(list.get(position).getService_content());
                        list_item.add(item);
                        intent.putExtra("list", (Serializable) list_item);
                        context.startActivity(intent);
                    }
                });
                //取消订单
                cancel_btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, Service_detail.class);
                        intent.putExtra("type_num", list.get(position).getService_type_id());
                        intent.putExtra("orderid", list.get(position).getOrderid());
                        intent.putExtra("type", list.get(position).getService_content());
                        intent.putExtra("time_start", list.get(position).getService_time1());
                        intent.putExtra("user_name", list.get(position).getUser_name());
                        intent.putExtra("place", list.get(position).getPlace());
                        intent.putExtra("price", list.get(position).getTotal_money());
                        intent.putExtra("areaid", list.get(position).getAreaid());
                        List<mlist_pay> list_item = new ArrayList<mlist_pay>();
                        mlist_pay item = new mlist_pay();
                        item.setPrice(list.get(position).getTotal_money());
                        item.setProject(list.get(position).getService_content());
                        list_item.add(item);
                        intent.putExtra("list", (Serializable) list_item);
                        context.startActivity(intent);
                    }
                });

            } else {
                //取消订单
                cancel_btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        list_cancel_reason = new ArrayList<cancel_reason>();
                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                        String url = RetrofitUtil.url_cancelreason;
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("group", "3");//3表示客户端，4表示阿姨端
                        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                        requestParams.put("token", AyiApplication.getInstance().accountService().token());
                        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                System.out.println("resp7onse" + response);
                                try {
                                    JSONArray data = response.getJSONArray("data");
                                    for (int i = 0; i < data.length(); i++) {
                                        cancel_reason item = new cancel_reason();
                                        item.setId(data.getJSONObject(i).getString("id"));
                                        item.setGroup(data.getJSONObject(i).getString("group"));
                                        item.setReason(data.getJSONObject(i).getString("reason"));
                                        list_cancel_reason.add(item);
                                    }
                                    String[] strings = new String[list_cancel_reason.size()];
                                    for (int i = 0; i < list_cancel_reason.size(); i++) {
                                        strings[i] = list_cancel_reason.get(i).getReason();
                                    }
                                    AlertDialog.Builder builder;
                                    builder = new AlertDialog.Builder(context);
                                    alert = builder.create();
                                    View view = LayoutInflater.from(context).inflate(R.layout.calcel_reason, null);
                                    spinner = (Spinner) view.findViewById(R.id.spinner);
                                    adapter_spinner1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, strings);
                                    adapter_spinner1.setDropDownViewResource(android.R.layout.simple_spinner_item);
                                    spinner.setAdapter(adapter_spinner1);
                                    //确定
                                    view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                            String url = RetrofitUtil.url_cancel_submit;
                                            RequestParams requestParams = new RequestParams();
                                            requestParams.put("orderid", list.get(position).getOrderid());
                                            requestParams.put("childid", "-1");
                                            requestParams.put("msg", list_cancel_reason.get(spinner.getSelectedItemPosition()).getReason());
                                            requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                                            requestParams.put("token", AyiApplication.getInstance().accountService().token());
                                            asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                    super.onSuccess(statusCode, headers, response);
                                                    System.out.println("resp8onse" + response);
                                                    alert.dismiss();
                                                    try {
                                                        if (response.getString("ret").equals("200") && response.getJSONObject("data").getString("status").equals("1")) {

                                                            AlertDialog.Builder builder;
                                                            builder = new AlertDialog.Builder(context);
                                                            alert = builder.create();
                                                            View view = LayoutInflater.from(context).inflate(R.layout.calcel_rejectokok, null);
                                                            TextView title = (TextView) view.findViewById(R.id.textname);
                                                            view.findViewById(R.id.cancel_btn).setVisibility(View.GONE);
                                                            view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    alert.dismiss();
                                                                    list.remove(position);
                                                                    notifyDataSetChanged();
                                                                }
                                                            });
                                                            title.setText(response.getJSONObject("data").getString("msg").toString());
                                                            alert.setView(view);
                                                            alert.show();
                                                        } else {
                                                            Toast.makeText(context, "取消失败", Toast.LENGTH_SHORT).show();
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
            }
//            System.out.println(list.get(position).getStatus()+"youmeiyou"+position);
            if ((!list.get(position).getStatus().equals("0")) && (!list.get(position).getStatus().equals("1"))) {
                cancel_btn.setVisibility(View.GONE);
//                System.out.println("youmeiyou"+position);
            }
        }
        total_money.setText(list.get(position).getTotal_money());
        suishoudai_money.setText(list.get(position).getSuishoudai_money());
        service_content.setText(list.get(position).getService_content());
        String[] str = list.get(position).getService_time1().split("\\|");

        get_time.setText(Data_time_cuo.gettime2(list.get(position).getGet_time()));
        get_time2.setText(Data_time_cuo.gettime4(list.get(position).getGet_time()));

        if (str.length > 2) {
            if (!str[2].substring(0, 1).equals("0")) {
                service_time3.setText(str[2]);
                service_time3.setVisibility(View.VISIBLE);
            }
            service_time1.setText(str[0]);
            service_time2.setText(str[1]);
        } else {
            service_time2.setText(str[0]);
            service_time1.setText(str[1]);
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context.getApplicationContext(), Order_one_detail.class);
                intent.putExtra("id", list.get(position).getOrderid());
                intent.putExtra("status", status.getText().toString());
                intent.putExtra("trialorder", list.get(position).getTrialorder());
                intent.putExtra("service_type_id", list.get(position).getService_type_id());
                intent.putExtra("type_num", list.get(position).getService_type_id());
                intent.putExtra("orderid", list.get(position).getOrderid());
                intent.putExtra("type", list.get(position).getService_content());
                intent.putExtra("time_start", list.get(position).getService_time1());
                intent.putExtra("user_name", list.get(position).getUser_name());
                intent.putExtra("place", list.get(position).getPlace());
                intent.putExtra("price", list.get(position).getTotal_money());
                intent.putExtra("areaid", list.get(position).getAreaid());
                List<mlist_pay> list_item = new ArrayList<mlist_pay>();
                mlist_pay item = new mlist_pay();
                item.setPrice(list.get(position).getTotal_money());
                item.setProject(list.get(position).getService_content());
                list_item.add(item);
                intent.putExtra("list", (Serializable) list_item);

                context.startActivity(intent);
            }
        });
        if (!list.get(position).getTrialorder().equals("1")) {
            if (Integer.parseInt(list.get(position).getService_type_id()) > 8) {
                zongjia.setVisibility(View.INVISIBLE);
            }
        }

        if (overtime_flag) {
            if (list.get(position).getStatus3().equals("3")) {
                status_2.setVisibility(View.VISIBLE);
                status_2.setText("对方拒绝加时");
            }
            if (list.get(position).getStatus3().equals("2")) {
                status_2.setVisibility(View.VISIBLE);
                status_2.setText("对方同意加时");
            } else {
                cancel_btn.setVisibility(View.VISIBLE);
                cancel_btn.setText("加时");
                cancel_btn.setTextColor(context.getResources().getColor(R.color.main_green));
                cancel_btn.setBackground(context.getResources().getDrawable(R.drawable.biankuang_ne_gre));
                cancel_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder;
                        builder = new AlertDialog.Builder(context);
                        final AlertDialog alert = builder.create();
                        View view = LayoutInflater.from(context).inflate(R.layout.jiashidialog, null);
                        final Spinner spinner = (Spinner) view.findViewById(R.id.canuptime);
                        String[] m = new String[6];
                        m[0] = 0.5 + "小时";
                        m[1] = 1 + "小时";
                        m[2] = 1.5 + "小时";
                        m[3] = 2 + "小时";
                        m[4] = 2.5 + "小时";
                        m[5] = 3 + "小时";
                        final ArrayAdapter<String> adapter_spinner1 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, m);
                        adapter_spinner1.setDropDownViewResource(android.R.layout.simple_spinner_item);

                        spinner.setAdapter(adapter_spinner1);
                        //确定
                        view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                System.out.println("点击了确定");
                                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                String url = RetrofitUtil.url_overtime;
                                RequestParams requestParams = new RequestParams();
                                requestParams.put("orderid", list.get(position).getOrderid());
                                requestParams.put("overtime", (spinner.getSelectedItemId() + 1) * 0.5);
                                requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                                requestParams.put("token", AyiApplication.getInstance().accountService().token());
                                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);
                                        try {
                                            System.out.println("resp9onse" + response);
                                            if (response.getString("ret").equals("200") && response.getJSONObject("data").getString("status").equals("1")) {
                                                Show_toast.showText(context, response.getJSONObject("data").getString("msg"));
                                                list.get(position).setStatus3("1");
                                                notifyDataSetChanged();
                                            } else {
                                                Show_toast.showText(context, response.getJSONObject("data").getString("msg"));
                                            }

                                            alert.dismiss();
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                        super.onFailure(statusCode, headers, throwable, errorResponse);
                                        Show_toast.showText(context, "加时预约提交失败");
                                        alert.dismiss();
                                    }
                                });

                            }
                        });
                        //取消
                        view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alert.dismiss();
                                System.out.println("点击了取消");
                            }
                        });


                        alert.setView(view);
                        alert.show();


                    }
                });
            }


        }

        if (overtime_flag2) {


            status_2.setVisibility(View.VISIBLE);
            status_2.setText("等待对方确认");
            cancel_btn.setVisibility(View.VISIBLE);
            cancel_btn.setText("取消加时");
            cancel_btn.setTextColor(context.getResources().getColor(R.color.main_green));
            cancel_btn.setBackground(context.getResources().getDrawable(R.drawable.biankuang_ne_gre));
            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(context);
                    final AlertDialog alert = builder.create();
                    View view = LayoutInflater.from(context).inflate(R.layout.cancel_overtime, null);
                    //确定
                    view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            System.out.println("点击了确定");
                            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                            String url = RetrofitUtil.url_calcelovertime;
                            RequestParams requestParams = new RequestParams();
                            requestParams.put("orderid", list.get(position).getOrderid());
                            requestParams.put("val", 4);
                            requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                            requestParams.put("token", AyiApplication.getInstance().accountService().token());
                            asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    try {
                                        System.out.println("resp11onse" + response);
                                        if (response.getString("ret").equals("200") && response.getJSONObject("data").getString("status").equals("1")) {
                                            Show_toast.showText(context, response.getJSONObject("data").getString("msg"));
                                            list.get(position).setStatus3("4");
                                            notifyDataSetChanged();
                                        } else {
                                            Show_toast.showText(context, response.getJSONObject("data").getString("msg"));
                                        }


                                        alert.dismiss();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                    super.onFailure(statusCode, headers, throwable, errorResponse);
                                    Show_toast.showText(context, "加时预约取消失败");
                                    alert.dismiss();
                                }
                            });

                        }
                    });
                    //取消
                    view.findViewById(R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                            System.out.println("点击了取消");
                        }
                    });


                    alert.setView(view);
                    alert.show();


                }
            });


        }
        if (!list.get(position).getParentoid().equals("-1") && list.get(position).getStatus3().equals("5")) {
            go_pay.setVisibility(View.GONE);
            status_2.setVisibility(View.VISIBLE);
            status_2.setText("阿姨已取消订单");
        }

        return view;
    }

}
