package com.ecoone.mindfulmealplanner.Pledge;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class PledgeFragment extends Fragment {

    public PledgeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_pledge, null );

        ViewPager viewPager = rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout myTabs = rootView.findViewById(R.id.result_tabs);
        myTabs.setupWithViewPager(viewPager);

        ViewGroup vg = (ViewGroup) myTabs.getChildAt(0);

        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    Log.i("testActivity", "test: " + ((TextView) tabViewChild).getTextSize());
                    Log.i("testActivity", "test: " + ((TextView) tabViewChild).getTypeface());
                }
            }
        }


        /*
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("mypledge").setIndicator("My Pledge"),
                MyPledgeFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("discover").setIndicator("Discover"),
                DiscoverFragment.class, null);
        */
        return rootView;
    }

    private void setupViewPager(ViewPager viewPager) {

        Adapter myAdapter = new Adapter(getChildFragmentManager());
        myAdapter.addFragment(new MyPledgeFragment(), "My Pledge");
        myAdapter.addFragment(new DiscoverFragment(), "Discover");
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

    static class Adapter extends FragmentPagerAdapter {
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


}
