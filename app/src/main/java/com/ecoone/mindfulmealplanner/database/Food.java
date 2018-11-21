package com.ecoone.mindfulmealplanner.database;

import java.util.HashMap;
import java.util.Map;

public class Food {
    public String foodName;
    public int co2eAmount;
    public HashMap<String, Integer> ingredient;

    public Food() {
        ingredient = new HashMap<>();
        ingredient.put("beef", 0);
        ingredient.put("pork", 0);
        ingredient.put("chicken", 0);
        ingredient.put("fish", 0);
        ingredient.put("eggs", 0);
        ingredient.put("beans", 0);
        ingredient.put("vegetables", 0);
    }
}
