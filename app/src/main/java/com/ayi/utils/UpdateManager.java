package com.ayi.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.activity.UpdateActivity;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.widget.MyUpdateDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class UpdateManager {
	private static final int DOWN_OVER = 2;
	private static final int DOWN_UPDATE = 1;
	public  static Thread downLoadThread;
	private Context mContext;
	public static MyUpdateDialog mpDialog;
	private int progress;
	private String apkUrl = new String();
	private String mcontent = new String();
	private static final String savePath = RetrofitUtil.CACHE_DIR;
	private static final String saveFileName = savePath + "/" + "sangeayi.apk";
	private boolean interceptFlag = false;
	private int mstatus = 0;
	public static final int REQUEST_PERMISSION_CODE = 100;
	private Handler mHandler = new Handler() {

		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1: {
				mpDialog.progress.setProgress(progress + 1);
//				mpDialog.shuzhi.setText((progress + 1) + "%");
//				mpDialog.progress.setProgress(progress);
				return;
			}
			case 2: {
				Toast.makeText(mContext, "下载完成现在安装！！", Toast.LENGTH_SHORT).show();
				mpDialog.dismiss();
				installApk();
				break;
			}
			}
		}
	};

	public UpdateManager(Context context, String url, String content, int status) {
		mContext = context;
		apkUrl = url;
		mcontent = content;
		mstatus = status;
	}
	//得到版本号
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
	public void checkUpdateInfo() {
		this.mpDialog = new MyUpdateDialog(this.mContext);
		try {
			this.mpDialog.dangqian_banben.setText(""+getAppVersionName(mContext));
		}catch (Exception e){
			System.out.println("错误"+e);
		}

		this.mpDialog.show();
		this.mpDialog.content.setText(mcontent);
		this.mpDialog.btu_on.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {
				paramAnonymousView.setEnabled(false);
				if ("mounted".equals(Environment.getExternalStorageState())) {
					UpdateManager.this.downLoadThread = new Thread(
							UpdateManager.this.mdownApkRunnable);
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
						int checkCode = mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
						if (checkCode != PackageManager.PERMISSION_GRANTED) {
							((Activity) mContext).requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
							return;
						}
						UpdateManager.this.downLoadThread.start();
					} else {
						UpdateManager.this.downLoadThread.start();
					}

					return;
				}
				Toast.makeText(UpdateManager.this.mContext, "亲，SD卡不在了，快去找找！！", Toast.LENGTH_SHORT).show();
			}
		});
		this.mpDialog.btu_off.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramAnonymousView) {

				if (mstatus == 1) {
					AyiApplication.getInstance().AppExit();
				} else {
					((UpdateActivity) mContext).finish();
				}
				UpdateManager.this.mpDialog.dismiss();
			}
		});
		this.mpDialog.setCancelable(false);
	}

	private Runnable mdownApkRunnable = new Runnable() {

		public void run() {
			try {

				HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(
						UpdateManager.this.apkUrl).openConnection();
				localHttpURLConnection.connect();
				int i = localHttpURLConnection.getContentLength();
				InputStream localInputStream = localHttpURLConnection
						.getInputStream();
				File localFile = new File(UpdateManager.savePath);
				if (!localFile.exists()){
					localFile.mkdir();
				}

				FileOutputStream localFileOutputStream = new FileOutputStream(
						new File(UpdateManager.saveFileName));
				int j = 0;
				byte[] arrayOfByte = new byte[1024];
				while (true) {
					int k = localInputStream.read(arrayOfByte);
					j += k;
					UpdateManager.this.progress = ((int) (100.0F * ((float)j /(float) i)));
					UpdateManager.this.mHandler.sendEmptyMessage(1);
					if (k <= 0){
						break;
					}
					localFileOutputStream.write(arrayOfByte, 0, k);
					boolean bool = UpdateManager.this.interceptFlag;

				}
				localFileOutputStream.close();
				localInputStream.close();
				UpdateManager.this.mHandler.sendEmptyMessage(2);
				return;
			} catch (MalformedURLException localMalformedURLException) {
				localMalformedURLException.printStackTrace();
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent("android.intent.action.VIEW");
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

}
