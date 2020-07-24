package com.example.martin.myapplication.storio;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.example.martin.myapplication.StockUpdate;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

/**
 * Created by martin on 7/23/20.
 */

public class StockUpdatePutResolver extends DefaultPutResolver<StockUpdate> {
    @NonNull
    @Override
    protected InsertQuery mapToInsertQuery(@NonNull StockUpdate object) {
        return InsertQuery.builder()
                .table(StockUpdateTable.TABLE)
                .build();
    }

    @NonNull
    @Override
    protected UpdateQuery mapToUpdateQuery(@NonNull StockUpdate object) {
        return UpdateQuery.builder()
                .table(StockUpdateTable.TABLE)
                .where(StockUpdateTable.Columns.ID + " = ?")
                .whereArgs(object.getId())
                .build();
    }

    @NonNull
    @Override
    protected ContentValues mapToContentValues(@NonNull StockUpdate object) {
        final ContentValues contentValues = new ContentValues();

        contentValues.put(StockUpdateTable.Columns.ID, object.getId());
        contentValues.put(StockUpdateTable.Columns.STOCK_SYMBOL, object.getStockSymbol());
        contentValues.put(StockUpdateTable.Columns.PRICE, getPrice(object));
        contentValues.put(StockUpdateTable.Columns.DATE, getDate(object));
        contentValues.put(StockUpdateTable.Columns.TWITTER_STATUS, object.getTwitterStatus());

        return contentValues;
    }

    private long getDate(@NonNull StockUpdate entity) {
        return entity.getDate().getTime();
    }

    private long getPrice(@NonNull StockUpdate entity) {
        return entity.getPrice().scaleByPowerOfTen(4).longValue();
    }

}
