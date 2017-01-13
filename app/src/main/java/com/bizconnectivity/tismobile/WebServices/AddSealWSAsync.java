package com.bizconnectivity.tismobile.WebServices;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.bizconnectivity.tismobile.Activities.StopOperationActivity;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Database.DataSources.JobDetailDataSource;

import static com.bizconnectivity.tismobile.Constant.STATUS_SCAN_SEAL;

public class AddSealWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String sealNo, sealPos, updatedBy, jobID;
	int totalCount;
	Boolean response;
	Dialog scanSealDialog;
	JobDetailDataSource jobDetailDataSource;

	public AddSealWSAsync(Context context, Dialog scanSealDialog, int totalCount, String sealNo, String jobID, String sealPos, String updatedBy) {

		this.context = context;
		this.scanSealDialog = scanSealDialog;
		this.totalCount = totalCount;
		this.sealNo = sealNo;
		this.jobID = jobID;
		this.sealPos = sealPos;
		this.updatedBy = updatedBy;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = AddSealWS.invokeAddSealWS(sealNo, jobID, sealPos, updatedBy);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		int sharedPreCount = sharedPref.getInt(Constant.SHARED_PREF_ADD_SEAL_COUNT, 1);

		if (sharedPreCount < totalCount) {

			editor.putInt(Constant.SHARED_PREF_ADD_SEAL_COUNT, ++sharedPreCount).commit();

		} else if (sharedPreCount == totalCount) {

			editor.putString(Constant.SHARED_PREF_SCAN_SEAL, "done").commit();

			//region set job status
			editor.putString(Constant.SHARED_PREF_JOB_STATUS, STATUS_SCAN_SEAL).commit();

			jobDetailDataSource = new JobDetailDataSource(context);
			jobDetailDataSource.open();
			jobDetailDataSource.updateJobDetails(sharedPref.getString(Constant.SHARED_PREF_JOB_ID, ""), STATUS_SCAN_SEAL);
			jobDetailDataSource.close();
			//endregion

			//close scan seal dialog
			scanSealDialog.dismiss();

			Intent intent = new Intent(context, StopOperationActivity.class);
			((StopOperationActivity) context).finish();
			context.startActivity(intent);
		}
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onProgressUpdate(Void... values) {

	}
}
