package com.ecoone.mindfulmealplanner.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.tools.Calculator;
import com.ecoone.mindfulmealplanner.tools.ChartValueFormatter;
import com.ecoone.mindfulmealplanner.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardChartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardChartFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_CHART_NUM = "chart_num";
    private static final String ARG_CHART_DATA = "chart_data";
    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(DashboardChartFragment)";

    private int chartNum;
    private TextView chartTitle;
    private PieChart chart;
    private String[] foodName;
    private float[] foodAmount;
    private float[] foodCo2Amount;

    public DashboardChartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param chartNum Parameter 1.
     * @param chartData Parameter 2.
     * @return A new instance of fragment DashboardChartFragment.
     */
    public static DashboardChartFragment newInstance(int chartNum, float[] chartData) {
        DashboardChartFragment fragment = new DashboardChartFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CHART_NUM, chartNum);
        args.putFloatArray(ARG_CHART_DATA, chartData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chartNum = getArguments().getInt(ARG_CHART_NUM);
            if (chartNum == 0)
                foodAmount = getArguments().getFloatArray(ARG_CHART_DATA);
            else
                foodCo2Amount = getArguments().getFloatArray(ARG_CHART_DATA);
        }
        foodName = findStringArrayRes("food_name");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard_plan_chart, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chart = view.findViewById(R.id.PieChart);
        chartTitle = view.findViewById(R.id.chartTitle);
        if (chartNum == 0) {
            setupPieChart(Calculator.toPercentage(foodAmount), foodName);
        }
        else {
            setupPieChart(Calculator.toPercentage(foodCo2Amount), foodName);
        }
    }

    void setupPieChart(float[] percentage, String[] foodNames){
        //setup pie chart
        List<PieEntry> pieEntries = new ArrayList<>();
        for (int i=0; i<percentage.length;i++){
            if(percentage[i] > 0.001)
                pieEntries.add(new PieEntry(percentage[i], percentage[i]>3 ? foodNames[i] : null ));
        }
        PieDataSet dataSet = new PieDataSet(pieEntries,null);
        // setup colors, chart description and labels
        int[] colors;
        Description description = chart.getDescription();
        description.setEnabled(false);
        if(chartNum == 0) {
            colors = new int[]{
                    R.color.chartBlue1,
                    R.color.chartBlue2,
                    R.color.chartBlue3,
                    R.color.chartBlue4,
                    R.color.chartBlue5,
                    R.color.chartBlue6,
                    R.color.chartBlue7};
            description.setText("Portion percentage");
            chartTitle.setText("Portion percentage");
        }
        else {
            colors = new int[] {
                    R.color.chartRed1,
                    R.color.chartRed2,
                    R.color.chartRed3,
                    R.color.chartRed4,
                    R.color.chartRed5,
                    R.color.chartRed6,
                    R.color.chartRed7};
            description.setText("CO2e percentage");
            chartTitle.setText("CO2e percentage");
        }
        dataSet.setColors(colors, getContext());
        dataSet.setValueFormatter(new ChartValueFormatter());
        dataSet.setValueTextSize(12);
        dataSet.setValueTextColor(Color.WHITE);
        PieData data = new PieData(dataSet);
        chart.setData(data);
        Legend legend = chart.getLegend();
        legend.setEnabled(false);
        legend.setWordWrapEnabled(true);
        chart.setDescription(description);
        chart.setUsePercentValues(true);
        chart.setHoleRadius(40);
        chart.setTransparentCircleRadius(50);
        chart.setTouchEnabled(false);
        chart.animateY(1000);
        chart.invalidate();
    }


    String[] findStringArrayRes(String resName) {
        int resId = getResources().getIdentifier(resName,
                "array", getActivity().getPackageName());
        return getResources().getStringArray(resId);
    }
}
