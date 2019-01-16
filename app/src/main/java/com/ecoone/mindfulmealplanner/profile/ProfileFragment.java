package com.ecoone.mindfulmealplanner.profile;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.database.User;
import com.ecoone.mindfulmealplanner.profile.account.UserAccountActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.profile.settings.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

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

    private ValueEventListener mValueEventListener;

    final DatabaseReference mDatabase = FirebaseDatabaseInterface.getDatabaseInstance();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(ProfileFragment)";
    private static final int LOGOUT_SIGN = 0;


    public static ProfileFragment newInstance() {

        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        settingLayout = view.findViewById(R.id.profile_setting_layout);
        userAccountLayout = view.findViewById(R.id.profile_user_data_detail);
        usericonImageView = view.findViewById(R.id.profile_user_icon);
        userDisplayNameTextView = view.findViewById(R.id.profile_user_display_name);
        userEmailTextView = view.findViewById(R.id.profile_user_email);
        setHasOptionsMenu(true);

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

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    userDispalyName = user.displayName;
                    userEmail = user.email;
                    userGender = user.gender;
                    userIconId = getDrawableIdByName(user.iconName);
                    userDisplayNameTextView.setText(userDispalyName);
                    userEmailTextView.setText(userEmail);
                    usericonImageView.setImageResource(userIconId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };


        mDatabase.child(FirebaseDatabaseInterface.ALLUSERSUID_NODE).child(userUid)
                .child(FirebaseDatabaseInterface.USERINFO_NODE)
                .addValueEventListener(mValueEventListener);
    }

    // Hides the share button on actionbar
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.action_share).setVisible(false);
        super.onPrepareOptionsMenu(menu);
    }

    private int getDrawableIdByName(String name) {
        Log.i(TAG, CLASSTAG + "Icon name " + name);
        return getResources()
                .getIdentifier(name, "drawable", Objects.requireNonNull(getActivity()).getPackageName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, CLASSTAG + " onDestroy");
        mDatabase.child(FirebaseDatabaseInterface.ALLUSERSUID_NODE).child(userUid)
                .child(FirebaseDatabaseInterface.USERINFO_NODE).removeEventListener(mValueEventListener);

    }
}