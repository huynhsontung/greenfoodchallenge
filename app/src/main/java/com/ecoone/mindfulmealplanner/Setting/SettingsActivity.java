package com.ecoone.mindfulmealplanner.Setting;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import com.ecoone.mindfulmealplanner.Setting.LogoutDialogPreference.OnInputListener;
import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.R;

import com.ecoone.mindfulmealplanner.R;

public class SettingsActivity extends PreferenceActivity implements OnInputListener{

    Toolbar mToolbar;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(SettingsActivity)";
    private static final String LOGOUT_ACTION = "com.ecoone.mindfulmealplanner.logoutaction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_settings);
//        setupToolbar();
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment()).commit();
    }

//    private void setupToolbar() {
//        mToolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(mToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        setTitle("Settings");
//    }

//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
//        startActivity(intent);
//        return true;
//    }

    public static class SettingsFragment extends PreferenceFragment{
        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preference);
        }
    }

    public static int logoutAction(Intent result) {
        return result.getIntExtra(LOGOUT_ACTION, 0);
    }

    // input: 0 logout, 1 logout with cleaning data
    @Override
    public void sendInput(int input) {
        Log.i(TAG, "sendInput: got the input: " + input + CLASSTAG);
        Intent data = new Intent();
        data.putExtra(LOGOUT_ACTION, input);
        setResult(RESULT_OK, data);
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
//        finish();
        Log.d(TAG, CLASSTAG + " onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, CLASSTAG + " onDestroy");
    }



}

