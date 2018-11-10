package com.ecoone.mindfulmealplanner.Profile.Setting;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecoone.mindfulmealplanner.R;

public class SettingFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKet) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preference);
    }
    

}
