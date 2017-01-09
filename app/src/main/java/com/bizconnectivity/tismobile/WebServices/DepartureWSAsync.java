package com.bizconnectivity.tismobile.WebServices;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.Activities.DashboardActivity;
import com.bizconnectivity.tismobile.Activities.JobMainActivity;
import com.bizconnectivity.tismobile.Activities.StopOperationActivity;
import com.bizconnectivity.tismobile.Constant;

public class DepartureWSAsync extends AsyncTask<String, Void, Void> {

	Context appContext;
	boolean response;
	int TimeslotID;
	String UpdatedBy;
	Dialog DepartureDialog;

	public DepartureWSAsync(Context context, Dialog departureDialog, int timeslotID, String updatedBy) {
		appContext = context;
		DepartureDialog = departureDialog;
		TimeslotID = timeslotID;
		UpdatedBy = updatedBy;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = DepartureWS.invokeAddDepartureWS(TimeslotID, UpdatedBy);
		return null;

	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = appContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (response) {

			editor.remove(Constant.SHARED_PREF_PPE);
			editor.remove(Constant.SHARED_PREF_PPE_PICTURE_URL);
			editor.remove(Constant.SHARED_PREF_SDS);
			editor.remove(Constant.SHARED_PREF_SDS_PDF_URL);
			editor.remove(Constant.SHARED_PREF_SCAN_DETAILS);
			editor.remove(Constant.SHARED_PREF_SAFETY_CHECKS);
			editor.remove(Constant.SHARED_PREF_OPERATOR_ID);
			editor.remove(Constant.SHARED_PREF_DRIVER_ID);
			editor.remove(Constant.SHARED_PREF_WORK_INSTRUCTION);
			editor.remove(Constant.SHARED_PREF_LOADING_ARM);
			editor.remove(Constant.SHARED_PREF_BATCH_CONTROLLER_L);
			editor.remove(Constant.SHARED_PREF_PUMP_START);
			editor.remove(Constant.SHARED_PREF_PUMP_STOP);
			editor.remove(Constant.SHARED_PREF_SCAN_SEAL);
			editor.remove(Constant.SCAN_VALUE_BOTTOM_SEAL1);
			editor.remove(Constant.SCAN_VALUE_BOTTOM_SEAL2);
			editor.remove(Constant.SCAN_VALUE_BOTTOM_SEAL3);
			editor.remove(Constant.SCAN_VALUE_BOTTOM_SEAL4);
			editor.remove(Constant.SHARED_PREF_ADD_SEAL_COUNT);
			editor.apply();

			DepartureDialog.dismiss();

			Intent intent = new Intent(appContext, DashboardActivity.class);
			((StopOperationActivity) appContext).finish();
			appContext.startActivity(intent);

		} else {

		}
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onProgressUpdate(Void... values) {

	}
}
