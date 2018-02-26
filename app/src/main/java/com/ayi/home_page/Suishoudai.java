package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.adapter.suishoudai_adapter;
import com.ayi.entity.item_suishoudai;
import com.ayi.entity.suishoudai_guodu;
import com.ayi.retrofit.RetrofitUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/10/17.
 */
public class Suishoudai extends Activity {
    private List<item_suishoudai> list;
    private View kongbai;
    private double screenWidth;
    private View progressBar1;

    GridView gridview;
    suishoudai_adapter adapter;
    private View button_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.suishoudai);
        //得到频宽；
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;
        init();
        init_wangluo();
        init_ok();
        init_back();


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
                JSONArray jsonArray=new JSONArray();
                List<suishoudai_guodu> list_ceshi=new ArrayList<suishoudai_guodu>();
                for (int i=0;i<list.size();i++){
                    if(!list.get(i).getNum().equals("0")){
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("mid",list.get(i).getId());
                            jsonObject.put("quantity",list.get(i).getNum());
                            jsonArray.put(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        suishoudai_guodu item=new suishoudai_guodu();
                        item.setName(list.get(i).getName());
                        item.setNum(list.get(i).getNum());
                        item.setId(list.get(i).getId());
                        list_ceshi.add(item);
                    }
                }
                Intent intent=new Intent(Suishoudai.this,Home_zdg_ok.class);

                intent.putExtra("flag_suishou", "true");
                intent.putExtra("flag_suishoudai", (Serializable) list_ceshi);
                intent.putExtra("flag_object", jsonArray.toString());

                startActivity(intent);
            }
        });


    }
    List<View> list_view;

    private void init_wangluo() {
        progressBar1.setVisibility(View.VISIBLE);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.setTimeout(20000);
        String url= RetrofitUtil.url_suishoudai;
        RequestParams requestParams=new RequestParams();
        requestParams.put("typeid",getIntent().getStringExtra("typeid"));
        requestParams.put("areaid", AyiApplication.area_id);
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token",AyiApplication.getInstance().accountService().token());
        System.out.println(getIntent().getStringExtra("typeid")+","+AyiApplication.area_id+","+AyiApplication.getInstance().accountService().id()+","+AyiApplication.getInstance().accountService().token());
        asyncHttpClient.post(url,requestParams,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonObject) {
                super.onSuccess(statusCode, headers, jsonObject);
                try {
                    progressBar1.setVisibility(View.GONE);
                    KLog.d("随手带",jsonObject.toString());
                    JSONArray jsonArray_data=jsonObject.getJSONArray("data");
                    if(jsonArray_data.length()==0){
                        kongbai.setVisibility(View.VISIBLE);
                    }else {
                        kongbai.setVisibility(View.GONE);
                    }
                    for (int i=0;i<jsonArray_data.length();i++){
                        item_suishoudai item=new item_suishoudai();
                        item.setSpec(jsonArray_data.getJSONObject(i).getString("spec"));
                        item.setId(jsonArray_data.getJSONObject(i).getString("id"));
                        item.setDetail(jsonArray_data.getJSONObject(i).getString("detail"));
                        item.setPrice(jsonArray_data.getJSONObject(i).getString("price"));
                        item.setFlagtext(jsonArray_data.getJSONObject(i).getString("flagtext"));
                        item.setBig_img(jsonArray_data.getJSONObject(i).getString("big_img"));
                        item.setName(jsonArray_data.getJSONObject(i).getString("name"));
                        item.setQuality_time(jsonArray_data.getJSONObject(i).getString("quality_time"));
                        item.setSmall_img(jsonArray_data.getJSONObject(i).getString("small_img"));
                        list.add(item);
                    }

                    if (huancun!=null){
                        System.out.println("huanc"+huancun);
                        System.out.println("huanc：list---"+list);
                        for (int i=0;i<huancun.size();i++){

                            System.out.println("huanc:几次"+huancun.size()+","+huancun.get(i));
                            for (int j=0;j<list.size();j++){
                                if(huancun.get(i).getId().equals(list.get(j).getId())){
                                    System.out.println("相同一次");
                                    list.get(j).setNum(huancun.get(i).getNum());
                                    break;
                                }
                            }
                        }
                    }
                    adapter=new suishoudai_adapter(list,Suishoudai.this);
                    gridview.setAdapter(adapter);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    List<suishoudai_guodu> huancun;
    private void init() {
        button_btn=findViewById(R.id.button_btn);
        progressBar1=findViewById(R.id.progressBar1);
        kongbai=findViewById(R.id.kongbai);
        if(getIntent().getSerializableExtra("suishou")!=null){
            huancun= (List<suishoudai_guodu>) getIntent().getSerializableExtra("suishou");
        }
        list_view=new ArrayList<>();
        list=new ArrayList<>();
        gridview= (GridView) findViewById(R.id.gridview);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.getStringExtra("num")!=null){
            for (int i=0;i<list.size();i++){
                if(i==Integer.valueOf(intent.getStringExtra("num"))){
                    list.get(i).setNum(intent.getStringExtra("duoshao"));
                    break;
                }

            }
            adapter.notifyDataSetChanged();


        }
    }
}
