package com.ecoone.mindfulmealplanner.explore;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class MealImageAdapter extends BaseAdapter {

    private Context context;
    private HashMap<String, Object> mealList;
    private ArrayList<String> mealNameList;
    private String resumePoint;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    MealImageAdapter(Context context, HashMap<String, Object> mealList){
        this.context = context;
        this.mealList = mealList;
        mealNameList = new ArrayList<>(mealList.keySet());
        Collections.reverse(mealNameList);
    }

    MealImageAdapter(Context context, HashMap<String, Object> mealList, String resumePoint){
        this.context = context;
        HashMap<String, Object> newMealList = new HashMap<>();
        newMealList.putAll(mealList);
        newMealList.putAll(this.mealList);
        mealList = newMealList;
        mealNameList = new ArrayList<>(mealList.keySet());
        Collections.reverse(mealNameList);
        this.resumePoint = resumePoint;
    }

    public static ExploreFragment newInstance() {

        Bundle args = new Bundle();

        ExploreFragment fragment = new ExploreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    HashMap<String, Object> getMealList() {
        return mealList;
    }

    @Override
    public int getCount() {
        return mealNameList.size();
    }

    @Override
    public Object getItem(int position) {
        return mealNameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        int paddingSize = 2;
        int imageSize = displayMetrics.widthPixels/3 - paddingSize*2;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(imageSize, imageSize));
            imageView.setPadding(paddingSize,paddingSize,paddingSize,paddingSize);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setTag(mealNameList.get(position));
            try {
                fetchImage(mealNameList.get(position), imageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            imageView = (ImageView) convertView;
        }
        return imageView;
    }

    private void fetchImage(String mealName, ImageView update) throws IOException {
        HashMap<String, Object> mealObj = (HashMap<String, Object>) mealList.get(mealName);
        HashMap<String, Object> foodList = (HashMap<String, Object>) mealObj.get("foodList");
        HashMap<String, Object> firstFood = (HashMap<String, Object>) foodList.get(foodList.keySet().toArray()[0]);
        String refUrl = (String) firstFood.get("storageReference");
        StorageReference imageRef = storage.getReferenceFromUrl(refUrl);
        File tmpFile = File.createTempFile(mealName + "_thumb", "jpg");
        imageRef.getFile(tmpFile).addOnSuccessListener(taskSnapshot -> {
            Bitmap bitmap = BitmapFactory.decodeFile(tmpFile.getPath());
            update.setImageBitmap(bitmap);
        }).addOnFailureListener(Throwable::printStackTrace);
    }


}

