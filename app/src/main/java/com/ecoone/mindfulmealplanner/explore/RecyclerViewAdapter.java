package com.ecoone.mindfulmealplanner.explore;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.profile.ProfileFragment;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter {
    private LayoutInflater inflater;
    private List<scroll_item_data> mList;
    public CardView cardView;
    private FragmentCommunication mCommunicator;

    public RecyclerViewAdapter(Context context, List<scroll_item_data> list,FragmentCommunication communication) {
        inflater = LayoutInflater.from(context);
        mList = list;
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
         private TextView mItemText;
         private CircleImageView mCircleImageView;
         public CardView cardView;
        FragmentCommunication mComminication;
        public RecyclerViewHolder(View itemView, FragmentCommunication Communicator){
            super(itemView);
            mItemText= (TextView) itemView.findViewById(R.id.text_popular);
            mCircleImageView= (CircleImageView) itemView.findViewById(R.id.image_popular);
            cardView = (CardView) itemView.findViewById(R.id.cardview);
            mComminication = Communicator;
            itemView.setOnClickListener(this);

        }

        public void bindView(int position){
            mItemText.setText(scroll_item_data.title[position]);
            mCircleImageView.setImageResource(scroll_item_data.picturePath[position]);
        }

        public void onClick(View view){
            Toast.makeText(view.getContext(),scroll_item_data.title[getPosition()],Toast.LENGTH_SHORT).show();
            mComminication.respond(getPosition(),scroll_item_data.title[getPosition()],scroll_item_data.picturePath[getPosition()]);
        }



    }
}



