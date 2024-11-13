package com.amv7mz.androidbeadando;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "measurements.db";
    public static final String TABLE_NAME = "measurements";
    public static final String COLUMN_NAME_TIMESTAMP = "timestamp";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_VALUE = "value";

    public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
            " (" + COLUMN_NAME_TIMESTAMP + " INTEGER PRIMARY KEY, " +
            COLUMN_NAME_NAME + " TEXT, " +  COLUMN_NAME_VALUE + " REAL)";

    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
