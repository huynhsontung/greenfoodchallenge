package com.ecoone.mindfulmealplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.db.User;
import com.ecoone.mindfulmealplanner.db.UserDao;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthCredential;
import com.google.firebase.auth.UserInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class InitialScreenActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private User userObj = new User();

    private String mUsername;
    private String mGender;

    private EditText mEditText;
    private TextView maleTextView;
    private TextView femaleTextView;
    private Button nextButton;

    private SharedPreferences settings;
    private int loginFlag = 0;
    private AppDatabase mDb;

    private Toast mToast;

    private PieChart mPieChart;
    private String[] foodName;
    private float[] foodAmount;
    private int foodLen;
    private ConstraintLayout[] mFoodSeekBarView;
    private TextView[] mFoodSeekBarTextView;
    private SeekBar[] mFoodSeekBarAction;
    private TextView[] mFoodSeekBarValueView;

    private static final String EXTRA_LOGIN_FLAG =
            "com.ecoone.mindfulmealplanner.initialscreenactivity.login_flag";
    private static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.initialscreenactivity.username";
    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(InitialActivity)";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN);

        setContentView(R.layout.activity_initial_screen);

        mEditText = findViewById(R.id.initial_screen_username_edit_text);
        maleTextView = findViewById(R.id.initial_screen_male_text_view);
        femaleTextView = findViewById(R.id.initial_screen_female_text_view);
        nextButton = findViewById(R.id.initial_screen_next);

        mPieChart = findViewById(R.id.initial_screen_pie_chart);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mDb = AppDatabase.getDatabase(getApplicationContext());
        DbInterface.setDb(mDb);

        // Remove data from database and SharedPreferences
//        setAllDataTemporary();

        // check if go to the fragment_dashboard
        checkIfGotoDashboard();

        setGenderTextViewAction();
        setNextButtonAction();

        initializeSeekBarView();
        setSeekBarValueView();
        setPieChartView(foodAmount);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {
                    userObj.username = Objects.requireNonNull(user.getEmail());
                    mUsername = userObj.username;
                    userObj.displayName = user.getDisplayName();
                    userObj.photoUrl = user.getPhotoUrl().toString();
                }
            }
        }
    }

    private void setAllDataTemporary() {
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(EXTRA_USERNAME);
        editor.remove(EXTRA_LOGIN_FLAG);
        editor.apply();
        mDb.planDao().deleteAll();
        mDb.userDao().deleteALL();
//        DbInterface.addUser(mDb, "arlenx", "male");
    }

    private void checkIfGotoDashboard() {
        loginFlag = isLogin();
        // if no user login
        if (loginFlag == 0) {
            Log.i(TAG, "No user log in. Wait for initialization." + CLASSTAG);
        }
        else {
            Log.i(TAG, "Someone already login" + CLASSTAG);
            mUsername = getUsernameInSharedPreference();
            if (mUsername == null) {
                Log.i(TAG, "Error. Username In SharedPreference is empty." + CLASSTAG);
                return;
            }
            startActivityAndFinish(mUsername);
        }
    }

    private int isLogin() {
        return settings.getInt(EXTRA_LOGIN_FLAG, 0);
    }

    private String getUsernameInSharedPreference() {
        return settings.getString(EXTRA_USERNAME, null);
    }

    private void setGenderTextViewAction() {
        maleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click male" + CLASSTAG);
                maleTextView.setBackgroundResource(R.drawable.inside_and_border_grey);
                femaleTextView.setBackgroundResource(R.drawable.border_grey);
                mGender = "male";
            }
        });

        femaleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click female" + CLASSTAG);
                femaleTextView.setBackgroundResource(R.drawable.inside_and_border_grey);
                maleTextView.setBackgroundResource(R.drawable.border_grey);
                mGender = "female";
            }
        });
    }

    private void setNextButtonAction() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInfoEntered()) {
                    userObj.gender = mGender;
                    userObj.currentPlanName = "Plan1";
                    Log.i(TAG, "Get username in Edittext:" + mUsername + " and write into db" + CLASSTAG);
                    mDb.userDao().addUser(userObj);
                    DbInterface.addPlan(mUsername,"Plan1", foodAmount);
                    Log.i(TAG, "Add User Info into db successfully");
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt(EXTRA_LOGIN_FLAG, 1);
                    editor.putString(EXTRA_USERNAME, mUsername);
                    editor.apply();
                    startActivityAndFinish(mUsername);
                }
            }
        });
    }

    private boolean isInfoEntered() {
//        mUsername = mEditText.getText().toString();
        if (mUsername.equals("")) {
            showCustomToast("Please enter your username!");
            return false;
        }
        if (mGender == null) {
            showCustomToast("Please choose the gender!");
            return false;
        }
        return true;
    }

    private void showCustomToast(String message) {
        mToast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
                0, 0);
        mToast.show();
    }

    private void initializeSeekBarView() {
        foodName = findStringArrayRes("food_name");
        foodLen = foodName.length;
        foodAmount = new float[foodLen];
        mFoodSeekBarView = new ConstraintLayout[foodLen];
        mFoodSeekBarTextView = new TextView[foodLen];
        mFoodSeekBarAction = new SeekBar[foodLen];
        mFoodSeekBarValueView = new TextView[foodLen];

        mFoodSeekBarView[0] = findViewById(R.id.initial_screen_seekbar_component_1);
        mFoodSeekBarView[1] = findViewById(R.id.initial_screen_seekbar_component_2);
        mFoodSeekBarView[2] = findViewById(R.id.initial_screen_seekbar_component_3);
        mFoodSeekBarView[3] = findViewById(R.id.initial_screen_seekbar_component_4);
        mFoodSeekBarView[4] = findViewById(R.id.initial_screen_seekbar_component_5);
        mFoodSeekBarView[5] = findViewById(R.id.initial_screen_seekbar_component_6);
        mFoodSeekBarView[6] = findViewById(R.id.initial_screen_seekbar_component_7);

        for (int i = 0; i < foodLen; i++) {
            mFoodSeekBarTextView[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_text);
            mFoodSeekBarAction[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_action);
            mFoodSeekBarValueView[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_value);
            mFoodSeekBarTextView[i].setText(foodName[i]);
            mFoodSeekBarAction[i].setProgress(randInt(0, mFoodSeekBarAction[i].getMax()));
            String amountText = String.valueOf(mFoodSeekBarAction[i].getProgress());
            mFoodSeekBarValueView[i].setText(amountText + " g");
            foodAmount[i] = Integer.valueOf(amountText);
        }
    }

    private void setSeekBarValueView() {
        for (int i = 0; i < foodLen; i++) {
            setSeekBarListener(i);
        }
    }

    private void setSeekBarListener(final int i) {
        mFoodSeekBarAction[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String amountText = String.valueOf(progress);
                mFoodSeekBarValueView[i].setText(amountText+ " g");
                foodAmount[i] = Integer.valueOf(amountText);
                setPieChartView(foodAmount);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setPieChartView(float[] data) {
        List<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < foodLen; i++) {
            // filter out 0 values
            if(data[i] > 0)
                entries.add(new PieEntry(data[i], foodName[i]));
        }
        PieDataSet pieDataSet = new PieDataSet(entries, null);
        pieDataSet.setValueFormatter(new ChartValueFormatter());
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData piedata = new PieData(pieDataSet);
        Legend legend = mPieChart.getLegend();
        legend.setEnabled(false);
        mPieChart.getDescription().setEnabled(false);
        mPieChart.setData(piedata);
        mPieChart.setUsePercentValues(true);
        mPieChart.invalidate();

    }

    private String[] findStringArrayRes(String resName) {
        int resId = getResources().getIdentifier(resName,
                "array", getPackageName());
        return getResources().getStringArray(resId);
    }

    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max- min + 1) + min;
    }

    private void startActivityAndFinish(String username) {
        Intent intent = MainActivity.newIntent(InitialScreenActivity.this, username);
        startActivity(intent);
        InitialScreenActivity.this.finish();
    }
}
