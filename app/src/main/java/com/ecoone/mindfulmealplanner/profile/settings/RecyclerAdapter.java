package com.ecoone.mindfulmealplanner.profile.settings;

import android.support.v7.widget.RecyclerView;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;

class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.CardViewHolder2> {


    class CardViewHolder2 extends RecyclerView.ViewHolder{
        TextView mtitle;
        TextView mdescription;
        ImageView myImage;
        public CardViewHolder2(@NonNull View itemView) {
            super(itemView);
            mtitle = itemView.findViewById(R.id.text_title);
            mdescription = itemView.findViewById(R.id.textView9);
            myImage = itemView.findViewById(R.id.testimageview);

        }
    }
    private String[] title;
    private String[] desc;
    private int[] imageIds;

    public RecyclerAdapter( String[] title, String[] desc, int[] imageIds){
        this.title = title;
        this.desc = desc;
        this.imageIds = imageIds;
    }

    @NonNull
    @Override
    public CardViewHolder2 onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_card_layout_about, viewGroup, false);
        return new CardViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder2 cardViewHolder, int i) {
        cardViewHolder.mtitle.setText(title[i]);
        cardViewHolder.mdescription.setText(desc[i]);
        if(imageIds[i] == 0)
            cardViewHolder.myImage.setVisibility(View.GONE);
        else
            cardViewHolder.myImage.setImageResource(imageIds[i]);
    }

    @Override
    public int getItemCount() {
        return title.length;
    }
}

