package com.example.martin.myapplication;

import android.util.Log;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by martin on 7/23/20.
 */

public class ErrorHandler implements Consumer<Throwable> {

    private static final ErrorHandler INSTANCE = new ErrorHandler();

    public static ErrorHandler get() {
        return INSTANCE;
    }

    private ErrorHandler() {

    }

    @Override
    public void accept(@NonNull Throwable throwable) throws Exception {
        Log.d("APP", "Error on " + Thread.currentThread().getName() + ":", throwable);
    }
}
