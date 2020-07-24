package com.example.martin.myapplication.storio;

import android.content.Context;
import android.util.Log;

import com.example.martin.myapplication.StockUpdate;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;

/**
 * Created by martin on 7/24/20.
 */

public class LocalItemPersistenceHandlingTransformer implements ObservableTransformer<StockUpdate, StockUpdate> {
    private final WeakReference<Context> contextWeakReference;

    private LocalItemPersistenceHandlingTransformer(Context context) {
        this.contextWeakReference = new WeakReference<Context>(context);
    }

    public static LocalItemPersistenceHandlingTransformer addLocalItemPersistenceHandling(Context context) {
        return new LocalItemPersistenceHandlingTransformer(context);
    }

    @Override
    public ObservableSource<StockUpdate> apply(@NonNull Observable<StockUpdate> upstream) {
        return upstream
                .doOnNext(this::saveStockUpdate)
                .onExceptionResumeNext(StorIOFactory.createLocalDbStockUpdateRetrievalObservable(this.contextWeakReference.get()));
    }

    private void saveStockUpdate(StockUpdate stockUpdate) {
        Log.v("saveStockUpdate", stockUpdate.getStockSymbol());
        StorIOFactory.get(this.contextWeakReference.get())
                .put()
                .object(stockUpdate)
                .prepare()
                .asRxSingle()
                .subscribe();
    }
}
