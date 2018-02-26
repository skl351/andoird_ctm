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
import com.ayi.entity.Place_item;
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
public class more_city_adapter extends BaseAdapter {

    private List<Place_item> list;
    Context context;
    public more_city_adapter(Context context, List<Place_item> list){
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
        view= LayoutInflater.from(context).inflate(R.layout.item_city_more, null);
        TextView text= (TextView) view.findViewById(R.id.more_text);
        text.setText(list.get(position).getArea_name());
        return view;
    }
}
