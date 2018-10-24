package com.ecoone.mindfulmealplanner.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.Calculator;
import com.ecoone.mindfulmealplanner.ChartValueFormatter;
import com.ecoone.mindfulmealplanner.ImproveActivity;
import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.DbInterface;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DashboardFragment extends Fragment {

    private String mUsername;
    private String mGender;
    private String mCurrentPlanName;
    private String[] foodName;
    private float[] foodAmount;
    private int foodLen;

    private AppDatabase mDb;

    private Button improveButton;
    private PieChart chart1;
    private PieChart chart2;
    private TextView mEditDoneIcon;
    private TextView currentPlanTextView; // just for initializeEditTextView()
    private TextView currentCo2eTextView;
    private EditText editPlanName;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(DashboardFragment)";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dashboard, null );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // write your code here
        mDb = AppDatabase.getDatabase(getContext());
        DbInterface.setDb(mDb);

        foodName = findStringArrayRes("food_name");
        foodLen = foodName.length;

        mUsername = getArguments().getString(MainActivity.EXTRA_USERNAME);
        mGender = DbInterface.getGender(mUsername);
        mCurrentPlanName = DbInterface.getCurrentPlanName(mUsername);
        foodAmount = DbInterface.getCurrentPlanArray(mUsername, mCurrentPlanName);

        improveButton = view.findViewById(R.id.fragment_dashboard_improve);
        chart1= view.findViewById(R.id.PieChart1);
        chart2= view.findViewById(R.id.PieChart2);
        editPlanName = view.findViewById(R.id.fragment_dashboard_edit_plan_name);
        mEditDoneIcon = view.findViewById(R.id.fragment_dashboard_icon_edit_done);
        currentPlanTextView = view.findViewById(R.id.fragment_dashboard_currentplan_text_view); // just for initializeEditTextView()
        currentCo2eTextView = view.findViewById(R.id.CurrentCo2eView);
        improveButton = view.findViewById(R.id.fragment_dashboard_improve);

        initializeEditTextView();
        setEditDoneIconAction(view);
        setupImproveButton();
        pieChartsView();
        calculateCurrentCo2e();

    }

    private void calculateCurrentCo2e() {
        float sumCo2ePerYear = Calculator.calculateCO2ePerYear(mDb.planDao().getPlan(mUsername, mCurrentPlanName));
        String message = getString(R.string.current_co2e, new DecimalFormat("###.###").format(sumCo2ePerYear));
        currentCo2eTextView.setText(message);
        if (sumCo2ePerYear > 1.7)
            currentCo2eTextView.setTextColor(getResources().getColor(R.color.chartRed1));
    }

    private void initializeEditTextView() {
        editPlanName.setText(mCurrentPlanName);
        editPlanName.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentPlanTextView.getTextSize());
        editPlanName.setTypeface(currentPlanTextView.getTypeface());
//        editPlanName.setTextColor(currentPlanTextView.getTextColors()); // grey(uncomment) or black(comment)
        editPlanName.setInputType(0);
    }

    private void setEditDoneIconAction(final View view) {
        mEditDoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPlanName.getInputType() == 0) {
                    editPlanName.setInputType(1);
                    mEditDoneIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.done, 0, 0, 0);
                    editPlanName.setSelection(editPlanName.getText().length());
                    editPlanName.selectAll();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editPlanName, InputMethodManager.SHOW_IMPLICIT);
                }
                else {
                    editPlanName.setInputType(0);
                    mCurrentPlanName = editPlanName.getText().toString();
                    editPlanName.setText(mCurrentPlanName);
                    mEditDoneIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    DbInterface.changeCurrentPlanName(mUsername, mCurrentPlanName);
                    Log.i(TAG, "-------------------------------------------------");
                    Log.i(TAG, "Current Plan name changed" + CLASSTAG);
                    Log.i(TAG, DbInterface.getUserDatatoString(mUsername).toString());
                    Log.i(TAG, DbInterface.getPlanDatatoString(mUsername).toString());
                    Log.i(TAG, "-------------------------------------------------");
                }
            }
        });
    }

    private void pieChartsView() {

//        int beefInGram = foodAmount[0];
//        int porkInGram = foodAmount[1];
//        int chickenInGram = foodAmount[2];
//        int fishInGram = foodAmount[3];
//        int eggsInGram = foodAmount[4];
//        int beansInGram = foodAmount[5];
//        int vegetablesInGram = foodAmount[6];
//
//
//        float beefco2e = beefInGram * 27/1000;
//        float porkco2e = porkInGram* 12/1000;
//        float chickenco2e = chickenInGram*7/1000;
//        float fishco2e = fishInGram*6/1000;
//        float eggsco2e = eggsInGram*5/1000;
//        float beansco2e = beansInGram*2/1000;
//        float vegetablesco2e = vegetablesInGram*2/1000;

//        float sumco2e = beefco2e+porkco2e+chickenco2e+fishco2e+eggsco2e+beansco2e+vegetablesco2e;


//        float beefco2per = beefco2e/sumco2e;
//        float porkco2per = porkco2e/sumco2e;
//        float chickenco2per = chickenco2e/sumco2e;
//        float fishco2per = fishco2e/sumco2e;
//        float eggsco2per = eggsco2e/sumco2e;
//        float beansco2per = beansco2e/sumco2e;
//        float vegetablesco2per = vegetablesco2e/sumco2e;
//
//
//        float sumInGram = beefInGram+porkInGram+chickenInGram+fishInGram+eggsInGram+beansInGram+vegetablesInGram;
//
//        float beefPercentage = beefInGram/sumInGram;
//        float porkPercentage = porkInGram/sumInGram;
//        float chickenPercentage = chickenInGram/sumInGram;
//        float fishPercentage = fishInGram/sumInGram;
//        float eggsPercentage = eggsInGram/sumInGram;
//        float beansPercentage = beansInGram/sumInGram;
//        float vegetablesPercentage = vegetablesInGram/sumInGram;



//        float percentage[] ={ beefPercentage, porkPercentage, chickenPercentage, fishPercentage , eggsPercentage, beansPercentage, vegetablesPercentage};
//        float co2Percentage[] = {beefco2per,porkco2per, chickenco2per,fishco2per,eggsco2per,beansco2per,vegetablesco2per};
//        float sumco2e = Calculator.calculateCO2ePerYear(mDb.planDao().getPlan(mUsername,mCurrentPlanName));
        float[] co2Amount = Calculator.calculateCO2eEachFood(mDb.planDao().getPlan(mUsername, mCurrentPlanName));
        setupPieChart1(foodAmount, foodName);
        setupPieChart2(co2Amount,foodName);
    }

    private void setupImproveButton(){
        //Wire up the button to improve the plan
        //...get the button
        improveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ImproveActivity.newIntent(getContext(), mUsername);
                startActivity(intent);
            }
        });
    }

    private void setupPieChart1(float[] percentage, String[] foodNames){
        //setup pie chart
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i=0; i<percentage.length;i++){
            if(percentage[i] > 0.001)
                pieEntries.add(new PieEntry(percentage[i], foodNames[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,null);
        dataSet.setColors(new int[]{
                        R.color.chartBlue1,
                        R.color.chartBlue2,
                        R.color.chartBlue3,
                        R.color.chartBlue4,
                        R.color.chartBlue5,
                        R.color.chartBlue6,
                        R.color.chartBlue7},
                    getContext());
        dataSet.setValueFormatter(new ChartValueFormatter());
        dataSet.setValueTextSize(10);
        dataSet.setValueTextColor(Color.WHITE);
        PieData data = new PieData(dataSet);
        chart1.setData(data);
        Description description = chart1.getDescription();
        description.setText("Portion percentage");
        Legend legend = chart1.getLegend();
        legend.setWordWrapEnabled(true);
        chart1.setDescription(description);
        chart1.setUsePercentValues(true);
        chart1.setDrawEntryLabels(false);
        chart1.animateY(1000);
        chart1.invalidate();
    }


    private void setupPieChart2(float[] co2Percentage, String[] foodNames){
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i=0; i<co2Percentage.length;i++){
            // filter out small values
            if(co2Percentage[i] > 0.001)
                pieEntries.add(new PieEntry(co2Percentage[i], foodNames[i]));
        }

        PieDataSet dataSet = new PieDataSet(pieEntries,null);
        dataSet.setColors(new int[]{
                        R.color.chartRed1,
                        R.color.chartRed2,
                        R.color.chartRed3,
                        R.color.chartRed4,
                        R.color.chartRed5,
                        R.color.chartRed6,
                        R.color.chartRed7},
                    getContext());
        dataSet.setValueFormatter(new ChartValueFormatter());
        dataSet.setValueTextSize(10);
        dataSet.setValueTextColor(Color.WHITE);
        PieData data = new PieData(dataSet);
        chart2.setData(data);
        Description description = chart2.getDescription();
        description.setText("CO2e percentage");
        Legend legend = chart2.getLegend();
        legend.setWordWrapEnabled(true);
        chart2.setDescription(description);
        chart2.setUsePercentValues(true);
        chart2.setDrawEntryLabels(false);
        chart2.animateY(1000);
        chart2.invalidate();
    }


    private String[] findStringArrayRes(String resName) {
        int resId = getResources().getIdentifier(resName,
                "array", getActivity().getPackageName());
        return getResources().getStringArray(resId);
    }

    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max- min + 1) + min;
    }
}
