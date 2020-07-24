package com.example.martin.myapplication.storio;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.constraint.solver.SolverVariable;

import com.example.martin.myapplication.StockUpdate;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by martin on 7/23/20.
 */

public class StockUpdateGetResolver extends DefaultGetResolver<StockUpdate> {
    @NonNull
    @Override
    public StockUpdate mapFromCursor(@NonNull Cursor cursor) {
        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(StockUpdateTable.Columns.ID));
        final long dateLong = cursor.getLong(cursor.getColumnIndexOrThrow(StockUpdateTable.Columns.DATE));
        final long priceLong = cursor.getLong(cursor.getColumnIndexOrThrow(StockUpdateTable.Columns.PRICE));
        final String stockSymbol = cursor.getString(cursor.getColumnIndexOrThrow(StockUpdateTable.Columns.STOCK_SYMBOL));

        Date date = getDate(dateLong);
        BigDecimal price = getPrice(priceLong);

        final StockUpdate stockUpdate = new StockUpdate(stockSymbol, price, date);

        stockUpdate.setId(id);

        return stockUpdate;
    }

    private BigDecimal getPrice(long priceLong) {
        return new BigDecimal(priceLong).scaleByPowerOfTen(-4);
    }

    private Date getDate(long dateLong) {
        return new Date(dateLong);
    }
}
