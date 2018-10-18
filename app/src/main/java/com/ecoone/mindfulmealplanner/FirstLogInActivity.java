package com.ecoone.mindfulmealplanner;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class FirstLogInActivity extends AppCompatActivity {
    private PieChart mPieChart;
    private String[] foodName;
    private int[] foodAmount;
    private int foodLen;
    private LinearLayout[] mfoodSeekBarView;
    private TextView[] mfoodSeekBarTextView;
    private SeekBar[] mfoodSeekBarAction;
    private TextView[] mfoodSeekBarValueView;
    private Button mSubmit;

    private static final String TAG = "mActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_log_in);

        mPieChart = findViewById(R.id.pie_chart);

        initializeSeekBarView();
        setSeekBarValueView();
        setPieChartView(foodAmount);

        mSubmit = findViewById(R.id.first_log_in_button);
        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstLogInActivity.this, DashboardActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initializeSeekBarView() {
        foodName = findStringArrayRes("food_name");
        foodLen = foodName.length;
        foodAmount = new int[foodLen];
        mfoodSeekBarView = new LinearLayout[foodLen];
        mfoodSeekBarTextView = new TextView[foodLen];
        mfoodSeekBarAction = new SeekBar[foodLen];
        mfoodSeekBarValueView = new TextView[foodLen];

        mfoodSeekBarView[0] = findViewById(R.id.seekbar_component_1);
        mfoodSeekBarView[1] = findViewById(R.id.seekbar_component_2);
        mfoodSeekBarView[2] = findViewById(R.id.seekbar_component_3);
        mfoodSeekBarView[3] = findViewById(R.id.seekbar_component_4);
        mfoodSeekBarView[4] = findViewById(R.id.seekbar_component_5);
        mfoodSeekBarView[5] = findViewById(R.id.seekbar_component_6);
        mfoodSeekBarView[6] = findViewById(R.id.seekbar_component_7);

        for (int i = 0; i < foodLen; i++) {
            mfoodSeekBarTextView[i] = mfoodSeekBarView[i].findViewById(R.id.seekbar_text);
            mfoodSeekBarAction[i] = mfoodSeekBarView[i].findViewById(R.id.seekbar_action);
            mfoodSeekBarValueView[i] = mfoodSeekBarView[i].findViewById(R.id.seekbar_value);
            mfoodSeekBarTextView[i].setText(foodName[i]);
            mfoodSeekBarAction[i].setProgress(randInt(0, mfoodSeekBarAction[i].getMax()));
            String amountText = String.valueOf(mfoodSeekBarAction[i].getProgress());
            mfoodSeekBarValueView[i].setText(amountText);
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
                mfoodSeekBarValueView[i].setText(amountText);
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
}
