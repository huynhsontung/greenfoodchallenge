package com.ecoone.mindfulmealplanner.Setting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ecoone.mindfulmealplanner.R;

//...
public class AboutPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
    }

    public static Intent makeIntent(Context openAboutPage){
        return new Intent(openAboutPage, AboutPageActivity.class);
    }
}
