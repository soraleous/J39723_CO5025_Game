package com.example.j39723.j39723_co5025_game;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Code adapted from https://www.youtube.com/watch?v=cakkKQYYCvQ
        int time = 3000; // For 3 Seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Runs LoginActivity afterwards
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        },time);
        // End of adapted code

    }
}
