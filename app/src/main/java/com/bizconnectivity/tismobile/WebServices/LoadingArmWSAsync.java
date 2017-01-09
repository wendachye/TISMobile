package com.bizconnectivity.tismobile.WebServices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.Activities.LoadingOperationActivity;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;

public class LoadingArmWSAsync extends AsyncTask<String, Void, Void> {

	Context appContext;
	String ArmNo;
	int TimeSlotID;
	Boolean response;

	public LoadingArmWSAsync(Context context, int timeSlotID, String armNo) {
		appContext = context;
		TimeSlotID = timeSlotID;
		ArmNo = armNo;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = LoadingArmWS.invokeCheckLoadingArmWS(TimeSlotID, ArmNo);
		return null;

	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = appContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (response) {

			editor.putString(Constant.SHARED_PREF_LOADING_ARM, "done").commit();

			Intent intent = new Intent(appContext, LoadingOperationActivity.class);
			((LoadingOperationActivity) appContext).finish();
			appContext.startActivity(intent);

		} else {

			editor.putString(Constant.SHARED_PREF_LOADING_ARM, "").commit();
			Common.shortToast(appContext, Constant.ERR_MSG_INVALID_LOADING_ARM);

			Intent intent = new Intent(appContext, LoadingOperationActivity.class);
			((LoadingOperationActivity) appContext).finish();
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
