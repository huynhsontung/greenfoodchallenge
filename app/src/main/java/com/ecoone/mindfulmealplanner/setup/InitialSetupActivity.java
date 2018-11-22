package com.ecoone.mindfulmealplanner.setup;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.database.Pledge;
import com.ecoone.mindfulmealplanner.R;
import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.database.Plan;
import com.ecoone.mindfulmealplanner.tools.NonSwipeableViewPager;
import com.ecoone.mindfulmealplanner.tutorial.TutorialActivity;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;


// **** AS OF NOW, TUTORIAL PACKAGE IS NOT USED IN THE APP ****

public class InitialSetupActivity extends AppCompatActivity implements Button.OnClickListener{

    private static final int RC_SIGN_IN = 123;
    private static final int NUMBER_OF_PAGES = 4;

    private static final String CLASSTAG = "(InitialSetupActivity)";
    private static final String TAG = "testActivity";
    private static final int flag = 1;
    private NonSwipeableViewPager mViewPager;
    private InitialSetupViewModel mViewModel;
    private SharedPreferences mShared;
    SharedPreferences.Editor editor;

    DatabaseReference mDatabase = FirebaseDatabaseInterface.getDatabaseInstance();
    FirebaseUser user;

    private static final String SKIP_TUTORIAL = "key";
    private static final String SKIP_MAIN_ACTIVITY_TUTORIAL = "mainactivity";
    private static final String SKIP_TUTORIAL_PLAN_LIST = "planlist";
    private static final String SKIP_IMPROVE_ACTIVITY_TUTORIAL = "improve";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(InitialSetupViewModel.class);
        mShared = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = mShared.edit();
        checkIfGoToDashboard();
    }

    //If user doesn't exist, go to signup page
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
                    Intent intent = new Intent(InitialSetupActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
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
        setContentView(R.layout.activity_initial_setup);
        Button nextButton = findViewById(R.id.button_next);

        mViewPager = findViewById(R.id.initial_setup_view_pager);
        PagerAdapter pagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                switch (i){
                    case 0:
                        return GreetingFragment.newInstance();
                    case 1:
                        return AskGenderFragment.newInstance();
                    case 2:
                        return PresetPlansFragment.newInstance();
                    default:
                        return PlanSetterFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                return NUMBER_OF_PAGES;
            }
        };
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setCurrentItem(0);


        nextButton.setOnClickListener(this);
    }

        @Override
    public void onClick(View v) {
            Log.i(TAG, CLASSTAG + mViewPager.getCurrentItem());
            Log.i(TAG, CLASSTAG + "initial button clicked");
        int position = mViewPager.getCurrentItem();
        if(position == 3){
            Log.i(TAG, CLASSTAG + "inside");
            Plan plan = new Plan();
            plan.planName = "Plan1";
            plan.beef = mViewModel.foodAmount[0];
            plan.pork = mViewModel.foodAmount[1];
            plan.chicken = mViewModel.foodAmount[2];
            plan.fish = mViewModel.foodAmount[3];
            plan.eggs = mViewModel.foodAmount[4];
            plan.beans = mViewModel.foodAmount[5];
            plan.vegetables = mViewModel.foodAmount[6];
            mViewModel.localUser.currentPlanName = plan.planName;
            mViewModel.localPlan = plan;

            if (mDatabase == null) {
                mDatabase = FirebaseDatabaseInterface.getDatabaseInstance();
            }

            // checks for zero inputs
            if(greaterThanZero(plan)) {

            Log.i(TAG, CLASSTAG + "check view model local user" + mViewModel.localUser);
            Log.i(TAG, CLASSTAG + "check view model local user" + mViewModel.localPlan);

            mViewModel.localUser.iconName = "android";
            FirebaseDatabaseInterface.writeUser(mViewModel.localUser);
            FirebaseDatabaseInterface.writePlan(mViewModel.localPlan);

            Pledge pledge = new Pledge();
            pledge.amount = 0;
            pledge.location = "Vancouver";
            FirebaseDatabaseInterface.writePledge(pledge);

                // reset sharedpreferences to enable tutorials and enter main activity
                editor.putInt(SKIP_TUTORIAL,0);
                editor.putInt(SKIP_MAIN_ACTIVITY_TUTORIAL,0);
                editor.putInt(SKIP_IMPROVE_ACTIVITY_TUTORIAL,0);
                editor.putInt(SKIP_TUTORIAL_PLAN_LIST,0);
                editor.apply();
                startActivityAndFinish();
            }
            else {
                Toast.makeText(this, "Please enter your estimated meal plan",Toast.LENGTH_SHORT)
                    .show();
            }
        }
        else {
            mViewPager.setCurrentItem(position+1);
        }
    }

    public boolean greaterThanZero(Plan plan) {
        float totalFoodInGrams = plan.beef + plan.beans + plan.chicken + plan.pork
                + plan.eggs + plan.vegetables + plan.fish;
        if(totalFoodInGrams > 0) {
            return true;
        }
        else {
            return false;
        }
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
        Log.i(TAG,CLASSTAG + "onbackpressed");
        int position = mViewPager.getCurrentItem();
        if(position == 0){
            super.onBackPressed();
        } else {
            mViewPager.setCurrentItem(position-1);
        }
    }

    private void startActivityAndFinish() {
        Intent intent = new Intent(InitialSetupActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
