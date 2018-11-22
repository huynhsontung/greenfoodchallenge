package com.ecoone.mindfulmealplanner.profile.account;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ecoone.mindfulmealplanner.R;


public class UserIconDialogFragment extends DialogFragment {

    private GridView userIconGridView;

    private String[] mIconTextList = {
            "android", "chicken", "egg",
            "fish", "meat", "moon",
            "star", "sun", "tree"
    };

    private int[] mIconImageIdList = {
            R.drawable.android, R.drawable.chicken, R.drawable.egg,
            R.drawable.fish, R.drawable.meat, R.drawable.moon,
            R.drawable.star, R.drawable.sun, R.drawable.tree
    };

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(UserIconDialogFragment)";

    public interface OnDataPassingListener {
        void passDataFromUserIconDialogToUserAccount(int input);
    }

    public OnDataPassingListener mOnDataPassingListener;

    public UserIconDialogFragment() {

    }
    public static UserIconDialogFragment newInstance() {
        return new UserIconDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_user_icon_dialog, null,false);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please choose a image").setView(view);
        final AlertDialog dialog = builder.create();

        UserIconAdapter userIconAdapter = new UserIconAdapter(getContext(), mIconTextList, mIconImageIdList);
        userIconGridView = view.findViewById(R.id.user_image_icon_gridview);
        userIconGridView.setAdapter(userIconAdapter);
        userIconGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "item: " + mIconTextList[position], Toast.LENGTH_LONG).show();
                mOnDataPassingListener.passDataFromUserIconDialogToUserAccount(mIconImageIdList[position]);
                dialog.dismiss();
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnDataPassingListener = (OnDataPassingListener) getActivity();
        }catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " +e.getMessage() + CLASSTAG);
        }
    }



}
