package com.ecoone.mindfulmealplanner.explore;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.R;



public class ImageAdapter extends BaseAdapter {
    String cityName;
    private int[] images_id={R.drawable.meat_eater_picture,R.drawable.average_picture
            ,R.drawable.vancouver,R.drawable.meat,R.drawable.moon,R.drawable.veggie_picture,R.drawable.green_leaf, R.drawable.egg,R.drawable.fish};
    Context context;
    ImageAdapter(Context ctx){
        this.context = ctx;
    }
    @Override
    public int getCount() {
        return images_id.length;
    }

    public ImageAdapter(Context context,String cityName){
        this.cityName = cityName;
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
        return images_id[position];
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
            imageView.setPadding(4,4,4,4);
        } else {
            imageView = (ImageView) convertView;
        }
        imageView.setImageResource(images_id[position]);
        return imageView;
    }
}

