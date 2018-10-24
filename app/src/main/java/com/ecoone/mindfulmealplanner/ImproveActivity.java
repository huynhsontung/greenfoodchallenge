package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class ImproveActivity extends AppCompatActivity {
    private LinearLayout[] mSeekBarLayout;
    private SeekBar[] mSeekBars;
    private TextView[] mSeekBarTextViews;

    private String[] foodName;
    private int foodLen;
    private int[] foodAmount;

    private static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.improveactivity.username";
    private static final String TAG = "testActivity";

    public static Intent newIntent(Context packageContext, String username) {
        Intent intent = new Intent(packageContext, MainActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_improve);

        initializeSeekBar();
    }

    private void initializeSeekBar() {
        foodName = findStringArrayRes("food_name");
        foodLen = foodName.length;
        foodAmount = new int[foodLen];
        mSeekBarLayout = new LinearLayout[foodLen];
        mSeekBarTextViews = new TextView[foodLen];
        mSeekBars = new SeekBar[foodLen];

        mSeekBarLayout[0] = findViewById(R.id.improve_seekbar_component_1);
        mSeekBarLayout[1] = findViewById(R.id.improve_seekbar_component_2);
        mSeekBarLayout[2] = findViewById(R.id.improve_seekbar_component_3);
        mSeekBarLayout[3] = findViewById(R.id.improve_seekbar_component_4);
        mSeekBarLayout[4] = findViewById(R.id.improve_seekbar_component_5);
        mSeekBarLayout[5] = findViewById(R.id.improve_seekbar_component_6);
        mSeekBarLayout[6] = findViewById(R.id.improve_seekbar_component_7);

        for (int i = 0; i < foodLen; i++) {
            mSeekBarTextViews[i] = mSeekBarLayout[i].findViewById(R.id.seekbar_text);
            mSeekBars[i] = mSeekBarLayout[i].findViewById(R.id.seekbar_action);
            mSeekBarTextViews[i].setVisibility(View.GONE);
            mSeekBars[i].setVisibility(View.GONE);
        }

    }

    private String[] findStringArrayRes(String resName) {
        int resId = getResources().getIdentifier(resName,
                "array", getPackageName());
        return getResources().getStringArray(resId);
    }
}
