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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.DB.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.DB.User;
import com.ecoone.mindfulmealplanner.Profile.UserAccount.UserAccountActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.Profile.Setting.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileFragment extends Fragment {

    private int userIconId;
    private String userDispalyName;
    private String userEmail;
    private String userGender;

    private LinearLayout settingLayout;
    private LinearLayout userAccountLayout;
    private ImageView usericonImageView;
    private TextView userDisplayNameTextView;
    private TextView userEmailTextView;

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(ProfileFragment)";
    private static final int LOGOUT_SIGN = 0;

    public interface OnDataPassingListener {
        void passDataFromProfileToMain(int input);
    }

    public OnDataPassingListener mOnDatPassingListener;

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

        settingLayout = view.findViewById(R.id.profile_setting_layout);
        userAccountLayout = view.findViewById(R.id.profile_user_data_detail);
        usericonImageView = view.findViewById(R.id.profile_user_icon);
        userDisplayNameTextView = view.findViewById(R.id.profile_user_display_name);
        userEmailTextView = view.findViewById(R.id.profile_user_email);


        settingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivityForResult(intent, LOGOUT_SIGN);
            }
        });

        userAccountLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = UserAccountActivity.newIntent(getContext(), userDispalyName, userGender,
                                                                        userEmail, userIconId);
               startActivity(intent);
            }
        });


        mDatabase.child(FirebaseDatabaseInterface.ALLUSERSUID_NODE).child(userUid)
                .child(FirebaseDatabaseInterface.USERINFO_NODE)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.getValue(User.class);
                        if (user != null) {
                            userDispalyName = user.displayName;
                            userEmail = user.email;
                            userGender = user.gender;
                            userIconId = getDrawableIdbyName(user.iconName);
                            userDisplayNameTextView.setText(userDispalyName);
                            userEmailTextView.setText(userEmail);
                            usericonImageView.setImageResource(userIconId);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private int getDrawableIdbyName(String name) {
        int resourceId = getActivity()
                .getResources()
                .getIdentifier(name, "drawable", getActivity().getPackageName());
        return resourceId;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == LOGOUT_SIGN) {
//            if (data != null) {
//                int logoutSign = SettingsActivity.getLogoutAction(data);
//                Log.i(TAG, CLASSTAG + "logoutSign" + logoutSign);
//                mOnDatPassingListener.passDataFromProfileToMain(logoutSign);
//            }
//
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnDatPassingListener = (OnDataPassingListener) getContext();
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