package com.bizconnectivity.tismobile.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.adapters.SearchResultAdapter;
import com.bizconnectivity.tismobile.adapters.SyncDataAdapter;
import com.bizconnectivity.tismobile.classes.JobDetail;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.LoadingBayDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.SealDetailDataSource;
import com.bizconnectivity.tismobile.webservices.AddSealWS;
import com.bizconnectivity.tismobile.webservices.DepartureWS;
import com.bizconnectivity.tismobile.webservices.PumpStartWS;
import com.bizconnectivity.tismobile.webservices.PumpStopWS;

import java.util.ArrayList;

import static com.bizconnectivity.tismobile.Common.formatWelcomeMsg;
import static com.bizconnectivity.tismobile.Common.isNetworkAvailable;
import static com.bizconnectivity.tismobile.Common.shortToast;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOGINNAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_NAME;

public class SyncDataActivity extends AppCompatActivity implements SyncDataAdapter.AdapterCallBack{

	ImageButton btnHome, btnSearch, btnSwitch, btnSettings;
	TextView headerMessage;
	Dialog exitDialog;
	RecyclerView recyclerView;
	LinearLayout footerLayout;
	LinearLayout headerLayout;
	SharedPreferences sharedPref;
	LoadingBayDetailDataSource loadingBayDetailDataSource;
	JobDetailDataSource jobDetailDataSource;
	ArrayList<JobDetail> jobDetailArrayList;
	SyncDataAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_data);

		sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

		//region Header and Footer
		assert getSupportActionBar() != null;
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        /*-------- Set User Login Details --------*/
		setUserLoginDetails();

        /*-------- Footer Buttons --------*/
		setFooterMenu();
		//endregion

		jobDetailArrayList = new ArrayList<>();
		jobDetailDataSource = new JobDetailDataSource(this);
		jobDetailDataSource.open();
		jobDetailArrayList = jobDetailDataSource.retrieveUnsyncJob();
		jobDetailDataSource.close();

		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new SyncDataAdapter(this, jobDetailArrayList, R.layout.list_view_search_result, this);
		recyclerView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings_sync_data, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		//noinspection SimplifiableIfStatement
		if (item.getItemId() == R.id.btnSync) {

			if (isNetworkAvailable(getApplicationContext())) {

				String updatedBy = sharedPref.getString(SHARED_PREF_LOGINNAME, "");

				departureAsync task = new departureAsync(jobDetailArrayList, updatedBy);
				task.execute();

			} else {

				shortToast(getApplicationContext(), "No Internet Connection.");
			}

			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private class departureAsync extends AsyncTask<String, Void, Void> {

		ProgressDialog progressDialog;
		ArrayList<JobDetail> jobDetailArrayList ;
		String updatedBy;
		SealDetailDataSource sealDetailDataSource;
		ArrayList<String> sealNo;

		private departureAsync(ArrayList<JobDetail> jobDetailArrayList, String updatedBy) {

			this.jobDetailArrayList = jobDetailArrayList;
			this.updatedBy = updatedBy;
		}

		@Override
		protected void onPreExecute() {

			//start progress dialog
			progressDialog = ProgressDialog.show(SyncDataActivity.this, "Please wait..", "Loading...", true);
		}

		@Override
		protected Void doInBackground(String... params) {

			for (int i=0; i<jobDetailArrayList.size(); i++) {

				sealDetailDataSource = new SealDetailDataSource(getApplicationContext());
				sealDetailDataSource.open();
				sealNo = sealDetailDataSource.retrieveSealNo(jobDetailArrayList.get(i).getJobID());
				sealDetailDataSource.close();

				AddSealWS.invokeAddSealWS2(sealNo, jobDetailArrayList.get(i).getJobID(), "bottom", updatedBy);
				DepartureWS.invokeAddDepartureWS(jobDetailArrayList.get(i).getJobID(), jobDetailArrayList.get(i).getRackOutTime(), updatedBy);
				PumpStartWS.invokeUpdatePumpStartWS(jobDetailArrayList.get(i).getJobID(), jobDetailArrayList.get(i).getPumpStartTime(), updatedBy);
				PumpStopWS.invokeUpdatePumpStopWS(jobDetailArrayList.get(i).getJobID(), jobDetailArrayList.get(i).getPumpStopTime(), updatedBy);

				jobDetailDataSource = new JobDetailDataSource(getApplicationContext());
				jobDetailDataSource.open();
				jobDetailDataSource.deleteFinishedJob(jobDetailArrayList.get(i).getJobID());
				jobDetailDataSource.close();
			}

			return null;
		}

		@Override
		protected void onProgressUpdate(Void... values) {

		}

		@Override
		protected void onPostExecute(Void result) {


			//close progress dialog
			progressDialog.dismiss();

			Intent intent = new Intent(getApplicationContext(), SyncDataActivity.class);
			finish();
			startActivity(intent);
		}
	}

	//region Header
    /*-------- Set User Login Details --------*/
	public void setUserLoginDetails() {

		headerLayout = (LinearLayout) findViewById(R.id.headerResult);

		headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);
		headerMessage.setText(formatWelcomeMsg(sharedPref.getString(SHARED_PREF_LOGINNAME, "")));
	}
	//endregion

	//region Footer
	public void setFooterMenu() {

		footerLayout = (LinearLayout) findViewById(R.id.footer);

		btnHome = (ImageButton) footerLayout.findViewById(R.id.btnHome);
		btnHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				btnHomeClicked();
			}
		});

		btnSearch = (ImageButton) footerLayout.findViewById(R.id.btnSearch);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				btnSearchClicked();
			}
		});

		btnSwitch = (ImageButton) footerLayout.findViewById(R.id.btnSwitch);
		btnSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				btnSwitchClicked();
			}
		});

		btnSettings = (ImageButton) footerLayout.findViewById(R.id.btnSettings);
		btnSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				btnSettingsClicked(view);
			}
		});
	}

	public void btnSearchClicked() {

		Intent intent = new Intent(this, SearchJobActivity.class);
		finish();
		startActivity(intent);
	}

	public void btnHomeClicked() {

		Intent intent = new Intent(this, DashboardActivity.class);
		finish();
		startActivity(intent);
	}

	public void btnSwitchClicked() {

		Intent intent = new Intent(this, SwitchJobActivity.class);
		finish();
		startActivity(intent);
	}

	public void btnSettingsClicked(View view) {

		settingsMenuOptions(view);
	}

	public void settingsMenuOptions(View view) {

		PopupMenu popup = new PopupMenu(this, view);

		// This activity implements OnMenuItemClickListener
		popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {

				switch (item.getItemId()) {

					case R.id.settingsMenuCheckIn:
						Intent intentCheckIn = new Intent(getApplicationContext(), CheckInActivity.class);
						finish();
						startActivity(intentCheckIn);
						return true;

					case R.id.settingsMenuExitApp:
						exitApplication();
						return true;

					case R.id.settingsMenuCheckOut:
						Intent intentCheckOut = new Intent(getApplicationContext(), CheckOutActivity.class);
						finish();
						startActivity(intentCheckOut);
						return true;

					case R.id.settingsMenuSyncData:
						Intent intentSyncData = new Intent(getApplicationContext(), SyncDataActivity.class);
						finish();
						startActivity(intentSyncData);
						return true;

					default:
						return false;
				}
			}
		});
		popup.inflate(R.menu.settings_menu);
		popup.show();
	}

	public void exitApplication() {

		if (exitDialog != null && exitDialog.isShowing())
			return;

		exitDialog = new Dialog(this);
		exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		exitDialog.setContentView(R.layout.dialog_exit_app);

		//region button confirm
		Button btnConfirm = (Button) exitDialog.findViewById(R.id.btnConfirm);
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//close exit dialog
				exitDialog.dismiss();

				//clear all shared preferences
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.clear().apply();

				//delete all loading bay
				loadingBayDetailDataSource = new LoadingBayDetailDataSource(getApplicationContext());
				loadingBayDetailDataSource.deleteAllLoadingBay();

				//clear all activity and start login activity
				Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
				intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intentLogin);
			}
		});
		//endregion

		//region button cancel
		Button btnCancel = (Button) exitDialog.findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//close exit dialog
				exitDialog.dismiss();
			}
		});
		//endregion

		int dividerId = exitDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = exitDialog.findViewById(dividerId);
		if (divider != null) {
			divider.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent));
		}
		assert exitDialog.getWindow() != null;
		exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		exitDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		exitDialog.show();
	}

	@Override
	public void adapterOnClick(int adapterPosition) {

	}
	//endregion
}
