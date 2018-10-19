package com.ecoone.mindfulmealplanner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    private String mUserName;

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
//        mDbInterface.addUser(mDb, "arlenx", "Arlen");
//        mDbInterface.isUserExist(mDb, "arlenx");

        Log.i(TAG, "````````````````````");
        String name = mDb.userDao().getUserId("arlenx");
        Log.i(TAG, "id: " + name);
//        User user = new User();
//        user.name = "arlenx";
//        user.id = "arlenxu";
//        mDb.userDao().addUser(user);
//        Log.i(TAG, "-------------------------");
//        initialization();

        loginFlag = isLogin();
//        Log.i(TAG, "Firse login sign: " + loginFlag);
        mUserName = getUserName();
//        Log.i(TAG, "First username: " + userName);

        if (loginFlag == 1) {
            Log.i(TAG, "Login !!!!");
            mUserName = getUserName();
            if (mUserName.length() != 0 ){
                Log.i(TAG, mUserName);
                startActivityAndFinish();
            }
        }

        setLoginAction();
//        setEditTextAction();


    }

    private void initialization() {
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(EXTRA_USERNAME);
        editor.remove(EXTRA_LOGIN_FLAG);
        editor.apply();
    }


    private int isLogin() {
        return settings.getInt(EXTRA_LOGIN_FLAG, 0);
    }

    private String getUserName() {
        return settings.getString(EXTRA_USERNAME, "");
    }

    private void setLoginAction() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserName = mEditText.getText().toString();
                if (mUserName.length() != 0) {
                    Log.i(TAG, "Get username:" + mUserName);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt(EXTRA_LOGIN_FLAG, 1);
                    editor.putString(EXTRA_USERNAME, mUserName);
                    editor.apply();
                }
                startActivityAndFinish();
            }
        });
    }

    private void setEditTextAction() {
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mUserName = mEditText.getText().toString();
                    if (mUserName.length() != 0) {
                        Log.i(TAG, "Get username:" + mUserName);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt(EXTRA_LOGIN_FLAG, 1);
                        editor.putString(EXTRA_USERNAME, mUserName);
                        editor.apply();
                    }
                    startActivityAndFinish();
                    return true;
                }
                return false;
            }
        });
    }

    private void startActivityAndFinish() {
        Intent intent = TestActivity.newIntent(LoginActivity.this, mUserName);
        startActivity(intent);
        LoginActivity.this.finish();
    }
}
