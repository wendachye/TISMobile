package com.bizconnectivity.tismobile.WebServices;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.Classes.GHS;
import com.bizconnectivity.tismobile.Constant;

import java.util.ArrayList;

public class GHSWSAsync extends AsyncTask<String, Void, Void> {

	String ProductName;
	Context appContext;

	ArrayList<GHS> ghsArrayList;

	public GHSWSAsync(Context context, String productName) {
		appContext = context;
		ProductName = productName;
	}


	@Override
	protected Void doInBackground(String... params) {

		ghsArrayList = GHSWS.invokeRetrieveGHSWS(ProductName);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = appContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (ghsArrayList.size() < 0) {


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
