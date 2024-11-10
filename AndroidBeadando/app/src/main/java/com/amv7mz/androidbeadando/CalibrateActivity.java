package com.amv7mz.androidbeadando;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CalibrateActivity extends AppCompatActivity implements SensorEventListener {

    final float NANOS_IN_S = 1000f * 1000f * 1000f;
    final float NANOS_IN_MILLIS = 1000f * 1000f;

    public static final String SHARED_PREF_NAME = "CalibrationSettings";

    SensorManager sm;
    Sensor linearAccelerometer;
    float value, prevValue;
    float minNoiseValue, maxNoiseValue;
    float sumNoiseValue, avgNoiseValue;
    int n_Oscillations, n_Measurements;
    float lastOscT, t1;
    float startT;
    final float TOTAL_T = 10.0f * NANOS_IN_S;

    float sumOscInterval, avgOscInterval, avgOscFreq;

    TextView calibrateBtn, calibrationDataDisplay;

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calibrate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        linearAccelerometer = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        calibrateBtn = findViewById(R.id.calibrateBtn);
        calibrationDataDisplay = findViewById(R.id.calibrationDataDisplay);
        calibrationDataDisplay.setText("Ide kerülnek a kalibrálási adatok...");

        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void onCalibrateStart(View v) {
        lastOscT = startT = SystemClock.elapsedRealtimeNanos();
        n_Oscillations = 0;
        calibrateBtn.setText("Folyamatban...");
        calibrateBtn.setEnabled(false);
        sm.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    void onCalibrateStop(){
        sm.unregisterListener(this, linearAccelerometer);
        sumOscInterval /= NANOS_IN_MILLIS; // convert to ms
        avgOscInterval = sumOscInterval / (float)n_Oscillations ;
        avgOscFreq = 1000f / avgOscInterval;
        float totalNoiseRange = maxNoiseValue - minNoiseValue;
        avgNoiseValue = sumNoiseValue / (float)n_Measurements;

        String output = "avgOscInterval: " + avgOscInterval + " ms\n" +
                "avgOscFreq: " + avgOscFreq + "Hz\n" +
                "maxNoiseValue: " + maxNoiseValue + "\n" +
                "minNoiseValue: " + minNoiseValue + "\n" +
                "totalNoiseRange: " + totalNoiseRange + "\n" +
                "avgNoiseValue: " + avgNoiseValue + "\n";

        sharedPreferences.edit().putFloat("avgNoiseValue", avgNoiseValue).apply();

        calibrationDataDisplay.setText(output);
        calibrateBtn.setText("Kész!");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION)
            return;

        t1 = event.timestamp;
        if(t1 - startT > TOTAL_T) {
            onCalibrateStop();
            return;
        }

        value = event.values[0];
        sumNoiseValue += value;
        n_Measurements++;

        // min, max keresés
        if(value < minNoiseValue) {
            minNoiseValue = value;
        } else if (value > maxNoiseValue) {
            maxNoiseValue = value;
        }

        boolean hasSignChanged = prevValue < 0 && value >= 0 || prevValue >= 0 && value < 0;
        if(hasSignChanged) {
            sumOscInterval += t1 - lastOscT;
            n_Oscillations++;
            lastOscT = t1;
            prevValue = value;
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // csak az interface miatt van
    }
}