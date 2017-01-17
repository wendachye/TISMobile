package com.bizconnectivity.tismobile.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.activities.LoadingOperationActivity;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;

import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_STATUS;
import static com.bizconnectivity.tismobile.Constant.STATUS_SCAN_LOADING_ARM;

public class LoadingArmWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String armNo;
	String jobID;
	Boolean response;
	ProgressDialog progressDialog;
	JobDetailDataSource jobDetailDataSource;

	public LoadingArmWSAsync(Context context, String jobID, String armNo) {

		this.context = context;
		this.jobID = jobID;
		this.armNo = armNo;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = LoadingArmWS.invokeCheckLoadingArmWS(jobID, armNo);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (response) {

			//region set job status
			editor.putString(SHARED_PREF_JOB_STATUS, STATUS_SCAN_LOADING_ARM).commit();

			jobDetailDataSource = new JobDetailDataSource(context);
			jobDetailDataSource.open();
			jobDetailDataSource.updateJobDetails(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_SCAN_LOADING_ARM);
			jobDetailDataSource.close();
			//endregion

			//end progress dialog
			progressDialog.dismiss();

			Intent intent = new Intent(context, LoadingOperationActivity.class);
			((LoadingOperationActivity) context).finish();
			context.startActivity(intent);

		} else {

			//end progress dialog
			progressDialog.dismiss();

			Common.shortToast(context, Constant.ERR_MSG_INVALID_LOADING_ARM);

			Intent intent = new Intent(context, LoadingOperationActivity.class);
			((LoadingOperationActivity) context).finish();
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
