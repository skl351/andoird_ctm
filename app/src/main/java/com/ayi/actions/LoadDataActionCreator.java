package com.ayi.actions;

import com.ayi.entity.Result;
import com.milk.flux.actions.ActionsCreator;
import com.milk.flux.dispatcher.Dispatcher;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by oceanzhang on 16/3/30.
 */
public class LoadDataActionCreator<T> extends ActionsCreator {
    public LoadDataActionCreator(Dispatcher dispatcher) {
        super(dispatcher);
    }

    public void loadData(Observable<Result<T>> observable){
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<T>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        dispatcher.dispatch("loadData", "error",e.getMessage());
                    }

                    @Override
                    public void onNext(Result<T> tResult) {
                        if(tResult.getRet() == 200) {
                            dispatcher.dispatch("loadData", "data", tResult.getData());
                        }else{
                            dispatcher.dispatch("loadData", "error", tResult.getMsg());
                        }
                    }
                });

    }
}
