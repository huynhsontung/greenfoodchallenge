package com.ecoone.mindfulmealplanner.setup;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
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
    private static final String CLASSTAG = "(PresetPlans)";
    private static final String TAG = "testActivity";
    InitialSetupViewModel mViewModel;
    private static CardView mMeatCard;
    private static CardView mVeggieCard;
    private static CardView mAverageCard;
    private static CardView mOtherCard;
    private static int flag;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial_setup_preset_plans,container,false);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(InitialSetupViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mMeatCard = view.findViewById(R.id.meat_eater_card);
        mVeggieCard = view.findViewById(R.id.veggie_card);
        mAverageCard = view.findViewById(R.id.average_card);
        mOtherCard = view.findViewById(R.id.other_card);
        mOtherCard.setCardBackgroundColor(Color.parseColor("#40e0d0"));
        flag = 4;
        setOnCardClickListener();

    }

    public void setOnCardClickListener() {
        mMeatCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeColorFromPreviousCard();
                flag = 1;
                mMeatCard.setCardBackgroundColor(Color.parseColor("#40e0d0"));
            }
        });

        mVeggieCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeColorFromPreviousCard();
                flag = 2;
                mVeggieCard.setCardBackgroundColor(Color.parseColor("#40e0d0"));
            }
        });

        mAverageCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeColorFromPreviousCard();
                flag = 3;
                mAverageCard.setCardBackgroundColor(Color.parseColor("#40e0d0"));
            }
        });

        mOtherCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeColorFromPreviousCard();
                flag = 4;
                mOtherCard.setCardBackgroundColor(Color.parseColor("#40e0d0"));
            }
        });
    }

    public void removeColorFromPreviousCard() {
        switch(flag) {
            case 1:
                mMeatCard.setCardBackgroundColor(Color.WHITE);
            case 2:
                mVeggieCard.setCardBackgroundColor(Color.WHITE);
            case 3:
                mAverageCard.setCardBackgroundColor(Color.WHITE);
            case 4:
                mOtherCard.setCardBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (this.isVisible()) {
            Log.i(TAG, CLASSTAG + "vis");
            if (!isVisibleToUser) {
                Log.i(TAG, CLASSTAG + "not vis");

            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,CLASSTAG + "paused");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG,CLASSTAG + "stopped");
    }


}
