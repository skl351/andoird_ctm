package com.ayi.stores;

import com.milk.flux.annotation.BindAction;
import com.milk.flux.dispatcher.Dispatcher;
import com.milk.flux.stores.Store;

import java.util.HashMap;

/**
 * Created by oceanzhang on 16/3/30.
 */
public class LoadDataStore<T> extends Store {
    private T data;
    public LoadDataStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @BindAction("loadData")
    public void loadData(HashMap<String,String> d){
        if(d.get("error") == null){
            data = (T) d.get("data");
            emitStoreChange(new StoreChangeEvent());
        }else{
            emitStoreChange(new StoreChangeEvent(true,d.get("error")));
        }
    }

    public T getData() {
        return data;
    }
}
