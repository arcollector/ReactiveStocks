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
}
