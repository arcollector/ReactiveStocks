package com.example.martin.myapplication.alphavantage.json;

import com.google.gson.annotations.SerializedName;

/**
 * Created by martin on 7/23/20.
 */

public class AlphaVantageGlobalQuote {

    @SerializedName("Global Quote")
    private AlphaVantageQuote quote;

    public AlphaVantageQuote getQuote() {
        return quote;
    }
}
