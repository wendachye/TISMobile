package com.bizconnectivity.tismobile.webservices;


import android.content.Context;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.classes.GHS;
import com.bizconnectivity.tismobile.classes.GHSDetail;
import com.bizconnectivity.tismobile.classes.PPE;
import com.bizconnectivity.tismobile.classes.PPEDetail;
import com.bizconnectivity.tismobile.database.DataSources.GHSDetailDataSource;
import com.bizconnectivity.tismobile.database.DataSources.PPEDetailDataSource;

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

			insertPPE(jobID, ppeArrayList);
		}
		//insert ghs details into sqlite database
		if (ghsArrayList.size() > 0) {

			insertGHS(jobID, ghsArrayList);
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
			int ppeID = 0;
			String ppeURL = ppeArrayList.get(i).getPpePictureURL();
			String ppeName = ppeURL.substring(0, ppeURL.indexOf("."));

			switch (ppeName) {

				case "ear_protection":
					ppeID = 1;
					break;

				case "face_shield":
					ppeID = 2;
					break;

				case "foot_protection":
					ppeID = 3;
					break;

				case "hand_protection":
					ppeID = 4;
					break;

				case "head_protection":
					ppeID = 5;
					break;

				case "mandatory_instruction":
					ppeID = 6;
					break;

				case "pedestrian_route":
					ppeID = 7;
					break;

				case "protective_clothing":
					ppeID = 8;
					break;

				case "respirator":
					ppeID = 9;
					break;

				case "safety_glasses":
					ppeID = 10;
					break;

				case "safety_harness":
					ppeID = 11;
					break;

				default:
					break;
			}

			ppeDetail.setJobID(jobID);
			ppeDetail.setPpeID(ppeID);

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
			int ghsID = 0;
			String ghsURL = ghsArrayList.get(i).getGhsPictureURL();
			String ghsName = ghsURL.substring(0, ghsURL.indexOf("."));

			switch (ghsName) {

				case "AcuteToxicity":
					ghsID = 1;
					break;

				case "AspirationToxicity":
					ghsID = 2;
					break;

				case "Corrosive":
					ghsID = 3;
					break;

				case "EnvironmentToxicity":
					ghsID = 4;
					break;

				case "Explosive":
					ghsID = 5;
					break;

				case "Flammable":
					ghsID = 6;
					break;

				case "GasesUnderPressure":
					ghsID = 7;
					break;

				case "Irritant":
					ghsID = 8;
					break;

				case "Oxidiser":
					ghsID = 9;
					break;

				default:
					break;
			}

			ghsDetail.setJobID(jobID);
			ghsDetail.setGhsID(ghsID);

			ghsDetailArrayList.add(ghsDetail);
		}

		GHSDetailDataSource ghsDetailDataSource = new GHSDetailDataSource(context);
		ghsDetailDataSource.open();
		ghsDetailDataSource.insertOrUpdateGHS(ghsDetailArrayList);
		ghsDetailDataSource.close();

	}
}
