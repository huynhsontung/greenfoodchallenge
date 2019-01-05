package com.ecoone.mindfulmealplanner.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Meal {
    public String mealName;
    public String mealType;
    public String restaurantName;
    public String mealDescription;
    public boolean isGreen;
    public boolean isPrivate;
    public List<String> tags;
    public HashMap<String, Food> foodList;

    // extra fields for database communication
    public String displayName;
    public String iconName;
    public String userUid;
    public String location;
    public HashMap<String,Integer> metrics;

    public Meal() {
        tags = new ArrayList<>();
        foodList = new HashMap<>();
        metrics = new HashMap<>();
        metrics.put("views",0);
        metrics.put("likes",0);
        metrics.put("shares",0);
    }

    public void clear() {
        mealName = null;
        mealType = null;
        restaurantName = null;
        mealDescription = null;
        tags = new ArrayList<>();
        foodList = new HashMap<>();
        isGreen = false;
        isPrivate = true;
    }

}
