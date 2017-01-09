package com.bizconnectivity.tismobile.WebServices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.Activities.ScanDetailsActivity;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;


public class DriverIDWSAsync extends AsyncTask<String, Void, Void> {

	Context appContext;
	String timeslotId, idNumber, response;

	public DriverIDWSAsync(Context context, String selectedJobID, String driverID) {
		appContext = context;
		timeslotId = selectedJobID;
		idNumber = driverID;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = DriverIDWS.invokeRetrieveDriverID(timeslotId, idNumber);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = appContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (!response.isEmpty()) {

			editor.putString(Constant.SHARED_PREF_DRIVER_ID, response).commit();

			Intent intent = new Intent(appContext, ScanDetailsActivity.class);
			((ScanDetailsActivity) appContext).finish();
			appContext.startActivity(intent);

		} else {

			editor.putString(Constant.SHARED_PREF_DRIVER_ID, "").commit();
			Common.shortToast(appContext, Constant.SCAN_MSG_INVALID_DRIVER_ID_RECEIVED);

			Intent intent = new Intent(appContext, ScanDetailsActivity.class);
			((ScanDetailsActivity) appContext).finish();
			appContext.startActivity(intent);

		}

	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onProgressUpdate(Void... values) {

	}
}
