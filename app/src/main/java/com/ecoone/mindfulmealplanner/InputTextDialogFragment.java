package com.ecoone.mindfulmealplanner;


import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;



public class InputTextDialogFragment extends DialogFragment {

    private static final String EXTRA_TITLE =
            "com.ecoone.mindfulmealplanner.inputtextdialogframent.title";

    public InputTextDialogFragment() {

    }

    public static InputTextDialogFragment newInstance(String title) {

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Enter a New Name");

        return builder.create();
    }
}

