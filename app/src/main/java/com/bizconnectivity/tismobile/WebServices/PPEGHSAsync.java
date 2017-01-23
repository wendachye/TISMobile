package com.bizconnectivity.tismobile.webservices;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.bizconnectivity.tismobile.classes.GHS;
import com.bizconnectivity.tismobile.classes.GHSDetail;
import com.bizconnectivity.tismobile.classes.PPE;
import com.bizconnectivity.tismobile.classes.PPEDetail;
import com.bizconnectivity.tismobile.database.datasources.GHSDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.LoadingBayDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.PPEDetailDataSource;

import java.util.ArrayList;

public class PPEGHSAsync extends AsyncTask<String, Void, Void> {

	Context context;
	String productName;
	String jobID;

	ArrayList<PPE> ppeArrayList = new ArrayList<>();
	ArrayList<GHS> ghsArrayList = new ArrayList<>();

	public PPEGHSAsync(Context context, String jobID, String productName) {

		this.context = context;
		this.jobID = jobID;
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

		//insert ppe details into sqlite database
		if (ppeArrayList.size() > 0) {
			Log.d("aa", "ppe");
			insertPPE(jobID, ppeArrayList);
		}else {
			Log.d("bb", "no ppe");
		}
		//insert ghs details into sqlite database
		if (ghsArrayList.size() > 0) {
			Log.d("aa", "ghs");
			insertGHS(jobID, ghsArrayList);
		}else {
			Log.d("bb", "no ghs");
		}
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onProgressUpdate(Void... values) {

	}

	public void insertPPE(String jobID, ArrayList<PPE> ppeArrayList) {

		ArrayList<PPEDetail> ppeDetailArrayList = new ArrayList<>();

		for (int i=0; i<ppeArrayList.size(); i++) {

			PPEDetail ppeDetail = new PPEDetail();
			String ppeURL = ppeArrayList.get(i).getPpePictureURL();
			String ppeName = ppeURL.substring(0, ppeURL.indexOf("."));

			switch (ppeName) {

				case "ear_protection":
					ppeDetail.setPpeID("1");
					break;

				case "face_shield":
					ppeDetail.setPpeID("2");
					break;

				case "foot_protection":
					ppeDetail.setPpeID("3");
					break;

				case "hand_protection":
					ppeDetail.setPpeID("4");
					break;

				case "head_protection":
					ppeDetail.setPpeID("5");
					break;

				case "mandatory_instruction":
					ppeDetail.setPpeID("6");
					break;

				case "pedestrian_route":
					ppeDetail.setPpeID("7");
					break;

				case "protective_clothing":
					ppeDetail.setPpeID("8");
					break;

				case "respirator":
					ppeDetail.setPpeID("9");
					break;

				case "safety_glasses":
					ppeDetail.setPpeID("10");
					break;

				case "safety_harness":
					ppeDetail.setPpeID("11");
					break;

				default:
					break;
			}

			ppeDetail.setJobID(jobID);
			Log.d("ppe", ppeDetail.getPpeID());
			ppeDetailArrayList.add(ppeDetail);
		}

		PPEDetailDataSource ppeDetailDataSource = new PPEDetailDataSource(context);
		ppeDetailDataSource.open();
		ppeDetailDataSource.insertOrUpdatePPE(ppeDetailArrayList);
		ppeDetailDataSource.close();
	}

	public void insertGHS(String jobID, ArrayList<GHS> ghsArrayList) {

		ArrayList<GHSDetail> ghsDetailArrayList = new ArrayList<>();

		for (int i=0; i<ghsArrayList.size(); i++) {

			GHSDetail ghsDetail = new GHSDetail();
			String ghsURL = ghsArrayList.get(i).getGhsPictureURL();
			String ghsName = ghsURL.substring(0, ghsURL.indexOf("."));

			switch (ghsName) {

				case "acute_toxicity":
					ghsDetail.setGhsID("1");
					break;

				case "aspiration_toxicity":
					ghsDetail.setGhsID("2");
					break;

				case "corrosive":
					ghsDetail.setGhsID("3");
					break;

				case "environment_toxicity":
					ghsDetail.setGhsID("4");
					break;

				case "explosive":
					ghsDetail.setGhsID("5");
					break;

				case "flammable":
					ghsDetail.setGhsID("6");
					break;

				case "gases_under_pressure":
					ghsDetail.setGhsID("7");
					break;

				case "irritant":
					ghsDetail.setGhsID("8");
					break;

				case "oxidiser":
					ghsDetail.setGhsID("9");
					break;

				default:
					break;
			}

			ghsDetail.setJobID(jobID);
			Log.d("ppe", ghsDetail.getGhsID());
			ghsDetailArrayList.add(ghsDetail);
		}

		GHSDetailDataSource ghsDetailDataSource = new GHSDetailDataSource(context);
		ghsDetailDataSource.open();
		ghsDetailDataSource.insertOrUpdateGHS(ghsDetailArrayList);
		ghsDetailDataSource.close();

	}
}
