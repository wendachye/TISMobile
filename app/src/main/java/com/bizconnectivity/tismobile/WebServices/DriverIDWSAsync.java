package com.bizconnectivity.tismobile.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.activities.ScanDetailsActivity;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;

import static com.bizconnectivity.tismobile.Constant.STATUS_DRIVER_ID;


public class DriverIDWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String jobID, driverID, response;
	JobDetailDataSource jobDetailDataSource;
	ProgressDialog progressDialog;

	public DriverIDWSAsync(Context context, String jobID, String driverID) {

		this.context = context;
		this.jobID = jobID;
		this.driverID = driverID;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = DriverIDWS.invokeRetrieveDriverID(jobID, driverID);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (!response.isEmpty()) {

			//update job status
			editor.putString(Constant.SHARED_PREF_JOB_STATUS, STATUS_DRIVER_ID).commit();

			jobDetailDataSource = new JobDetailDataSource(context);
			jobDetailDataSource.open();
			jobDetailDataSource.updateJobDetails(sharedPref.getString(Constant.SHARED_PREF_JOB_ID, ""), STATUS_DRIVER_ID);
			jobDetailDataSource.close();

			//end progress dialog
			progressDialog.dismiss();

			Intent intent = new Intent(context, ScanDetailsActivity.class);
			((ScanDetailsActivity) context).finish();
			context.startActivity(intent);

		} else {

			//end progress dialog
			progressDialog.dismiss();

			Common.shortToast(context, Constant.SCAN_MSG_INVALID_DRIVER_ID_RECEIVED);

			Intent intent = new Intent(context, ScanDetailsActivity.class);
			((ScanDetailsActivity) context).finish();
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
