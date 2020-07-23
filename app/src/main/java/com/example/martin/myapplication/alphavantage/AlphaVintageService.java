package com.example.martin.myapplication.alphavantage;

import com.example.martin.myapplication.alphavantage.json.AlphaVantageGlobalQuote;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by martin on 7/23/20.
 */

public interface AlphaVintageService {

    @GET("query?function=GLOBAL_QUOTE")
    Single<AlphaVantageGlobalQuote> get(
            @Query("symbol") String symbol,
            @Query("apikey") String apiKey
    );
}
