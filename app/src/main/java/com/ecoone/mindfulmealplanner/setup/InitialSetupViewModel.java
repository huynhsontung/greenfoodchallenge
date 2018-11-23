package com.ecoone.mindfulmealplanner.setup;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.ecoone.mindfulmealplanner.database.Plan;
import com.ecoone.mindfulmealplanner.database.User;

// Holds the users profile and plan data
// Only used as a pre-stage before firebase push
public class InitialSetupViewModel extends ViewModel {
    public User localUser = new User();
    public Plan localPlan = new Plan();
    public float[] foodAmount = new float[7];
    private MutableLiveData<String> displayName = new MutableLiveData<>();

    public MutableLiveData<String> getDisplayName(){return displayName;}
}
