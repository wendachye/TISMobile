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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.adapters.SearchResultAdapter;
import com.bizconnectivity.tismobile.classes.JobDetail;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.LoadingBayDetailDataSource;

import java.util.ArrayList;

import static com.bizconnectivity.tismobile.Common.formatWelcomeMsg;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOGINNAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_NAME;

public class SearchResultActivity extends AppCompatActivity implements SearchResultAdapter.AdapterCallBack{

	//region declaration
	ImageButton btnHome, btnSearch, btnSwitch, btnSettings;
	TextView headerMessage;
	Dialog exitDialog;
	RecyclerView recyclerView;
	RelativeLayout footerLayout;
	LinearLayout headerLayout;
	SharedPreferences sharedPref;
	SearchResultAdapter searchResultAdapter;
	JobDetailDataSource jobDetailDataSource;
	LoadingBayDetailDataSource loadingBayDetailDataSource;
	ArrayList<JobDetail> jobDetailArrayList;
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

		headerLayout = (LinearLayout) findViewById(R.id.header);

		headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);
		headerMessage.setText(formatWelcomeMsg(sharedPref.getString(SHARED_PREF_LOGINNAME, "")));
	}
	//endregion

	//region Footer
	public void setFooterMenu() {

		footerLayout = (RelativeLayout) findViewById(R.id.footer);

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
	//endregion

	@Override
	public void onBackPressed() {

		Intent intent = new Intent(this, SearchJobActivity.class);
		finish();
		startActivity(intent);
	}

	@Override
	public void adapterOnClick(int adapterPosition) {

	}
}
