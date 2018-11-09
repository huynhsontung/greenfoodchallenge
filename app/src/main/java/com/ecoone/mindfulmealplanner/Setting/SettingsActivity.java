package com.ecoone.mindfulmealplanner.Setting;

import android.os.Bundle;
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

import com.ecoone.mindfulmealplanner.MainActivity;
import com.ecoone.mindfulmealplanner.R;

import com.ecoone.mindfulmealplanner.R;

public class SettingsActivity extends AppCompatActivity implements LogoutDialogPreference.OnInputListener {
    private static final String LOGOUT_ACTION = "com.ecoone.mindfulmealplanner.logoutaction";
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setupToolbar();
        getFragmentManager().beginTransaction().replace(R.id.screen_area, new SettingsFragment()).commit();
    }

    private void setupToolbar() {
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Settings");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        return true;
    }

    public static int logoutAction(Intent result) {
        return result.getIntExtra(LOGOUT_ACTION, 0);
    }

    // input: 0 logout, 1 logout with cleaning data
    @Override
    public void sendInput(int input) {

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

}

