package com.ayi.stores;

import com.ayi.entity.TimeSelect;
import com.milk.flux.annotation.BindAction;
import com.milk.flux.dispatcher.Dispatcher;
import com.milk.flux.stores.Store;

import java.util.HashMap;
import java.util.List;

/**
 * Created by oceanzhang on 16/3/28.
 */
public class TimeSelectStore extends Store {
    private List<List<TimeSelect>> timeSelectData;
    public TimeSelectStore(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public List<List<TimeSelect>> getTimeSelectData() {
        return timeSelectData;
    }

    @BindAction("loadTimeSelect")
    public void loadTimeSelect(HashMap<String,Object> data){
        if(data.get("error") == null){
            timeSelectData = (List<List<TimeSelect>>) data.get("data");
            emitStoreChange(new StoreChangeEvent(1,false,"loadTimeSelect success!"));
        }else{
            emitStoreChange(new StoreChangeEvent(1,true,"loadTimeSelect error!"));
        }
    }
}
