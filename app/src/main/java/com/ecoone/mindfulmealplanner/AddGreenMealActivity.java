package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SearchView;

public class AddGreenMealActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;

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

        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddGreenMealActivity.this,
                android.R.layout.simple_list_item_1, RESTAURANT_SUGGESTIONS);
        autoCompleteTextView.setAdapter(adapter);

        goToPhoto = (Button)findViewById(R.id.add_green_meal_startphoto_button);
        goToPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(AddGreenMealActivity.this, AddGreenMealPhotoActivity2.class);
                startActivity(myIntent);
            }
        });
    }
}
