package com.ayi.home_page;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.utils.Data_time_cuo;
import com.ayi.utils.Show_toast;
import com.ayi.zidingyi_view.AmountView;
import com.ayi.zidingyi_view.DateTimePickDialogUtil;
import com.socks.library.KLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Administrator on 2016/10/19.
 */
public class Other_last2_time_select extends Activity {
    private TextView show_long;

    private View select_time_big;
    private TextView select_time;
    private View button_btn;
    private AmountView select_data;
    private View center_content_bm;
    private View center_content_yes;
    private View center_content_ys;
    private View center_content_zglr;

    private String typw_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.other_last2_time_select);
        typw_num=getIntent().getStringExtra("type_num");
        center_content_bm=findViewById(R.id.center_content_bm);
        center_content_yes=findViewById(R.id.center_content_yes);
        center_content_ys=findViewById(R.id.center_content_ys);
        center_content_zglr=findViewById(R.id.center_content_zglr);
       switch (typw_num){
           case "12":
               center_content_bm.setVisibility(View.VISIBLE);  break;
           case "13":
               center_content_zglr.setVisibility(View.VISIBLE);
               break;

           case "14":
               center_content_ys.setVisibility(View.VISIBLE);
               break;
           case "15":
               center_content_yes.setVisibility(View.VISIBLE);
               break;


       }
        init();
        init_ok();
        init_back();
        init_time_select_click();
    }

    private void init_back() {
        findViewById(R.id.top).findViewById(R.id.logreg_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init_ok() {
        button_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (select_time.getText().toString().equals("")) {
                    Toast.makeText(Other_last2_time_select.this, "请选择时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                //新年
                long time_1= Long.parseLong(Data_time_cuo.date2TimeStamp(select_time.getText().toString(), "yyyy-MM-dd"));
                System.out.println("----"+time_1);
                if(time_1>= AyiApplication.time1&&time_1<AyiApplication.time2){
                    AlertDialog.Builder builder;
                    builder = new AlertDialog.Builder(Other_last2_time_select.this);
                    final AlertDialog alert = builder.create();
                    View view = LayoutInflater.from(Other_last2_time_select.this).inflate(R.layout.newyearalert, null);
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

                if (getIntent().getStringExtra("flag").equals("ys")) {
                    Intent intent = new Intent(Other_last2_time_select.this, Home_yuesao_ok.class);
                    intent.putExtra("biaozhi1", select_time.getText().toString());
                    intent.putExtra("biaozhi2", flag_num);
                    System.out.println("biaozhi" + select_time.getText().toString() + "," + flag_num);
                    intent.putExtra("yuyue_time_string", select_time.getText().toString());
                    intent.putExtra("time_start", Data_time_cuo.date2TimeStamp(select_time.getText().toString(), "yyyy-MM-dd"));
                    intent.putExtra("period", "" + (flag_num));
                    startActivity(intent);
                    KLog.e("yuyue_time_string:" + select_time.getText().toString() + "time_start:" + Data_time_cuo.date2TimeStamp(select_time.getText().toString(), "yyyy-MM-dd") + "period" + flag_num);
                }

                if (getIntent().getStringExtra("flag").equals("bm")) {
                    Intent intent = new Intent(Other_last2_time_select.this, Home_baomu_ok.class);
                    intent.putExtra("biaozhi1", select_time.getText().toString());
                    intent.putExtra("biaozhi2", flag_num);
                    System.out.println("biaozhi" + select_time.getText().toString() + "," + flag_num);
                    intent.putExtra("yuyue_time_string", select_time.getText().toString());
                    intent.putExtra("time_start", Data_time_cuo.date2TimeStamp(select_time.getText().toString(), "yyyy-MM-dd"));
                    intent.putExtra("period", "" + (flag_num));
                    startActivity(intent);
                    KLog.e("yuyue_time_string:" + select_time.getText().toString() + "time_start:" + Data_time_cuo.date2TimeStamp(select_time.getText().toString(), "yyyy-MM-dd") + "period" + flag_num);
                }

            }
        });

    }
    private int flag_num=1;

    private void init() {

        show_long= (TextView) findViewById(R.id.show_long);
        button_btn = findViewById(R.id.button_btn);

        select_time_big = findViewById(R.id.select_time_big);
        select_time = (TextView) findViewById(R.id.select_time);

        String weiz=typw_num.equals("14")?"周":"月";
        show_long.setText(weiz);
        System.out.println("biaozhi1" + getIntent().getStringExtra("biaozhi1") + "," + Integer.valueOf(getIntent().getIntExtra("biaozhi2", 1)));
        select_time.setText(getIntent().getStringExtra("biaozhi1"));

        select_data = (AmountView) findViewById(R.id.select_data);
        select_data.setGoods_storage(12);
        select_data.setOnAmountChangeListener(new AmountView.OnAmountChangeListener() {
            @Override
            public void onAmountChange(View view, int amount) {
                flag_num=amount;
            }
        });

    }
    private void init_time_select_click() {
        select_time_big.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                if(select_time.getText().toString().equals("")){
                    Date date=new Date();//取时间
                    Calendar calendar   =   new GregorianCalendar();
                    calendar.setTime(date);
                    calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
                    date=calendar.getTime();   //这个时间就是日期往后推一天的结果
                    System.out.println("看看1"+sdf.format(new java.util.Date()));
                    DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                            Other_last2_time_select.this, sdf.format(date));
                    dateTimePicKDialog.dateTimePicKDialog(select_time);
                }else {
                    System.out.println("看看2"+select_time.getText().toString());
                    DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                            Other_last2_time_select.this, select_time.getText().toString());
                    dateTimePicKDialog.dateTimePicKDialog(select_time);
                }

            }
        });
    }
}
