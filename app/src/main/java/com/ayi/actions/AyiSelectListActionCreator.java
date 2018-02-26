package com.ayi.actions;

import android.content.Context;

import com.ayi.entity.AyiSelect;
import com.ayi.entity.Result;
import com.milk.flux.actions.BaseRecyclerListActionCreator;
import com.milk.flux.dispatcher.Dispatcher;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by oceanzhang on 16/2/18.
 * 抢单
 */
public class AyiSelectListActionCreator extends BaseRecyclerListActionCreator<AyiSelect> {


    protected AyiSelectListActionCreator(Dispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void loadData(Observable observable, final boolean loadMore, final Context context) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Result<List<AyiSelect>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        loadDataError(e.getMessage());
                    }

                    @Override
                    public void onNext(Result<List<AyiSelect>> o) {
                        if (o.getRet() == 200) {
                            List<AyiSelect> orders = o.getData();
                            loadDataComplete(orders, loadMore);
                        } else {
                            onError(new Exception(o.getMsg()));
                        }
                    }
                });

    }

}
