package com.bizconnectivity.tismobile.WebServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.Activities.StopOperationActivity;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;

import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_BOTTOM_SEAL1;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_BOTTOM_SEAL2;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_BOTTOM_SEAL3;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_BOTTOM_SEAL4;

public class CheckSealWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String sealNo;
	String jobID;
	Boolean response;
	ProgressDialog progressDialog;

	public CheckSealWSAsync(Context context, String jobID, String sealNo) {

		this.context = context;
		this.jobID = jobID;
		this.sealNo = sealNo;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = CheckSealWS.invokeCheckSeal(jobID, sealNo);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (response) {

			String seal = sharedPref.getString(Constant.SHARED_PREF_SCAN_VALUE, "");

			if (seal.equals(SCAN_VALUE_BOTTOM_SEAL1)) {

				editor.putString(SCAN_VALUE_BOTTOM_SEAL1, sealNo).commit();

			} else if (seal.equals(SCAN_VALUE_BOTTOM_SEAL2)) {

				editor.putString(SCAN_VALUE_BOTTOM_SEAL2, sealNo).commit();

			} else if (seal.equals(SCAN_VALUE_BOTTOM_SEAL3)) {

				editor.putString(SCAN_VALUE_BOTTOM_SEAL3, sealNo).commit();

			} else {

				editor.putString(SCAN_VALUE_BOTTOM_SEAL4, sealNo).commit();
			}

			//end progress dialog
			progressDialog.dismiss();

			((StopOperationActivity) context).scanSealDialog();

		} else {

			//end progress dialog
			progressDialog.dismiss();

			Common.shortToast(context, Constant.ERR_MSG_INVALID_SEAL_NO);

			Intent intent = new Intent(context, StopOperationActivity.class);
			((StopOperationActivity) context).finish();
			context.startActivity(intent);
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
}
