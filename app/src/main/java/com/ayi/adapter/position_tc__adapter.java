package com.ayi.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.activity.LoginActivity;
import com.ayi.entity.item_position_tc;
import com.ayi.home_page.Business_appointment_tc;
import com.ayi.home_page.Combo;
import com.ayi.home_page.Home_daike_guodu2;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.widget.MyNewyearDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/8/30.
 */
public class position_tc__adapter extends BaseAdapter {

    private List<item_position_tc> list;
    Context context;

    public position_tc__adapter(Context context, List<item_position_tc> list) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private View view;

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        view = LayoutInflater.from(context).inflate(R.layout.item_position_view, null);
//        ImageView zuobian_img= (ImageView) view.findViewById(R.id.zuobian_img);
        ImageView image_1 = (ImageView) view.findViewById(R.id.image_1);
        TextView hot_text = (TextView) view.findViewById(R.id.hot_text);
        TextView price1 = (TextView) view.findViewById(R.id.price1);
        TextView item_title = (TextView) view.findViewById(R.id.item_title);
        ImageLoader.getInstance().displayImage(list.get(position).getSimple_img(), image_1);
        if (list.get(position).getCornertitle() != null && !list.get(position).getCornertitle().equals("")) {
            hot_text.setText(list.get(position).getCornertitle());
        } else {
            hot_text.setVisibility(View.GONE);
        }

        price1.setText(list.get(position).getPrice());
        item_title.setText(list.get(position).getTitle());
//        Bitmap bitmap = Bitmap.createBitmap(400, 150, Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(bitmap);//得到位图的canvas---能都点出draw方法的
//        Paint paint = new Paint();
//        paint.setColor(context.getResources().getColor(R.color.gray_3));
//        canvas.drawCircle(50,50,50,paint);
//        zuobian_img.setImageBitmap(bitmap);
        System.out.println("2type_id" + list.get(position).getId() + "," + list.get(position).getType_id());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //新年
                if (alert_newyears(context)){
                    return;
                }

                if (AyiApplication.getInstance().accountService().id().isEmpty() && AyiApplication.getInstance().accountService().token().isEmpty()) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    return;
                } else {
                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                    String url = RetrofitUtil.url_get_info;
                    asyncHttpClient.setTimeout(20000);
                    RequestParams requestParams = new RequestParams();
                    requestParams.put("user_id", AyiApplication.getInstance().accountService().id());
                    requestParams.put("token", AyiApplication.getInstance().accountService().token());
                    asyncHttpClient.post(url, requestParams, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            try {
                                String ret = response.getString("ret");
                                if (!ret.equals("200")) {
                                    Intent intent = new Intent(context, LoginActivity.class);
                                    context.startActivity(intent);
                                } else {

                                    if (AyiApplication.getInstance().getCanvalet() == 1) {
                                        Intent intent = new Intent(context, Home_daike_guodu2.class);
                                        intent.putExtra("ccsp_id", list.get(position).getId());
                                        context.startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(context, Business_appointment_tc.class);
                                        intent.putExtra("ccsp_id", list.get(position).getId());
                                        context.startActivity(intent);
                                    }

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                            super.onFailure(statusCode, headers, throwable, errorResponse);
                            Intent intent = new Intent(context, LoginActivity.class);
                            context.startActivity(intent);
                        }
                    });

                }
            }
        });
        return view;
    }

    //新年
    public boolean alert_newyears(Context context) {

        long time = getTime();
        if (time < AyiApplication.time2 && time >=AyiApplication.time1) {
            final MyNewyearDialog dialog=new MyNewyearDialog(context);
            dialog.viewold.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            dialog.show();
            Window dialogWindow = dialog.getWindow();
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            p.height = (int) (dm.heightPixels * 0.9); // 高度设置为屏幕的0.6，根据实际情况调整
            p.width = (int) (dm.widthPixels * 0.9); // 宽度设置为屏幕的0.65，根据实际情况调整
            dialogWindow.setAttributes(p);
            return true;
        }
        return false;
    }

    public long getTime() {
        long time = System.currentTimeMillis() / 1000;//获取系统时间的10位的时间戳
        return time;
    }
}
