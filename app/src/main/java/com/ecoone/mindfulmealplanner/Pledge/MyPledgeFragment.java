package com.ecoone.mindfulmealplanner.Pledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MyPledgeFragment extends Fragment {

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private TextView mEditDoneIcon;
    private EditText editPlesgeName;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(MyPledgeFragment)";


    public static Intent newIntent(Context mContext) {
        Intent intent = new Intent(mContext, DailyPledgeService.class);
        return intent;
    }

    public MyPledgeFragment() {
        // Required empty public constructor
    }

    public static MyPledgeFragment newInstance(String userName, String planName) {
        Bundle args = new Bundle();
        MyPledgeFragment fragment = new MyPledgeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_pledge, null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        mDatabase.child("users").child(userUid)

    }
}
