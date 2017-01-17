package com.bizconnectivity.tismobile.webservices;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.activities.DashboardActivity;
import com.bizconnectivity.tismobile.activities.StopOperationActivity;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;

public class DepartureWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	boolean response;
	String jobID;
	String updatedBy;
	Dialog departureDialog;
	ProgressDialog progressDialog;
	JobDetailDataSource jobDetailDataSource;

	public DepartureWSAsync(Context context, Dialog departureDialog, String jobID, String updatedBy) {

		this.context = context;
		this.departureDialog = departureDialog;
		this.jobID = jobID;
		this.updatedBy = updatedBy;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = DepartureWS.invokeAddDepartureWS(jobID, updatedBy);
		return null;

	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (response) {

			//region remove job in sqlite
			jobDetailDataSource = new JobDetailDataSource(context);
			jobDetailDataSource.open();
			jobDetailDataSource.deleteFinishedJob(sharedPref.getString(Constant.SHARED_PREF_JOB_ID, ""));
			jobDetailDataSource.close();
			//endregion

			//region remove job details shared pref
			editor.remove(Constant.SHARED_PREF_JOB_ID);
			editor.remove(Constant.SHARED_PREF_CUSTOMER_NAME);
			editor.remove(Constant.SHARED_PREF_PRODUCT_NAME);
			editor.remove(Constant.SHARED_PREF_TANK_NO);
			editor.remove(Constant.SHARED_PREF_LOADING_BAY);
			editor.remove(Constant.SHARED_PREF_LOADING_ARM);
			editor.remove(Constant.SHARED_PREF_SDS_FILE_PATH);
			editor.remove(Constant.SHARED_PREF_OPERATOR_ID);
			editor.remove(Constant.SHARED_PREF_DRIVER_ID);
			editor.remove(Constant.SHARED_PREF_WORK_INSTRUCTION);
			editor.remove(Constant.SHARED_PREF_PUMP_START_TIME);
			editor.remove(Constant.SHARED_PREF_PUMP_STOP_TIME);
			editor.remove(Constant.SHARED_PREF_RACK_OUT_TIME);
			editor.remove(Constant.SHARED_PREF_JOB_STATUS);
			editor.remove(Constant.SHARED_PREF_JOB_DATE);
			editor.remove(Constant.SHARED_PREF_BATCH_CONTROLLER);
			editor.remove(Constant.SHARED_PREF_BATCH_CONTROLLER_LITRE);
			editor.remove(Constant.SCAN_VALUE_BOTTOM_SEAL1);
			editor.remove(Constant.SCAN_VALUE_BOTTOM_SEAL2);
			editor.remove(Constant.SCAN_VALUE_BOTTOM_SEAL3);
			editor.remove(Constant.SCAN_VALUE_BOTTOM_SEAL4);
			editor.apply();
			//endregion

			//end progress dialog
			progressDialog.dismiss();

			//close departure dialog
			departureDialog.dismiss();

			Intent intent = new Intent(context, DashboardActivity.class);
			((StopOperationActivity) context).finish();
			context.startActivity(intent);

		} else {

			//end progress dialog
			progressDialog.dismiss();
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
