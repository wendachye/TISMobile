package com.bizconnectivity.tismobile.webservices;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.widget.Button;

import com.bizconnectivity.tismobile.activities.JobMainActivity;
import com.bizconnectivity.tismobile.classes.GHS;
import com.bizconnectivity.tismobile.classes.PPE;
import com.bizconnectivity.tismobile.R;

import java.util.ArrayList;

public class PPEWSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	Button buttonPPE;
	String productName;
	boolean isOffline;
	ArrayList<PPE> ppeArrayList = new ArrayList<>();
	ArrayList<GHS> ghsArrayList = new ArrayList<>();
	ProgressDialog progressDialog;

	public PPEWSAsync(Context context, boolean isOffline, Button buttonPPE, String productName) {

		this.context = context;
		this.isOffline = isOffline;
		this.buttonPPE = buttonPPE;
		this.productName = productName;
	}


	@Override
	protected Void doInBackground(String... params) {

		ppeArrayList = PPEWS.invokeRetrievePPEWS(productName);
		ghsArrayList = GHSWS.invokeRetrieveGHSWS(productName);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {


		if (ppeArrayList.size() > 0 || ghsArrayList.size() > 0) {

			//close progress dialog
			progressDialog.dismiss();

			((JobMainActivity) context).ppeDialog(isOffline, ppeArrayList, ghsArrayList);

		} else {

			//close progress dialog
			progressDialog.dismiss();

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
			alertDialog.setTitle("No PPE");
			alertDialog.setMessage("This product don't have PPE. Please contact the Safety Department.");
			alertDialog.setIcon(R.drawable.ic_alert_circle_outline);
			alertDialog.setPositiveButton("OK", null);
			alertDialog.show();

			buttonPPE.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
			buttonPPE.setBackgroundColor(ContextCompat.getColor(context, R.color.colorGreen));
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
