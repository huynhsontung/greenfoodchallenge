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

import java.util.ArrayList;

public class AddGreenMealPhotoActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CAMERA_IMAGE = 2;

    private static final String[] GREEN_MEALS = {
            "Beyond Meat Burger", "California Roll", "Vodka", "Cheese Pizza", "Cold Cut Combo",
            "Burger", "Coffee", "Strawberry Smoothie"
    };
    ArrayList<AutoCompleteTextView> listOfAutoCompletes;
    private AutoCompleteTextView autoCompleteTextView;
    private ImageView plusFoodItem;
    private int test = 0;
    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintSet;

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

        constraintLayout = findViewById(R.id.add_green_meal_photo_constraintlayout);


        listOfAutoCompletes = new ArrayList<AutoCompleteTextView>();

        viewPager = findViewById(R.id.add_green_meal_photo_pager);
        viewPager.setOffscreenPageLimit(1);
        adapter = new AddPhotoFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        autoCompleteTextView = findViewById(R.id.add_greal_meal_photo_auto);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddGreenMealPhotoActivity.this,
                android.R.layout.simple_list_item_1, GREEN_MEALS);
        autoCompleteTextView.setAdapter(adapter);
       /* listOfAutoCompletes.add(autoCompleteTextView);

        plusFoodItem = findViewById(R.id.add_green_meal_photo_addtextview);

        plusFoodItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addLine();
            }
        });*/

    }

    /*private void addLine() {
        //ConstraintLayout cl = (ConstraintLayout)findViewById(R.id.add_green_meal_photo_constraintlayout);
        constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        AutoCompleteTextView addNewAutoComplete = new AutoCompleteTextView(this);
        //addNewAutoComplete.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
        //        ViewGroup.LayoutParams.WRAP_CONTENT));
        constraintLayout.addView(addNewAutoComplete);
        int testing = View.generateViewId();
        addNewAutoComplete.setId(testing);
        *//*ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(cl);*//*
        //constraintSet.clone(this, R.id.add_green_meal_photo_constraintlayout);
        constraintSet.connect(addNewAutoComplete.getId(), ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START, 60);
        constraintSet.connect(addNewAutoComplete.getId(), ConstraintSet.TOP,
                ConstraintSet.PARENT_ID, ConstraintSet.TOP, 60);
        constraintSet.applyTo(constraintLayout);
        *//*ConstraintLayout.LayoutParams p = new ConstraintLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addNewAutoComplete.setLayoutParams(p);
        cl.addView(addNewAutoComplete);*//*

    }
*/
    // When user selects image from gallery

}
