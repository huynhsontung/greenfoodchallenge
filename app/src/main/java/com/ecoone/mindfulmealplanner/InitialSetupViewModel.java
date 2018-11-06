package com.ecoone.mindfulmealplanner;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ecoone.mindfulmealplanner.db.Plan;
import com.ecoone.mindfulmealplanner.db.User;

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
