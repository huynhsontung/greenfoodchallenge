package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.db.Plan;
import com.ecoone.mindfulmealplanner.fragments.PlanListFragment;

import java.util.List;

public class PlanPagerActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private List<Plan> mPlans;

    private String mUsername;
    private AppDatabase mDb;

    private static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.planpageractivity.username";


    public static Intent newIntent(Context packageContext, String username) {
        Intent intent = new Intent(packageContext, PlanPagerActivity.class);
        intent.putExtra(EXTRA_USERNAME, username);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_pager);

        mViewPager = findViewById(R.id.plan_view_pager);

        mDb = AppDatabase.getDatabase(getApplicationContext());
        DbInterface.setDb(mDb);

        mUsername = getIntent().getStringExtra(EXTRA_USERNAME);
        mPlans = DbInterface.getAllPlans(mUsername);

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Plan plan = mPlans.get(position);
//                return
            }

            @Override
            public int getCount() {
                return 0;
            }
        });

    }
}
