package com.ecoone.mindfulmealplanner.setup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.tools.ChartValueFormatter;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class PlanSetterFragment extends Fragment {

    private static final String CLASSTAG = "(PlanSetterFragment)";
    private static final String TAG = "testActivity";

    public static PlanSetterFragment newInstance(){
        return new PlanSetterFragment();
    }

    InitialSetupViewModel mViewModel;
    private PieChart mPieChart;
    private String[] foodName;
    private int foodLen;
    private ConstraintLayout[] mFoodSeekBarView;
    private TextView[] mFoodSeekBarTextView;
    private SeekBar[] mFoodSeekBarAction;
    private TextView[] mFoodSeekBarValueView;
    private View view;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG,CLASSTAG + "create view plan setter");
        view = inflater.inflate(R.layout.fragment_initial_setup_set_plan, container, false);
        mViewModel = ViewModelProviders.of(getActivity()).get(InitialSetupViewModel.class);
        mPieChart = view.findViewById(R.id.initial_screen_pie_chart);
        initializeSeekBarView(view);
        setPieChartView(mViewModel.foodAmount);
        return view;
    }

    // If fragment is visible, run the function
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            Log.i(TAG, CLASSTAG + "vis");
            initializeSeekBarView(view);
            if (!isVisibleToUser) {
                Log.i(TAG, CLASSTAG + "not vis");
            }
        }
    }

    private void initializeSeekBarView(View view) {
        foodName = findStringArrayRes("food_name");
        foodLen = foodName.length;
        mFoodSeekBarView = new ConstraintLayout[foodLen];
        mFoodSeekBarTextView = new TextView[foodLen];
        mFoodSeekBarAction = new SeekBar[foodLen];
        mFoodSeekBarValueView = new TextView[foodLen];

        mFoodSeekBarView[0] = view.findViewById(R.id.initial_screen_seekbar_component_1);
        mFoodSeekBarView[1] = view.findViewById(R.id.initial_screen_seekbar_component_2);
        mFoodSeekBarView[2] = view.findViewById(R.id.initial_screen_seekbar_component_3);
        mFoodSeekBarView[3] = view.findViewById(R.id.initial_screen_seekbar_component_4);
        mFoodSeekBarView[4] = view.findViewById(R.id.initial_screen_seekbar_component_5);
        mFoodSeekBarView[5] = view.findViewById(R.id.initial_screen_seekbar_component_6);
        mFoodSeekBarView[6] = view.findViewById(R.id.initial_screen_seekbar_component_7);

        for (int i = 0; i < foodLen; i++) {
            mFoodSeekBarTextView[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_text);
            mFoodSeekBarAction[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_action);
            mFoodSeekBarValueView[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_value);
            mFoodSeekBarTextView[i].setText(foodName[i]);
            mFoodSeekBarAction[i].setProgress((int)mViewModel.foodAmount[i]);
            int amount = mFoodSeekBarAction[i].getProgress();
            Log.i(TAG,CLASSTAG + mFoodSeekBarAction[i].getProgress());
            mFoodSeekBarValueView[i].setText(getString(R.string.amount_gram,amount));
        }
        setSeekBarChangeListener();
    }

    private void setSeekBarChangeListener() {
        for (int i = 0; i < foodLen; i++) {
            final int finalI = i;
            mFoodSeekBarAction[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    mFoodSeekBarValueView[finalI].setText(getString(R.string.amount_gram,progress));
                    mViewModel.foodAmount[finalI] = progress;
                    setPieChartView(mViewModel.foodAmount);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }
    }

    // generates pie cart data
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
                "array", Objects.requireNonNull(getActivity()).getPackageName());
        return getResources().getStringArray(resId);
    }

}
