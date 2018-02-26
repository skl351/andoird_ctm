package com.ayi.order_four;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.adapter.pingjia_pic_adapter;
import com.ayi.adapter.pingjia_pic_adapter2;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Data_time_cuo;
import com.ayi.utils.FileUploadHelper;
import com.ayi.utils.Show_toast;
import com.ayi.zidingyi_view.KeyboardChangeListener;
import com.ayi.zidingyi_view.StarBarView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.finalteam.rxgalleryfinal.RxGalleryFinal;
import cn.finalteam.rxgalleryfinal.imageloader.ImageLoaderType;
import cn.finalteam.rxgalleryfinal.rxbus.RxBusResultSubscriber;
import cn.finalteam.rxgalleryfinal.rxbus.event.ImageMultipleResultEvent;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2017/2/7.
 */

public class Pingjia_jiemian extends Activity implements KeyboardChangeListener.KeyBoardListener {
    private KeyboardChangeListener mKeyboardChangeListener;
    private GridView pingjia_pic;
    private pingjia_pic_adapter adapter;
    private pingjia_pic_adapter2 adapter2;
    private String orderid = "";
    private StarBarView ratingBar;
    private StarBarView ratingBar2;
    private EditText myself_edit;
    private TextView myself_text;
    private TextView time;
    private TextView time2;
    private View time2_big;
    private Button button_btn;
    private View progressBar1;
    private View xjpj1;
    private View time_big;
    private TextView ayi_pingjiatext;
    private View top;
    private View back;
    private TextView starttext;

    public void back_init() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private ScrollView scrollView_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pingjia_jiemian_view);
        scrollView_1 = (ScrollView) findViewById(R.id.scrollView_1);
        starttext = (TextView) findViewById(R.id.starttext);
        top = findViewById(R.id.top);
        back = top.findViewById(R.id.top);
        ayi_pingjiatext = (TextView) findViewById(R.id.ayi_pingjiatext);
        xjpj1 = findViewById(R.id.xjpj1);
        time_big = findViewById(R.id.time_big);
        progressBar1 = findViewById(R.id.progressBar1);
        button_btn = (Button) findViewById(R.id.button_btn);
        time2_big = findViewById(R.id.time2_big);
        ratingBar = (StarBarView) findViewById(R.id.ratingBar);
        ratingBar2 = (StarBarView) findViewById(R.id.ratingBar2);
        myself_edit = (EditText) findViewById(R.id.myself_edit);
        myself_text = (TextView) findViewById(R.id.myself_text);
        time = (TextView) findViewById(R.id.time);
        time2 = (TextView) findViewById(R.id.time2);
        back_init();
        mKeyboardChangeListener = new KeyboardChangeListener(this);
        mKeyboardChangeListener.setKeyBoardListener(this);

        if (getIntent().getStringExtra("orderid") != null) {
            orderid = getIntent().getStringExtra("orderid");
        }
        pingjia_pic = (GridView) findViewById(R.id.pingjia_pic);
        wangluo_init();


    }

    private List<String> list_pic;
    public static List<String> list_pic2;

    private void wangluo_init() {
        progressBar1.setVisibility(View.VISIBLE);
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_getnewevalua;
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("orderid", orderid);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("response" + response);

                try {

                    //表示户主
                    if (response.getJSONObject("data").getJSONObject("customer").getInt("count") > 0) {
                        button_btn.setText("关闭");
                        button_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                onBackPressed();
                            }
                        });
//                        Toast.makeText(Pingjia_jiemian.this,"客户评价过了",Toast.LENGTH_SHORT).show();
                        System.out.println("xxxx" + response.getJSONObject("data").getJSONObject("customer").isNull("img") + response.getJSONObject("data").getJSONObject("customer").get("img"));

                        if (response.getJSONObject("data").getJSONObject("customer").getJSONArray("img").length() > 0) {
                            System.out.println("图片不为空");
                            pingjia_pic.setVisibility(View.VISIBLE);
                            JSONArray js_img = response.getJSONObject("data").getJSONObject("customer").getJSONArray("img");
                            list_pic = new ArrayList<String>();
                            for (int i = 0; i < js_img.length(); i++) {
                                System.out.println("addpicurl" + js_img.get(i));
                                list_pic.add(js_img.getString(i));
                            }
                            adapter = new pingjia_pic_adapter(Pingjia_jiemian.this, list_pic);
                            pingjia_pic.setAdapter(adapter);
                        } else {
                            System.out.println("图片为空");
                            pingjia_pic.setVisibility(View.GONE);
                        }

                        if (response.getJSONObject("data").getJSONObject("customer").getJSONArray("list").length() > 0) {
                            ratingBar2.setVisibility(View.GONE);
                            starttext.setVisibility(View.VISIBLE);
                            starttext.setText(Integer.parseInt(response.getJSONObject("data").getJSONObject("customer").getJSONArray("list").getJSONObject(0).getString("eService")) + "星");
                            myself_edit.setVisibility(View.GONE);

                            if (!response.getJSONObject("data").
                                    getJSONObject("customer").getJSONArray("list").getJSONObject(0).getString("remark").equals("")) {
                                myself_text.setVisibility(View.VISIBLE);
                                myself_text.setText(response.getJSONObject("data").
                                        getJSONObject("customer").getJSONArray("list").getJSONObject(0).getString("remark"));
                            } else {
                                myself_text.setVisibility(View.VISIBLE);
                                myself_text.setText("未写评语");
                            }

                            time2.setText(Data_time_cuo.gettime(
                                    response.getJSONObject("data").
                                            getJSONObject("customer").getJSONArray("list").getJSONObject(0).getString("ts")
                            ));


                        }
                    } else {
                        pingjia_pic.setVisibility(View.VISIBLE);
                        list_pic2 = new ArrayList<>();
//                        Toast.makeText(Pingjia_jiemian.this,"客户没有评价过",Toast.LENGTH_SHORT).show();
                        myself_edit.setVisibility(View.VISIBLE);
                        myself_text.setVisibility(View.GONE);
                        time2_big.setVisibility(View.GONE);
                        list_pic = new ArrayList<String>();
                        adapter2 = new pingjia_pic_adapter2(Pingjia_jiemian.this, list_pic2);
                        pingjia_pic.setAdapter(adapter2);
                        //上传
                        button_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progressBar1.setVisibility(View.VISIBLE);
                                button_btn.setEnabled(false);
                                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                String url = RetrofitUtil.url_getqiniu;
                                asyncHttpClient.post(url, new JsonHttpResponseHandler() {
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                        super.onSuccess(statusCode, headers, response);

                                        if (list_pic2.size() != 0) {
                                            for (int i = 0; i < list_pic2.size(); i++) {
                                                try {
                                                    FileUploadHelper.uploadFile(new File(list_pic2.get(i)), uploadListener, response.getJSONObject("data").getString("token"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        } else {
                                            wangluo_shangchuan();
                                        }

                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                        super.onFailure(statusCode, headers, throwable, errorResponse);
                                        System.out.println("获取7牛失败");
                                        button_btn.setEnabled(true);

                                    }
                                });


                            }
                        });
                    }

                    if (response.getJSONObject("data").getJSONObject("cleaner").getInt("count") > 0) {


                        if (response.getJSONObject("data").getJSONObject("cleaner").getJSONArray("list").length() > 0) {
                            ratingBar.setStarRating(Integer.parseInt(response.getJSONObject("data").getJSONObject("cleaner").getJSONArray("list").getJSONObject(0).getString("eService")));
                            ratingBar.setIsIndicator(true);
                            if (response.getJSONObject("data").getJSONObject("cleaner").getJSONArray("list").getJSONObject(0).getString("remark").equals("")) {
                                ayi_pingjiatext.setText("对方未写评语");
                            } else {
                                ayi_pingjiatext.setText(response.getJSONObject("data").getJSONObject("cleaner").getJSONArray("list").getJSONObject(0).getString("remark"));
                            }

                            time.setText(Data_time_cuo.gettime(
                                    response.getJSONObject("data").
                                            getJSONObject("cleaner").getJSONArray("list").getJSONObject(0).getString("ts")
                            ));


                        }
                    } else {
                        xjpj1.setVisibility(View.GONE);
                        time_big.setVisibility(View.GONE);
                    }

                    progressBar1.setVisibility(View.GONE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println("errorResponse" + errorResponse);
                progressBar1.setVisibility(View.GONE);

            }
        });
    }

    public void xx() {

        if (Build.VERSION.SDK_INT >= 23) {
            KLog.e("error", "进来这里2");
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(Pingjia_jiemian.this, Manifest.permission.CAMERA);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                KLog.e("error", "进来这里3");
                ActivityCompat.requestPermissions(Pingjia_jiemian.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_ASK_CALL_PHONE);
                return;
            } else {
                KLog.e("error", "进来这里4");
                //上面已经写好的拨号方法
                yy();
            }
        } else {
            KLog.e("error", "进来这里5");
            //上面已经写好的拨号方法
            yy();
        }


    }

    public void yy() {


        RxGalleryFinal
                .with(Pingjia_jiemian.this)
                .image()
                .multiple()
                .maxSize(8)
                .imageLoader(ImageLoaderType.GLIDE)
                .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {

                    @Override
                    protected void onEvent(ImageMultipleResultEvent imageMultipleResultEvent) throws Exception {


                        for (int i = 0; i < imageMultipleResultEvent.getResult().size(); i++) {
                            String path = imageMultipleResultEvent.getResult().get(i).getOriginalPath();
                            File file = new File(path);
                            System.out.println("文件1大小" + file.length());
                            String ceche = "";
                            if (path.endsWith("jpg") || path.endsWith("jpeg") || path.endsWith("JPG") || path.endsWith("JPEG")) {
                                ceche = Environment.getExternalStorageDirectory() + "/cache" + System.currentTimeMillis() + ".jpg";
                            } else if (path.endsWith("png") || path.endsWith("PNG")) {
                                ceche = Environment.getExternalStorageDirectory() + "/cache" + System.currentTimeMillis() + ".png";
                            }
                            compress(path, ceche);
                            File file2 = new File(ceche);
                            System.out.println("文件2大小" + file2.length());
                            list_pic2.add(ceche);
                            System.out.println("文件ceche" + ceche);

                        }

                        adapter2.notifyDataSetChanged();
//                                    ImageLoader.getInstance().displayImage("file://"+imageMultipleResultEvent.getResult().get(0).getOriginalPath(),imagee1);
//                                    ImageLoader.getInstance().displayImage("file://"+imageMultipleResultEvent.getResult().get(1).getOriginalPath(),imagee2);
//                                    ImageLoader.getInstance().displayImage("file://"+imageMultipleResultEvent.getResult().get(2).getOriginalPath(),imagee3);
                        Toast.makeText(Pingjia_jiemian.this, "已选择" + imageMultipleResultEvent.getResult().size() + "张图片", Toast.LENGTH_SHORT).show();
                    }

                })
                .openGallery();
    }

    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    KLog.e("error", "进来这里6");
                    // Permission Granted
                    yy();
                } else {
                    KLog.e("error", "进来这里7");
                    // Permission Denied
                    Toast.makeText(Pingjia_jiemian.this, "相机功能被禁止", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    List<String> list_url = new ArrayList<>();
    FileUploadHelper.UploadListener uploadListener = new FileUploadHelper.UploadListener() {
        @Override
        public void onComplete(int uploadId, String url) {

            System.out.println("url=duoshao" + url);
            list_url.add("http://7xroa9.com2.z0.glb.qiniucdn.com/" + url);
            if (list_url.size() == list_pic2.size()) {
                wangluo_shangchuan();
            }

//            uploadImageUrl = url;
//            postEditData(url);
//            hideProgressDialog();
        }

        @Override
        public void onError(int uploadId, String message) {
            System.out.println("上传头像失败");
//            showToast("上传头像失败.");
//            hideProgressDialog();
            button_btn.setEnabled(true);
        }

        @Override
        public void onProgress(int uploadId, double percent) {
//            changeProgress((int) (percent * 100));
        }
    };

    private void wangluo_shangchuan() {

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url1 = RetrofitUtil.url_pingjia_submit;
        RequestParams requestParams = new RequestParams();
        requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
        requestParams.put("token", AyiApplication.getInstance().accountService().token());
        requestParams.put("orderid", orderid);
        requestParams.put("eService", ratingBar2.getStarRating());
        requestParams.put("remark", myself_edit.getText().toString());


        if (list_url != null && list_url.size() != 0) {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < list_url.size(); i++) {
                jsonArray.put(list_url.get(i));
            }

            requestParams.put("img", jsonArray);
        }
        asyncHttpClient.post(url1, requestParams, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("responseqq" + response);
                try {
                    if (response.getString("ret").equals("200") && response.getJSONObject("data").getString("status").equals("1")) {
                        button_btn.setEnabled(true);
                        Show_toast.showText(Pingjia_jiemian.this, "评价成功");
                    }
                    //删除
                    for (int i = 0; i < list_pic2.size(); i++) {
                        System.out.println("list_pic2" + list_pic2.get(i));
                        DeleteFolder(list_pic2.get(i));

                    }
                    progressBar1.setVisibility(View.GONE);
                    onBackPressed();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println("errorResponseqq" + errorResponse);
                Show_toast.showText(Pingjia_jiemian.this, "评价失败");
                progressBar1.setVisibility(View.GONE);
                button_btn.setEnabled(true);

            }
        });
    }

    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param filePath 要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                System.out.println("filePa1th" + filePath);
                File f = new File(filePath);
                return f.delete();
            } else {
                System.out.println("filePat2h" + filePath);
                // 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            }
        }
    }

    /**
     * 删除文件夹以及目录下的文件
     *
     * @param filePath 被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

    int computeSampleSize(BitmapFactory.Options options,
                          int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
                Math.floor(w / minSideLength), Math.floor(h / minSideLength));
        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    public void compress(String srcPath, String url) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        float hh = dm.heightPixels;
        float ww = dm.widthPixels;
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, opts);
        opts.inJustDecodeBounds = false;
        int w = opts.outWidth;
        int h = opts.outHeight;
        int size = 0;
        if (w <= ww && h <= hh) {
            size = 1;
        } else {
            double scale = w >= h ? w / ww : h / hh;
            double log = Math.log(scale) / Math.log(2);
            double logCeil = Math.ceil(log);
            size = (int) Math.pow(2, logCeil);
        }

        opts.inSampleSize = size;
        bitmap = BitmapFactory.decodeFile(srcPath, opts);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int quality = 100;
        if (srcPath.endsWith("jpg") || srcPath.endsWith("jpeg") || srcPath.endsWith("JPG") || srcPath.endsWith("JPEG")) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
        } else if (srcPath.endsWith("png") || srcPath.endsWith("PNG")) {
            bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos);
        }

        System.out.println(baos.toByteArray().length);
        while (baos.toByteArray().length > 45 * 1024) {
            baos.reset();

            if (srcPath.endsWith("jpg") || srcPath.endsWith("jpeg") || srcPath.endsWith("JPG") || srcPath.endsWith("JPEG")) {

                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            } else if (srcPath.endsWith("png") || srcPath.endsWith("PNG")) {
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                } catch (Exception e) {
                    Show_toast.showText(Pingjia_jiemian.this, "所选图片格式错误，请重新选择");
                }


            }

            quality -= 20;
            System.out.println(baos.toByteArray().length);
        }
        try {
            baos.writeTo(new FileOutputStream(url));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onKeyboardChange(boolean isShow, int keyboardHeight) {
        Log.d("skl", "onKeyboardChange() called with: " + "isShow = [" + isShow + "], keyboardHeight = [" + keyboardHeight + "]");
        if (isShow) {
            scrollView_1.post(new Runnable() {
                @Override
                public void run() {
                    System.out.println("v.getHeight()" + scrollView_1.getHeight());
                    scrollView_1.smoothScrollTo(0, scrollView_1.getHeight() - 250);
//                    scrollView_1.fullScroll(ScrollView.FOCUS_DOWN);
                    // 获取编辑框焦点
                    myself_edit.requestFocus();
                }
            });
        }

    }
}
