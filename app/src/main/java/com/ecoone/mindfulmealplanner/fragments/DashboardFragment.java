package com.ecoone.mindfulmealplanner.fragments;

import android.app.ProgressDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.Calculator;
import com.ecoone.mindfulmealplanner.DashboardViewModel;
import com.ecoone.mindfulmealplanner.DbInterface;
import com.ecoone.mindfulmealplanner.ImproveActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.db.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.db.Plan;
import com.ecoone.mindfulmealplanner.db.User;
import com.ecoone.mindfulmealplanner.db.mCallback;
import com.ecoone.mindfulmealplanner.db.onGetDataListener;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class DashboardFragment extends Fragment {


    private String mCurrentPlanName;
    private String[] foodName;

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private Task<Void> allTask;

    private Button improveButton;
    private TextView mEditDoneIcon;
    private TextView currentPlanTextView; // just for setEditTextView()
    private TextView currentCo2eTextView;
    private TextView relevantInfo;
    private EditText editPlanName;
    private Button logout;

    private FirebaseFunctions mFunctions;

    private Integer test;

    private ViewPager mChartPager;
    private PagerAdapter mChartPagerAdapter;
    private DashboardViewModel mViewModel;
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
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // write your code here
        mViewModel = ViewModelProviders.of(this).get(DashboardViewModel.class);
        foodName = findStringArrayRes("food_name");

        improveButton = view.findViewById(R.id.fragment_dashboard_improve);
        editPlanName = view.findViewById(R.id.fragment_dashboard_edit_plan_name);
        mEditDoneIcon = view.findViewById(R.id.fragment_dashboard_icon_edit_done);
        currentPlanTextView = view.findViewById(R.id.fragment_dashboard_currentplan_text_view); // just for setEditTextView()
        currentCo2eTextView = view.findViewById(R.id.CurrentCo2eView);
        relevantInfo = view.findViewById(R.id.relevantInfo);
        improveButton = view.findViewById(R.id.fragment_dashboard_improve);
        logout = view.findViewById(R.id.logout);

        mFunctions = FirebaseFunctions.getInstance();

        mDatabase.child("users").child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                mCurrentPlanName = user.currentPlanName;
                setEditTextView(mCurrentPlanName);
                Plan mCurrentPlan = dataSnapshot.child("plans").child(mCurrentPlanName).getValue(Plan.class);
                if(mCurrentPlan != null) {
                    mViewModel.mCurrentPlan.setValue(mCurrentPlan);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mViewModel.mCurrentPlan.observe(this, new Observer<Plan>() {
            @Override
            public void onChanged(Plan plan) {
                calculateCurrentCo2e(plan);
                setupPieChartFragmentPager(plan);

            }
        });

        setEditDoneIconAction(view);
        setupImproveButton();

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
            }
        });


//        getSumPledge()
//                .addOnCompleteListener(new OnCompleteListener<Integer>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Integer> task) {
//                        test = task.getResult();
//                        Log.i(TAG, "cloud func: " +test);
//                    }
//                });
//
//        Log.i(TAG, "cloud func: " +test);

    }

//    private Task<Integer> getSumPledge() {
//        return mFunctions
//                .getHttpsCallable("getSumPledge")
//                .call()
//                .continueWith(new Continuation<HttpsCallableResult, Integer>() {
//                    @Override
//                    public Integer then(@NonNull Task<HttpsCallableResult> task) throws Exception {
//                        Integer result = (Integer) task.getResult().getData();
//                        return result;
//                    }
//                });
//    }

    private void calculateCurrentCo2e(Plan plan) {
        float sumCo2ePerYear = Calculator.calculateCO2ePerYear(plan);
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

    private void setEditTextView(String currentPlanName) {
        editPlanName.setText(currentPlanName);
        editPlanName.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentPlanTextView.getTextSize());
        editPlanName.setTypeface(currentPlanTextView.getTypeface());
//        editPlanName.setTextColor(currentPlanTextView.getTextColors()); // grey(uncomment) or black(comment)
        editPlanName.setInputType(0);
    }

    private void setEditDoneIconAction(final View view ) {
        mEditDoneIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Plan mCurrentPlan = mViewModel.mCurrentPlan.getValue();
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
                    editPlanName.setText(newPlanName);
                    mEditDoneIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.edit, 0, 0, 0);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    FirebaseDatabaseInterface.updateCurrentPlanName(mCurrentPlan, mCurrentPlanName, newPlanName);
                }
            }
        });

    }

    private void setupPieChartFragmentPager(Plan plan) {
        final float[] co2Amount = Calculator.calculateCO2eEachFood(plan);
        final float[] foodAmount = DbInterface.getPlanArray(plan);

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
        //Wire up the button to improve the plan3
        //...get the button
        improveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ImproveActivity.newIntent(getContext());
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


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        setupPieChartFragmentPager();
//        calculateCurrentCo2e();
//        setEditTextView();
//    }


}
