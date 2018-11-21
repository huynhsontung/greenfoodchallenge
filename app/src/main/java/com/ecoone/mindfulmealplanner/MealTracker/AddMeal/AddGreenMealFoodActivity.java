package com.ecoone.mindfulmealplanner.MealTracker.AddMeal;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.ecoone.mindfulmealplanner.R;

import java.util.ArrayList;

public class AddGreenMealFoodActivity extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_CAMERA_IMAGE = 2;

    private static final String[] GREEN_MEALS = {
            "Beyond Meat Burger", "California Roll", "Vodka", "Cheese Pizza", "Cold Cut Combo",
            "Burger", "Coffee", "Strawberry Smoothie"
    };


    private Toolbar mToolbar;
    private AutoCompleteTextView autoCompleteTextView;
    private ImageView plusFoodItem;
    private int test = 0;
    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintSet;

    private ChipGroup myChipGroup;
    ArrayList<AutoCompleteTextView> listOfAutoCompletes;
    EditText getMealName;

    private ImageView foodPhoto;

    public static Intent newIntent(Context packageContext, String restaurantName) {
        Intent intent = new Intent(packageContext, AddGreenMealFoodActivity.class);
        intent.putExtra("restaurantName", restaurantName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_green_meal_photo);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Add Meal Food");

        constraintLayout = findViewById(R.id.add_green_meal_photo_constraintlayout);


        Chip chipBeef = findViewById(R.id.chip_beef);
        Chip chipPork = findViewById(R.id.chip_pork);
        Chip chipChicken = findViewById(R.id.chip_chicken);
        Chip chipFish = findViewById(R.id.chip_fish);
        Chip chipEggs = findViewById(R.id.chip_eggs);
        Chip chipBeans = findViewById(R.id.chip_beans);
        Chip chipVeggies = findViewById(R.id.chip_veggies);

        ChipClickListener(chipBeans);
        ChipClickListener(chipBeef);
        ChipClickListener(chipChicken);
        ChipClickListener(chipEggs);
        ChipClickListener(chipFish);
        ChipClickListener(chipPork);
        ChipClickListener(chipVeggies);

        listOfAutoCompletes = new ArrayList<AutoCompleteTextView>();

        foodPhoto = findViewById(R.id.add_green_meal_photo_image);

        autoCompleteTextView = findViewById(R.id.add_green_meal_photo_auto);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddGreenMealFoodActivity.this,
                android.R.layout.simple_list_item_1, GREEN_MEALS);
        autoCompleteTextView.setAdapter(adapter);


    }

    private void ChipClickListener(final Chip myChip) {
        myChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                myChip.setChipBackgroundColorResource(R.color.colorAccent);
            }
        });
    }

    // When user selects image from gallery

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
