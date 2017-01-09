package com.bizconnectivity.tismobile.WebServices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.Activities.StopOperationActivity;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;

public class CheckSealWSAsync extends AsyncTask<String, Void, Void> {

	Context appContext;
	String SealNo;
	int TimeSlotID;
	Boolean response;

	public CheckSealWSAsync(Context context, int timeSlotID, String sealNo) {
		appContext = context;
		TimeSlotID = timeSlotID;
		SealNo = sealNo;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = CheckSealWS.invokeCheckSeal(TimeSlotID, SealNo);
		return null;

	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = appContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (response) {

			editor.putString(Constant.SHARED_PREF_SCAN_SEAL, "done");
			editor.putString(Constant.SCAN_VALUE_BOTTOM_SEAL1, SealNo);
			editor.commit();

			((StopOperationActivity) appContext).scanSealDialog();

		} else {

			editor.putString(Constant.SHARED_PREF_SCAN_SEAL, "");
			editor.putString(Constant.SCAN_VALUE_BOTTOM_SEAL1, "");
			editor.commit();

			Common.shortToast(appContext, Constant.ERR_MSG_INVALID_SEAL_NO);

			Intent intent = new Intent(appContext, StopOperationActivity.class);
			((StopOperationActivity) appContext).finish();
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
