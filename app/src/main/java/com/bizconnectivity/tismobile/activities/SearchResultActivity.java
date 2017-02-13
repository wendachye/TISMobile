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
import com.bizconnectivity.tismobile.database.models.JobDetail;
import com.bizconnectivity.tismobile.database.models.LoadingBayDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.bizconnectivity.tismobile.Common.*;
import static com.bizconnectivity.tismobile.Constant.*;

public class SearchResultActivity extends AppCompatActivity implements SearchResultAdapter.AdapterCallBack{

	//region declaration

	//header
	@BindView(R.id.header_search_result)
	LinearLayout mLinearLayoutHeader;
	@BindView(R.id.text_header)
	TextView mTextViewHeader;

	//content
	@BindView(R.id.recycler_view)
	RecyclerView recyclerView;

	//footer
	@BindView(R.id.footer_search_result)
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
	LoadingBayDetail loadingBayDetail;
	ArrayList<JobDetail> jobDetailArrayList;
	PopupMenu popupMenu;
	Dialog exitDialog;
	SharedPreferences sharedPref;
	SearchResultAdapter searchResultAdapter;
	boolean isActivityStarted = false;

	//endregion

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_result);

		ButterKnife.bind(this);
		realm = Realm.getDefaultInstance();
		sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

		//action bar
		assert getSupportActionBar() != null;
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setIcon(R.mipmap.ic_launcher);

		//header
		mTextViewHeader.setText(formatWelcomeMsg(sharedPref.getString(SHARED_PREF_LOGIN_NAME, "")));

		//get job details from intent
		Intent intent = getIntent();
		String json = intent.getExtras().getString(KEY_SEARCH);

		//convert to array list from json
		Gson gson = new Gson();
		Type type = new TypeToken<ArrayList<JobDetail>>(){}.getType();
		jobDetailArrayList = gson.fromJson(json, type);

		//recycler view setup
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		searchResultAdapter = new SearchResultAdapter(this, jobDetailArrayList, R.layout.list_view_search_result, this);
		recyclerView.setAdapter(searchResultAdapter);
	}


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

							loadingBayDetail = new LoadingBayDetail();
							loadingBayDetail.setLoadingBayNo(results.getLoadingBayNo());
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

		Intent intent = new Intent(this, SearchJobActivity.class);
		isActivityStarted = true;
		startActivity(intent);
	}

	@Override
	public void adapterOnClick(int adapterPosition) {}

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

		realm.close();
	}
}
