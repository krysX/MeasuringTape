package com.amv7mz.androidbeadando;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventCallback;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MeasuringTapeActivity extends AppCompatActivity implements SensorEventListener {

    TextView startStop;


    SensorManager sm;
    Sensor linearAccelerometer;


    boolean isMeasuring;
    float acc, vel, dist;
    float t0, t1, deltaT;

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

        //sm.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_UI);

        isMeasuring = false;
    }

    @Override
    protected void onResume() {
        super.onRestart();
        acc = dist = vel = 0.0f;


    }

    public void startStopMeasurement(View view) {
        isMeasuring = !isMeasuring;
        if(!isMeasuring) {
            sm.unregisterListener(this, linearAccelerometer);
            Intent i = new Intent(MeasuringTapeActivity.this, SaveMeasurementActivity.class);
            i.putExtra("distance_cm", dist * 100.0f);
            startActivity(i);
        } else {
            sm.registerListener(this, linearAccelerometer, SensorManager.SENSOR_DELAY_UI);
            t0 = Calendar.getInstance().getTimeInMillis();
            startStop.setText("Stop");
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        t1 = Calendar.getInstance().getTimeInMillis();
        deltaT = (t1 - t0) / 1000.0f;

        float x = event.values[0]; float y = event.values[1]; float z = event.values[2];
        acc = (float) Math.sqrt(x * x + y * y + z * z);

        vel += acc * deltaT;
        dist += vel * deltaT;

        t0 = t1;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
