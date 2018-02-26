package com.ayi.activity;

import android.content.Intent;
import android.view.View;

import com.ayi.R;

import butterknife.OnClick;

/**
 * Created by oceanzhang on 16/3/24.
 * ayi://servicedesc
 */
public class ServiceDescriptionActivity extends TempletActivity {
    @Override
    public void initView() {
        super.initView();
        setTitle("服务说明");
        setView(R.layout.activity_service_desc);
    }
    @OnClick(R.id.act_service_desc_btn_go)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId() == R.id.act_service_desc_btn_go) {
            Intent intent = new Intent(ServiceDescriptionActivity.this,WebViewActivity.class);
            intent.putExtra("url",getQueryParameter("tag"));
            startActivity(intent);
            finish();
        }
    }
}
