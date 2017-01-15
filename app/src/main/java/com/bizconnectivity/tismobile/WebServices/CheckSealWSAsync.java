package com.bizconnectivity.tismobile.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.activities.StopOperationActivity;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;

import static com.bizconnectivity.tismobile.Constant.ERR_MSG_CANNOT_ADD_SEAL;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_BOTTOM_SEAL1;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_BOTTOM_SEAL2;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_BOTTOM_SEAL3;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_BOTTOM_SEAL4;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_SCAN_VALUE;

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

			String seal = sharedPref.getString(SHARED_PREF_SCAN_VALUE, "");

			String seal1 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL1, "");
			String seal2 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL2, "");
			String seal3 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL3, "");
			String seal4 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL4, "");

			if (seal.equals(SCAN_VALUE_BOTTOM_SEAL1)) {

				if (!sealNo.equals(seal1)) {

					editor.putString(SCAN_VALUE_BOTTOM_SEAL1, sealNo).apply();

				} else {

					Common.shortToast(context, ERR_MSG_CANNOT_ADD_SEAL);
				}

			} else if (seal.equals(SCAN_VALUE_BOTTOM_SEAL2)) {

				if (!sealNo.equals(seal1) && !sealNo.equals(seal2)) {

					editor.putString(SCAN_VALUE_BOTTOM_SEAL2, sealNo).apply();

				} else {

					Common.shortToast(context, ERR_MSG_CANNOT_ADD_SEAL);
				}

			} else if (seal.equals(SCAN_VALUE_BOTTOM_SEAL3)) {

				if (!sealNo.equals(seal1) && !sealNo.equals(seal2) && !sealNo.equals(seal3)) {

					editor.putString(SCAN_VALUE_BOTTOM_SEAL3, sealNo).apply();

				} else {

					Common.shortToast(context, ERR_MSG_CANNOT_ADD_SEAL);
				}

			} else {

				if (!sealNo.equals(seal1) && !sealNo.equals(seal2) && !sealNo.equals(seal3) && !sealNo.equals(seal4)) {

					editor.putString(SCAN_VALUE_BOTTOM_SEAL4, sealNo).apply();

				} else {

					Common.shortToast(context, ERR_MSG_CANNOT_ADD_SEAL);
				}
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
