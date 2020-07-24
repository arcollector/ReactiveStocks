package com.example.martin.myapplication;

import java.io.Serializable;

import com.example.martin.myapplication.alphavantage.json.AlphaVantageQuote;

import java.math.BigDecimal;
import java.util.Date;

import twitter4j.Status;

/**
 * Created by martin on 7/23/20.
 */

public class StockUpdate implements Serializable {

    private final String stockSymbol;
    private final BigDecimal price;
    private final Date date;
    private final String twitterStatus;
    private Integer id;

    public StockUpdate(String stockSymbol, BigDecimal price, Date date, String twitterStatus) {
        if(stockSymbol == null) {
            stockSymbol = "";
        }
        if(twitterStatus == null) {
            twitterStatus = "";
        }

        this.stockSymbol = stockSymbol;
        this.price = price;
        this.date = date;
        this.twitterStatus = twitterStatus;
    }

    public static StockUpdate create(AlphaVantageQuote r) {
        return new StockUpdate(r.getSymbol(), new BigDecimal(r.getLastTradePriceOnly()), new Date(), "");
    }

    public static StockUpdate create(Status status) {
        return new StockUpdate("", BigDecimal.ZERO, status.getCreatedAt(), status.getText());
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Date getDate() {
        return date;
    }

    public String getTwitterStatus() {
        return twitterStatus;
    }

    public boolean isTwiterStatusUpdate() {
        return !twitterStatus.isEmpty();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockUpdate that = (StockUpdate) o;

        if (stockSymbol != null ? !stockSymbol.equals(that.stockSymbol) : that.stockSymbol != null)
            return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (twitterStatus != null ? !twitterStatus.equals(that.twitterStatus) : that.twitterStatus != null)
            return false;
        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        int result = stockSymbol != null ? stockSymbol.hashCode() : 0;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (twitterStatus != null ? twitterStatus.hashCode() : 0);
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }
}
