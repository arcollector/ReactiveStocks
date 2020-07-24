package com.example.martin.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.martin.myapplication.alphavantage.AlphaVantageServiceFactory;
import com.example.martin.myapplication.alphavantage.AlphaVintageService;
import com.example.martin.myapplication.alphavantage.json.AlphaVantageGlobalQuote;
import com.example.martin.myapplication.storio.StockUpdateTable;
import com.example.martin.myapplication.storio.StorIOFactory;
import com.pushtorefresh.storio.sqlite.queries.Query;
import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by martin on 7/23/20.
 */

public class ExampleLifecycleActivity extends RxAppCompatActivity {

    private static final String TAG = "ExampleLifecycleActivit";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        /*
        Observable.interval(1, TimeUnit.SECONDS)
                .doOnNext((r) -> Log.d(TAG, "1"))
                .doOnDispose(() -> Log.d(TAG, "1 disposed"))
                .compose(bindToLifecycle())
                .subscribe();

        Observable.interval(1, TimeUnit.SECONDS)
                .doOnNext((r) -> Log.d(TAG, "2"))
                .doOnDispose(() -> Log.d(TAG, "2 disposed"))
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe();

        Observable.interval(1, TimeUnit.SECONDS)
                .doOnNext((r) -> Log.d(TAG, "3"))
                .doOnDispose(() -> Log.d(TAG, "3 disposed"))
                .compose(bindToLifecycle())
                .subscribe();
                */

        AlphaVintageService alphaVintageService = new AlphaVantageServiceFactory().create();

        String apiKey = BuildConfig.ALPHAVANTAGE_API_KEY;

        BiFunction<Long, String, Observable<AlphaVantageGlobalQuote>> getTickerData =
                (integer, s) -> alphaVintageService
                        .get(s, apiKey)
                        .toObservable();

        log("creating main observable");
        Observable.merge(
                Observable.combineLatest(
                        Observable.interval(0, 1, TimeUnit.MINUTES),
                        Observable.just("GOOG"),
                        getTickerData
                )
                        .doOnNext(r -> log("combineLatest 1 molesting"))
                        .doOnDispose(() -> log("combineLatest 1 disposed")),

                Observable.combineLatest(
                        Observable.interval(0, 1, TimeUnit.MINUTES),
                        Observable.just("TWTR"),
                        getTickerData
                )
                        .doOnNext(r -> log("combineLatest 2 molesting"))
                        .doOnDispose(() -> log("combineLatest 2 disposed")),

                Observable.combineLatest(
                        Observable.interval(0, 1, TimeUnit.MINUTES),
                        Observable.just("AAPL"),
                        getTickerData
                )
                        .doOnNext(r -> log("combineLatest 3 molesting"))
                        .doOnDispose(() -> log("combineLatest 3 disposed"))
        )
                .doOnDispose(() -> log("merge disposed"))
                .compose(bindToLifecycle())
                /*.flatMap(r ->
                    Observable.<Observable<AlphaVantageGlobalQuote>>error(new RuntimeException("Crash"))
                )*/
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> {
                    Toast.makeText(this, "We couldn't reach internet - falling back to local data", Toast.LENGTH_SHORT)
                            .show();
                })
                .observeOn(Schedulers.io())
                .map(r -> r.blockingSingle().getQuote())
                .map(StockUpdate::create)
                .observeOn(AndroidSchedulers.mainThread())
                // uncomment this to test errors
                /*.doOnNext(r -> {
                    throw new RuntimeException();
                })*/
                .subscribe(
                        stockUpdate -> {
                            Log.d(TAG, "New update" + stockUpdate.getStockSymbol());
                        }, throwable -> {
                        /*Log.d(TAG, "error");
                        Log.d(TAG, throwable.toString());*/
                        });

    }

    private void log(String text) {
        Log.d(TAG, text);
    }

}
