package com.ecoone.mindfulmealplanner;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

    private String mUserName;
    private TextView mTestTextView;

    private static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.testactivity.username";


    public static Intent newIntent(Context packageContext, String userName) {
        Intent intent = new Intent(packageContext, TestActivity.class);
        intent.putExtra(EXTRA_USERNAME, userName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mUserName = getIntent().getStringExtra(EXTRA_USERNAME);
        mTestTextView = findViewById(R.id.test_view);
        mTestTextView.setText(mUserName);
    }
}
