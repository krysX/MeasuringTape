package com.amv7mz.androidbeadando;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class SaveMeasurementActivity extends AppCompatActivity {

    Bundle extras;

    TextView amountTextView;

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
        extras = getIntent().getExtras();
        String text = "-";
        if (extras != null) {
            float value = extras.getFloat("distance_cm");
            text = String.format(Locale.ENGLISH,"%.1f", value);
        }
        amountTextView.setText(text);
    }
}