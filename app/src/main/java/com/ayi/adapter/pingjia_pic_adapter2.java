package com.ayi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ayi.R;
import com.ayi.order_four.Big_pic;
import com.ayi.order_four.Pingjia_jiemian;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.Serializable;
import java.util.List;


/**
 * Created by Administrator on 2016/8/30.
 */
public class pingjia_pic_adapter2 extends BaseAdapter {

    private List<String> list;
    Context context;
    public pingjia_pic_adapter2(Context context, List<String> list){
        this.list=list;
        this.context=context;
    }
    @Override
    public int getCount() {
        return list.size()+1;
    }

    @Override
    public Object getItem(int position) {
        if (list.size()==position){
            return null;
        }

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    private View view;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Pingjia_jiemian pj= (Pingjia_jiemian) context;
        //直接编辑
        view= LayoutInflater.from(context).inflate(R.layout.pingjia_pic_view, null);
        View pingjia_pic_img=view.findViewById(R.id.pingjia_pic_img);
        ImageView pic= (ImageView) view.findViewById(R.id.pingjia_pic_img);
        View click_pic_delete_big= view.findViewById(R.id.click_pic_delete_big);
        click_pic_delete_big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();

            }
        });
        pingjia_pic_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转一个viewpager
                Intent intent = new Intent(context, Big_pic.class);
                intent.putExtra("imgs", (Serializable) list);
                intent.putExtra("flag", position);
                context.startActivity(intent);
            }
        });
        if (position==list.size()){

            pic.setBackgroundResource(R.mipmap.pingjia_addpic);
            click_pic_delete_big.setVisibility(View.GONE);

            pingjia_pic_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    pj.xx();
                }
            });

            return view;
        }

        ImageLoader.getInstance().displayImage("file:/"+list.get(position),pic);

        return view;
    }


}
