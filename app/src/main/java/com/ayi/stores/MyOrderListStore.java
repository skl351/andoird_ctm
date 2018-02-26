package com.ayi.stores;

import com.ayi.entity.MyOrder;
import com.milk.flux.annotation.BindAction;
import com.milk.flux.dispatcher.Dispatcher;
import com.milk.flux.stores.BaseRecyclerListStore;

import java.util.HashMap;

/**
 * Created by oceanzhang on 16/2/21.
 */
public class MyOrderListStore extends BaseRecyclerListStore<MyOrder> {
    protected MyOrderListStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @BindAction("calcelOrder")
    public void calcelOrder(HashMap<String,Object> data){
        if(data.get("error") == null){
            int code = (Integer) data.get("code");
            String msg ;
            if(code == 200){
                msg = "取消订单成功.";
            }else if(code == 406){
                msg = "该订单已被取消.";
            }else if(code == 407){
                msg = "该订单不能被取消.";
            }else if(code == 414){
                msg = "该订单已超过两小时,不能取消.";
            }else{
                msg = "未知错误.";
            }
            emitStoreChange(new StoreChangeEvent(code,false,msg));
        }else{
            emitStoreChange(new StoreChangeEvent(true,"vie order failed"));
        }
    }

    @BindAction("startWork")
     public void startWork(HashMap<String,Object> data){
        if(data.get("error") == null){
            int code = (Integer) data.get("code");
            String msg ;
            if(code == 200){
                msg = "开始工作成功.";
            }else if(code == 411){
                msg = "开始工作失败.";
            }else if(code == 410){
                msg = "该工作已在进行.";
            }else{
                msg = "未知错误.";
            }
            emitStoreChange(new StoreChangeEvent(code,false,msg));
        }else{
            emitStoreChange(new StoreChangeEvent(true,"start work failed"));
        }
    }
    @BindAction("finishWork")
    public void finishWork(HashMap<String,Object> data){
        if(data.get("error") == null){
            int code = (Integer) data.get("code");
            String msg ;
            if(code == 200){
                msg = "完成工作成功.";
            }else{
                msg = "完成工作失败,请重试.";
            }
            emitStoreChange(new StoreChangeEvent(code,false,msg));
        }else{
            emitStoreChange(new StoreChangeEvent(true,"start work failed"));
        }
    }
}
