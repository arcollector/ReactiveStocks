package com.example.martin.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.trello.rxlifecycle2.RxLifecycle;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by martin on 7/23/20.
 */

public class ExampleLifecycleActivity extends RxAppCompatActivity {

    private static final String TAG = "ExampleLifecycleActivit";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

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

    }
}
