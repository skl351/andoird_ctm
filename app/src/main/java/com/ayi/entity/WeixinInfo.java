package com.ayi.entity;

/**
 * Created by oceanzhang on 16/3/24.
 */
import java.io.Serializable;

public class WeixinInfo implements Serializable{
    private String appid;
    private String mch_id;
    private String nonce_str;
    private String prepay_id;
    private String result_code;
    private String return_code;
    private String return_msg;
    private String sign;
    private String trade_type;
    private String out_trade_no;


    public String getAppid() {
        return appid == null ? "" : appid;
    }

    public void setAppid(String appid) {
        this.appid = appid == null ? "" : appid;
    }

    public String getMch_id() {
        return mch_id == null ? "" : mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id == null ? "" : mch_id;
    }

    public String getNonce_str() {
        return nonce_str == null ? "" : nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str == null ? "" : nonce_str;
    }

    public String getPrepay_id() {
        return prepay_id == null ? "" : prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id == null ? "" : prepay_id;
    }

    public String getResult_code() {
        return result_code == null ? "" : result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code == null ? "" : result_code;
    }

    public String getReturn_code() {
        return return_code == null ? "" : return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code == null ? "" : return_code;
    }

    public String getReturn_msg() {
        return return_msg == null ? "" : return_msg;
    }

    public void setReturn_msg(String return_msg) {
        this.return_msg = return_msg == null ? "" : return_msg;
    }

    public String getSign() {
        return sign == null ? "" : sign;
    }

    public void setSign(String sign) {
        this.sign = sign == null ? "" : sign;
    }

    public String getTrade_type() {
        return trade_type == null ? "" : trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type == null ? "" : trade_type;
    }

    public String getOut_trade_no() {
        return out_trade_no == null ? "" : out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no == null ? "" : out_trade_no;
    }

    @Override
    public String toString() {
        return "WeixinInfo{" +
                "appid='" + appid + '\'' +
                ", mch_id='" + mch_id + '\'' +
                ", nonce_str='" + nonce_str + '\'' +
                ", prepay_id='" + prepay_id + '\'' +
                ", result_code='" + result_code + '\'' +
                ", return_code='" + return_code + '\'' +
                ", return_msg='" + return_msg + '\'' +
                ", sign='" + sign + '\'' +
                ", trade_type='" + trade_type + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                '}';
    }
}
