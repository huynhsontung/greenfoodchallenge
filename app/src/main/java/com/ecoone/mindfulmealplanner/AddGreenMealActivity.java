package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.database.Food;
import com.ecoone.mindfulmealplanner.database.Meal;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddGreenMealActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private EditText mealNameEditView;
    private AutoCompleteTextView restaurantNameAutoCompleteTextView;
    private EditText mealDescriptionEditView;
    private RadioGroup mealTypeRadioGroup;
    private CheckBox visibleCheckBox;
    private Button nextButton;
    private Button resetButton;

    private LinearLayout addFoodLayout;


    private HashMap<String, Boolean> allRestaurantMenu;
    private AutoCompleteRestaurantAdapter mAdapter;

    private Meal mMeal;

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(AddGreenMealActivity)";

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

        mealNameEditView = findViewById(R.id.add_meal_name_editview);
        restaurantNameAutoCompleteTextView = findViewById(R.id.add_meal_autoCompleteTextView);
        mealDescriptionEditView = findViewById(R.id.add_meal_description);
        nextButton = findViewById(R.id.add_meal_next_button);
        resetButton = findViewById(R.id.add_meal_reset_button);
        visibleCheckBox = findViewById(R.id.add_meal_checkbox);
        mealTypeRadioGroup = findViewById(R.id.add_meal_rasio_group);

        addFoodLayout = findViewById(R.id.add_food_layout);

        allRestaurantMenu = new HashMap<>();

        mMeal = new Meal();

        initializeActivity();




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

    }

    private void initializeActivity() {
        getAllRestaurantMenu().addOnCompleteListener(new OnCompleteListener<HashMap<String, Boolean>>() {
            @Override
            public void onComplete(@NonNull Task<HashMap<String, Boolean>> task) {
                allRestaurantMenu = task.getResult();

                if (allRestaurantMenu != null) {
                    ArrayList<Map.Entry<String, Boolean>> list = new ArrayList<>(allRestaurantMenu.entrySet());
                    mAdapter = new AutoCompleteRestaurantAdapter(getApplicationContext(), list);
                    restaurantNameAutoCompleteTextView.setAdapter(mAdapter);
                }
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMealBasicInfoComplete() ) {
                    setMealBasicInfoEditableStatus(0);
                    nextButton.setTextColor(Color.WHITE);


                    addFoodLayout.setVisibility(View.VISIBLE);

                }
                else {
                    showCustomToast("Please enter the meal information.");
                }

            }
        });

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setMealBasicInfoEditableStatus(1);
                nextButton.setTextColor(getResources().getColor(R.color.button_grey));
                addFoodLayout.setVisibility(View.GONE);

            }
        });

    }

    private void setMealInfo() {
        mMeal.mealName = mealNameEditView.getText().toString();
        RadioButton mealTypeButton = findViewById(mealTypeRadioGroup.getCheckedRadioButtonId());
        mMeal.mealType = mealTypeButton.getText().toString();
        mMeal.restaurantName = restaurantNameAutoCompleteTextView.getText().toString();
        if (mealDescriptionEditView.getText().toString().equals("")) {
            mMeal.mealDescription = "No Description.";
        }
        mMeal.mealDescription = mealDescriptionEditView.getText().toString();

        if (visibleCheckBox.isChecked()) {
            mMeal.isPrivate = false;
        }
        else {
            mMeal.isPrivate = true;
        }

    }

    private boolean isMealBasicInfoComplete() {
//        Log.i(TAG, CLASSTAG + mealTypeRadioGroup.getCheckedRadioButtonId());
        if (!mealNameEditView.getText().toString().equals("") &&
                !restaurantNameAutoCompleteTextView.getText().toString().equals("") &&
                mealTypeRadioGroup.getCheckedRadioButtonId() != -1) {
            return true;
        }
        return false;
    }

    // sign: 0 uneditable, 1 editable
    private void setMealBasicInfoEditableStatus(int sign) {
        if (sign == 0) {
            mealNameEditView.setEnabled(false);
            restaurantNameAutoCompleteTextView.setEnabled(false);
            mealDescriptionEditView.setEnabled(false);
            for (int i = 0; i < mealTypeRadioGroup.getChildCount(); i++) {
                mealTypeRadioGroup.getChildAt(i).setEnabled(false);
            }
            visibleCheckBox.setEnabled(false);
        }
        else {
            mealNameEditView.setEnabled(true);
            restaurantNameAutoCompleteTextView.setEnabled(true);
            mealDescriptionEditView.setEnabled(true);
            for (int i = 0; i < mealTypeRadioGroup.getChildCount(); i++) {
                mealTypeRadioGroup.getChildAt(i).setEnabled(true);
            }
            visibleCheckBox.setEnabled(true);
        }

    }

    private void showCustomToast(String message) {
        Toast mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
                0, 0);
        mToast.show();
    }

    private void addFoodtoMeal(Meal meal, Food food){
        String foodName = food.foodName;
        if (meal.isGreen) {
            meal.totalCo2eAmount += food.co2eAmount;
        }
        food.foodName = null;
        meal.foodInfo.put(foodName, food);
    }

    private Task<HashMap<String, Boolean>> getAllRestaurantMenu() {
        return FirebaseFunctions.getInstance().getHttpsCallable("getAllRestaurantMenu")
                .call().continueWith(new Continuation<HttpsCallableResult, HashMap<String, Boolean>>() {
                    @Override
                    public HashMap<String, Boolean> then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                        HashMap<String, Boolean> result = (HashMap<String, Boolean>) task.getResult().getData();
                        return result;
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
