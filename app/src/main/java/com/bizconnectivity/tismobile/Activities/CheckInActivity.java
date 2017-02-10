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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.database.models.GHSDetail;
import com.bizconnectivity.tismobile.database.models.JobDetail;
import com.bizconnectivity.tismobile.database.models.LoadingBayDetail;
import com.bizconnectivity.tismobile.database.models.PPEDetail;
import com.bizconnectivity.tismobile.database.models.SealDetail;
import com.bizconnectivity.tismobile.webservices.CheckInWS;
import com.bizconnectivity.tismobile.webservices.GHSWS;
import com.bizconnectivity.tismobile.webservices.JobDetailWS;
import com.bizconnectivity.tismobile.webservices.PPEWS;
import com.bizconnectivity.tismobile.webservices.SealNoWS;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;

import static com.bizconnectivity.tismobile.Common.*;
import static com.bizconnectivity.tismobile.Constant.*;


public class CheckInActivity extends AppCompatActivity {

    //region declaration

    //header
    @BindView(R.id.header_check_in)
    LinearLayout mLinearLayoutHeader;
    @BindView(R.id.text_header)
    TextView mTextViewHeader;

    //content
    @BindView(R.id.button_technician_nric)
    Button mButtonTechnicianNRIC;
    @BindView(R.id.text_technician_nric)
    TextView mTextViewTechnicianNRIC;
    @BindView(R.id.button_loading_bay)
    Button mButtonLoadingBay;
    @BindView(R.id.text_loading_bay)
    TextView mTextViewLoadingBay;

    //footer
    @BindView(R.id.footer_check_in)
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
    RealmResults<LoadingBayDetail> loadingBayDetailResults;
    LoadingBayDetail loadingBayDetail;
    PopupMenu popupMenu;
    Dialog exitDialog;
    SharedPreferences sharedPref;
    String technicianID, trunkBayString;
    boolean isActivityStarted = false;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //action bar
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //header
        mTextViewHeader.setText(formatCheckInMsg(sharedPref.getString(SHARED_PREF_LOGIN_NAME, "")));

        //region initial technician nric
        technicianID = sharedPref.getString(SHARED_PREF_TECHNICIAN_ID, "");

        if (!technicianID.isEmpty()) {

            mTextViewTechnicianNRIC.setText(technicianID);
            mButtonTechnicianNRIC.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            mButtonTechnicianNRIC.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));

            mButtonLoadingBay.setEnabled(true);

        } else {

            mTextViewTechnicianNRIC.setText("");
            mButtonTechnicianNRIC.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            mButtonTechnicianNRIC.getBackground().clearColorFilter();
        }
        //endregion

        //region initial loading bay no
        loadingBayDetailResults = realm.where(LoadingBayDetail.class).equalTo("status", LOADING_BAY_NO_CHECK_IN).findAllSorted("loadingBayNo", Sort.ASCENDING);

        trunkBayString = "";

        if (loadingBayDetailResults.size() > 0) {

            for (LoadingBayDetail results : loadingBayDetailResults) {

                if (trunkBayString.isEmpty()) {

                    trunkBayString = results.getLoadingBayNo();

                } else {

                    trunkBayString = loadingBayString(trunkBayString, results.getLoadingBayNo());
                }
            }

            mTextViewLoadingBay.setText(trunkBayString);
            mButtonLoadingBay.setEnabled(true);
            mButtonLoadingBay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
            mButtonLoadingBay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));

        } else {

            mTextViewLoadingBay.setText("");
            mButtonLoadingBay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorBlack));
            mButtonLoadingBay.getBackground().clearColorFilter();
        }
        //endregion

        //region auto update loading bay no
        loadingBayDetailResults.addChangeListener(new RealmChangeListener<RealmResults<LoadingBayDetail>>() {
            @Override
            public void onChange(RealmResults<LoadingBayDetail> element) {

                trunkBayString = "";

                for (LoadingBayDetail results : loadingBayDetailResults) {

                    if (trunkBayString.isEmpty()) {

                         trunkBayString = results.getLoadingBayNo();

                    } else {

                        trunkBayString = loadingBayString(trunkBayString, results.getLoadingBayNo());
                    }
                }

                mTextViewLoadingBay.setText(trunkBayString);
                mButtonLoadingBay.setEnabled(true);
                mButtonLoadingBay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                mButtonLoadingBay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
            }
        });
        //endregion
    }

    @OnClick(R.id.button_technician_nric)
    public void btnScanTechnician(View view) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_TECHNICIAN_ID).apply();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(SCAN_MSG_PROMPT_TECHNICIAN_ID);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @OnClick(R.id.button_loading_bay)
    public void btnScanLoadingBay(View view) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_TRUCK_LOADING_BAY).apply();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(SCAN_MSG_PROMPT_TRUCK_LOADING_BAY);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    //region Barcode Scanner
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult scanningIntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanningIntentResult != null) {

            // Retrieve the content of the scan as strings value.
            String scanContent = scanningIntentResult.getContents();

            if (scanContent != null) {

                String returnScanValue = sharedPref.getString(SHARED_PREF_SCAN_VALUE, "");
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(SHARED_PREF_SCAN_VALUE);
                editor.apply();

                if (returnScanValue.equals(SCAN_VALUE_TECHNICIAN_ID)) {

                    editor.putString(SHARED_PREF_TECHNICIAN_ID, scanContent).apply();

                    Intent intent = new Intent(this, CheckInActivity.class);
                    isActivityStarted = true;
                    startActivity(intent);

                } else if (returnScanValue.equals(SCAN_VALUE_TRUCK_LOADING_BAY)) {

                    if (isNetworkAvailable(this)) {

                        //check loading bay no with web service
                        new loadingBayAsync(scanContent).execute();

                    } else {

                        shortToast(this, "No internet connection.");
                    }

                } else {

                    //invalid scan type
                    shortToast(this, SCAN_MSG_INVALID_DATA_RECEIVED);
                }

            } else {

                //no data received
                shortToast(this, SCAN_MSG_NO_DATA_RECEIVED);
            }

        } else {

            // If scan data is not received (for example, if the user cancels the scan by pressing the back button),
            // we can simply output a message.
            shortToast(this, SCAN_MSG_CANCEL_SCANNING);
        }
    }
    //endregion

    //region retrieve loading bay
    private class loadingBayAsync extends AsyncTask<String, Void, Void> {

        LoadingBayDetail loadingBayDetail;
        ProgressDialog progressDialog;
        String rackNo;
        boolean response;

        private loadingBayAsync(String rackNo) {

            this.rackNo = rackNo;
        }

        @Override
        protected void onPreExecute() {

            //start progress dialog
            progressDialog = ProgressDialog.show(CheckInActivity.this, "Please wait..", "Loading...", true);
        }

        @Override
        protected Void doInBackground(String... params) {

            response = CheckInWS.invokeCheckTruckRack(rackNo);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (response) {

                //region insert or update loading bay details to database
                if (realm.where(LoadingBayDetail.class).equalTo("loadingBayNo", rackNo).equalTo("status", LOADING_BAY_NO_CHECK_OUT).count() > 0) {

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            loadingBayDetail = new LoadingBayDetail();
                            loadingBayDetail.setLoadingBayNo(rackNo);
                            loadingBayDetail.setStatus(LOADING_BAY_NO_CHECK_IN);

                            realm.copyToRealmOrUpdate(loadingBayDetail);
                        }
                    });

                } else if (realm.where(LoadingBayDetail.class).equalTo("loadingBayNo", rackNo).equalTo("status", LOADING_BAY_NO_CHECK_IN).count() > 0) {

                    shortToast(getApplicationContext(), ERR_MSG_TRUCK_BAY_ALREADY_CHECKED_IN);

                } else {

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            loadingBayDetail = new LoadingBayDetail();
                            loadingBayDetail.setLoadingBayNo(rackNo);
                            loadingBayDetail.setStatus(LOADING_BAY_NO_CHECK_IN);

                            realm.copyToRealm(loadingBayDetail);
                        }
                    });
                }
                //endregion

                //retrieve job details from web service
                new jobDetailsAsync(rackNo).execute();

                //close progress dialog
                progressDialog.dismiss();

            } else {

                //close progress dialog
                progressDialog.dismiss();

                //prompt error message
                shortToast(getApplicationContext(), ERR_MSG_INVALID_TRUCK_BAY);
            }
        }
    }
    //endregion

    //region retrieve job details
    private class jobDetailsAsync extends AsyncTask<String, Void, Void> {

        String rackNo;
        ArrayList<JobDetail> jobDetailArrayList;
        JobDetail jobDetail;

        private  jobDetailsAsync(String rackNo) {

            this.rackNo = rackNo;
        }

        @Override
        protected Void doInBackground(String... params) {

            jobDetailArrayList = JobDetailWS.invokeRetrieveAllJobs(rackNo);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            for (final JobDetail results : jobDetailArrayList) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        jobDetail = new JobDetail();
                        jobDetail.setJobID(results.getJobID());
                        jobDetail.setCustomerName(results.getCustomerName());
                        jobDetail.setProductName(results.getProductName());
                        jobDetail.setTankNo(results.getTankNo());
                        jobDetail.setLoadingBayNo(results.getLoadingBayNo());
                        jobDetail.setLoadingArm(results.getLoadingArm());
                        jobDetail.setSdsFilePath(results.getSdsFilePath());
                        jobDetail.setOperatorID(results.getOperatorID());
                        jobDetail.setDriverID(results.getDriverID());
                        jobDetail.setJobDate(results.getJobDate());

                        realm.copyToRealmOrUpdate(jobDetail);
                    }
                });

                new PPEGHSAsync(results.getJobID(), results.getProductName()).execute();

                new sealNoAsync(results.getJobID()).execute();
            }
        }
    }
    //endregion

    //region retrieve ppe and ghs
    private class PPEGHSAsync extends AsyncTask<String, Void, Void> {

        ArrayList<PPEDetail> ppeArrayList;
        PPEDetail ppeDetail;
        ArrayList<GHSDetail> ghsArrayList;
        GHSDetail ghsDetail;
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

                        ppeDetail = new PPEDetail();
                        ppeName = results.getPpeURL().substring(0, results.getPpeURL().indexOf("."));

                        if (realm.where(PPEDetail.class).equalTo("jobID", jobID).equalTo("ppeName", ppeName).count() == 0) {

                            if (realm.where(PPEDetail.class).max("ppeID") == null) {

                                ppeDetail.setPpeID(1);

                            } else {

                                ppeDetail.setPpeID(realm.where(PPEDetail.class).max("ppeID").intValue() + 1);
                            }

                            ppeDetail.setPpeName(ppeName);
                            ppeDetail.setPpeURL(results.getPpeURL());
                            ppeDetail.setJobID(jobID);

                            realm.copyToRealm(ppeDetail);

                        } else {

                            ppeDetail.setPpeID(realm.where(PPEDetail.class).max("ppeID").intValue() + 1);
                            ppeDetail.setPpeName(ppeName);
                            ppeDetail.setPpeURL(results.getPpeURL());
                            ppeDetail.setJobID(jobID);

                            realm.copyToRealmOrUpdate(ppeDetail);
                        }
                    }
                });
            }

            for (final GHSDetail results : ghsArrayList) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        ghsDetail = new GHSDetail();
                        ghsName = results.getGhsURL().substring(0, results.getGhsURL().indexOf("."));

                        if (realm.where(GHSDetail.class).equalTo("jobID", jobID).equalTo("ghsName", ghsName).count() == 0) {

                            if (realm.where(GHSDetail.class).max("ghsID") == null) {

                                ghsDetail.setGhsID(1);

                            } else {

                                ghsDetail.setGhsID(realm.where(GHSDetail.class).max("ghsID").intValue() + 1);
                            }

                            ghsDetail.setGhsName(ghsName);
                            ghsDetail.setGhsURL(results.getGhsURL());
                            ghsDetail.setJobID(jobID);

                            realm.copyToRealm(ghsDetail);

                        } else {

                            ghsDetail.setGhsID(realm.where(GHSDetail.class).max("ghsID").intValue() + 1);
                            ghsDetail.setGhsName(ghsName);
                            ghsDetail.setGhsURL(results.getGhsURL());
                            ghsDetail.setJobID(jobID);

                            realm.copyToRealmOrUpdate(ghsDetail);
                        }
                    }
                });
            }
        }
    }
    //endregion

    //region retrieve seal
    private class sealNoAsync extends AsyncTask<String, Void, Void> {

        String jobID;
        ArrayList<SealDetail> sealNoArrayList;
        SealDetail sealDetail;

        private sealNoAsync(String jobID) {

            this.jobID = jobID;
        }

        @Override
        protected Void doInBackground(String... params) {

            sealNoArrayList = SealNoWS.invokeRetrieveSealNo(jobID);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            for (final SealDetail results : sealNoArrayList) {

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        sealDetail = new SealDetail();
                        sealDetail.setSealNo(results.getSealNo());
                        sealDetail.setJobID(jobID);

                        realm.copyToRealmOrUpdate(sealDetail);
                    }
                });
            }
        }
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
        Button btnConfirm = (Button) exitDialog.findViewById(R.id.button_confirm);

        // if button is clicked, close the custom dialog
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //clear all shared preferences
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear().apply();

                //check out all the loading bay
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
