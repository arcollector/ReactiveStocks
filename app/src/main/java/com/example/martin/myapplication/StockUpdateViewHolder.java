package com.example.martin.myapplication;

import android.icu.math.BigDecimal;
import android.icu.text.DecimalFormat;
import android.icu.text.NumberFormat;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by martin on 7/23/20.
 */

public class StockUpdateViewHolder extends RecyclerView.ViewHolder {

    private static final NumberFormat PRICE_FORMAT = new DecimalFormat("#0.00");

    @BindView(R.id.stock_item_symbol)
    TextView stockSymbol;

    @BindView(R.id.stock_item_date)
    TextView date;

    @BindView(R.id.stock_item_price)
    TextView price;

    StockUpdateViewHolder(View v) {
        super(v);
        ButterKnife.bind(this, v);
    }

    void setStockSymbol(String stockSymbol) {
        this.stockSymbol.setText(stockSymbol);
    }

    void setPrice(BigDecimal price) {
        this.price.setText(PRICE_FORMAT.format(price.floatValue()));
    }

    void setDate(Date date) {
        this.date.setText(DateFormat.format("yyyy-MM-dd hh:mm", date));
    }
}
