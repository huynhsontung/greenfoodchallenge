package com.ecoone.mindfulmealplanner;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AutoCompleteRestaurantAdapter extends ArrayAdapter<Map.Entry<String, Boolean>> implements Filterable {

    private List<Map.Entry<String, Boolean>> allRestaurantName, suggestions;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(AutoCompleteRestaurantAdapte)";

    private Filter restaurantFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
//            Log.i(TAG, CLASSTAG + "check constaints: " + constraint);
            if (constraint != null && constraint.length() > 1) {
                suggestions.clear();
                for (Map.Entry<String, Boolean> item : allRestaurantName) {
                    if (item.getKey().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(item);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }
            else {
                return new FilterResults();
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.i(TAG, CLASSTAG + "check publishResults: " + results);
            if (results != null && results.count > 0)  {
                suggestions = (List<Map.Entry<String, Boolean>>) results.values;
                notifyDataSetChanged();
            }
            else {
                suggestions = allRestaurantName;
            }
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Map.Entry<String, Boolean>) resultValue).getKey();
        }
    };

    public AutoCompleteRestaurantAdapter(Context context, List<Map.Entry<String, Boolean>> list) {
        super(context, 0, list);
        allRestaurantName = new ArrayList<>(list);
        suggestions = new ArrayList<>(list);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return restaurantFilter;
    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @Nullable ViewGroup parent) {

        View view = convertView;


        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_autocomplete_restaurant, parent, false);

            TextView restaurantNameTextView = view.findViewById(R.id.restaurant_name_text_view);
            ImageView restaurantIconImageView =view.findViewById(R.id.restaurant_icon_image_view);
            Map.Entry<String, Boolean> item = allRestaurantName.get(position);

            if (item != null) {
                if (item.getValue()) {
                    restaurantIconImageView.setVisibility(View.VISIBLE);
                }
                restaurantNameTextView.setText(item.getKey());
            }


        }

//
//        Map.Entry<String, Boolean> item = (Map.Entry<String, Boolean>) getItem(position);
//
////
//
//        if (item != null) {
//            Log.i(TAG, CLASSTAG + " check item: " + item.getKey() + ", " + item.getValue());
//
//        }
        return view;
    }

    @Nullable
    @Override
    public Map.Entry<String, Boolean> getItem(int position) {
        return allRestaurantName.get(position);
    }

//    @Override
//    public int getCount() {
//        return allRestaurantName.size();
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
}
