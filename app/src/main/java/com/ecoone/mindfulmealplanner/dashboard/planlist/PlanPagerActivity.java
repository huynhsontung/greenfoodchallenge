//package com.ecoone.mindfulmealplanner.DashBoard.PlanList;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.ActionBar;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.MenuItem;
//
//import com.ecoone.mindfulmealplanner.DB.DbInterface;
//import com.ecoone.mindfulmealplanner.R;
//import com.ecoone.mindfulmealplanner.DB.AppDatabase;
//import com.ecoone.mindfulmealplanner.DB.Plan;
//
//import java.util.List;
//
//public class PlanPagerActivity extends AppCompatActivity {
//
//    private ViewPager mViewPager;
//
//    private List<Plan> mPlans;
//    private String mUsername;
//    private String mPlanName;
//    private List<String> mAllPlanName;
//    private AppDatabase mDb;
//
//    private static final String EXTRA_USERNAME =
//            "com.ecoone.mindfulmealplanner.planpageractivity.username";
//    private static final String EXTRA_PLANNAME =
//            "com.ecoone.mindfulmealplanner.planpageractivity.planname";
//
//    private static final String TAG = "testActivity";
//    private static final String CLASSTAG = "(PlanPageActivity)";
//
//
//    public static Intent newIntent(Context packageContext,
//                                   String username,
//                                   String planName) {
//        Intent intent = new Intent(packageContext, PlanPagerActivity.class);
//        intent.putExtra(EXTRA_USERNAME, username);
//        intent.putExtra(EXTRA_PLANNAME, planName);
//        return intent;
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_plan_pager);
//
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setTitle("Plan Detail");
//
//        mViewPager = findViewById(R.id.plan_view_pager);
//
//        mDb = AppDatabase.getDatabase(getApplicationContext());
//        DbInterface.setDb(mDb);
//
//        mUsername = getIntent().getStringExtra(EXTRA_USERNAME);
//        Log.i(TAG, "User name: " + mUsername + CLASSTAG);
//        mPlanName = getIntent().getStringExtra(EXTRA_PLANNAME);
//        Log.i(TAG, "Plan name: " + mPlanName + CLASSTAG);
//        mPlans = DbInterface.getAllPlans(mUsername);
//        mAllPlanName = DbInterface.getAllPlansName(mUsername);
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        mViewPager.setAdapter(new FragmentPagerAdapter(fragmentManager) {
//
//            @Override
//            public Fragment getItem(int position) {
//                Plan plan = mPlans.get(position);
//                return PlanFragment.newInstance(mUsername, plan.planName);
//            }
//
//            @Override
//            public int getCount() {
//                return mPlans.size();
//            }
//        });
//
//        mViewPager.setCurrentItem(mAllPlanName.indexOf(mPlanName));
//
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (getFragmentManager().getBackStackEntryCount() > 0 ) {
//            getFragmentManager().popBackStack();
//        }
//        else {
//            super.onBackPressed();
//        }
//    }
//}
