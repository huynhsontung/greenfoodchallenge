package com.ecoone.mindfulmealplanner.Profile.Setting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.ecoone.mindfulmealplanner.R;

//...
public class AboutPageActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }

    public static Intent makeIntent(Context openAboutPage){
        return new Intent(openAboutPage, AboutPageActivity.class);
    }
}
