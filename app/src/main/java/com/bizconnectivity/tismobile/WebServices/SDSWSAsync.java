package com.bizconnectivity.tismobile.webservices;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Button;

import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.DataSources.JobDetailDataSource;
import com.bizconnectivity.tismobile.R;

public class SDSWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String jobID;
	String SDSPDFURL;
	Button buttonSDS;

	ProgressDialog progressDialog;

	JobDetailDataSource jobDetailDataSource;

	public SDSWSAsync(Context context, Button buttonSDS, String jobID) {

		this.context = context;
		this.buttonSDS = buttonSDS;
		this.jobID = jobID;
	}

	@Override
	protected Void doInBackground(String... params) {

		SDSPDFURL = SDSWS.invokeRetrieveSDSWS(jobID);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = context.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (!SDSPDFURL.isEmpty()) {

			//button sds setup
			buttonSDS.setEnabled(true);
			buttonSDS.setTextColor(context.getResources().getColor(R.color.colorWhite));
			buttonSDS.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));

			//update job status
			editor.putString(Constant.SHARED_PREF_JOB_STATUS, Constant.STATUS_SDS).apply();

			jobDetailDataSource = new JobDetailDataSource(context);
			jobDetailDataSource.open();
			jobDetailDataSource.updateJobDetails(sharedPref.getString(Constant.SHARED_PREF_JOB_ID, ""), Constant.STATUS_SDS);
			jobDetailDataSource.close();

			//end progress dialog
			progressDialog.dismiss();

			//full url path of sds file
			String URL = Constant.SDS_FILE_LOCATION + SDSPDFURL;

			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
			context.startActivity(browserIntent);

		} else {

			//end progress dialog
			progressDialog.dismiss();

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
			alertDialog.setTitle("No SDS");
			alertDialog.setMessage("This product don't have SDS. Please contact the Safety Department.");
			alertDialog.setIcon(R.drawable.ic_alert_circle_outline);
			alertDialog.setPositiveButton("OK", null);
			alertDialog.show();

			buttonSDS.setTextColor(context.getResources().getColor(R.color.colorWhite));
			buttonSDS.setBackgroundColor(context.getResources().getColor(R.color.colorGreen));

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
