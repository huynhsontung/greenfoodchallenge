package com.ecoone.mindfulmealplanner.MealTracker;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecoone.mindfulmealplanner.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MealDiscoverFragment extends Fragment {

    RecyclerView mRecyclerView;

    public MealDiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_meal_discover, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = view.findViewById(R.id.meal_discover_recyclerview);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        String[] strings = new String[15];

        MealDiscoverAdapter mealDiscoverAdapter = new MealDiscoverAdapter(getContext(), strings);
        mRecyclerView.setAdapter(mealDiscoverAdapter);
    }
}
