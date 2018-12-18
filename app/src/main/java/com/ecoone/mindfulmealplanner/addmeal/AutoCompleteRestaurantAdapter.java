package com.ecoone.mindfulmealplanner.addmeal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AutoCompleteRestaurantAdapter extends ArrayAdapter<Map.Entry<String, Boolean>> {

    private List<Map.Entry<String, Boolean>> allRestaurantName, suggestions;
    private Context mContext;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(AutoCompleteRestaurantAdapter)";

    public AutoCompleteRestaurantAdapter(Context context, List<Map.Entry<String, Boolean>> list) {
        super(context, 0, list);
        mContext = context;
        allRestaurantName = new ArrayList<>(list);
        suggestions = new ArrayList<>(list);
    }



    @Nullable
    @Override
    public Map.Entry<String, Boolean> getItem(int position) {
        return suggestions.get(position);
    }

    @Override
    public int getCount() {
        return suggestions.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @Nullable ViewGroup parent) {




        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =  inflater.inflate(R.layout.list_item_autocomplete_restaurant, null);
        }

        TextView restaurantNameTextView = convertView.findViewById(R.id.restaurant_name_text_view);
        ImageView restaurantIconImageView =convertView.findViewById(R.id.restaurant_icon_image_view);

        Map.Entry<String, Boolean> item = getItem(position);
        Log.i(TAG, CLASSTAG + "Check Item: " +item.getKey() + ", " + item.getValue());


        if (item.getValue()) {
            restaurantIconImageView.setVisibility(View.VISIBLE);
        }
        else {
            restaurantIconImageView.setVisibility(View.GONE);
        }

        restaurantNameTextView.setText(item.getKey());

//
//        Map.Entry<String, Boolean> item = (Map.Entry<String, Boolean>) getItem(position);
//
////
//
//        if (item != null) {
//            Log.i(TAG, CLASSTAG + " check item: " + item.getKey() + ", " + item.getValue());
//
//        }
        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return restaurantFilter;
    }

    private Filter restaurantFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            Log.i(TAG, CLASSTAG + "check constaints: " + constraint);
            FilterResults results = new FilterResults();
            List<Map.Entry<String, Boolean>> newList = new ArrayList<>();
            if (constraint != null) {
                for (Map.Entry<String, Boolean> item : allRestaurantName) {
                    if (item.getKey().toLowerCase().contains(constraint.toString().toLowerCase().trim())) {
                        newList.add(item);
                    }
                }
                results.values = newList;
                results.count = newList.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            Log.i(TAG, CLASSTAG + "check constraint and publishResults: " +constraint + ", " + results.values);
            suggestions.clear();
            if (results != null && results.count > 0)  {
                suggestions.addAll((ArrayList<Map.Entry<String, Boolean>>) results.values);
                notifyDataSetChanged();
            }
            else if (constraint == null) {
                suggestions.addAll(allRestaurantName);
                notifyDataSetInvalidated();
            }
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Map.Entry<String, Boolean>) resultValue).getKey();
        }
    };
//
//    @Nullable
//    @Override
//    public Map.Entry<String, Boolean> getItem(int position) {
//        return suggestions.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return suggestions != null && suggestions.size() > 0 ? suggestions.size() : 0;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
}
