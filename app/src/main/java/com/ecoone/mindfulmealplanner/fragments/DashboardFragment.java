package com.ecoone.mindfulmealplanner.fragments;

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

import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.dbInterface;

public class DashboardFragment extends Fragment {

    private String mUsername;
    private String mGender;

    private TextView nameTextView;
    private TextView genderTextView;
    private TextView dbTextView;
    private TextView currentPlan;

    private AppDatabase mDb;
    private dbInterface mDbInterface;

    private static final String TAG = "testActivity";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_dashboard, null );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // write your code here
        mDb = AppDatabase.getDatabase(getContext());
        mDbInterface = new dbInterface(mDb);

        nameTextView = view.findViewById(R.id.fragment_dashboard_test_name);
        genderTextView = view.findViewById(R.id.fragment_dashboard_test_gender);
        currentPlan = view.findViewById(R.id.fragment_dashboard_test_currentplan);
        dbTextView = view.findViewById(R.id.fragment_dashboard_test_db);

        mUsername = getArguments().getString(MainActivity.EXTRA_USERNAME);
        mGender = mDbInterface.getGenderbyUsername(mUsername);

        Log.i(TAG, "Name in dashboard fragment: " + mUsername);

        mDbInterface.addPlan(mUsername);
        showUserInfo();
    }

    private void showUserInfo() {
        nameTextView.setText(mUsername);
        genderTextView.setText(mGender);
        currentPlan.setText(mDbInterface.getCurrentPlanbyUsername(mUsername));
        dbTextView.setText(mDbInterface.fetchPlanDatatoString(mUsername));
    }
}
