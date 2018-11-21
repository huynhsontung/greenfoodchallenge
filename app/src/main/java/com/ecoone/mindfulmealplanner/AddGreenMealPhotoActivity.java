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

        final Chip testChip = (Chip)findViewById(R.id.chip_beans);
        testChip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //testChip.setBackgroundColor(getResources().getColor(R.color.chartBlue7));
                Toast.makeText(getApplicationContext(), "Lalala", Toast.LENGTH_SHORT).show();
            }
        });


        myChipGroup = (ChipGroup)findViewById(R.id.add_green_meal_photo_chipgroup);
        myChipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                Chip myChip = chipGroup.findViewById(i);

                if(myChip != null){
                    myChip.setBackgroundColor(getResources().getColor(R.color.chartBlue7));
                    Toast.makeText(getApplicationContext(), "Lalala", Toast.LENGTH_SHORT).show();
                }
            }
        });

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

    // When user selects image from gallery

}
