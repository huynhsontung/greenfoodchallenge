package com.ecoone.mindfulmealplanner.explore;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_horizontal_scroll_item, viewGroup,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        ((RecyclerViewHolder) viewHolder).bindView(i);

    }

    @Override
    public int getItemCount() {
        return scroll_item_data.title.length;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

         private TextView mItemText;
         private CircleImageView mCircleImageView;
        public RecyclerViewHolder(View itemView){
            super(itemView);
            mItemText= (TextView) itemView.findViewById(R.id.text_popular);
            mCircleImageView= (CircleImageView) itemView.findViewById(R.id.image_popular);
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            mItemText.setText(scroll_item_data.title[position]);
            mCircleImageView.setImageResource(scroll_item_data.picturePath[position]);
        }

        public void onClick(View view){

        }

    }
}



