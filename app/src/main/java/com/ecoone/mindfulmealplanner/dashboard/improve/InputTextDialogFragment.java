package com.ecoone.mindfulmealplanner.dashboard.improve;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class InputTextDialogFragment extends DialogFragment{

    private EditText mEditText;
    private ArrayList<String> planNameList = new ArrayList<>();
    final DatabaseReference mDatabase = FirebaseDatabaseInterface.getDatabaseInstance();
    final String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

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

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        AlertDialog dialog;
        builder.setTitle("Enter a New Name");

        mDatabase.child("users").child(userUid).child("plans").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot mDataSnapshot: dataSnapshot.getChildren()) {
                    String planName = mDataSnapshot.getKey();
                    planNameList.add(planName);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String input = mEditText.getText().toString();
                if (input.equals("")) {
                    showCustomToast("the new plan name is empty!");
                }
                else if (planNameList.contains(input)) {
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

