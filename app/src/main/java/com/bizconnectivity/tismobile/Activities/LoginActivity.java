package com.bizconnectivity.tismobile.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.WebServices.ConstantWS;
import com.bizconnectivity.tismobile.WebServices.UserWSAsync;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity  {

    // UI references.
    private AutoCompleteTextView mUsernameView;
    private EditText mPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mUsernameView = (AutoCompleteTextView) findViewById(R.id.tbUsername);
        mPasswordView = (EditText) findViewById(R.id.tbPassword);

        Button mUserSignInButton = (Button) findViewById(R.id.btnSignIn);
        mUserSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button mUserSignInCancelButton = (Button) findViewById(R.id.btnSignInCancel);
        mUserSignInCancelButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelLogin();
            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // clear all values in shared preference
        SharedPreferences sharedPref = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check if username is entered.
        if(TextUtils.isEmpty(username))
        {
            mUsernameView.setError(Constant.ERR_MSG_USERNAME_REQUIRED);
            focusView = mUsernameView;
            cancel = true;
        }
        // Check if password is entered.
        else if(TextUtils.isEmpty(password))
        {
            mPasswordView.setError(Constant.ERR_MSG_PASSWORD_REQUIRED);
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (Common.isNetworkAvailable(this)) {
                if (username.equals(Constant.TEST_USERNAME) && password.equals(Constant.TEST_PASSWORD)) {
                    Intent intent = new Intent(this, DashboardActivity.class);
                    startActivity(intent);
                } else {
                    UserWSAsync task = new UserWSAsync(this, ConstantWS.WSTYPE_LOGIN, username, password);
                    task.execute();
                }
            }
            else {
                Common.shortToast(this, "Network Not Available");
            }
        }
    }

    private void cancelLogin()
    {
        View focusView = null;

        // Reset errors and clear value.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mUsernameView.setText(null);
        mPasswordView.setText(null);

        focusView = mUsernameView;
        focusView.requestFocus();
    }
}

