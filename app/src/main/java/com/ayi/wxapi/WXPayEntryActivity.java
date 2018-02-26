package com.ayi.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ayi.retrofit.RetrofitUtil;
import com.socks.library.KLog;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, "wxaa3fb8979e3eeae7");
        api.handleIntent(getIntent(), this);
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    public void onReq(BaseReq req) {
    }

    //微信支付必经之路。
    public void onResp(BaseResp resp) {
        KLog.e("weix:","到这边了"+resp.toString());
        if(resp.getType() == 5) {
            if(resp.errCode == 0) {
                Toast.makeText(this, "支付成功", Toast.LENGTH_SHORT).show();
                KLog.e("weix:","支付成功");
                Intent intent = new Intent(RetrofitUtil.APP_BORADCASTRECEIVER4);
                Bundle mBundle = new Bundle();
                intent.putExtras(mBundle);
                sendBroadcast(intent);
            } else if(resp.errCode == -2) {
                Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
                KLog.e("weix:","没有交易");
            } else {
                Toast.makeText(this, "支付失败", Toast.LENGTH_SHORT).show();
                KLog.e("weix:","支付失败");
            }
            finish();
        }
    }
}
