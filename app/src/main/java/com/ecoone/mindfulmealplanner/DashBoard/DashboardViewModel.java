package com.ecoone.mindfulmealplanner.DashBoard;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ecoone.mindfulmealplanner.DB.Plan;

public class DashboardViewModel extends ViewModel {

    public MutableLiveData<String> mCurrentPlanName = new MutableLiveData<>();
    public MutableLiveData<Plan> mCurrentPlan = new MutableLiveData<>();

}
