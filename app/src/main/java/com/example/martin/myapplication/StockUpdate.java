package com.example.martin.myapplication;

import java.io.Serializable;
import android.icu.math.BigDecimal;
import java.util.Date;

/**
 * Created by martin on 7/23/20.
 */

public class StockUpdate implements Serializable {

    private final String stockSymbol;
    private final BigDecimal price;
    private final Date date;

    StockUpdate(String stockSymbol, double price, Date date) {
        this.stockSymbol = stockSymbol;
        this.price = new BigDecimal(price);
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

}
