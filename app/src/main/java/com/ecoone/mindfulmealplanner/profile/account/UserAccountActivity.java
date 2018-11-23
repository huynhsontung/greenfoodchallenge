package com.ecoone.mindfulmealplanner.profile.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.database.FirebaseDatabaseInterface;
import com.ecoone.mindfulmealplanner.R;

public class UserAccountActivity extends AppCompatActivity implements UserIconDialogFragment.OnDataPassingListener {

    private int userIconId;
    private String userDispalyName;
    private String userEmail;
    private String userGender;

    private LinearLayout usericonImageLayout;
    private LinearLayout userDisplayNameLayout;
    private LinearLayout userEmailLayout;
    private LinearLayout userGenderLayout;


    private ImageView usericonImageView;
    private TextView userDisplayNameTextView;
    private TextView userEmailTextView;
    private TextView userGenderTextView;

    private static final String TAG = "testActivity";
    private static final String CLASSTAG = "(UserAccountActivity)";

    public static Intent newIntent(Context context, String userDispalyName, String userGender,
                                   String userEmail, int userIconId) {
        Intent intent = new Intent(context, UserAccountActivity.class);
        intent.putExtra("displayName", userDispalyName);
        intent.putExtra("gender", userGender);
        intent.putExtra("email", userEmail);
        intent.putExtra("iconId", userIconId);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Account Information");

        usericonImageLayout = findViewById(R.id.user_account_icon_layout);
        userDisplayNameLayout = findViewById(R.id.user_account_display_name_layout);
        userEmailLayout = findViewById(R.id.user_account_display_email_layout);
        userGenderLayout = findViewById(R.id.user_account_display_gender_layout);

        usericonImageView = findViewById(R.id.user_account_user_icon);
        userDisplayNameTextView = findViewById(R.id.user_account_user_display_name);
        userEmailTextView = findViewById(R.id.user_account_user_email);
        userGenderTextView = findViewById(R.id.user_account_user_gender);

        userDispalyName = getIntent().getStringExtra("displayName");
        userGender = getIntent().getStringExtra("gender");
        userEmail = getIntent().getStringExtra("email");
        userIconId = getIntent().getIntExtra("iconId", 0);

        usericonImageView.setImageResource(userIconId);
        userDisplayNameTextView.setText(userDispalyName);
        userEmailTextView.setText(userEmail);
        userGenderTextView.setText(userGender);

        usericonImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                UserIconDialogFragment dialog= UserIconDialogFragment.newInstance();
                dialog.show(fm, "fragment_icon");
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void passDataFromUserIconDialogToUserAccount(int input) {
        Log.i(TAG, "passDataFromUserIconDialogToUserAccount: got the input: " + input + CLASSTAG);
        usericonImageView.setImageResource(input);

        String iconName = getResources().getResourceEntryName(input);
        FirebaseDatabaseInterface.updateUserIconName(iconName);
    }

}
