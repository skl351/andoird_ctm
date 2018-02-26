package com.ayi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ayi.R;
import com.ayi.entity.item_ayi_list;
import com.ayi.home_page.Ayi_list_2_new;
import com.ayi.home_page.Ayi_list_item;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2016/8/30.
 */
public class Ayi_list_adapter_2 extends BaseAdapter {
    private int index = -1;
    private List<item_ayi_list> list;
    Context context;
    public Ayi_list_adapter_2(Context context, List<item_ayi_list> list){
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
        index= Ayi_list_2_new.index_flag;
        view= LayoutInflater.from(context).inflate(R.layout.item_ayi_list2, null);
        ImageView headimg= (ImageView) view.findViewById(R.id.headimg);
        TextView name= (TextView) view.findViewById(R.id.ayi_name);
        TextView ayi_old= (TextView) view.findViewById(R.id.ayi_old);
        TextView ayi_place= (TextView) view.findViewById(R.id.ayi_place);
        TextView service_times= (TextView) view.findViewById(R.id.service_times);
        RadioButton select_img= (RadioButton) view.findViewById(R.id.select_img);
        TextView ayi_money= (TextView) view.findViewById(R.id.ayi_money);
        select_img.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    index = position;
                    Ayi_list_2_new.index_flag=index;
                    notifyDataSetChanged();
                }
            }
        });
        if (index == position) {// 选中的条目和当前的条目是否相等
            select_img.setChecked(true);
        } else {
            select_img.setChecked(false);
        }
        ayi_money.setText( list.get(position).getOut_price());
        ImageLoader.getInstance().displayImage(list.get(position).getImg_head(),headimg);
        name.setText(list.get(position).getName());
        ayi_old.setText(list.get(position).getOld());
        ayi_place.setText(list.get(position).getPlace());
        service_times.setText(list.get(position).getTimes());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Ayi_list_item.class);
                intent.putExtra("cleaner_id",list.get(position).getId());
                Activity a=(Activity)context;
                a.startActivityForResult(intent,1);
            }
        });

        return view;
    }
}
