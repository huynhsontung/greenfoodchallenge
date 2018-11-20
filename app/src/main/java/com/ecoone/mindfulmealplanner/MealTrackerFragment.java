package com.ecoone.mindfulmealplanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;


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
        return inflater.inflate(R.layout.fragment_meal_tracker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button mButton = view.findViewById(R.id.moveToAddMeal);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mIntent = AddGreenMealActivity.newIntent(getContext());
                startActivityForResult(mIntent,0);
            }
        });
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
