package com.ecoone.mindfulmealplanner.explore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class ImageAdapter extends BaseAdapter {
//    String cityName;
//    private int[] images_id={R.drawable.surrey,R.drawable.anmore,R.drawable.vancouver};
    private Context context;
    private ArrayList<String> path = new ArrayList<>();

    @Override
    public int getCount() {
        return path.size();
    }

    public ImageAdapter(Context context, ArrayList<String> path){
        this.path = path;
        this.context = context;
    }

    public static ExploreFragment newInstance() {

        Bundle args = new Bundle();

        ExploreFragment fragment = new ExploreFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public Object getItem(int position) {
        return path.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(370, 370));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        for (int i = 0; i < path.size(); i++) {
            storageReference = storageReference.child(path.get(i));
            final long ONE_MEGABYTE = 1024 * 1024;
            storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });

        }
//        imageView.setImageResource(images_id[position]);
//        Toast.makeText(context, cityName, Toast.LENGTH_SHORT).show();
        return imageView;
    }
}

