package com.bizconnectivity.tismobile.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.adapters.CustomExpandableListAdapter;
import com.bizconnectivity.tismobile.classes.JobDetail;
import com.bizconnectivity.tismobile.classes.LoadingBayList;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.LoadingBayDetailDataSource;
import com.bizconnectivity.tismobile.R;

import java.util.ArrayList;

import static com.bizconnectivity.tismobile.Common.formatCheckedInTruckLoadingBay;
import static com.bizconnectivity.tismobile.Common.formatWelcomeMsg;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_CUSTOMER_NAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_DRIVER_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_DATE;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_STATUS;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOADING_ARM;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOADING_BAY;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOGINNAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_NAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_OPERATOR_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_PRODUCT_NAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_PUMP_START_TIME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_PUMP_STOP_TIME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_RACK_OUT_TIME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_SDS_FILE_PATH;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_TANK_NO;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_WORK_INSTRUCTION;

public class DashboardActivity extends AppCompatActivity {

    //region declaration
    ImageButton btnHome, btnSearch, btnSwitch, btnSettings;
    TextView headerMessage, tvTruckBayTitle, tvLoadingBayOrderId;
    Dialog exitDialog;
    LinearLayout footerLayout;
    LinearLayout headerLayout;
    SharedPreferences sharedPref;
    ExpandableListView expandableListView;
    CustomExpandableListAdapter customExpandableListAdapter;
    LoadingBayDetailDataSource loadingBayDetailDataSource;
    JobDetailDataSource jobDetailDataSource;
    LoadingBayList loadingBayLists;
    JobDetail jobDetail;
    ArrayList<LoadingBayList> loadingBayArraylist;
    ArrayList<JobDetail> childArrayList;
    ArrayList<String> groupArrayList;
    String jobID, loadingBay;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //region expandable list view settings
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);

        loadingBayArraylist = new ArrayList<>();
        loadingBayArraylist = retrieveAllLoadingBay();

        customExpandableListAdapter = new CustomExpandableListAdapter(this, loadingBayArraylist);
        expandableListView.setAdapter(customExpandableListAdapter);

        //expand all the list view at the first time
        for (int i=0; i<customExpandableListAdapter.getGroupCount(); i++ ) {

            expandableListView.expandGroup(i);
        }
        //endregion

        //region expandable list view child onclick
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                //region retrieve job details
                tvLoadingBayOrderId = (TextView) view.findViewById(R.id.tvLoadingBayOrderId);
                jobID = tvLoadingBayOrderId.getText().toString();

                jobDetail = new JobDetail();
                jobDetailDataSource = new JobDetailDataSource(getApplicationContext());

                jobDetailDataSource.open();
                jobDetail = jobDetailDataSource.retrieveJobDetails(jobID);
                jobDetailDataSource.close();
                //endregion

                //store shared preferences
                storeJobDetailSharedPref(jobDetail);

                //start job main activity
                Intent intent = new Intent(getApplicationContext(), JobMainActivity.class);
                finish();
                startActivity(intent);

                return true;
            }
        });
        //endregion

        //region expandable list view group onclick
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {

                if (parent.isGroupExpanded(groupPosition)) {

                    parent.collapseGroup(groupPosition);

                } else {

                    parent.expandGroup(groupPosition);
                }

                return true;
            }
        });
        //endregion

        //region Header and Footer
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        /*-------- Set User Login Details --------*/
        setUserLoginDetails();
        setLoadingBayNo();

        /*-------- Footer Buttons --------*/
        setFooterMenu();
        //endregion
    }

    public ArrayList<LoadingBayList> retrieveAllLoadingBay() {

        loadingBayArraylist = new ArrayList<>();

        //region retrieve all loading bay
        groupArrayList = new ArrayList<>();
        loadingBayDetailDataSource = new LoadingBayDetailDataSource(this);

        loadingBayDetailDataSource.open();
        groupArrayList = loadingBayDetailDataSource.retrieveAllLoadingBay();
        loadingBayDetailDataSource.close();

        //endregion

        //region retrieve all job details
        if (groupArrayList.size() > 0) {

            for (int i = 0; i<groupArrayList.size(); i++) {

                childArrayList = new ArrayList<>();
                loadingBayLists = new LoadingBayList();

                jobDetailDataSource = new JobDetailDataSource(this);

                jobDetailDataSource.open();
                childArrayList = jobDetailDataSource.retrieveAllPendingJobDetails(groupArrayList.get(i));
                jobDetailDataSource.close();

                //group title setter
                loadingBayLists.setGroupTitle(groupArrayList.get(i));

                //child details setter
                loadingBayLists.setJobDetails(childArrayList);

                loadingBayArraylist.add(loadingBayLists);
            }
        }
        //endregion

        return loadingBayArraylist;
    }

    public void storeJobDetailSharedPref(JobDetail jobDetail) {

        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString(SHARED_PREF_JOB_ID, jobDetail.getJobID());
        editor.putString(SHARED_PREF_CUSTOMER_NAME, jobDetail.getCustomerName());
        editor.putString(SHARED_PREF_PRODUCT_NAME, jobDetail.getProductName());
        editor.putString(SHARED_PREF_TANK_NO, jobDetail.getTankNo());
        editor.putString(SHARED_PREF_LOADING_BAY, jobDetail.getLoadingBayNo());
        editor.putString(SHARED_PREF_LOADING_ARM, jobDetail.getLoadingArm());
        editor.putString(SHARED_PREF_SDS_FILE_PATH, jobDetail.getSdsFilePath());
        editor.putString(SHARED_PREF_OPERATOR_ID, jobDetail.getOperatorID());
        editor.putString(SHARED_PREF_DRIVER_ID, jobDetail.getDriverID());
        editor.putString(SHARED_PREF_WORK_INSTRUCTION, jobDetail.getWorkInstruction());
        editor.putString(SHARED_PREF_PUMP_START_TIME, jobDetail.getPumpStartTime());
        editor.putString(SHARED_PREF_PUMP_STOP_TIME, jobDetail.getPumpStopTime());
        editor.putString(SHARED_PREF_RACK_OUT_TIME, jobDetail.getRackOutTime());
        editor.putString(SHARED_PREF_JOB_STATUS, jobDetail.getJobStatus());
        editor.putString(SHARED_PREF_JOB_DATE, jobDetail.getJobDate());
        editor.apply();
    }

    //region Header
    /*-------- Set User Login Details --------*/
    public void setUserLoginDetails() {

        headerLayout = (LinearLayout) findViewById(R.id.headerDashboard);

        headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);
        headerMessage.setText(formatWelcomeMsg(sharedPref.getString(SHARED_PREF_LOGINNAME, "")));
    }

    public void setLoadingBayNo() {

        tvTruckBayTitle = (TextView) findViewById(R.id.tvTruckBayTitle);

        //region check loading bay

        loadingBay = "";

        if (!groupArrayList.isEmpty()) {

            for (int i = 0; i < groupArrayList.size(); i++) {

                if (loadingBay.isEmpty()) {

                    loadingBay = groupArrayList.get(i);

                } else {

                    loadingBay = loadingBay + ", " + groupArrayList.get(i);
                }
            }
        }
        //endregion

        tvTruckBayTitle.setText(formatCheckedInTruckLoadingBay(loadingBay));
    }
    //endregion

    //region Footer
    public void setFooterMenu() {

        footerLayout = (LinearLayout) findViewById(R.id.footer);

        btnHome = (ImageButton) footerLayout.findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                finish();
                startActivity(intent);
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

        exitApplication();
    }


}
