package com.ecoone.mindfulmealplanner.MealTracker;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ecoone.mindfulmealplanner.R;

import java.util.List;

public class MealDiscoverAdapter extends RecyclerView.Adapter<MealDiscoverAdapter.MealItemViewHolder> {

    private Context mContext;
    private String[] mStrings;

    MealDiscoverAdapter(Context context, String[] strings) {
        mContext = context;
        mStrings = strings;
    }


    @Override
    public MealItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_meal_discover, parent, false);
        return new MealItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MealItemViewHolder holder, int i) {

    }

    @Override
    public int getItemCount() {
        return mStrings.length;
    }

    class MealItemViewHolder extends RecyclerView.ViewHolder {

        MealItemViewHolder(View itemView) {
            super(itemView);
        }
    }

}
