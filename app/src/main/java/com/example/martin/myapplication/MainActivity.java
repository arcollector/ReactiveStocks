package com.example.martin.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.martin.myapplication.alphavantage.AlphaVantageServiceFactory;
import com.example.martin.myapplication.alphavantage.AlphaVintageService;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.hello_world_salute)
    TextView helloText;

    @BindView(R.id.stock_updates_recycler_view)
    RecyclerView recycleView;
    private LinearLayoutManager layoutManager;
    private StockDataAdapter stockDataAdapter;

    public void demo() {
        Observable.just("1")
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.d(TAG, "Hello " + s);
                    }
                });

        Observable.just("1")
                .map(new Function<String, String>() {
                    @Override
                    public String apply(@NonNull String s) {
                        return s + "mapped";
                    }
                })
                .flatMap(new Function<String, Observable<String>>() {
                    @Override
                    public Observable<String> apply(@NonNull String s) throws Exception {
                        return Observable.just("flat-" + s);
                    }
                })
                .doOnNext(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) {
                        Log.d(TAG, "on next " + s);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String s) throws Exception {
                        Log.d(TAG, "Hello " + s);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {
                        Log.d(TAG, "Error!");
                    }
                });

        Observable.just("1")
                .map(s -> s + "mapped")
                .flatMap(s -> Observable.just("flat-" + s))
                .doOnNext(s -> Log.d(TAG, "on next " + s))
                .subscribe(
                        e -> Log.d(TAG, "Hello " + e),
                        t -> Log.d(TAG, "Error!")
                );

        Observable.just("1")
                .subscribe(e -> Log.d(TAG, "Hello " + e));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        recycleView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);

        stockDataAdapter = new StockDataAdapter();
        recycleView.setAdapter(stockDataAdapter);

        Observable.just("Please use this app responsibly!")
                .subscribe(s -> helloText.setText(s));

        AlphaVintageService alphaVintageService = new AlphaVantageServiceFactory().create();

        String apiKey = "U6EHDPZAEKU4H1QH";

        Disposable google = Observable.interval(0, 1, TimeUnit.MINUTES)
                .flatMap(
                        i -> alphaVintageService.get("GOOG", apiKey).toObservable()
                )
                .subscribeOn(Schedulers.io())
                .map(r -> Arrays.asList(r.getQuote()))
                .flatMap(Observable::fromIterable)
                .map(r -> StockUpdate.create(r))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stockUpdate -> {
                    Log.d(TAG, "New update" + stockUpdate.getStockSymbol());
                    stockDataAdapter.add(stockUpdate);
                });

        Disposable appl = Observable.interval(0, 1, TimeUnit.MINUTES)
                .flatMap(
                        i -> alphaVintageService.get("AAPL", apiKey)
                                .toObservable()
                )
                .subscribeOn(Schedulers.io())
                .map(r -> Arrays.asList(r.getQuote()))
                .flatMap(Observable::fromIterable)
                .map(r -> StockUpdate.create(r))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stockUpdate -> {
                    Log.d(TAG, "New update" + stockUpdate.getStockSymbol());
                    stockDataAdapter.add(stockUpdate);
                });

        Disposable twtr = Observable.interval(0, 1, TimeUnit.MINUTES)
                .flatMap(
                        i -> alphaVintageService.get("TWTR", apiKey).toObservable()
                )
                .subscribeOn(Schedulers.io())
                .map(r -> Arrays.asList(r.getQuote()))
                .flatMap(Observable::fromIterable)
                .map(r -> StockUpdate.create(r))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stockUpdate -> {
                    Log.d(TAG, "New update" + stockUpdate.getStockSymbol());
                    stockDataAdapter.add(stockUpdate);
                });

        /*
        Observable.just(
                new StockUpdate("GOOGLE", 12.43, new Date()),
                new StockUpdate("APPL", 645.1, new Date()),
                new StockUpdate("TWTR", 1.43, new Date())
        ).subscribe(stockUpdate -> {
            Log.d(TAG, "New update " + stockUpdate.getStockSymbol());
            stockDataAdapter.add(stockUpdate);
        });
        */
    }

    private void log(String text) {
        Log.d(TAG, text);
    }
}
