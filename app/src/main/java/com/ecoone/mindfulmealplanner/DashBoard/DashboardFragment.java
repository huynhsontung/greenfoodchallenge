package com.ecoone.mindfulmealplanner.DashBoard;

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
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.Tool.Calculator;
import com.ecoone.mindfulmealplanner.DashBoard.Improve.ImproveActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.DB.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.DB.Plan;
import com.ecoone.mindfulmealplanner.DB.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;

import java.text.DecimalFormat;

public class DashboardFragment extends Fragment {


    private String mCurrentPlanName;

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private Button improveButton;
    private TextView mEditDoneIcon;
    private TextView currentPlanTextView; // just for setEditTextView()
    private TextView currentCo2eTextView;
    private TextView relevantInfo;
    private EditText editPlanName;
    private Button logout;

    private FirebaseFunctions mFunctions;

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

        improveButton = view.findViewById(R.id.fragment_dashboard_improve);
        editPlanName = view.findViewById(R.id.fragment_dashboard_edit_plan_name);
        mEditDoneIcon = view.findViewById(R.id.fragment_dashboard_icon_edit_done);
        currentPlanTextView = view.findViewById(R.id.fragment_dashboard_currentplan_text_view); // just for setEditTextView()
        currentCo2eTextView = view.findViewById(R.id.CurrentCo2eView);
        relevantInfo = view.findViewById(R.id.relevantInfo);
        improveButton = view.findViewById(R.id.fragment_dashboard_improve);
        logout = view.findViewById(R.id.logout);

        mFunctions = FirebaseFunctions.getInstance();


        setFirebaseValueListener();
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

    private void setFirebaseValueListener() {
        mDatabase.child(userUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.child("userInfo").getValue(User.class);
                mCurrentPlanName = user.currentPlanName;
                setEditTextView(mCurrentPlanName);
                Plan mCurrentPlan = dataSnapshot.child("planInfo").child(mCurrentPlanName).getValue(Plan.class);
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
                    FirebaseDatabaseInterface.updateCurrentPlanNameAndPlan(mCurrentPlan, mCurrentPlanName, newPlanName);
                }
            }
        });

    }

    private void setupPieChartFragmentPager(Plan plan) {
        final float[] co2Amount = Calculator.calculateCO2eEachFood(plan);
        final float[] foodAmount = FirebaseDatabaseInterface.getPlanArray(plan);

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

    //    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        setupPieChartFragmentPager();
//        calculateCurrentCo2e();
//        setEditTextView();
//    }


}
