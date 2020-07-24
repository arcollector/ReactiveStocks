package com.example.martin.myapplication;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Created by martin on 7/23/20.
 */

public class MockActivity extends AppCompatActivity {

    private static final String TAG = "MockActivity";

    /*
    private String[] bigArray = new String[10000000];
    private static final List<MockActivity> INSTANCES = new ArrayList();
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);
        bigArray[0] = "test";
        INSTANCES.add(this);
        Log.i(TAG, "Activity Created");
    }
    */

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mock);

        Observable.interval(0, 2, TimeUnit.SECONDS)
                .subscribe(i -> Log.i(TAG, "Instance" + this.toString() + " reporting"));

        Log.i(TAG, "Activity Created");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "Activity Destroyed");
    }
}
