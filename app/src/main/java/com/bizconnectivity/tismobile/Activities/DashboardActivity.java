package com.bizconnectivity.tismobile.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.Adapters.CustomExpandableListAdapter;
import com.bizconnectivity.tismobile.Classes.JobDetail;
import com.bizconnectivity.tismobile.Classes.TruckLoadingBayList;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Database.DataSources.JobDetailDataSource;
import com.bizconnectivity.tismobile.Database.DataSources.LoadingBayDetailDataSource;
import com.bizconnectivity.tismobile.R;

import java.util.ArrayList;

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

public class DashboardActivity extends AppCompatActivity {

    Context context;
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;
    TextView headerMessage, tvTruckBayTitle;
    Dialog exitDialog;

    public SharedPreferences sharedPref;

    ExpandableListView expandableListView;
    CustomExpandableListAdapter customExpandableListAdapter;

    LoadingBayDetailDataSource loadingBayDetailDataSource;
    JobDetailDataSource jobDetailDataSource;

    TruckLoadingBayList truckLoadingBayLists;
    JobDetail jobDetail;

    ArrayList<TruckLoadingBayList> truckLoadingBayArrayList;
    ArrayList<JobDetail> childArrayList;
    ArrayList<String> groupArrayList;
    ArrayList<String> totalLoadingBay = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        context = this;
        sharedPref = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        expandableListView = (ExpandableListView) findViewById(R.id.expandable_list_view);
        truckLoadingBayArrayList = new ArrayList<>();
        truckLoadingBayArrayList = retrieveAllLoadingBay();

        customExpandableListAdapter = new CustomExpandableListAdapter(this, truckLoadingBayArrayList);
        expandableListView.setAdapter(customExpandableListAdapter);

        //expand all the list view at the first time
        for ( int i = 0; i < customExpandableListAdapter.getGroupCount(); i++ ) {

            expandableListView.expandGroup(i);
        }

        //region child onclick
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                TextView tvLoadingBayOrderId = (TextView) v.findViewById(R.id.tvLoadingBayOrderId);
                String jobID = tvLoadingBayOrderId.getText().toString();

                jobDetail = new JobDetail();

                jobDetailDataSource = new JobDetailDataSource(context);
                //open database
                jobDetailDataSource.open();
                //retrieve job details by job ID
                jobDetail = jobDetailDataSource.retrieveJobDetails(jobID);
                //close database
                jobDetailDataSource.close();

                //store shared preferences
                storeJobDetailsSharedPref(jobDetail);

                //status of job for navigation
                statusNavigation(jobDetail.getJobStatus());

                return true;
            }
        });
        //endregion

        //region group onclick
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

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
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        /*-------- Set User Login Details --------*/
        setUserLoginDetails();
        setLoadingBayNo();

        /*-------- Footer Buttons --------*/
        setFooterMenu();
        //endregion

    }

    public ArrayList<TruckLoadingBayList> retrieveAllLoadingBay() {

        groupArrayList = new ArrayList<>();
        truckLoadingBayArrayList = new ArrayList<>();

        loadingBayDetailDataSource = new LoadingBayDetailDataSource(this);
        //open database
        loadingBayDetailDataSource.open();
        //retrieve all loading bay no
        groupArrayList = loadingBayDetailDataSource.retrieveAllLoadingBay();
        //close database
        loadingBayDetailDataSource.close();

        if (groupArrayList.size() > 0) {

            totalLoadingBay = groupArrayList;

            for (int i = 0; i< groupArrayList.size(); i++) {

                childArrayList = new ArrayList<>();
                truckLoadingBayLists = new TruckLoadingBayList();

                String loadingBay = groupArrayList.get(i);
                //group title setter
                truckLoadingBayLists.setGroupTitle(loadingBay);

                jobDetailDataSource = new JobDetailDataSource(this);
                //open database
                jobDetailDataSource.open();
                //retrieve all the job details for the loading bay
                childArrayList = jobDetailDataSource.retrieveAllJobDetails(truckLoadingBayLists);
                //close database
                jobDetailDataSource.close();
                //child job details setter
                truckLoadingBayLists.setJobDetails(childArrayList);

                truckLoadingBayArrayList.add(truckLoadingBayLists);
            }

        }

        return truckLoadingBayArrayList;
    }

    public void storeJobDetailsSharedPref(JobDetail jobDetail) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.SHARED_PREF_JOB_ID, jobDetail.getJobID());
        editor.putString(Constant.SHARED_PREF_CUSTOMER_NAME, jobDetail.getCustomerName());
        editor.putString(Constant.SHARED_PREF_PRODUCT_NAME, jobDetail.getProductName());
        editor.putString(Constant.SHARED_PREF_TANK_NO, jobDetail.getTankNo());
        editor.putString(Constant.SHARED_PREF_LOADING_BAY, jobDetail.getLoadingBayNo());
        editor.putString(Constant.SHARED_PREF_LOADING_ARM, jobDetail.getLoadingArm());
        editor.putString(Constant.SHARED_PREF_SDS_FILE_PATH, jobDetail.getSdsFilePath());
        editor.putString(Constant.SHARED_PREF_OPERATOR_ID, jobDetail.getOperatorID());
        editor.putString(Constant.SHARED_PREF_DRIVER_ID, jobDetail.getDriverID());
        editor.putString(Constant.SHARED_PREF_WORK_INSTRUCTION, jobDetail.getWorkInstruction());
        editor.putString(Constant.SHARED_PREF_PUMP_START_TIME, jobDetail.getPumpStartTime());
        editor.putString(Constant.SHARED_PREF_PUMP_STOP_TIME, jobDetail.getPumpStopTime());
        editor.putString(Constant.SHARED_PREF_RACK_OUT_TIME, jobDetail.getRackOutTime());
        editor.putString(Constant.SHARED_PREF_JOB_STATUS, jobDetail.getJobStatus());
        editor.putString(Constant.SHARED_PREF_JOB_DATE, jobDetail.getJobDate());
        editor.commit();

    }

    public void statusNavigation(String jobStatus) {

        switch (jobStatus) {

            case STATUS_PENDING:

                Intent intent = new Intent(this, JobMainActivity.class);
                finish();
                startActivity(intent);
                break;

            case STATUS_PPE:

                Intent intentPPE = new Intent(this, JobMainActivity.class);
                finish();
                startActivity(intentPPE);
                break;

            case STATUS_SDS:

                Intent intentSDS = new Intent(this, JobMainActivity.class);
                finish();
                startActivity(intentSDS);
                break;

            case STATUS_OPERATOR_ID:

                Intent intentSD = new Intent(this, ScanDetailsActivity.class);
                finish();
                startActivity(intentSD);
                break;

            case STATUS_DRIVER_ID:

                Intent intentDI = new Intent(this, ScanDetailsActivity.class);
                finish();
                startActivity(intentDI);
                break;

            case STATUS_WORK_INSTRUCTION:

                Intent intentWI = new Intent(this, JobMainActivity.class);
                finish();
                startActivity(intentWI);
                break;

            case STATUS_SAFETY_CHECKS :

                Intent intentSC = new Intent(this, LoadingOperationActivity.class);
                finish();
                startActivity(intentSC);
                break;

            case STATUS_SCAN_LOADING_ARM:

                Intent intentLA = new Intent(this, LoadingOperationActivity.class);
                finish();
                startActivity(intentLA);
                break;

            case STATUS_BATCH_CONTROLLER:

                Intent intentBC = new Intent(this, LoadingOperationActivity.class);
                finish();
                startActivity(intentBC);
                break;

            case STATUS_PUMP_START:

                Intent intentPS = new Intent(this, StopOperationActivity.class);
                finish();
                startActivity(intentPS);
                break;

            case STATUS_PUMP_STOP:

                Intent intentPSTP = new Intent(this, StopOperationActivity.class);
                finish();
                startActivity(intentPSTP);
                break;

            case STATUS_SCAN_SEAL:

                Intent intentSS = new Intent(this, StopOperationActivity.class);
                finish();
                startActivity(intentSS);
                break;

            default:

                break;
        }
    }

    //region Header
    /*-------- Set User Login Details --------*/
    public void setUserLoginDetails() {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.SHARED_PREF_LOGINNAME, Constant.LOGIN_LOGINNAME);
        editor.commit();

        LinearLayout headerLayout = (LinearLayout) findViewById(R.id.header);
        headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);
        headerMessage.setText(Common.formatWelcomeMsg(Constant.LOGIN_LOGINNAME));
    }

    public void setLoadingBayNo() {

        tvTruckBayTitle = (TextView) findViewById(R.id.tvTruckBayTitle);

        String loadingBay = "";

        if (!totalLoadingBay.isEmpty()) {

            for (int i = 0; i < totalLoadingBay.size(); i++) {

                if (loadingBay.isEmpty()) {

                    loadingBay = totalLoadingBay.get(i);

                } else {

                    loadingBay = loadingBay + ", " + totalLoadingBay.get(i);
                }
            }

            tvTruckBayTitle.setText(Common.formatCheckedInTruckLoadingBay(loadingBay));

        }

    }
    //endregion

    //region Footer
    public void setFooterMenu() {

        RelativeLayout footerLayout = (RelativeLayout) findViewById(R.id.footer);
        btnAlert = (ImageButton) footerLayout.findViewById(R.id.btnHome);
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

        Intent intent = new Intent(context, SearchJobActivity.class);
        finish();
        startActivity(intent);
    }

    public void btnSwitchClicked() {

        Intent intent = new Intent(context, SwitchTruckBayActivity.class);
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
                        Intent intentCheckIn = new Intent(context, CheckInActivity.class);
                        finish();
                        startActivity(intentCheckIn);
                        return true;

                    case R.id.settingsMenuExitApp:
                        exitApplication();
                        return true;

                    case R.id.settingsMenuCheckOut:
                        Intent intentCheckOut = new Intent(context, CheckOutActivity.class);
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
        Button btnConfirm = (Button) exitDialog.findViewById(R.id.btnConfirm);

        // if button is clicked, close the custom dialog
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                exitDialog.dismiss();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.commit();

                loadingBayDetailDataSource = new LoadingBayDetailDataSource(context);
                loadingBayDetailDataSource.deleteAllLoadingBay();

                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentLogin);

            }
        });

        Button btnCancel = (Button) exitDialog.findViewById(R.id.btnCancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });

        int dividerId = exitDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = exitDialog.findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        }

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
