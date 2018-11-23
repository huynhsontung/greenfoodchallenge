package com.ecoone.mindfulmealplanner.tutorial;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecoone.mindfulmealplanner.R;

// ** NOTE:
// ** THIS TUTORIAL FOLDER IS NOT USED FOR SPRINT 3
// ** KEPT IN PROJECT FOR FUTURE PURPOSES

public class MealTutorialFragment extends Fragment {
    public static MealTutorialFragment newInstance() {
        return new MealTutorialFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_meal, container, false);
        return view;
    }
}
