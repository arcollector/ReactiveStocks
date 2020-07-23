package com.example.martin.myapplication;

import java.io.Serializable;

import com.example.martin.myapplication.alphavantage.json.AlphaVantageQuote;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by martin on 7/23/20.
 */

public class StockUpdate implements Serializable {

    private final String stockSymbol;
    private final BigDecimal price;
    private final Date date;
    private Integer id;

    StockUpdate(String stockSymbol, BigDecimal price, Date date) {
        this.stockSymbol = stockSymbol;
        this.price = price;
        this.date = date;
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

    public static StockUpdate create(AlphaVantageQuote r) {
        return new StockUpdate(r.getSymbol(), new BigDecimal(r.getLastTradePriceOnly()), new Date());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
