package com.bizconnectivity.tismobile.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.activities.SearchJobActivity;
import com.bizconnectivity.tismobile.activities.SearchResultActivity;
import com.bizconnectivity.tismobile.classes.JobDetail;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;

import java.util.ArrayList;

import static com.bizconnectivity.tismobile.Common.shortToast;
import static com.bizconnectivity.tismobile.Constant.MSG_CUSTOMER_NAME_NOT_FOUND;

public class CustomerNameSearchWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String customerName;
	ArrayList<JobDetail> jobDetailArrayList;
	JobDetailDataSource jobDetailDataSource;
	ProgressDialog progressDialog;

	public CustomerNameSearchWSAsync(Context context, String orderID) {

		this.context = context;
		this.customerName = orderID;
	}

	@Override
	protected Void doInBackground(String... params) {

		jobDetailArrayList = new ArrayList<>();
		jobDetailArrayList = CustomerNameSearchWS.invokeRetrieveJobDetailByCustomerName(customerName);

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
			}

			//close progress dialog
			progressDialog.dismiss();

			Intent intent = new Intent(context, SearchResultActivity.class);
			((SearchJobActivity) context).finish();
			intent.putExtra("jobDetailArrayList", jobDetailArrayList);
			context.startActivity(intent);

		} else {

			//close progress dialog
			progressDialog.dismiss();

			shortToast(context, MSG_CUSTOMER_NAME_NOT_FOUND);
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
