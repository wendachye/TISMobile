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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.database.models.JobDetail;
import com.bizconnectivity.tismobile.database.models.LoadingBayDetail;
import com.bizconnectivity.tismobile.webservices.LoadingArmWS;
import com.bizconnectivity.tismobile.webservices.PumpStartWS;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.bizconnectivity.tismobile.Common.*;
import static com.bizconnectivity.tismobile.Constant.*;

public class LoadingOperationActivity extends AppCompatActivity {

    //region declaration

    //header
    @BindView(R.id.header_loading_operation)
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
    @BindView(R.id.button_scan_loading_arm)
    Button mButtonScanLoadingArm;
    @BindView(R.id.button_batch_controller)
    Button mButtonBatchController;
    @BindView(R.id.button_pump_start)
    Button mButtonPumpStart;
    @BindView(R.id.text_scan_loading_arm)
    TextView mTextViewScanLoadingArm;
    @BindView(R.id.text_batch_controller)
    TextView mTextViewBatchController;
    @BindView(R.id.text_pump_start)
    TextView mTextViewPumpStart;

    //footer
    @BindView(R.id.footer_loading_operation)
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
    PopupMenu popupMenu;
    Dialog exitDialog, batchControllerDialog, pumpStartDialog;
    SharedPreferences sharedPref;
    String jobStatus, loadingArm, batchController, pumpStartTime;
    boolean isActivityStarted = false;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_operation);

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
        jobStatus = sharedPref.getString(SHARED_PREF_JOB_STATUS, "");
        loadingArm = sharedPref.getString(SHARED_PREF_LOADING_ARM, "");
        batchController = sharedPref.getString(SHARED_PREF_BATCH_CONTROLLER, "");
        pumpStartTime = sharedPref.getString(SHARED_PREF_PUMP_START_TIME, "");

        switch (jobStatus) {

            case STATUS_SAFETY_CHECKS:
                mButtonScanLoadingArm.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                mButtonScanLoadingArm.getBackground().clearColorFilter();
                mTextViewScanLoadingArm.setText("");

                mButtonBatchController.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                mButtonBatchController.getBackground().clearColorFilter();
                mTextViewBatchController.setText("");

                mButtonPumpStart.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                mButtonPumpStart.getBackground().clearColorFilter();
                mTextViewPumpStart.setText("");
                break;

            case STATUS_SCAN_LOADING_ARM:
                mButtonScanLoadingArm.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonScanLoadingArm.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mTextViewScanLoadingArm.setText(loadingArm);

                mButtonBatchController.setEnabled(true);
                break;

            case STATUS_BATCH_CONTROLLER:
                mButtonScanLoadingArm.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonScanLoadingArm.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mTextViewScanLoadingArm.setText(loadingArm);

                mButtonBatchController.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonBatchController.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mButtonBatchController.setEnabled(true);
                mTextViewBatchController.setText(batchController);

                mButtonPumpStart.setEnabled(true);
                break;

            case STATUS_PUMP_START:
                mButtonScanLoadingArm.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonScanLoadingArm.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mTextViewScanLoadingArm.setText(loadingArm);

                mButtonBatchController.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonBatchController.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mButtonBatchController.setEnabled(true);
                mTextViewBatchController.setText(batchController);

                mButtonPumpStart.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonPumpStart.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mButtonPumpStart.setEnabled(true);
                mTextViewPumpStart.setText(pumpStartTime);
                break;

            default:
                break;
        }

        //endregion
    }

    @OnClick(R.id.button_scan_loading_arm)
    public void btnScanLoadingArm(View view) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_LOADING_ARM).apply();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(SCAN_MSG_PROMPT_SCAN_LOADING_ARM);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @OnClick(R.id.button_batch_controller)
    public void btnBatchController(View view) {

        batchController();
    }

    @OnClick(R.id.button_pump_start)
    public void btnPumpStart(View view) {

        btnPumpStartClicked();
    }

    //region Batch Controller
    public void batchController() {

        if (batchControllerDialog != null && batchControllerDialog.isShowing()) return;

        batchControllerDialog = new Dialog(this);
        batchControllerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        batchControllerDialog.setContentView(R.layout.dialog_batch_controller);

        final TextView mTextViewMetricTon = (TextView) batchControllerDialog.findViewById(R.id.text_metric_ton);
        final EditText mEditTextLitre = (EditText) batchControllerDialog.findViewById(R.id.edit_litre);

        //region set batch controller status
        if (sharedPref.getString(SHARED_PREF_JOB_STATUS, "").equals(STATUS_BATCH_CONTROLLER)) {

            String litre = sharedPref.getString(SHARED_PREF_BATCH_CONTROLLER_LITRE, "");
            String metric = sharedPref.getString(SHARED_PREF_BATCH_CONTROLLER, "");

            mTextViewMetricTon.setText(metric);
            mEditTextLitre.setText(litre);

        } else {

            mEditTextLitre.setText("");
        }
        //endregion

        //region edit text changed listener
        mEditTextLitre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String litre = mEditTextLitre.getText().toString();

                if (litre.length() > 0) {

                    Float calculation = Float.parseFloat(litre) / 1000;
                    String metricTon = getString(R.string.metric_ton_message, calculation.toString());
                    mTextViewMetricTon.setText(metricTon);

                } else {

                    mTextViewMetricTon.setText(R.string.metric_ton);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //endregion

        //region button confirm
        Button btnConfirm = (Button) batchControllerDialog.findViewById(R.id.button_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPref.edit();

                if (!mEditTextLitre.getText().toString().isEmpty()) {

                    //update job status
                    String litre = mEditTextLitre.getText().toString();
                    String metric = mTextViewMetricTon.getText().toString();

                    editor.putString(SHARED_PREF_BATCH_CONTROLLER_LITRE, litre);
                    editor.putString(SHARED_PREF_BATCH_CONTROLLER, metric);
                    editor.putString(SHARED_PREF_JOB_STATUS, STATUS_BATCH_CONTROLLER);
                    editor.apply();

                    updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_BATCH_CONTROLLER);

                    //set batch controller button
                    mButtonBatchController.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                    mButtonBatchController.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                    mTextViewBatchController.setText(batchController);
                    mButtonPumpStart.setEnabled(true);

                    //close batch controller dialog
                    batchControllerDialog.dismiss();

                } else {

                    //show error message
                    shortToast(getApplicationContext(), BATCH_CONTROLLER_MESSAGE);
                }

            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) batchControllerDialog.findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //close batch controller dialog
                batchControllerDialog.dismiss();

            }
        });
        //endregion

        int dividerId = batchControllerDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = batchControllerDialog.findViewById(dividerId);
        if (divider != null) divider.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent));
        assert batchControllerDialog.getWindow() != null;
        batchControllerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        batchControllerDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        batchControllerDialog.show();
    }
    //endregion

    //region Pump Start

    public void btnPumpStartClicked() {

        if (pumpStartDialog != null && pumpStartDialog.isShowing()) return;

        pumpStartDialog = new Dialog(this);
        pumpStartDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pumpStartDialog.setContentView(R.layout.dialog_pump_start);

        //region button confirm
        Button btnConfirm = (Button) pumpStartDialog.findViewById(R.id.button_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isNetworkAvailable(getApplicationContext())) {

                    //check with webservice
                    new pumpStartWSAsync(sharedPref.getString(SHARED_PREF_JOB_ID, ""), sharedPref.getString(SHARED_PREF_LOGIN_NAME, "")).execute();

                    //close pump start dialog
                    pumpStartDialog.dismiss();

                    //navigate to stop operation activity
                    Intent intent = new Intent(getApplicationContext(), StopOperationActivity.class);
                    isActivityStarted = true;
                    startActivity(intent);

                } else {

                    //update job status
                    final Calendar calendar = Calendar.getInstance();
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(SHARED_PREF_PUMP_START_TIME, simpleDateFormat2.format(calendar.getTime()));
                    editor.putString(SHARED_PREF_JOB_STATUS, STATUS_PUMP_START);
                    editor.apply();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            JobDetail jobDetail = realm.where(JobDetail.class).equalTo("jobID", sharedPref.getString(SHARED_PREF_JOB_ID, "")).findFirst();
                            jobDetail.setJobStatus(STATUS_PUMP_START);
                            jobDetail.setPumpStartTime(simpleDateFormat2.format(calendar.getTime()));

                            realm.copyToRealmOrUpdate(jobDetail);
                        }
                    });

                    //close pump start dialog
                    pumpStartDialog.dismiss();

                    //navigate to stop operation activity
                    Intent intent = new Intent(getApplicationContext(), StopOperationActivity.class);
                    isActivityStarted = true;
                    startActivity(intent);
                }

            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) pumpStartDialog.findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //close pump start dialog
                pumpStartDialog.dismiss();
            }
        });
        //endregion

        int dividerId = pumpStartDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = pumpStartDialog.findViewById(dividerId);
        if (divider != null) divider.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent));
        assert pumpStartDialog.getWindow() != null;
        pumpStartDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pumpStartDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pumpStartDialog.show();
    }

    private class pumpStartWSAsync extends AsyncTask<String, Void, Void> {

        String jobID, updatedBy;
        Boolean response;

        private pumpStartWSAsync(String jobID, String updatedBy) {

            this.jobID = jobID;
            this.updatedBy = updatedBy;
        }

        @Override
        protected Void doInBackground(String... params) {

            Calendar calendar = Calendar.getInstance();
            response = PumpStartWS.invokeUpdatePumpStartWS(jobID, simpleDateFormat3.format(calendar.getTime()), updatedBy);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (response) {

                //update job status
                final Calendar calendar = Calendar.getInstance();
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(SHARED_PREF_PUMP_START_TIME, simpleDateFormat2.format(calendar.getTime()));
                editor.putString(SHARED_PREF_JOB_STATUS, STATUS_PUMP_START);
                editor.apply();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        JobDetail jobDetail = realm.where(JobDetail.class).equalTo("jobID", sharedPref.getString(SHARED_PREF_JOB_ID, "")).findFirst();
                        jobDetail.setJobStatus(STATUS_PUMP_START);
                        jobDetail.setPumpStartTime(simpleDateFormat2.format(calendar.getTime()));

                        realm.copyToRealmOrUpdate(jobDetail);
                    }
                });
            }
        }
    }

    //endregion

    //region Barcode Scanner (Scan Loading Arm)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult scanningIntentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (scanningIntentResult != null) {

            // Retrieve the content of the scan as strings value.
            String scanContent = scanningIntentResult.getContents();

            if (scanContent != null) {

                String returnScanValue = sharedPref.getString(SHARED_PREF_SCAN_VALUE, "");
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(SHARED_PREF_SCAN_VALUE).apply();

                if (returnScanValue.equals(SCAN_VALUE_LOADING_ARM)) {

                    if (isNetworkAvailable(this)) {

                        //check with webservice
                        new loadingArmWSAsync(sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent).execute();

                    } else {

                        if (scanContent.equals(sharedPref.getString(SHARED_PREF_LOADING_ARM, ""))) {

                            //update job status
                            editor.putString(SHARED_PREF_JOB_STATUS, STATUS_SCAN_LOADING_ARM).apply();
                            updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_SCAN_LOADING_ARM);

                            //set loading arm button
                            mButtonScanLoadingArm.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                            mButtonScanLoadingArm.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                            mTextViewScanLoadingArm.setText(scanContent);
                            mButtonBatchController.setEnabled(true);

                        } else {

                            //show error message
                            shortToast(this, ERR_MSG_INVALID_LOADING_ARM);
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

    private class loadingArmWSAsync extends AsyncTask<String, Void, Void> {

        String jobID, loadingArm;
        Boolean response;
        ProgressDialog progressDialog;

        private loadingArmWSAsync(String jobID, String loadingArm) {

            this.jobID = jobID;
            this.loadingArm = loadingArm;
        }

        @Override
        protected void onPreExecute() {

            //start progress dialog
            progressDialog = ProgressDialog.show(getApplicationContext(), "Please wait..", "Loading...", true);
        }

        @Override
        protected Void doInBackground(String... params) {

            response = LoadingArmWS.invokeCheckLoadingArmWS(jobID, loadingArm);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (response) {

                //update job status
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(SHARED_PREF_JOB_STATUS, STATUS_SCAN_LOADING_ARM).apply();
                updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_SCAN_LOADING_ARM);

                //set loading arm button
                mButtonScanLoadingArm.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                mButtonScanLoadingArm.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                mTextViewScanLoadingArm.setText(loadingArm);
                mButtonBatchController.setEnabled(true);

                //close progress dialog
                progressDialog.dismiss();

            } else {

                //close progress dialog
                progressDialog.dismiss();

                //show error message
                shortToast(getApplicationContext(), ERR_MSG_INVALID_LOADING_ARM);
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
