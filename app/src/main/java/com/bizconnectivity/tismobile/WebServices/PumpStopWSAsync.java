package com.bizconnectivity.tismobile.WebServices;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.Activities.StopOperationActivity;
import com.bizconnectivity.tismobile.Constant;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PumpStopWSAsync extends AsyncTask<String, Void, Void> {

	Context appContext;
	String UpdatedBy;
	int TimeSlotID;
	Boolean response;

	Calendar calendar = Calendar.getInstance();
	SimpleDateFormat dateFormat = new SimpleDateFormat("HH:MM");

	public PumpStopWSAsync(Context context, int timeSlotID, String updatedBy) {
		appContext = context;
		TimeSlotID = timeSlotID;
		UpdatedBy = updatedBy;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = PumpStopWS.invokeUpdatePumpStopWS(TimeSlotID, UpdatedBy);
		return null;

	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = appContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

		if (response) {

			SharedPreferences.Editor editor = sharedPref.edit();
			editor.putString(Constant.SHARED_PREF_PUMP_STOP, dateFormat.format(calendar.getTime())).commit();

			Intent intent = new Intent(appContext, StopOperationActivity.class);
			((StopOperationActivity) appContext).finish();
			appContext.startActivity(intent);

		} else {

		}
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onProgressUpdate(Void... values) {

	}
}
