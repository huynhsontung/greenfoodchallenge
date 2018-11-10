package com.ecoone.mindfulmealplanner.Profile;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.Profile.Setting.SettingsActivity;

public class ProfileFragment extends Fragment {

    private LinearLayout settingLayout;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(ProfileFragment)";
    private static final int LOGOUT_SIGN = 0;

    public interface OnDatPassingListener {
        void passDataFromProfileToMain(int input);
    }

    public OnDatPassingListener mOnDatPassingListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView t = view.findViewById(R.id.test);

        Log.i("testActivity", "test: " + t.getTextSize());
        Log.i("testActivity", "test: " + t.getTypeface());
        settingLayout = view.findViewById(R.id.profile_setting_layout);


        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new  Intent(getContext(), SettingsActivity.class);
                startActivityForResult(intent, LOGOUT_SIGN);
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGOUT_SIGN) {
            if (data != null) {
                int logoutSign = SettingsActivity.getLogoutAction(data);
                Log.i(TAG, CLASSTAG + "logoutSign" + logoutSign);
                mOnDatPassingListener.passDataFromProfileToMain(logoutSign);
            }

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnDatPassingListener = (OnDatPassingListener) getContext();
        }catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " +e.getMessage() + CLASSTAG);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, CLASSTAG + " onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, CLASSTAG + " onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, CLASSTAG + " onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, CLASSTAG + " onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, CLASSTAG + " onDestroy");

    }
}