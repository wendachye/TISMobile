package com.bizconnectivity.tismobile.WebServices;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.bizconnectivity.tismobile.Activities.StopOperationActivity;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;

public class AddSealWSAsync extends AsyncTask<String, Void, Void> {

	Context appContext;
	String SealNo, SealPos, UpdatedBy;
	int TimeSlotID, TotalCount;
	Boolean response;
	Dialog ScanSealDialog;

	public AddSealWSAsync(Context context, Dialog scanSealDialog, int totalCount, String sealNo, int timeSlotID, String sealPos, String updatedBy) {
		appContext = context;
		ScanSealDialog = scanSealDialog;
		TotalCount = totalCount;
		SealNo = sealNo;
		TimeSlotID = timeSlotID;
		SealPos = sealPos;
		UpdatedBy = updatedBy;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = AddSealWS.invokeAddSealWS(SealNo, TimeSlotID, SealPos, UpdatedBy);
		return null;

	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = appContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		int sharedPreCount = sharedPref.getInt(Constant.SHARED_PREF_ADD_SEAL_COUNT, 1);

		if (sharedPreCount < TotalCount) {

			editor.putInt(Constant.SHARED_PREF_ADD_SEAL_COUNT, ++sharedPreCount).commit();

		} else if (sharedPreCount == TotalCount) {

			editor.putString(Constant.SHARED_PREF_SCAN_SEAL, "done").commit();
			ScanSealDialog.dismiss();

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
