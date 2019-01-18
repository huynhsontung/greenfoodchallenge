package com.ecoone.mindfulmealplanner.explore;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.HashMap;

public class ExploreDetailViewModel extends ViewModel {
    String mealIdentifier;
    String mealName;
    String mealDescription;
    String authorName;
    String authorIcon;
    String authorUid;
    String restaurantName;
    ArrayList<String> photoRefs = new ArrayList<>();
    ArrayList<String> foodNames = new ArrayList<>();
    MutableLiveData<Integer> likes = new MutableLiveData<>();

}
