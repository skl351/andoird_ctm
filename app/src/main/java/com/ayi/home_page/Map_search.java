package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.adapter.poi_adapter;
import com.ayi.entity.item_map;
import com.ayi.retrofit.RetrofitUtil;
import com.ayi.utils.Debug_net;
import com.ayi.utils.Show_toast;
import com.ayi.zidingyi_view.MyLocationListener;
import com.ayi.zidingyi_view.PoiOverlay;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.socks.library.KLog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/9/6.
 */
public class Map_search extends Activity {

    MapView mMapView = null;
    BaiduMap mBaiduMap;
    private PoiSearch poiSearch;
    public LocationClient mLocationClient = null;
    private boolean isFirstIN = true;
    GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private ListView item_list;
    private List<item_map> list = new ArrayList<>();
    poi_adapter adapter;
    private float zoom = 18;
    private String mibu_city = "";
    //这个是定位监听器
    public BDLocationListener myListener = new MyLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location.getLocType() != 161 && location.getLocType() != 61) {
                Show_toast.showText(Map_search.this, "请打开app定位功能");
                mLocationClient.stop();
                Map_search.this.finish();
                return;
            }
            //这个判断是否修改
            if (!latitude.equals("")) {
                /**获取经纬度*/
                double lat = Double.valueOf(latitude);//纬度
                double lng = Double.valueOf(longitude);//经度
                latitude = "";
                longitude = "";
                LatLng ptCenter = new LatLng(lat, lng);
                LatLng latLng = new LatLng(lat, lng);
                MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).target(latLng).build();
                MapStatusUpdate msp = MapStatusUpdateFactory.newMapStatus(mapStatus);
                mBaiduMap.animateMapStatus(msp);
                System.out.println("起点修改：" + lat + "," + lng);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));//通过坐标
                return;
            }
            //通过手动定位
            if (flag_dingwei) {
                flag_dingwei = false;
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//                LatLng latLng=new LatLng(29.810412,121.551494);
                MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).target(latLng).build();
                MapStatusUpdate msp = MapStatusUpdateFactory.newMapStatus(mapStatus);
                mBaiduMap.animateMapStatus(msp);
                isFirstIN = false;
                /**获取经纬度*/
                double lat = location.getLatitude();//纬度
                double lng = location.getLongitude();//经度
                LatLng ptCenter = new LatLng(lat, lng);
                System.out.println("手动定位到这里起点：" + lat + "," + lng);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));//通过坐标
                return;
            }
//            super.onReceiveLocation(location);
            KLog.e("location.getLocType()", location.getLocType() + "," + location.getCity());
//            MyLocationData data= new MyLocationData.Builder()
//                    .accuracy(location.getRadius())
//                    .latitude(location.getLatitude())
//                    .longitude(location.getLatitude())
//                    .build();
//                mBaiduMap.setMyLocationData(data);
            if (location.getCity().split("市")[0].equals(AyiApplication.place)) {
                System.out.println("当前城市是为左上角");
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).target(latLng).build();
                MapStatusUpdate msp = MapStatusUpdateFactory.newMapStatus(mapStatus);
                mBaiduMap.animateMapStatus(msp);
                isFirstIN = false;
                /**获取经纬度*/
                double lat = location.getLatitude();//纬度
                double lng = location.getLongitude();//经度
                LatLng ptCenter = new LatLng(lat, lng);
                System.out.println("起点：" + lat + "," + lng);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));//通过坐标
            } else {
                System.out.println("当前城市不是为左上角");
                AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                String url = RetrofitUtil.url_dizhi_jinwei;
                RequestParams requestParams = new RequestParams();
                requestParams.put("address", AyiApplication.place);
//                String url= "http://api.map.baidu.com/geocoder?address="+AyiApplication.place+"&output=json&key=t1zCa2SVbfFKaK1vpgQqaTqB7tvegZOG";
                asyncHttpClient.get(url, requestParams, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            System.out.println("response" + response);
                            LatLng latLng = new LatLng(Double.valueOf(response.getJSONObject("data").getJSONObject("result").getJSONObject("location").getString("lat")), Double.valueOf(response.getJSONObject("data").getJSONObject("result").getJSONObject("location").getString("lng")));
                            MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).target(latLng).build();
                            MapStatusUpdate msp = MapStatusUpdateFactory.newMapStatus(mapStatus);
                            mBaiduMap.animateMapStatus(msp);
                            isFirstIN = false;
                            /**获取经纬度*/
                            double lat = Double.valueOf(response.getJSONObject("data").getJSONObject("result").getJSONObject("location").getString("lat"));//纬度
                            double lng = Double.valueOf(response.getJSONObject("data").getJSONObject("result").getJSONObject("location").getString("lng"));//经度
                            LatLng ptCenter = new LatLng(lat, lng);
                            System.out.println("起点：" + lat + "," + lng);
                            mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));//通过坐标
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        System.out.println(errorResponse + "," + throwable);

                    }
                });
            }
        }
    };
    private String latitude = "";//维度
    private String longitude = "";//经度
    int totalPage;
    private View top;
    BitmapDescriptor mCurrentMarker;
    private Button request;
    private TextView key_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        poiSearch = PoiSearch.newInstance();
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_place);
        if (getIntent().getStringExtra("longitude") != null) {
            latitude = getIntent().getStringExtra("latitude");
            longitude = getIntent().getStringExtra("longitude");
        }
        init();
        top_about();//点击相关
        mapview_baidu();//map相关
//        poi_lin();//poi监听
        map_click();//地图点击的地方
        initLocation();//定位相关信息
//        btn_click_serch();//按钮点击
//        citySearch(10, key_text.getText().toString().toString());//城市搜索
        mLocationClient.start();
    }

    private void init() {
        item_list = (ListView) findViewById(R.id.item_list);
        request = (Button) findViewById(R.id.request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude = "";
                longitude = "";
                key_text.setText("");
                requestLocation();//请求定位
            }
        });
        key_text = (TextView) findViewById(R.id.key_text);
        key_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Map_search.this, Map_search_edit.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 手动请求定位的方法
     */
    private boolean flag_dingwei = false;

    public void requestLocation() {
        if (mLocationClient != null && mLocationClient.isStarted()) {
            flag_dingwei = true;
            mLocationClient.requestLocation();
        } else {
            Log.d("log", "locClient is null or not started");
        }
    }

    private void map_click() {
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                System.out.println(mapPoi.getName());
                LatLng latLng = new LatLng(mapPoi.getPosition().latitude, mapPoi.getPosition().longitude);
                MapStatus mapStatus = new MapStatus.Builder().target(latLng).build();
                MapStatusUpdate msp = MapStatusUpdateFactory.newMapStatus(mapStatus);
                mBaiduMap.animateMapStatus(msp);//标到这里
                key_text.setText("");
                latitude = "";
                longitude = "";
//                key_text.setText(mapPoi.getName());
                return false;
            }
        });
    }

    private void poi_lin() {
        //设置搜索监听
        poiSearch.setOnGetPoiSearchResultListener(new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                if (poiResult == null
                        || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {// 没有找到检索结果
                    Toast.makeText(Map_search.this, "未找到结果",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回
                    mBaiduMap.clear();
                    MyPoiOverlay poiOverlay = new MyPoiOverlay(mBaiduMap);
                    poiOverlay.setData(poiResult);// 设置POI数据
                    mBaiduMap.setOnMarkerClickListener(poiOverlay);
                    poiOverlay.addToMap();// 将所有的overlay添加到地图上
                    poiOverlay.zoomToSpan();
                    //
//                    Toast.makeText(
//                            Map_search.this,
//                            "总共查到" + poiResult.getTotalPoiNum() + "个兴趣点, 分为"
//                                    + totalPage + "页", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(Map_search.this, "抱歉，未找到结果",
                            Toast.LENGTH_SHORT).show();
                } else {// 正常返回结果的时候，此处可以获得很多相关信息
                    LatLng latLng = new LatLng(poiDetailResult.getLocation().latitude, poiDetailResult.getLocation().longitude);
                    MapStatus mapStatus = new MapStatus.Builder().target(latLng).build();
                    MapStatusUpdate msp = MapStatusUpdateFactory.newMapStatus(mapStatus);
                    mBaiduMap.animateMapStatus(msp);
                    System.out.println(poiDetailResult.getName() + poiDetailResult.getLocation().latitude + "," + poiDetailResult.getLocation().longitude + "--" + poiDetailResult.getAddress());
                    latitude = String.valueOf(poiDetailResult.getLocation().latitude);
                    longitude = String.valueOf(poiDetailResult.getLocation().longitude);
//                    key_text.setText(poiDetailResult.getAddress()+poiDetailResult.getName());
                }

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
    }

    private void mapview_baidu() {
        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);
        // 隐藏logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)) {
            child.setVisibility(View.INVISIBLE);
        }

        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.showMapPoi(true);//显示底部poi
        // 修改为自定义marker
        mCurrentMarker = null;
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.NORMAL, true, null));
        zhuantaigaibian();
        fandilibianma();
    }

    /**
     * 范地理编码
     */
    private void fandilibianma() {
        mSearch = GeoCoder.newInstance();
        mSearch.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {

            @Override
            public void onGetGeoCodeResult(GeoCodeResult result) {
                if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(Map_search.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                //获取地理编码结果

            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(Map_search.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                //获取地理编码结果
                List<PoiInfo> list_info = reverseGeoCodeResult.getPoiList();
//                for (int i=0;i<list_info.size();i++){
//                    System.out.println(list_info.get(i).city);
//                    System.out.println(list_info.get(i).location.latitude);
//                    System.out.println(list_info.get(i).location.longitude);
//                    System.out.println(list_info.get(i).address);
//                    System.out.println(list_info.get(i).name);
//                }
                if (list_info != null) {
                    list.clear();
                    for (int i = 0; i < list_info.size(); i++) {
                        item_map item = new item_map();
                        item.setName(list_info.get(i).name);
                        item.setAddress(list_info.get(i).address);
                        item.setLatitude(list_info.get(i).location.latitude);
                        item.setLongitude(list_info.get(i).location.longitude);
                        item.setCity(list_info.get(i).city);
                        list.add(item);
                    }
                    if (adapter != null) {
                        adapter.notifyDataSetChanged();
                        System.out.println("第N次");
                    } else {
                        System.out.println("第一次");
                        adapter = new poi_adapter(list, Map_search.this);
                        item_list.setAdapter(adapter);
                        item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                key_text.setText(list.get(position).getName()+","+list.get(position).getAddress());
                                key_text.setText(list.get(position).getName());
                                latitude = String.valueOf(list.get(position).getLatitude());
                                longitude = String.valueOf(list.get(position).getLongitude());
                                if (list.get(position).getCity() == null) {
                                    AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
                                    String url = RetrofitUtil.url_jinweidu;
//                                String url=  "http://api.map.baidu.com/geocoder?location="+latitude+","+longitude+"&output=json&key=t1zCa2SVbfFKaK1vpgQqaTqB7tvegZOG";
                                    RequestParams requestParams = new RequestParams();
                                    requestParams.put("Lat", latitude);
                                    requestParams.put("Lng", longitude);
                                    asyncHttpClient.get(url, requestParams, new JsonHttpResponseHandler() {
                                        @Override
                                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                            super.onSuccess(statusCode, headers, response);
                                            try {
                                                System.out.println("response" + response);
                                                if (response.getJSONObject("data").getJSONObject("result").getJSONObject("addressComponent").getString("city").length() > 0) {
                                                    mibu_city = response.getJSONObject("data").getJSONObject("result").getJSONObject("addressComponent").getString("city").
                                                            substring(0, response.getJSONObject("data").getJSONObject("result").getJSONObject("addressComponent").getString("city").length() - 1);
                                                }
                                                latitude = response.getJSONObject("data").getJSONObject("result").getJSONObject("location").getString("lat");
                                                longitude = response.getJSONObject("data").getJSONObject("result").getJSONObject("location").getString("lng");
                                                //跳转
                                                LatLng latLng = new LatLng(response.getJSONObject("data").getJSONObject("result").getJSONObject("location").getDouble("lat"), response.getJSONObject("data").getJSONObject("result").getJSONObject("location").getDouble("lng"));
                                                MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).target(latLng).build();
                                                MapStatusUpdate msp = MapStatusUpdateFactory.newMapStatus(mapStatus);
                                                mBaiduMap.animateMapStatus(msp);
                                                System.out.println("---" + mibu_city + "," + latitude + "," + longitude);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Debug_net.debug_tiaoshi("10090", e.getMessage(), e.toString() + latitude + "," + longitude + "," + mibu_city + "," + response.toString(), "公司经纬度接口");
                                            }
                                        }

                                        @Override
                                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                                            super.onFailure(statusCode, headers, throwable, errorResponse);
                                            System.out.println(errorResponse + "," + throwable);
                                            Debug_net.debug_tiaoshi("10091", throwable.toString(), throwable.toString(), "公司经纬度接口网络出错");
                                        }
                                    });
                                } else {
                                    mibu_city = list.get(position).getCity().substring(0, list.get(position).getCity().length() - 1);
                                    //跳转
                                    LatLng latLng = new LatLng(list.get(position).getLatitude(), list.get(position).getLongitude());
                                    MapStatus mapStatus = new MapStatus.Builder().zoom(zoom).target(latLng).build();
                                    MapStatusUpdate msp = MapStatusUpdateFactory.newMapStatus(mapStatus);
                                    mBaiduMap.animateMapStatus(msp);
                                }
                                KLog.d("click----", mibu_city + "," + latitude + "," + longitude + "," + list.get(position).getName() + "," + list.get(position).getCity());
                            }
                        });
                    }
                }
            }
        });
    }

    /**
     * 状态改变
     */
    private void zhuantaigaibian() {

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                LatLng mCenterLatLng = mapStatus.target;
                /**获取经纬度*/
                double lat = mCenterLatLng.latitude;//纬度
                double lng = mCenterLatLng.longitude;//经度
                System.out.println("状态改变的：" + lat + "," + lng);
                LatLng ptCenter = new LatLng(lat, lng);
                mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(ptCenter));//通过坐标
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                zoom = mapStatus.zoom;
            }
        });
        mBaiduMap
                .setMyLocationConfigeration(new MyLocationConfiguration(
                        MyLocationConfiguration.LocationMode.NORMAL, true, null));

    }

    private void top_about() {
        top = findViewById(R.id.top);
        top.findViewById(R.id.logreg_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        top.findViewById(R.id.logreg_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KLog.e("传送" + latitude + longitude + "," + key_text.getText().toString() + "," + mibu_city);
                if (!latitude.equals("")) {
//                    if((!"".equals(AyiApplication.shiji_dizhi))&&(!AyiApplication.shiji_dizhi.equals(MainActivity.place))){
//                        Toast.makeText(Map_search.this,"请选择相应城市",Toast.LENGTH_SHORT).show();
//                    }else {
                    Intent intent = new Intent(Map_search.this, User_info.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("place_name", key_text.getText().toString());
                    intent.putExtra("mibu_city", mibu_city);
                    startActivity(intent);
//                    }
                } else {
                    Toast.makeText(Map_search.this, "请搜索点击具体地址", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //按钮点击择poi
    private void btn_click_serch() {

    }


    @Override
    protected void onStart() {
        super.onStart();
        mBaiduMap.setMyLocationEnabled(true);
        if (!mLocationClient.isStarted())
            mLocationClient.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //停止定位
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient.stop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 0;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要

        mLocationClient.setLocOption(option);
    }

    //根据poi的点击
    private class MyPoiOverlay extends PoiOverlay {

        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int index) {
            super.onPoiClick(index);
            PoiInfo poi = getPoiResult().getAllPoi().get(index);
            // if (poi.hasCaterDetails) {
            poiSearch.searchPoiDetail((new PoiDetailSearchOption())
                    .poiUid(poi.uid));
            // }
            return true;
        }
    }

}
