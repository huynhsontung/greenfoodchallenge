package com.ecoone.mindfulmealplanner.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.DbInterface;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.github.mikephil.charting.charts.PieChart;

public class PlanFragment extends Fragment {

    private String mUsername;
    private String mPlanName;
    private String[] foodName;
    private int[] foodAmount;

    private AppDatabase mDb;

    private Button setAsCurrentButton;
    private PieChart chart1;
    private PieChart chart2;
    private TextView mEditDoneIcon;
    private TextView currentPlanTextView; // just for setEditTextView()
    private TextView currentCo2eTextView;
    private EditText editPlanName;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(PlanFragment)";
    private static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.planFragment.username";
    private static final String EXTRA_PLANNAME =
            "com.ecoone.mindfulmealplanner.planFragment.planname";

    public static PlanFragment newInstance(String username, String planName) {
        Bundle args = new Bundle();
        args.putString(EXTRA_USERNAME, username);
        args.putString(EXTRA_PLANNAME, planName);
        PlanFragment fragment = new PlanFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_plan, null );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDb = AppDatabase.getDatabase(getContext());
        DbInterface.setDb(mDb);

        mUsername = getArguments().getString(EXTRA_USERNAME);
        mPlanName = getArguments().getString(EXTRA_PLANNAME);
        foodName = findStringArrayRes("food_name");

        setAsCurrentButton = view.findViewById(R.id.fragment_plan_set_as_current);
        chart1 = view.findViewById(R.id.frag_PieChart1);
        chart2 = view.findViewById(R.id.frag_PieChart2);
        editPlanName = view.findViewById(R.id.fragment_plan_edit_plan_name);
        mEditDoneIcon = view.findViewById(R.id.fragment_plan_icon_edit_done);
        currentPlanTextView = view.findViewById(R.id.fragment_dashboard_currentplan_text_view);
        currentCo2eTextView = view.findViewById(R.id.frag_CurrentCo2eView);

        setEditTextView();
        setEditDoneIconAction(view);
    }

    private void setEditTextView() {
        editPlanName.setText(mPlanName);
        editPlanName.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentPlanTextView.getTextSize());
        editPlanName.setTypeface(currentPlanTextView.getTypeface());
//        editPlanName.setTextColor(currentPlanTextView.getTextColors()); // grey(uncomment) or black(comment)
        editPlanName.setInputType(0);
    }

    private void setEditDoneIconAction(final View view) {
        mEditDoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editPlanName.getInputType() == 0) {
                    editPlanName.setInputType(1);
                    mEditDoneIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.done, 0, 0, 0);
                    editPlanName.setSelection(editPlanName.getText().length());
                    editPlanName.selectAll();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editPlanName, InputMethodManager.SHOW_IMPLICIT);
                }
                else {
                    editPlanName.setInputType(0);
                    String newPlanName = editPlanName.getText().toString();
                    DbInterface.changePlanName(mUsername, mPlanName, newPlanName);
                    mPlanName = newPlanName;
                    editPlanName.setText(mPlanName);
                    mEditDoneIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        });
    }

    private String[] findStringArrayRes(String resName) {
        int resId = getResources().getIdentifier(resName,
                "array", getActivity().getPackageName());
        return getResources().getStringArray(resId);
    }
}
