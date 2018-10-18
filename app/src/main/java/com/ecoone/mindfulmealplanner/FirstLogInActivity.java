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
import java.util.concurrent.ThreadLocalRandom;


public class FirstLogInActivity extends AppCompatActivity {
    private LineChart chart;
    private LinearLayout[] foodSeekBarView;

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

        setSeekBarView();
    }

    private void setSeekBarView() {
        String[] foodName = findStringArrayRes("food_name");
        foodSeekBarView = new LinearLayout[foodName.length];
        foodSeekBarView[0] = findViewById(R.id.seekbar_component_1);
        Log.i(TAG, foodName[0]);
        foodSeekBarView[1] = findViewById(R.id.seekbar_component_2);
        foodSeekBarView[2] = findViewById(R.id.seekbar_component_3);
        foodSeekBarView[3] = findViewById(R.id.seekbar_component_4);
        foodSeekBarView[4] = findViewById(R.id.seekbar_component_5);
        foodSeekBarView[5] = findViewById(R.id.seekbar_component_6);
        foodSeekBarView[6] = findViewById(R.id.seekbar_component_7);
        for (int i = 0; i < foodName.length; i++) {
            TextView mfoodSeekBarTextView = foodSeekBarView[i].findViewById(R.id.seekbar_text);
            SeekBar mfoodSeekBarAction = foodSeekBarView[i].findViewById(R.id.seekbar_action);
            TextView mfoodSeekBarValueView = foodSeekBarView[i].findViewById(R.id.seekbar_value);
            mfoodSeekBarTextView.setText(foodName[i]);
            mfoodSeekBarAction.setProgress(randInt(0, mfoodSeekBarAction.getMax()));
            mfoodSeekBarValueView.setText(String.valueOf(mfoodSeekBarAction.getProgress()));
        }
    }

    private String[] findStringArrayRes(String resName) {
        int resId = getResources().getIdentifier(resName,
                "array", getPackageName());
        return getResources().getStringArray(resId);
    }

    private static int randInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
