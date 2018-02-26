package com.ayi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.ayi.R;
import com.ayi.actions.PayActionsCreator;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.stores.MyInfoStore;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import butterknife.OnClick;

/**
 * 分享
 * Created by oceanzhang on 16/3/24.
 */
public class ShareActivity extends TempletActivity<MyInfoStore, PayActionsCreator> {
    public IWXAPI api;

    @Override
    public void initView() {
        super.initView();
        setView(R.layout.activity_share);
        setTitle("分享");
    }

    @OnClick({R.id.share_layout_circle_friends, R.id.share_layout_friends, R.id.share_layout_qq_zone})
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.share_layout_circle_friends:
                wechatShare(1);
                break;
            case R.id.share_layout_friends:
                wechatShare(0);
                break;
            case R.id.share_layout_qq_zone://qq空间
                Intent intent = new Intent(ShareActivity.this, WebViewActivity.class);
                intent.putExtra("url", "http://sns.qzone.qq.com/cgi-bin/qzshare/cgi_qzshare_onekey?url=" + RetrofitUtil.URL);
                startActivity(intent);
                break;
        }
    }

    private void wechatShare(int flag) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://doc.sangeayi.com/share.html";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "找阿姨神器";
        msg.description = "找阿姨不用到处跑，三个阿姨方便又可靠";
        //这里替换一张自己工程里的图片资源
        Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.share_logo);
        msg.setThumbImage(thumb);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = flag == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    @Override
    protected void onStart() {
        super.onStart();
        api = WXAPIFactory.createWXAPI(ShareActivity.this, "wxaa3fb8979e3eeae7", true);
        api.registerApp("wxaa3fb8979e3eeae7");

    }
}
