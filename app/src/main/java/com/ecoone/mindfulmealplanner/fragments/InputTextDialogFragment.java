package com.ecoone.mindfulmealplanner.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.DbInterface;
import com.ecoone.mindfulmealplanner.ImproveActivity;
import com.ecoone.mindfulmealplanner.db.AppDatabase;

import java.util.List;

import javax.annotation.Nullable;


public class InputTextDialogFragment extends DialogFragment{

    private String mUsername;

    private EditText mEditText;

    private AppDatabase mDb;

    private static final String EXTRA_TITLE =
            "com.ecoone.mindfulmealplanner.inputtextdialogframent.title";
    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(DialogFrament)";

    public interface OnInputListener{
        void sendInput(String input);
    }

    public OnInputListener mOnInputListener;


    public InputTextDialogFragment() {

    }

    public static InputTextDialogFragment newInstance() {
        InputTextDialogFragment inputTextDialogFragment = new InputTextDialogFragment();
        return inputTextDialogFragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        mDb = AppDatabase.getDatabase(getContext());
        DbInterface.setDb(mDb);

        mUsername = getArguments().getString(ImproveActivity.EXTRA_USERNAME);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog;
        builder.setTitle("Enter a New Name");

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = mEditText.getText().toString();
                List<String> plansName = DbInterface.getAllPlansName(mUsername);
                Log.i(TAG, "Check if input plan name is in the database: " + plansName.contains(input) + CLASSTAG);
                if (input.equals("")) {
                    showCustomToast("the new plan name is empty!");
                }
                else if (plansName.contains(input)) {
                    showCustomToast("the new plan name is duplicated!");
                }
                else{
                    Log.i(TAG, "EditText:" + input + CLASSTAG);
                    mOnInputListener.sendInput(input);
                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        mEditText = new EditText(getContext());
        mEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        mEditText.setMaxLines(1);
        builder.setView(mEditText);
        dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        return dialog;
    }

    private void showCustomToast(String message) {
        Toast mToast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL,
                0, 0);
        mToast.show();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mOnInputListener = (OnInputListener) getActivity();
        }catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " +e.getMessage() + CLASSTAG);
        }
    }
}

