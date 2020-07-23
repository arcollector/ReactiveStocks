package com.example.martin.myapplication.storio;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by martin on 7/23/20.
 */

public class StorIODbHelper extends SQLiteOpenHelper {

    public StorIODbHelper(Context context) {
        super(context, "reactivestocks.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(StockUpdateTable.createTableQuery());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
