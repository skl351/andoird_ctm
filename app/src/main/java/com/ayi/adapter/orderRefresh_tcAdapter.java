package com.ayi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.entity.item_order_tc;
import com.ayi.home_page.Order_pay_tc;
import com.ayi.retrofit.RetrofitUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.milk.base.R;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;


public class orderRefresh_tcAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //上拉加载更多
    public static final int PULLUP_LOAD_MORE = 0;
    //正在加载中
    public static final int LOADING_MORE = 1;
    //上拉加载更多状态-默认为0
    private int load_more_status = 0;
    private LayoutInflater mInflater;
    private List<item_order_tc> mTitles = null;
    private static final int TYPE_ITEM = 0;  //普通Item View
    private static final int TYPE_FOOTER = 1;  //顶部FootView
    public Context Mcontext;
    private MyItemClickListener mItemClickListener;

    public orderRefresh_tcAdapter(Context context, List<item_order_tc> list) {
        Mcontext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mTitles = list;
    }

    /**
     * item显示类型
     *
     * @param parent
     * @param viewType
     * @return
     */
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //进行判断显示类型，来创建返回不同的View
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(R.layout.taocan_item_view, parent, false);
            //这边可以做一些属性设置，甚至事件监听绑定
            //view.setBackgroundColor(Color.RED);
            ItemViewHolder itemViewHolder = new ItemViewHolder(view, Mcontext, mItemClickListener);
            return itemViewHolder;
        }
//        else if(viewType==TYPE_FOOTER){
//            View foot_view=mInflater.inflate(R.layout.recycler_load_more_layout,parent,false);
//            //这边可以做一些属性设置，甚至事件监听绑定
//            //view.setBackgroundColor(Color.RED);
//            FootViewHolder footViewHolder=new FootViewHolder(foot_view);
//            return footViewHolder;
//        }
        return null;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface MyItemClickListener {
        void onItemClick(View view, int postion);
    }

    /**
     * 数据的绑定显示
     *
     * @param holder
     * @param position
     */
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        KLog.e("运行了信息");
        ((ItemViewHolder) holder).status.setTextColor(Mcontext.getResources().getColor(R.color.order_gra));
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).bit_view.setVisibility(View.GONE);
            ((ItemViewHolder) holder).quxiao.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).padey.setVisibility(View.VISIBLE);
            ((ItemViewHolder) holder).order_number.setText(mTitles.get(position).getOrdernum());
            ((ItemViewHolder) holder).service_place.setText(mTitles.get(position).getContact_addr() + "," + mTitles.get(position).getContact_door());
            ((ItemViewHolder) holder).service_title.setText(mTitles.get(position).getTitle());
            ((ItemViewHolder) holder).service_time.setText(mTitles.get(position).getTime());
            if (mTitles.get(position).getIsvalet() == 1) {
                ((ItemViewHolder) holder).daikexiadan_id.setVisibility(View.VISIBLE);
            } else {
                ((ItemViewHolder) holder).daikexiadan_id.setVisibility(View.GONE);
            }

            switch (mTitles.get(position).getStatus()) {
                case "-2":
                    ((ItemViewHolder) holder).status.setText("已退款");
                    ((ItemViewHolder) holder).status.setTextColor(Mcontext.getResources().getColor(R.color.order_gra));
                    break;
                case "-1":
                    ((ItemViewHolder) holder).status.setText("已取消");
                    ((ItemViewHolder) holder).status.setTextColor(Mcontext.getResources().getColor(R.color.order_gra));
                    break;
                case "0":
                    if (mTitles.get(position).getPayed().equals("0")) {
                        ((ItemViewHolder) holder).status.setText("未支付");
                        ((ItemViewHolder) holder).status.setTextColor(Mcontext.getResources().getColor(R.color.yishouli));
                        ((ItemViewHolder) holder).bit_view.setVisibility(View.VISIBLE);
                        ((ItemViewHolder) holder).padey.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                asyncHttpClient.setTimeout(20000);
                                String url = RetrofitUtil.url_tc_dowmload;//得到套餐订单列表
                                RequestParams requestParams = new RequestParams();
                                requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                                requestParams.put("token", AyiApplication.getInstance().accountService().token());
                                requestParams.put("id", mTitles.get(position).getId());
                                requestParams.put("ordernum", mTitles.get(position).getOrdernum());
                                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                                        super.onSuccess(statusCode, headers, jsonObject);
                                        try {
                                            System.out.println("OrderServicePackage.getList" + jsonObject.toString());
                                            if ("200".equals(jsonObject.getString("ret"))) {
                                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                                Intent intent = new Intent(Mcontext, Order_pay_tc.class);
                                                intent.putExtra("contacts", jsonObject1.getString("contacts"));
                                                intent.putExtra("contact_phone", jsonObject1.getString("contact_phone"));
                                                intent.putExtra("contact_addr", jsonObject1.getString("contact_addr"));
                                                intent.putExtra("contact_door", jsonObject1.getString("contact_door"));
                                                intent.putExtra("price", jsonObject1.getString("price"));
                                                intent.putExtra("ccsp_title", jsonObject1.getString("ccsp_title"));
                                                intent.putExtra("ordernum", jsonObject1.getString("ordernum"));
                                                intent.putExtra("orderid", jsonObject1.getString("id"));
                                                intent.putExtra("type_id", jsonObject1.getString("service_type_id"));
                                                Mcontext.startActivity(intent);
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
                        });
                        ((ItemViewHolder) holder).quxiao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(Mcontext);
                                final AlertDialog alert = builder.create();
                                View view = LayoutInflater.from(Mcontext).inflate(com.ayi.R.layout.calcel_rejectokok, null);
                                view.findViewById(com.ayi.R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alert.dismiss();
                                    }
                                });
                                view.findViewById(com.ayi.R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                        String url = RetrofitUtil.url_cancel_submit;
                                        RequestParams requestParams = new RequestParams();
                                        requestParams.put("orderid", mTitles.get(position).getId());
                                        requestParams.put("ordernum", mTitles.get(position).getOrdernum());
                                        requestParams.put("childid", "-1");
                                        requestParams.put("msg", "");
                                        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                                        requestParams.put("token", AyiApplication.getInstance().accountService().token());
                                        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                super.onSuccess(statusCode, headers, response);
                                                try {
                                                    System.out.println("resp5onse" + response.toString());
                                                    if (response.getString("ret").equals("200") && response.getJSONObject("data").getString("status").equals("1")) {
                                                        mTitles.get(position).setStatus("-1");
                                                        notifyDataSetChanged();
                                                        alert.dismiss();
                                                    }
                                                    Toast.makeText(Mcontext, response.getJSONObject("data").getString("message").toString(), Toast.LENGTH_SHORT).show();
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

                                alert.setView(view);
                                alert.show();

                            }
                        });
                        break;
                    } else {

                        ((ItemViewHolder) holder).status.setText("待受理");
                        ((ItemViewHolder) holder).status.setTextColor(Mcontext.getResources().getColor(R.color.yishouli));
                        ((ItemViewHolder) holder).bit_view.setVisibility(View.VISIBLE);
                        ((ItemViewHolder) holder).padey.setVisibility(View.GONE);
                        ((ItemViewHolder) holder).quxiao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder;
                                builder = new AlertDialog.Builder(Mcontext);
                                final AlertDialog alert = builder.create();
                                View view = LayoutInflater.from(Mcontext).inflate(com.ayi.R.layout.calcel_rejectokok, null);
                                view.findViewById(com.ayi.R.id.cancel_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        alert.dismiss();
                                    }
                                });
                                view.findViewById(com.ayi.R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                        String url = RetrofitUtil.url_cancel_submit;
                                        RequestParams requestParams = new RequestParams();
                                        requestParams.put("orderid", mTitles.get(position).getId());
                                        requestParams.put("ordernum", mTitles.get(position).getOrdernum());
                                        requestParams.put("childid", "-1");
                                        requestParams.put("msg", "");
                                        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                                        requestParams.put("token", AyiApplication.getInstance().accountService().token());
                                        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                super.onSuccess(statusCode, headers, response);
                                                try {
                                                    System.out.println("resp5onse" + response.toString());
                                                    if (response.getString("ret").equals("200") && response.getJSONObject("data").getString("status").equals("1")) {
                                                        mTitles.get(position).setStatus("-2");
                                                        notifyDataSetChanged();
                                                        alert.dismiss();
                                                    }
                                                    Toast.makeText(Mcontext, response.getJSONObject("data").getString("message").toString(), Toast.LENGTH_SHORT).show();
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

                                alert.setView(view);
                                alert.show();


                            }
                        });
                        break;
                    }
                case "1":
                    ((ItemViewHolder) holder).status.setText("已受理");
                    ((ItemViewHolder) holder).status.setTextColor(Mcontext.getResources().getColor(R.color.daishouli));
                    if (mTitles.get(position).getPayed().equals("0")) {
                        ((ItemViewHolder) holder).bit_view.setVisibility(View.VISIBLE);
                        ((ItemViewHolder) holder).quxiao.setVisibility(View.GONE);
                        ((ItemViewHolder) holder).padey.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                asyncHttpClient.setTimeout(20000);
                                String url = RetrofitUtil.url_tc_dowmload;//得到套餐订单列表
                                RequestParams requestParams = new RequestParams();
                                requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                                requestParams.put("token", AyiApplication.getInstance().accountService().token());
                                requestParams.put("id", mTitles.get(position).getId());
                                requestParams.put("ordernum", mTitles.get(position).getOrdernum());
                                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                                        super.onSuccess(statusCode, headers, jsonObject);
                                        try {
                                            System.out.println("OrderServicePackage.getList" + jsonObject.toString());
                                            if ("200".equals(jsonObject.getString("ret"))) {
                                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                                Intent intent = new Intent(Mcontext, Order_pay_tc.class);
                                                intent.putExtra("contacts", jsonObject1.getString("contacts"));
                                                intent.putExtra("contact_phone", jsonObject1.getString("contact_phone"));
                                                intent.putExtra("contact_addr", jsonObject1.getString("contact_addr"));
                                                intent.putExtra("contact_door", jsonObject1.getString("contact_door"));
                                                intent.putExtra("price", jsonObject1.getString("price"));
                                                intent.putExtra("ccsp_title", jsonObject1.getString("ccsp_title"));
                                                intent.putExtra("ordernum", jsonObject1.getString("ordernum"));
                                                intent.putExtra("orderid", jsonObject1.getString("id"));
                                                intent.putExtra("type_id", jsonObject1.getString("service_type_id"));
                                                Mcontext.startActivity(intent);
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
                        });
                    }
                    break;


            }

        }

    }

    /**
     * 进行判断是普通Item视图还是FootView视图
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return TYPE_ITEM;
        // 最后一个item设置为footerView
//    if (position + 1 == getItemCount()) {
//                return TYPE_FOOTER;
//            } else {
//                return TYPE_ITEM;
//            }
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    //自定义的ViewHolder，持有每个Item的的所有界面元素
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView order_number;
        public TextView status;
        public TextView service_title;
        public TextView service_place;
        public TextView service_time;
        public View padey;
        public View quxiao;
        public View bit_view;
        public View daikexiadan_id;
        private Context mcontext;
        private MyItemClickListener mListener;

        public ItemViewHolder(View view, Context context, MyItemClickListener onlisten) {
            super(view);
            mcontext = context;
            mListener = onlisten;
            view.setOnClickListener(this);
            order_number = (TextView) view.findViewById(R.id.order_number);
            status = (TextView) view.findViewById(R.id.status);
            service_title = (TextView) view.findViewById(R.id.service_title);
            service_place = (TextView) view.findViewById(R.id.service_place);
            service_time = (TextView) view.findViewById(R.id.service_time);
            padey = view.findViewById(R.id.padey);
            quxiao = view.findViewById(R.id.quxiao);
            bit_view = view.findViewById(R.id.bit_view);
            daikexiadan_id = view.findViewById(R.id.daikexiadan_id);
        }

        @Override
        public void onClick(View v) {
//            System.out.println("点击了3"+getOldPosition()+","+getAdapterPosition()+","+getPosition()+","+getLayoutPosition());
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }

        }
    }


    /**
     * //上拉加载更多
     * PULLUP_LOAD_MORE=0;
     * //正在加载中
     * LOADING_MORE=1;
     * //加载完成已经没有更多数据了
     * NO_MORE_DATA=2;
     *
     * @param status
     */
    public void changeMoreStatus(int status) {
        load_more_status = status;
        notifyDataSetChanged();
    }
}
