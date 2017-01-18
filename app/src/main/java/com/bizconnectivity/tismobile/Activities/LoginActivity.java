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
import com.bizconnectivity.tismobile.database.datasources.UserDetailDataSource;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.webservices.UserWSAsync;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

import static com.bizconnectivity.tismobile.Constant.ERR_MSG_INCORRECT_PASSWORD;
import static com.bizconnectivity.tismobile.Constant.ERR_MSG_INCORRECT_USERNAME;
import static com.bizconnectivity.tismobile.Constant.MSG_LOGIN_CORRECT;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOGINNAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_NAME;
import static com.bizconnectivity.tismobile.Constant.TEST_PASSWORD;
import static com.bizconnectivity.tismobile.Constant.TEST_USERNAME;
import static com.bizconnectivity.tismobile.webservices.TechnicianIDWS.invokeTechnicianIDWS;

public class LoginActivity extends AppCompatActivity  {

    //region declaration
    TextInputEditText mUsernameView, mPasswordView;
    UserDetail userDetail;
    UserDetailDataSource userDetailDataSource;
    SharedPreferences sharedPref;
    View focusView;
    String username, password, message;
    //endregion

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

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        // clear all values in shared preference
        sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear().apply();
    }

    private void attemptLogin() {

        SharedPreferences.Editor editor = sharedPref.edit();

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        username = mUsernameView.getText().toString();
        password = mPasswordView.getText().toString();

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

                    editor.putString(SHARED_PREF_LOGINNAME, TEST_USERNAME).apply();

                    //navigate to dashboard activity
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

                    editor.putString(SHARED_PREF_LOGINNAME, TEST_USERNAME).apply();

                    //navigate to dashboard activity
                    Intent intent = new Intent(this, DashboardActivity.class);
                    finish();
                    startActivity(intent);

                } else {

                    //region check with database
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
                    //endregion
                }
            }
        }
    }

    private void cancelLogin() {

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

        //region retrieve user login details from database
        userDetailDataSource = new UserDetailDataSource(this);

        userDetailDataSource.open();
        message = userDetailDataSource.retrieveUserDetails(userDetail);
        userDetailDataSource.close();

        if (message.equals(MSG_LOGIN_CORRECT)) {

            //navigate to dashboard
            Intent intent = new Intent(this, DashboardActivity.class);
            finish();
            startActivity(intent);

        } else if (message.equals(ERR_MSG_INCORRECT_USERNAME)) {

            mUsernameView.setError(ERR_MSG_INCORRECT_USERNAME);
            focusView = mUsernameView;
            focusView.requestFocus();

        } else {

            mPasswordView.setError(ERR_MSG_INCORRECT_PASSWORD);
            focusView = mPasswordView;
            focusView.requestFocus();
        }
        //endregion
    }
}

