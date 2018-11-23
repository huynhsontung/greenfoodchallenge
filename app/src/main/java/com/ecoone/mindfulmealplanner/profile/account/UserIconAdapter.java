package com.ecoone.mindfulmealplanner.profile.account;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;

public class UserIconAdapter extends BaseAdapter {

    private Context mContext;
    private String[] mIconTextList;
    private int[] mIconImageIdList;

    public UserIconAdapter(Context context, String[] iconTextList, int[] iconImageIdList) {
        mContext = context;
        mIconTextList = iconTextList;
        mIconImageIdList = iconImageIdList;
    }

    @Override
    public int getCount() {
        return mIconTextList.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View mUserIconGridView;

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            mUserIconGridView = inflater.inflate(R.layout.list_item_user_icon, parent, false);
            TextView mIconTextView = mUserIconGridView.findViewById(R.id.user_icon_text);
            ImageView mIconImageView = mUserIconGridView.findViewById(R.id.user_icon_image);
            mIconTextView.setText(mIconTextList[position]);
            mIconImageView.setImageResource(mIconImageIdList[position]);
        }
        else {
            mUserIconGridView = convertView;
        }
        return mUserIconGridView;
    }
}
