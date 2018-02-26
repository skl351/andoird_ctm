package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.ayi.R;
import com.ayi.utils.FileService;

/**套餐订单
 * Created by Administrator on 2017/7/6.
 */

public class Home_daike_guodu2 extends Activity {
    private View back;
    private View daikexiadan;
    private View zijixiadan;
    private String ccsp_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_daike_guodu);
        ccsp_id = getIntent().getStringExtra("ccsp_id");
        init();
        init_back();
        init_click();
    }

    private void init_click() {
        daikexiadan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileService service = new FileService(Home_daike_guodu2.this);
                String info = "" + "~" + "" + "~" + "" + "~" + "" + "~" + "" + "~" + "" + "~" + "";
                try {
                    service.saveToRom("user2.txt", info);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent=new Intent(Home_daike_guodu2.this,Business_appointment_tc.class);
                intent.putExtra("ccsp_id",ccsp_id);
                intent.putExtra("flag", true);
                intent.putExtra("need_hidden", true);
                startActivity(intent);
            }
        });
        zijixiadan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home_daike_guodu2.this,Business_appointment_tc.class);
                intent.putExtra("ccsp_id",ccsp_id);
                startActivity(intent);
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
