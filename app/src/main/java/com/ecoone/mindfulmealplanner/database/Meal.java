package com.ecoone.mindfulmealplanner.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Meal {
    public String mealName;
    public String mealType;
    public String restaurantName;
    public String mealDescription;
    public List<String> tags;
    public boolean isGreen;
    public boolean isPrivate;
    public int totalCo2eAmount;
    public HashMap<String, Object> foodInfo;

    public Meal() {
        tags = new ArrayList<>();
        foodInfo = new HashMap<>();
    }

    public void clear() {
        mealName = null;
        mealType = null;
        restaurantName = null;
        mealDescription = null;
        tags = null;
        foodInfo = null;
        totalCo2eAmount = 0;
        isGreen = false;
        isPrivate = true;
    }

}
