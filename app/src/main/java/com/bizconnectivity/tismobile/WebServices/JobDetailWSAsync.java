package com.bizconnectivity.tismobile.WebServices;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.bizconnectivity.tismobile.Classes.JobDetail;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Database.DataSources.JobDetailDataSource;

import java.util.ArrayList;
import java.util.Date;

public class JobDetailWSAsync extends AsyncTask<String, Void, Void> {

	Context appContext;
	String TruckRackNo;
	Date BookingDate;

	ArrayList<JobDetail> jobDetailArrayList = new ArrayList<>();
	JobDetailDataSource jobDetailDataSource;
	JobDetail jobDetail;

	public JobDetailWSAsync(Context context, Date bookingDate, String truckRackNo) {
		appContext = context;
		BookingDate = bookingDate;
		TruckRackNo = truckRackNo;
	}


	@Override
	protected Void doInBackground(String... params) {

		jobDetailArrayList = JobDetailWS.invokeRetrieveAllJobs(BookingDate, TruckRackNo);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = appContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (jobDetailArrayList.size() > 0) {

			for (int i=0; i<jobDetailArrayList.size(); i++) {

				jobDetailDataSource = new JobDetailDataSource(appContext);
				jobDetailDataSource.open();

				jobDetailDataSource.insertOrUpdateJobDetails(jobDetailArrayList.get(i));

				jobDetailDataSource.close();
			}

		}

	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onProgressUpdate(Void... values) {

	}
}
