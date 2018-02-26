package com.ayi.widget;


import android.app.Dialog;
import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayi.R;
import com.ayi.zidingyi_view.CBProgressBar;


public class MyNewyearDialog extends Dialog {

    public View viewold;

    public MyNewyearDialog(Context context) {
        super(context, R.style.MyProgressDialog);
        setContentView(R.layout.newyear);
        viewold=findViewById(R.id.viewold);

    }

}
