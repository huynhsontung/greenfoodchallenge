package com.ecoone.mindfulmealplanner.dashboard;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class DashboardViewModel extends ViewModel {
    private MutableLiveData<String> currentPlan = new MutableLiveData<>();

    public MutableLiveData<String> getCurrentPlan() {
        return currentPlan;
    }
}
