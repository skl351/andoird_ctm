package com.ayi.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.WebViewActivity;
import com.ayi.entity.Result;
import com.ayi.retrofit.RetrofitUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.milk.base.BaseLoadWithRefreshFragment;
import com.milk.flux.actions.BaseLoadDataActionCreator;
import com.milk.flux.stores.BaseLoadDataStore;
import com.socks.library.KLog;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;

import butterknife.Bind;
import cz.msebera.android.httpclient.Header;
import rx.Observable;
import rx.functions.Func2;

/**
 * Created by oceanzhang on 16/3/24.
 */
public class MainKefuFragment extends BaseLoadWithRefreshFragment<JSONObject,BaseLoadDataStore<JSONObject>,BaseLoadDataActionCreator<JSONObject>>{

    @Bind(R.id.frag_main_kefu_btn_call)
    Button btnCall;
    @Bind(R.id.frag_main_kefu_tableview)
    LinearLayout tableView;

    @Override
    public void initData() {
        super.initData();
    }

    @Override
    protected Observable<JSONObject> createRequest() {
        Observable.Transformer<Result<JSONObject>,JSONObject> transformer1 = RetrofitUtil.applySchedulers();
        Observable.Transformer<Result<JSONArray>,JSONArray> transformer2 = RetrofitUtil.applySchedulers();
        Observable<JSONArray> observable1 = RetrofitUtil.getService().getQuestionList( AyiApplication.m).compose(transformer2);
        Observable<JSONObject> observable2 = RetrofitUtil.getService().getQuestionServicePhone(AyiApplication.getInstance().areaId(), AyiApplication.m).compose(transformer1);
        Observable<JSONObject> obs = Observable.zip(observable1, observable2, new Func2<JSONArray, JSONObject, JSONObject>() {
            @Override
            public JSONObject call(JSONArray jsonArrayResult, JSONObject jsonObjectResult) {
                JSONObject object = new JSONObject();
                object.put("questionPhone",jsonObjectResult.getString("value"));
                object.put("questionList",jsonArrayResult);
                return object;
            }
        });
        return obs;
    }
    final public static int REQUEST_CODE_ASK_CALL_PHONE = 123;
//7-10
    @Override
    protected void bindView(JSONObject jsonObject) {
        super.bindView(jsonObject);
        btnCall.setTag(jsonObject.getString("questionPhone"));
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri url=Uri.parse("tel:"+getResources().getString(R.string.Customer_phone));
                Intent intent=new Intent(Intent.ACTION_DIAL,url);
                startActivity(intent);
            }
        });

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        String url= RetrofitUtil.url_kefulist;
        asyncHttpClient.post(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, org.json.JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("response"+response);
                try {
                    tableView.removeAllViews();
                    for(int i=0;i<response.getJSONArray("data").length();i++){
                        final org.json.JSONObject obj =response.getJSONArray("data").getJSONObject(i);
                        LinearLayout item = (LinearLayout) inflateView(R.layout.view_item_kefu_question_copy);
                        TextView title= (TextView) item.findViewById(R.id.title);
                        title.setText(obj.getString("title"));

                        item.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent(getActivity(),WebViewActivity.class);
                                    intent.putExtra("title",obj.getString("title"));
                                    intent.putExtra("url",obj.getString("url"));
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });
                        tableView.addView(item);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, org.json.JSONArray errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_CALL_PHONE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    KLog.e("error","进来这里6");
                    // Permission Granted
                    Uri url=Uri.parse("tel:"+getResources().getString(R.string.Customer_phone));
                    Intent intent=new Intent(Intent.ACTION_DIAL,url);
                    startActivity(intent);
                } else {
                    KLog.e("error","进来这里7");
                    // Permission Denied
                    Toast.makeText(getActivity(), "电话拨号被禁止", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    @Override
    protected int getRefreshContentLayoutId() {
        return R.layout.fragment_main_kefu;
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.client_kufu));
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.client_kufu));
    }
}
