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
import com.bizconnectivity.tismobile.database.models.LoadingBayDetail;
import com.bizconnectivity.tismobile.webservices.DriverIDWS;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.bizconnectivity.tismobile.Common.*;
import static com.bizconnectivity.tismobile.Constant.*;

public class ScanDetailsActivity extends AppCompatActivity {

    //region declaration

    //header
    @BindView(R.id.header_scan_details)
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
    @BindView(R.id.button_operator_id)
    Button mButtonOperatorID;
    @BindView(R.id.button_driver_id)
    Button mButtonDriverID;
    @BindView(R.id.button_work_instruction)
    Button mButtonWorkInstruction;
    @BindView(R.id.text_operator_id)
    TextView mTextViewOperatorID;
    @BindView(R.id.text_driver_id)
    TextView mTextViewDriverID;

    //footer
    @BindView(R.id.footer_scan_details)
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
    Dialog exitDialog;
    PopupMenu popupMenu;
    String jobStatus, operatorID, driverID;
    SharedPreferences sharedPref;
    boolean isActivityStarted = false;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_details);

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

        //retrieve job details from shared preferences
        jobStatus = sharedPref.getString(SHARED_PREF_JOB_STATUS, "");
        operatorID = sharedPref.getString(SHARED_PREF_OPERATOR_ID, "");
        driverID = sharedPref.getString(SHARED_PREF_DRIVER_ID, "");

        switch (jobStatus) {

            case STATUS_SDS:
                mButtonOperatorID.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                mButtonOperatorID.getBackground().clearColorFilter();
                mTextViewOperatorID.setText("");

                mButtonDriverID.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                mButtonDriverID.getBackground().clearColorFilter();
                mTextViewDriverID.setText("");

                mButtonWorkInstruction.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                mButtonWorkInstruction.getBackground().clearColorFilter();
                break;

            case STATUS_OPERATOR_ID:
                mButtonOperatorID.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonOperatorID.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mTextViewOperatorID.setText(operatorID);

                mButtonDriverID.setEnabled(true);
                break;

            case STATUS_DRIVER_ID:
                mButtonOperatorID.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonOperatorID.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mTextViewOperatorID.setText(operatorID);

                mButtonDriverID.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonDriverID.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mButtonDriverID.setEnabled(true);
                mTextViewDriverID.setText(driverID);

                mButtonWorkInstruction.setEnabled(true);
                break;

            default:
                break;
        }

        //endregion
    }

    @OnClick(R.id.button_operator_id)
    public void btnOperatorID(View view) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_OPERATOR_ID).apply();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(SCAN_MSG_PROMPT_OPERATOR_ID);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @OnClick(R.id.button_driver_id)
    public void btnDriverID(View view) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_DRIVER_ID).apply();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(SCAN_MSG_PROMPT_DRIVER_ID);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    @OnClick(R.id.button_work_instruction)
    public void btnWorkInstruction(View view) {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_WORK_INSTRUCTION).apply();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(SCAN_MSG_PROMPT_WORK_INSTRUCTION);
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
                editor.remove(SHARED_PREF_SCAN_VALUE).apply();

                if (returnScanValue.equals(SCAN_VALUE_OPERATOR_ID)) {

                    //update job status
                    editor.putString(SHARED_PREF_JOB_STATUS, STATUS_OPERATOR_ID).apply();
                    updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_OPERATOR_ID);

                    //set operator id button
                    mTextViewOperatorID.setText(scanContent);
                    mButtonOperatorID.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                    mButtonOperatorID.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));

                    mButtonDriverID.setEnabled(true);

                } else if (returnScanValue.equals(SCAN_VALUE_DRIVER_ID)) {

                    if (isNetworkAvailable(this)) {

                        new driverIDWSAsync(sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent).execute();

                    } else {

                        if (scanContent.equals(sharedPref.getString(SHARED_PREF_DRIVER_ID, ""))) {

                            //update job status
                            editor.putString(SHARED_PREF_JOB_STATUS, STATUS_DRIVER_ID).apply();
                            updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_DRIVER_ID);

                            //set driver id button
                            mButtonDriverID.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                            mButtonDriverID.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                            mTextViewDriverID.setText(scanContent);
                            mButtonWorkInstruction.setEnabled(true);

                        } else {

                            //show error message
                            shortToast(this, SCAN_MSG_INVALID_DRIVER_ID_RECEIVED);
                        }
                    }

                } else if (returnScanValue.equals(SCAN_VALUE_WORK_INSTRUCTION)) {

                    if (scanContent.equals(sharedPref.getString(SHARED_PREF_JOB_ID, ""))) {

                        //update job status
                        editor.putString(SHARED_PREF_JOB_STATUS, STATUS_WORK_INSTRUCTION).apply();
                        updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_WORK_INSTRUCTION);

                        //navigate to job main activity
                        Intent intent = new Intent(this, JobMainActivity.class);
                        isActivityStarted = true;
                        startActivity(intent);

                    } else {

                        //show error message
                        shortToast(this, SCAN_MSG_INVALID_WORK_INSTRUCTION_RECEIVED);
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

    private class driverIDWSAsync extends AsyncTask<String, Void, Void> {

        String jobID, driverID;
        Boolean response;
        ProgressDialog progressDialog;

        private driverIDWSAsync(String jobID, String driverID) {

            this.jobID = jobID;
            this.driverID = driverID;
        }

        @Override
        protected void onPreExecute() {

            //start progress dialog
            progressDialog = ProgressDialog.show(ScanDetailsActivity.this, "Please wait..", "Loading...", true);
        }

        @Override
        protected Void doInBackground(String... params) {

            response = DriverIDWS.invokeRetrieveDriverID(jobID, driverID);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (response) {

                //update job status
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(SHARED_PREF_JOB_STATUS, STATUS_DRIVER_ID).apply();
                updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_DRIVER_ID);

                //set driver id button
                mButtonDriverID.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                mButtonDriverID.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                mTextViewDriverID.setText(driverID);
                mButtonWorkInstruction.setEnabled(true);

                //close progress dialog
                progressDialog.dismiss();

            } else {

                //close progress dialog
                progressDialog.dismiss();

                //show error message
                shortToast(getApplicationContext(), SCAN_MSG_INVALID_DRIVER_ID_RECEIVED);
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
