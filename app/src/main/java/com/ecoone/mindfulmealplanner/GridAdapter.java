package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private ArrayList<Bitmap> images;
    private LayoutInflater layoutInflater;
    private Context context;

    public GridAdapter(Context context, ArrayList<Bitmap> photos){
        this.context = context;
        this.images = photos;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ImageView mImageView;

        if (convertView == null) {
            mImageView = new ImageView(context);
            mImageView.setLayoutParams(new GridView.LayoutParams(200, 300));
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setPadding(8, 8, 8, 8);
        } else {
            mImageView = (ImageView) convertView;
        }
        mImageView.setImageBitmap(images.get(i));
        // mImageView.setImageResource(imageIDs[position]);
        return mImageView;
    }
}
