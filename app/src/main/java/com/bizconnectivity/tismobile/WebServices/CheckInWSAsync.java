package com.bizconnectivity.tismobile.webservices;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.activities.CheckInActivity;
import com.bizconnectivity.tismobile.classes.CheckIn;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.datasources.LoadingBayDetailDataSource;

import static com.bizconnectivity.tismobile.Constant.ERR_MSG_TRUCK_BAY_ALREADY_CHECKED_IN;

public class CheckInWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String rackNo;
	boolean response;
	boolean returnResult;
	LoadingBayDetailDataSource loadingBayDetailDataSource;
	CheckIn checkIn;

	ProgressDialog progressDialog;

	public CheckInWSAsync(Context context, String rackNo) {

		this.context = context;
		this.rackNo = rackNo;
	}

	@Override
	protected Void doInBackground(String... params) {

		response = CheckInWS.invokeCheckTruckRack(rackNo);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		if (response) {

			//region insert loading bay to database
			checkIn = new CheckIn();
			loadingBayDetailDataSource = new LoadingBayDetailDataSource(context);

			checkIn.setLoadingBayNo(rackNo);
			loadingBayDetailDataSource.open();
			returnResult = loadingBayDetailDataSource.insertLoadingBayNo(context, checkIn);
			loadingBayDetailDataSource.close();

			if (!returnResult) {

				Common.shortToast(context, ERR_MSG_TRUCK_BAY_ALREADY_CHECKED_IN);
			}
			//endregion

			//get all the job details from web service
			JobDetailWSAsync task = new JobDetailWSAsync(context, rackNo);
        	task.execute();

			//close progress dialog
			progressDialog.dismiss();

			//navigate back to checkIn Activity
			Intent intent = new Intent(context, CheckInActivity.class);
			((CheckInActivity) context).finish();
			context.startActivity(intent);

		} else {

			//close progress dialog
			progressDialog.dismiss();

			Common.shortToast(context, Constant.ERR_MSG_INVALID_TRUCK_BAY);
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
