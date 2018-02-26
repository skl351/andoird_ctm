package com.ayi.retrofit;

import android.content.Context;
import android.os.Environment;

import com.ayi.AyiApplication;
import com.ayi.entity.Result;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.milk.retrofit.CacheCallAdapterFactory;
import com.milk.retrofit.CacheOperateImpl;
import com.milk.retrofit.HttpLoggingInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by John on 16/2/24.
 */
public class RetrofitUtil {
    /**
     * 本地缓存目录
     */
    public static String CACHE_DIR;
    /**
     * 图片缓存目录
     */
    public static String CACHE_DIR_IMAGE;
    /**
     * 待上传图片缓存目录
     */
    public static String CACHE_DIR_UPLOADING_IMG;
    public static String CACHE_DIR_SMILEY = null;
    /**
     * 图片目录
     */
    public static final String CACHE_IMAGE;
    public static String HOST = AyiApplication.URL;//开发的
    //    public static final String HOST = "http://ta.sangeayi.com/sangeayi/public/sangeayi/";//开发的
    //    public static final String HOST = "http://betaapi.sangeayi.com/sangeayi/public/sangeayi/";//王总发的一个连接
//    public static final String HOST = "http://api.sangeayi.com/sangeayi/public/sangeayi/";//正式
    //        public static final String HOST = "http://demoapi.sangeayi.com/sangeayi/public/sangeayi/";//演示
    public static final String INDEX_URL = "http://wx.sangeayi.com/wap.php"; //生产环境
    //    public static final String INDEX_URL = "http:// x.sangeayi.com/new/wap.php";  //开发环境
    public static final String URL = "http://wx.sangeayi.com/";  //生产环境
    //    public static final String URL = "http://wx.sangeayi.com/new/";//开发环境
    public static final String APP_BORADCASTRECEIVER = "wx.sangeayi.com1";
    public static final String APP_BORADCASTRECEIVER2 = "wx.sangeayi.com2";
    public static final String APP_BORADCASTRECEIVER3 = "wx.sangeayi.com3";
    public static final String APP_BORADCASTRECEIVER4 = "wx.sangeayi.com4";
    private static Retrofit retrofit;
    private static ApiService apiService;
    private static final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    private static boolean isInit = false;
    public static String url_qingqiuwenx = HOST + "&service=Customer.pay";//支付宝回调
    public static String url_zidongdenglu = HOST + "&service=User.login&group=3&client=2";//自动登录时用
    public static String url_position_recomend = HOST + "&service=PositionRecommend.getList";//消息列表的返回
    public static String url_return_list = HOST + "&service=Notifys.getList";//消息列表的返回
    public static String url_return_getinfo = HOST + "&service=Notifys.getInfo";//消息列表详情
    public static String url_duigong_list = HOST + "&service=OrderCorporate.getList";//对公列表
    public static String url_tc_dowmload = HOST + "&service=OrderServicePackage.getInfo";//套餐订单下单
    public static String url_tc_order_list = HOST + "&service=OrderServicePackage.getList";//到套餐订单列表
    public static String url_tc_deltail = HOST + "&service=CompanyServicePackage.getInfo";//到套餐详情接口
    public static String url_duig_data = HOST + "&service=CompanyCorporate.getList";//对公业务数据
    public static String url_duig_order_delere = HOST + "&service=OrderCorporate.getInfo";//对公订单详情
    public static String url_taocanlist = HOST + "&service=CompanyServicePackage.getList";//得到套餐列表
    public static String url_center_icon = HOST + "&service=CompanyServiceType.getList";//首页图标接口
    public static String url_jinweidu = HOST + "&service=Baidu.getaddrbyLatLng";//经纬度转地址
    public static String url_dizhi_jinwei = HOST + "&service=Baidu.getLatLng";//地址转经纬度
    public static String url_dbug = HOST + "&service=Debug.add";//debug
    public static String url_getqiniu = HOST + "&service=Qiniu.getToken";//获取7牛
    public static String url_duihuanyhq = HOST + "&service=Coupon.createcoupon";//领取优惠券
    public static String url_getnewevalua = HOST + "&service=Order.getEvaluateList";//得到新评价
    public static String url_list_order = HOST + "&service=Order.getListByCustomer";//订单历列表
    public static String url_list_order_detail = HOST + "&service=Order.getDetail";//订单详情
    public static String url_list_getEvaluate = HOST + "&service=Order.getEvaluate";//订单评价
    public static String url_yuyue_info = HOST + "&service=Service.getServiceInfo";//预约之前的基本信息
    public static String url_order_add = HOST + "&service=Order.add";//预约提交url
    public static String url_time_select = HOST + "&service=Service.getServiceWindows";//得到服务时间的接口
    public static String url_cancelreason = HOST + "&service=Order.cancelReason";//取消信息
    public static String url_cancel_submit = HOST + "&service=Order.cancelByCustomer";//取消订单提交
    public static String url_cancel_shiyong = HOST + "&service=Order.reviseOrderStatus2";//取消试用订单/或者正式签约
    public static String url_pingjia_submit = HOST + "&service=Order.evaluate";//评价订单提交
    public static String url_overtime = HOST + "&service=Order.overtimeAdd";//加时预约订单
    public static String url_calcelovertime = HOST + "&service=Order.reviseStatus3";//取消加时
    public static String url_ayi_select = HOST + "&service=Service.getServiceCleaners";//阿姨选择接口
    public static String url_ayi_info = HOST + "&service=Cleaner.getCleanerInfo";//阿姨的详细情况
    public static String url_pay_order = HOST + "&service=Order.pay";//订单支付
    public static String url_get_coupon = HOST + "&service=Coupon.getList";//获取优惠券
    public static String url_get_info = HOST + "&service=Customer.detail";//获取个人信息
    public static String url_pd_enough = HOST + "&service=Order.enough";//判断余额是否充足
    public static String url_pay_order_success = HOST + "&service=Order.paySuccess";//支付成功
    public static String url_place_list = HOST + "&service=Area.getOpenAreaList";//获取开通地区
    public static String url_loginout = HOST + "&service=User.loginout";//退出
    public static String url_child_order = HOST + "&service=Order.getChildList";//返回子订单
    public static String url_suishoudai = HOST + "&service=Merchandise.getList";//随手带
    public static String url_suishoudai_item_info = HOST + "&service=Merchandise.detail";//随手带具体信息
    public static String url_banan = HOST + "&service=Banner.getList";//获取bananer
    public static String url_getqianyue_info = HOST + "&service=Order.getTemporaryOrderInfo";//获取正式签约的信息--暂时没用
    public static String url_kefulist = HOST + "&service=Question.getList&typeid=2";//获得客服列表
    public static String url_addresslist = HOST + "&service=Addre.getList";//得到地址列表
    public static String url_add_addresslist = HOST + "&service=Addre.add";//添加地址：
    public static String url_dele_addresslist = HOST + "&service=Addre.delete";//删除地址：
    public static String url_modify_addresslist = HOST + "&service=Addre.edit";//修改地址：
    public static String url_weix = HOST + "&service=WxPay.payment";//微信支付：
    public static String url_near_ayi = HOST + "&service=Service.getNearbyCleaners";//附近阿姨：
    public static String url_ALIpay = HOST + "&service=Alipay.payment";//支付宝url
    public static String url_xiadanmoren = HOST + "&service=Robot.getdefaultsetsbyuser";//下单默认
    public static String url_setxiadanmoren = HOST + "&service=Robot.setdefaultvalues";//设置下单默认

    static {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            CACHE_DIR = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + "/sangeayi";
        } else {
            CACHE_DIR = Environment.getRootDirectory().getAbsolutePath()
                    + "/sangeayi";
        }
        CACHE_DIR_SMILEY = CACHE_DIR + "/smiley";
        CACHE_IMAGE = CACHE_DIR + "/image";
        CACHE_DIR_IMAGE = CACHE_DIR + "/pic";
        CACHE_DIR_UPLOADING_IMG = CACHE_DIR + "/uploading_img";
    }

    public static void iniy() {
        url_qingqiuwenx = HOST + "&service=Customer.pay";//支付宝回调
        url_zidongdenglu = HOST + "&service=User.login&group=3&client=2";//自动登录时用
        url_position_recomend = HOST + "&service=PositionRecommend.getList";//消息列表的返回
        url_return_list = HOST + "&service=Notifys.getList";//消息列表的返回
        url_return_getinfo = HOST + "&service=Notifys.getInfo";//消息列表详情
        url_duigong_list = HOST + "&service=OrderCorporate.getList";//对公列表
        url_tc_dowmload = HOST + "&service=OrderServicePackage.getInfo";//套餐订单下单
        url_tc_order_list = HOST + "&service=OrderServicePackage.getList";//到套餐订单列表
        url_tc_deltail = HOST + "&service=CompanyServicePackage.getInfo";//到套餐详情接口
        url_duig_data = HOST + "&service=CompanyCorporate.getList";//对公业务数据
        url_duig_order_delere = HOST + "&service=OrderCorporate.getInfo";//对公订单详情
        url_taocanlist = HOST + "&service=CompanyServicePackage.getList";//得到套餐列表
        url_center_icon = HOST + "&service=CompanyServiceType.getList";//首页图标接口
        url_jinweidu = HOST + "&service=Baidu.getaddrbyLatLng";//经纬度转地址
        url_dizhi_jinwei = HOST + "&service=Baidu.getLatLng";//地址转经纬度
        url_dbug = HOST + "&service=Debug.add";//debug
        url_getqiniu = HOST + "&service=Qiniu.getToken";//获取7牛
        url_duihuanyhq = HOST + "&service=Coupon.createcoupon";//领取优惠券
        url_getnewevalua = HOST + "&service=Order.getEvaluateList";//得到新评价
        url_list_order = HOST + "&service=Order.getListByCustomer";//订单历列表
        url_list_order_detail = HOST + "&service=Order.getDetail";//订单详情
        url_list_getEvaluate = HOST + "&service=Order.getEvaluate";//订单评价
        url_yuyue_info = HOST + "&service=Service.getServiceInfo";//预约之前的基本信息
        url_order_add = HOST + "&service=Order.add";//预约提交url
        url_time_select = HOST + "&service=Service.getServiceWindows";//得到服务时间的接口
        url_cancelreason = HOST + "&service=Order.cancelReason";//取消信息
        url_cancel_submit = HOST + "&service=Order.cancelByCustomer";//取消订单提交
        url_cancel_shiyong = HOST + "&service=Order.reviseOrderStatus2";//取消试用订单/或者正式签约
        url_pingjia_submit = HOST + "&service=Order.evaluate";//评价订单提交
        url_overtime = HOST + "&service=Order.overtimeAdd";//加时预约订单
        url_calcelovertime = HOST + "&service=Order.reviseStatus3";//取消加时
        url_ayi_select = HOST + "&service=Service.getServiceCleaners";//阿姨选择接口
        url_ayi_info = HOST + "&service=Cleaner.getCleanerInfo";//阿姨的详细情况
        url_pay_order = HOST + "&service=Order.pay";//订单支付
        url_get_coupon = HOST + "&service=Coupon.getList";//获取优惠券
        url_get_info = HOST + "&service=Customer.detail";//获取个人信息
        url_pd_enough = HOST + "&service=Order.enough";//判断余额是否充足
        url_pay_order_success = HOST + "&service=Order.paySuccess";//支付成功
        url_place_list = HOST + "&service=Area.getOpenAreaList";//获取开通地区
        url_loginout = HOST + "&service=User.loginout";//退出
        url_child_order = HOST + "&service=Order.getChildList";//返回子订单
        url_suishoudai = HOST + "&service=Merchandise.getList";//随手带
        url_suishoudai_item_info = HOST + "&service=Merchandise.detail";//随手带具体信息
        url_banan = HOST + "&service=Banner.getList";//获取bananer
        url_getqianyue_info = HOST + "&service=Order.getTemporaryOrderInfo";//获取正式签约的信息--暂时没用
        url_kefulist = HOST + "&service=Question.getList&typeid=2";//获得客服列表
        url_addresslist = HOST + "&service=Addre.getList";//得到地址列表
        url_add_addresslist = HOST + "&service=Addre.add";//添加地址：
        url_dele_addresslist = HOST + "&service=Addre.delete";//删除地址：
        url_modify_addresslist = HOST + "&service=Addre.edit";//修改地址：
        url_weix = HOST + "&service=WxPay.payment";//微信支付：
        url_near_ayi = HOST + "&service=Service.getNearbyCleaners";//附近阿姨：
        url_ALIpay = HOST + "&service=Alipay.payment";//支付宝url
        url_xiadanmoren = HOST + "&service=Robot.getdefaultsetsbyuser";//下单默认
        url_setxiadanmoren = HOST + "&service=Robot.setdefaultvalues";//设置下单默认
    }

    public static void init(Context context) {
        HOST = AyiApplication.URL;
        if (!isInit) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            //HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            //设置日志级别,最高
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            //client.interceptors().add(interceptor);
            retrofit = new Retrofit.Builder().baseUrl(HOST)
                    //在此处声明使用FastJsonConverter做为转换器
                    .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addConverterFactory(FastJsonConverterFactory.create())
                    .addCallAdapterFactory(CacheCallAdapterFactory.create(CacheOperateImpl.createInstance(context.getApplicationContext())))
                    .client(client)
                    .build();
        }
    }

    public static ApiService getService() {
        if (apiService == null) {
            if (retrofit == null) {
                System.out.println("AyiApplication.URL" + AyiApplication.URL + "," + HOST);
                if (AyiApplication.URL.equals("")) {

                    AyiApplication.URL = AyiApplication.getInstance().getURL();
                    AyiApplication.m = AyiApplication.getInstance().getURL_m();
                    HOST = AyiApplication.URL;
                    System.out.println("AyiApplication.URL" + AyiApplication.URL + "," + HOST);
                    iniy();
                }
                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                //HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                //设置日志级别,最高
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
                //client.interceptors().add(interceptor);
                retrofit = new Retrofit.Builder().baseUrl(HOST)
                        //在此处声明使用FastJsonConverter做为转换器
                        .addConverterFactory(GsonConverterFactory.create(gson))
//                    .addConverterFactory(FastJsonConverterFactory.create())
                        .addCallAdapterFactory(CacheCallAdapterFactory.create(CacheOperateImpl.createInstance(AyiApplication.getInstance()))).client(client)
                        .build();
            }
            apiService = retrofit.create(ApiService.class);
        }

        return apiService;
    }

    //自定义异常类
    public static class APIException extends Exception {
        int code;

        public APIException(int code, String detailMessage) {
            super(detailMessage);
            this.code = code;
        }

        @Override
        public String toString() {
            return "code:" + code + "  message:" + super.getMessage();
        }
    }

    private static <T> Observable<T> flatResponse(final Result<T> result) {
        return Observable.create(new Observable.OnSubscribe<T>() {

            @Override
            public void call(Subscriber<? super T> subscriber) {
                if (result.getRet() == 200) {
                    if (!subscriber.isUnsubscribed()) {
                        T res = result.getData();

                        subscriber.onNext(res);
                    }
                } else {
                    if (!subscriber.isUnsubscribed()) {
                        //TODO CODE
                        subscriber.onError(new APIException(result.getRet(), result.getMsg()));
                    }
                    return;
                }
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            }
        });
    }


    public static <T> Observable.Transformer<Result<T>, T> applySchedulers() {
        return new Observable.Transformer<Result<T>, T>() {
            @Override
            public Observable<T> call(Observable<Result<T>> observable) {
                return observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .flatMap(new Func1() {
                            @Override
                            public Observable<T> call(Object response) {
                                return flatResponse((Result<T>) response);
                            }
                        });
            }
        };
    }
}
