package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class AddGreenMealPhotoActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CAMERA_IMAGE = 2;

    EditText getMealName;
    private ViewPager viewPager;
    private AddPhotoFragmentAdapter adapter;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AddGreenMealPhotoActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_green_meal_photo);


        viewPager = findViewById(R.id.image_pager);
        viewPager.setOffscreenPageLimit(6);
        adapter = new AddPhotoFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

    }

    // When user selects image from gallery

}
