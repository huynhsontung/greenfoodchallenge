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
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.DB.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.DB.Pledge;
import com.ecoone.mindfulmealplanner.R;
import com.google.firebase.auth.FirebaseAuth;
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

    private TextView mEditDoneIcon;
    private EditText editPlesgeName;
    private Spinner mSpinner;
    private ArrayAdapter<CharSequence> mAdapter;
    private ArrayList<String> locationList;

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

        mEditDoneIcon = view.findViewById(R.id.my_pledge_icon_edit_done);
        editPlesgeName = view.findViewById(R.id.my_pledge_edit_plan_name);
        mSpinner = view.findViewById(R.id.my_pledge_spinner);
        mAdapter = ArrayAdapter.createFromResource(getContext(), R.array.location, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);

        locationList = new ArrayList<>(Arrays.asList(findStringArrayRes("location")));

        setEditTextView(0);

//        setFirebaseValueListener();

        setEditDoneIconAction(view);
        setSpinnerListener();
    }

    private void setFirebaseValueListener() {
        mDatabase.child("users").child(userUid).child("pledge").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Pledge pledge = dataSnapshot.getValue(Pledge.class);
                int amount = pledge.amount;
                String location = pledge.location;
                if (amount != 0 && location != null) {
                    setEditTextView(amount);
                    setSpinnerView(location);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setEditTextView(int amount) {
        editPlesgeName.setText(String.valueOf(amount));
        editPlesgeName.setInputType(0);
    }

    private void setSpinnerView(String location) {
        int position = locationList.indexOf(location);
        mSpinner.setSelection(position);
    }

    private void setEditDoneIconAction(final View view) {
        mEditDoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPlesgeName.getInputType() == 0) {
                    editPlesgeName.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                    mEditDoneIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.done, 0, 0, 0);
                    editPlesgeName.setSelection(editPlesgeName.getText().length());
                    editPlesgeName.selectAll();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editPlesgeName, InputMethodManager.SHOW_IMPLICIT);
                }
                else {
                    editPlesgeName.setInputType(0);
                    int newAmount = Integer.valueOf(editPlesgeName.getText().toString());
                    editPlesgeName.setText(String.valueOf(newAmount));
                    mEditDoneIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    FirebaseDatabaseInterface.updatePledgeAmount(newAmount);
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
}
