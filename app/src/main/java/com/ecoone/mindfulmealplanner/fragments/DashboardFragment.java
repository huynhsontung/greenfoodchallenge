package com.ecoone.mindfulmealplanner.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.dbInterface;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DashboardFragment extends Fragment {

    private String mUsername;
    private String mGender;
    private String mCurrentPlan;

    private TextView nameTextView;
    private TextView genderTextView;
    private TextView dbTextView;
    private TextView currentPlan;
    private Button testAddPlans;

    private AppDatabase mDb;
    private dbInterface mDbInterface;

    private String[] foodName;
    private int[] foodAmount;
    private int foodLen;

    private PieChart mPieChart;

    private static final String TAG = "testActivity";

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
        mDbInterface = new dbInterface(mDb);

        nameTextView = view.findViewById(R.id.fragment_dashboard_test_name);
        genderTextView = view.findViewById(R.id.fragment_dashboard_test_gender);
        currentPlan = view.findViewById(R.id.fragment_dashboard_test_currentplan);
        dbTextView = view.findViewById(R.id.fragment_dashboard_test_db);
        dbTextView.setMovementMethod(new ScrollingMovementMethod());
        testAddPlans = view.findViewById(R.id.fragment_dashboard_test_add_plan);

        testAddPlans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] foodAmount = new int[7];
                for (int i = 0; i < 7; i++) {
                    foodAmount[i] = randInt(0, 300);
                }
                mDbInterface.addPlan(mUsername, foodAmount);
                showUserInfo();
            }
        });

        mPieChart = view.findViewById(R.id.fragment_dashboard_pie_chart);

        mUsername = getArguments().getString(MainActivity.EXTRA_USERNAME);
        mGender = mDbInterface.getGenderbyUsername(mUsername);
        mCurrentPlan = mDbInterface.getCurrentPlanNamebyUsername(mUsername);

        Log.i(TAG, "Name in dashboard fragment: " + mUsername);

        foodName = findStringArrayRes("food_name");
        foodLen = foodName.length;
        foodAmount = mDbInterface.getCurrentPlanArray(mUsername, mCurrentPlan);
        setPieChartView(foodAmount);

        showUserInfo();
    }

    private void showUserInfo() {
        nameTextView.setText(mUsername);
        genderTextView.setText(mGender);
        currentPlan.setText(mCurrentPlan);
        dbTextView.setText(mDbInterface.getPlanDatatoString(mUsername));
    }

    private String[] findStringArrayRes(String resName) {
        int resId = getResources().getIdentifier(resName,
                "array", getActivity().getPackageName());
        return getResources().getStringArray(resId);
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

    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max- min + 1) + min;
    }
}
