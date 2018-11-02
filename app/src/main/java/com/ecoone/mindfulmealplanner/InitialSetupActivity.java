package com.ecoone.mindfulmealplanner;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.db.AppDatabase;
import com.ecoone.mindfulmealplanner.db.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.db.Plan;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.ecoone.mindfulmealplanner.MainActivity.EXTRA_USERNAME;

public class InitialSetupActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private static final int NUMBER_OF_PAGES = 3;
    private static final String EXTRA_LOGIN_FLAG =
            "com.ecoone.mindfulmealplanner.initialscreenactivity.login_flag";

    private InitialSetupViewModel mViewModel;
    AppDatabase database;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_setup);
        database = AppDatabase.getDatabase(this);
        mViewModel = ViewModelProviders.of(this).get(InitialSetupViewModel.class);
        checkIfGoToDashboard();
        setupViewPager();
        observeFinish();
    }

    private void observeFinish() {
        final android.arch.lifecycle.Observer<Boolean> checkerObserver = new android.arch.lifecycle.Observer<Boolean>() {
            @Override
            public void onChanged(Boolean checker) {
                if(!checker){return;}
                database.userDao().addUser(mViewModel.localUser);
                database.planDao().addPlan(mViewModel.localPlan);
                user = FirebaseAuth.getInstance().getCurrentUser();
                FirebaseDatabaseInterface.writeUser(user.getUid(),mViewModel.localUser);
                FirebaseDatabaseInterface.writePlan(user.getUid(),mViewModel.localPlan);
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(EXTRA_LOGIN_FLAG, 1);
                editor.putString(EXTRA_USERNAME, mViewModel.localUser.username);
                editor.apply();
                startActivityAndFinish(mViewModel.localUser.username);
            }
        };
        mViewModel.getChecker().observe(this,checkerObserver);
    }

    private void checkIfGoToDashboard() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int loginFlag = preferences.getInt(EXTRA_LOGIN_FLAG, 0);
        // if no user login
        if (loginFlag != 0){
            String mUsername = preferences.getString(EXTRA_USERNAME, null);
            startActivityAndFinish(mUsername);
        } else {
            getUserId();
        }
    }



    private void startActivityAndFinish(String username) {
        Intent intent = MainActivity.newIntent(InitialSetupActivity.this, username);
        startActivity(intent);
        InitialSetupActivity.this.finish();
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

    private void getUserId() {
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
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                mViewModel.localUser.username = user.getEmail();
                mViewModel.localUser.displayName = user.getDisplayName();
                mViewModel.localUser.photoUrl = user.getPhotoUrl().toString();
                mViewModel.getDisplayName().setValue(user.getDisplayName());
            }
        }
    }


    // Fragments declarations
    public static class GreetingFragment extends Fragment {
        public static GreetingFragment newInstance() {
            return new GreetingFragment();
        }

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
                    plan.username = mViewModel.localUser.username;
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
                mFoodSeekBarAction[i].setProgress(50);
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
    }

}
