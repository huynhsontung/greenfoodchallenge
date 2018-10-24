package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.os.DropBoxManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.db.Plan;

public class ImproveActivity extends AppCompatActivity {

    private String mUsername;
    private String mGender;

    private TextView mCurrentPlanCo2eTextView;
    private TextView mImporvedPlanCo2eTextView;
    private LinearLayout[] mSeekBarLayout;
    private SeekBar[] mSeekBars;
    private TextView[] mSeekBarTextViews;

    private String[] foodName;
    private int foodLen;
//    private int[] foodAmount;

    private AppDatabase mDb;


    private static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.improveactivity.username";

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(ImproveActivity)";

    public static Intent newIntent(Context packageContext, String username) {
        Intent intent = new Intent(packageContext, ImproveActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improve);

        mDb = AppDatabase.getDatabase(getApplicationContext());
        DbInterface.setDb(mDb);

        foodName = findStringArrayRes("food_name");
        foodLen = foodName.length;

        mCurrentPlanCo2eTextView = findViewById(R.id.improve_current_plan);
        mImporvedPlanCo2eTextView = findViewById(R.id.improve_improved_plan);

        mUsername = getIntent().getStringExtra(EXTRA_USERNAME);
        mGender = DbInterface.getGender(mUsername);

        Log.i(TAG, "Username in Improve:" + mUsername + CLASSTAG);
        Log.i(TAG, "Gender in Improve:" + mGender + CLASSTAG);

        initializePlansCo2eTextView();
//        initializeSeekBar();
    }

    private void initializePlansCo2eTextView() {
        Plan currentPlan = DbInterface.getCurrentPlan(mUsername);
        Log.i(TAG, "Get the current plan" + CLASSTAG + ":\n" +
                DbInterface.getCurrentPlanDatatoString(mUsername));
        NewPlan mNewPlan = new NewPlan(currentPlan, mGender);
        Plan improvedPlan = mNewPlan.suggestPlan();
        setPlansCo2eTextView(currentPlan, improvedPlan);

    }

    private void setPlansCo2eTextView(Plan currentPlan, Plan improvedPlan) {
        String mCurrentPlanCo2e = String.valueOf(Calculator.calculateCO2ePerDay(currentPlan));
        String mImporvedPlanCo2e = String.valueOf(Calculator.calculateCO2ePerDay(improvedPlan));

        mCurrentPlanCo2eTextView.setText(String.format("%s g", mCurrentPlanCo2e));
        mImporvedPlanCo2eTextView.setText(String.format("%s g", mImporvedPlanCo2e));
    }

    private void initializeSeekBar() {
//        foodAmount = new int[foodLen];
//        mSeekBarLayout = new LinearLayout[foodLen];
//        mSeekBarTextViews = new TextView[foodLen];
//        mSeekBars = new SeekBar[foodLen];
//
//        mSeekBarLayout[0] = findViewById(R.id.improve_seekbar_component_1);
//        mSeekBarLayout[1] = findViewById(R.id.improve_seekbar_component_2);
//        mSeekBarLayout[2] = findViewById(R.id.improve_seekbar_component_3);
//        mSeekBarLayout[3] = findViewById(R.id.improve_seekbar_component_4);
//        mSeekBarLayout[4] = findViewById(R.id.improve_seekbar_component_5);
//        mSeekBarLayout[5] = findViewById(R.id.improve_seekbar_component_6);
//        mSeekBarLayout[6] = findViewById(R.id.improve_seekbar_component_7);
//
//        for (int i = 0; i < foodLen; i++) {
//            mSeekBarTextViews[i] = mSeekBarLayout[i].findViewById(R.id.seekbar_text);
//            mSeekBars[i] = mSeekBarLayout[i].findViewById(R.id.seekbar_action);
//            mSeekBarTextViews[i].setVisibility(View.GONE);
//            mSeekBars[i].setVisibility(View.GONE);
//        }

    }

    private String[] findStringArrayRes(String resName) {
        int resId = getResources().getIdentifier(resName,
                "array", getPackageName());
        return getResources().getStringArray(resId);
    }
}
