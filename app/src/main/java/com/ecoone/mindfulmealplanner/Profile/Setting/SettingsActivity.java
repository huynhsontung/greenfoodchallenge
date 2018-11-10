package com.ecoone.mindfulmealplanner.Profile.Setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.ecoone.mindfulmealplanner.Profile.ProfileFragment;


import android.content.Intent;
import android.util.Log;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.R;

public class SettingsActivity extends FragmentActivity implements LogoutDialogPreference.OnDataPassingListener {

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(SettingsActivity)";
    private static final String LOGOUT_ACTION = "com.ecoone.mindfulmealplanner.logoutaction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction().replace(R.id.setting_screen_area, new SettingsFragment()).commit();



    }


//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return true;
//    }

    public static int getLogoutAction(Intent result) {
        return result.getIntExtra(LOGOUT_ACTION, 0);
    }

    // input: 0 logout, 1 logout with cleaning data
    @Override
    public void passDataFromLogoutDialogToSetting(int input) {
        Log.i(TAG, CLASSTAG + "passDataFromLogoutDialogToSetting: got the input " + input);
        Intent data = new Intent();
        data.putExtra(LOGOUT_ACTION, input);
        setResult(RESULT_OK, data);
        finish();
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


