package com.ecoone.mindfulmealplanner.InitialSetup;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.DB.Pledge;
import com.ecoone.mindfulmealplanner.Tool.ChartValueFormatter;
import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.DB.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.DB.Plan;
import com.firebase.ui.auth.AuthUI;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;


public class InitialSetupActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final int NUMBER_OF_PAGES = 3;

    private static final String CLASSTAG = "(InitialSetupActivity)";
    private static final String TAG = "testActivity";

    private InitialSetupViewModel mViewModel;
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setup);
        mViewModel = ViewModelProviders.of(this).get(InitialSetupViewModel.class);
        checkIfGoToDashboard();

        observeFinish();

    }

    private void observeFinish() {
        final android.arch.lifecycle.Observer<Boolean> checkerObserver = new android.arch.lifecycle.Observer<Boolean>() {
            @Override
            public void onChanged(Boolean checker) {

                Log.i(TAG, CLASSTAG + "View Model data changed");
                if(!checker){return;}

                if (mDatabase == null) {
                    com.google.firebase.database.FirebaseDatabase database = com.google.firebase.database.FirebaseDatabase.getInstance();
                    database.setPersistenceEnabled(true);
                    mDatabase = database.getReference();
                }


                Log.i(TAG, CLASSTAG + "check view model local user" + mViewModel.localUser);
                Log.i(TAG, CLASSTAG + "check view model local user" + mViewModel.localPlan);

                mViewModel.localUser.iconName = "android";
                FirebaseDatabaseInterface.writeUser(mViewModel.localUser);
                FirebaseDatabaseInterface.writePlan(mViewModel.localPlan);

                Pledge pledge = new Pledge();
                pledge.amount = 0;
                pledge.location = "Vancouver";
                FirebaseDatabaseInterface.writePledge(pledge);

                startActivityAndFinish();
            }
        };
        mViewModel.getChecker().observe(this,checkerObserver);
    }

    private void checkIfGoToDashboard() {
        user = FirebaseAuth.getInstance().getCurrentUser();

        // current user not exist(in firebase local)
        if(user == null){
            // go to sign in page
            goSignInPage();
        }
        // current user exist
        else {
            Log.i(TAG, "current user exist in local firebase, UID: " + user.getUid());
            checkIfUserDataExist();
        }
    }

    // firebase will tave over. When the activity in this functinon finished, onActivityResult
    // function (Override) will be called
    private void goSignInPage() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.EmailBuilder().build(),
                                new AuthUI.IdpConfig.GoogleBuilder().build()))
                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, CLASSTAG + "resultCode: " + resultCode);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                checkIfUserDataExist();
            }
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
    }

    // check if user data exist in firebase database
    private void checkIfUserDataExist() {
        String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i(TAG, "check user data, UID: " + userUid);
        mDatabase.child(FirebaseDatabaseInterface.ALLUSERSUID_NODE)
                .child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.i(TAG, CLASSTAG + "exist");
                    startActivityAndFinish();
                }
                else {
                    Log.i(TAG, CLASSTAG + "not exist");

                    user = FirebaseAuth.getInstance().getCurrentUser();

                    String a = user.getDisplayName();
                    String b = user.getEmail();
                    String c = user.getUid();

                    Log.i(TAG, CLASSTAG + " check user email, name, uid: " + a + " " + b + " " + c);
                    user = FirebaseAuth.getInstance().getCurrentUser();
                    mViewModel.localUser.displayName = user.getDisplayName();
                    mViewModel.localUser.email = user.getEmail();
                    mViewModel.getDisplayName().setValue(user.getDisplayName());
                    setupViewPager();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupViewPager() {
        ViewPager mViewPager = findViewById(R.id.initial_setup_view_pager);
        PagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i){
                    case 0: return GreetingFragment.newInstance();
                    case 1: return AskGenderFragment.newInstance();
                    default: return PlanSetterFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return NUMBER_OF_PAGES;
            }
        };
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(0);
    }

    // Fragments declarations
    public static class GreetingFragment extends Fragment {
        public static GreetingFragment newInstance() {
            return new GreetingFragment();
        }

        private static final String CLASSTAG = "(GreetingFragment)";
        private static final String TAG = "testActivity";

        InitialSetupViewModel mViewModel;
        TextView greetingText;
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(InitialSetupViewModel.class);
            View view = inflater.inflate(R.layout.fragment_initial_setup_greeting, container, false);
            greetingText = view.findViewById(R.id.greeting_text);
            greetingText.setText("Greeting!");
            mViewModel.getDisplayName().observe(this, new Observer<String>() {
                @Override
                public void onChanged(String newDisplayName) {
                    mViewModel.localUser.displayName = newDisplayName;
                    greetingText.setText(getString(R.string.greeting_user,newDisplayName));
                }
            });
            return view;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
        }

    }

    public static class AskGenderFragment extends Fragment implements AdapterView.OnItemSelectedListener {
        InitialSetupViewModel mViewModel;
        private static final String CLASSTAG = "( AskGenderFragment)";
        private static final String TAG = "testActivity";
        public static AskGenderFragment newInstance() {
            return new AskGenderFragment();
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_initial_setup_ask_gender, container, false);
            mViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(InitialSetupViewModel.class);
            Spinner spinner = view.findViewById(R.id.ask_gender_spinner);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getContext()),R.array.gender_array,android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(0);
            spinner.setOnItemSelectedListener(this);
            return view;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mViewModel.localUser.gender = (String) parent.getItemAtPosition(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public static class PlanSetterFragment extends Fragment{

        private static final String CLASSTAG = "(PlanSetterFragment)";
        private static final String TAG = "testActivity";

        public static PlanSetterFragment newInstance(){
            return new PlanSetterFragment();
        }

        InitialSetupViewModel mViewModel;

        private Button finishButton;
        private PieChart mPieChart;
        private String[] foodName;
        private float[] foodAmount;
        private int foodLen;
        private ConstraintLayout[] mFoodSeekBarView;
        private TextView[] mFoodSeekBarTextView;
        private SeekBar[] mFoodSeekBarAction;
        private TextView[] mFoodSeekBarValueView;
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_initial_setup_set_plan, container, false);
            mViewModel = ViewModelProviders.of(getActivity()).get(InitialSetupViewModel.class);
            mPieChart = view.findViewById(R.id.initial_screen_pie_chart);
            initializeSeekBarView(view);
            setPieChartView(foodAmount);
            finishButton = view.findViewById(R.id.initial_screen_finish_button);
            setupFinishButton();
            return view;
        }

        private void setupFinishButton() {
            finishButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Plan plan = new Plan();
                    plan.planName = "Plan1";
                    plan.beef = foodAmount[0];
                    plan.pork = foodAmount[1];
                    plan.chicken = foodAmount[2];
                    plan.fish = foodAmount[3];
                    plan.eggs = foodAmount[4];
                    plan.beans = foodAmount[5];
                    plan.vegetables = foodAmount[6];
                    mViewModel.localUser.currentPlanName = plan.planName;
                    mViewModel.localPlan = plan;
                    mViewModel.getChecker().setValue(true);
                }
            });
        }

        private void initializeSeekBarView(View view) {
            foodName = findStringArrayRes("food_name");
            foodLen = foodName.length;
            foodAmount = new float[foodLen];
            mFoodSeekBarView = new ConstraintLayout[foodLen];
            mFoodSeekBarTextView = new TextView[foodLen];
            mFoodSeekBarAction = new SeekBar[foodLen];
            mFoodSeekBarValueView = new TextView[foodLen];

            mFoodSeekBarView[0] = view.findViewById(R.id.initial_screen_seekbar_component_1);
            mFoodSeekBarView[1] = view.findViewById(R.id.initial_screen_seekbar_component_2);
            mFoodSeekBarView[2] = view.findViewById(R.id.initial_screen_seekbar_component_3);
            mFoodSeekBarView[3] = view.findViewById(R.id.initial_screen_seekbar_component_4);
            mFoodSeekBarView[4] = view.findViewById(R.id.initial_screen_seekbar_component_5);
            mFoodSeekBarView[5] = view.findViewById(R.id.initial_screen_seekbar_component_6);
            mFoodSeekBarView[6] = view.findViewById(R.id.initial_screen_seekbar_component_7);

            for (int i = 0; i < foodLen; i++) {
                mFoodSeekBarTextView[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_text);
                mFoodSeekBarAction[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_action);
                mFoodSeekBarValueView[i] = mFoodSeekBarView[i].findViewById(R.id.seekbar_value);
                mFoodSeekBarTextView[i].setText(foodName[i]);
                mFoodSeekBarAction[i].setProgress(randInt(0, mFoodSeekBarAction[i].getMax()));
                int amount = mFoodSeekBarAction[i].getProgress();
                mFoodSeekBarValueView[i].setText(getString(R.string.amount_gram,amount));
                foodAmount[i] = amount;
            }
            setSeekBarChangeListener();
        }

        private void setSeekBarChangeListener() {
            for (int i = 0; i < foodLen; i++) {
                final int finalI = i;
                mFoodSeekBarAction[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        mFoodSeekBarValueView[finalI].setText(getString(R.string.amount_gram,progress));
                        foodAmount[finalI] = progress;
                        setPieChartView(foodAmount);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
            }
        }

        private void setPieChartView(float[] data) {
            List<PieEntry> entries = new ArrayList<>();
            for (int i = 0; i < foodLen; i++) {
                // filter out 0 values
                if(data[i] > 0)
                    entries.add(new PieEntry(data[i], foodName[i]));
            }
            PieDataSet pieDataSet = new PieDataSet(entries, null);
            pieDataSet.setValueFormatter(new ChartValueFormatter());
            pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
            PieData piedata = new PieData(pieDataSet);
            Legend legend = mPieChart.getLegend();
            legend.setEnabled(false);
            mPieChart.getDescription().setEnabled(false);
            mPieChart.setData(piedata);
            mPieChart.setUsePercentValues(true);
            mPieChart.invalidate();
        }

        private String[] findStringArrayRes(String resName) {
            int resId = getResources().getIdentifier(resName,
                    "array", Objects.requireNonNull(getActivity()).getPackageName());
            return getResources().getStringArray(resId);
        }

        private static int randInt(int min, int max) {
            Random rand = new Random();
            return rand.nextInt(max- min + 1) + min;
        }

    }

    private void startActivityAndFinish() {
        Intent intent = MainActivity.newIntent(InitialSetupActivity.this);
        startActivity(intent);
        finish();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.i(TAG,CLASSTAG + "onbackpressed");
        finish();
    }
}
