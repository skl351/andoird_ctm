package com.ayi.utils;

import com.ayi.retrofit.RetrofitUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2017/5/9.
 */

public class Debug_net {

    public  static void  debug_tiaoshi(String bug,String code,String content,String device){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url = RetrofitUtil.url_dbug;
        RequestParams requestParams = new RequestParams();
        requestParams.put("type",bug);
        requestParams.put("code",code);
        requestParams.put("content",content);
        requestParams.put("device",device);
        asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                KLog.e("url_dbug",response.toString());

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
}
