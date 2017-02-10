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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.adapters.CustomExpandableListAdapter;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.database.models.JobDetail;
import com.bizconnectivity.tismobile.database.models.JobList;
import com.bizconnectivity.tismobile.database.models.LoadingBayDetail;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.Sort;

import static com.bizconnectivity.tismobile.Common.*;
import static com.bizconnectivity.tismobile.Constant.*;

public class SwitchJobActivity extends AppCompatActivity {

    //region declaration

    //header
    @BindView(R.id.header_switch)
    LinearLayout mLinearLayoutHeader;
    @BindView(R.id.text_header)
    TextView mTextViewHeader;

    //content
    @BindView(R.id.text_switch_title)
    TextView mTextViewSwitchTitle;
    @BindView(R.id.expandable_list_view)
    ExpandableListView mExpandableListView;

    //footer
    @BindView(R.id.footer_switch)
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
    JobList jobList;
    JobDetail jobDetail;
    ArrayList<JobList> jobListArray;
    ArrayList<JobDetail> childArrayList;

    SharedPreferences sharedPref;
    CustomExpandableListAdapter customExpandableListAdapter;
    PopupMenu popupMenu;
    Dialog exitDialog;
    String trunkBayString;
    boolean isActivityStarted = false;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_job);

        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //action bar
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //region header
        mTextViewHeader.setText(formatWelcomeMsg(sharedPref.getString(SHARED_PREF_LOGIN_NAME, "")));

        //region retrieve loading bay and job details
        trunkBayString = "";
        jobListArray = new ArrayList<>();

        for (LoadingBayDetail results : realm.where(LoadingBayDetail.class).equalTo("status", LOADING_BAY_NO_CHECK_IN).findAllSorted("loadingBayNo", Sort.ASCENDING)) {

            jobList = new JobList();
            childArrayList = new ArrayList<>();

            if (trunkBayString.isEmpty()) {

                trunkBayString = results.getLoadingBayNo();

            } else {

                trunkBayString = loadingBayString(trunkBayString, results.getLoadingBayNo());
            }

            for (JobDetail jobListResults : realm.where(JobDetail.class).equalTo("loadingBayNo", results.getLoadingBayNo())
                    .notEqualTo("jobStatus", "Pending").equalTo("rackOutTime", "").findAll()) {

                childArrayList.add(jobListResults);
            }

            jobList.setLoadingBayNo(results.getLoadingBayNo());
            jobList.setJobDetails(childArrayList);

            jobListArray.add(jobList);
        }
        //endregion

        mTextViewSwitchTitle.setText(formatCheckedInTruckLoadingBay(trunkBayString));
        //endregion

        //region expandable list view settings
        customExpandableListAdapter = new CustomExpandableListAdapter(this, jobListArray);
        mExpandableListView.setAdapter(customExpandableListAdapter);

        //expand all the list view at the first time
        for (int i=0; i<customExpandableListAdapter.getGroupCount(); i++) {

            mExpandableListView.expandGroup(i);
        }
        //endregion

        //region expandable list view child onclick
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPostion, int childPosition, long id) {

                //retrieve job details
                TextView jobID = (TextView) view.findViewById(R.id.text_job_id);
                jobDetail = new JobDetail();
                jobDetail = realm.where(JobDetail.class).equalTo("jobID", jobID.getText().toString()).findFirst();

                //store shared preferences
                storeJobDetailSharedPref(jobDetail);

                //status of job for navigation
                statusNavigation(jobDetail.getJobStatus());

                return true;
            }
        });
        //endregion

        //region expandable list view group onclick
        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
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
        Button btnConfirm = (Button) exitDialog.findViewById(R.id.button_confirm);

        // if button is clicked, close the custom dialog
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

        Button btnCancel = (Button) exitDialog.findViewById(R.id.button_cancel);
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
