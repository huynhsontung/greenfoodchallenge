package com.ecoone.mindfulmealplanner.Pledge;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.DB.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.DB.Pledge;
import com.ecoone.mindfulmealplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;


public class MyPledgeFragment extends Fragment {

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public static int pledgeAmount;
    private TextView mEditDoneIcon;
    private EditText editPledgeName;
    private Spinner mSpinner;
    private ArrayAdapter<CharSequence> mAdapter;
    private ArrayList<String> locationList;
    private LinearLayout mLinearLayout;
    private ImageView mImageView;
    private TextView planCO2TextView;

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
        setHasOptionsMenu(true);
        mEditDoneIcon = view.findViewById(R.id.my_pledge_icon_edit_done);
        editPledgeName = view.findViewById(R.id.my_pledge_edit_plan_name);
        mSpinner = view.findViewById(R.id.my_pledge_spinner);
        mAdapter = ArrayAdapter.createFromResource(getContext(), R.array.location, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        planCO2TextView = view.findViewById(R.id.user_tip);
        planCO2TextView.setText(String.format("Note: Your current plan produces %.2f kg of CO2e per week",PledgeLogic.getCurrentPlanCO2PerWeek()));
        locationList = new ArrayList<>(Arrays.asList(findStringArrayRes("location")));

        setFirebaseValueListener();

        setEditDoneIconAction(view);
        setSpinnerListener();
    }

    private void setFirebaseValueListener() {
        mDatabase.child(FirebaseDatabaseInterface.ALLUSERSUID_NODE)
                .child(userUid).child("pledgeInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, CLASSTAG + "firebase listener call");
                Pledge pledge = dataSnapshot.getValue(Pledge.class);

                if (pledge != null) {
                    int amount = pledge.amount;
                    String location = pledge.location;
                    setEditTextView(amount);
                    setSpinnerView(location);
                }
                pledgeAmount = pledge.amount;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setEditTextView(int amount) {
        editPledgeName.setText(String.valueOf(amount));
        editPledgeName.setInputType(0);
    }

    private void setSpinnerView(String location) {
        int position = locationList.indexOf(location);
        mSpinner.setSelection(position);
    }

    private void setEditDoneIconAction(final View view) {
        mEditDoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPledgeName.getInputType() == 0) {
                    editPledgeName.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                    mEditDoneIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.done, 0, 0, 0);
                    editPledgeName.setSelection(editPledgeName.getText().length());
                    editPledgeName.selectAll();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editPledgeName, InputMethodManager.SHOW_IMPLICIT);
                    Log.i(TAG, CLASSTAG + "keyboard open");
                }
                else {
                    editPledgeName.setInputType(0);
                    int newAmount = Integer.valueOf(editPledgeName.getText().toString());
                    editPledgeName.setText(String.valueOf(newAmount));
                    mEditDoneIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    FirebaseDatabaseInterface.updatePledgeAmount(newAmount);
                    Log.i(TAG, CLASSTAG + "keyboard close");
                }
            }
        });

    }

    private void setSpinnerListener() {
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newLocation = locationList.get(position);
                FirebaseDatabaseInterface.updatePledgeLocation(newLocation);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private String[] findStringArrayRes(String resName) {
        int resId = getContext().getResources().getIdentifier(resName,
                "array", getContext().getPackageName());
        return getContext().getResources().getStringArray(resId);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("Withdraw my pledge");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle() == "Withdraw my pledge" ){
            setEditTextView(0);
            FirebaseDatabaseInterface.updatePledgeAmount(0);
        } else super.onOptionsItemSelected(item);
        return true;
    }


}
