package com.ayi.alipay;

import com.ayi.AyiApplication;
import com.ayi.utils.SignUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class Alipay {
    //-----支付宝----//
    // 商户PID
    public static final String PARTNER = "2088121262858704";
    // 商户收款账号
    public static final String SELLER = "44167704@qq.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMZIbgr4hhfjEwagaJz/42oFdalKrqbLn109VOuWahj4jF1pu88IHVaxgIXTnMOzRsF85pKfLjkD4zlaxATkX5WN5sbsTZdAUjB5vNJBoPr79Qq6AEm1TUVroPYcM8KdidwRJ6eqmHDyRwo76maJEYKPFRGvlIuKQJQJZx0f0R8XAgMBAAECgYAHvgZF+WRLBHvWs2bCUp+PoTzx6k+dha/oVR++vzXPWc0pTJqfv+JdYzrwfs3NOm/V2EmsifilE0jJEYk746rzu1Aax5iLLtFcApmglYvUDhtp1HFvfR5u58YDPCsI8WPc3KCS71sK/m5ZU2OyQnQVL0y1E3Nrj6boSEXmBhMiqQJBAPqD+6/S+RT0xXw4neiiWt9RY0lhqMn9zUfBV56P3azzX0y0/YU7yWdddAYBLfbHvWj6SHtgG0oehRV2eq1r4Y0CQQDKn7VbjwxGuKu4OcHJGOAkshDlPRjpHgwtYqtgs7B6D5wH1KWNGss1i3ls7ihzPebS4ga2TljvJ5Evi5C+G/AzAkEAwtkxQ/pWXiRcDliDCdRt1dCZ5xOcky9jIXKSUfQYOVM6RpFntt9fsZQQkDMOqymEe0hgZAXGdAn0/VKf7I9CaQJBAIaPHR8alip5BQCoynpDnMaBwhqaQmq9W5TgS7NCYVYN+65vlylnu79pSk/+sLonR5yQIaj7yq+3dQFAC/VUFGUCQQDZW+29U3aSoxJWbGau4JqBOlZbFF2mWQR1QHGJ6GCHhiDd40yU7fBgYGJ7sBcbEkoy3bXdJ/c+hHyaPoZKOYuN";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGSG4K+IYX4xMGoGic/+NqBXWpSq6my59dPVTrlmoY+IxdabvPCB1WsYCF05zDs0bBfOaSny45A+M5WsQE5F+VjebG7E2XQFIwebzSQaD6+/UKugBJtU1Fa6D2HDPCnYncESenqphw8kcKO+pmiRGCjxURr5SLikCUCWcdH9EfFwIDAQAB";
    public Alipay(){

    }
    public static String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * create the order info. 创建订单信息
     */
    public static String getOrderInfo(String subject, String body, String price,String paynumber) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + paynumber + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     * AyiApplication.getInstance().accountService()
     */
    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("HHMMdd", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        Random r = new Random();
        key = key + r.nextInt();
        Random random=new Random();
        String str= String.valueOf(random.nextInt(10000));
        key = AyiApplication.getInstance().accountService().mobile()+"_"+key.substring(0, 6)+str;
        return key;
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public static String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
