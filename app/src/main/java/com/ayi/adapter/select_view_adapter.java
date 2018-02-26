package com.ayi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ayi.R;
import com.ayi.entity.time_select_item;
import com.ayi.home_page.Zdg_time_select;
import com.ayi.utils.Data_time_cuo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class select_view_adapter extends BaseAdapter {
    private int selectedPosition=-1;
    private List<time_select_item> list;
    private Context context;
    public select_view_adapter(List<time_select_item> list, Context context){
        this.list=list;
        this.context=context;
    }

    private boolean flag=false;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {
        flag=false;
        convertView= LayoutInflater.from(context).inflate(R.layout.select_view_item,null);
        View all_re=convertView.findViewById(R.id.all_re);
        TextView time= (TextView) convertView.findViewById(R.id.time);
        TextView status= (TextView) convertView.findViewById(R.id.status);

        time.setText(list.get(position).getTime());
        status.setText(flag_status(list.get(position).getFull(),time,status,list.get(position).getStart_unix_time(),all_re));

        if(selectedPosition==position){
            Zdg_time_select.yuyue_time_string= Data_time_cuo.gettime2(list.get(position).getStart_unix_time())+"("+formatweek(list.get(position).getWeekday())+")"+list.get(position).getTime();
            Zdg_time_select.time_start_unix=list.get(position).getStart_unix_time();
            Zdg_time_select.time_finish_unix=list.get(position).getFinish_unix_time();
            if (flag){

                all_re.setBackgroundResource(R.drawable.all_week_green);
                time.setTextColor(context.getResources().getColor(R.color.white));
                status.setTextColor(context.getResources().getColor(R.color.white));

            }else {
                Zdg_time_select.yuyue_time_string="";
                all_re.setBackgroundResource(R.drawable.border_week);
            }

        }else{
            all_re.setBackgroundResource(R.drawable.border_week);

        }

        return convertView;
    }
    String flag_status(String l, TextView a, TextView b, String t, View v){
        String return_str="";
        switch (l){
            case "1":return_str="已约满";
                a.setTextColor(context.getResources().getColor(R.color.luebai));
                b.setTextColor(context.getResources().getColor(R.color.luebai));
                flag=false;
                break;
            case "0":
                if(Long.parseLong(t)< System.currentTimeMillis()/1000){
                    return_str="已过期";
                    a.setTextColor(context.getResources().getColor(R.color.luebai));
                    b.setTextColor(context.getResources().getColor(R.color.luebai));
                    flag=false;
                    break;
                }
                return_str="可约";
                a.setTextColor(context.getResources().getColor(R.color.main_green));
                b.setTextColor(context.getResources().getColor(R.color.main_green));
                v.setBackgroundResource(R.drawable.border_week_green);
                flag=true;
                break;
            default:return_str="";break;
        }
        return return_str;

    }

    public  int getselect(){
        return selectedPosition;
    }
    public void clearselect(int position) {
        selectedPosition = position;
    }
    private String formatweek(String weekday) {
        String str="";
        switch (weekday){
            case "0":str="星期日";
                break;
            case "1":str="星期一";
                break;
            case "2":str="星期二";
                break;
            case "3":str="星期三";
                break;
            case "4":str="星期四";
                break;
            case "5":str="星期五";
                break;
            case "6":str="星期六";
                break;

        }
        return str;
    }
}
