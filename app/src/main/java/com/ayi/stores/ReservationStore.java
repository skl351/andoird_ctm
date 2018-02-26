package com.ayi.stores;

import com.ayi.entity.PayOrder;
import com.ayi.entity.ServiceInfo;
import com.milk.flux.annotation.BindAction;
import com.milk.flux.dispatcher.Dispatcher;
import com.milk.flux.stores.Store;

import java.util.HashMap;

/**
 * Created by oceanzhang on 16/3/28.
 */
public class ReservationStore extends Store {
    public ReservationStore(Dispatcher dispatcher) {
        super(dispatcher);
    }
    private ServiceInfo serviceInfo;
    private PayOrder orderInfo;
    @BindAction("loadServiceInfo")
    public void loadServiceInfo(HashMap<String,Object> data){
        if(data.get("error") == null){
            serviceInfo = (ServiceInfo) data.get("data");
            emitStoreChange(new StoreChangeEvent(1,false,"load service info success."));
        }else{
            emitStoreChange(new StoreChangeEvent(1,true, (String) data.get("error")));
        }
    }
    @BindAction("submitServiceOrder")
    public void submitServiceOrder(HashMap<String,Object> data){
        if(data.get("error") == null){
            orderInfo = (PayOrder) data.get("data");
            emitStoreChange(new StoreChangeEvent(2,false,"submitServiceOrder success."));
        }else{
            emitStoreChange(new StoreChangeEvent(2,true, (String) data.get("error")));
        }
    }

    public ServiceInfo getServiceInfo() {
        return serviceInfo;
    }

    public PayOrder getOrderInfo() {
        return orderInfo;
    }
}
