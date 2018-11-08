package com.ecoone.mindfulmealplanner.Setting;

import android.content.Context;
import android.content.Intent;
import android.preference.DialogPreference;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ecoone.mindfulmealplanner.InitialSetup.InitialSetupActivity;
import com.ecoone.mindfulmealplanner.R;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LogoutDialogPreference extends DialogPreference {

    private boolean isCheckboxChecked = false;

    public LogoutDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setDialogLayoutResource(R.layout.preference_logout_dialog);
        setDialogTitle("Do you want to logout?");
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        CheckBox checkBox = view.findViewById(R.id.logout_dialog_checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckboxChecked = isChecked;
            }
        });
    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        // When the user selects "OK", persist the new value
        if (positiveResult) {
            if (isCheckboxChecked) {

            }
            else {
                AuthUI.getInstance().signOut(getContext())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(getContext(), InitialSetupActivity.class);
                                getContext().startActivity(intent);
                            }});
            }
        }
    }

}
