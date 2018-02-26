package com.ayi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ayi.R;
import com.ayi.order_four.Big_pic2;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2016/8/30.
 */
public class pingjia_pic_adapter extends BaseAdapter {

    private List<String> list;
    Context context;

    public pingjia_pic_adapter(Context context, List<String> list) {
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //直接显示
        view = LayoutInflater.from(context).inflate(R.layout.pingjia_pic_view, null);
        View pingjia_pic_img = view.findViewById(R.id.pingjia_pic_img);
        ImageView pic = (ImageView) view.findViewById(R.id.pingjia_pic_img);
        View click_pic_delete_big = view.findViewById(R.id.click_pic_delete_big);
        click_pic_delete_big.setVisibility(View.GONE);
        pingjia_pic_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转一个viewpager
                Intent intent = new Intent(context, Big_pic2.class);
                intent.putExtra("imgs", (Serializable) list);
                System.out.println("position"+position);
                intent.putExtra("flag", position);
                context.startActivity(intent);
            }
        });
        ImageLoader.getInstance().displayImage(list.get(position), pic);

        return view;
    }
}
