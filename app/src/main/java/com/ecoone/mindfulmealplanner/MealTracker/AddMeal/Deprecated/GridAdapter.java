package com.ecoone.mindfulmealplanner.MealTracker.AddMeal.Deprecated;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.ecoone.mindfulmealplanner.R;

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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        final ImageView mImageView;
        if (convertView == null) {
            mImageView = new ImageView(context);
            mImageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setPadding(20, 100, 8, 100);
        } else {
            mImageView = (ImageView) convertView;
        }
        mImageView.setImageBitmap(images.get(i));

        mImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, mImageView);
                popupMenu.getMenuInflater().inflate(R.menu.add_green_meal_delete_photo, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        images.remove(i);
                        notifyDataSetChanged();
                        return true;
                    }
                });
                popupMenu.show();
                //images.remove(i);
                //notifyDataSetChanged();
                return true;
            }
        });
        // mImageView.setImageResource(imageIDs[position]);
        return mImageView;
    }
}
