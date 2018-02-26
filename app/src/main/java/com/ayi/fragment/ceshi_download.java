package com.ayi.fragment;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.MainActivity;
import com.ayi.entity.Result;
import com.ayi.home_page.Business_appointment;
import com.ayi.retrofit.ApiService;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Show_toast;
import com.milk.utils.Log;
import com.socks.library.KLog;

import java.io.File;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/11/6.
 */

public class ceshi_download extends Activity {
    long xx = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ceshixiazai);
        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//创建下载任务,downloadUrl就是下载链接
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://pic4.nipic.com/20091217/3885730_124701000519_2.jpg"));
//指定下载路径和下载文件名
                request.setDestinationInExternalPublicDir("/download/", "ceshi.jpg");

//获取下载管理器
                DownloadManager downloadManager = (DownloadManager) ceshi_download.this.getSystemService(Context.DOWNLOAD_SERVICE);
//将下载任务加入下载队列，否则不会进行下载
                xx = downloadManager.enqueue(request);
                //注册广播接收者，监听下载状态
                getApplication().registerReceiver(receiver,
                        new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
            }
        });
        findViewById(R.id.fenxiang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(Environment.getExternalStorageDirectory() + "/download/" + "ceshi.jpg");//这里是文件位置
                System.out.println(file.exists());
                Intent share = new Intent(Intent.ACTION_SEND);
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                share.setType("*/*");
                startActivity(Intent.createChooser(share, "发送"));
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.putExtra(Intent.EXTRA_SUBJECT, "测试标题");
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_TEXT, "测试内容");
                startActivity(Intent.createChooser(intent, "来自xxx"));
            }
        });

    }

    //广播接受者，接收下载状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();//检查下载状态
        }
    };

    //检查下载状态
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        query.setFilterById(xx);//筛选下载任务，传入任务ID，可变参数
        Cursor c = downloadManager.query(query);

        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Log.i(">>>下载暂停");
                case DownloadManager.STATUS_PENDING:
                    Log.i(">>>下载延迟");
                case DownloadManager.STATUS_RUNNING:
                    Log.i(">>>正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.i(">>>下载完成");

                    break;
                case DownloadManager.STATUS_FAILED:
                    Log.i(">>>下载失败");
                    break;
            }
        }
    }

}
