package com.bizconnectivity.tismobile.webservices;

import android.content.Context;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.classes.JobDetail;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;

import java.util.ArrayList;

public class JobDetailWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String truckRackNo;
	ArrayList<JobDetail> jobDetailArrayList;
	JobDetailDataSource jobDetailDataSource;

	public JobDetailWSAsync(Context context, String truckRackNo) {

		this.context = context;
		this.truckRackNo = truckRackNo;
	}

	@Override
	protected Void doInBackground(String... params) {

		jobDetailArrayList = new ArrayList<>();
//		jobDetailArrayList = JobDetailWS.invokeRetrieveAllJobs(truckRackNo);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		if (jobDetailArrayList.size() > 0) {

			for (int i=0; i<jobDetailArrayList.size(); i++) {

				//insert or update database
				jobDetailDataSource = new JobDetailDataSource(context);

				jobDetailDataSource.open();
				jobDetailDataSource.insertOrUpdateJobDetails(jobDetailArrayList.get(i));
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
