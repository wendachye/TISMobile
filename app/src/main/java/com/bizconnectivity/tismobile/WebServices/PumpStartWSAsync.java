package com.bizconnectivity.tismobile.webservices;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.bizconnectivity.tismobile.activities.LoadingOperationActivity;
import com.bizconnectivity.tismobile.activities.StopOperationActivity;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import static com.bizconnectivity.tismobile.Constant.STATUS_PUMP_START;
import static com.bizconnectivity.tismobile.Constant.simpleDateFormat2;
import static com.bizconnectivity.tismobile.Constant.simpleDateFormat3;

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

		Calendar calendar = Calendar.getInstance();
		String time = simpleDateFormat3.format(calendar.getTime());

		response = PumpStartWS.invokeUpdatePumpStartWS(jobID, time, updatedBy);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (response) {

			//region set job status and pump start time
			Calendar calendar = Calendar.getInstance();
			editor.putString(Constant.SHARED_PREF_PUMP_START_TIME, simpleDateFormat2.format(calendar.getTime()));
			editor.putString(Constant.SHARED_PREF_JOB_STATUS, STATUS_PUMP_START);
			editor.apply();

			jobDetailDataSource = new JobDetailDataSource(context);
			jobDetailDataSource.open();
			jobDetailDataSource.updateJobStatus(sharedPref.getString(Constant.SHARED_PREF_JOB_ID, ""), STATUS_PUMP_START);
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
