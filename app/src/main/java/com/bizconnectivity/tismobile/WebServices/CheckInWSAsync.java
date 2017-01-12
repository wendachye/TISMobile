package com.bizconnectivity.tismobile.WebServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bizconnectivity.tismobile.Activities.CheckInActivity;
import com.bizconnectivity.tismobile.Activities.DashboardActivity;
import com.bizconnectivity.tismobile.Classes.CheckIn;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Database.DataSources.LoadingBayDetailDataSource;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class CheckInWSAsync extends AsyncTask<String, Void, Void> {

	Context appContext;
	String rackNo;
	boolean response;

	LoadingBayDetailDataSource loadingBayDetailDataSource;
	CheckIn checkIn;

	ProgressDialog progressDialog;

	public CheckInWSAsync(Context context, String rack) {
		appContext = context;
		rackNo = rack;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = CheckInWS.invokeCheckTruckRack(rackNo);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		if (response) {

			//insert into sqlite database
			checkIn = new CheckIn();
			checkIn.setLoadingBayNo(rackNo);

			loadingBayDetailDataSource = new LoadingBayDetailDataSource(appContext);
			//open database
			loadingBayDetailDataSource.open();
			//insert loading bay no into sqlite
			loadingBayDetailDataSource.insertLoadingBayNo(appContext, checkIn);
			//close database
			loadingBayDetailDataSource.close();

			//get all the job details from web service
			JobDetailWSAsync task = new JobDetailWSAsync(appContext, Constant.calendar.getTime(), rackNo);
        	task.execute();

			//end progress dialog
			progressDialog.dismiss();

			//navigate back to checkIn Activity
			Intent intent = new Intent(appContext, CheckInActivity.class);
			((CheckInActivity) appContext).finish();
			appContext.startActivity(intent);

		} else {

			//end progress dialog
			progressDialog.dismiss();

			Common.shortToast(appContext, Constant.ERR_MSG_INVALID_TRUCK_BAY);

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
