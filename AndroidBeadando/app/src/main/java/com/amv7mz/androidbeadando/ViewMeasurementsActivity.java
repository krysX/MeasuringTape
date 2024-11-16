package com.amv7mz.androidbeadando;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ViewMeasurementsActivity extends AppCompatActivity {

    ListView listView;
    ArrayAdapter<String> listAdapter;
    ArrayList<String> measurements;


    Cursor cursor;
    MyDatabase myDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_measurements);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        try {
            measurements = new ArrayList<String>();

            listAdapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, measurements);
            listView = findViewById(R.id.listView);
            listView.setAdapter(listAdapter);

            myDatabase = new MyDatabase(this);
            cursor = myDatabase.getCursor();

            while(!cursor.isAfterLast()) {
                long timestamp = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME_TIMESTAMP));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME_NAME));
                float value = cursor.getFloat(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME_VALUE));
                Measurement m = new Measurement(timestamp, name, value);
                measurements.add(m.toString());
                cursor.moveToNext();
            }

//            if(!measurements.isEmpty())
                listAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, R.string.view_history_empty, Toast.LENGTH_LONG);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDatabase.closeDb();
    }
}