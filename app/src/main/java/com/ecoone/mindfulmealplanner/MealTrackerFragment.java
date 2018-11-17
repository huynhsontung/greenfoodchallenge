package com.ecoone.mindfulmealplanner;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class MealTrackerFragment extends Fragment {


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
                Intent mIntent = AddGreenMealPhotoActivity.newIntent(getContext());
                startActivityForResult(mIntent,0);
            }
        });
    }
}
