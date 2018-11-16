package com.ecoone.mindfulmealplanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class AddGreenMealActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CAMERA_IMAGE = 2;

    EditText getMealName;
    private ViewPager viewPager;
    private AddPhotoFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_green_meal);


        viewPager = findViewById(R.id.image_pager);
        viewPager.setOffscreenPageLimit(6);
        adapter = new AddPhotoFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

    }

    // When user selects image from gallery

}
