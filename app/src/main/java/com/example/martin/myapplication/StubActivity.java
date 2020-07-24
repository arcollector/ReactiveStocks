package com.example.martin.myapplication;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by martin on 7/23/20.
 */

public class StubActivity extends AppCompatActivity {

    private Disposable subscribe;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        subscribe = Observable.interval(1, TimeUnit.SECONDS)
                .subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(subscribe != null) {
            subscribe.dispose();
        }
    }
}
