package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.database.Food;
import com.ecoone.mindfulmealplanner.database.Meal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class AddGreenMealActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText addGreenMealNameEditView;
    private AutoCompleteTextView autoCompleteTextView;
    private EditText addGreenMealDescription;

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private static final String[] RESTAURANT_SUGGESTIONS = {
            "A&W", "Tim Hortons", "Pizza Hut", "Liquor Store",
            "Subway", "SFU Dining Hall", "Togo Sushi", "Nester's Market", "Starbucks"
    };

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AddGreenMealActivity.class);
        return intent;
    }

    Button goToPhoto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_green_meal);

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Add Meal");

        addGreenMealNameEditView = findViewById(R.id.add_meal_name_editview);
        autoCompleteTextView = findViewById(R.id.add_meal_autoCompleteTextView);
        addGreenMealDescription = findViewById(R.id.add_meal_description);

//        Meal meal = new Meal();
//        meal.mealName = "testMeal";
//        Set<String> s = new HashSet<>();
//        s.add("lunch");
//        s.add("test");
//        meal.tags.addAll(s);
//        meal.isGreen = true;
////
//        Food food = new Food();
//        food.foodName = "testFood";
//        food.co2eAmount = 100;
//
//        food.ingredient.put("beef", 10);
//        food.ingredient.put("pork", 10);
//        meal.foodInfo.put("testFood", food);
//
//        mDatabase.child(FirebaseDatabaseInterface.ALLUSERSUID_NODE).child(userUid)
//                .child("mealInfo").setValue(meal);


//        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddGreenMealActivity.this,
//                android.R.layout.simple_list_item_1, RESTAURANT_SUGGESTIONS);
//        autoCompleteTextView.setAdapter(adapter);
//
//        goToPhoto = (Button)findViewById(R.id.add_green_meal_startphoto_button);
//        goToPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent myIntent = new Intent(AddGreenMealActivity.this, AddGreenMealPhotoActivity.class);
//                startActivity(myIntent);
//            }
//        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
