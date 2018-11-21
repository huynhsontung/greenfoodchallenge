package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

    private ChipGroup myChipGroup;



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


        Chip chipBeef = findViewById(R.id.chip_beef);
        Chip chipPork = findViewById(R.id.chip_pork);
        Chip chipChicken = findViewById(R.id.chip_chicken);
        Chip chipFish = findViewById(R.id.chip_fish);
        Chip chipEggs = findViewById(R.id.chip_eggs);
        Chip chipBeans = (Chip)findViewById(R.id.chip_beans);
        Chip chipVeggies = findViewById(R.id.chip_veggies);

        ChipClickListener(chipBeans);
        ChipClickListener(chipBeef);
        ChipClickListener(chipChicken);
        ChipClickListener(chipEggs);
        ChipClickListener(chipFish);
        ChipClickListener(chipPork);
        ChipClickListener(chipVeggies);

        /*chipBeans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chipBeans.setChipBackgroundColorResource(R.color.colorAccent);
                Toast.makeText(getApplicationContext(), "Lalala", Toast.LENGTH_SHORT).show();
            }
        });*/

        listOfAutoCompletes = new ArrayList<AutoCompleteTextView>();

        viewPager = findViewById(R.id.add_green_meal_photo_pager);
        viewPager.setOffscreenPageLimit(1);
        adapter = new AddPhotoFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        autoCompleteTextView = findViewById(R.id.add_greal_meal_photo_auto);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddGreenMealPhotoActivity.this,
                android.R.layout.simple_list_item_1, GREEN_MEALS);
        autoCompleteTextView.setAdapter(adapter);


    }

    private void ChipClickListener(final Chip myChip) {
        myChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //myChip.setChipBackgroundColorResource(R.color.colorAccent);

            }
        });
    }

    // When user selects image from gallery

}
