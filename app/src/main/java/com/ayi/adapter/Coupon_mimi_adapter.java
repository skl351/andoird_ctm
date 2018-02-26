package com.ayi.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayi.R;
import com.ayi.entity.item_coupon;
import com.ayi.home_page.Coupon_delite;
import com.ayi.utils.Data_time_cuo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class Coupon_mimi_adapter extends BaseAdapter {

    private List<item_coupon> list;
    Context context;
    public Coupon_mimi_adapter(Context context, List<item_coupon> list){
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
        view= LayoutInflater.from(context).inflate(R.layout.item_coupon, null);
        View xuyaoxianshi=view.findViewById(R.id.xuyaoxianshi);
        TextView use_tiaojian= (TextView) view.findViewById(R.id.use_tiaojian);
        TextView price= (TextView) view.findViewById(R.id.price);

        TextView time= (TextView) view.findViewById(R.id.time);
        TextView area= (TextView) view.findViewById(R.id.area);
        area.setText(list.get(position).getPlace());
        ImageView status= (ImageView) view.findViewById(R.id.status);
        price.setText("￥"+list.get(position).getPrice());
        time.setText(Data_time_cuo.gettime2(list.get(position).getTime_finish()));
//       String[] ty=list.get(position).getTypeids().split("\\|");
        String zong_type="";
//        for (int i=0;i<ty.length;i++){
//            switch (ty[i]){
//                case "5":zong_type=zong_type+"日常保洁";break;
//                case "6":zong_type=zong_type+"清洗油烟机";break;
//                case "7":zong_type=zong_type+"擦玻璃";break;
//                case "8":zong_type=zong_type+"开荒";break;
//                case "9":zong_type=zong_type+"日常保洁（长期）";break;
//                case "10":zong_type=zong_type+"做饭";break;
//                case "11":zong_type=zong_type+"日常保洁+做饭";break;
//                case "12":zong_type=zong_type+"生活起居";break;
//                case "13":zong_type=zong_type+"照顾老人";break;
//                case "14":zong_type=zong_type+"月嫂";break;
//                case "15":zong_type=zong_type+"育儿嫂";break;
//
//            }
//            zong_type=zong_type+",";
//        }
        zong_type= list.get(position).getTypenames();
        use_tiaojian.setText(zong_type);

        switch (list.get(position).getStatus()){
           case "0":
               status.setVisibility(View.VISIBLE);
               xuyaoxianshi.setVisibility(View.VISIBLE);
               status.setBackgroundResource(R.mipmap.yishiyong);//已使用
               break;
           case "1":break;
           case "2":
               xuyaoxianshi.setVisibility(View.VISIBLE);
               status.setVisibility(View.VISIBLE);//已过期
               status.setBackgroundResource(R.mipmap.yiguoqi)
               ;break;
       }
        final String finalZong_type = zong_type;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Coupon_delite.class);
                intent.putExtra("use_place",list.get(position).getPlace());
                intent.putExtra("use_money",list.get(position).getPrice());
                intent.putExtra("use_date",list.get(position).getTime_finish());
                intent.putExtra("use_type", finalZong_type);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
