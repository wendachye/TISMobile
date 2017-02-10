package com.bizconnectivity.tismobile.activities;

import android.app.Dialog;
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
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.adapters.SearchResultAdapter;
import com.bizconnectivity.tismobile.classes.JobDetail;
import com.bizconnectivity.tismobile.database.datasources.LoadingBayDetailDataSource;

import java.util.ArrayList;

import static com.bizconnectivity.tismobile.Common.formatWelcomeMsg;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOGIN_NAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_NAME;

public class SearchResultActivity extends AppCompatActivity implements SearchResultAdapter.AdapterCallBack{

	//region declaration
	ImageButton btnHome, btnSearch, btnSwitch, btnSettings;
	TextView headerMessage;
	Dialog exitDialog;
	RecyclerView recyclerView;
	LinearLayout footerLayout;
	LinearLayout headerLayout;
	SharedPreferences sharedPref;
	SearchResultAdapter searchResultAdapter;
	LoadingBayDetailDataSource loadingBayDetailDataSource;
	ArrayList<JobDetail> jobDetailArrayList;
	boolean isActivityStarted = false;
	//endregion

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);

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

		jobDetailArrayList = (ArrayList<JobDetail>) getIntent().getSerializableExtra("jobDetailArrayList");

		recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		searchResultAdapter = new SearchResultAdapter(this, jobDetailArrayList, R.layout.list_view_search_result, this);
		recyclerView.setAdapter(searchResultAdapter);
	}

	//region Header
    /*-------- Set User Login Details --------*/
	public void setUserLoginDetails() {

		headerLayout = (LinearLayout) findViewById(R.id.headerResult);

		headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);
		headerMessage.setText(formatWelcomeMsg(sharedPref.getString(SHARED_PREF_LOGIN_NAME, "")));
	}
	//endregion

	//region Footer
	public void setFooterMenu() {

		footerLayout = (LinearLayout) findViewById(R.id.footer);

		btnHome = (ImageButton) footerLayout.findViewById(R.id.button_home);
		btnHome.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				btnHomeClicked();
			}
		});

		btnSearch = (ImageButton) footerLayout.findViewById(R.id.button_search);
		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				btnSearchClicked();
			}
		});

		btnSwitch = (ImageButton) footerLayout.findViewById(R.id.button_switch);
		btnSwitch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				btnSwitchClicked();
			}
		});

		btnSettings = (ImageButton) footerLayout.findViewById(R.id.button_settings);
		btnSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				btnSettingsClicked(view);
			}
		});
	}

	public void btnSearchClicked() {

		Intent intent = new Intent(this, SearchJobActivity.class);
		isActivityStarted = true;
		startActivity(intent);
	}

	public void btnHomeClicked() {

		Intent intent = new Intent(this, DashboardActivity.class);
		isActivityStarted = true;
		startActivity(intent);
	}

	public void btnSwitchClicked() {

		Intent intent = new Intent(this, SwitchJobActivity.class);
		isActivityStarted = true;
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

					case R.id.menu_check_in:
						Intent intentCheckIn = new Intent(getApplicationContext(), CheckInActivity.class);
						isActivityStarted = true;
						startActivity(intentCheckIn);
						return true;

					case R.id.menu_exit:
						exitApplication();
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
		Button btnConfirm = (Button) exitDialog.findViewById(R.id.button_confirm);
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
				intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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
	//endregion

	@Override
	public void onBackPressed() {

		Intent intent = new Intent(this, SearchJobActivity.class);
		isActivityStarted = true;
		startActivity(intent);
	}

	@Override
	public void adapterOnClick(int adapterPosition) {

	}

	@Override
	protected void onStop() {

		super.onStop();

		if (isActivityStarted) {

			finish();
		}
	}
}
