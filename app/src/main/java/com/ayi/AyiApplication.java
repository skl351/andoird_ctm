package com.ayi;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.support.v4.app.FragmentTabHost;
import android.widget.Toast;

import com.ayi.account.AccountService;
import com.ayi.account.impl.AyiAccountService;
import com.ayi.entity.Location;
import com.ayi.retrofit.RetrofitUtil;
import com.baidu.mapapi.SDKInitializer;
import com.milk.base.BaseApplication;
import com.milk.utils.Log;
import com.qiniu.android.common.Zone;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.Recorder;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.persistent.FileRecorder;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.UUID;

/**
 * 一个初始化的上下文对象
 * Created by oceanzhang on 16/3/24.
 */
public class AyiApplication extends BaseApplication {
    public static String m;
    public static String URL = "";
    public static int widthPixels = 720;// 屏幕宽度
    public static int heightPixels = 1280;// 屏幕高度
    public static String flag_tc_dg = "1";//1-订单 2-套餐 3-对公
    public static String flag_xuyaoshuax = "false";//false 不用刷新  true 刷新
    public static String place = "长春";
    public static String area_id = "2201";
    public static double lat = 0;
    public static double logna = 0;
    //新年
    public static long time1 = 1518624000;//2018-2-15
    public static long time2 = 1518883200;//2018-2-18
    private static UploadManager uploadManager;

    public static UploadManager getUploadManager() {
        return uploadManager;
    }

    private static AyiApplication instance = null;
    private AccountService accountService = null;
    private static Stack<Activity> activityStack;
    private FragmentTabHost tabHost;
    public static SharedPreferences sysInitSharedPreferences;
    public static String first_start_flag;
    private String webview_url;
    private String webview_url2;
    private int canvalet;

    public static AyiApplication getInstance() {
        return instance;
    }

    private boolean flag_first = true;//第一次
    private boolean flag_order_first = true;

    public boolean isFlag_order_first() {
        return flag_order_first;
    }

    public void setFlag_order_first(boolean flag_order_first) {
        this.flag_order_first = flag_order_first;
    }

    public boolean isFlag_first() {
        return flag_first;
    }

    public void setFlag_first(boolean flag_first) {
        this.flag_first = flag_first;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        do_some();
    }

    public void do_some() {
        System.out.println("app新创建");
        sysInitSharedPreferences = getSharedPreferences("sysini", 0);
        MultiDex.install(this);
        instance = this;
        webview_url = "";
        webview_url2 = "";
//        createCacheDir();
        SDKInitializer.initialize(getApplicationContext());
        Recorder recorder = null;
        try {
            recorder = new FileRecorder(this.getFilesDir().getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        KeyGenerator keyGenerator = new KeyGenerator() {
            @Override
            public String gen(String key, File file) {
                return UUID.randomUUID().toString();
            }
        };
        //默认重试5次失败
        Configuration config = new Configuration.Builder()
                .connectTimeout(10)
                .responseTimeout(60)
                .recorder(recorder, keyGenerator)
                .zone(Zone.zone0)
                .build();
        uploadManager = new UploadManager(config);
    }

    // 创建SD卡缓存目录
    private void createCacheDir() {


        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File f = new File(RetrofitUtil.CACHE_DIR);
            if (f.exists()) {
                System.out.println("SD卡缓存目录:已存在!");
            } else {
                if (f.mkdirs()) {
                    System.out.println("SD卡缓存目录:" + f.getAbsolutePath() + "已创建!");
                } else {
                    System.out.println("SD卡缓存目录:创建失败!");
                }
            }
            File ff = new File(RetrofitUtil.CACHE_DIR_IMAGE);
            if (ff.exists()) {
                System.out.println("SD卡照片缓存目录:已存在!");
            } else {
                if (ff.mkdirs()) {
                    System.out.println("SD卡照片缓存目录:" + ff.getAbsolutePath() + "已创建!");
                } else {
                    System.out.println("SD卡照片缓存目录:创建失败!");
                }
            }
            File fff = new File(RetrofitUtil.CACHE_DIR_IMAGE);
            if (fff.exists()) {
                System.out.println("SD卡缓存目录:已存在!");
            } else {
                if (fff.mkdirs()) {
                    System.out.println("SD卡照片缓存目录:" + fff.getAbsolutePath() + "已创建!");
                } else {
                    System.out.println("SD卡照片缓存目录:创建失败!");
                }
            }

            File ffff = new File(RetrofitUtil.CACHE_DIR_UPLOADING_IMG);
            if (ffff.exists()) {
                System.out.println("SD卡上传缓存目录:已存在!");
            } else {
                if (ffff.mkdirs()) {
                    System.out.println("SD卡上传缓存目录:" + ffff.getAbsolutePath() + "已创建!");
                } else {
                    System.out.println("SD卡上传缓存目录:创建失败!");
                }
            }
        } else {
            Toast.makeText(AyiApplication.this, "亲，您的SD不在了，可能有的功能不能用奥，赶快看看吧。", Toast.LENGTH_SHORT).show();
        }

    }

    public void setcodetime(String a) {
        sysInitSharedPreferences.edit().putString("codetime", a).commit();
    }

    public String getcodetime() {
        return sysInitSharedPreferences.getString("codetime", "0");
    }

    public void setURL(String a) {
        sysInitSharedPreferences.edit().putString("my_URL" + AyiApplication.getInstance().accountService().id(), a).commit();
    }

    public String getURL() {
        return sysInitSharedPreferences.getString("my_URL" + AyiApplication.getInstance().accountService().id(), "");
    }

    public void setURL_m(String a) {
        sysInitSharedPreferences.edit().putString("my_URL_m" + AyiApplication.getInstance().accountService().id(), a).commit();
    }

    public String getURL_m() {
        return sysInitSharedPreferences.getString("my_URL_m" + AyiApplication.getInstance().accountService().id(), "");
    }

    public int getCanvalet() {
        return sysInitSharedPreferences.getInt("canvalet", 0);
    }

    public void setCanvalet(int canvalet) {
        sysInitSharedPreferences.edit().putInt("canvalet", canvalet).commit();
    }

    public static String version_sam;

    public String getversion_sam() {
        return version_sam;
    }

    public void setversion_sam(String version_sam) {
        this.version_sam = version_sam;
        sysInitSharedPreferences.edit().putString("version_sam", this.version_sam).commit();
    }

    public String getFirst_start_flag() {
        return first_start_flag;
    }

    public void setFirst_start_flag(String first_start_flag) {
        this.first_start_flag = first_start_flag;
        sysInitSharedPreferences.edit().putString("first_start_flag", this.first_start_flag).commit();
    }

    public String getWebview_url() {
        return webview_url;
    }

    public void setWebview_url(String webview_url) {
        this.webview_url = webview_url;
        sysInitSharedPreferences.edit().putString("webview_url", this.webview_url).commit();
    }

    public String getWebview_url2() {
        return webview_url2;
    }

    public void setWebview_url2(String webview_url2) {
        this.webview_url2 = webview_url2;
        sysInitSharedPreferences.edit().putString("webview_url2", this.webview_url).commit();
    }

    @Override
    protected void init() {
        super.init();
        Log.isDebug = true;
    }

    public void setTabHost(FragmentTabHost tabHost) {
        this.tabHost = tabHost;
    }

    public FragmentTabHost getTabHost() {
        return tabHost;
    }

    public AccountService accountService() {
        if (accountService == null) {
            accountService = new AyiAccountService(this);
        }
        return accountService;
    }

    //"lng":106.57690601157,"lat":29.53561074027
    public String areaId() {
        return "3302";
    }

    public Location location() {
        return new Location(29.815204179927, 106.57690601157);
    }

    public String userId() {
        return accountService().id();
    }

    public String token() {
        return accountService().token();
    }

    /**
     * add Activity 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
    public void AppExit() {
        try {
            finishAllActivity();
        } catch (Exception e) {
        }
    }

}
