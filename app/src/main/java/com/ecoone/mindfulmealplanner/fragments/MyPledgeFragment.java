package com.ecoone.mindfulmealplanner.fragments;

import android.app.IntentService;
import android.app.Service;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.DbInterface;
import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.DailyPledgeService;
import com.ecoone.mindfulmealplanner.db.AppDatabase;

import org.w3c.dom.Text;

import java.util.concurrent.TimeUnit;


public class MyPledgeFragment extends Fragment {

    private AppDatabase mDb;
    private String myCurrentPlanName;
    private String mUserName;
    private Context mContext;
    private Button pledgeButton;
    View view;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String pTag = "pledge";
    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(MyPledgeFragment)";

    private static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.myPledgeFragment.username";
    private static final String EXTRA_PLANNAME =
            "com.ecoone.mindfulmealplanner.myPledgeFragment.planname";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public static Intent newIntent(Context mContext) {
        Intent intent = new Intent(mContext, DailyPledgeService.class);
        return intent;
    }

    public MyPledgeFragment() {
        // Required empty public constructor
    }

    public static MyPledgeFragment newInstance(String userName, String planName) {
        Bundle args = new Bundle();
        args.putString(EXTRA_USERNAME, userName);
        args.putString(EXTRA_PLANNAME, planName);

        MyPledgeFragment fragment = new MyPledgeFragment();
        fragment.setArguments(args);

        return fragment;
    }


    // TODO: Rename and change types and number of parameters
/*
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(pTag, "in view");
        mContext = getContext();
        return inflater.inflate(R.layout.fragment_my_pledge, null);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(pTag, "onStart");
        view = getView();
        pledgeButton = view.findViewById(R.id.my_pledge_pledge);
        pledgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = newIntent(getContext());
                mContext.startService(mIntent);
            }
        });
    }
}
