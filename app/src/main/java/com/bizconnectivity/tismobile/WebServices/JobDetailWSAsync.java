package com.bizconnectivity.tismobile.WebServices;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.bizconnectivity.tismobile.Classes.GHS;
import com.bizconnectivity.tismobile.Classes.GHSDetail;
import com.bizconnectivity.tismobile.Classes.JobDetail;
import com.bizconnectivity.tismobile.Classes.PPE;
import com.bizconnectivity.tismobile.Classes.PPEDetail;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Database.DataSources.GHSDetailDataSource;
import com.bizconnectivity.tismobile.Database.DataSources.JobDetailDataSource;
import com.bizconnectivity.tismobile.Database.DataSources.PPEDetailDataSource;

import java.util.ArrayList;
import java.util.Date;

public class JobDetailWSAsync extends AsyncTask<String, Void, Void> {

	Context appContext;
	String TruckRackNo;
	Date BookingDate;

	ArrayList<JobDetail> jobDetailArrayList = new ArrayList<>();

	JobDetailDataSource jobDetailDataSource;

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

		if (jobDetailArrayList.size() > 0) {

			for (int i=0; i<jobDetailArrayList.size(); i++) {

				jobDetailDataSource = new JobDetailDataSource(appContext);
				//open database
				jobDetailDataSource.open();
				//insert all the job details into sqlite
				jobDetailDataSource.insertOrUpdateJobDetails(jobDetailArrayList.get(i));
				//close database
				jobDetailDataSource.close();

				//retrieve all the ppe and ghs from web service
				PPEGHSAsync task = new PPEGHSAsync(appContext, jobDetailArrayList.get(i).getJobID(), jobDetailArrayList.get(i).getProductName());
				task.execute();
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
