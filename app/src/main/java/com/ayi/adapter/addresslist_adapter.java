package com.ayi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.entity.item_place_info;
import com.ayi.home_page.User_info;
import com.ayi.home_page.User_info_before;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.FileService;
import com.ayi.utils.Show_toast;
import com.ayi.zidingyi_view.Forhuitiao;
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

import static com.ayi.home_page.User_info_before.add_address_click;
import static com.ayi.home_page.User_info_before.flag_guanli;
import static com.ayi.home_page.User_info_before.guanli;

/**
 * Created by Administrator on 2016/8/30.
 */
public class addresslist_adapter extends BaseAdapter {

    private List<item_place_info> list;
    Context context;
    public addresslist_adapter(Context context, List<item_place_info> list){
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
        view= LayoutInflater.from(context).inflate(R.layout.item_placeaddress, null);
        view.setBackgroundResource(0);
        TextView jutizhi= (TextView) view.findViewById(R.id.jutizhi);
        TextView user_name= (TextView) view.findViewById(R.id.user_name);
        TextView phonee= (TextView) view.findViewById(R.id.phonee);
        TextView city= (TextView) view.findViewById(R.id.city);
        View yigetu=view.findViewById(R.id.yigetu);
        View lianggetu=view.findViewById(R.id.lianggetu);

        //判断是否在修改
        if(list.get(position).isFlag()){
            yigetu.setVisibility(View.VISIBLE);
            lianggetu.setVisibility(View.VISIBLE);
            yigetu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    final AlertDialog alert=builder.create();
                    View view = LayoutInflater.from(context).inflate(R.layout.add_delete_reason, null);
                    View ok=  view.findViewById(R.id.ok_btn);
                    View cancel_btn=  view.findViewById(R.id.cancel_btn);
                    cancel_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                        }
                    });
                    ok.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                            String url= RetrofitUtil.url_dele_addresslist;
                            RequestParams requestParams=new RequestParams();
                            requestParams.put("adid",list.get(position).getId());
                            requestParams.put("user_id",  AyiApplication.getInstance().accountService().id());
                            requestParams.put("token",  AyiApplication.getInstance().accountService().token());
                            System.out.println("user_id"+AyiApplication.getInstance().accountService().id()+"token"+ AyiApplication.getInstance().accountService().token());
                            asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                    super.onSuccess(statusCode, headers, response);
                                    System.out.println("response"+response);
                                    try {
                                        if(response.getString("ret").equals("200")&&response.getJSONObject("data").getString("status").equals("1")){
                                            Show_toast.showText(context,"删除地址成功");

//                                            FileService service = new FileService(context);
//                                            String info = "" + "~" + "" + "~" + "" + "~" + "" + "~" + "" + "~" + "" + "~" + AyiApplication.shiji_dizhi;
//                                            try {
//                                                service.saveToRom("user.txt", info);
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                            }

                                            AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                            String url= RetrofitUtil.url_addresslist;
                                            RequestParams requestParams=new RequestParams();
                                            requestParams.put("pagesize",  100);
                                            requestParams.put("currentpage",  1);
                                            requestParams.put("user_id",  AyiApplication.getInstance().accountService().id());
                                            requestParams.put("token",  AyiApplication.getInstance().accountService().token());
                                            System.out.println("user_id"+AyiApplication.getInstance().accountService().id()+"token"+ AyiApplication.getInstance().accountService().token());
                                            asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                                                @Override
                                                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                    super.onSuccess(statusCode, headers, response);
                                                    try {
                                                        System.out.println(response.toString());
                                                        if( response.getString("ret").equals("200")){
                                                            list.clear();
                                                            JSONObject jsonObject=response.getJSONObject("data");
                                                            JSONArray jsonArray= jsonObject.getJSONArray("list");
                                                            for (int i=0;i<jsonArray.length();i++){
                                                                item_place_info item=new item_place_info();
                                                                item.setName(jsonArray.getJSONObject(i).getString("name"));
                                                                item.setPhone(jsonArray.getJSONObject(i).getString("tel"));
                                                                item.setPlace(jsonArray.getJSONObject(i).getString("addr"));
                                                                item.setDoor(jsonArray.getJSONObject(i).getString("door"));
                                                                item.setLatitude(jsonArray.getJSONObject(i).getString("latitude"));
                                                                item.setLongitide(jsonArray.getJSONObject(i).getString("longitude"));
                                                                item.setId(jsonArray.getJSONObject(i).getString("id"));
                                                                item.setAreaname(jsonArray.getJSONObject(i).getString("areaname"));
                                                                if(!item.getAreaname().equals(AyiApplication.place)){
                                                                    item.setFlag_city(false);
                                                                }else {
                                                                    item.setFlag_city(true);
                                                                }
                                                                list.add(item);
                                                            }
                                                            if(list.size()>0){
                                                                List<item_place_info> list_zilei=new ArrayList<item_place_info>();
                                                                for (int i=0;i<list.size();i++){
                                                                    if (list.get(i).isFlag_city()){
                                                                        list_zilei.add(list.get(i));
                                                                    }
                                                                }
                                                                for (int i=0;i<list.size();i++){
                                                                    if (!list.get(i).isFlag_city()){
                                                                        list_zilei.add(list.get(i));
                                                                    }
                                                                }
//                                                                list.clear();
//                                                                list=list_zilei;

                                                                FileService service = new FileService(context);
                                                                String info = list.get(0).getName() + "~" + list.get(0).getPhone() + "~" + list.get(0).getPlace() + "~" + list.get(0).getDoor() + "~" + list.get(0).getLatitude() + "~" + list.get(0).getLongitide() + "~" + list.get(0).getAreaname();
                                                                try {
                                                                    System.out.println("saveToRom3"+list.get(0).getAreaname());
                                                                    service.saveToRom("user.txt", info);
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                            }else {
                                                                FileService service = new FileService(context);
                                                                try {
                                                                    service.saveToRom("user.txt", "");
                                                                } catch (Exception e) {
                                                                    e.printStackTrace();
                                                                }
                                                                guanli.setEnabled(false);
                                                            }
                                                            notifyDataSetChanged();
                                                            alert.dismiss();
                                                            guanli.setText("管理");
                                                            add_address_click.setVisibility(View.VISIBLE);
                                                            flag_guanli=!flag_guanli;
                                                            Forhuitiao d= (Forhuitiao) context;
                                                            d.doonejiekou();
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
            lianggetu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,User_info.class);
                    User_info_before user_info_before= (User_info_before) context;
                    intent.putExtra("flag_place",user_info_before.getIntent().getStringExtra("flag_place"));
                    intent.putExtra("name",list.get(position).getName());
                    intent.putExtra("phone",list.get(position).getPhone());
                    intent.putExtra("add1",list.get(position).getPlace());
                    intent.putExtra("add2",list.get(position).getDoor());
                    intent.putExtra("lat",list.get(position).getLatitude());
                    intent.putExtra("long",list.get(position).getLongitide());
                    intent.putExtra("adid",list.get(position).getId());
                    intent.putExtra("areaname",list.get(position).getAreaname());
                    context.startActivity(intent);
                }
            });
        }


        jutizhi.setText(list.get(position).getPlace()+","+list.get(position).getDoor());
        user_name.setText(list.get(position).getName());
        phonee.setText(list.get(position).getPhone());
        city.setText("["+list.get(position).getAreaname()+"]");
        if (!list.get(position).isFlag_city()){
            KLog.e(list.get(position).getAreaname()+"是透明");
//            view.setBackgroundResource(R.color.touming_color3);
            city.setTextColor(context.getResources().getColor(R.color.touming_color3));
            jutizhi.setTextColor(context.getResources().getColor(R.color.touming_color3));
            user_name.setTextColor(context.getResources().getColor(R.color.touming_color3));
            phonee.setTextColor(context.getResources().getColor(R.color.touming_color3));
        }
        return view;
    }
}
