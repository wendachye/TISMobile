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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.database.datasources.LoadingBayDetailDataSource;
import com.bizconnectivity.tismobile.webservices.DriverIDWSAsync;
import com.bizconnectivity.tismobile.webservices.WorkInstructionWSAsync;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.bizconnectivity.tismobile.Constant.SCAN_MSG_PROMPT_DRIVER_ID;
import static com.bizconnectivity.tismobile.Constant.SCAN_MSG_PROMPT_OPERATOR_ID;
import static com.bizconnectivity.tismobile.Constant.SCAN_MSG_PROMPT_WORK_INSTRUCTION;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_DRIVER_ID;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_OPERATOR_ID;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_WORK_INSTRUCTION;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_DRIVER_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_STATUS;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_NAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_OPERATOR_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_SCAN_VALUE;
import static com.bizconnectivity.tismobile.Constant.STATUS_DRIVER_ID;
import static com.bizconnectivity.tismobile.Constant.STATUS_OPERATOR_ID;
import static com.bizconnectivity.tismobile.Constant.STATUS_SDS;
import static com.bizconnectivity.tismobile.Constant.STATUS_WORK_INSTRUCTION;

public class ScanDetailsActivity extends AppCompatActivity {

    //region declaration
    TextView headerMessage, tvOperatorId, tvDriverId, tv_jobID, tv_customerName, tv_loadingBay, tv_loadingArm;
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;
    Dialog exitDialog;
    LinearLayout footerLayout;
    LinearLayout headerLayout;
    Button btnScanOperatorId, btnScanDriverId, btnScanWorkInstruction;
    SharedPreferences sharedPref;
    JobDetailDataSource jobDetailDataSource;
    LoadingBayDetailDataSource loadingBayDetailDataSource;
    String jobStatus, operatorID, driverID, welcomeMessage, jobID, customerName, loadingBay, loadingArm;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_details);

        sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //region Header and Footer
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        /*-------- Set User Login Details --------*/
        setUserLoginDetails();

        /*-------- footer buttons --------*/
        setFooterMenu();
        //endregion

        //region button operator id
        tvOperatorId = (TextView) findViewById(R.id.tvOperatorId);
        btnScanOperatorId = (Button) findViewById(R.id.btnScanOperatorId);
        btnScanOperatorId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnScanOperatorIdClicked();
            }
        });
        //endregion

        //region button driver id
        tvDriverId = (TextView) findViewById(R.id.tvDriverId);
        btnScanDriverId = (Button) findViewById(R.id.btnScanDriverId);
        btnScanDriverId.setEnabled(false);
        btnScanDriverId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnScanDriverIdClicked();
            }
        });
        //endregion

        //region button work instruction
        btnScanWorkInstruction = (Button) findViewById(R.id.btnScanWorkInstruction);
        btnScanWorkInstruction.setEnabled(false);
        btnScanWorkInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnScanWorkInstructionClicked();
            }
        });
        //endregion

        //region status settings

        //retrieve job details from shared preferences
        jobStatus = sharedPref.getString(SHARED_PREF_JOB_STATUS, "");
        operatorID = sharedPref.getString(SHARED_PREF_OPERATOR_ID, "");
        driverID = sharedPref.getString(SHARED_PREF_DRIVER_ID, "");

        switch (jobStatus) {

            case STATUS_SDS:
                btnScanOperatorId.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                btnScanOperatorId.getBackground().clearColorFilter();
                tvOperatorId.setText("");

                btnScanDriverId.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                btnScanDriverId.getBackground().clearColorFilter();
                tvDriverId.setText("");

                btnScanWorkInstruction.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                btnScanWorkInstruction.getBackground().clearColorFilter();
                break;

            case STATUS_OPERATOR_ID:
                btnScanOperatorId.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnScanOperatorId.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                tvOperatorId.setText(operatorID);

                btnScanDriverId.setEnabled(true);
                break;

            case STATUS_DRIVER_ID:
                btnScanOperatorId.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnScanOperatorId.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                tvOperatorId.setText(operatorID);

                btnScanDriverId.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnScanDriverId.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                btnScanDriverId.setEnabled(true);
                tvDriverId.setText(driverID);

                btnScanWorkInstruction.setEnabled(true);
                break;

            default:
                btnScanOperatorId.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnScanOperatorId.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                tvOperatorId.setText(operatorID);

                btnScanDriverId.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnScanDriverId.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                btnScanDriverId.setEnabled(true);
                tvDriverId.setText(driverID);

                btnScanWorkInstruction.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnScanWorkInstruction.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                btnScanWorkInstruction.setEnabled(true);
                break;

        }

        //endregion
    }

    //region Header
    /*-------- Set User Login Details --------*/
    public void setUserLoginDetails() {

        headerLayout = (LinearLayout) findViewById(R.id.headerSD);
        headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);
        tv_jobID = (TextView) headerLayout.findViewById(R.id.tvOrderId);
        tv_customerName = (TextView) headerLayout.findViewById(R.id.tvCustomer);
        tv_loadingBay = (TextView) headerLayout.findViewById(R.id.tvBay);
        tv_loadingArm = (TextView) headerLayout.findViewById(R.id.tvArm);

        //retrieve shared preferences
        sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "");
        welcomeMessage = sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "");
        jobID = sharedPref.getString(SHARED_PREF_JOB_ID, "");
        customerName = sharedPref.getString(Constant.SHARED_PREF_CUSTOMER_NAME, "");
        loadingBay = sharedPref.getString(Constant.SHARED_PREF_LOADING_BAY, "");
        loadingArm = sharedPref.getString(Constant.SHARED_PREF_LOADING_ARM, "");

        headerMessage.setText(Common.formatWelcomeMsg(welcomeMessage));
        tv_jobID.setText(jobID);
        tv_customerName.setText(customerName);
        tv_loadingBay.setText(loadingArm);
        tv_loadingArm.setText(loadingBay);
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
        finish();
        startActivity(intentHome);
    }

    public void btnSearchClicked() {

        Intent intentSearchJob = new Intent(this, SearchJobActivity.class);
        finish();
        startActivity(intentSearchJob);
    }

    public void btnSwitchClicked() {

        Intent intentSwitchTruckBay = new Intent(this, SwitchJobActivity.class);
        finish();
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

                    case R.id.settingsMenuSyncData:
                        Intent intentSyncData = new Intent(getApplicationContext(), SyncDataActivity.class);
                        finish();
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

    public void btnScanOperatorIdClicked() {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_OPERATOR_ID).apply();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(SCAN_MSG_PROMPT_OPERATOR_ID);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    public void btnScanDriverIdClicked() {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_DRIVER_ID).apply();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(SCAN_MSG_PROMPT_DRIVER_ID);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    public void btnScanWorkInstructionClicked() {

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
                editor.remove(SHARED_PREF_SCAN_VALUE);
                editor.apply();

                if (returnScanValue.equals(SCAN_VALUE_OPERATOR_ID)) {

                    //region update job status
                    editor.putString(SHARED_PREF_JOB_STATUS, STATUS_OPERATOR_ID).commit();

                    jobDetailDataSource = new JobDetailDataSource(this);
                    jobDetailDataSource.open();
                    jobDetailDataSource.updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_OPERATOR_ID);
                    jobDetailDataSource.close();
                    //endregion

                    //region button operator setup
                    tvOperatorId.setText(scanContent);
                    btnScanOperatorId.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                    btnScanOperatorId.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));

                    btnScanDriverId.setEnabled(true);
                    //endregion

                } else if (returnScanValue.equals(SCAN_VALUE_DRIVER_ID)) {

                    if (Common.isNetworkAvailable(this)) {

                        DriverIDWSAsync task = new DriverIDWSAsync(this, sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent);
                        task.execute();

                    } else {

                        if (scanContent.equals(sharedPref.getString(SHARED_PREF_DRIVER_ID, ""))) {

                            editor.putString(Constant.SHARED_PREF_JOB_STATUS, STATUS_DRIVER_ID).apply();

                            jobDetailDataSource = new JobDetailDataSource(this);
                            jobDetailDataSource.open();
                            jobDetailDataSource.updateJobStatus(sharedPref.getString(Constant.SHARED_PREF_JOB_ID, ""), STATUS_DRIVER_ID);
                            jobDetailDataSource.close();

                            Intent intent = new Intent(this, ScanDetailsActivity.class);
                            finish();
                            startActivity(intent);

                        } else {

                            Common.shortToast(this, Constant.SCAN_MSG_INVALID_DRIVER_ID_RECEIVED);
                        }
                    }

                } else if (returnScanValue.equals(SCAN_VALUE_WORK_INSTRUCTION)) {

                    if (Common.isNetworkAvailable(this)) {

                        WorkInstructionWSAsync task = new WorkInstructionWSAsync(this, sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent);
                        task.execute();

                    } else {

                        if (scanContent.equals(sharedPref.getString(SHARED_PREF_JOB_ID, ""))) {

                            editor.putString(SHARED_PREF_JOB_STATUS, STATUS_WORK_INSTRUCTION).apply();

                            jobDetailDataSource = new JobDetailDataSource(this);
                            jobDetailDataSource.open();
                            jobDetailDataSource.updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_WORK_INSTRUCTION);
                            jobDetailDataSource.close();

                            Intent intent = new Intent(this, JobMainActivity.class);
                            finish();
                            startActivity(intent);

                        } else {

                            Common.shortToast(this, Constant.SCAN_MSG_INVALID_WORK_INSTRUCTION_RECEIVED);
                        }
                    }

                } else {
                    Common.shortToast(this, Constant.SCAN_MSG_INVALID_DATA_RECEIVED);
                }
            } else {
                Common.shortToast(this, Constant.SCAN_MSG_NO_DATA_RECEIVED);
            }
        } else {
            // If scan data is not received (for example, if the user cancels the scan by pressing the back button),
            // we can simply output a message.
            Common.shortToast(this, Constant.SCAN_MSG_CANCEL_SCANNING);
        }
    }
    //endregion

	public void onBackPressed() {

	}
}
