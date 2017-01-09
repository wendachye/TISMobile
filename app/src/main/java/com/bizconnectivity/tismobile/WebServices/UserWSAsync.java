package com.bizconnectivity.tismobile.WebServices;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.Activities.CheckInActivity;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Activities.DashboardActivity;

import java.util.HashSet;
import java.util.Set;

public class UserWSAsync extends AsyncTask<String, Void, Void> {

    Context appContext;

    String type;
    String username;
    String password;
    String rackNo;

    boolean success;

    ProgressDialog progressDialog;

    public UserWSAsync(Context context, String tType, String name, String pw) {
        appContext = context;
        type = tType;
        username = name;
        password = pw;
    }

    public UserWSAsync(Context context, String tType, String rack) {
        appContext = context;
        type = tType;
        rackNo = rack;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (type.equals(ConstantWS.WSTYPE_LOGIN))
            success = UserWS.invokeLoginWS(username, password);
        else if (type.equals(ConstantWS.WSTYPE_CHECKTRUCKRACK))
            success = UserWS.invokeCheckTruckRackExistsWS(rackNo);
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (type.equals(ConstantWS.WSTYPE_LOGIN)) {

            if (success) {
                //end progress dialog
                progressDialog.dismiss();
                //set username
                Constant.LOGIN_LOGINNAME = username;
                //navigate to dashboard activity
                Intent intent = new Intent(appContext, DashboardActivity.class);
                appContext.startActivity(intent);
            } else {
                //end progress dialog
                progressDialog.dismiss();

                Common.shortToast(appContext, Constant.ERR_MSG_LOGIN_INCORRECT);
            }
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
}
