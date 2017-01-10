package com.bizconnectivity.tismobile.WebServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.Activities.LoginActivity;
import com.bizconnectivity.tismobile.Classes.UserDetail;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Activities.DashboardActivity;
import com.bizconnectivity.tismobile.Database.DataSources.UserDetailDataSource;
import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

public class UserWSAsync extends AsyncTask<String, Void, Void> {

    Context appContext;
    String Username;
    String Password;
    boolean success;

    UserDetail userDetail = new UserDetail();
    UserDetailDataSource userDetailDataSource;
    ProgressDialog progressDialog;

    public UserWSAsync(Context context, String username, String password) {
        appContext = context;
        Username = username;
        Password = password;
    }

    @Override
    protected Void doInBackground(String... params) {

        success = UserWS.invokeLoginWS(Username, Password);

        return null;

    }

    @Override
    protected void onPostExecute(Void result) {


        if (success) {

            //set username
            Constant.LOGIN_LOGINNAME = Username;

            //insert into sqlite database
            try {

                //encrypt password
                String encryptedPassword = AESCrypt.encrypt(Password, Constant.KEY_ENCRYPT);
                userDetail.setUsername(Username);
                userDetail.setPassword(encryptedPassword);

                insertOrUpdateUserDetails(userDetail);

            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            }

            //end progress dialog
            progressDialog.dismiss();

            //navigate to dashboard activity
            Intent intent = new Intent(appContext, DashboardActivity.class);
            ((LoginActivity)appContext).finish();
            appContext.startActivity(intent);

        } else {

            //end progress dialog
            progressDialog.dismiss();
            //prompt error message
            Common.shortToast(appContext, Constant.ERR_MSG_LOGIN_INCORRECT);

        }
    }

    @Override
    protected void onPreExecute() {
        //start progress dialog
        progressDialog = ProgressDialog.show(appContext, "Please wait..", "Loading...", true);
    }

    @Override
    protected void onProgressUpdate(Void... values) {

    }

    private void insertOrUpdateUserDetails(UserDetail userDetail) {

        userDetailDataSource = new UserDetailDataSource(appContext);
        userDetailDataSource.open();

        userDetailDataSource.insertOrUpdateUserDetails(userDetail);

        userDetailDataSource.close();
    }


}
