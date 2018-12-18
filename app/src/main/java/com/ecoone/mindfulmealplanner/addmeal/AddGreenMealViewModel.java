package com.ecoone.mindfulmealplanner.addmeal;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.view.View;

import com.ecoone.mindfulmealplanner.database.Meal;

import java.util.ArrayList;

public class AddGreenMealViewModel extends ViewModel {
    public MutableLiveData<Meal> meal = new MutableLiveData<>();
    public ArrayList<View> addedFoodViewList;
}

