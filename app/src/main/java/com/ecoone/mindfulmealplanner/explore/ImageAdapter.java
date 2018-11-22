package com.ecoone.mindfulmealplanner.explore;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.R;



public class ImageAdapter extends BaseAdapter {
    String ioi;
    private int[] images_id={R.drawable.surrey,R.drawable.anmore,R.drawable.vancouver,R.drawable.burnaby,R.drawable.westvancouver,R.drawable.whiterock,R.drawable.richmond,R.drawable.districtlangley,R.drawable.citynvan,R.drawable.districtnvan,R.drawable.anmore,R.drawable.vancouver,R.drawable.surrey};
    Context ctx;
    ImageAdapter(Context ctx){
        this.ctx = ctx;
    }
    @Override
    public int getCount() {
        return images_id.length;
    }

    public ImageAdapter(Context c,String argu){
        ioi = argu;
        ctx = c;
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

        View gridView = convertView;
        if(gridView==null){
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.discover_pics,null);
        }

        ImageView i1=(ImageView)gridView.findViewById(R.id.myImage);
        i1.setImageResource(images_id[position]);
        Toast.makeText(ctx, ioi, Toast.LENGTH_SHORT).show();
        return gridView;
    }
}

