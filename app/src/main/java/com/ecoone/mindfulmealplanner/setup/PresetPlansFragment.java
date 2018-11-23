package com.ecoone.mindfulmealplanner.setup;

import android.arch.lifecycle.ViewModelProviders;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecoone.mindfulmealplanner.R;

import java.util.Objects;

public class PresetPlansFragment extends Fragment {
    public static PresetPlansFragment newInstance() {
        return new PresetPlansFragment();
    }

    private static final String CLASSTAG = "(PresetPlans)";
    private static final String TAG = "testActivity";
    InitialSetupViewModel mViewModel;
    RecyclerView presetList;
    private static int flag;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_initial_setup_preset_plans, container, false);
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(InitialSetupViewModel.class);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presetList = view.findViewById(R.id.presets_recycler_view);
        setupRecyclerView();
        flag = 4;
    }

    private void setupRecyclerView() {
        Resources resources = getResources();
        String[] presetNames = resources.getStringArray(R.array.preset_names);
        String[] presetDescs = resources.getStringArray(R.array.preset_descriptions);
        int[] presetImages = new int[presetNames.length];
        presetImages[0] = R.drawable.meat_eater_picture;
        presetImages[1] = R.drawable.veggie_picture;
        presetImages[2] = R.drawable.average_picture;
        presetList.setHasFixedSize(true);
        PlanPresetRecyclerAdapter adapter = new PlanPresetRecyclerAdapter(presetNames, presetDescs, presetImages);
        presetList.setAdapter(adapter);
        presetList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.getSelectedPosition().observe(this, new android.arch.lifecycle.Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                flag = integer + 1;
                setFoodAmount();
            }
        });

    }

    // updates the viewmodels food amount according to users selected preset plan
    public void setFoodAmount() {
        String gender = mViewModel.localUser.gender;
        // 350g daily
        if (gender.equals("male")) {
            switch (flag) {
                case 1:
                    Log.i(TAG, CLASSTAG + 1);
                    mViewModel.foodAmount[0] = 50;
                    mViewModel.foodAmount[1] = 50;
                    mViewModel.foodAmount[2] = 125;
                    mViewModel.foodAmount[3] = 50;
                    mViewModel.foodAmount[4] = 25;
                    mViewModel.foodAmount[5] = 25;
                    mViewModel.foodAmount[6] = 25;
                    break;
                case 2:
                    Log.i(TAG, CLASSTAG + 2);
                    mViewModel.foodAmount[0] = 0;
                    mViewModel.foodAmount[1] = 0;
                    mViewModel.foodAmount[2] = 0;
                    mViewModel.foodAmount[3] = 25;
                    mViewModel.foodAmount[4] = 25;
                    mViewModel.foodAmount[5] = 100;
                    mViewModel.foodAmount[6] = 200;
                    break;
                case 3:
                    Log.i(TAG, CLASSTAG + 3);
                    mViewModel.foodAmount[0] = 50;
                    mViewModel.foodAmount[1] = 50;
                    mViewModel.foodAmount[2] = 75;
                    mViewModel.foodAmount[3] = 25;
                    mViewModel.foodAmount[4] = 25;
                    mViewModel.foodAmount[5] = 50;
                    mViewModel.foodAmount[6] = 75;
                    break;
                default:
                    Log.i(TAG, CLASSTAG + 4);
                    mViewModel.foodAmount[0] = 0;
                    mViewModel.foodAmount[1] = 0;
                    mViewModel.foodAmount[2] = 0;
                    mViewModel.foodAmount[3] = 0;
                    mViewModel.foodAmount[4] = 0;
                    mViewModel.foodAmount[5] = 0;
                    mViewModel.foodAmount[6] = 0;
                    break;
            }
        } else {
            //250g daily
            switch (flag) {
                case 1:
                    mViewModel.foodAmount[0] = 25;
                    mViewModel.foodAmount[1] = 25;
                    mViewModel.foodAmount[2] = 125;
                    mViewModel.foodAmount[3] = 20;
                    mViewModel.foodAmount[4] = 15;
                    mViewModel.foodAmount[5] = 15;
                    mViewModel.foodAmount[6] = 25;
                    break;
                case 2:
                    mViewModel.foodAmount[0] = 0;
                    mViewModel.foodAmount[1] = 0;
                    mViewModel.foodAmount[2] = 0;
                    mViewModel.foodAmount[3] = 10;
                    mViewModel.foodAmount[4] = 20;
                    mViewModel.foodAmount[5] = 70;
                    mViewModel.foodAmount[6] = 150;
                    break;
                case 3:
                    mViewModel.foodAmount[0] = 25;
                    mViewModel.foodAmount[1] = 25;
                    mViewModel.foodAmount[2] = 75;
                    mViewModel.foodAmount[3] = 25;
                    mViewModel.foodAmount[4] = 20;
                    mViewModel.foodAmount[5] = 30;
                    mViewModel.foodAmount[6] = 75;
                    break;
                default:
                    mViewModel.foodAmount[0] = 0;
                    mViewModel.foodAmount[1] = 0;
                    mViewModel.foodAmount[2] = 0;
                    mViewModel.foodAmount[3] = 0;
                    mViewModel.foodAmount[4] = 0;
                    mViewModel.foodAmount[5] = 0;
                    mViewModel.foodAmount[6] = 0;
                    break;
            }
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, CLASSTAG + "paused");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, CLASSTAG + "stopped");
    }


}
