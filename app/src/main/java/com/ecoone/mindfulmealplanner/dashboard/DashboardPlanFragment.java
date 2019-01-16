package com.ecoone.mindfulmealplanner.dashboard;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.pledge.PledgeLogic;
import com.ecoone.mindfulmealplanner.tools.Calculator;
import com.ecoone.mindfulmealplanner.dashboard.improve.ImproveActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.dashboard.improve.ImproveActivity;
import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.database.Plan;
import com.ecoone.mindfulmealplanner.database.User;
import com.ecoone.mindfulmealplanner.pledge.PledgeLogic;
import com.ecoone.mindfulmealplanner.tools.Calculator;
import com.elconfidencial.bubbleshowcase.BubbleShowCase;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;
import com.elconfidencial.bubbleshowcase.BubbleShowCaseSequence;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;

import java.text.DecimalFormat;
import java.util.Objects;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.listener.DismissListener;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class DashboardPlanFragment extends Fragment {


    private String mCurrentPlanName;
    private Plan mCurrentPlan;

    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private Button improveButton;
    private TextView mEditDoneIcon;
    private TextView currentPlanTextView; // just for setEditTextView()
    private TextView currentCo2eTextView;
    private TextView relevantInfo;
    private EditText editPlanName;
    private ImageView rightArrow;
    private FirebaseFunctions mFunctions;
    private ValueEventListener mValueEventListener;
    private DashboardViewModel mViewModel;
    private ViewPager mChartPager;
    private PagerAdapter mChartPagerAdapter;
    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(DashboardPlanFragment)";
    SharedPreferences mSharedPreferences;
    SharedPreferences.Editor editor;
    private static final String SKIP_TUTORIAL_DASHBOARD_PLAN = "dashboardplan";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = ViewModelProviders.of(Objects.requireNonNull(getParentFragment())).get(DashboardViewModel.class);

        return inflater.inflate(R.layout.fragment_dashboard_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // write your code here
        improveButton = view.findViewById(R.id.fragment_dashboard_improve);
        editPlanName = view.findViewById(R.id.fragment_dashboard_edit_plan_name);
        mEditDoneIcon = view.findViewById(R.id.fragment_dashboard_icon_edit_done);
        currentPlanTextView = view.findViewById(R.id.fragment_dashboard_currentplan_text_view); // just for setEditTextView()
        currentCo2eTextView = view.findViewById(R.id.CurrentCo2eView);
        relevantInfo = view.findViewById(R.id.relevantInfo);
        improveButton = view.findViewById(R.id.fragment_dashboard_improve);

        mFunctions = FirebaseFunctions.getInstance();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        editor = mSharedPreferences.edit();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setFirebaseValueListener();
        setOnPlanSelectListener();
        setupImproveButton();
        setEditDoneIconAction(view);
    }

    private void setOnPlanSelectListener() {
        mViewModel.getCurrentPlan().observe(this, planName -> {
            if(planName!=null){
                FirebaseDatabaseInterface.updateCurrentPlanName(planName);
            }
        });
    }

    private void setFirebaseValueListener() {
        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, CLASSTAG + "firebase listener call");
                User user = dataSnapshot.child("userInfo").getValue(User.class);

                if(user != null) {
                    mCurrentPlanName = user.currentPlanName;
                    mCurrentPlan = dataSnapshot.child("planInfo").child(mCurrentPlanName).getValue(Plan.class);
                    setEditTextView(mCurrentPlanName);
                    if (mCurrentPlan != null) {
                        calculateCurrentCo2e(mCurrentPlan);
                        setupPieChartFragmentPager(mCurrentPlan);
                        PledgeLogic.updateCurrentPlan(mCurrentPlan);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabase.child(FirebaseDatabaseInterface.ALLUSERSUID_NODE)
                .child(userUid).addValueEventListener(mValueEventListener);

    }

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

    private void setEditDoneIconAction(final View view) {

        mEditDoneIcon.setOnClickListener(v -> {
            Log.i(TAG, CLASSTAG + "inpute type: " + editPlanName.getInputType());
            if (editPlanName.getInputType() == 0) {
                editPlanName.requestFocus();
                editPlanName.setInputType(1);
                mEditDoneIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_done, 0, 0, 0);
                editPlanName.setSelection(editPlanName.getText().length());
                editPlanName.selectAll();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean test = imm.showSoftInput(editPlanName, InputMethodManager.SHOW_IMPLICIT);
                Log.i(TAG, CLASSTAG + "showsoftInput return: " + test);
                Log.i(TAG, CLASSTAG + "keyboard open");
            }
            else {
                String newPlanName = editPlanName.getText().toString();
                if(!newPlanName.isEmpty()) {
                    while(newPlanName.startsWith(" ")){
                        if(newPlanName.length()>1) {
                            newPlanName = newPlanName.substring(1);
                        } else {
                            Toast.makeText(getContext(),"New name cannot be all space",Toast.LENGTH_LONG).show();
                            return;
                        }
                    }
                    FirebaseDatabaseInterface.updateCurrentPlanNameAndPlan(mCurrentPlan, mCurrentPlanName, newPlanName);
                } else {
                    Toast.makeText(getContext(),"New name cannot be empty",Toast.LENGTH_LONG).show();
                    return;
                }

                editPlanName.setInputType(0);
                editPlanName.setText(newPlanName);
                mEditDoneIcon.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_edit, 0, 0, 0);
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                boolean test = imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                Log.i(TAG, CLASSTAG + "hidesoftInput return: " + test);
                Log.i(TAG, CLASSTAG + "keyboard close");

            }
        });

    }

    private void setupPieChartFragmentPager(Plan plan) {
        final float[] co2Amount = Calculator.calculateCO2eEachFood(plan);
        final float[] foodAmount = FirebaseDatabaseInterface.getPlanArray(plan);

        mChartPager = getView().findViewById(R.id.fragment_dashboard_chart_pager);
        mChartPagerAdapter = new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                if (i == 0) {
                    return DashboardChartFragment.newInstance(i, foodAmount);
                }
                else {
                    return DashboardChartFragment.newInstance(i, co2Amount);
                }
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public int getItemPosition(@NonNull Object object) {
                return POSITION_NONE;
            }
        };
        mChartPager.setAdapter(mChartPagerAdapter);
        mChartPager.setCurrentItem(0);
        mChartPager.setClipToPadding(false);
        mChartPager.setPadding(100,20,100,20);
        mChartPager.setPageMargin(24);
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
        setFirebaseValueListener();
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
        mDatabase.child(FirebaseDatabaseInterface.ALLUSERSUID_NODE)
                .child(userUid).removeEventListener(mValueEventListener);
        Log.i(TAG, CLASSTAG + "firebase listener remove");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, CLASSTAG + " onDestroy");

    }

}
