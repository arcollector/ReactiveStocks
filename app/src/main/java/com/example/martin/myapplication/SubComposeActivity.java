package com.example.martin.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by martin on 7/23/20.
 */

public class SubComposeActivity extends AppCompatActivity {

    private CompositeDisposable disposable;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        Disposable disposable1 = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe();

        Disposable disposable2 = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe();

        Disposable disposable3 = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe();

        disposable = new CompositeDisposable();
        disposable.addAll(disposable1, disposable2, disposable3);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(disposable != null) {
            disposable.dispose();
        }
    }
}
