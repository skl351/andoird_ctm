package com.ayi.home_page;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ayi.AyiApplication;
import com.ayi.R;
import com.ayi.adapter.poi_adapter;
import com.ayi.entity.item_map;
import com.ayi.utils.Show_toast;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class Map_search_edit extends Activity {
    private String mibu_city="";//修改的城市
    private String latitude = "";//维度
    private String longitude = "";//经度
    private View top;
    private PoiSearch poiSearch;
    private TextView key_text;

    private ListView item_list;
    private List<item_map> list = new ArrayList<>();
    poi_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        poiSearch = PoiSearch.newInstance();
        setContentView(R.layout.map_place2);
        init();
        top_about();
        poi_lin();//poi监听
    }

    private void init() {
        item_list = (ListView) findViewById(R.id.item_list);
        key_text = (TextView) findViewById(R.id.key_text);
        key_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                citySearch(0, key_text.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

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
//                    Show_toast.showText(Map_search_edit.this, "未找到结果");
                    return;
                }

                if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {// 检索结果正常返回

//获取地理编码结果
                    List<PoiInfo> list_info = poiResult.getAllPoi();

                    if (list_info != null) {
                        list.clear();

                         for (int i = 0; i < list_info.size(); i++) {

                             item_map item = new item_map();
                             try{
                             item.setName(list_info.get(i).name);
                             item.setAddress(list_info.get(i).address);
                             item.setLatitude(list_info.get(i).location.latitude);
                             item.setLongitude(list_info.get(i).location.longitude);
                             item.setCity(list_info.get(i).city);
                             }catch (Exception e){
                                 e.printStackTrace();
                                 continue;
                             }
                             list.add(item);
                         }

                        System.out.println(list);

                        if (adapter != null) {
                            adapter.notifyDataSetChanged();
                        } else {
                            adapter = new poi_adapter(list, Map_search_edit.this);
                            item_list.setAdapter(adapter);
                            item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    key_text.setText(list.get(position).getName() + "," + list.get(position).getAddress());
                                    latitude = String.valueOf(list.get(position).getLatitude());
                                    longitude = String.valueOf(list.get(position).getLongitude());
                                    KLog.d("click----", latitude + "," + longitude + "," + list.get(position).getName() + "," + list.get(position).getAddress()+","+list.get(position).getCity());
                                    mibu_city=list.get(position).getCity().substring(0,list.get(position).getCity().length()-1);
                                }
                            });
                        }
                    }
                }
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
                if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Show_toast.showText(Map_search_edit.this, "抱歉，未找到结果");
                } else {// 正常返回结果的时候，此处可以获得很多相关信息
//                    LatLng latLng=new LatLng(poiDetailResult.getLocation().latitude,poiDetailResult.getLocation().longitude);
//                    MapStatus mapStatus=new MapStatus.Builder().target(latLng).build();
//                    MapStatusUpdate msp= MapStatusUpdateFactory.newMapStatus(mapStatus);
//
//                    System.out.println(poiDetailResult.getName()+poiDetailResult.getLocation().latitude+","+poiDetailResult.getLocation().longitude+"--"+poiDetailResult.getAddress());
//                    latitude= String.valueOf(poiDetailResult.getLocation().latitude);
//                    longitude= String.valueOf(poiDetailResult.getLocation().longitude);
                }

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        });
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
                if (!latitude.equals("")) {

                    Intent intent = new Intent(Map_search_edit.this, User_info.class);
                    intent.putExtra("latitude", latitude);
                    intent.putExtra("longitude", longitude);
                    intent.putExtra("place_name", key_text.getText().toString());
                    intent.putExtra("mibu_city", mibu_city);
                    startActivity(intent);
                } else {
                    Show_toast.showText(Map_search_edit.this, "请搜索点击具体地址");
                }

            }
        });
    }

    private void citySearch(int page, String s) {
        // 设置检索参数
        PoiCitySearchOption citySearchOption = new PoiCitySearchOption();
        citySearchOption.city(AyiApplication.place);// 城市
        citySearchOption.keyword(s);// 关键字
        citySearchOption.pageCapacity(20);// 默认每页10条
        citySearchOption.pageNum(page);// 分页编号
        // 发起检索请求
        poiSearch.searchInCity(citySearchOption);

    }
}
