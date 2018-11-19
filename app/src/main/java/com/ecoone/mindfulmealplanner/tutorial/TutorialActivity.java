package com.ecoone.mindfulmealplanner.tutorial;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.database.Pledge;
import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.database.Plan;
import com.ecoone.mindfulmealplanner.tools.NonSwipeableViewPager;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TutorialActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int NUMBER_OF_PAGES = 5;

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
                case 2:
                    Log.i(TAG,CLASSTAG + 2);
                    return PledgeTutorialFragment.newInstance();
                case 3:
                    Log.i(TAG,CLASSTAG + 3);
                    return MealTutorialFragment.newInstance();
                default:
                    Log.i(TAG,CLASSTAG + 4);
                    return ProfileTutorialFragment.newInstance();
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