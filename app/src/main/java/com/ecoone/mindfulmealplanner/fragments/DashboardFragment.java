package com.ecoone.mindfulmealplanner.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.Calculator;
import com.ecoone.mindfulmealplanner.ImproveActivity;
import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.DbInterface;
import com.ecoone.mindfulmealplanner.db.Plan;

import java.text.DecimalFormat;
import java.util.Random;

public class DashboardFragment extends Fragment {

    private String mUsername;
    private String mGender;
    private String mCurrentPlanName;
    private String[] foodName;
    private float[] foodAmount;
    private int foodLen;

    private AppDatabase mDb;

    private Button improveButton;
    private TextView mEditDoneIcon;
    private TextView currentPlanTextView; // just for setEditTextView()
    private TextView currentCo2eTextView;
    private TextView relevantInfo;
    private EditText editPlanName;

    private ViewPager mChartPager;
    private PagerAdapter mChartPagerAdapter;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(DashboardFragment)";

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
        DbInterface.setDb(mDb);

        mUsername = getArguments().getString(MainActivity.EXTRA_USERNAME);
        mGender = DbInterface.getGender(mUsername);
        foodName = findStringArrayRes("food_name");
        foodLen = foodName.length;

        improveButton = view.findViewById(R.id.fragment_dashboard_improve);
        editPlanName = view.findViewById(R.id.fragment_dashboard_edit_plan_name);
        mEditDoneIcon = view.findViewById(R.id.fragment_dashboard_icon_edit_done);
        currentPlanTextView = view.findViewById(R.id.fragment_dashboard_currentplan_text_view); // just for setEditTextView()
        currentCo2eTextView = view.findViewById(R.id.CurrentCo2eView);
        relevantInfo = view.findViewById(R.id.relevantInfo);
        improveButton = view.findViewById(R.id.fragment_dashboard_improve);

        setUserInfo();
        setEditTextView();
        setEditDoneIconAction(view);
        setupImproveButton();
        setupPieChartFragmentPager();
        calculateCurrentCo2e();

    }

    private void setUserInfo() {
        mCurrentPlanName = DbInterface.getCurrentPlanName(mUsername);
        Plan plan = DbInterface.getCurrentPlan(mUsername);
        foodAmount = DbInterface.getPlanArray(plan);
    }

    private void calculateCurrentCo2e() {
        float sumCo2ePerYear = Calculator.calculateCO2ePerYear(mDb.planDao().getPlan(mUsername, mCurrentPlanName));
        String message = getString(R.string.current_co2e, new DecimalFormat("###.###").format(sumCo2ePerYear));
        currentCo2eTextView.setText(message);
        float kmWasted = Calculator.calculateSavingsInKm(sumCo2ePerYear);
        if(kmWasted > 100){
            relevantInfo.setText(getString(R.string.km_wasted,new DecimalFormat("###,###,###.#").format(kmWasted)));
            relevantInfo.setVisibility(View.VISIBLE);
        }

        if (sumCo2ePerYear > 1.7)
            currentCo2eTextView.setTextColor(Color.RED);
        else {
            currentCo2eTextView.setTextColor(Color.BLUE);
        }
    }

    private void setEditTextView() {
        mCurrentPlanName = DbInterface.getCurrentPlanName(mUsername);
        editPlanName.setText(mCurrentPlanName);
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
                    mCurrentPlanName = editPlanName.getText().toString();
                    editPlanName.setText(mCurrentPlanName);
                    mEditDoneIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    DbInterface.changeCurrentPlanName(mUsername, mCurrentPlanName);
                }
            }
        });
    }


    private void setupPieChartFragmentPager() {
        final float[] co2Amount = Calculator.calculateCO2eEachFood(mDb.planDao().getPlan(mUsername,mCurrentPlanName));

        mChartPager = getView().findViewById(R.id.fragment_dashboard_chart_pager);
        mChartPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if (i == 0)
                    return DashboardChartFragment.newInstance(i,foodAmount);
                else
                    return DashboardChartFragment.newInstance(i,co2Amount);
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
        mChartPager.setAdapter(mChartPagerAdapter);
        mChartPager.setCurrentItem(0);
    }

    private void setupImproveButton(){
        //Wire up the button to improve the plan
        //...get the button
        improveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ImproveActivity.newIntent(getContext(), mUsername);
                startActivityForResult(intent,0);
            }
        });
    }


    private String[] findStringArrayRes(String resName) {
        int resId = getResources().getIdentifier(resName,
                "array", getActivity().getPackageName());
        return getResources().getStringArray(resId);
    }

    private static int randInt(int min, int max) {
        Random rand = new Random();
        return rand.nextInt(max- min + 1) + min;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setupPieChartFragmentPager();
        calculateCurrentCo2e();
        setEditTextView();
    }


}
