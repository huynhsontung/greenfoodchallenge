package com.ecoone.mindfulmealplanner;

import android.content.SharedPreferences;
import android.nfc.Tag;
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
    private String userName;
    private EditText mEditText;
    private TextView mTextView;
    private Button getUserName;
    private Button login;
    private SharedPreferences settings;
    private int loginFlag = 0;

    private static final String LOGINTAG = "ISLOGIN";
    private static final String UserNAMETAG = "USERNAME";
    private static final String TAG = "testActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditText = findViewById(R.id.username_edit_text);
        mTextView = findViewById(R.id.test_view);
        getUserName = findViewById(R.id.get_username);
        login = findViewById(R.id.login);
        settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        initialization();

        loginFlag = isLogin();
//        Log.i(TAG, "Firse login sign: " + loginFlag);
        userName = getUserName();
//        Log.i(TAG, "First username: " + userName);

        if (loginFlag == 1) {
            Log.i(TAG, "Login !!!!");
            userName = getUserName();
            if (userName.length() != 0 ){
                Log.i(TAG, userName);
            }
        }

        setLoginAction();
//        setEditTextAction();


    }

    private void initialization() {
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(UserNAMETAG);
        editor.remove(LOGINTAG);
        editor.apply();
    }


    private int isLogin() {
        return settings.getInt(LOGINTAG, 0);
    }

    private String getUserName() {
        return settings.getString(UserNAMETAG, "");
    }

    private void setLoginAction() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName = mEditText.getText().toString();
                if (userName.length() != 0) {
                    Log.i(TAG, "Get username:" + userName);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt(LOGINTAG, 1);
                    editor.putString(UserNAMETAG, userName);
                    editor.apply();
                }
            }
        });
    }

    private void setEditTextAction() {
        mEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    userName = mEditText.getText().toString();
                    if (userName.length() != 0) {
                        Log.i(TAG, "Get username:" + userName);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putInt(LOGINTAG, 1);
                        editor.putString(UserNAMETAG, userName);
                        editor.apply();
                    }
                    return true;
                }
                return false;
            }
        });
    }
}
