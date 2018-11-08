package com.ecoone.mindfulmealplanner.InitialSetup;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ecoone.mindfulmealplanner.DB.Plan;
import com.ecoone.mindfulmealplanner.DB.User;

public class InitialSetupViewModel extends ViewModel {
    public User localUser = new User();
    public Plan localPlan = new Plan();
    private MutableLiveData<Boolean> checker = new MutableLiveData<>();
    private MutableLiveData<String> displayName = new MutableLiveData<>();

    public MutableLiveData<Boolean> getChecker() {
        return checker;
    }
    public MutableLiveData<String> getDisplayName(){return displayName;}
}
