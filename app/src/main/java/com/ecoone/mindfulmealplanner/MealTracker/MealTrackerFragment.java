package com.ecoone.mindfulmealplanner.MealTracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ecoone.mindfulmealplanner.MealTracker.AddMeal.AddGreenMealActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.dashboard.DashboardFragment;
import com.ecoone.mindfulmealplanner.dashboard.DashboardPlanFragment;
import com.ecoone.mindfulmealplanner.dashboard.planlist.PlanListFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MealTrackerFragment extends Fragment {

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(MealTrackerFragment)";

    public MealTrackerFragment() {
        // Required empty public constructor
    }
    public static MealTrackerFragment newInstance() {

        Bundle args = new Bundle();

        MealTrackerFragment fragment = new MealTrackerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.fragment_meal_tracker, container, false);

        ViewPager viewPager = rootView.findViewById(R.id.meal_discover_viewpager);
        setupViewPager(viewPager);

        TabLayout myTabs = rootView.findViewById(R.id.meal_discover_tabs);
        myTabs.setupWithViewPager(viewPager);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



//        Button mButton = view.findViewById(R.id.moveToAddMeal);
//        mButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent mIntent = AddGreenMealActivity.newIntent(getContext());
//                startActivityForResult(mIntent,0);
//            }
//        });
    }


    private void setupViewPager(ViewPager viewPager) {

        MealTrackerFragment.Adapter myAdapter = new  MealTrackerFragment.Adapter(getChildFragmentManager());
        myAdapter.addFragment(new MealDiscoverFragment(), "Test1");
        myAdapter.addFragment(new MealDiscoverFragment(), "Test2");
        viewPager.setAdapter(myAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                Objects.requireNonNull(getActivity()).invalidateOptionsMenu();
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, CLASSTAG + " onDestroy");
    }
}
