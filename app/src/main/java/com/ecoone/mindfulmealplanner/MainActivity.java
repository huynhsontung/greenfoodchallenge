package com.ecoone.mindfulmealplanner;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ecoone.mindfulmealplanner.dashboard.DashboardFragment;
import com.ecoone.mindfulmealplanner.explore.ExploreFragment;
import com.ecoone.mindfulmealplanner.pledge.MyPledgeFragment;
import com.ecoone.mindfulmealplanner.pledge.PledgeFragment;
import com.ecoone.mindfulmealplanner.profile.ProfileFragment;
import com.ecoone.mindfulmealplanner.tools.NonSwipeableViewPager;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private NonSwipeableViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private android.support.v7.widget.Toolbar mToolbar;
    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(MainActivity)";
    private static final String SKIP_MAIN_ACTIVITY_TUTORIAL = "mainactivity";
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.main_bottom_nav);
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        MenuItem menuItem = mBottomNavigationView.getMenu().findItem(R.id.nav_dashboard);
        setTitle(menuItem.getTitle());
        setupFragmentListForNav();

        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = mSharedPreferences.edit();
        checkForTutorial();
    }

    // Checks if its the users first time on the app
    // If flag == 0, it is the users first time -> show tutorial
    // otherwise, dont show tutorial
    private void checkForTutorial() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        int flag = mSharedPreferences.getInt(SKIP_MAIN_ACTIVITY_TUTORIAL, 0);
        Drawable d = getResources().getDrawable(R.drawable.cabbage_icon);

        if (flag == 0) {
            final BubbleShowCaseBuilder bubble1 = new BubbleShowCaseBuilder(this)
                    .title("Understand and Improve your current plan!")
                    .titleTextSize(18)
                    .description("Compare previous plans ")
                    .image(d)
                    .targetView(findViewById(R.id.nav_dashboard));


            final BubbleShowCaseBuilder bubble2 = new BubbleShowCaseBuilder(this)
                    .title("Make a Pledge to save CO2e!")
                    .titleTextSize(18)
                    .description("Browse other pledgers and pledge statistics")
                    .targetView(findViewById(R.id.nav_pledge))
                    .image(d);

            final BubbleShowCaseBuilder bubble3 = new BubbleShowCaseBuilder(this)
                    .title("Post your Green Meals here!")
                    .titleTextSize(18)
                    .description("Discover other Green Meals")
                    .targetView(findViewById(R.id.nav_meal_tracker))
                    .image(d);

            final BubbleShowCaseBuilder bubble4 = new BubbleShowCaseBuilder(this)
                    .title("View account details!")
                    .titleTextSize(18)
                    .description("Read about the goal of this app!")
                    .targetView(findViewById(R.id.nav_profile))
                    .image(d);

            new BubbleShowCaseSequence()
                    .addShowCase(bubble1)
                    .addShowCase(bubble2)
                    .addShowCase(bubble3)
                    .addShowCase(bubble4)
                    .show();

            editor.putInt(SKIP_MAIN_ACTIVITY_TUTORIAL, 1);
            editor.apply();
            Log.i(TAG, CLASSTAG + "dashplan done");
        }

    }

    // Creates new instances for the fragments on the bottomnavigationview
    // Sets up the view pager for the bottomnavigationview
    private void setupFragmentListForNav() {
        mViewPager = findViewById(R.id.main_content);
        mViewPager.setOffscreenPageLimit(3);
        FragmentPagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i) {
                    case 0:
                        return DashboardFragment.newInstance();

                    case 1:
                        return PledgeFragment.newInstance();

                    case 2:
                        return ExploreFragment.newInstance();

                    case 3:
                        return ProfileFragment.newInstance();

                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return mBottomNavigationView.getMenu().size();
            }
        };
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                invalidateOptionsMenu();
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    // Switches to the fragment that is clicked on
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        setTitle(menuItem.getTitle());
        switch (menuItem.getItemId()) {
            case R.id.nav_dashboard:
                mViewPager.setCurrentItem(0);
                break;

            case R.id.nav_pledge:
                mViewPager.setCurrentItem(1);
                break;

            case R.id.nav_meal_tracker:
                mViewPager.setCurrentItem(2);
                break;

            case R.id.nav_profile:
                mViewPager.setCurrentItem(3);
                break;

            default:
                return false;
        }
        return true;
    }

    // Creates the action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    // onclick event for sharing peldge
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                Intent intent = new Intent(
                        android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareText = "I have pledged ";
                shareText = shareText + String.valueOf(MyPledgeFragment.pledgeAmount) + "kg of CO2e in this app. Come and take a look!";
                intent.putExtra(

                        android.content.Intent.EXTRA_TEXT, shareText + "     Download this app here https://play.google.com/store/search?q=greenfoodchallenge");

                startActivity(Intent.createChooser(
                        intent,
                        "Share Via"));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
