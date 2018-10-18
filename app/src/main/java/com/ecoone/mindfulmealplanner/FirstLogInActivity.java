package com.ecoone.mindfulmealplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class FirstLogInActivity extends AppCompatActivity {
    private LineChart chart;
    private String[] foodName;
    private int foodLen;
    private LinearLayout[] foodSeekBarView;
    private TextView[] mfoodSeekBarTextView;
    private SeekBar[] mfoodSeekBarAction;
    private TextView[] mfoodSeekBarValueView;

    private static final String TAG = "mActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_log_in);

        chart = findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<>();

        entries.add(new Entry(1,1));
        entries.add(new Entry(2,2));

        LineDataSet dataSet = new LineDataSet(entries, "Label");

        LineData lineData = new LineData(dataSet);
        chart.setData(lineData);
        chart.invalidate();

        initializeSeekBarView();
        setSeekBarValueView();
    }

    private void initializeSeekBarView() {
        foodName = findStringArrayRes("food_name");
        foodLen = foodName.length;
        foodSeekBarView = new LinearLayout[foodLen];
        mfoodSeekBarTextView = new TextView[foodLen];
        mfoodSeekBarAction = new SeekBar[foodLen];
        mfoodSeekBarValueView = new TextView[foodLen];

        foodSeekBarView[0] = findViewById(R.id.seekbar_component_1);
        foodSeekBarView[1] = findViewById(R.id.seekbar_component_2);
        foodSeekBarView[2] = findViewById(R.id.seekbar_component_3);
        foodSeekBarView[3] = findViewById(R.id.seekbar_component_4);
        foodSeekBarView[4] = findViewById(R.id.seekbar_component_5);
        foodSeekBarView[5] = findViewById(R.id.seekbar_component_6);
        foodSeekBarView[6] = findViewById(R.id.seekbar_component_7);

        for (int i = 0; i < foodLen; i++) {
            mfoodSeekBarTextView[i] = foodSeekBarView[i].findViewById(R.id.seekbar_text);
            mfoodSeekBarAction[i] = foodSeekBarView[i].findViewById(R.id.seekbar_action);
            mfoodSeekBarValueView[i] = foodSeekBarView[i].findViewById(R.id.seekbar_value);
            mfoodSeekBarTextView[i].setText(foodName[i]);
            mfoodSeekBarAction[i].setProgress(randInt(0, mfoodSeekBarAction[i].getMax()));
            mfoodSeekBarValueView[i].setText(String.valueOf(mfoodSeekBarAction[i].getProgress()));
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
                mfoodSeekBarValueView[i].setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
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
