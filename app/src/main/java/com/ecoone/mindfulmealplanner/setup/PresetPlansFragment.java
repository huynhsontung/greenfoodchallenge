package com.ecoone.mindfulmealplanner.setup;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.ecoone.mindfulmealplanner.R;

import java.util.Objects;

public class PresetPlansFragment extends Fragment {
    public static PresetPlansFragment newInstance() { return new PresetPlansFragment(); }
    private static final String CLASSTAG = "(PlanSetterFragment)";
    private static final String TAG = "testActivity";
    InitialSetupViewModel mViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial_setup_preset_plans,container,false);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(InitialSetupViewModel.class);
        return view;
    }


}
