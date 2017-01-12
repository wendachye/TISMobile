package com.bizconnectivity.tismobile.WebServices;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import com.bizconnectivity.tismobile.Activities.JobMainActivity;
import com.bizconnectivity.tismobile.Classes.GHS;
import com.bizconnectivity.tismobile.Classes.PPE;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.R;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PPEWSAsync extends AsyncTask<String, Void, Void> {

	String ProductName;
	Context appContext;

	ArrayList<PPE> ppeArrayList = new ArrayList<>();
	ArrayList<GHS> ghsArrayList = new ArrayList<>();

	Button ButtonPPE;

	public PPEWSAsync(Context context, Button buttonPPE, String productName) {
		appContext = context;
		ButtonPPE = buttonPPE;
		ProductName = productName;
	}


	@Override
	protected Void doInBackground(String... params) {

		ppeArrayList = PPEWS.invokeRetrievePPEWS(ProductName);
		ghsArrayList = GHSWS.invokeRetrieveGHSWS(ProductName);

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		SharedPreferences sharedPref = appContext.getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		if (ppeArrayList.size() > 0 || ghsArrayList.size() > 0) {

//            editor.putString(Constant.SHARED_PREF_PPE_PICTURE_URL, ppePictureURL).commit();

			((JobMainActivity) appContext).ppeDialog(ppeArrayList, ghsArrayList);

		} else {

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(appContext);
			alertDialog.setTitle("No PPE");
			alertDialog.setMessage("This product don't have PPE. Please contact the Safety Department.");
			alertDialog.setIcon(R.drawable.ic_alert_circle_outline);
			alertDialog.setPositiveButton("OK", null);
			alertDialog.show();

			editor.putString(Constant.SHARED_PREF_PPE, "done").commit();

			ButtonPPE.setTextColor(appContext.getResources().getColor(R.color.colorWhite));
			ButtonPPE.setBackgroundColor(appContext.getResources().getColor(R.color.colorGreen));

		}

	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onProgressUpdate(Void... values) {

	}
}
