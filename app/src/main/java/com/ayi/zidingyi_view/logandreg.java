package com.ayi.zidingyi_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayi.R;


/**
 * Created by android on 2016/3/21.
 */
public class logandreg extends RelativeLayout {
    private ImageView img_lef;
    private TextView center_text;
    private View logreg_right;

    public logandreg(Context context) {
        this(context, null);
    }

    public logandreg(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public logandreg(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View.inflate(context, R.layout.view_logandreg, this);
        img_lef = (ImageView) findViewById(R.id.logreg_left_nei);
        center_text = (TextView) findViewById(R.id.logreg_center);
        logreg_right = findViewById(R.id.logreg_right);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.logandreg);
        int n = ta.getIndexCount();

        for (int i = 0; i < n; i++) {
            int attr = ta.getIndex(i);
            switch (attr) {
                case R.styleable.logandreg_left_but:
                    BitmapDrawable drawable = (BitmapDrawable) ta.getDrawable(attr);
                    img_lef.setBackground(drawable);
//                    img_lef.setBackground(drawable);
                    break;
                case R.styleable.logandreg_center_text:
                    String string = ta.getString(attr);
                    center_text.setText(string);
                    break;
                case R.styleable.logandreg_right_but:
                    boolean flag = ta.getBoolean(attr, false);
                    if (flag) {
                        logreg_right.setVisibility(VISIBLE);
                    } else {
                        logreg_right.setVisibility(GONE);
                    }
                    break;


            }

        }
    }
}
