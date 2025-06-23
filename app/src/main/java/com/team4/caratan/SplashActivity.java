package com.team4.caratan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

public class SplashActivity extends AppCompatActivity {

    private mDBhandler dbHandler;
    private Users users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();
        getWindow().setStatusBarColor(Color.WHITE);

        if(SharedPrefManager.getInstance(getApplicationContext()).isLoggedIn()) {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //overridePendingTransition(0,0);
                finish();
            },2000);
        } else {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            },2000);
        }
    }
}