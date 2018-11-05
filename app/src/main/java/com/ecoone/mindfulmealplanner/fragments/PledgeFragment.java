package com.ecoone.mindfulmealplanner.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import com.ecoone.mindfulmealplanner.R;

import java.util.ArrayList;
import java.util.List;


public class PledgeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FragmentTabHost mTabHost;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public PledgeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_pledge, null );

        ViewPager viewPager = (ViewPager)rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        TabLayout myTabs = (TabLayout) rootView.findViewById(R.id.result_tabs);
        myTabs.setupWithViewPager(viewPager);


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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
