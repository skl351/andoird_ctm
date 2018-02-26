package com.ayi.home_page;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.utils.Data_time_cuo;
import com.ayi.utils.Show_toast;
import com.ayi.zidingyi_view.DateTimePickDialogUtil;
import com.socks.library.KLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Administrator on 2016/10/16.
 */
public class Other_time_select extends Activity {

    private Spinner spin_zhouqi;
    private List<View> list_week;
    private View week1;
    private View week2;
    private View week3;
    private View week4;
    private View week5;
    private View week6;
    private View week7;
    private ArrayAdapter<String> adapter_spinner1;
    private View time_select;
    private TextView select_time;
    private String day_fre = "";
    private View tiaofu_6_01;
    private View button_btn;
    private TextView wenxintishi;
    private View tishi_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_time_select);
        init();
//        init_text();
        init_ok();
        init_week();
        init_time_select_click();
        init_back();

    }

//    private void init_text() {
//        String day_fre2="";
//        for (int i=0;i<list_week.size();i++){
//            if(list_week.get(i).isSelected()){
//                day_fre2=day_fre2+i+"|";
//            }
//        }
//        if(day_fre2.equals("")){
//            wenxintishi.setText("请选择服务频率");
//        }else {
//            day_fre=day_fre2.substring(0,day_fre2.length()-1);//得到一个参数
//
//            if(select_time.getText().toString().equals("")){
//                wenxintishi.setText("请选择开始时间");
//            }else {
//                tishi_content.setVisibility(View.VISIBLE);
//                wenxintishi.setVisibility(View.GONE);
//                day_start=Data_time_cuo.date2TimeStamp(select_time.getText().toString(),
//                        "yyyy-MM-dd");//开始时间--毫秒数
//            }
//
//        }
//
//    }

    private void init_back() {
        findViewById(R.id.top).findViewById(R.id.logreg_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private String day_start;
    private String day_finish;
    private String time_start;
    private String time_finish;
    private String yuyue_time_string;


    private void init_ok() {
        button_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String day_fre2 = "";
                for (int i = 0; i < list_week.size(); i++) {
                    if (list_week.get(i).isSelected()) {
                        day_fre2 = day_fre2 + i + "|";
                    }
                }
                if (day_fre2.equals("")) {
                    Toast.makeText(Other_time_select.this, "请选择服务频率", Toast.LENGTH_SHORT).show();
                    return;
                }
                day_fre = day_fre2.substring(0, day_fre2.length() - 1);//得到一个参数

                if (select_time.getText().toString().equals("")) {
                    Toast.makeText(Other_time_select.this, "请选择开始时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                //新年
                long time_1 = Long.parseLong(Data_time_cuo.date2TimeStamp(select_time.getText().toString(), "yyyy-MM-dd"));
                if (time_1 >= AyiApplication.time1 && time_1 < AyiApplication.time2) {
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(Other_time_select.this);
                    final AlertDialog alert = builder.create();
                    View view = LayoutInflater.from(Other_time_select.this).inflate(R.layout.newyearalert, null);
                    view.findViewById(R.id.ok_btn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.dismiss();
                        }
                    });
                    alert.setView(view);
                    alert.show();
                    return;
                }
                day_start = Data_time_cuo.date2TimeStamp(select_time.getText().toString(),
                        "yyyy-MM-dd");//开始时间--毫秒数

                 /*
                转换结束的时间戳
                 */
//                String zhouqi=  spin_zhouqi.getSelectedItem().toString().substring(0,1);//周期-月
//                String time=select_time.getText().toString();
//                String time_yue=time.split("-")[1];
//                String time_nian=time.split("-")[0];
//                String time_other=time.split("-")[2];
//                int yue= Integer.parseInt(time_yue)+Integer.parseInt(zhouqi);
//                if(yue>12){
//                    yue=yue-12;
//                    time_nian= String.valueOf(Integer.parseInt(time_nian)+1);
//                }
//                String zuizhong=time_nian+"-"+yue+"-"+time_other;


                time_start = Data_time_cuo.date2TimeStamp(select_time.getText().toString(), "yyyy-MM-dd");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String reStr = null;
                try {
                    Date dt = sdf.parse(select_time.getText().toString());
                    Calendar rightNow = Calendar.getInstance();
                    rightNow.setTime(dt);
                    rightNow.add(Calendar.MONTH, spin_zhouqi.getSelectedItemPosition() + 1);
                    rightNow.add(Calendar.DAY_OF_YEAR, -1);
                    Date dt1 = rightNow.getTime();
                    reStr = sdf.format(dt1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                time_finish = Data_time_cuo.date2TimeStamp(reStr, "yyyy-MM-dd");
                day_finish = Data_time_cuo.date2TimeStamp(reStr,
                        "yyyy-MM-dd");
                Intent intent = new Intent(Other_time_select.this, Home_zdg_cq_ok.class);
                intent.putExtra("time_biaozhi1", day_fre);
                intent.putExtra("time_biaozhi2", select_time.getText().toString());
                intent.putExtra("time_biaozhi3", spin_zhouqi.getSelectedItemPosition());
                intent.putExtra("time_finish", time_finish);
                intent.putExtra("time_start", time_start);
                intent.putExtra("day_start", day_start);
                intent.putExtra("day_finish", day_finish);
                intent.putExtra("day_fre", day_fre);
                intent.putExtra("yuyue_time_string", select_time.getText().toString());
                KLog.d("service_time", day_fre + "," + time_start + "," + time_finish + "," + day_start + "," + day_finish + "," + select_time.getText().toString() + "," + spin_zhouqi.getSelectedItemPosition());
                startActivity(intent);

            }
        });
    }

    //    private String initEndDateTime = "2016-10-1 12:00"; // 初始化结束时间
    private void init_time_select_click() {

        time_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if (select_time.getText().toString().equals("")) {
                    Date date = new Date();//取时间
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(date);
                    calendar.add(calendar.DATE, 1);//把日期往后增加一天.整数往后推,负数往前移动
                    date = calendar.getTime();   //这个时间就是日期往后推一天的结果
                    System.out.println("看看1" + sdf.format(new java.util.Date()));
                    DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                            Other_time_select.this, sdf.format(date));
                    dateTimePicKDialog.dateTimePicKDialog(select_time);
                } else {
                    System.out.println("看看2" + select_time.getText().toString());
                    DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                            Other_time_select.this, select_time.getText().toString());
                    dateTimePicKDialog.dateTimePicKDialog(select_time);
                }

            }
        });
    }


    private TextView time_start_view;
    private TextView time_end;
    private TextView time_stu;

    private void init() {
        time_start_view = (TextView) findViewById(R.id.time_start);
        time_end = (TextView) findViewById(R.id.time_end);
        time_stu = (TextView) findViewById(R.id.time_stu);
        tishi_content = findViewById(R.id.tishi_content);
        wenxintishi = (TextView) findViewById(R.id.wenxintishi);
        button_btn = findViewById(R.id.button_btn);
        tiaofu_6_01 = findViewById(R.id.tiaofu_6_01);
        if (Build.VERSION.SDK_INT >= 23) {
            tiaofu_6_01.setVisibility(View.VISIBLE);
        }
        select_time = (TextView) findViewById(R.id.select_time);
        select_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String day_fre2 = "";
                for (int i = 0; i < list_week.size(); i++) {
                    if (list_week.get(i).isSelected()) {
                        day_fre2 = day_fre2 + i + "|";
                    }
                }
                if (s.equals("")) {

                    if (day_fre2.equals("")) {
                        wenxintishi.setText("请选择服务频率");
                        wenxintishi.setVisibility(View.VISIBLE);
                        tishi_content.setVisibility(View.GONE);
                    } else {
                        wenxintishi.setText("请选择开始时间");
                        wenxintishi.setVisibility(View.VISIBLE);
                        tishi_content.setVisibility(View.GONE);
                    }


                } else {
                    if (day_fre2.equals("")) {
                        wenxintishi.setText("请选择服务频率");
                        wenxintishi.setVisibility(View.VISIBLE);
                        tishi_content.setVisibility(View.GONE);
                    } else {
                        tishi_content.setVisibility(View.VISIBLE);
                        wenxintishi.setVisibility(View.GONE);

                        day_fre = day_fre2.substring(0, day_fre2.length() - 1);//得到一个参数

                        String[] xx = day_fre.split("\\|");
                        String xxx = "";
                        for (int i = 0; i < xx.length; i++) {
                            switch (xx[i]) {
                                case "0":
                                    xxx = xxx + "周日,";
                                    break;
                                case "1":
                                    xxx = xxx + "周一,";
                                    break;
                                case "2":
                                    xxx = xxx + "周二,";
                                    break;
                                case "3":
                                    xxx = xxx + "周三,";
                                    break;
                                case "4":
                                    xxx = xxx + "周四,";
                                    break;
                                case "5":
                                    xxx = xxx + "周五,";
                                    break;
                                case "6":
                                    xxx = xxx + "周六,";
                                    break;
                            }
                        }
                        time_stu.setText(Html.fromHtml("每<font color=\"#e38918\">" + xxx.substring(0, xxx.length() - 1) + "</font>为您提供服务"));


                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String reStr = null;
                        try {
                            Date dt = sdf.parse(s.toString());
                            Calendar rightNow = Calendar.getInstance();
                            rightNow.setTime(dt);
                            rightNow.add(Calendar.MONTH, spin_zhouqi.getSelectedItemPosition() + 1);
                            rightNow.add(Calendar.DAY_OF_MONTH, -1);
                            Date dt1 = rightNow.getTime();
                            reStr = sdf.format(dt1);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        time_end.setText(reStr);
                        time_start_view.setText(s);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        time_select = findViewById(R.id.time_select);
        spin_zhouqi = (Spinner) findViewById(R.id.spin_zhouqi);
        String[] m = new String[6];
        for (int i = 0; i < 6; i++) {
            m[i] = "" + (i + 1) + "个月";
        }
        adapter_spinner1 = new ArrayAdapter<String>(Other_time_select.this, android.R.layout.simple_spinner_item, m);
        adapter_spinner1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_zhouqi.setAdapter(adapter_spinner1);
        spin_zhouqi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String reStr = null;
                try {
                    Date dt = sdf.parse(select_time.getText().toString());
                    Calendar rightNow = Calendar.getInstance();
                    rightNow.setTime(dt);
                    rightNow.add(Calendar.MONTH, spin_zhouqi.getSelectedItemPosition() + 1);
                    rightNow.add(Calendar.DAY_OF_MONTH, -1);
                    Date dt1 = rightNow.getTime();
                    reStr = sdf.format(dt1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                time_end.setText(reStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        list_week = new ArrayList<>();
        week1 = findViewById(R.id.week1);
        week2 = findViewById(R.id.week2);
        week3 = findViewById(R.id.week3);
        week4 = findViewById(R.id.week4);
        week5 = findViewById(R.id.week5);
        week6 = findViewById(R.id.week6);
        week7 = findViewById(R.id.week7);
        list_week.add(week7);
        list_week.add(week1);
        list_week.add(week2);
        list_week.add(week3);
        list_week.add(week4);
        list_week.add(week5);
        list_week.add(week6);
        if (getIntent().getStringExtra("time_biaozhi1") != null) {
            String[] xx = getIntent().getStringExtra("time_biaozhi1").split("\\|");
            for (int i = 0; i < xx.length; i++) {
                list_week.get(Integer.valueOf(xx[i])).setSelected(true);
            }
            select_time.setText(getIntent().getStringExtra("time_biaozhi2"));
            spin_zhouqi.setSelection(Integer.valueOf(getIntent().getIntExtra("time_biaozhi3", 0)));
        }

    }

    private void init_week() {
        for (int i = 0; i < list_week.size(); i++) {
            list_week.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (v.isSelected()) {
                        v.setSelected(false);
                        String day_fre2 = "";
                        for (int i = 0; i < list_week.size(); i++) {
                            if (list_week.get(i).isSelected()) {
                                day_fre2 = day_fre2 + i + "|";
                            }
                        }

                        if (day_fre2.equals("")) {
                            wenxintishi.setText("请选择服务频率");
                            wenxintishi.setVisibility(View.VISIBLE);
                            tishi_content.setVisibility(View.GONE);
                        } else {
                            if (select_time.getText().toString().equals("")) {
                                wenxintishi.setText("请选择开始时间");
                                wenxintishi.setVisibility(View.VISIBLE);
                                tishi_content.setVisibility(View.GONE);
                            } else {
                                wenxintishi.setVisibility(View.GONE);
                                tishi_content.setVisibility(View.VISIBLE);

                                day_fre = day_fre2.substring(0, day_fre2.length() - 1);//得到一个参数

                                String[] xx = day_fre.split("\\|");
                                String xxx = "";
                                for (int i = 0; i < xx.length; i++) {
                                    switch (xx[i]) {
                                        case "0":
                                            xxx = xxx + "周日,";
                                            break;
                                        case "1":
                                            xxx = xxx + "周一,";
                                            break;
                                        case "2":
                                            xxx = xxx + "周二,";
                                            break;
                                        case "3":
                                            xxx = xxx + "周三,";
                                            break;
                                        case "4":
                                            xxx = xxx + "周四,";
                                            break;
                                        case "5":
                                            xxx = xxx + "周五,";
                                            break;
                                        case "6":
                                            xxx = xxx + "周六,";
                                            break;
                                    }
                                }
                                time_stu.setText(Html.fromHtml("每<font color=\"#e38918\">" + xxx.substring(0, xxx.length() - 1) + "</font>为您提供服务"));


                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                String reStr = null;
                                try {
                                    Date dt = sdf.parse(select_time.getText().toString());
                                    Calendar rightNow = Calendar.getInstance();
                                    rightNow.setTime(dt);
                                    rightNow.add(Calendar.MONTH, spin_zhouqi.getSelectedItemPosition() + 1);
                                    rightNow.add(Calendar.DAY_OF_MONTH, -1);
                                    Date dt1 = rightNow.getTime();
                                    reStr = sdf.format(dt1);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                time_end.setText(reStr);
                                ;


                            }
                        }

                    } else {
                        v.setSelected(true);

                        String day_fre2 = "";
                        for (int i = 0; i < list_week.size(); i++) {
                            if (list_week.get(i).isSelected()) {
                                day_fre2 = day_fre2 + i + "|";
                            }
                        }
                        if (select_time.getText().toString().equals("")) {
                            wenxintishi.setText("请选择开始时间");
                            wenxintishi.setVisibility(View.VISIBLE);
                            tishi_content.setVisibility(View.GONE);
                        } else {
                            wenxintishi.setVisibility(View.GONE);
                            tishi_content.setVisibility(View.VISIBLE);

                            day_fre = day_fre2.substring(0, day_fre2.length() - 1);//得到一个参数

                            String[] xx = day_fre.split("\\|");
                            String xxx = "";
                            for (int i = 0; i < xx.length; i++) {
                                switch (xx[i]) {
                                    case "0":
                                        xxx = xxx + "周日,";
                                        break;
                                    case "1":
                                        xxx = xxx + "周一,";
                                        break;
                                    case "2":
                                        xxx = xxx + "周二,";
                                        break;
                                    case "3":
                                        xxx = xxx + "周三,";
                                        break;
                                    case "4":
                                        xxx = xxx + "周四,";
                                        break;
                                    case "5":
                                        xxx = xxx + "周五,";
                                        break;
                                    case "6":
                                        xxx = xxx + "周六,";
                                        break;
                                }
                            }

                            time_stu.setText(Html.fromHtml("每<font color=\"#e38918\">" + xxx.substring(0, xxx.length() - 1) + "</font>为您提供服务"));


                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String reStr = null;
                            try {
                                Date dt = sdf.parse(select_time.getText().toString());
                                Calendar rightNow = Calendar.getInstance();
                                rightNow.setTime(dt);
                                rightNow.add(Calendar.MONTH, spin_zhouqi.getSelectedItemPosition() + 1);
                                rightNow.add(Calendar.DAY_OF_MONTH, -1);
                                Date dt1 = rightNow.getTime();
                                reStr = sdf.format(dt1);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            time_end.setText(reStr);
                            time_start_view.setText(select_time.getText().toString());
                        }
                    }
                }
            });
        }
    }
}
