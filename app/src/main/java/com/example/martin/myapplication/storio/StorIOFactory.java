package com.example.martin.myapplication.storio;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;

import com.example.martin.myapplication.StockUpdate;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResolver;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;
import com.pushtorefresh.storio.sqlite.operations.get.GetResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

/**
 * Created by martin on 7/23/20.
 */

public class StorIOFactory {

    private static StorIOSQLite INSTANCE;

    public synchronized static StorIOSQLite get(Context context) {
        if(INSTANCE != null) {
            return INSTANCE;
        }

        INSTANCE = DefaultStorIOSQLite.builder()
                .sqliteOpenHelper(new StorIODbHelper(context))
                .addTypeMapping(StockUpdate.class, SQLiteTypeMapping.<StockUpdate>builder()
                    .putResolver(new StockUpdatePutResolver())
                    .getResolver(new StockUpdateGetResolver())
                    .deleteResolver(new StockUpdateDeleteResolver())
                    .build())
                .build();

        return INSTANCE;
    }

    private static GetResolver<StockUpdate> createGetResolver() {
        return new DefaultGetResolver<StockUpdate>() {
            @NonNull
            @Override
            public StockUpdate mapFromCursor(@NonNull Cursor cursor) {
                return null;
            }
        };
    }

    private static DeleteResolver<StockUpdate> createDeleteResolver() {
        return new DefaultDeleteResolver<StockUpdate>() {
            @NonNull
            @Override
            protected DeleteQuery mapToDeleteQuery(@NonNull StockUpdate object) {
                return null;
            }
        };
    }
}
