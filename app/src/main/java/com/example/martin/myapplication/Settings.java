package com.example.martin.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences2.RxSharedPreferences;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

import static com.example.martin.myapplication.LoggerTransformer.debugLog;

/**
 * Created by martin on 7/24/20.
 */

public class Settings {

    private static Settings INSTANCE;

    private Subject<List<String>> keywordsSubject = BehaviorSubject.create();
    private Subject<List<String>> symbolsSubject = BehaviorSubject.create();
    /**
     * Hint: Keep a strong reference on your RxSharedPreferences
     * instance for as long as you want to observe them to prevent
     * listeners from being GCed.
     */
    private final RxSharedPreferences rxPreferences;

    private Settings(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        rxPreferences = RxSharedPreferences.create(preferences);

        rxPreferences.getString("pref_keywords", "").asObservable()
                .compose(debugLog("pref_keywords"))
                .filter(v -> !v.isEmpty())
                .map(value -> value.split(" "))
                .map(Arrays::asList)
                .subscribe(keywordsSubject);

        rxPreferences.getString("pref_symbols", "").asObservable()
                .compose(debugLog("pref_symbols"))
                .filter(v -> !v.isEmpty())
                .map(String::toUpperCase)
                .map(value -> value.split(" "))
                .map(Arrays::asList)
                .subscribe(symbolsSubject);
    }

    public synchronized static Settings get(Context context) {
        if(INSTANCE != null) {
            return INSTANCE;
        }
        INSTANCE = new Settings(context);
        return INSTANCE;
    }

    public Observable<List<String>> getMonitoredKeywords() {
        return keywordsSubject;
    }

    public Observable<List<String>> getMonitoredSymbols() {
        return symbolsSubject;
    }
}
