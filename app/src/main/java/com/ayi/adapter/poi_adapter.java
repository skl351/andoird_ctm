package com.ayi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ayi.R;
import com.ayi.entity.item_map;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class poi_adapter extends BaseAdapter {

    private List<item_map> list;
    private Context context;
    public poi_adapter(List<item_map> list, Context context){
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= LayoutInflater.from(context).inflate(R.layout.item_list,null);
        TextView nae= (TextView) convertView.findViewById(R.id.text1);
        TextView nae2= (TextView) convertView.findViewById(R.id.text2);
        nae.setText(list.get(position).getName());
        nae2.setText(list.get(position).getAddress());

        return convertView;
    }
}
