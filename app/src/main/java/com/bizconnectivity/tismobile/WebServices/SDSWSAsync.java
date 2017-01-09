package com.bizconnectivity.tismobile.WebServices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Button;

import com.bizconnectivity.tismobile.Activities.JobMainActivity;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.R;

public class SDSWSAsync extends AsyncTask<String, Void, Void> {

	Context appContext;
	int TimeslotID;

	String SDSPDFURL;
	Button ButtonSDS;

	public SDSWSAsync(Context context, Button buttonSDS, int timeslotID) {
		appContext = context;
		ButtonSDS = buttonSDS;
		TimeslotID = timeslotID;
	}

	@Override
	protected Void doInBackground(String... params) {

		SDSPDFURL = SDSWS.invokeRetrieveSDSWS(TimeslotID);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = appContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (!SDSPDFURL.isEmpty()) {

			String URL = Constant.SDS_FILE_LOCATION + SDSPDFURL;

			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
			appContext.startActivity(browserIntent);

			editor.putString(Constant.SHARED_PREF_SDS_PDF_URL, SDSPDFURL).commit();
			editor.putString(Constant.SHARED_PREF_SDS, "done").commit();

			ButtonSDS.setTextColor(appContext.getResources().getColor(R.color.colorWhite));
			ButtonSDS.setBackgroundColor(appContext.getResources().getColor(R.color.colorGreen));

		} else {

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(appContext);
			alertDialog.setTitle("No SDS");
			alertDialog.setMessage("This product don't have SDS. Please contact the Safety Department.");
			alertDialog.setIcon(R.drawable.ic_alert_circle_outline);
			alertDialog.setPositiveButton("OK", null);
			alertDialog.show();

			editor.putString(Constant.SHARED_PREF_SDS, "done").commit();

			ButtonSDS.setTextColor(appContext.getResources().getColor(R.color.colorWhite));
			ButtonSDS.setBackgroundColor(appContext.getResources().getColor(R.color.colorGreen));

		}

	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onProgressUpdate(Void... values) {

	}
}
