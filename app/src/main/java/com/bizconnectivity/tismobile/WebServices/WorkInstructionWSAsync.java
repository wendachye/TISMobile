package com.bizconnectivity.tismobile.WebServices;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.Activities.JobMainActivity;
import com.bizconnectivity.tismobile.Activities.ScanDetailsActivity;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;

public class WorkInstructionWSAsync extends AsyncTask<String, Void, Void> {

	Context appContext;
	String timeSlotID, workInstruction, response;

	public WorkInstructionWSAsync(Context context, String selectedJobID, String scanContent) {
		appContext = context;
		timeSlotID = selectedJobID;
		workInstruction = scanContent;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = WorkInstructionWS.invokeRetrieveWorkInstruction(timeSlotID, workInstruction);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = appContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (!response.isEmpty()) {

			editor.putString(Constant.SHARED_PREF_WORK_INSTRUCTION, response).commit();
			editor.putString(Constant.SHARED_PREF_SCAN_DETAILS, "done").commit();

			Intent intent = new Intent(appContext, JobMainActivity.class);
			((ScanDetailsActivity) appContext).finish();
			appContext.startActivity(intent);

		} else {

			editor.putString(Constant.SHARED_PREF_WORK_INSTRUCTION, "").commit();
			Common.shortToast(appContext, Constant.SCAN_MSG_INVALID_WORK_INSTRUCTION_RECEIVED);

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
