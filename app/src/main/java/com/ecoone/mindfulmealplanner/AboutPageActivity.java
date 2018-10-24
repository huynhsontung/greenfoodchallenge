package com.ecoone.mindfulmealplanner.fragments;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ecoone.mindfulmealplanner.R;
//...
public class aboutpage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutpage);
    }

    public static Intent makeIntent(Context openaboutpage){
        return new Intent(openaboutpage, aboutpage.class);
    }
}
