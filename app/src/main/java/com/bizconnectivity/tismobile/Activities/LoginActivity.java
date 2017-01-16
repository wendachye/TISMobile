package com.bizconnectivity.tismobile.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.bizconnectivity.tismobile.classes.UserDetail;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.DataSources.UserDetailDataSource;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.webservices.UserWSAsync;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOGINNAME;
import static com.bizconnectivity.tismobile.Constant.TEST_PASSWORD;
import static com.bizconnectivity.tismobile.Constant.TEST_USERNAME;

public class LoginActivity extends AppCompatActivity  {

    TextInputEditText mUsernameView, mPasswordView;
    UserDetail userDetail;
    UserDetailDataSource userDetailDataSource;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameView = (TextInputEditText) findViewById(R.id.tbUsername);
        mPasswordView = (TextInputEditText) findViewById(R.id.tbPassword);

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
        sharedPref = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
    }

    private void attemptLogin() {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        View focusView = null;

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        if(TextUtils.isEmpty(username)) {

            mUsernameView.setError(Constant.ERR_MSG_USERNAME_REQUIRED);
            focusView = mUsernameView;
            focusView.requestFocus();

        } else if(TextUtils.isEmpty(password)) {

            mPasswordView.setError(Constant.ERR_MSG_PASSWORD_REQUIRED);
            focusView = mPasswordView;
            focusView.requestFocus();

        } else {

            if (Common.isNetworkAvailable(this)) {

                if (username.equals(TEST_USERNAME) && password.equals(TEST_PASSWORD)) {

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(SHARED_PREF_LOGINNAME, TEST_USERNAME).commit();

                    //navigate to dashboard
                    Intent intent = new Intent(this, DashboardActivity.class);
                    finish();
                    startActivity(intent);

                } else {

                    //check with webservice
                    UserWSAsync task = new UserWSAsync(this, username, password);
                    task.execute();
                }

            } else {

                if (username.equals(TEST_USERNAME) && password.equals(TEST_PASSWORD)) {

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(SHARED_PREF_LOGINNAME, TEST_USERNAME).commit();

                    //navigate to dashboard
                    Intent intent = new Intent(this, DashboardActivity.class);
                    finish();
                    startActivity(intent);

                } else {

                    //check sqlite database
                    try {

                        //encrypt password
                        String encryptedPassword = AESCrypt.encrypt(password, Constant.KEY_ENCRYPT);
                        userDetail = new UserDetail();
                        userDetail.setUsername(username);
                        userDetail.setPassword(encryptedPassword);

                        checkUserLogin(userDetail);

                    } catch (GeneralSecurityException e) {
                        e.printStackTrace();
                    }

                }
            }
        }
    }

    private void cancelLogin() {

        View focusView = null;

        // Reset errors and clear value.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        mUsernameView.setText(null);
        mPasswordView.setText(null);

        focusView = mUsernameView;
        focusView.requestFocus();
    }

    private void checkUserLogin(UserDetail userDetail) {

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);
        View focusView = null;

        userDetailDataSource = new UserDetailDataSource(this);
        //open database
        userDetailDataSource.open();
        //retrieve user details
        String message = userDetailDataSource.retrieveUserDetails(userDetail);
        //close databse
        userDetailDataSource.close();

        if (message.equals(Constant.MSG_LOGIN_CORRECT)) {

            //navigate to dashboard
            Intent intent = new Intent(this, DashboardActivity.class);
            finish();
            startActivity(intent);

        } else if (message.equals(Constant.ERR_MSG_INCORRECT_USERNAME)) {

            mUsernameView.setError(Constant.ERR_MSG_INCORRECT_USERNAME);
            focusView = mUsernameView;
            focusView.requestFocus();

        } else {

            mPasswordView.setError(Constant.ERR_MSG_INCORRECT_PASSWORD);
            focusView = mPasswordView;
            focusView.requestFocus();
        }
    }

}

