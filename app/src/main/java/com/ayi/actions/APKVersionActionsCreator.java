package com.ayi.actions;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.entity.Result;
import com.ayi.entity.Version;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.UpdateManager;
import com.milk.flux.actions.ActionsCreator;
import com.milk.flux.dispatcher.Dispatcher;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class APKVersionActionsCreator extends ActionsCreator {
    public APKVersionActionsCreator(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public void getVersion(final Context context) {
        try {

            RetrofitUtil.getService().getVersion( AyiApplication.m)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<Result<List<Version>>>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                            ((Activity) context).finish();
//                            Toast.makeText(context, "版本检测失败"+e, Toast.LENGTH_LONG).show();
                            System.out.println("版本检测失败"+e);
                        }

                        @Override
                        public void onNext(Result<List<Version>> listResult) {
                            System.out.println("listResult"+listResult);
                            if (listResult.getRet() == 200) {
                                if (listResult.getData().size() > 0) {
                                    //最新版本号
                                    double lastversion = Double.valueOf(listResult.getData().get(0).getV_last_ver());
                                    //最低版本号
                                    double unactivatedversion = Double.valueOf(listResult.getData().get(0).getV_unactivated_ver());
                                    //下载路径
                                    String url = listResult.getData().get(0).getV_APK_path() + listResult.getData().get(0).getV_last_ver() + "/" + listResult.getData().get(0).getV_APK_name();

                                    //在最低版本号内提示更新
                                    if (lastversion > getAppVersionName(context) && unactivatedversion <= getAppVersionName(context)) {
                                        new UpdateManager(context, url, listResult.getData().get(0).getV_meno(), 0).checkUpdateInfo();
                                        //在最低版本号下直径更新
                                    } else if (unactivatedversion > getAppVersionName(context)) {
                                        new UpdateManager(context, url, listResult.getData().get(0).getV_meno(), 1).checkUpdateInfo();
                                    } else {
                                        ((Activity) context).finish();
                                    }
                                }
                            } else {
                            }
                        }
                    });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static double getAppVersionName(Context context) {
        double versionName = 0;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = Double.valueOf(pi.versionName);
        } catch (Exception e) {

        }
        return versionName;
    }

}
