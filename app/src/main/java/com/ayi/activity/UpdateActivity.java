package com.ayi.activity;


import android.content.pm.PackageManager;

import com.ayi.actions.APKVersionActionsCreator;
import com.ayi.stores.MyInfoStore;
import com.ayi.utils.Show_toast;
import com.ayi.utils.UpdateManager;
import com.socks.library.KLog;

import static com.ayi.utils.UpdateManager.REQUEST_PERMISSION_CODE;

/**
 *检测版本号 --是否需要更新
 * Created by Administrator on 2016/6/1.
 */
public class UpdateActivity extends TempletActivity<MyInfoStore,APKVersionActionsCreator>{
    public void initView(){
        super.initView();
        hideTitleBar();

//        setView(R.layout.update);
        actionsCreator().getVersion(UpdateActivity.this);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    KLog.e("error", "进来这里6");
                    UpdateManager.downLoadThread.start();
                } else {
                    UpdateManager.mpDialog.btu_on.setEnabled(true);
                    Show_toast.showText(UpdateActivity.this,"请打开存储权限以完成更新");
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
