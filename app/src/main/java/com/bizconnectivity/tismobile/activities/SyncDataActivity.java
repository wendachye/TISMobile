package com.bizconnectivity.tismobile.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
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

import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.adapters.SyncDataAdapter;
import com.bizconnectivity.tismobile.database.models.GHSDetail;
import com.bizconnectivity.tismobile.database.models.JobDetail;
import com.bizconnectivity.tismobile.database.models.LoadingBayDetail;
import com.bizconnectivity.tismobile.database.models.PPEDetail;
import com.bizconnectivity.tismobile.database.models.SealDetail;
import com.bizconnectivity.tismobile.webservices.AddSealWS;
import com.bizconnectivity.tismobile.webservices.DepartureWS;
import com.bizconnectivity.tismobile.webservices.PumpStartWS;
import com.bizconnectivity.tismobile.webservices.PumpStopWS;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.bizconnectivity.tismobile.Common.*;
import static com.bizconnectivity.tismobile.Constant.*;

public class SyncDataActivity extends AppCompatActivity{

	//region declaration

	//header
	@BindView(R.id.header_sync)
	LinearLayout mLinearLayoutHeader;
	@BindView(R.id.text_header)
	TextView mTextViewHeader;

	//content
	@BindView(R.id.recycler_view)
	RecyclerView mRecyclerView;

	//footer
	@BindView(R.id.footer_sync)
	LinearLayout mLinearLayoutFooter;
	@BindView(R.id.button_home)
	ImageButton mImageButtonHome;
	@BindView(R.id.button_search)
	ImageButton mImageButtonSearch;
	@BindView(R.id.button_switch)
	ImageButton mImageButtonSwitch;
	@BindView(R.id.button_settings)
	ImageButton mImageButtonSettings;

	Realm realm;
	Dialog exitDialog;
	PopupMenu popupMenu;
	SharedPreferences sharedPref;
	boolean isActivityStarted = false;
	ProgressDialog progressDialog;
	//endregion

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_data);

		ButterKnife.bind(this);
		realm = Realm.getDefaultInstance();
		sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

		//action bar
		assert getSupportActionBar() != null;
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //header
		mTextViewHeader.setText(formatWelcomeMsg(sharedPref.getString(SHARED_PREF_LOGIN_NAME, "")));

		//recycler view setup
		mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
		mRecyclerView.setAdapter(new SyncDataAdapter(this, realm.where(JobDetail.class).notEqualTo("rackOutTime", "").findAll()));
		mRecyclerView.setHasFixedSize(true);
	}

	//region Menu
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
		if (item.getItemId() == R.id.menu_refresh) {

			if (realm.where(JobDetail.class).notEqualTo("rackOutTime", "").count() > 0) {

				if (isNetworkAvailable(this)) {

					//display progress dialog
					progressDialog = ProgressDialog.show(SyncDataActivity.this, "Please wait..", "Loading...", true);

					realm.executeTransactionAsync(new Realm.Transaction() {
						@Override
						public void execute(Realm realm) {

							String updatedBy = sharedPref.getString(SHARED_PREF_LOGIN_NAME, "");

							for (JobDetail results : realm.where(JobDetail.class).notEqualTo("rackOutTime", "").findAll()) {

								PumpStartWS.invokeUpdatePumpStartWS(results.getJobID(), results.getPumpStartTime(), updatedBy);
								PumpStopWS.invokeUpdatePumpStopWS(results.getJobID(), results.getPumpStopTime(), updatedBy);
								DepartureWS.invokeAddDepartureWS(results.getJobID(), results.getRackOutTime(), updatedBy);

								for (SealDetail seals : realm.where(SealDetail.class).equalTo("jobID", results.getJobID()).equalTo("status", "Used").findAll()) {

									AddSealWS.invokeAddSealWS(seals.getSealNo(), results.getJobID(), "bottom", updatedBy);
								}

								realm.where(GHSDetail.class).equalTo("jobID", results.getJobID()).findAll().deleteAllFromRealm();
								realm.where(PPEDetail.class).equalTo("jobID", results.getJobID()).findAll().deleteAllFromRealm();
								realm.where(SealDetail.class).equalTo("jobID", results.getJobID()).findAll().deleteAllFromRealm();
								realm.where(JobDetail.class).equalTo("jobID", results.getJobID()).notEqualTo("rackOutTime", "").findAll().deleteAllFromRealm();
							}
						}
					}, new Realm.Transaction.OnSuccess() {
						@Override
						public void onSuccess() {

							//close progress dialog
							progressDialog.dismiss();

							//show success message
							longToast(getApplicationContext(), SUCCESS_SYNC);
						}
					}, new Realm.Transaction.OnError() {
						@Override
						public void onError(Throwable error) {

							//close progress dialog
							progressDialog.dismiss();

							//show error message
							longToast(getApplicationContext(), FAIL_SYNC);
						}
					});

				} else {

					shortToast(this, NO_INTERNET);
				}

			} else {

				shortToast(this, NO_DATA_SYNC);
			}

			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	//endregion

	//region Footer
	@OnClick(R.id.button_home)
	public void btnHome(View view) {

		Intent intent = new Intent(this, DashboardActivity.class);
		isActivityStarted = true;
		startActivity(intent);
	}

	@OnClick(R.id.button_search)
	public void btnSearch(View view) {

		Intent intent = new Intent(this, SearchJobActivity.class);
		isActivityStarted = true;
		startActivity(intent);
	}

	@OnClick(R.id.button_switch)
	public void btnSwitch(View view) {

		Intent intent = new Intent(this, SwitchJobActivity.class);
		isActivityStarted = true;
		startActivity(intent);
	}

	@OnClick(R.id.button_settings)
	public void btnSettings(View view) {

		popupMenu = new PopupMenu(this, view);

		// This activity implements OnMenuItemClickListener
		popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {

				switch (item.getItemId()) {

					case R.id.menu_check_in:
						Intent intentCheckIn = new Intent(getApplicationContext(), CheckInActivity.class);
						isActivityStarted = true;
						startActivity(intentCheckIn);
						return true;

					case R.id.menu_check_out:
						Intent intentCheckOut = new Intent(getApplicationContext(), CheckOutActivity.class);
						isActivityStarted = true;
						startActivity(intentCheckOut);
						return true;

					case R.id.menu_sync_data:
						Intent intentSyncData = new Intent(getApplicationContext(), SyncDataActivity.class);
						isActivityStarted = true;
						startActivity(intentSyncData);
						return true;

					case R.id.menu_exit:
						exitApplication();
						return true;

					default:
						return false;
				}
			}
		});
		popupMenu.inflate(R.menu.settings_menu);
		popupMenu.show();
	}

	public void exitApplication() {

		if (exitDialog != null && exitDialog.isShowing()) return;

		exitDialog = new Dialog(this);
		exitDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		exitDialog.setContentView(R.layout.dialog_exit_app);

		//region button confirm
		Button btnConfirm = (Button) exitDialog.findViewById(R.id.button_confirm);
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//clear all shared preferences
				SharedPreferences.Editor editor = sharedPref.edit();
				editor.clear().apply();

				//check out all loading bay
				realm.executeTransaction(new Realm.Transaction() {
					@Override
					public void execute(Realm realm) {

						for (LoadingBayDetail results : realm.where(LoadingBayDetail.class).equalTo("status", LOADING_BAY_NO_CHECK_IN).findAll()) {

							LoadingBayDetail loadingBayDetail = realm.where(LoadingBayDetail.class).equalTo("loadingBayNo", results.getLoadingBayNo()).findFirst();
							loadingBayDetail.setStatus(LOADING_BAY_NO_CHECK_OUT);

							realm.copyToRealmOrUpdate(loadingBayDetail);
						}
					}
				});

				//close exit dialog
				exitDialog.dismiss();

				//clear all activity and start login activity
				Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
				intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				isActivityStarted = true;
				startActivity(intentLogin);
			}
		});
		//endregion

		//region button cancel
		Button btnCancel = (Button) exitDialog.findViewById(R.id.button_cancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				exitDialog.dismiss();
			}
		});
		//endregion

		int dividerId = exitDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		View divider = exitDialog.findViewById(dividerId);
		if (divider != null) divider.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent));
		assert exitDialog.getWindow() != null;
		exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		exitDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		exitDialog.show();
	}
	//endregion

	@Override
	public void onBackPressed() {

		exitApplication();
	}

	@Override
	protected void onStop() {

		super.onStop();

		if (isActivityStarted) {

			finish();
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		mRecyclerView.setAdapter(null);
		realm.close();
	}
}
