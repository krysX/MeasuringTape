package com.amv7mz.androidbeadando;

import android.icu.text.SimpleDateFormat;

import androidx.annotation.NonNull;

import java.util.Date;
import java.util.Locale;

public class Measurement {
    private long timestamp;
    private String name;
    private float value;

    public Measurement(long timestamp, String name, float value) {
        this.timestamp = timestamp;
        this.name = name;
        this.value = value;
    }

    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date(timestamp));
    }

    public String getName() {
        return name;
    }

    public float getValue() {
        return value;
    }

    public String getValueAsString() {
        return String.format(Locale.getDefault(), "%.1f", value);
    }

    @NonNull
    public String toString() {
        return name + ": " + getValueAsString() + " cm (" + getDateString() + ")";
    }
}
