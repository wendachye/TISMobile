package com.bizconnectivity.tismobile.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.activities.CheckInActivity;
import com.bizconnectivity.tismobile.classes.CheckIn;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.datasources.TechnicianDetailDataSource;

public class TechnicianIDWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String nric;
	boolean response;

	TechnicianDetailDataSource technicianDetailDataSource;
	CheckIn checkIn;

	ProgressDialog progressDialog;

	public TechnicianIDWSAsync(Context context, String nric) {

		this.context = context;
		this.nric = nric;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = TechnicianIDWS.invokeTechnicianIDWS(nric);
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (response) {

			editor.putString(Constant.SHARED_PREF_TECHNICIAN_ID, nric).apply();

			//insert into sqlite database
			checkIn = new CheckIn();
			checkIn.setTechnicianNRIC(nric);

			technicianDetailDataSource = new TechnicianDetailDataSource(context);
			//open database
			technicianDetailDataSource.open();
			//insert technician nric
			technicianDetailDataSource.insertTechnicianNRIC(checkIn);
			//close database
			technicianDetailDataSource.close();

			//end progress dialog
			progressDialog.dismiss();

			Intent intent = new Intent(context, CheckInActivity.class);
			((CheckInActivity) context).finish();
			context.startActivity(intent);

		} else {

			editor.putString(Constant.SHARED_PREF_TECHNICIAN_ID, "").commit();

			//end progress dialog
			progressDialog.dismiss();

			Common.shortToast(context, Constant.ERR_MSG_INVALID_TECHNICIAN_NRIC);

			Intent intent = new Intent(context, CheckInActivity.class);
			((CheckInActivity) context).finish();
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
