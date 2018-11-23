package com.ecoone.mindfulmealplanner.profile.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.setup.InitialSetupActivity;
import com.ecoone.mindfulmealplanner.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class SettingsActivity extends AppCompatActivity implements LogoutDialogPreference.OnLogoutListener {

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(SettingsActivity)";
    private static final String LOGOUT_ACTION = "com.ecoone.mindfulmealplanner.logoutaction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.setting_screen_area, new SettingsFragment()).commit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public static int getLogoutAction(Intent result) {
        return result.getIntExtra(LOGOUT_ACTION, 0);
    }

    // input: 0 logout, 1 logout with cleaning data
    @Override
    public void onLogout(int input) {
        Log.i(TAG, CLASSTAG + "onLogout: got the input " + input);
        if (input == 1) {
            FirebaseDatabaseInterface.deleteUserData();
        }
        AuthUI.getInstance().signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent= new Intent(SettingsActivity.this, InitialSetupActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                });
    }

    public static class SettingsFragment extends PreferenceFragment{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
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

}


