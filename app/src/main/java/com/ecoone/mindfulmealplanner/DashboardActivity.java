package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DashboardActivity extends AppCompatActivity {


    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, TestActivity.class);
//        intent.putExtra(EXTRA_USERNAME, userName);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }
}
