package com.ecoone.mindfulmealplanner.setup;

import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;

class PlanPresetRecyclerAdapter extends RecyclerView.Adapter<PlanPresetRecyclerAdapter.CardViewHolder> implements View.OnClickListener{

    class CardViewHolder extends RecyclerView.ViewHolder{

        TextView headerText;
        TextView subtext;
        ImageView image;
        RadioButton radioButton;
        public CardViewHolder(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.preset_header_text);
            subtext = itemView.findViewById(R.id.preset_subtext);
            image = itemView.findViewById(R.id.preset_image);
            radioButton = itemView.findViewById(R.id.preset_choice);
        }
    }

    private MutableLiveData<Integer> selectedPosition = new MutableLiveData<>();
    private String[] title;
    private String[] desc;
    private int[] imageIds;
    public PlanPresetRecyclerAdapter( String[] title, String[] desc, int[] imageIds){
        selectedPosition.setValue(RecyclerView.NO_POSITION);
        this.title = title;
        this.desc = desc;
        this.imageIds = imageIds;
    }

    @NonNull
    @Override
    public CardViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.component_preset_card,viewGroup, false);
        return new CardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHolder cardViewHolder, int i) {
        cardViewHolder.headerText.setText(title[i]);
        cardViewHolder.subtext.setText(desc[i]);
        if(imageIds[i] == 0)
            cardViewHolder.image.setVisibility(View.GONE);
        else
            cardViewHolder.image.setImageResource(imageIds[i]);

        cardViewHolder.radioButton.setTag(i);
        cardViewHolder.radioButton.setChecked(i == selectedPosition.getValue());
        cardViewHolder.radioButton.setOnClickListener(this);
        cardViewHolder.itemView.setTag(i);
        cardViewHolder.itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        itemCheckChanged(v);
    }

    private void itemCheckChanged(View v) {
        selectedPosition.setValue((Integer) v.getTag());
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return title.length;
    }

    public MutableLiveData<Integer> getSelectedPosition() {
        return selectedPosition;
    }
}
