package com.bizconnectivity.tismobile.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.database.models.GHSDetail;
import com.bizconnectivity.tismobile.database.models.JobDetail;
import com.bizconnectivity.tismobile.database.models.JobList;
import com.bizconnectivity.tismobile.database.models.LoadingBayDetail;
import com.bizconnectivity.tismobile.database.models.PPEDetail;
import com.bizconnectivity.tismobile.database.models.SealDetail;
import com.bizconnectivity.tismobile.webservices.GHSWS;
import com.bizconnectivity.tismobile.webservices.JobDetailWS;
import com.bizconnectivity.tismobile.webservices.PPEWS;
import com.bizconnectivity.tismobile.webservices.SealNoWS;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.Sort;

import static com.bizconnectivity.tismobile.Common.*;
import static com.bizconnectivity.tismobile.Constant.*;

public class DashboardActivity extends AppCompatActivity {

    //region declaration

    //header
    @BindView(R.id.header_dashboard)
    LinearLayout mLinearLayoutHeader;
    @BindView(R.id.text_header)
    TextView mTextViewHeader;

    //content
    @BindView(R.id.text_dashboard_title)
    TextView mTextViewDashboardTitle;
    @BindView(R.id.expandable_list_view)
    ExpandableListView mExpandableListView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    //footer
    @BindView(R.id.footer_dashboard)
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
    ArrayList<JobDetail> childArrayList;
    ArrayList<JobList> jobListArray;
    JobList jobList;
    PopupMenu popupMenu;
    Dialog exitDialog;
    CustomExpandableListAdapter customExpandableListAdapter;
    SharedPreferences sharedPref;
    String trunkBayString;
    ArrayList<String> rackNoArray;
    boolean isActivityStarted = false;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //action bar
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //region header
        mTextViewHeader.setText(formatWelcomeMsg(sharedPref.getString(SHARED_PREF_LOGIN_NAME, "")));

        trunkBayString = "";
        rackNoArray = new ArrayList<>();

        if (isNetworkAvailable(getApplicationContext())) {

            for (LoadingBayDetail results : realm.where(LoadingBayDetail.class).equalTo("status", LOADING_BAY_NO_CHECK_IN).findAllSorted("loadingBayNo", Sort.ASCENDING)) {

                if (trunkBayString.isEmpty()) {

                    trunkBayString = results.getLoadingBayNo();

                } else {

                    trunkBayString = loadingBayString(trunkBayString, results.getLoadingBayNo());
                }

                rackNoArray.add(results.getLoadingBayNo());
            }

            //set loading bay no
            mTextViewDashboardTitle.setText(formatCheckedInTruckLoadingBay(trunkBayString));

            if (rackNoArray.size() > 0) {

                new jobDetailsAsync(rackNoArray).execute();
            }

        } else {

            //display no internet message
            shortToast(getApplicationContext(), NO_INTERNET);
        }

        //region expandable list view child onclick
        mExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition, long id) {

                //retrieve job details
                TextView jobID = (TextView) view.findViewById(R.id.text_job_id);

                //store shared preferences
                storeJobDetailSharedPref(realm.where(JobDetail.class).equalTo("jobID", jobID.getText().toString()).findFirst());

                //start job main activity
                Intent intent = new Intent(getApplicationContext(), JobMainActivity.class);
                isActivityStarted = true;
                startActivity(intent);

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

        //region swipe refresh layout
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (isNetworkAvailable(getApplicationContext())) {

                    trunkBayString = "";
                    rackNoArray = new ArrayList<>();

                    for (LoadingBayDetail results : realm.where(LoadingBayDetail.class).equalTo("status", LOADING_BAY_NO_CHECK_IN).findAllSorted("loadingBayNo", Sort.ASCENDING)) {

                        if (trunkBayString.isEmpty()) {

                            trunkBayString = results.getLoadingBayNo();

                        } else {

                            trunkBayString = loadingBayString(trunkBayString, results.getLoadingBayNo());
                        }

                        rackNoArray.add(results.getLoadingBayNo());
                    }

                    //set loading bay no
                    mTextViewDashboardTitle.setText(formatCheckedInTruckLoadingBay(trunkBayString));

                    if (rackNoArray.size() > 0) {

                        new jobDetailsAsync(rackNoArray).execute();
                    }

                    mSwipeRefreshLayout.setRefreshing(false);

                } else {

                    //display no internet message
                    shortToast(getApplicationContext(), NO_INTERNET);

                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        });
        //endregion
    }

    private class jobDetailsAsync extends AsyncTask<String, Void, Void> {

        ArrayList<String> rackNoArray;
        ArrayList<JobDetail> jobDetailArrayList;
        ProgressDialog progressDialog;

        private jobDetailsAsync(ArrayList<String> rackNoArray) {

            this.rackNoArray = rackNoArray;
        }

        @Override
        protected void onPreExecute() {

            //start progress dialog
            progressDialog = ProgressDialog.show(DashboardActivity.this, "Please wait..", "Loading...", true);
        }

        @Override
        protected Void doInBackground(String... params) {

            for(int i=0; i<rackNoArray.size(); i++) {

                jobDetailArrayList = JobDetailWS.invokeRetrieveAllJobs(rackNoArray.get(i));
            }

            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {

            for (final JobDetail results : jobDetailArrayList) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        JobDetail jobDetail;

                        if (realm.where(JobDetail.class).equalTo("jobID", results.getJobID()).count() > 0) {

                            jobDetail = realm.where(JobDetail.class).equalTo("jobID", results.getJobID()).findFirst();
                            jobDetail.setCustomerName(results.getCustomerName());
                            jobDetail.setProductName(results.getProductName());
                            jobDetail.setTankNo(results.getTankNo());
                            jobDetail.setLoadingBayNo(results.getLoadingBayNo());
                            jobDetail.setLoadingArm(results.getLoadingArm());
                            jobDetail.setSdsFilePath(results.getSdsFilePath());
                            jobDetail.setOperatorID(results.getOperatorID());
                            jobDetail.setDriverID(results.getDriverID());
                            jobDetail.setJobDate(results.getJobDate());

                        } else {

                            jobDetail = realm.createObject(JobDetail.class, results.getJobID());
                            jobDetail.setCustomerName(results.getCustomerName());
                            jobDetail.setProductName(results.getProductName());
                            jobDetail.setTankNo(results.getTankNo());
                            jobDetail.setLoadingBayNo(results.getLoadingBayNo());
                            jobDetail.setLoadingArm(results.getLoadingArm());
                            jobDetail.setSdsFilePath(results.getSdsFilePath());
                            jobDetail.setOperatorID(results.getOperatorID());
                            jobDetail.setDriverID(results.getDriverID());
                            jobDetail.setJobDate(results.getJobDate());
                        }

                        realm.copyToRealmOrUpdate(jobDetail);

                        //remove all ppe & ghs & seal details
                        realm.where(PPEDetail.class).findAll().deleteAllFromRealm();
                        realm.where(GHSDetail.class).findAll().deleteAllFromRealm();
                        realm.where(SealDetail.class).findAll().deleteAllFromRealm();
                    }
                });

                //retrieve ppe & ghs details
                new PPEGHSAsync(results.getJobID(), results.getProductName()).execute();

                //retrieve seal details
                new sealNoAsync(results.getJobID()).execute();
            }

            //retrieve from local database
            jobListArray = new ArrayList<>();
            jobList = new JobList();
            childArrayList = new ArrayList<>();

            for(int i=0; i<rackNoArray.size(); i++) {

                for (JobDetail jobListResults : realm.where(JobDetail.class).equalTo("loadingBayNo", rackNoArray.get(i)).equalTo("jobStatus", "Pending").equalTo("rackOutTime", "").findAll()) {

                    childArrayList.add(jobListResults);
                }

                jobList.setLoadingBayNo(rackNoArray.get(i));
                jobList.setJobDetails(childArrayList);
                jobListArray.add(jobList);
            }

            //set custom adapter
            customExpandableListAdapter = new CustomExpandableListAdapter(getApplicationContext(), jobListArray);
            mExpandableListView.setAdapter(customExpandableListAdapter);

            //expand all the list view at the first time
            for (int i=0; i<customExpandableListAdapter.getGroupCount(); i++ ) {

                mExpandableListView.expandGroup(i);
            }

            //close progress dialog
            progressDialog.dismiss();
        }
    }

    private class PPEGHSAsync extends AsyncTask<String, Void, Void> {

        ArrayList<PPEDetail> ppeArrayList;
        ArrayList<GHSDetail> ghsArrayList;
        String jobID, productName, ppeName, ghsName;

        private PPEGHSAsync(String jobID, String productName) {

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

            for (final PPEDetail results : ppeArrayList) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        PPEDetail ppeDetail;
                        ppeName = results.getPpeURL().substring(0, results.getPpeURL().indexOf("."));

                        if (realm.where(PPEDetail.class).max("ppeID") == null) {

                            ppeDetail = realm.createObject(PPEDetail.class, 1);
                            ppeDetail.setPpeName(ppeName);
                            ppeDetail.setPpeURL(results.getPpeURL());
                            ppeDetail.setJobID(jobID);

                        } else {

                            ppeDetail = realm.createObject(PPEDetail.class, realm.where(PPEDetail.class).max("ppeID").intValue() + 1);
                            ppeDetail.setPpeName(ppeName);
                            ppeDetail.setPpeURL(results.getPpeURL());
                            ppeDetail.setJobID(jobID);
                        }

                        realm.copyToRealmOrUpdate(ppeDetail);
                    }
                });
            }

            for (final GHSDetail results : ghsArrayList) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        GHSDetail ghsDetail;
                        ghsName = results.getGhsURL().substring(0, results.getGhsURL().indexOf("."));

                        if (realm.where(GHSDetail.class).max("ghsID") == null) {

                            ghsDetail = realm.createObject(GHSDetail.class, 1);
                            ghsDetail.setGhsName(ghsName);
                            ghsDetail.setGhsURL(results.getGhsURL());
                            ghsDetail.setJobID(jobID);

                        } else {

                            ghsDetail = realm.createObject(GHSDetail.class, realm.where(GHSDetail.class).max("ghsID").intValue() + 1);
                            ghsDetail.setGhsName(ghsName);
                            ghsDetail.setGhsURL(results.getGhsURL());
                            ghsDetail.setJobID(jobID);
                        }

                        realm.copyToRealmOrUpdate(ghsDetail);
                    }
                });
            }
        }
    }

    private class sealNoAsync extends AsyncTask<String, Void, Void> {

        String jobID;
        ArrayList<SealDetail> sealNoArrayList;

        private sealNoAsync(String jobID) {

            this.jobID = jobID;
        }

        @Override
        protected Void doInBackground(String... params) {

            sealNoArrayList = SealNoWS.invokeRetrieveSealNo(jobID);

            return null;
        }

        @Override
        protected void onPostExecute(final Void result) {

            for (final SealDetail results : sealNoArrayList) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        if (realm.where(SealDetail.class).equalTo("sealNo", results.getSealNo()).equalTo("jobID", jobID).count() == 0) {

                            SealDetail sealDetail;
                            sealDetail = realm.createObject(SealDetail.class, results.getSealNo());
                            sealDetail.setJobID(jobID);

                            realm.copyToRealmOrUpdate(sealDetail);
                        }
                    }
                });
            }
        }
    }

    private void storeJobDetailSharedPref(JobDetail jobDetail) {

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
