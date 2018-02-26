package com.ayi.home_page;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ayi.R;
import com.ayi.utils.Data_time_cuo;

/**
 * Created by Administrator on 2016/12/4.
 */
public class Coupon_delite extends Activity {

    private TextView use_place;
    private TextView use_money;
    private TextView use_date;
    private TextView use_type;
    private View top;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.coupon_delite);
        init();
        init_text();
    }

    private void init_text() {
        use_place.setText(getIntent().getStringExtra("use_place"));
        use_money.setText("￥"+getIntent().getStringExtra("use_money"));
        use_date.setText(Data_time_cuo.gettime2(getIntent().getStringExtra("use_date")));
        use_type.setText(getIntent().getStringExtra("use_type"));
    }

    private void init() {
        top=findViewById(R.id.top);
        top.findViewById(R.id.logreg_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        use_place= (TextView) findViewById(R.id.user_place);
        use_money= (TextView) findViewById(R.id.use_money);
        use_date= (TextView) findViewById(R.id.use_date);
        use_type= (TextView) findViewById(R.id.use_type);
    }
}
