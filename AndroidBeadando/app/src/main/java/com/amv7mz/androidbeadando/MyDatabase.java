package com.amv7mz.androidbeadando;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.util.Calendar;

public class MyDatabase {

    private static SQLiteDatabase dbHelper;

    private static final String[] ALL_COLUMNS = new String[] {
            DatabaseHelper.COLUMN_NAME_TIMESTAMP,
            DatabaseHelper.COLUMN_NAME_NAME,
            DatabaseHelper.COLUMN_NAME_VALUE
    };

    public MyDatabase(Context context) {
        dbHelper = new DatabaseHelper(context).getWritableDatabase();
    }

    public void saveMeasurement(String name, float value) {
        Cursor c = dbHelper.query(DatabaseHelper.TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);

        long timestamp = Calendar.getInstance().getTimeInMillis();

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME_TIMESTAMP, timestamp);
        values.put(DatabaseHelper.COLUMN_NAME_NAME, name);
        values.put(DatabaseHelper.COLUMN_NAME_VALUE, value);
        dbHelper.insert(DatabaseHelper.TABLE_NAME, null, values);
        c.close();
    }

    public Cursor getCursor() {
        Cursor c = dbHelper.query(DatabaseHelper.TABLE_NAME, ALL_COLUMNS, null, null, null, null, null);
        if(c.moveToFirst() || c.getCount() > 0) {
            c.moveToFirst();
        } else {
            c.close();
            return null;
        }
        return c;
    }

    public void closeDb() {
        dbHelper.close();
    }

}
