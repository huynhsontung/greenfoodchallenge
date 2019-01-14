package com.ecoone.mindfulmealplanner.addmeal;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.tools.NonSwipeableViewPager;

public class AddGreenMealActivity extends AppCompatActivity {

    private NonSwipeableViewPager mViewPager;
    private AddGeneralFragment generalFragment;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AddGreenMealActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_green_meal);
        mViewPager = findViewById(R.id.add_food_main_content);
        setupViewPager();
    }

    private void setupViewPager() {
        FragmentPagerAdapter adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if (i == 0)
                    return AddGeneralFragment.newInstance();
                else
                    return AddDetailFragment.newInstance();
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        if(fragment instanceof AddDetailFragment){
            ((AddDetailFragment)fragment).setCallback(this);
        } else {
            ((AddGeneralFragment)fragment).setCallback(this);
            generalFragment = (AddGeneralFragment) fragment;
        }
    }

    public void switchPage(){
        if(mViewPager.getCurrentItem() == 0)
            mViewPager.setCurrentItem(1);
        else mViewPager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem() == 1) {
            mViewPager.setCurrentItem(0);
            generalFragment.notifyBackPressed();
        }
        else
            super.onBackPressed();
    }
}
