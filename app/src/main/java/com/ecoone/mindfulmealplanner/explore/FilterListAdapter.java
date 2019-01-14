package com.ecoone.mindfulmealplanner.explore;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;

public class FilterListAdapter extends RecyclerView.Adapter {

    public interface FilterListCallback {
        void onFilterSelect(int position, String filter);
    }

    private FilterListCallback handle;

    public FilterListAdapter(FilterListCallback handle) {
        this.handle = handle;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_horizontal_scroll_item, viewGroup,false);
        return new RecyclerViewHolder(view, handle);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
        ((RecyclerViewHolder) viewHolder).bindView(i);
    }

    @Override
    public int getItemCount() {
        return FilterOptions.title.length;
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
         private TextView tabTextView;
         private ImageView tabImageView;
         private CardView cardView;
        FilterListCallback handle;

        public RecyclerViewHolder(View itemView, FilterListCallback handle){
            super(itemView);
            tabTextView = (TextView) itemView.findViewById(R.id.tab_text);
            tabImageView = itemView.findViewById(R.id.tab_background);
            cardView = (CardView) itemView.findViewById(R.id.tab_cardview);
            this.handle = handle;
            itemView.setOnClickListener(this);
        }

        public void bindView(int position){
            tabTextView.setText(FilterOptions.title[position]);
            tabImageView.setImageResource(FilterOptions.picturePath[position]);
            itemView.setTag(position);
        }

        public void onClick(View view){
            int position = (int) itemView.getTag();
            handle.onFilterSelect(position,FilterOptions.title[position]);
        }
    }


}



