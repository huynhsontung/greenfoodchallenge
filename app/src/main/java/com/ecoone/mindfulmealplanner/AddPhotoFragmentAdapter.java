package com.ecoone.mindfulmealplanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class AddPhotoFragmentAdapter extends FragmentPagerAdapter {
    public AddPhotoFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        AddPhotoFragment addPhotoFragment = new AddPhotoFragment();
        return addPhotoFragment;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
