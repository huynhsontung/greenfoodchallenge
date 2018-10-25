package com.ecoone.mindfulmealplanner;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ecoone.mindfulmealplanner.db.Plan;

import java.util.List;

public class PlanPagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Plan> mPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_pager);
    }
}
