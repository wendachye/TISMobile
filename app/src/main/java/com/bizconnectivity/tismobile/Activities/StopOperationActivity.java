package com.bizconnectivity.tismobile.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.bizconnectivity.tismobile.webservices.AddSealWS;
import com.bizconnectivity.tismobile.webservices.CheckSealWS;
import com.bizconnectivity.tismobile.webservices.DepartureWS;
import com.bizconnectivity.tismobile.webservices.PumpStopWS;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.bizconnectivity.tismobile.Common.*;
import static com.bizconnectivity.tismobile.Constant.*;

public class StopOperationActivity extends AppCompatActivity {

    //region declaration

    //header
    @BindView(R.id.header_stop_operation)
    LinearLayout mLinearLayoutHeader;
    @BindView(R.id.text_header)
    TextView mTextViewHeader;
    @BindView(R.id.text_job_id)
    TextView mTextViewJobID;
    @BindView(R.id.text_customer_name)
    TextView mTextViewCustomerName;
    @BindView(R.id.text_loading_bay)
    TextView mTextViewLoadingBay;
    @BindView(R.id.text_loading_arm)
    TextView mTextViewLoadingArm;

    //content
    @BindView(R.id.button_pump_stop)
    Button mButtonPumpStop;
    @BindView(R.id.button_scan_seal)
    Button mButtonScanSeal;
    @BindView(R.id.button_departure)
    Button mButtonDeparture;
    @BindView(R.id.text_pump_stop)
    TextView mTextViewPumpStop;

    //footer
    @BindView(R.id.footer_stop_operation)
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
    SharedPreferences sharedPref;
    PopupMenu popupMenu;
    Dialog exitDialog, pumpStopDialog, scanSealDialog, departureDialog;
    boolean isActivityStarted = false;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_operation);

        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //action bar
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        //header
        mTextViewHeader.setText(formatWelcomeMsg(sharedPref.getString(SHARED_PREF_LOGIN_NAME, "")));
        mTextViewJobID.setText(sharedPref.getString(SHARED_PREF_JOB_ID, ""));
        mTextViewCustomerName.setText(sharedPref.getString(SHARED_PREF_CUSTOMER_NAME, ""));
        mTextViewLoadingBay.setText(sharedPref.getString(SHARED_PREF_LOADING_BAY, ""));
        mTextViewLoadingArm.setText(sharedPref.getString(SHARED_PREF_LOADING_ARM, ""));

        //region status settings

        //retrieve job status from shared preferences
        switch (sharedPref.getString(SHARED_PREF_JOB_STATUS, "")) {

            case STATUS_PUMP_START:
                mButtonPumpStop.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                mButtonPumpStop.getBackground().clearColorFilter();
                mTextViewPumpStop.setText("");

                mButtonScanSeal.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                mButtonScanSeal.getBackground().clearColorFilter();

                mButtonDeparture.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                mButtonDeparture.getBackground().clearColorFilter();
                break;

            case STATUS_PUMP_STOP:
                mButtonPumpStop.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonPumpStop.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mButtonPumpStop.setEnabled(false);
                mTextViewPumpStop.setText(sharedPref.getString(SHARED_PREF_PUMP_STOP_TIME, ""));

                mButtonScanSeal.setEnabled(true);
                break;

            case STATUS_SCAN_SEAL:
                mButtonPumpStop.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonPumpStop.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mButtonPumpStop.setEnabled(false);
                mTextViewPumpStop.setText(sharedPref.getString(SHARED_PREF_PUMP_STOP_TIME, ""));

                mButtonScanSeal.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonScanSeal.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mButtonScanSeal.setEnabled(true);

                mButtonDeparture.setEnabled(true);
                break;

            default:
                break;
        }
        //endregion
    }

    @OnClick(R.id.button_pump_stop)
    public void btnPumpStop(View view) {

        btnPumpStopClicked();
    }

    @OnClick(R.id.button_scan_seal)
    public void btnScanSeal(View view) {

        if (sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL1, "").isEmpty()) {

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_BOTTOM_SEAL1).apply();

            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt(SCAN_MSG_PROMPT_SCAN_BOTTOM_SEAL);
            integrator.setBeepEnabled(true);
            integrator.initiateScan();

        } else {

            scanSealDialog();
        }
    }

    @OnClick(R.id.button_departure)
    public void btnDeparture(View view) {

        btnDepartureClicked();
    }

    //region Pump Stop
    public void btnPumpStopClicked() {

        if (pumpStopDialog != null && pumpStopDialog.isShowing()) return;

        pumpStopDialog = new Dialog(this);
        pumpStopDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pumpStopDialog.setContentView(R.layout.dialog_pump_stop);

        //region button confirm
        Button btnConfirm = (Button) pumpStopDialog.findViewById(R.id.button_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNetworkAvailable(getApplicationContext())) {

                    //update webservice
                    new pumpStopWSAsync(sharedPref.getString(SHARED_PREF_JOB_ID, ""), sharedPref.getString(SHARED_PREF_LOGIN_NAME, "")).execute();

                    //close pump stop dialog
                    pumpStopDialog.dismiss();

                } else {

                    //update local database
                    final Calendar calendar = Calendar.getInstance();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(SHARED_PREF_PUMP_STOP_TIME, simpleDateFormat2.format(calendar.getTime()));
                    editor.putString(SHARED_PREF_JOB_STATUS, STATUS_PUMP_STOP);
                    editor.apply();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            JobDetail jobDetail = realm.where(JobDetail.class).equalTo("jobID", sharedPref.getString(SHARED_PREF_JOB_ID, "")).findFirst();
                            jobDetail.setPumpStopTime(simpleDateFormat2.format(calendar.getTime()));
                            jobDetail.setJobStatus(STATUS_PUMP_STOP);

                            realm.copyToRealmOrUpdate(jobDetail);
                        }
                    });
                    //endregion

                    //set button pump stop
                    mButtonPumpStop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                    mButtonPumpStop.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                    mButtonPumpStop.setEnabled(false);
                    mButtonScanSeal.setEnabled(true);

                    //close pump stop dialog
                    pumpStopDialog.dismiss();
                }
            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) pumpStopDialog.findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //close pump stop dialog
                pumpStopDialog.dismiss();
            }
        });
        //endregion

        int dividerId = pumpStopDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = pumpStopDialog.findViewById(dividerId);
        if (divider != null) divider.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent));
        assert pumpStopDialog.getWindow() != null;
        pumpStopDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pumpStopDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pumpStopDialog.show();
    }

    private class pumpStopWSAsync extends AsyncTask<String, Void, Void> {

        String jobID, updatedBy;
        Boolean response;
        ProgressDialog progressDialog;

        private pumpStopWSAsync(String jobID, String updatedBy) {

            this.jobID = jobID;
            this.updatedBy = updatedBy;
        }

        @Override
        protected void onPreExecute() {

            //start progress dialog
            progressDialog = ProgressDialog.show(StopOperationActivity.this, "Please wait..", "Loading...", true);
        }

        @Override
        protected Void doInBackground(String... params) {

            Calendar calendar = Calendar.getInstance();
            response = PumpStopWS.invokeUpdatePumpStopWS(jobID, simpleDateFormat3.format(calendar.getTime()), updatedBy);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (response) {

                //region update job status
                final Calendar calendar = Calendar.getInstance();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(SHARED_PREF_PUMP_STOP_TIME, simpleDateFormat2.format(calendar.getTime()));
                editor.putString(SHARED_PREF_JOB_STATUS, STATUS_PUMP_STOP);
                editor.apply();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        JobDetail jobDetail = realm.where(JobDetail.class).equalTo("jobID", sharedPref.getString(SHARED_PREF_JOB_ID, "")).findFirst();
                        jobDetail.setPumpStopTime(simpleDateFormat2.format(calendar.getTime()));
                        jobDetail.setJobStatus(STATUS_PUMP_STOP);

                        realm.copyToRealmOrUpdate(jobDetail);
                    }
                });
                //endregion

                //set button pump stop
                mButtonPumpStop.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                mButtonPumpStop.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                mButtonPumpStop.setEnabled(false);
                mButtonScanSeal.setEnabled(true);

                //close progress dialog
                progressDialog.dismiss();

            } else {

                //close progress dialog
                progressDialog.dismiss();

                //show error message
                shortToast(getApplicationContext(), ERR_CONTACT_CUSTOMER_SERVICE);
            }
        }
    }
    //endregion

    //region Seal

    public void scanSealDialog() {

        if (scanSealDialog != null && scanSealDialog.isShowing()) return;

        scanSealDialog = new Dialog(this);
        scanSealDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        scanSealDialog.setContentView(R.layout.dialog_scan_seal);

        //region button scan more seal
        Button btnScanMoreSeals = (Button) scanSealDialog.findViewById(R.id.btnScanMoreSeals);
	    btnScanMoreSeals.setOnClickListener(new View.OnClickListener() {
		    @Override
		    public void onClick(View view) {

			    scanSealDialog.dismiss();
			    scanMoreSealsClicked();
		    }
	    });
        //endregion

        final ArrayList<String> countSeal = new ArrayList<>();

        String seal1 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL1, "");
        String seal2 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL2, "");
        String seal3 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL3, "");
        String seal4 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL4, "");

        //region set seal 1
        TextView  tvSeal1 = (TextView) scanSealDialog.findViewById(R.id.tvSeal1);
	    if (seal1.isEmpty()) {

		    tvSeal1.setVisibility(View.GONE);

	    } else {

		    tvSeal1.setText(seal1);
		    tvSeal1.setVisibility(View.VISIBLE);
		    countSeal.add(seal1);
	    }
        //endregion

        //region set seal 2
        TextView  tvSeal2 = (TextView) scanSealDialog.findViewById(R.id.tvSeal2);
        if (seal2.isEmpty()) {

            tvSeal2.setVisibility(View.GONE);

        } else {

            tvSeal2.setText(seal2);
            tvSeal2.setVisibility(View.VISIBLE);
	        countSeal.add(seal2);
        }
        //endregion

        //region set seal 3
        TextView  tvSeal3 = (TextView) scanSealDialog.findViewById(R.id.tvSeal3);
        if (seal3.isEmpty()) {

            tvSeal3.setVisibility(View.GONE);

        } else {

            tvSeal3.setText(seal3);
            tvSeal3.setVisibility(View.VISIBLE);
	        countSeal.add(seal3);
        }
        //endregion

        //region set seal 4
        TextView  tvSeal4 = (TextView) scanSealDialog.findViewById(R.id.tvSeal4);
        if (seal4.isEmpty()) {

            tvSeal4.setVisibility(View.GONE);

        } else {

            tvSeal4.setText(seal4);
            tvSeal4.setVisibility(View.VISIBLE);
	        countSeal.add(seal4);

            btnScanMoreSeals.setEnabled(false);
        }
        //endregion

        //region button confirm
	    Button btnConfirm = (Button) scanSealDialog.findViewById(R.id.button_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

	            String jobID = sharedPref.getString(SHARED_PREF_JOB_ID, "");
	            String updatedBy = sharedPref.getString(SHARED_PREF_LOGIN_NAME, "");
	            String sealPosition = "bottom";

                if (isNetworkAvailable(getApplicationContext())) {

                    //update webservice
                    new addSealWSAsync(countSeal, jobID, sealPosition, updatedBy).execute();

                    //close seal dialog
                    scanSealDialog.dismiss();

                } else {

                    //update local database
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            for (int i = 0; i < countSeal.size(); i++) {

                                SealDetail sealDetail = realm.where(SealDetail.class).equalTo("sealNo", countSeal.get(i)).findFirst();
                                sealDetail.setStatus("Used");

                                realm.copyToRealmOrUpdate(sealDetail);
                            }
                        }
                    });

                    //update job status
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(SHARED_PREF_JOB_STATUS, STATUS_SCAN_SEAL).apply();
                    updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_SCAN_SEAL);

                    //set button scan seal
                    mButtonScanSeal.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                    mButtonScanSeal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                    mButtonScanSeal.setEnabled(true);
                    mButtonDeparture.setEnabled(true);

                    //close scan seal dialog
                    scanSealDialog.dismiss();
                }

            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) scanSealDialog.findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //close scan seal dialog
	            scanSealDialog.dismiss();
            }
        });
        //endregion

        int dividerId = scanSealDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = scanSealDialog.findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent));
        }
        assert scanSealDialog.getWindow() != null;
        scanSealDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        scanSealDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        scanSealDialog.show();
    }

    public void scanMoreSealsClicked() {

        SharedPreferences.Editor editor = sharedPref.edit();

        String seal2 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL2, "");
        String seal3 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL3, "");
        String seal4 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL4, "");

        if (seal2.isEmpty()) {

            editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_BOTTOM_SEAL2).apply();

        } else if (seal3.isEmpty()) {

            editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_BOTTOM_SEAL3).apply();

        } else if (seal4.isEmpty()) {

            editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_BOTTOM_SEAL4).apply();
        }

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(SCAN_MSG_PROMPT_SCAN_BOTTOM_SEAL);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    private class addSealWSAsync extends AsyncTask<String, Void, Void> {

        String sealPosition, updatedBy, jobID;
        ArrayList<String> sealDetailArrayList;
        Boolean response;
        ProgressDialog progressDialog;

        private addSealWSAsync(ArrayList<String> sealNo, String jobID, String sealPosition, String updatedBy) {

            this.sealDetailArrayList = sealNo;
            this.jobID = jobID;
            this.sealPosition = sealPosition;
            this.updatedBy = updatedBy;
        }

        @Override
        protected void onPreExecute() {

            //start progress dialog
            progressDialog = ProgressDialog.show(StopOperationActivity.this, "Please wait..", "Loading...", true);
        }

        @Override
        protected Void doInBackground(String... params) {

            for (int i = 0; i < sealDetailArrayList.size(); i++) {

                response = AddSealWS.invokeAddSealWS(sealDetailArrayList.get(i), jobID, sealPosition, updatedBy);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (response) {

                //update job status
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(SHARED_PREF_JOB_STATUS, STATUS_SCAN_SEAL).apply();
                updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_SCAN_SEAL);

                //set button scan seal
                mButtonScanSeal.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                mButtonScanSeal.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                mButtonScanSeal.setEnabled(true);
                mButtonDeparture.setEnabled(true);

                //close progress dialog
                progressDialog.dismiss();

            } else {

                //close progress dialog
                progressDialog.dismiss();

                //show error message
                shortToast(getApplicationContext(), ERR_MSG_SEAL_CANNOT_ADD);
            }
        }
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

                if (returnScanValue.equals(SCAN_VALUE_BOTTOM_SEAL1)) {

                    if (isNetworkAvailable(this)) {

                        //check with webservice
                        new checkSealWSAsync(sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent).execute();

                    } else {

                        if (realm.where(SealDetail.class).equalTo("sealNo", scanContent)
                                .equalTo("jobID", sharedPref.getString(SHARED_PREF_JOB_ID, "")).count() > 0) {

                            String seal1 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL1, "");

                            if (!scanContent.equals(seal1)) {

                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(SCAN_VALUE_BOTTOM_SEAL1, scanContent).apply();

                            } else {

                                shortToast(this, ERR_MSG_CANNOT_ADD_SEAL);
                            }

                            scanSealDialog();

                        } else {

                            shortToast(this, ERR_MSG_INVALID_SEAL_NO);
                        }
                    }

                } else if (returnScanValue.equals(SCAN_VALUE_BOTTOM_SEAL2)) {

                    if (isNetworkAvailable(this)) {

                        //check with webservice
                        new checkSealWSAsync(sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent).execute();

                    } else {

                        if (realm.where(SealDetail.class).equalTo("sealNo", scanContent)
                                .equalTo("jobID", sharedPref.getString(SHARED_PREF_JOB_ID, "")).count() > 0) {

                            String seal1 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL1, "");

                            if (!scanContent.equals(seal1)) {

                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(SCAN_VALUE_BOTTOM_SEAL2, scanContent).apply();

                            } else {

                                shortToast(this, ERR_MSG_CANNOT_ADD_SEAL);
                            }

                            scanSealDialog();

                        } else {

                            shortToast(this, ERR_MSG_INVALID_SEAL_NO);
                        }
                    }

                } else if (returnScanValue.equals(SCAN_VALUE_BOTTOM_SEAL3)) {

                    if (isNetworkAvailable(this)) {

                        //check with webservice
                        new checkSealWSAsync(sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent).execute();

                    } else {

                        if (realm.where(SealDetail.class).equalTo("sealNo", scanContent)
                                .equalTo("jobID", sharedPref.getString(SHARED_PREF_JOB_ID, "")).count() > 0) {

                            String seal1 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL1, "");
                            String seal2 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL2, "");

                            if (!scanContent.equals(seal1) && !scanContent.equals(seal2)) {

                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(SCAN_VALUE_BOTTOM_SEAL3, scanContent).apply();

                            } else {

                                shortToast(this, ERR_MSG_CANNOT_ADD_SEAL);
                            }

                            scanSealDialog();

                        } else {

                            shortToast(this, ERR_MSG_INVALID_SEAL_NO);
                        }
                    }

                } else if (returnScanValue.equals(SCAN_VALUE_BOTTOM_SEAL4)) {

                    if (isNetworkAvailable(this)) {

                        //check with webservice
                        new checkSealWSAsync(sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent).execute();

                    } else {

                        if (realm.where(SealDetail.class).equalTo("sealNo", scanContent)
                                .equalTo("jobID", sharedPref.getString(SHARED_PREF_JOB_ID, "")).count() > 0) {

                            String seal1 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL1, "");
                            String seal2 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL2, "");
                            String seal3 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL3, "");

                            if (!scanContent.equals(seal1) && !scanContent.equals(seal2) && !scanContent.equals(seal3)) {

                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString(SCAN_VALUE_BOTTOM_SEAL4, scanContent).apply();

                            } else {

                                shortToast(this, ERR_MSG_CANNOT_ADD_SEAL);
                            }

                            scanSealDialog();

                        } else {

                            shortToast(this, ERR_MSG_INVALID_SEAL_NO);
                        }
                    }

                } else {

                    shortToast(this, SCAN_MSG_INVALID_DATA_RECEIVED);
                }

            } else {

                shortToast(this, SCAN_MSG_NO_DATA_RECEIVED);
            }

        } else {
            // If scan data is not received (for example, if the user cancels the scan by pressing the back button),
            // we can simply output a message.
            shortToast(this, SCAN_MSG_CANCEL_SCANNING);
        }
    }

    private class checkSealWSAsync extends AsyncTask<String, Void, Void> {

        String jobID, sealNo;
        Boolean response;
        ProgressDialog progressDialog;

        private checkSealWSAsync(String jobID, String sealNo) {

            this.jobID = jobID;
            this.sealNo = sealNo;
        }

        @Override
        protected void onPreExecute() {

            //start progress dialog
            progressDialog = ProgressDialog.show(StopOperationActivity.this, "Please wait..", "Loading...", true);
        }

        @Override
        protected Void doInBackground(String... params) {

            response = CheckSealWS.invokeCheckSeal(jobID, sealNo);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            SharedPreferences.Editor editor = sharedPref.edit();

            if (response) {

                String seal = sharedPref.getString(SHARED_PREF_SCAN_VALUE, "");
                String seal1 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL1, "");
                String seal2 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL2, "");
                String seal3 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL3, "");
                String seal4 = sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL4, "");

                if (seal.equals(SCAN_VALUE_BOTTOM_SEAL1)) {

                    if (!sealNo.equals(seal1)) {

                        editor.putString(SCAN_VALUE_BOTTOM_SEAL1, sealNo).apply();

                    } else {

                        shortToast(getApplicationContext(), ERR_MSG_CANNOT_ADD_SEAL);
                    }

                } else if (seal.equals(SCAN_VALUE_BOTTOM_SEAL2)) {

                    if (!sealNo.equals(seal1) && !sealNo.equals(seal2)) {

                        editor.putString(SCAN_VALUE_BOTTOM_SEAL2, sealNo).apply();

                    } else {

                        shortToast(getApplicationContext(), ERR_MSG_CANNOT_ADD_SEAL);
                    }

                } else if (seal.equals(SCAN_VALUE_BOTTOM_SEAL3)) {

                    if (!sealNo.equals(seal1) && !sealNo.equals(seal2) && !sealNo.equals(seal3)) {

                        editor.putString(SCAN_VALUE_BOTTOM_SEAL3, sealNo).apply();

                    } else {

                        shortToast(getApplicationContext(), ERR_MSG_CANNOT_ADD_SEAL);
                    }

                } else {

                    if (!sealNo.equals(seal1) && !sealNo.equals(seal2) && !sealNo.equals(seal3) && !sealNo.equals(seal4)) {

                        editor.putString(SCAN_VALUE_BOTTOM_SEAL4, sealNo).apply();

                    } else {

                        shortToast(getApplicationContext(), ERR_MSG_CANNOT_ADD_SEAL);
                    }
                }

                //close progress dialog
                progressDialog.dismiss();

                scanSealDialog();

            } else {

                //close progress dialog
                progressDialog.dismiss();

                //show error message
                shortToast(getApplicationContext(), ERR_MSG_INVALID_SEAL_NO);
            }
        }
    }
    //endregion

    //endregion

    //region Departure
    public void btnDepartureClicked() {

        if (departureDialog != null && departureDialog.isShowing()) return;

        departureDialog = new Dialog(this);
        departureDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        departureDialog.setContentView(R.layout.dialog_departure);

        //region button confirm
        Button btnConfirm = (Button) departureDialog.findViewById(R.id.button_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNetworkAvailable(getApplicationContext())) {

                    //update webservice
                    new departureWSAsync(sharedPref.getString(SHARED_PREF_JOB_ID, ""), sharedPref.getString(SHARED_PREF_LOGIN_NAME, "")).execute();

                } else {

                    //update local database
                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            Calendar calendar = Calendar.getInstance();

                            JobDetail jobDetail = realm.where(JobDetail.class).equalTo("jobID", sharedPref.getString(SHARED_PREF_JOB_ID, "")).findFirst();
                            jobDetail.setRackOutTime(simpleDateFormat3.format(calendar.getTime()));

                            realm.copyToRealmOrUpdate(jobDetail);
                        }
                    });

                    //region remove shared preferences
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove(SHARED_PREF_JOB_ID);
                    editor.remove(SHARED_PREF_CUSTOMER_NAME);
                    editor.remove(SHARED_PREF_PRODUCT_NAME);
                    editor.remove(SHARED_PREF_TANK_NO);
                    editor.remove(SHARED_PREF_LOADING_BAY);
                    editor.remove(SHARED_PREF_LOADING_ARM);
                    editor.remove(SHARED_PREF_SDS_FILE_PATH);
                    editor.remove(SHARED_PREF_OPERATOR_ID);
                    editor.remove(SHARED_PREF_DRIVER_ID);
                    editor.remove(SHARED_PREF_WORK_INSTRUCTION);
                    editor.remove(SHARED_PREF_PUMP_START_TIME);
                    editor.remove(SHARED_PREF_PUMP_STOP_TIME);
                    editor.remove(SHARED_PREF_RACK_OUT_TIME);
                    editor.remove(SHARED_PREF_JOB_STATUS);
                    editor.remove(SHARED_PREF_JOB_DATE);
                    editor.remove(SHARED_PREF_BATCH_CONTROLLER);
                    editor.remove(SHARED_PREF_BATCH_CONTROLLER_LITRE);
                    editor.remove(SCAN_VALUE_BOTTOM_SEAL1);
                    editor.remove(SCAN_VALUE_BOTTOM_SEAL2);
                    editor.remove(SCAN_VALUE_BOTTOM_SEAL3);
                    editor.remove(SCAN_VALUE_BOTTOM_SEAL4);
                    editor.apply();
                    //endregion

                    //close departure dialog
                    departureDialog.dismiss();

                    //display message
                    longToast(getApplicationContext(), DEPARTURE_MESSAGE);

                    //navigate to dashboard activity
                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    isActivityStarted = true;
                    startActivity(intent);
                }
            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) departureDialog.findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //close departure dialog
                departureDialog.dismiss();
            }
        });
        //endregion

        int dividerId = departureDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = departureDialog.findViewById(dividerId);
        if (divider != null) divider.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent));
        assert departureDialog.getWindow() != null;
        departureDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        departureDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        departureDialog.show();
    }

    private class departureWSAsync extends AsyncTask<String, Void, Void> {

        String jobID, updatedBy;
        Boolean response;
        ProgressDialog progressDialog;

        private departureWSAsync(String jobID, String updatedBy) {

            this.jobID = jobID;
            this.updatedBy = updatedBy;
        }

        @Override
        protected void onPreExecute() {

            //start progress dialog
            progressDialog = ProgressDialog.show(StopOperationActivity.this, "Please wait..", "Loading...", true);
        }

        @Override
        protected Void doInBackground(String... params) {

            Calendar calendar = Calendar.getInstance();
            response = DepartureWS.invokeAddDepartureWS(jobID, simpleDateFormat3.format(calendar.getTime()), updatedBy);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (response) {

                //region remove job details shared pref
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(SHARED_PREF_JOB_ID);
                editor.remove(SHARED_PREF_CUSTOMER_NAME);
                editor.remove(SHARED_PREF_PRODUCT_NAME);
                editor.remove(SHARED_PREF_TANK_NO);
                editor.remove(SHARED_PREF_LOADING_BAY);
                editor.remove(SHARED_PREF_LOADING_ARM);
                editor.remove(SHARED_PREF_SDS_FILE_PATH);
                editor.remove(SHARED_PREF_OPERATOR_ID);
                editor.remove(SHARED_PREF_DRIVER_ID);
                editor.remove(SHARED_PREF_WORK_INSTRUCTION);
                editor.remove(SHARED_PREF_PUMP_START_TIME);
                editor.remove(SHARED_PREF_PUMP_STOP_TIME);
                editor.remove(SHARED_PREF_RACK_OUT_TIME);
                editor.remove(SHARED_PREF_JOB_STATUS);
                editor.remove(SHARED_PREF_JOB_DATE);
                editor.remove(SHARED_PREF_BATCH_CONTROLLER);
                editor.remove(SHARED_PREF_BATCH_CONTROLLER_LITRE);
                editor.remove(SCAN_VALUE_BOTTOM_SEAL1);
                editor.remove(SCAN_VALUE_BOTTOM_SEAL2);
                editor.remove(SCAN_VALUE_BOTTOM_SEAL3);
                editor.remove(SCAN_VALUE_BOTTOM_SEAL4);
                editor.apply();
                //endregion

                //delete the job from local database
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        realm.where(JobDetail.class).equalTo("jobID", jobID).findFirst().deleteFromRealm();
                        realm.where(GHSDetail.class).equalTo("jobID", jobID).findAll().deleteAllFromRealm();
                        realm.where(PPEDetail.class).equalTo("jobID", jobID).findAll().deleteAllFromRealm();
                        realm.where(SealDetail.class).equalTo("jobID", jobID).findAll().deleteAllFromRealm();
                    }
                });

                //end progress dialog
                progressDialog.dismiss();

                //navigate to dashboard activity
                Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                isActivityStarted = true;
                startActivity(intent);

            } else {

                //end progress dialog
                progressDialog.dismiss();

                //show error message
                shortToast(getApplicationContext(), ERR_MSG_DEPARTURE);
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

	public void onBackPressed() {

        Intent intent = new Intent(this, DashboardActivity.class);
        isActivityStarted = true;
        startActivity(intent);
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
