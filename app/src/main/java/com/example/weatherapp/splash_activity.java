package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class splash_activity extends AppCompatActivity {
    private static int time_out = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent homeIntent = new Intent( splash_activity.this,MainActivity.class);
                    startActivity(homeIntent);
                    finish();
            }
        },time_out);
    }
}