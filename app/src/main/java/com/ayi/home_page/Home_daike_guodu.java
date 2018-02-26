package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.utils.FileService;

/**
 * 正常订单
 * Created by Administrator on 2017/7/6.
 */

public class Home_daike_guodu extends Activity {
    private View back;
    private View daikexiadan;
    private View zijixiadan;
    private String service_id;
    private String title;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_daike_guodu);
        service_id = getIntent().getStringExtra("service_id");
        title = getIntent().getStringExtra("title");
        init();
        init_back();
        init_click();
    }

    private void init_click() {
        daikexiadan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                FileService service = new FileService(Home_daike_guodu.this);
                String info = "" + "~" + "" + "~" + "" + "~" + "" + "~" + "" + "~" + "" + "~" + "";
                try {
                    service.saveToRom("user2.txt", info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                switch (service_id) {
                    case "5":
                    case "6":
                    case "7":
                    case "8":
                        intent = new Intent(Home_daike_guodu.this, Home_zdg_ok.class);
                        intent.putExtra("service_id", service_id);
                        intent.putExtra("title", title);
                        intent.putExtra("flag", true);
                        intent.putExtra("need_hidden", true);
                        startActivity(intent);
                        break;
                    case "9":
                    case "10":
                    case "11":
                        intent = new Intent(Home_daike_guodu.this, Home_zdg_cq_ok.class);
                        intent.putExtra("service_id", service_id);
                        intent.putExtra("title", title);
                        intent.putExtra("flag", true);
                        intent.putExtra("need_hidden", true);
                        startActivity(intent);
                        break;
                    case "12":
                    case "13":
                        intent = new Intent(Home_daike_guodu.this, Home_baomu_ok.class);
                        intent.putExtra("service_id", service_id);
                        intent.putExtra("title", title);
                        intent.putExtra("flag", true);
                        intent.putExtra("need_hidden", true);
                        startActivity(intent);
                        break;
                    case "14":
                    case "15":
                        intent = new Intent(Home_daike_guodu.this, Home_yuesao_ok.class);
                        intent.putExtra("service_id", service_id);
                        intent.putExtra("title", title);
                        intent.putExtra("flag", true);
                        intent.putExtra("need_hidden", true);
                        startActivity(intent);
                        break;
                }



            }
        });
        zijixiadan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                switch (service_id) {
                    case "5":
                    case "6":
                    case "7":
                    case "8":
                        intent = new Intent(Home_daike_guodu.this, Home_zdg_ok.class);
                        intent.putExtra("service_id", service_id);
                        intent.putExtra("title", title);
                        startActivity(intent);
                        break;
                    case "9":
                    case "10":
                    case "11":
                        intent = new Intent(Home_daike_guodu.this, Home_zdg_cq_ok.class);
                        intent.putExtra("service_id", service_id);
                        intent.putExtra("title", title);
                        startActivity(intent);

                        break;
                    case "12":
                    case "13":
                        intent = new Intent(Home_daike_guodu.this, Home_baomu_ok.class);
                        intent.putExtra("service_id", service_id);
                        intent.putExtra("title", title);
                        startActivity(intent);

                        break;
                    case "14":
                    case "15":
                        intent = new Intent(Home_daike_guodu.this, Home_yuesao_ok.class);
                        intent.putExtra("service_id", service_id);
                        intent.putExtra("title", title);
                        startActivity(intent);
                        break;
                }
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

    private void init() {
        daikexiadan = findViewById(R.id.daikexiadan);
        zijixiadan = findViewById(R.id.zijixiadan);
        back = findViewById(R.id.top).findViewById(R.id.logreg_left);
    }
}
