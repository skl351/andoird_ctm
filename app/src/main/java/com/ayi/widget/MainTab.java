package com.ayi.widget;


import com.ayi.R;
import com.ayi.fragment.MainIndexFragment2;
import com.ayi.fragment.MainKefuFragment;
import com.ayi.fragment.MainMineFragment;
import com.ayi.fragment.MainOrderFragment_ok;
import com.ayi.fragment.MaininfoFragment;

/**
 * 枚举表--列好了下面4个图标和文字
 * Created by John on 16/1/29.
 */
public enum MainTab {


    //主页修改
    MAIN_INDEX(0, R.string.main_tab_title_index, R.drawable.main_tab_icon_index, MainIndexFragment2.class),

    //订单修改
    MAIN_PRODUCT_LIST(1, R.string.main_tab_title_order, R.drawable.main_tab_icon_order, MainOrderFragment_ok.class),
    //信息
    MAIN_Info(2, R.string.main_tab_title_info, R.drawable.main_tab_icon_info, MaininfoFragment.class),

    //客服修改
    MAIN_SHOPCAR(3, R.string.main_tab_title_kefu, R.drawable.main_tab_icon_kefu, MainKefuFragment.class),

    //我的
    MAIN_MIME(4, R.string.main_tab_title__my, R.drawable.main_tab_icon_mine,
            MainMineFragment.class);

    private int idx;
    private int resName;
    private int resIcon;
    private Class<?> clz;

    private MainTab(int idx, int resName, int resIcon, Class<?> clz) {
        this.idx = idx;
        this.resName = resName;
        this.resIcon = resIcon;
        this.clz = clz;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Class<?> getClz() {
        return clz;
    }

    public void setClz(Class<?> clz) {
        this.clz = clz;
    }
}
