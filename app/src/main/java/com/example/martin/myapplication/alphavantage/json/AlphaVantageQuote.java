package com.example.martin.myapplication.alphavantage.json;

import android.icu.math.BigDecimal;

import com.google.gson.annotations.SerializedName;

/**
 * Created by martin on 7/23/20.
 */

public class AlphaVantageQuote {

    @SerializedName("01. symbol")
    private String symbol;

    @SerializedName("02. low")
    private Double daysLow;

    @SerializedName("03. high")
    private Double daysHigh;

    @SerializedName("06. volume")
    private String volume;

    @SerializedName("05. price")
    private Double lastTradePriceOnly;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Double getDaysLow() {
        return daysLow;
    }

    public void setDaysLow(Double daysLow) {
        this.daysLow = daysLow;
    }

    public Double getDaysHigh() {
        return daysHigh;
    }

    public void setDaysHigh(Double daysHigh) {
        this.daysHigh = daysHigh;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public Double getLastTradePriceOnly() {
        return lastTradePriceOnly;
    }

    public void setLastTradePriceOnly(Double lastTradePriceOnly) {
        this.lastTradePriceOnly = lastTradePriceOnly;
    }
}
