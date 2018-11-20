package com.ecoone.mindfulmealplanner.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Meal {
    public String mealName;
    public List<String> tags;
    public boolean isGreen;
    public HashMap<String, Object> foodInfo;

    public Meal() {
        tags = new ArrayList<>();
        foodInfo = new HashMap<>();
    }

}
