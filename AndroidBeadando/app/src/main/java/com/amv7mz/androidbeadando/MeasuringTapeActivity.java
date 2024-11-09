package com.amv7mz.androidbeadando;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventCallback;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.Locale;

public class MeasuringTapeActivity extends AppCompatActivity implements SensorEventListener {

    TextView startStop;


    SensorManager sm;
    Sensor linearAccelerometer;


    boolean isCalibrated;
    float offsetY;
    float accCutoff = 0.01f;
    boolean isMeasuring;
    float acc, vel, dist;
    float t0, t1, deltaT;
    final float NANOS_IN_MILLIS = 1000_000f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_measuring_tape);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        startStop = findViewById(R.id.startStopButton);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        linearAccelerometer = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        isCalibrated = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        acc = dist = vel = 0.0f;
        t0 = t1 = deltaT = 0.0f;
    }

    public void startStopMeasurement(View view) {
//        if (!isCalibrated) {
//            sm
//        }
        isMeasuring = !isMeasuring;
        if(!isMeasuring) {
            startStop.setText("Start");
            sm.unregisterListener(this, linearAccelerometer);
            Intent i = new Intent(MeasuringTapeActivity.this, SaveMeasurementActivity.class);
            i.putExtra("distance_cm", Math.abs(dist * 100.0f));
            startActivity(i);
        } else {
            sm.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_GAME);
            t0 = SystemClock.elapsedRealtimeNanos();
            startStop.setText("Stop");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType() != Sensor.TYPE_LINEAR_ACCELERATION)
            return;

        if(!isCalibrated) {
            offsetY = event.values[0];
            t0 = event.timestamp;
            isCalibrated = true;
            return;
        }

        t1 = (float) event.timestamp;
        deltaT = (t1 - t0) / NANOS_IN_MILLIS / 1000.0f;

        acc = event.values[0] - offsetY;
        // Using the x-axis
        String debugInfo = String.format(Locale.ENGLISH,"acc = %f\tvel = %f\tdist = %f\tdeltaT = %f", acc, vel, dist, deltaT);
        Log.d("MeasuringTapeActivity", "onSensorChanged: " + debugInfo);
        vel += acc * deltaT;
        dist += vel * deltaT;
        startStop.setText(Integer.toString((int)(dist * 100.0f)));

        t0 = t1;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
