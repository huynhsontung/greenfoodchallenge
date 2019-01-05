package com.ecoone.mindfulmealplanner.database;

import android.graphics.Bitmap;

import java.util.HashMap;

public class Food {
    public String foodName;
    public Integer co2eAmount;
    public HashMap<String, Integer> ingredient;
    public Bitmap photoBitmap;
    public String storageReference;

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
