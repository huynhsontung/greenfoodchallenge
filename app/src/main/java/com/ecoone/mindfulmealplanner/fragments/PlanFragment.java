package com.ecoone.mindfulmealplanner.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;

public class PlanFragment extends Fragment {

    private TextView mTextView;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(PlanFragment)";
    private static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.planFragment.username";

    public static PlanFragment newInstance(String planName) {
        Bundle args = new Bundle();
        args.putString(EXTRA_USERNAME, planName);
        PlanFragment fragment = new PlanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_plan, null );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String planName= getArguments().getString(EXTRA_USERNAME);
        Log.i(TAG, "plan name: " + planName + CLASSTAG);
        mTextView = view.findViewById(R.id.fragment_plan_name);
        mTextView.setText(planName);

    }
}
