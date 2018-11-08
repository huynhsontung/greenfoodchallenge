package com.ecoone.mindfulmealplanner.DashBoard.Improve;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.Pledge.PledgeLogic;
import com.ecoone.mindfulmealplanner.Tool.Calculator;
import com.ecoone.mindfulmealplanner.Tool.ChartValueFormatter;
import com.ecoone.mindfulmealplanner.Tool.NewPlan;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.DB.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.DB.Plan;
import com.ecoone.mindfulmealplanner.DB.User;
import com.ecoone.mindfulmealplanner.DashBoard.Improve.InputTextDialogFragment.OnInputListener;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ImproveActivity extends AppCompatActivity implements OnInputListener {

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
    private Plan improvedPlan;

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private PieChart mPieChart;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(ImproveActivity)";

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ImproveActivity.class);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improve);

        mPieChart = findViewById(R.id.improve_piechart);
        mImprovedPlanCo2ePerYearTextView = findViewById(R.id.improve_improved_plan_per_year);
        mPlanDifferenceCo2ePerYearTextView = findViewById(R.id.improve_difference_of_plan_per_year);
        editButton = findViewById(R.id.improve_edit);
        saveAsButton = findViewById(R.id.improve_save_as);
        saveButton = findViewById(R.id.improve_save);

        foodName = findStringArrayRes("food_name");
        foodLen = foodName.length;

        setFirebaseValueListener();

    }

    private void setFirebaseValueListener() {
        mDatabase.child(FirebaseDatabaseInterface.ALLUSERSUID_NODE)
                .child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, CLASSTAG + "firebase listener call");
                User user = dataSnapshot.child("userInfo").getValue(User.class);
                String userGender = user.gender;
                String mCurrentPlanName = user.currentPlanName;
                Plan currentPlan = dataSnapshot.child("planInfo").child(mCurrentPlanName).getValue(Plan.class);
                if (userGender != null && mCurrentPlanName != null) {
                    currentPlan.planName = mCurrentPlanName;
                    initializePlansCo2eTextView(currentPlan, userGender);
                    initializeSeekBarView();
                    setButtonAction();
                    setSeekBarValueView(currentPlan, improvedPlan);
                    PledgeLogic.updateCurrentPlan(currentPlan);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initializePlansCo2eTextView(Plan currentPlan, String gender) {
        NewPlan mNewPlan = new NewPlan(currentPlan, gender);
        improvedPlan = mNewPlan.suggestPlan();
        improvedPlan.planName = currentPlan.planName;
        improvedPlan.planName = currentPlan.planName;
        String str = new DecimalFormat("###,###,###").format(Calculator.usePlanVancouver(improvedPlan));
        String message = String.format("If everyone in Vancouver uses your " +
                "plan, %s tonnes of CO2e can be saved! Way to go!", str);

        final Snackbar mSnackbar = Snackbar.make(findViewById(R.id.layout_improve),
                                    message, Snackbar.LENGTH_INDEFINITE);
        mSnackbar.setAction("Got it!", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSnackbar.dismiss();
            }
        });
        View mSnackbarView = mSnackbar.getView();
        TextView textView = mSnackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextSize(10);
        mSnackbar.show();

        float[] foodAmount = FirebaseDatabaseInterface.getPlanArray(improvedPlan);

        setPlanCo2eText(0, currentPlan);
        setPieChartView(foodAmount, true,0);
    }

    private void setPlanCo2eText(int color, Plan currentPlan) {
        float mImprovedPlanCo2ePerYear = Calculator.calculateCO2ePerYear(improvedPlan);
        float currentPlanCo2ePerYear = Calculator.calculateCO2ePerYear(currentPlan);
        mImprovedPlanCo2ePerYearTextView.setText(String.format("%s Tonnes", new DecimalFormat("###.###").format(mImprovedPlanCo2ePerYear)));
        float differenceCo2ePerYear = Calculator.comparePlan(currentPlanCo2ePerYear, improvedPlan);
        mPlanDifferenceCo2ePerYearTextView.setText(String.format("%s Tonnes", new DecimalFormat("###.###").format(differenceCo2ePerYear)));
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

        float[] foodAmount = FirebaseDatabaseInterface.getPlanArray(improvedPlan);
        for (int i = 0; i < foodLen; i++) {
            mFoodSeekBarTextView[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_text);
            mFoodSeekBarAction[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_action);
            mFoodSeekBarValueView[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_value);
            mFoodSeekBarTextView[i].setText(foodName[i]);
            mFoodSeekBarAction[i].setProgress((int)foodAmount[i]);
            mFoodSeekBarValueView[i].setText(String.valueOf((int)foodAmount[i]) + " g");
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
                FirebaseDatabaseInterface.updatePlan(improvedPlan);
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

    private void setSeekBarValueView(Plan currentPlan, Plan improvedPlan) {
        for (int i = 0; i < foodLen; i++) {
            setSeekBarListener(i,currentPlan, improvedPlan);
        }
    }

    private void setSeekBarListener(final int i, final Plan currentPlan, final Plan improvedPlan) {
        mFoodSeekBarAction[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                float[] mFoodAmount = FirebaseDatabaseInterface.getPlanArray(improvedPlan);
                String amountText = String.valueOf(progress);
                mFoodSeekBarValueView[i].setText(amountText+ " g");
                mFoodAmount[i] = Integer.valueOf(amountText);
                updateImprovedPlan(mFoodAmount);
                int color = getColorInt(currentPlan);
                setPlanCo2eText(color, currentPlan);
                setPieChartView(mFoodAmount, false,color);
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
    private int getColorInt(Plan currentPlan) {
        Float currentPlanCo2ePerYear = Calculator.calculateCO2ePerYear(currentPlan);
        float differenceCo2ePerYear = Calculator.comparePlan(currentPlanCo2ePerYear, improvedPlan);
        if (differenceCo2ePerYear > 0) {
            return 0;
        }
        else if (differenceCo2ePerYear < 0){
            return 1;
        }
        return 2;
    }

    private void updateImprovedPlan(float[] mFoodAmount) {
        this.improvedPlan.beef = mFoodAmount[0];
        this.improvedPlan.pork = mFoodAmount[1];
        this.improvedPlan.chicken = mFoodAmount[2];
        this.improvedPlan.fish = mFoodAmount[3];
        this.improvedPlan.eggs = mFoodAmount[4];
        this.improvedPlan.beans = mFoodAmount[5];
        this.improvedPlan.vegetables = mFoodAmount[6];
    }

    private String[] findStringArrayRes(String resName) {
        int resId = getApplicationContext().getResources().getIdentifier(resName,
                "array", getPackageName());
        return getApplicationContext().getResources().getStringArray(resId);
    }

    private void showAlertDialog() {
        FragmentManager fm = getSupportFragmentManager();
        InputTextDialogFragment dialog = InputTextDialogFragment.newInstance();
        dialog.show(fm, "fragment_alert");
    }

    private void saveAsDbAction(String newPlanName) {
        improvedPlan.planName = newPlanName;
        FirebaseDatabaseInterface.writePlan(improvedPlan);
        FirebaseDatabaseInterface.updateCurrentPlanName(newPlanName);
        finish();
    }

    @Override
    public void sendInput(String input) {
        Log.i(TAG, "sendInput: got the input: " + input + CLASSTAG);
        saveAsDbAction(input);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, CLASSTAG + " onStart");
//        setFirebaseValueListener();
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
