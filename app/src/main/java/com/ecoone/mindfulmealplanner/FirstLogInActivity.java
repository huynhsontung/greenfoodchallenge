package com.ecoone.mindfulmealplanner;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirstLogInActivity extends AppCompatActivity {
    private LineChart chart;

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
    }
}
