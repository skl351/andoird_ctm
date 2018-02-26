package com.ayi.activity;

import com.milk.flux.actions.BaseLoadDataActionCreator;
import com.milk.flux.stores.BaseLoadDataStore;
import com.milk.flux.stores.Store;

import rx.Observable;

/**
 * Created by oceanzhang on 16/3/31.
 */
public abstract class BaseLoadDataActivity<T,S extends BaseLoadDataStore<T>,A extends BaseLoadDataActionCreator<T>> extends TempletActivity<S,A> {
    @Override
    protected void loadAgain() {
        startLoading();
        actionsCreator().loadData(createRequest());
    }

    @Override
    protected boolean flux() {
        return true;
    }

    @Override
    public void initData() {
        super.initData();
        if(autoLoad()) {
            startLoading();
            actionsCreator().loadData(createRequest());
        }
    }
    protected boolean autoLoad(){
        return true;
    }
    @Override
    protected void updateView(Store.StoreChangeEvent event) {
        super.updateView(event);
        if(event.code == 200){
            if(event.error){
                loadError();
            }else{
                endLoading();
                bindView(store().getData());
            }
        }

    }
    protected abstract void bindView(T t);
    protected abstract Observable<T> createRequest();

}
