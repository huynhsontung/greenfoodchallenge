package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class LogoutDialogPreference extends DialogPreference {
    public LogoutDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        setDialogTitle("Logout?");
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            // User selected OK
        }
        else {
            // User selected Cancel
        }
    }
}
