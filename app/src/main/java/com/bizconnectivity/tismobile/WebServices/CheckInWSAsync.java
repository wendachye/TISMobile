package com.bizconnectivity.tismobile.WebServices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.bizconnectivity.tismobile.Activities.CheckInActivity;
import com.bizconnectivity.tismobile.Activities.DashboardActivity;
import com.bizconnectivity.tismobile.Classes.CheckIn;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Database.DataSources.LoadingBayDetailDataSource;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

public class CheckInWSAsync extends AsyncTask<String, Void, Void> {

	Context appContext;
	String rackNo;
	boolean response;

	LoadingBayDetailDataSource loadingBayDetailDataSource;
	CheckIn checkIn;

	ProgressDialog progressDialog;

	public CheckInWSAsync(Context context, String rack) {
		appContext = context;
		rackNo = rack;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = CheckInWS.invokeCheckTruckRack(rackNo);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = appContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

		if (response) {

			Set<String> checkedInTruckBay = sharedPref.getStringSet(Constant.SHARED_PREF_TRUCK_LOADING_BAY, null);

			if (checkedInTruckBay == null) {

				//add new rackNo
				checkedInTruckBay = new HashSet<>();
				checkedInTruckBay.add(rackNo);

			} else {

				if (checkedInTruckBay.contains(rackNo)) {

					Common.shortToast(appContext, Constant.ERR_MSG_TRUCK_BAY_ALREADY_CHECKED_IN);

				} else {

					//add new rackNo
					checkedInTruckBay.add(rackNo);

				}

			}

			//save truck loading bay ID
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putStringSet(Constant.SHARED_PREF_TRUCK_LOADING_BAY, checkedInTruckBay);
			editor.commit();

			//insert into sqlite database
			checkIn = new CheckIn();
			checkIn.setLoadingBayNo(rackNo);
			loadingBayDetailDataSource = new LoadingBayDetailDataSource(appContext);
			loadingBayDetailDataSource.open();
			loadingBayDetailDataSource.insertLoadingBayNo(checkIn);
			loadingBayDetailDataSource.close();

			Calendar calendar = Calendar.getInstance();
			calendar.set(2016, Calendar.MARCH, 22, 8, 0, 0);
			String truckNo = "01";

			//get all the job details from rack no
			JobDetailWSAsync task = new JobDetailWSAsync(appContext, calendar.getTime(), truckNo);
        	task.execute();

			//end progress dialog
			progressDialog.dismiss();

			//navigate back to checkIn Activity
			Intent intent = new Intent(appContext, CheckInActivity.class);
			((CheckInActivity) appContext).finish();
			appContext.startActivity(intent);

		} else {

			//end progress dialog
			progressDialog.dismiss();

			Common.shortToast(appContext, Constant.ERR_MSG_INVALID_TRUCK_BAY);

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
