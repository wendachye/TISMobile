package com.bizconnectivity.tismobile.webservices;

import android.content.Context;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.classes.JobDetail;
import com.bizconnectivity.tismobile.database.DataSources.JobDetailDataSource;

import java.util.ArrayList;
import java.util.Date;

public class JobDetailWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String truckRackNo;
	Date bookingDate;

	ArrayList<JobDetail> jobDetailArrayList = new ArrayList<>();

	JobDetailDataSource jobDetailDataSource;

	public JobDetailWSAsync(Context context, Date bookingDate, String truckRackNo) {

		this.context = context;
		this.bookingDate = bookingDate;
		this.truckRackNo = truckRackNo;
	}

	@Override
	protected Void doInBackground(String... params) {

		jobDetailArrayList = JobDetailWS.invokeRetrieveAllJobs(bookingDate, truckRackNo);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		if (jobDetailArrayList.size() > 0) {

			for (int i=0; i<jobDetailArrayList.size(); i++) {

				jobDetailDataSource = new JobDetailDataSource(context);
				//open database
				jobDetailDataSource.open();
				//insert all the job details into sqlite
				jobDetailDataSource.insertOrUpdateJobDetails(jobDetailArrayList.get(i));
				//close database
				jobDetailDataSource.close();

				//retrieve all the ppe and ghs from web service
				PPEGHSAsync task = new PPEGHSAsync(context, jobDetailArrayList.get(i).getJobID(), jobDetailArrayList.get(i).getProductName());
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
