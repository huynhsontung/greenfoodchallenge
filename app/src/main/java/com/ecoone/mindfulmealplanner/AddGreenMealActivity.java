package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AddGreenMealActivity extends AppCompatActivity {

    private static final String[] restaurantSuggestions = {
            "A&W", "Tim Hortons", "Pizza Hut", "Liquor Store", "Subway", "SFU Dining Hall"
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

        goToPhoto = (Button)findViewById(R.id.add_green_meal_startphoto_button);
        goToPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(AddGreenMealActivity.this, AddGreenMealPhotoActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
