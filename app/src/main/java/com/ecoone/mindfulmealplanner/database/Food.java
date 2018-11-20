package com.ecoone.mindfulmealplanner.database;

import java.util.HashMap;
import java.util.Map;

public class Food {
    public String foodName;
    public int co2eAmount;
    public HashMap<String, Integer> ingredient;

    public Food() {
        ingredient = new HashMap<>();
    }
}
