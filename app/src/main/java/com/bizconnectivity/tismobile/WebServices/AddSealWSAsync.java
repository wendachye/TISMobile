package com.bizconnectivity.tismobile.webservices;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.activities.StopOperationActivity;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;

import java.util.ArrayList;

import static com.bizconnectivity.tismobile.Constant.ERR_MSG_SEAL_CANNOT_ADD;
import static com.bizconnectivity.tismobile.Constant.STATUS_SCAN_SEAL;

public class AddSealWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String sealPos, updatedBy, jobID;
	ArrayList<String> sealNo;
	int totalCount;
	Boolean response;
	Dialog scanSealDialog;
	JobDetailDataSource jobDetailDataSource;

	public AddSealWSAsync(Context context, Dialog scanSealDialog, int totalCount, ArrayList<String> sealNo, String jobID, String sealPos, String updatedBy) {

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

		for (int i=0; i<totalCount; i++) {

			response = AddSealWS.invokeAddSealWS(sealNo.get(i), jobID, sealPos, updatedBy);
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (response) {

			//region set job status
			editor.putString(Constant.SHARED_PREF_JOB_STATUS, STATUS_SCAN_SEAL).apply();

			jobDetailDataSource = new JobDetailDataSource(context);
			jobDetailDataSource.open();
			jobDetailDataSource.updateJobStatus(sharedPref.getString(Constant.SHARED_PREF_JOB_ID, ""), STATUS_SCAN_SEAL);
			jobDetailDataSource.close();
			//endregion

			//close scan seal dialog
			scanSealDialog.dismiss();

			Intent intent = new Intent(context, StopOperationActivity.class);
			((StopOperationActivity) context).finish();
			context.startActivity(intent);

		} else {

			Common.shortToast(context, ERR_MSG_SEAL_CANNOT_ADD);
		}
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onProgressUpdate(Void... values) {

	}
}
