package com.ecoone.mindfulmealplanner.Setting;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;


import com.ecoone.mindfulmealplanner.InitialSetup.InitialSetupActivity;
import com.ecoone.mindfulmealplanner.R;
import com.google.firebase.auth.FirebaseAuth;

//...
public class LogoutActivity extends AppCompatActivity {
    public int checkstatus=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View checkBoxView = View.inflate(this, R.layout.activity_logout, null);
        CheckBox checkBox = checkBoxView.findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                Toast.makeText(getApplicationContext(), isChecked? "ON":"OFF",Toast.LENGTH_SHORT).show();
                if(isChecked){
                    checkstatus=1;
                }
                else{
                    checkstatus=2;
                }

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Logout");
        builder.setView(checkBoxView)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if(checkstatus == 1){
//                            mDatabase.child(ALLUSERSUID_NODE).child(userUid).removeValue();
                        }
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(LogoutActivity.this, InitialSetupActivity.class);
                        startActivity(intent);
                        }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        Intent intent = new Intent(LogoutActivity.this, SettingsActivity.class);
                        startActivity(intent);
                    }
                }).show();
    }


    public static Intent makeIntent(Context openLogout){
        return new Intent(openLogout, LogoutActivity.class);
    }
}
