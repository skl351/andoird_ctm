package com.ayi.retrofit;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ayi.AyiApplication;
import com.ayi.entity.AyiSelect;
import com.ayi.entity.Merchandise;
import com.ayi.entity.MyOrder;
import com.ayi.entity.PayOrder;
import com.ayi.entity.Result;
import com.ayi.entity.ServiceInfo;
import com.ayi.entity.TimeSelect;
import com.ayi.entity.UserInfo;
import com.ayi.entity.Version;
import com.ayi.entity.WeixinInfo;
import com.ayi.utils.Show_toast;
import com.milk.retrofit.CacheType;
import com.milk.retrofit.UseCache;

import java.util.HashMap;
import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by oceanzhang on 16/2/23.
 */
public interface ApiService {

    @POST("?service=User.getAuthCode")
    Observable<Result> getVerifyCode(@Query("username") String userName,@Query("secret_key") String vnum);

    @POST("?service=OrderCorporate.add")
    Observable<Result> Corporateadd(@Query("areaid") String areaid,
                                    @Query("contacts") String contacts,
                                    @Query("contact_phone") String contact_phone,
                                    @Query("contact_addr") String contact_addr,
                                    @Query("contact_door") String contact_door,
                                    @Query("content") String content,
                                    @Query("token") String token,
                                    @Query("user_id") String userId,
                                    @Query("dg_id") String du_id,
                                    @Query("secret_key") String vnum
    );

    @POST("?service=OrderServicePackage.add")
    Observable<Result> ServicePackageadd(@Query("areaid") String areaid,
                                         @Query("contacts") String contacts,
                                         @Query("contact_phone") String contact_phone,
                                         @Query("contact_addr") String contact_addr,
                                         @Query("contact_door") String contact_door,
                                         @Query("remark") String remark,
                                         @Query("ccsp_id") String ccsp_id,
                                         @Query("token") String token,
                                         @Query("user_id") String userId,
                                         @Query("isvalet") int isvalet,
                                         @Query("secret_key") String vnum);


    @POST("?service=User.login&group=3&client=2")
    Observable<Result<UserInfo>> login(@Query("username") String userName, @Query("password") String passwork,
                                       @Query("latitude") double latitude, @Query("longitude") double longitude,
                                       @Query("bak") String versionname,
                                       @Query("secret_key") String vnum);

    @POST("?service=Cleaner.getCleanerInfo")
    @FormUrlEncoded
    Observable<Result<UserInfo>> getUserInfo(@Field("user_id") String userId, @Field("cleaner_id") String cleanerId, @Field("token") String token,
                                             @Query("secret_key") String vnum);

    //获取我的 金币和余额信息,
    @POST("?service=Customer.detail")
    @FormUrlEncoded
    Observable<Result<JSONObject>> customerDetail(@Field("user_id") String user_id, @Field("token") String token,
                                                  @Query("secret_key") String vnum);

    @UseCache(CacheType.NORMAL)
    @GET("?service=Service.getServiceInfo")
    Observable<Result<ServiceInfo>> getServiceInfo(@Query("typeid") int typeid, @Query("areaid") String areaid);

    @POST("?service=Service.getServiceWindows")
    @FormUrlEncoded
    Observable<Result<List<List<TimeSelect>>>> getTimeSelect(@FieldMap HashMap<String, String> params);

    @POST("?service=Version.getversioninfo&requester=2")
    Observable<Result<List<Version>>> getVersion(@Query("secret_key") String vnum);

    //获取可选择的阿姨列表
    @POST("?service=Service.getServiceCleaners")
    @FormUrlEncoded
    Observable<Result<List<AyiSelect>>> getAyiSelectList(@FieldMap HashMap<String, String> params);

    //预约提交
    @POST("?service=Order.add")
    @FormUrlEncoded
    Observable<Result<PayOrder>> submitServiceOrder(@FieldMap HashMap<String, String> params);

    @UseCache(CacheType.HOURLY)
    @GET("?service=Merchandise.detail")
    Observable<Result<Merchandise>> getProductDetail(@Query("mid") String mid);


    @GET("?service=Order.getDetail")
    Observable<Result<MyOrder>> getMyOrderDetail(@Query("orderid") String orderId, @Query("user_id") String userId, @Query("token") String token);


    @POST("?service=Order.cancel")
    @FormUrlEncoded
    Observable<Result> cancelOrder(@Field("orderid") String orderid, @Field("childid") String childid, @Field("msg") String msg, @Field("user_id") String userId, @Field("token") String token);

    @POST("?service=Order.getListByCustomer")
    @FormUrlEncoded
    Observable<Result<List<MyOrder>>> getMyOrderList(@Field("type") int type, @Field("currentpage") int currentpage, @Field("pagesize") int pageSize, @Field("user_id") String user_id, @Field("token") String token);


    //客服列表
    @GET("?service=Question.getList&typeid=2")
    @UseCache(CacheType.DAILY)
    Observable<Result<JSONArray>> getQuestionList(@Query("secret_key") String vnum);

    @GET("?service=Question.getServicePhone")
    @UseCache(CacheType.DAILY)
    Observable<Result<JSONObject>> getQuestionServicePhone(@Query("areaid") String areaid,@Query("secret_key") String vnum);

    //订单评价
    @POST("?service=Order.evaluate")
    @FormUrlEncoded
    Observable<Result> orderEvaluate(@FieldMap HashMap<String, String> params);

    @POST("?service=WxPay.payment")
    @FormUrlEncoded
    Observable<Result<WeixinInfo>> weixinPay(@Field("body") String body, @Field("totalfee") String totalfee,
                                             @Field("goodstag") String goodstag, @Field("source") String source,
                                             @Field("user_id") String user_id, @Field("token") String token,
                                             @Query("secret_key") String vnum);

    @POST("?service=WxPay.callback")
    @FormUrlEncoded
    Observable<Result<WeixinInfo>> weixinCallback(@Field("out_trade_no") String out_trade_no,
                                                  @Field("source") String source,
                                                  @Field("user_id") String user_id, @Field("token") String token,
                                                  @Query("secret_key") String vnum);

    @POST("?service=Customer.pay")
    @FormUrlEncoded
    Observable<Result> customerPay(@Field("money") String money, @Field("out_trade_no") String out_trade_no, @Field("user_id") String user_id, @Field("token") String token,
                                   @Query("secret_key") String vnum);

    @POST("?service=Customer.rechargepay")
    @FormUrlEncoded
    Observable<Result> rechargePay(@Field("rechargeid") String rechargeid, @Field("user_id") String user_id, @Field("token") String token,
                                   @Query("secret_key") String vnum);

//    Observable<>
//http://blog.csdn.net/soslinken/article/details/51274327
    //文件上传
//    @POST("/file")
//    @Multipart
//    Observable<Result> uploadFile(@Part("file\"; filename=\"avatar.png\"") RequestBody file);


}
