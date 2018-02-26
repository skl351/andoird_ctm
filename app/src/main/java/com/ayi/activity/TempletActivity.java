package com.ayi.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.milk.base.BaseActivity;
import com.milk.flux.actions.ActionsCreator;
import com.milk.flux.stores.Store;

import butterknife.ButterKnife;

/**
 * Created by oceanzhang on 16/2/18.
 * 模板activity 包含titleBar 通过setView设置显示的视图 通过setFragment加载一个fragment.
 */
public class TempletActivity<S extends Store,C extends ActionsCreator> extends BaseActivity<S,C> implements View.OnClickListener{
    RelativeLayout title;
    TextView tvTitle;
    View templet_header_img_btn_left_big;
    ImageButton imgBtnRight;
    TextView txtBtnLeft;
    TextView txtBtnRight;
    FrameLayout contentView;
    ImageView imgSearch;
    EditText etSearch;

    FrameLayout emptyView;
    TextView tvErrorMsg;
    View progressView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AyiApplication.getInstance().addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AyiApplication.getInstance().finishActivity(this);
    }
    @Override
    protected final boolean hasActionBar() {
        return false;
    }
    @Override
    protected final int getLayoutId() {
        return R.layout.activity_templet;
    }
    protected boolean hasBackBtn(){
        return true;
    }
    @Override
    public void initView() {
        super.initView();
        contentView = (FrameLayout) findViewById(R.id.templet_content_view);
        imgSearch  = (ImageView) findViewById(R.id.templet_header_img_btn_search);
        etSearch  = (EditText) findViewById(R.id.templet_header_edit_search);
        title = (RelativeLayout) findViewById(R.id.templet_header);
        tvTitle = (TextView) findViewById(R.id.templet_header_title);
        templet_header_img_btn_left_big = findViewById(R.id.templet_header_img_btn_left_big);
        imgBtnRight = (ImageButton) findViewById(R.id.templet_header_img_btn_right);
        txtBtnLeft = (TextView) findViewById(R.id.templet_header_txt_btn_left);
        txtBtnRight = (TextView) findViewById(R.id.templet_header_txt_btn_right);
        emptyView = (FrameLayout) findViewById(R.id.templet_empty_view);
        tvErrorMsg = (TextView) findViewById(R.id.act_templet_empty_tv_msg);
        progressView = findViewById(R.id.templet_empty_progress_view);
        if(fullScreen()){
            title.setVisibility(View.GONE);
        }
        if(hasBackBtn()){
            templet_header_img_btn_left_big.setVisibility(View.VISIBLE);
        }
        templet_header_img_btn_left_big.setOnClickListener(this);
        imgBtnRight.setOnClickListener(this);
        txtBtnRight.setOnClickListener(this);
        txtBtnLeft.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
    }
    protected final void setView(@LayoutRes int layoutId){
        setView(inflateView(layoutId));
    }
    protected final void setView(View view){
        contentView.removeAllViews();
        contentView.addView(view, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        ButterKnife.bind(this, view);
    }
    protected final void setFragment(@Nullable Fragment fragment){
        getSupportFragmentManager().beginTransaction().add(R.id.templet_content_view,fragment).commit();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.templet_header_txt_btn_left:

            case R.id.templet_header_img_btn_left_big:
                onTitleLeftBtnClick(v);
                break;
            case R.id.templet_header_txt_btn_right:
            case R.id.templet_header_img_btn_right:
                onTitleRightBtnClick(v);
                break;
            case R.id.templet_header_img_btn_search:
                btnSearchClick();
                break;
        }
    }
    protected void onTitleLeftBtnClick(View v){
        if(hasBackBtn()){
            finish();
        }
    }
    protected void onTitleRightBtnClick(View v){}
    public final void setTitle(String title){
        imgSearch.setVisibility(View.GONE);
        etSearch.setVisibility(View.GONE);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }
    public final void setTitle(int resId){
        setTitle(getString(resId));
    }
    public final void setLeftBtnTxt(String text){
        if(!hasBackBtn()) {
            txtBtnLeft.setVisibility(View.VISIBLE);
            templet_header_img_btn_left_big.setVisibility(View.GONE);
            txtBtnLeft.setText(text);
        }
    }
    public final void setLeftBtnTxt(int resId){
        setLeftBtnTxt(getString(resId));
    }
    public final void setRightBtnTxt(String text){
        txtBtnRight.setVisibility(View.VISIBLE);
        imgBtnRight.setVisibility(View.GONE);
        txtBtnRight.setText(text);
    }
    public final void setRightBtnTxt(int resId){
        setRightBtnTxt(getString(resId));
    }

    public final void setRightBtnImg(int resId){
        txtBtnRight.setVisibility(View.GONE);
        imgBtnRight.setVisibility(View.VISIBLE);
        imgBtnRight.setImageResource(resId);
    }

    public final void showSearchView(boolean editable){
        tvTitle.setVisibility(View.GONE);
        if(editable){
            imgSearch.setVisibility(View.GONE);
            etSearch.setVisibility(View.VISIBLE);
        }else{
            imgSearch.setVisibility(View.VISIBLE);
            etSearch.setVisibility(View.GONE);
        }
    }
    protected void btnSearchClick(){

    }
    public final void showTitleBar(){
        title.setVisibility(View.VISIBLE);
    }
    public final void hideTitleBar(){
        title.setVisibility(View.GONE);
    }


    public void startLoading(){
        progressView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
        tvErrorMsg.setVisibility(View.GONE);
    }
    public void endLoading(){
        emptyView.setVisibility(View.GONE);
        contentView.setVisibility(View.VISIBLE);
        tvErrorMsg.setVisibility(View.GONE);
    }
    public void loadError(){
        progressView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
        contentView.setVisibility(View.GONE);
        tvErrorMsg.setVisibility(View.VISIBLE);
        emptyView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAgain();
            }
        });
    }

    protected void loadAgain(){

    }
}
