package com.ecoone.mindfulmealplanner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ecoone.mindfulmealplanner.setup.InitialSetupActivity;

public class SplashScreen extends AppCompatActivity {

    // Shows splash screen until app is loaded
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Intent intent = new Intent(getApplicationContext(),InitialSetupActivity.class);
        startActivity(intent);
        finish();
    }
}
