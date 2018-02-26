package com.ayi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ayi.R;
import com.ayi.activity.Voice_setting;
import com.ayi.entity.daily_net_info;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class custom_spinner_adapter extends ArrayAdapter {

    List<daily_net_info> m;
    Context context;

    public custom_spinner_adapter(Voice_setting voice_setting, int simple_spinner_item, List a) {
        super(voice_setting, simple_spinner_item, a);
        m = a;
        this.context = voice_setting;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView.inflate(context, R.layout.xialakuang, null);
        TextView text = (TextView) view.findViewById(R.id.text1);
        try {
            if (position == 0) {
                text.setText("请点击选择");
            } else {
                text.setText(Html.fromHtml(m.get(position).getSize() + "(<font color=\"#e38918\">￥" + m.get(position).getPrice() +
                        "</font>/" + m.get(position).getDur() + "小时)"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }
        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        try {
            if (position == 0) {
                tv.setText("请点击选择");
            } else {
                tv.setText(Html.fromHtml(m.get(position).getSize() + "(<font color=\"#e38918\">￥" + m.get(position).getPrice() +
                        "</font>/" + m.get(position).getDur() + "小时)"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
