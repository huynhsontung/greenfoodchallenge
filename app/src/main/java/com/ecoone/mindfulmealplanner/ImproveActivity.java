package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.db.Plan;
import com.ecoone.mindfulmealplanner.fragments.InputTextDialogFragment;
import com.ecoone.mindfulmealplanner.fragments.InputTextDialogFragment.OnInputListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class ImproveActivity extends AppCompatActivity implements OnInputListener {

    private String mUsername;
    private String mGender;

    private TextView mImprovedPlanCo2ePerYearTextView;
    private TextView mPlanDifferenceCo2ePerYearTextView;
    private Button editButton;
    private Button saveAsButton;
    private Button saveButton;
    private ConstraintLayout[] mFoodSeekBarView;
    private TextView[] mFoodSeekBarTextView;
    private SeekBar[] mFoodSeekBarAction;
    private TextView[] mFoodSeekBarValueView;

    private String[] foodName;
    private int foodLen;
    private float[] foodAmount;
    private Plan currentPlan;
    private Plan improvedPlan;
    private float currentPlanCo2ePerYear;
    private float differenceCo2ePerYear;



    private AppDatabase mDb;
    private PieChart mPieChart;

    public static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.improveactivity.username";

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(ImproveActivity)";

    public static Intent newIntent(Context packageContext, String username) {
        Intent intent = new Intent(packageContext, ImproveActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improve);

        mDb = AppDatabase.getDatabase(getApplicationContext());
        DbInterface.setDb(mDb);

        mPieChart = findViewById(R.id.improve_piechart);

        mUsername = getIntent().getStringExtra(EXTRA_USERNAME);
        mGender = DbInterface.getGender(mUsername);
        foodName = findStringArrayRes("food_name");
        foodLen = foodName.length;

        mImprovedPlanCo2ePerYearTextView = findViewById(R.id.improve_improved_plan_per_year);
        mPlanDifferenceCo2ePerYearTextView = findViewById(R.id.improve_difference_of_plan_per_year);
        editButton = findViewById(R.id.improve_edit);
        saveAsButton = findViewById(R.id.improve_save_as);
        saveButton = findViewById(R.id.improve_save);

        Log.i(TAG, "Username in Improve:" + mUsername + CLASSTAG);
        Log.i(TAG, "Gender in Improve:" + mGender + CLASSTAG);

        currentPlan = DbInterface.getCurrentPlan(mUsername);
        Log.i(TAG, "Get the current plan" + CLASSTAG + ":\n" +
                DbInterface.getPlanDatatoString(currentPlan));
        currentPlanCo2ePerYear = Calculator.calculateCO2ePerYear(currentPlan);

        initializePlansCo2eTextView();
        initializeSeekBarView();
        setButtonAction();
        setSeekBarValueView();
    }

    private void initializePlansCo2eTextView() {
        NewPlan mNewPlan = new NewPlan(currentPlan, mGender);
        improvedPlan = mNewPlan.suggestPlan();
        foodAmount = DbInterface.getPlanArray(improvedPlan);
        Log.i(TAG, "Get the improved plan" + CLASSTAG + ":\n" +
                DbInterface.getPlanDatatoString(improvedPlan));
        setPlanCo2eText(0);
        setPieChartView(DbInterface.getPlanArray(improvedPlan), true,0);
    }

    private void setPlanCo2eText(int color) {
        String mImporvedPlanCo2ePerYear = String.valueOf(Calculator.calculateCO2ePerYear(improvedPlan));
        mImprovedPlanCo2ePerYearTextView.setText(String.format("%s Metric Tonnes", mImporvedPlanCo2ePerYear));
        differenceCo2ePerYear = Calculator.comparePlan(currentPlanCo2ePerYear, improvedPlan);
        mPlanDifferenceCo2ePerYearTextView.setText(String.format("%s Metric Tonnes", String.valueOf(differenceCo2ePerYear)));
        if (color == 0){
            mPlanDifferenceCo2ePerYearTextView.setTextColor(Color.BLUE);
        }
        else if (color == 1){
            mPlanDifferenceCo2ePerYearTextView.setTextColor(Color.RED);
        }
        else {
            mPlanDifferenceCo2ePerYearTextView.setTextColor(Color.BLACK);
        }
    }

    private void initializeSeekBarView() {
        mFoodSeekBarView = new ConstraintLayout[foodLen];
        mFoodSeekBarTextView = new TextView[foodLen];
        mFoodSeekBarAction = new SeekBar[foodLen];
        mFoodSeekBarValueView = new TextView[foodLen];

        mFoodSeekBarView[0] = findViewById(R.id.improve_seekbar_component_1);
        mFoodSeekBarView[1] = findViewById(R.id.improve_seekbar_component_2);
        mFoodSeekBarView[2] = findViewById(R.id.improve_seekbar_component_3);
        mFoodSeekBarView[3] = findViewById(R.id.improve_seekbar_component_4);
        mFoodSeekBarView[4] = findViewById(R.id.improve_seekbar_component_5);
        mFoodSeekBarView[5] = findViewById(R.id.improve_seekbar_component_6);
        mFoodSeekBarView[6] = findViewById(R.id.improve_seekbar_component_7);

        for (int i = 0; i < foodLen; i++) {
            mFoodSeekBarTextView[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_text);
            mFoodSeekBarAction[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_action);
            mFoodSeekBarValueView[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_value);
            mFoodSeekBarTextView[i].setText(foodName[i]);
            mFoodSeekBarAction[i].setProgress((int)foodAmount[i]);
            mFoodSeekBarValueView[i].setText(String.valueOf(foodAmount[i]) + " g");
        }
    }

    private void setButtonAction() {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < foodLen; i++) {
                    mFoodSeekBarView[i].setVisibility(View.VISIBLE);
                }
                v.setVisibility(View.GONE);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbInterface.updateCurrentPlan(mUsername, improvedPlan);
                finish();
            }
        });

        saveAsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
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
                updateImprovedPlan();
                int color = getColorInt();
                setPlanCo2eText(color);
                setPieChartView(foodAmount, false,color);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    // color, 0: blue, 1: red, 2: black
    private int getColorInt() {
        differenceCo2ePerYear = Calculator.comparePlan(currentPlanCo2ePerYear, improvedPlan);
        if (differenceCo2ePerYear > 0) {
            return 0;
        }
        else if (differenceCo2ePerYear < 0){
            return 1;
        }
        return 2;
    }

    private void updateImprovedPlan() {
        improvedPlan.beef = foodAmount[0];
        improvedPlan.pork = foodAmount[1];
        improvedPlan.chicken = foodAmount[2];
        improvedPlan.fish = foodAmount[3];
        improvedPlan.eggs = foodAmount[4];
        improvedPlan.beans = foodAmount[5];
        improvedPlan.vegetables = foodAmount[6];
    }

    private void setPieChartView(float[] percentage, boolean animate, int color){
        //setup pie chart
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i=0; i<percentage.length;i++){
            if(percentage[i] > 0.001)
                pieEntries.add(new PieEntry(percentage[i], foodName));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries,null);
        if (color == 0 || color == 2) {
            dataSet.setColors(new int[]{
                            R.color.chartBlue1,
                            R.color.chartBlue2,
                            R.color.chartBlue3,
                            R.color.chartBlue4,
                            R.color.chartBlue5,
                            R.color.chartBlue6,
                            R.color.chartBlue7},
                    getApplicationContext());
        }
        else {
            dataSet.setColors(new int[]{
                            R.color.chartRed1,
                            R.color.chartRed2,
                            R.color.chartRed3,
                            R.color.chartRed4,
                            R.color.chartRed5,
                            R.color.chartRed6,
                            R.color.chartRed7},
                    getApplicationContext());
        }

        dataSet.setValueFormatter(new ChartValueFormatter());
        dataSet.setValueTextSize(10);
        dataSet.setValueTextColor(Color.WHITE);
        PieData data = new PieData(dataSet);
        mPieChart.setData(data);
        Description description = mPieChart.getDescription();
        description.setText("Portion percentage");
        Legend legend = mPieChart.getLegend();
        legend.setEnabled(false);
        legend.setWordWrapEnabled(true);
        mPieChart.setDescription(description);
        mPieChart.setUsePercentValues(true);
        mPieChart.setDrawEntryLabels(false);
        if (animate) {
            mPieChart.animateY(1000);
        }
        mPieChart.invalidate();
    }


    private String[] findStringArrayRes(String resName) {
        int resId = getApplicationContext().getResources().getIdentifier(resName,
                "array", getPackageName());
        return getApplicationContext().getResources().getStringArray(resId);
    }

    private void showAlertDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_USERNAME, mUsername);
        FragmentManager fm = getSupportFragmentManager();
        InputTextDialogFragment dialog = InputTextDialogFragment.newInstance();
        dialog.setArguments(bundle);
        dialog.show(fm, "fragment_alert");
    }

    private void saveAsDbAction(String newPlanName) {
        DbInterface.addPlan(mUsername, newPlanName, foodAmount);
        DbInterface.updateUserCurrentPlanName(mUsername, newPlanName);
        finish();
    }

    @Override
    public void sendInput(String input) {
        Log.i(TAG, "sendInput: got the input: " + input + CLASSTAG);
        saveAsDbAction(input);
    }
}
