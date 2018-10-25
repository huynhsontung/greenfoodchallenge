package com.ecoone.mindfulmealplanner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.db.Plan;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class ImproveActivity extends AppCompatActivity {

    private String mUsername;
    private String mGender;

    private TextView mCurrentPlanCo2eTextView;
    private TextView mImporvedPlanCo2eTextView;
    private Button editButton;
    private Button saveAsButton;
    private Button saveButton;
    private LinearLayout[] mfoodSeekBarView;
    private TextView[] mfoodSeekBarTextView;
    private SeekBar[] mfoodSeekBarAction;
    private TextView[] mfoodSeekBarValueView;

    private String[] foodName;
    private int foodLen;
    private int[] foodAmount;
    private Plan currentPlan;
    private Plan improvedPlan;

    private AppDatabase mDb;
    private PieChart mPieChart;

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

        mPieChart = findViewById(R.id.improve_piechart);

        mUsername = getIntent().getStringExtra(EXTRA_USERNAME);
        mGender = DbInterface.getGender(mUsername);
        foodName = findStringArrayRes("food_name");
        foodLen = foodName.length;

        mCurrentPlanCo2eTextView = findViewById(R.id.improve_current_plan);
        mImporvedPlanCo2eTextView = findViewById(R.id.improve_improved_plan);
        editButton = findViewById(R.id.improve_edit);
        saveAsButton = findViewById(R.id.improve_save_as);
        saveButton = findViewById(R.id.improve_save);


        Log.i(TAG, "Username in Improve:" + mUsername + CLASSTAG);
        Log.i(TAG, "Gender in Improve:" + mGender + CLASSTAG);

        initializePlansCo2eTextView();
        initializeSeekBarView();
        setButtonAction();
        setSeekBarValueView();
    }

    private void initializePlansCo2eTextView() {
        currentPlan = DbInterface.getCurrentPlan(mUsername);
        Log.i(TAG, "Get the current plan" + CLASSTAG + ":\n" +
                DbInterface.getPlanDatatoString(currentPlan));
        NewPlan mNewPlan = new NewPlan(currentPlan, mGender);
        improvedPlan = mNewPlan.suggestPlan();
        Log.i(TAG, "Get the improved plan" + CLASSTAG + ":\n" +
                DbInterface.getPlanDatatoString(improvedPlan));
        foodAmount = DbInterface.getPlanArray(improvedPlan);
        String mCurrentPlanCo2e = String.valueOf(Calculator.calculateCO2ePerDay(currentPlan));
        mCurrentPlanCo2eTextView.setText(String.format("%s g", mCurrentPlanCo2e));
        mCurrentPlanCo2eTextView.setTextColor(Color.BLACK);
        setImprovedPlanCo2eText(0);
        setPieChartView(DbInterface.getPlanArray(improvedPlan), true,0);
    }

    private void initializeSeekBarView() {
        mfoodSeekBarView = new LinearLayout[foodLen];
        mfoodSeekBarTextView = new TextView[foodLen];
        mfoodSeekBarAction = new SeekBar[foodLen];
        mfoodSeekBarValueView = new TextView[foodLen];

        mfoodSeekBarView[0] = findViewById(R.id.improve_seekbar_component_1);
        mfoodSeekBarView[1] = findViewById(R.id.improve_seekbar_component_2);
        mfoodSeekBarView[2] = findViewById(R.id.improve_seekbar_component_3);
        mfoodSeekBarView[3] = findViewById(R.id.improve_seekbar_component_4);
        mfoodSeekBarView[4] = findViewById(R.id.improve_seekbar_component_5);
        mfoodSeekBarView[5] = findViewById(R.id.improve_seekbar_component_6);
        mfoodSeekBarView[6] = findViewById(R.id.improve_seekbar_component_7);

        for (int i = 0; i < foodLen; i++) {
            mfoodSeekBarTextView[i] = mfoodSeekBarView[i].findViewById(R.id.seekbar_text);
            mfoodSeekBarAction[i] = mfoodSeekBarView[i].findViewById(R.id.seekbar_action);
            mfoodSeekBarValueView[i] = mfoodSeekBarView[i].findViewById(R.id.seekbar_value);
            mfoodSeekBarTextView[i].setText(foodName[i]);
            mfoodSeekBarAction[i].setProgress(foodAmount[i]);
            mfoodSeekBarValueView[i].setText(String.valueOf(foodAmount[i]) + " g");
        }
    }

    private void setButtonAction() {
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < foodLen; i++) {
                    mfoodSeekBarView[i].setVisibility(View.VISIBLE);
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
                InputTextDialogFragment inputTextDialogFragment = new InputTextDialogFragment();
                inputTextDialogFragment.onCreateDialog(null).show();
            }
        });
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
                updateImprovedPlan();
                int color = getColorInt();
                setImprovedPlanCo2eText(color);
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
        float mCurrentPlanCo2e = Calculator.calculateCO2ePerDay(currentPlan);
        float mImporvedPlanCo2e = Calculator.calculateCO2ePerDay(improvedPlan);
        if (mImporvedPlanCo2e < mCurrentPlanCo2e) {
            return 0;
        }
        else if (mImporvedPlanCo2e > mCurrentPlanCo2e){
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

    private void setImprovedPlanCo2eText(int color) {
        String mImporvedPlanCo2e = String.valueOf(Calculator.calculateCO2ePerDay(improvedPlan));
        mImporvedPlanCo2eTextView.setText(String.format("%s g", mImporvedPlanCo2e));
        if (color == 0){
            mImporvedPlanCo2eTextView.setTextColor(Color.BLUE);
        }
        else if (color == 1){
            mImporvedPlanCo2eTextView.setTextColor(Color.RED);
        }
        else {
            mImporvedPlanCo2eTextView.setTextColor(Color.BLACK);
        }
    }


    private void setPieChartView(int[] percentage, boolean animate, int color){
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
        int resId = getResources().getIdentifier(resName,
                "array", getPackageName());
        return getResources().getStringArray(resId);
    }
}
