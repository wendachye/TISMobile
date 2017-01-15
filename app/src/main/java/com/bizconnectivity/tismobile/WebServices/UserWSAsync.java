package com.bizconnectivity.tismobile.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.activities.LoginActivity;
import com.bizconnectivity.tismobile.classes.UserDetail;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.activities.DashboardActivity;
import com.bizconnectivity.tismobile.database.DataSources.UserDetailDataSource;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOGINNAME;

public class UserWSAsync extends AsyncTask<String, Void, Void> {

    Context context;
    String username;
    String password;
    boolean response;

    ProgressDialog progressDialog;

    UserDetail userDetail;
    UserDetailDataSource userDetailDataSource;

    public UserWSAsync(Context context, String username, String password) {

        this.context = context;
        this.username = username;
        this.password = password;
    }

    @Override
    protected Void doInBackground(String... params) {

        response = UserWS.invokeLoginWS(username, password);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {

        SharedPreferences sharedPref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (response) {

            //set username
            editor.putString(SHARED_PREF_LOGINNAME, username).apply();

            //insert into sqlite database
            try {

                //encrypt password
                String encryptedPassword = AESCrypt.encrypt(password, Constant.KEY_ENCRYPT);
                userDetail = new UserDetail();
                userDetail.setUsername(username);
                userDetail.setPassword(encryptedPassword);

                insertOrUpdateUserDetails(userDetail);

            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }

            //end progress dialog
            progressDialog.dismiss();

            //navigate to dashboard activity
            Intent intent = new Intent(context, DashboardActivity.class);
            ((LoginActivity)context).finish();
            context.startActivity(intent);

        } else {

            //end progress dialog
            progressDialog.dismiss();

            //prompt error message
            Common.shortToast(context, Constant.ERR_MSG_LOGIN_INCORRECT);
        }
    }

    @Override
    protected void onPreExecute() {

        //start progress dialog
        progressDialog = ProgressDialog.show(context, "Please wait..", "Loading...", true);
    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }

    //insert or update sqlite database
    private void insertOrUpdateUserDetails(UserDetail userDetail) {

        userDetailDataSource = new UserDetailDataSource(context);
        //open database
        userDetailDataSource.open();
        //insert or update to database
        userDetailDataSource.insertOrUpdateUserDetails(userDetail);
        //close database
        userDetailDataSource.close();
    }


}
