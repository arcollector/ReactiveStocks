package com.example.martin.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.martin.myapplication.alphavantage.AlphaVantageServiceFactory;
import com.example.martin.myapplication.alphavantage.AlphaVintageService;
import com.example.martin.myapplication.alphavantage.json.AlphaVantageGlobalQuote;
import com.example.martin.myapplication.storio.StorIOFactory;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.MaybeSource;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;

import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class MainActivity extends RxAppCompatActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.no_data_available)
    TextView noDataAvailableView;

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

    private Configuration initTwitter() {
        /*
        StatusListener listener = new StatusListener() {

            @Override
            public void onException(Exception ex) {
                Log.d("TWITTER", "onException!");
                ex.printStackTrace();
            }

            @Override
            public void onStatus(Status status) {
                Log.d("TWITTER", status.getUser().getName() + " : " + status.getText());
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {

            }

            @Override
            public void onStallWarning(StallWarning warning) {

            }
        };
        */

        Configuration configuration = new ConfigurationBuilder()
                .setDebugEnabled(true)
                .setOAuthConsumerKey(BuildConfig.TWITTER_CONSUMER_API_KEY)
                .setOAuthConsumerSecret(BuildConfig.TWITTER_CONSUMER_API_SECRET_KEY)
                .setOAuthAccessToken(BuildConfig.TWITTER_ACCESS_TOKEN)
                .setOAuthAccessTokenSecret(BuildConfig.TWITTER_ACCESS_TOKEN_SECRET)
                .build();

        /*
        TwitterStream twitterStream = new TwitterStreamFactory(configuration)
                .getInstance();
        twitterStream.addListener(listener);
        twitterStream.filter();
        twitterStream.filter(
                new FilterQuery()
                        .track("Twitter", "Google", "Apple")
                        .language("en")
        );
        */

        return configuration;
    }

    private FilterQuery getTwitterFilterQuery() {
        return new FilterQuery()
                .track("Twitter", "Google", "Apple")
                .language("en");
    }

    private BiFunction<Long, String, Observable<AlphaVantageGlobalQuote>> initAlphaVantage() {
        AlphaVintageService alphaVintageService = new AlphaVantageServiceFactory().create();

        String alphavantageApiKey = BuildConfig.ALPHAVANTAGE_API_KEY;

        BiFunction<Long, String, Observable<AlphaVantageGlobalQuote>> getTickerData =
                (integer, s) -> alphaVintageService
                        .get(s, alphavantageApiKey)
                        .toObservable();

        return getTickerData;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        RxJavaPlugins.setErrorHandler(ErrorHandler.get());

        recycleView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(layoutManager);

        stockDataAdapter = new StockDataAdapter();
        recycleView.setAdapter(stockDataAdapter);

        Observable.just("Please use this app responsibly!")
                .subscribe(s -> helloText.setText(s));

        log("creating main observable");
        Observable.merge(
            createFinancialStockUpdateObservable(
                    Arrays.asList("GOOG", "TWTR", "AAPL")
            ),
            createTweetStockUpdateObservable(
                    initTwitter(),
                    getTwitterFilterQuery(),
                    Arrays.asList("twitter", "google", "apple")
            )
        )
                .doOnDispose(() -> log("merge disposed"))
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(error -> {
                    log("doOnError", "error");
                    Toast.makeText(this, "We couldn't reach internet - falling back to local data", Toast.LENGTH_SHORT)
                            .show();
                })
                .observeOn(Schedulers.io())
                .doOnNext(this::saveStockUpdate)
                .onExceptionResumeNext(StorIOFactory.createLocalDbStockUpdateRetrievalObservable(this))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    stockUpdate -> {
                        Log.d(TAG, "New update" + stockUpdate.getStockSymbol());
                        noDataAvailableView.setVisibility(View.GONE);
                        stockDataAdapter.add(stockUpdate);
                    }, throwable -> {
                        if(stockDataAdapter.getItemCount() == 0) {
                            noDataAvailableView.setVisibility(View.VISIBLE);
                        }
                        /*Log.d(TAG, "error");
                        Log.d(TAG, throwable.toString());*/
                    });

        // region without merge parent
/*
        Disposable google = Observable.interval(0, 1, TimeUnit.MINUTES)
                .flatMap(
                        i -> alphaVintageService.get("GOOG", alphavantageApiKey).toObservable()
                )
                .subscribeOn(Schedulers.io())
                .map(r -> Arrays.asList(r.getQuote()))
                .flatMap(Observable::fromIterable)
                .map(StockUpdate::create)
                .doOnNext(this::saveStockUpdate)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(stockUpdate -> {
                    Log.d(TAG, "New update" + stockUpdate.getStockSymbol());
                    stockDataAdapter.add(stockUpdate);
                });

        Disposable appl = Observable.interval(0, 1, TimeUnit.MINUTES)
                .flatMap(
                        i -> alphaVintageService.get("AAPL", alphavantageApiKey)
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
                        i -> alphaVintageService.get("TWTR", alphavantageApiKey).toObservable()
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
*/
        // endregion

        // region first steps
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
        // endregion
    }

    private Observable<StockUpdate> createTweetStockUpdateObservable(
            Configuration twitterConfiguration,
            FilterQuery twitterFilterQuery,
            List<String> trackingKeywords
    ) {
        return observeTwitterStream(twitterConfiguration, twitterFilterQuery)
                .sample(2700, TimeUnit.MILLISECONDS)
                .map(StockUpdate::create)
                .flatMapMaybe(filterTweetsBasedOnKeywords(trackingKeywords));

    }

    @android.support.annotation.NonNull
    private Function<StockUpdate, MaybeSource<? extends StockUpdate>> filterTweetsBasedOnKeywords(List<String> trackingKeywords) {
        return r ->
            Observable
                .fromIterable(trackingKeywords)
                .filter(keyword ->
                    r.getTwitterStatus().toLowerCase().contains(keyword)
                )
                .map(keyword -> r)
                .firstElement();
    }

    private Observable<StockUpdate> createFinancialStockUpdateObservable(List<String> tickers) {
        List<Observable<Observable<AlphaVantageGlobalQuote>>> stockObservables = new ArrayList<>();
        tickers.forEach(
            ticker -> stockObservables.add(
                createObservableRetrieveStockData(ticker)
            )
        );
        return Observable
            .merge(stockObservables)
            .flatMap(r -> r)
            .map(AlphaVantageGlobalQuote::getQuote)
            .map(StockUpdate::create)
            .groupBy(r -> r.getStockSymbol())
            .flatMap(r -> r.distinctUntilChanged());
    }

    private Observable<Observable<AlphaVantageGlobalQuote>> createObservableRetrieveStockData(
            String tickerSymbol
    ) {
        BiFunction<Long, String, Observable<AlphaVantageGlobalQuote>> getTickerData = initAlphaVantage();
        /*
        BiFunction<Long, String, Observable<AlphaVantageGlobalQuote>> getTickerData =
            (integer, s) ->
                    Observable
                            .<AlphaVantageGlobalQuote>error(
                                    new RuntimeException("Crash")
                            );
        */
        return Observable.combineLatest(
            Observable.interval(0, 1, TimeUnit.MINUTES),
            Observable.just(tickerSymbol),
            getTickerData
        );
    }

    private void log(String tag, String text) {
        Log.d(tag, text);
    }

    private void log(String text) {
        Log.d(TAG, text);
    }

    private void saveStockUpdate(StockUpdate stockUpdate) {
        log("saveStockUpdate", stockUpdate.getStockSymbol());
        StorIOFactory.get(this)
                .put()
                .object(stockUpdate)
                .prepare()
                .asRxSingle()
                .subscribe();
    }

    @OnClick(R.id.start_another_activity_button)
    public void onStartAnotherActivityButtonClick(Button button) {
        startActivity(new Intent(this, ExampleLifecycleActivity.class));
    }

    Observable<Status> observeTwitterStream(Configuration configuration, FilterQuery filterQuery) {
        return Observable.create(emitter -> {
            final TwitterStream twitterStream = new TwitterStreamFactory(configuration).getInstance();

            emitter.setCancellable(() -> {
                Schedulers.io().scheduleDirect(() -> twitterStream.cleanUp());
            });

            StatusListener listener = new StatusListener() {
                @Override
                public void onStatus(Status status) {
                    emitter.onNext(status);
                }

                @Override
                public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

                }

                @Override
                public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

                }

                @Override
                public void onScrubGeo(long userId, long upToStatusId) {

                }

                @Override
                public void onStallWarning(StallWarning warning) {

                }

                @Override
                public void onException(Exception ex) {
                    Log.d("TWITTER", "fail observerTwitterStream");
                    ex.printStackTrace();
                }
            };

            twitterStream.addListener(listener);
            twitterStream.filter(filterQuery);
        });
    }
}
