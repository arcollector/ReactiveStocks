package com.example.martin.myapplication;

import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;

/**
 * Created by martin on 7/24/20.
 */

public class LoggerTransformer<R> implements ObservableTransformer<R, R> {

    private final String tag;

    private void log(String stage, Object item) {
        Log.d(
            "APP-DEBUG:" + tag,
            stage + ":" + Thread.currentThread().getName() + ":" + item
        );
    }

    private void log(String stage, Throwable throwable) {
        Log.e(
            "APP-DEBUG:" + tag,
            stage + ":" + Thread.currentThread().getName() + ":error",
            throwable
        );
    }

    public LoggerTransformer(String tag) {
        this.tag = tag;
    }

    public static <R> LoggerTransformer<R> debugLog(String tag) {
        return new LoggerTransformer<>(tag);
    }

    @Override
    public ObservableSource<R> apply(@NonNull Observable<R> upstream) {
        return upstream
            .doOnNext(v -> log("doOnNext", v))
            .doOnError(error -> log("doOnError", error))
            .doOnComplete(() -> log("doOnCOmplete", upstream.toString()))
            .doOnTerminate(() -> log("doOnTerminate", upstream.toString()))
            .doOnDispose(() -> log("doOnDisponse", upstream.toString()))
            .doOnSubscribe(v -> log("doOnSubscribe", upstream.toString()));
    }
}
