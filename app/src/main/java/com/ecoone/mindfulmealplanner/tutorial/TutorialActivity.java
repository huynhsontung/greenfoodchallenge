package com.ecoone.mindfulmealplanner.tutorial;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.R;

public class TutorialActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int NUMBER_OF_PAGES = 3;

    private static final String CLASSTAG = "(TutorialActivity)";
    private static final String TAG = "testActivity";
    private ViewPager mViewPager;
    private View mSkipTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpViewPager();
        View testButton = findViewById(R.id.enter_dashboard_button);
        testButton.setOnClickListener(this);
    }

    public void setUpViewPager() {
        setContentView(R.layout.activity_tutorial);
        mViewPager = findViewById(R.id.tutorial_view_pager);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        TabLayout mTabLayout = findViewById(R.id.dots);
        mTabLayout.setupWithViewPager(mViewPager, true);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(0);

    }

    @Override
    public void onClick(View view) {
        Log.i(TAG, CLASSTAG + "on click");
        skipTutorial();
    }

    public void skipTutorial() {
        Log.i(TAG, CLASSTAG + "skipping");
        Intent intent = new Intent(TutorialActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    Log.i(TAG,CLASSTAG + 0);
                    return StartTutorialFragment.newInstance();
                case 1:
                    Log.i(TAG,CLASSTAG + 1);
                    return DashboardTutorialFragment.newInstance();
                default:
                    Log.i(TAG,CLASSTAG + 4);
                    return FinishTutorialFragment.newInstance();
            }
        }
        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }
    }

    @Override
    public void onBackPressed() {
        Log.i(TAG, CLASSTAG + "onbackpressed");
        int position = mViewPager.getCurrentItem();
        if (position == 0) {
            super.onBackPressed();
        } else {
            mViewPager.setCurrentItem(position - 1);
        }
    }


}
