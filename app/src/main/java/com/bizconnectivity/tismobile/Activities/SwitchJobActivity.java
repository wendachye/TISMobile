package com.bizconnectivity.tismobile.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.LoadingBayDetailDataSource;
import com.bizconnectivity.tismobile.R;

import java.util.ArrayList;

import static com.bizconnectivity.tismobile.Common.formatWelcomeMsg;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_CUSTOMER_NAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_DRIVER_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_DATE;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_STATUS;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOADING_ARM;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOADING_BAY;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOGINNAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_OPERATOR_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_PRODUCT_NAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_PUMP_START_TIME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_PUMP_STOP_TIME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_SDS_FILE_PATH;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_TANK_NO;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_WORK_INSTRUCTION;
import static com.bizconnectivity.tismobile.Constant.STATUS_BATCH_CONTROLLER;
import static com.bizconnectivity.tismobile.Constant.STATUS_DRIVER_ID;
import static com.bizconnectivity.tismobile.Constant.STATUS_OPERATOR_ID;
import static com.bizconnectivity.tismobile.Constant.STATUS_PENDING;
import static com.bizconnectivity.tismobile.Constant.STATUS_PPE;
import static com.bizconnectivity.tismobile.Constant.STATUS_PUMP_START;
import static com.bizconnectivity.tismobile.Constant.STATUS_PUMP_STOP;
import static com.bizconnectivity.tismobile.Constant.STATUS_SAFETY_CHECKS;
import static com.bizconnectivity.tismobile.Constant.STATUS_SCAN_LOADING_ARM;
import static com.bizconnectivity.tismobile.Constant.STATUS_SCAN_SEAL;
import static com.bizconnectivity.tismobile.Constant.STATUS_SDS;
import static com.bizconnectivity.tismobile.Constant.STATUS_WORK_INSTRUCTION;

public class SwitchJobActivity extends AppCompatActivity {

    //region declaration
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;
    TextView headerMessage, tvLoadingBayOrderId;
    Dialog exitDialog;
    LinearLayout footerLayout;
    LinearLayout headerLayout;
    SharedPreferences sharedPref;
    ExpandableListView expandableListView;
    CustomExpandableListAdapter customExpandableListAdapter;
    ArrayList<LoadingBayList> loadingBayArrayList;
    ArrayList<String> groupArrayList;
    ArrayList<JobDetail> childArrayList;
    LoadingBayDetailDataSource loadingBayDetailDataSource;
    JobDetailDataSource jobDetailDataSource;
    LoadingBayList loadingBayList;
    JobDetail jobDetail;
    String jobID;
    boolean isActivityStarted = false;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_job);

        sharedPref = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //region expandable list view settings
        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);

        loadingBayArrayList = new ArrayList<>();
        loadingBayArrayList = retrieveAllLoadingBay();

        //set custom adapter for expandable list view
        customExpandableListAdapter = new CustomExpandableListAdapter(this, loadingBayArrayList);
        expandableListView.setAdapter(customExpandableListAdapter);

        //expand all the list view at the first time
        for (int i=0; i<customExpandableListAdapter.getGroupCount(); i++) {

            expandableListView.expandGroup(i);
        }
        //endregion

        //region list view child onclick
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPostion, int childPosition, long id) {

                tvLoadingBayOrderId = (TextView) view.findViewById(R.id.tvLoadingBayOrderId);
                jobID = tvLoadingBayOrderId.getText().toString();

                jobDetail = new JobDetail();
                jobDetailDataSource = new JobDetailDataSource(getApplicationContext());

                jobDetailDataSource.open();
                jobDetail = jobDetailDataSource.retrieveJobDetails(jobID);
                jobDetailDataSource.close();

                //store shared preferences
                storeJobDetailSharedPref(jobDetail);

                //status of job for navigation
                statusNavigation(jobDetail.getJobStatus());

                return true;
            }
        });
        //endregion

        //region list view group onclick
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

        /*-------- footer buttons --------*/
        setFooterMenu();
        //endregion
    }

    public ArrayList<LoadingBayList> retrieveAllLoadingBay() {

        loadingBayArrayList = new ArrayList<>();

        //region retrieve all loading bay
        groupArrayList = new ArrayList<>();
        loadingBayDetailDataSource = new LoadingBayDetailDataSource(this);

        loadingBayDetailDataSource.open();
        groupArrayList = loadingBayDetailDataSource.retrieveAllLoadingBay();
        loadingBayDetailDataSource.close();
        //endregion

        //region retrieve all job details
        if (groupArrayList.size() > 0) {

            for (int i=0; i<groupArrayList.size(); i++) {

                childArrayList = new ArrayList<>();
                loadingBayList = new LoadingBayList();
                jobDetailDataSource = new JobDetailDataSource(this);

                jobDetailDataSource.open();
                childArrayList = jobDetailDataSource.retrieveAllStartedJobDetails(groupArrayList.get(i));
                jobDetailDataSource.close();

                //group title setter
                loadingBayList.setGroupTitle(groupArrayList.get(i));

                //child details setter
                loadingBayList.setJobDetails(childArrayList);

                loadingBayArrayList.add(loadingBayList);
            }
        }
        //endregion

        return loadingBayArrayList;
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
        editor.putString(SHARED_PREF_JOB_STATUS, jobDetail.getJobStatus());
        editor.putString(SHARED_PREF_JOB_DATE, jobDetail.getJobDate());
        editor.apply();
    }

    public void statusNavigation(String jobStatus) {

        switch (jobStatus) {

            case STATUS_PENDING:

                Intent intent = new Intent(this, JobMainActivity.class);
                isActivityStarted = true;
                startActivity(intent);
                break;

            case STATUS_PPE:

                Intent intentPPE = new Intent(this, JobMainActivity.class);
                isActivityStarted = true;
                startActivity(intentPPE);
                break;

            case STATUS_SDS:

                Intent intentSDS = new Intent(this, JobMainActivity.class);
                isActivityStarted = true;
                startActivity(intentSDS);
                break;

            case STATUS_OPERATOR_ID:

                Intent intentSD = new Intent(this, ScanDetailsActivity.class);
                isActivityStarted = true;
                startActivity(intentSD);
                break;

            case STATUS_DRIVER_ID:

                Intent intentDI = new Intent(this, ScanDetailsActivity.class);
                isActivityStarted = true;
                startActivity(intentDI);
                break;

            case STATUS_WORK_INSTRUCTION:

                Intent intentWI = new Intent(this, JobMainActivity.class);
                isActivityStarted = true;
                startActivity(intentWI);
                break;

            case STATUS_SAFETY_CHECKS :

                Intent intentSC = new Intent(this, LoadingOperationActivity.class);
                isActivityStarted = true;
                startActivity(intentSC);
                break;

            case STATUS_SCAN_LOADING_ARM:

                Intent intentLA = new Intent(this, LoadingOperationActivity.class);
                isActivityStarted = true;
                startActivity(intentLA);
                break;

            case STATUS_BATCH_CONTROLLER:

                Intent intentBC = new Intent(this, LoadingOperationActivity.class);
                isActivityStarted = true;
                startActivity(intentBC);
                break;

            case STATUS_PUMP_START:

                Intent intentPS = new Intent(this, StopOperationActivity.class);
                isActivityStarted = true;
                startActivity(intentPS);
                break;

            case STATUS_PUMP_STOP:

                Intent intentPSTP = new Intent(this, StopOperationActivity.class);
                isActivityStarted = true;
                startActivity(intentPSTP);
                break;

            case STATUS_SCAN_SEAL:

                Intent intentSS = new Intent(this, StopOperationActivity.class);
                isActivityStarted = true;
                startActivity(intentSS);
                break;

            default:

                break;
        }
    }

    //region Header
    /*-------- Set User Login Details --------*/
    public void setUserLoginDetails() {

        headerLayout = (LinearLayout) findViewById(R.id.headerSwitch);
        headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);

        headerMessage.setText(formatWelcomeMsg(sharedPref.getString(SHARED_PREF_LOGINNAME, "")));
    }
    //endregion

    //region Footer
    public void setFooterMenu() {

        footerLayout = (LinearLayout) findViewById(R.id.footer);

        btnAlert = (ImageButton) footerLayout.findViewById(R.id.btnHome);
        btnAlert.setOnClickListener(new View.OnClickListener() {
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

    public void btnHomeClicked() {

        Intent intentHome = new Intent(this, DashboardActivity.class);
        isActivityStarted = true;
        startActivity(intentHome);
    }

    public void btnSearchClicked() {

        Intent intentSearchJob = new Intent(this, SearchJobActivity.class);
        isActivityStarted = true;
        startActivity(intentSearchJob);
    }

    public void btnSwitchClicked() {

        Intent intentSwitchTruckBay = new Intent(this, SwitchJobActivity.class);
        isActivityStarted = true;
        startActivity(intentSwitchTruckBay);
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
                        isActivityStarted = true;
                        startActivity(intentCheckIn);
                        return true;

                    case R.id.settingsMenuExitApp:
                        exitApplication();
                        return true;

                    case R.id.settingsMenuCheckOut:
                        Intent intentCheckOut = new Intent(getApplicationContext(), CheckOutActivity.class);
                        isActivityStarted = true;
                        startActivity(intentCheckOut);
                        return true;

                    case R.id.settingsMenuSyncData:
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
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                isActivityStarted = true;
                startActivity(intentLogin);
            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) exitDialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    @Override
    public void onStop() {

        super.onStop();

        if (isActivityStarted) {

            finish();
        }
    }
}
