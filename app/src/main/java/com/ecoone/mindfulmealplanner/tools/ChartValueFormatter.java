package com.ecoone.mindfulmealplanner.tools;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class ChartValueFormatter implements IValueFormatter {
    private DecimalFormat mFormat;

    public ChartValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        if(value < 3) return "";
            else return mFormat.format(value) + "%";
    }
}
