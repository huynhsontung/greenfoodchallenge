package com.ecoone.mindfulmealplanner.MealTracker.AddMeal.ActivityType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;
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
import java.util.Arrays;
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
    private TextView addFoodLayoutMealTypeTextView;

    private HashMap<String, Boolean> allRestaurantMenu;
    private AutoCompleteRestaurantAdapter mAdapter;

    private Meal mMeal;

    private ArrayList<byte[]> photoList;
    private ArrayList<View> mViewList;

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(AddGreenMealActivity)";
    private static final int REQUEST_ADD_FOOD_INFO = 0;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AddGreenMealActivity.class);
        return intent;
    }

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
        addFoodLayoutMealTypeTextView = findViewById(R.id.add_food_layout_meal_type);

        allRestaurantMenu = new HashMap<>();
        photoList = new ArrayList<>();
        mViewList = new ArrayList<>();
        mMeal = new Meal();
//        photoList
        initializeActivity();

        Button test = findViewById(R.id.test);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddGreenMealFoodActivity.newIntent(AddGreenMealActivity.this,
                        mMeal.restaurantName, mMeal.isGreen);

                startActivityForResult(intent, REQUEST_ADD_FOOD_INFO);

            }
        });
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
                    setMealInfo();
//                    nextButton.setTextColor(Color.WHITE);
                    addFoodLayoutMealTypeTextView.setText(mMeal.mealType);
                    addFoodLayout.setVisibility(View.VISIBLE);
                    addNewFoodView();
//                    sendMealToFirebase();

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
//                nextButton.setTextColor(getResources().getColor(R.color.button_grey));
                mMeal.clear();
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
        else {
            mMeal.mealDescription = mealDescriptionEditView.getText().toString();
        }

        if (visibleCheckBox.isChecked()) {
            mMeal.isPrivate = false;
        }
        else {
            mMeal.isPrivate = true;
        }

        String restaurantName = restaurantNameAutoCompleteTextView.getText().toString();

        if (allRestaurantMenu.containsKey(restaurantName)) {
            if (allRestaurantMenu.get(restaurantName)) {
                mMeal.isGreen = true;
            }
            else {
                mMeal.isGreen = false;
            }
        }
        else {
            mMeal.isGreen = false;
        }

        mMeal.tags.addAll(Arrays.asList(mMeal.mealName, mMeal.mealType, mMeal.restaurantName));

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

    private void setAddFoodDetailLayout() {

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

    private void sendMealToFirebase() {
        Food food1 = new Food();
        food1.foodName = "testFood1";
        food1.co2eAmount = 100;
        food1.ingredient.put("beef", 100);
        food1.ingredient.put("pork", 10);


        Food food2 = new Food();
        food2.foodName = "testFood2";
        food2.co2eAmount = 200;
        food2.ingredient.put("beef", 20);
        food2.ingredient.put("fish", 120);
        addFoodToMeal(food1);
        addFoodToMeal(food2);

        FirebaseDatabaseInterface.writeMeal(mMeal);
    }

    private void showCustomToast(String message) {
        Toast mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
                0, 0);
        mToast.show();
    }

    private void addFoodToMeal(Food food){
        String foodName = food.foodName;
        if (mMeal.isGreen) {
            mMeal.totalCo2eAmount += food.co2eAmount;
        }
        food.foodName = null;
        mMeal.foodList.put(foodName, food);
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

    private void addNewFoodView() {
        View child = LayoutInflater.from(getApplicationContext()).inflate(R.layout.add_food_detail_component, addFoodLayout, false);

        ImageView add_icon = child.findViewById(R.id.red_add_icon_image_view);
        mViewList.add(child);
        addFoodLayout.addView(child);

        add_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddGreenMealFoodActivity.newIntent(AddGreenMealActivity.this,
                        mMeal.restaurantName, mMeal.isGreen);
                startActivityForResult(intent, REQUEST_ADD_FOOD_INFO);
                Log.d(TAG, CLASSTAG + " 1. Back!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d(TAG, CLASSTAG + " 2. Back!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_ADD_FOOD_INFO) {
            if (data == null) {
                return;
            }

            Log.d(TAG, CLASSTAG + " 3. Back!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//            byte[] photoArraybyte = AddGreenMealFoodActivity.getPhotoArraybyte(data);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(photoArraybyte, 0, photoArraybyte.length);

//            Log.i(TAG, CLASSTAG + "bitmap size" + bitmap.getByteCount());

//            ImageView photo_image = mViewList.get(mViewList.size()-1).findViewById(R.id.red_add_icon_image_view);
//            photo_image.setOnClickListener(null);
//            photo_image.setImageBitmap(bitmap);

            addNewFoodView();


//            Food food = AddGreenMealFoodActivity.getFoodInfo(data);
//            addFoodToMeal(food);
        }

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, CLASSTAG + " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, CLASSTAG + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, CLASSTAG + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, CLASSTAG + " onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, CLASSTAG + " onDestroy");
    }
}

