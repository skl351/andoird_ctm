package com.ayi.order_four;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.MainActivity;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Data_time_cuo;
import com.ayi.utils.Web_view_guyong;
import com.ayi.zidingyi_view.DateTimePickDialogUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import cz.msebera.android.httpclient.Header;

/**
 * 服务协议
 * Created by Administrator on 2016/9/18.
 */
public class service_info extends Activity {

    private TextView select_time;
    private Spinner select_data;
    private View top;
    private Button submit;
    private Button calcel;
    private View guyonghetong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_info_view);
        init();
        init_guyonghetong();
        init_back();
        init_time_select_click();
        init_ok();
        init_calcel();
    }
    private void init_guyonghetong() {
        guyonghetong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(service_info.this, Web_view_guyong.class);
                startActivity(intent);
            }
        });
    }
    private void init_calcel() {
        calcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init_ok() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit.setEnabled(false);
                if(select_time.getText().toString().isEmpty()){
                    Toast.makeText(service_info.this,"请选择时间",Toast.LENGTH_SHORT).show();
                    submit.setEnabled(true);
                    return;
                }

                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                String url= RetrofitUtil.url_cancel_shiyong;
                RequestParams requestParams=new RequestParams();
                requestParams.put("orderid", getIntent().getStringExtra("orderId"));
                requestParams.put("val", 7);
                requestParams.put("day_start", Data_time_cuo.date2TimeStamp(select_time.getText().toString(),"yyyy-MM-dd"));
                //这个是周
                if(getIntent().getStringExtra("yonglaipanduan").equals("14")){
                    String date=select_time.getText().toString();
                    String da1=date.split(" ")[0];//日期
                    int r=(select_data.getSelectedItemPosition()+1)*7;
                    try {
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        Date dt=sdf.parse(da1);
                        Calendar rightNow = Calendar.getInstance();
                        rightNow.setTime(dt);
                        rightNow.add(Calendar.DAY_OF_YEAR,r);
                        Date dt1=rightNow.getTime();
                        String reStr = sdf.format(dt1);
                        String zong=reStr;
                        requestParams.put("day_finish",Data_time_cuo.date2TimeStamp(zong,"yyyy-MM-dd"));

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }else {
                    //这个是月
                    String date=select_time.getText().toString();
                    String da1=date.split(" ")[0];//日期
                    int r=(select_data.getSelectedItemPosition()+1);
                    try {
                        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
                        Date dt=sdf.parse(da1);
                        Calendar rightNow = Calendar.getInstance();
                        rightNow.setTime(dt);
                        rightNow.add(Calendar.MONTH,r);
                        Date dt1=rightNow.getTime();
                        String reStr = sdf.format(dt1);
                        String zong=reStr;
                        requestParams.put("day_finish",Data_time_cuo.date2TimeStamp(zong,"yyyy-MM-dd"));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                requestParams.put("period",(select_data.getSelectedItemPosition()+1));
                requestParams.put("price", yueguyong_fee.getText().toString());
                requestParams.put("user_id",  AyiApplication.getInstance().accountService().id());
                requestParams.put("token",  AyiApplication.getInstance().accountService().token());
                asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        System.out.println(response);
                        Intent intent=new Intent(service_info.this, MainActivity.class);
                        intent.putExtra("tab","1");
                        intent.putExtra("for_refesh","1");
                        startActivity(intent);

                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);

                    }
                });


            }
        });
    }

    private void init_back() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private ArrayAdapter<String> adapter_spinner;
    private View back;

    private TextView leixing;
    private TextView lianxiren;
    private TextView lianxidianhua;
    private TextView fuwudizhi;
    private TextView fuwuayi;
    private TextView ayilianxidianhua;
    private TextView yueguyong_fee;
    private TextView baozhengj;
    private TextView service_text1;
    private TextView service_text4;
    private TextView guyongzhifufangshi;
    private void init() {
        guyonghetong=findViewById(R.id.guyonghetong);
        service_text1= (TextView) findViewById(R.id.service_text1);
        service_text4= (TextView) findViewById(R.id.service_text4);
        guyongzhifufangshi= (TextView) findViewById(R.id.guyongzhifufangshi);
        String[] m=new String[7];
        String weiz=getIntent().getStringExtra("yonglaipanduan").equals("14")?"周":"月";
        for (int i=0;i<7;i++){
            m[i]=""+(i+1)+weiz;
        }
        service_text1.setText("阿姨"+weiz+"雇用费");
        guyongzhifufangshi.setText("按"+weiz+"线上支付");
        service_text4.setText("等于一个"+weiz+"总费用");
        submit= (Button) findViewById(R.id.submit);
        calcel= (Button) findViewById(R.id.calcel);
        leixing= (TextView) findViewById(R.id.leixing);
        lianxiren= (TextView) findViewById(R.id.lianxiren);
        lianxidianhua= (TextView) findViewById(R.id.lianxidianhua);
        fuwudizhi= (TextView) findViewById(R.id.fuwudizhi);
        fuwuayi= (TextView) findViewById(R.id.fuwuayi);
        ayilianxidianhua= (TextView) findViewById(R.id.ayilianxidianhua);

        yueguyong_fee= (TextView) findViewById(R.id.yueguyong_fee);
        baozhengj= (TextView) findViewById(R.id.baozhengj);
        leixing.setText(getIntent().getStringExtra("type"));
        lianxiren.setText(getIntent().getStringExtra("user_name"));
        lianxidianhua.setText(getIntent().getStringExtra("phone"));
        fuwudizhi.setText(getIntent().getStringExtra("place"));
        fuwuayi.setText(getIntent().getStringExtra("ayi_name"));
        ayilianxidianhua.setText(getIntent().getStringExtra("phone2"));
        String price=getIntent().getStringExtra("price");
        yueguyong_fee.setText(price);
        DecimalFormat df   = new DecimalFormat("######0.00");
        baozhengj.setText(yueguyong_fee.getText().toString());
        top=findViewById(R.id.top);
        back=top.findViewById(R.id.logreg_left);
        select_data= (Spinner) findViewById(R.id.select_data);
        select_time= (TextView) findViewById(R.id.select_time);

        adapter_spinner=new ArrayAdapter<String>(service_info.this,android.R.layout.simple_spinner_item,m);
        adapter_spinner.setDropDownViewResource(android.R.layout.simple_spinner_item);
        select_data.setAdapter(adapter_spinner);

    }

    private void init_time_select_click() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        select_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date=new Date();//取时间
                Calendar calendar   =   new GregorianCalendar();
                calendar.setTime(date);
                calendar.add(calendar.DATE,1);//把日期往后增加一天.整数往后推,负数往前移动
                date=calendar.getTime();   //这个时间就是日期往后推一天的结果
                System.out.println("看看1"+sdf.format(new java.util.Date()));
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        service_info.this, sdf.format(date));
                dateTimePicKDialog.dateTimePicKDialog(select_time);
            }
        });
    }
}
