package com.ecoone.mindfulmealplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InitialScreenActivity extends AppCompatActivity {

    private String mUsername;
    private String mGender;

    private EditText mEditText;
    private TextView maleTextView;
    private TextView femaleTextView;
    private Button nextButton;

    private SharedPreferences settings;
    private int loginFlag = 0;
    private AppDatabase mDb;
    private dbInterface mDbInterface;

    private Toast mToast;

    private PieChart mPieChart;
    private String[] foodName;
    private int[] foodAmount;
    private int foodLen;
    private LinearLayout[] mfoodSeekBarView;
    private TextView[] mfoodSeekBarTextView;
    private SeekBar[] mfoodSeekBarAction;
    private TextView[] mfoodSeekBarValueView;

    private static final String EXTRA_LOGIN_FLAG =
            "com.ecoone.mindfulmealplanner.initialscreenactivity.login_flag";
    private static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.initialscreenactivity.username";
    private static final String TAG = "testActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);

        mEditText = findViewById(R.id.initial_screen_username_edit_text);
        maleTextView = findViewById(R.id.initial_screen_male_text_view);
        femaleTextView = findViewById(R.id.initial_screen_female_text_view);
        nextButton = findViewById(R.id.initial_screen_next);

        mPieChart = findViewById(R.id.initial_screen_pie_chart);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mDb = AppDatabase.getDatabase(getApplicationContext());
        mDbInterface = new dbInterface(mDb);

        // Remove data from database and SharedPreferences
        initialization();

        // check if go to the fragment_dashboard
        checkIfGotoDashboard();

        setGenderTextViewAction();
        setNextButtonAction();

        initializeSeekBarView();
        setSeekBarValueView();
        setPieChartView(foodAmount);

    }

    private void initialization() {
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(EXTRA_USERNAME);
        editor.remove(EXTRA_LOGIN_FLAG);
        editor.apply();
        mDb.planDao().deleteAll();
        mDb.userDao().deleteALL();
//        dbInterface.addUser(mDb, "arlenx", "male");
    }

    private void checkIfGotoDashboard() {
        loginFlag = isLogin();
        // if no user login
        if (loginFlag == 0) {
            Log.i(TAG, "No user log in. Wait for initialization.");
        }
        else {
            Log.i(TAG, "Someone already login");
            mUsername = getUsernameInSharedPreference();
            if (mUsername == "") {
                Log.i(TAG, "Error. Username is empty.");
                return;
            }
            startActivityAndFinish(mUsername);
        }
    }

    private int isLogin() {
        return settings.getInt(EXTRA_LOGIN_FLAG, 0);
    }

    private String getUsernameInSharedPreference() {
        return settings.getString(EXTRA_USERNAME, "");
    }

    private void setGenderTextViewAction() {
        maleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click male");
                maleTextView.setBackgroundResource(R.drawable.inside_and_border_grey);
                femaleTextView.setBackgroundResource(R.drawable.border_grey);
                mGender = "male";
            }
        });

        femaleTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click female");
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
                    Log.i(TAG, "Get username in Edittext:" + mUsername + " and write into db");
                    mDbInterface.addUser(mUsername, mGender, "Plan1");
                    mDbInterface.addPlan(mUsername, foodAmount);
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
        mUsername = mEditText.getText().toString();
        if (mUsername.length() == 0) {
            showCustomToast("please enter your username!");
            return false;
        }
        if (mGender == null) {
            showCustomToast("please choose the gender!");
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
        foodAmount = new int[foodLen];
        mfoodSeekBarView = new LinearLayout[foodLen];
        mfoodSeekBarTextView = new TextView[foodLen];
        mfoodSeekBarAction = new SeekBar[foodLen];
        mfoodSeekBarValueView = new TextView[foodLen];

        mfoodSeekBarView[0] = findViewById(R.id.initial_screen_seekbar_component_1);
        mfoodSeekBarView[1] = findViewById(R.id.initial_screen_seekbar_component_2);
        mfoodSeekBarView[2] = findViewById(R.id.initial_screen_seekbar_component_3);
        mfoodSeekBarView[3] = findViewById(R.id.initial_screen_seekbar_component_4);
        mfoodSeekBarView[4] = findViewById(R.id.initial_screen_seekbar_component_5);
        mfoodSeekBarView[5] = findViewById(R.id.initial_screen_seekbar_component_6);
        mfoodSeekBarView[6] = findViewById(R.id.initial_screen_seekbar_component_7);

        for (int i = 0; i < foodLen; i++) {
            mfoodSeekBarTextView[i] = mfoodSeekBarView[i].findViewById(R.id.seekbar_text);
            mfoodSeekBarAction[i] = mfoodSeekBarView[i].findViewById(R.id.seekbar_action);
            mfoodSeekBarValueView[i] = mfoodSeekBarView[i].findViewById(R.id.seekbar_value);
            mfoodSeekBarTextView[i].setText(foodName[i]);
            mfoodSeekBarAction[i].setProgress(randInt(0, mfoodSeekBarAction[i].getMax()));
            String amountText = String.valueOf(mfoodSeekBarAction[i].getProgress());
            mfoodSeekBarValueView[i].setText(amountText + " g");
            foodAmount[i] = Integer.valueOf(amountText);
        }
    }

    private void setSeekBarValueView() {
        for (int i = 0; i < foodLen; i++) {
            setSeekBarListener(i);
        }
    }

    private void setSeekBarListener(final int i) {
        mfoodSeekBarAction[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String amountText = String.valueOf(progress);
                mfoodSeekBarValueView[i].setText(amountText+ " g");
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

    private void setPieChartView(int[] data) {
        List<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < foodLen; i++) {
            entries.add(new PieEntry(data[i], foodName[i]));
        }
        PieDataSet pieDataSet = new PieDataSet(entries, "Test");
        PieData piedata = new PieData(pieDataSet);
        mPieChart.setData(piedata);
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
        AppDatabase.destroyInstance();
        Intent intent = MainActivity.newIntent(InitialScreenActivity.this, username);
        startActivity(intent);
        InitialScreenActivity.this.finish();
    }
}
