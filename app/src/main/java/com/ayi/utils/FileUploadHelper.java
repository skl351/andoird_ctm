package com.ayi.utils;

import android.os.Handler;
import android.os.Looper;

import com.ayi.AyiApplication;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCancellationSignal;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by oceanzhang on 16/2/4.
 */
public class FileUploadHelper {
    private static String xx="";
    private static volatile FileUploadHelper instance = null;
    private static FileUploadHelper getUploadHelper(){
        if(instance == null){
            synchronized (FileUploadHelper.class){
                if(instance == null){
                    instance = new FileUploadHelper();
                }
            }
        }
        return instance;
    }
    private static AtomicInteger uploadId = new AtomicInteger();
    private UploadManager uploadManager;
    private FileUploadHelper(){
        uploadManager = AyiApplication.getUploadManager();
    }

    static class FileUpload implements Runnable {
        int uploadId;
        File file;
        UploadListener listener;
        private Handler handler = new Handler(Looper.getMainLooper());
        public FileUpload(int uploadId, File file, UploadListener listener) {
            this.file = file;
            this.listener = listener;
            this.uploadId = uploadId;
        }

        @Override
        public void run() {
            try {

                    getUploadHelper().uploadManager.put(file, generateUploadKey(),xx, new UpCompletionHandler() {

                        @Override
                        public void complete(final String key, ResponseInfo info, org.json.JSONObject response) {
                            if (listener != null) {
                                if(Looper.myLooper() == Looper.getMainLooper()){
                                    listener.onComplete(uploadId, key);
                                }else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            listener.onComplete(uploadId, key);
                                        }
                                    });
                                }
                            }
                        }
                    }, new UploadOptions(null, null, false, new UpProgressHandler() {
                        @Override
                        public void progress(final String key, final double percent) {
                            if (listener != null) {
                                if(Looper.myLooper() == Looper.getMainLooper()){
                                    listener.onProgress(uploadId, percent);
                                }else {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            listener.onProgress(uploadId, percent);
                                        }
                                    });
                                }

                            }
                        }
                    }, new UpCancellationSignal() {
                        @Override
                        public boolean isCancelled() {
                            return false;
                        }
                    }));





            }catch (final Exception e){
                e.printStackTrace();
                if (listener != null) {
                    if(Looper.myLooper() == Looper.getMainLooper()){
                        listener.onError(uploadId, e.getMessage());
                    }else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                listener.onError(uploadId, e.getMessage());
                            }
                        });
                    }

                }
            }


        }
    }

    private static String generateUploadKey(){
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddhhmmss");
        return format.format(new Date(System.currentTimeMillis()))+generateRandomStr(8);
    }
    private static String generateRandomStr(int len) {
        //字符源，可以根据需要删减
        String generateSource = "0123456789abcdefghigklmnopqrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ";
        String rtnStr = "";
        for (int i = 0; i < len; i++) {
            //循环随机获得当次字符，并移走选出的字符
            String nowStr = String.valueOf(generateSource.charAt((int) Math.floor(Math.random() * generateSource.length())));
            rtnStr += nowStr;
            generateSource = generateSource.replaceAll(nowStr, "");
        }
        return rtnStr;
    }

    static ExecutorService executor = Executors.newFixedThreadPool(7);
    public static int uploadFile(File file, UploadListener listener,String x){
        xx=x;
        int id = uploadId.getAndIncrement();
        FileUpload upload = new FileUpload(id,file,listener);
        executor.execute(upload);
        return id;
    }
    public static interface UploadListener{
        void onComplete(int uploadId, String url);
        void onError(int uploadId, String message);
        void onProgress(int uploadId, double percent);
    }
}
