package com.amv7mz.androidbeadando;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

public class SaveMeasurementActivity extends AppCompatActivity {

    Bundle extras;

    TextView amountTextView, nameInput;

    MyDatabase db;

    float value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_save_measurement);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        amountTextView = findViewById(R.id.amountTextView);
        nameInput = findViewById(R.id.nameInput);

        extras = getIntent().getExtras();
        String text = "-";
        if (extras != null) {
            value = extras.getFloat("distance_cm");
            text = String.format(Locale.ENGLISH,"%.1f", value);
        }
        amountTextView.setText(text);

        db = new MyDatabase(this);
//        db = openOrCreateDatabase("measurements.db", SQLiteDatabase.CREATE_IF_NECESSARY, null);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.closeDb();
    }

    public void saveMeasurement(View view) {
        String name = nameInput.getText().toString();
        db.saveMeasurement(name, value);

        Toast.makeText(this, "Mérés sikeresen elmentve!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SaveMeasurementActivity.this, MainActivity.class));
    }
}