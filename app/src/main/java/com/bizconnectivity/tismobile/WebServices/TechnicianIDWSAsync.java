package com.bizconnectivity.tismobile.WebServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.Activities.CheckInActivity;
import com.bizconnectivity.tismobile.Classes.CheckIn;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Database.DataSources.TechnicianDetailDataSource;

public class TechnicianIDWSAsync extends AsyncTask<String, Void, Void> {

	Context appContext;
	String NRIC;
	boolean response;

	TechnicianDetailDataSource technicianDetailDataSource;
	CheckIn checkIn;

	ProgressDialog progressDialog;

	public TechnicianIDWSAsync(Context context, String nric) {

		appContext = context;
		NRIC = nric;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = TechnicianIDWS.invokeTechnicianIDWS(NRIC);
		return null;

	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = appContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (response) {

			editor.putString(Constant.SHARED_PREF_TECHNICIAN_ID, NRIC).commit();

			//insert into sqlite database
			checkIn = new CheckIn();
			checkIn.setTechnicianNRIC(NRIC);

			technicianDetailDataSource = new TechnicianDetailDataSource(appContext);
			//open database
			technicianDetailDataSource.open();
			//insert technician nric
			technicianDetailDataSource.insertTechnicianNRIC(checkIn);
			//close database
			technicianDetailDataSource.close();

			//end progress dialog
			progressDialog.dismiss();

			Intent intent = new Intent(appContext, CheckInActivity.class);
			((CheckInActivity)appContext).finish();
			appContext.startActivity(intent);

		} else {

			editor.putString(Constant.SHARED_PREF_TECHNICIAN_ID, "").commit();

			//end progress dialog
			progressDialog.dismiss();

			Common.shortToast(appContext, Constant.ERR_MSG_INVALID_TECHNICIAN_NRIC);

			Intent intent = new Intent(appContext, CheckInActivity.class);
			((CheckInActivity) appContext).finish();
			appContext.startActivity(intent);

		}

	}

	@Override
	protected void onPreExecute() {

		//start progress dialog
		progressDialog = ProgressDialog.show(appContext, "Please wait..", "Loading...", true);
	}

	@Override
	protected void onProgressUpdate(Void... values) {

	}
}
