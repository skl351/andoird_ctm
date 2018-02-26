package com.ayi.stores;

import com.ayi.entity.AyiSelect;
import com.milk.flux.dispatcher.Dispatcher;
import com.milk.flux.stores.BaseRecyclerListStore;

/**
 * Created by oceanzhang on 16/2/21.
 */
public class AyiSelectListStore extends BaseRecyclerListStore<AyiSelect> {
    protected AyiSelectListStore(Dispatcher dispatcher) {
        super(dispatcher);
    }
}
