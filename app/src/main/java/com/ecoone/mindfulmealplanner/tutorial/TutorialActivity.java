package com.ecoone.mindfulmealplanner.tutorial;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
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
import android.widget.Button;

import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.setup.InitialSetupActivity;

// ** NOTE:
// ** THIS TUTORIAL FOLDER IS NOT USED FOR SPRINT 3
// ** KEPT IN PROJECT FOR FUTURE PURPOSES

public class TutorialActivity extends AppCompatActivity implements Button.OnClickListener {
    private static final int NUMBER_OF_PAGES = 3;

    private static final String CLASSTAG = "(TutorialActivity)";
    private static final String TAG = "testActivity";
    private static final String SKIP_TUTORIAL = "key";

    private ViewPager mViewPager;
    private Button mSkipTutorial;
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = mSharedPreferences.edit();
        checkIfInDashboard();
        setUpViewPager();
        mSkipTutorial = findViewById(R.id.enter_dashboard_button);
        mSkipTutorial.setOnClickListener(this);
    }

    public void checkIfInDashboard() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        int flag = mSharedPreferences.getInt(SKIP_TUTORIAL,0);
        if(flag == 0) {

        }
        else if (flag == 1) {
            skipTutorial();
        }
    }

    public void setUpViewPager() {
        setContentView(R.layout.activity_tutorial);
        Log.i(TAG,CLASSTAG + "view[age");
        mViewPager = findViewById(R.id.tutorial_view_pager);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        TabLayout mTabLayout = findViewById(R.id.dots);
        mTabLayout.setupWithViewPager(mViewPager, true);
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (i == 2){
                    mSkipTutorial.setVisibility(View.INVISIBLE);
                } else {
                    mSkipTutorial.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
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

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, CLASSTAG + " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, CLASSTAG + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, CLASSTAG + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, CLASSTAG + " onStop");
    }
    //
    @Override
    public void onDestroy() {
        super.onDestroy();
        editor.putInt(SKIP_TUTORIAL,1);
        editor.apply();
        Log.d(TAG, CLASSTAG + " onDestroy");
    }


}
