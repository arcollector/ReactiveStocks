package com.example.martin.myapplication.storio;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.martin.myapplication.StockUpdate;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

/**
 * Created by martin on 7/23/20.
 */

public class StockUpdateDeleteResolver extends DefaultDeleteResolver<StockUpdate> {

    @NonNull
    @Override
    protected DeleteQuery mapToDeleteQuery(@NonNull StockUpdate object) {
        return DeleteQuery.builder()
                .table(StockUpdateTable.TABLE)
                .where(StockUpdateTable.Columns.ID + " = ?")
                .whereArgs(object.getId())
                .build();
    }
}
