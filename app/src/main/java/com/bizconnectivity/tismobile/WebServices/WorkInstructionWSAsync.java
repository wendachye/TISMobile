package com.bizconnectivity.tismobile.webservices;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.activities.JobMainActivity;
import com.bizconnectivity.tismobile.activities.ScanDetailsActivity;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.DataSources.JobDetailDataSource;

import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_STATUS;
import static com.bizconnectivity.tismobile.Constant.STATUS_WORK_INSTRUCTION;

public class WorkInstructionWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String jobID, workInstruction, response;
	JobDetailDataSource jobDetailDataSource;
	ProgressDialog progressDialog;

	public WorkInstructionWSAsync(Context context, String jobID, String workInstruction) {

		this.context = context;
		this.jobID = jobID;
		this.workInstruction = workInstruction;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = WorkInstructionWS.invokeRetrieveWorkInstruction(jobID, workInstruction);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (!response.isEmpty()) {

			//update job status
			editor.putString(SHARED_PREF_JOB_STATUS, STATUS_WORK_INSTRUCTION).apply();

			jobDetailDataSource = new JobDetailDataSource(context);
			jobDetailDataSource.open();
			jobDetailDataSource.updateJobDetails(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_WORK_INSTRUCTION);
			jobDetailDataSource.close();

			//end progress dialog
			progressDialog.dismiss();

			Intent intent = new Intent(context, JobMainActivity.class);
			((ScanDetailsActivity) context).finish();
			context.startActivity(intent);

		} else {

			//end progress dialog
			progressDialog.dismiss();

			Common.shortToast(context, Constant.SCAN_MSG_INVALID_WORK_INSTRUCTION_RECEIVED);

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
