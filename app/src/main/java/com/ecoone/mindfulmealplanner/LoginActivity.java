package com.ecoone.mindfulmealplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ecoone.mindfulmealplanner.db.AppDatabase;

public class LoginActivity extends AppCompatActivity {
    private String mUsername;

    private EditText mEditText;
    private TextView mTextView;
    private Button getUserName;
    private Button login;
    private SharedPreferences settings;
    private int loginFlag = 0;

    private AppDatabase mDb;

    private static final String EXTRA_LOGIN_FLAG =
            "com.ecoone.mindfulmealplanner.loginactivity.login_flag";
    private static final String EXTRA_USERNAME =
            "com.ecoone.mindfulmealplanner.loginactivity.username";
    private static final String TAG = "testActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditText = findViewById(R.id.username_edit_text);
        mTextView = findViewById(R.id.login_test_view);
        getUserName = findViewById(R.id.get_username);
        login = findViewById(R.id.login);

        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mDb = AppDatabase.getDatabase(getApplicationContext());

        initialization();



        setLoginAction();

        checkIfLogin();
//        setEditTextAction();


    }

    private void initialization() {
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(EXTRA_USERNAME);
        editor.remove(EXTRA_LOGIN_FLAG);
        editor.apply();
        mDb.planDao().deleteAll();
        mDb.userDao().deleteALL();
        dbInterface.addUser(mDb, "arlenx", "male");
    }

    private void checkIfLogin() {
        loginFlag = isLogin();
        // if no user login
        if (loginFlag == 0) {
            Log.i(TAG, "No user log in. Wait for action to check if user in the db.");
//            mUsername = mEditText.getText().toString();
            // check db
//            String name = dbInterface.getUserbyUsername(mDb, mUsername);
            // if user exists, login
//            Log.i(TAG, "User name:" + name);
//            if (name != null) {
//                Log.i(TAG, "User exist.");
//                startActivityAndFinish(mUsername);
//            }
//            else {
//                // sign up
//                Log.i(TAG, "need to sign up");
//            }
        }
        // someone already log in
        else {
            Log.i(TAG, "Someone already login");
            mUsername = getUsername();
            startActivityAndFinish(mUsername);
        }
    }

    private int isLogin() {
        return settings.getInt(EXTRA_LOGIN_FLAG, 0);
    }

    private String getUsername() {
        return settings.getString(EXTRA_USERNAME, "");
    }

    private void setLoginAction() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUsername = mEditText.getText().toString();
                if (mUsername.length() != 0) {
                    Log.i(TAG, "Get username in Edittext:" + mUsername + " and check db");
                    // check db
                    String name = dbInterface.getUserbyUsername(mDb, mUsername);
                    Log.i(TAG, "Get user name in db:" + name);
                    // if user exists, login
                    if (name != null) {
                        Log.i(TAG, "User exist.");
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt(EXTRA_LOGIN_FLAG, 1);
                        editor.putString(EXTRA_USERNAME, mUsername);
                        editor.apply();
                        startActivityAndFinish(mUsername);
                    } else {
                        // sign up
                        Log.i(TAG, "need to sign up");
                    }
                }
            }
        });
    }

//    private void setEditTextAction() {
//        mEditText.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
//                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
//                    mUsername = mEditText.getText().toString();
//                    if (mUsername.length() != 0) {
//                        Log.i(TAG, "Get username:" + mUsername);
//                        SharedPreferences.Editor editor = settings.edit();
//                        editor.putInt(EXTRA_LOGIN_FLAG, 1);
//                        editor.putString(EXTRA_USERNAME, mUsername);
//                        editor.apply();
//                    }
//                    startActivityAndFinish();
//                    return true;
//                }
//                return false;
//            }
//        });
//    }

    private void startActivityAndFinish(String username) {
        Intent intent = DashboardActivity.newIntent(LoginActivity.this);
        startActivity(intent);
        LoginActivity.this.finish();
    }
}
