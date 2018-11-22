package com.ecoone.mindfulmealplanner.explore;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private FragmentCommunication mCommunicator;

    public RecyclerViewAdapter(FragmentCommunication communication) {
        mCommunicator=communication;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_horizontal_scroll_item, viewGroup,false);
        return new RecyclerViewHolder(view, mCommunicator);

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        ((RecyclerViewHolder) viewHolder).bindView(i);

    }

    @Override
    public int getItemCount() {
        return scroll_item_data.title.length;
    }



    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         private TextView tabTextView;
         private ImageView tabImageView;
         private CardView cardView;
        FragmentCommunication mCommunication;

        public RecyclerViewHolder(View itemView, FragmentCommunication Communicator){
            super(itemView);
            tabTextView = (TextView) itemView.findViewById(R.id.tab_text);
            tabImageView = itemView.findViewById(R.id.tab_background);
            cardView = (CardView) itemView.findViewById(R.id.tab_cardview);
            mCommunication = Communicator;
            itemView.setOnClickListener(this);

        }

        public void bindView(int position){
            tabTextView.setText(scroll_item_data.title[position]);
            tabImageView.setImageResource(scroll_item_data.picturePath[position]);
            itemView.setTag(position);
        }

        public void onClick(View view){
            int position = (int) itemView.getTag();
            mCommunication.respond(position,scroll_item_data.title[position],scroll_item_data.picturePath[position]);
        }



    }
}



