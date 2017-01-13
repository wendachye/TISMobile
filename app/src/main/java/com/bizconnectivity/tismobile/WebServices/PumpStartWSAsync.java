package com.bizconnectivity.tismobile.WebServices;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.Activities.LoadingOperationActivity;
import com.bizconnectivity.tismobile.Activities.StopOperationActivity;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Database.DataSources.JobDetailDataSource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.bizconnectivity.tismobile.Constant.STATUS_PUMP_START;
import static com.bizconnectivity.tismobile.Constant.calendar;
import static com.bizconnectivity.tismobile.Constant.simpleDateFormat2;

public class PumpStartWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String updatedBy;
	String jobID;
	Boolean response;
	Dialog pumpStartDialog;
	ProgressDialog progressDialog;
	JobDetailDataSource jobDetailDataSource;

	public PumpStartWSAsync(Context context, Dialog pumpStartDialog, String jobID, String updatedBy) {

		this.context = context;
		this.pumpStartDialog = pumpStartDialog;
		this.jobID = jobID;
		this.updatedBy = updatedBy;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = PumpStartWS.invokeUpdatePumpStartWS(jobID, updatedBy);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (response) {

			//region set job status and pump start time
			editor.putString(Constant.SHARED_PREF_PUMP_START_TIME, simpleDateFormat2.format(calendar.getTime())).commit();
			editor.putString(Constant.SHARED_PREF_JOB_STATUS, STATUS_PUMP_START).commit();

			jobDetailDataSource = new JobDetailDataSource(context);
			jobDetailDataSource.open();
			jobDetailDataSource.updateJobDetails(sharedPref.getString(Constant.SHARED_PREF_JOB_ID, ""), STATUS_PUMP_START);
			jobDetailDataSource.close();
			//endregion

			//end progress dialog
			progressDialog.dismiss();
			//end pump start dialog
			pumpStartDialog.dismiss();

			Intent intent = new Intent(context, StopOperationActivity.class);
			((LoadingOperationActivity) context).finish();
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
