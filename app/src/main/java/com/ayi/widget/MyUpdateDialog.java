package com.ayi.widget;


import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayi.R;
import com.ayi.zidingyi_view.CBProgressBar;


public class MyUpdateDialog extends Dialog {
    public ImageView btu_off;
    public Button btu_on;
    public CBProgressBar progress;
    public TextView shuzhi;
    public TextView content;
    public TextView dangqian_banben;
    
    public MyUpdateDialog(Context context) {
        super(context, R.style.MyProgressDialog);
        setContentView(R.layout.my_update);
        content = (TextView)findViewById(R.id.content);
        content.setMovementMethod(ScrollingMovementMethod.getInstance());
        btu_on = (Button)findViewById(R.id.btu_on);
        btu_off = (ImageView)findViewById(R.id.btu_off);
        progress = (CBProgressBar)findViewById(R.id.progress);
        dangqian_banben= (TextView) findViewById(R.id.dangqian_banben);
        shuzhi = (TextView)findViewById(R.id.shuzhi);
//        btu_on.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setEnabled(false);
//            }
//        });
    }

}
