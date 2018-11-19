package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;

public class AddGreenMealPhotoActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CAMERA_IMAGE = 2;

    private static final String[] GREEN_MEALS = {
            "Pizza", "Beyond Meat Burger", "California Roll", "Vodka"
    };

    private AutoCompleteTextView autoCompleteTextView;
    private ImageView plusFoodItem;

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

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView2);
        plusFoodItem = findViewById(R.id.add_green_meal_photo_addtextview);

        plusFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLine();
            }
        });

    }

    private void addLine() {
        ConstraintLayout cl = (ConstraintLayout)findViewById(R.id.add_green_meal_photo_constraintlayout);
        ConstraintSet set = new ConstraintSet();

        AutoCompleteTextView addNewAutoComplete = new AutoCompleteTextView(this);

        ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addNewAutoComplete.setLayoutParams(p);
        cl.addView(addNewAutoComplete);

    }

    // When user selects image from gallery

}
