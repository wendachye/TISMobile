package com.bizconnectivity.tismobile.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.WebServices.DriverIDWSAsync;
import com.bizconnectivity.tismobile.WebServices.WorkInstructionWSAsync;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class ScanDetailsActivity extends AppCompatActivity {

    Context context;
    TextView headerMessage, tvOperatorId, tvDriverId, tv_jobID, tv_customerName, tv_loadingBay, tv_loadingArm;
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;
    Dialog exitDialog;
    Button btnScanOperatorId, btnScanDriverId, btnScanWorkInstruction;

    public SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_details);

        context = this;
        sharedPref = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //region Header and Footer
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        /*-------- Set User Login Details --------*/
        setUserLoginDetails();

        /*-------- footer buttons --------*/
        setFooterMenu();
        //endregion

        tvOperatorId = (TextView) findViewById(R.id.tvOperatorId);
        tvDriverId = (TextView) findViewById(R.id.tvDriverId);

        btnScanOperatorId = (Button) findViewById(R.id.btnScanOperatorId);
        btnScanOperatorId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnScanOperatorIdClicked(view);
            }
        });

        btnScanDriverId = (Button) findViewById(R.id.btnScanDriverId);
        btnScanDriverId.setEnabled(false);
        btnScanDriverId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnScanDriverIdClicked(view);
            }
        });

        btnScanWorkInstruction = (Button) findViewById(R.id.btnScanWorkInstruction);
        btnScanWorkInstruction.setEnabled(false);
        btnScanWorkInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnScanWorkInstructionClicked(view);
            }
        });

        String operatorID = sharedPref.getString(Constant.SHARED_PREF_OPERATOR_ID, "");
        String driverID = sharedPref.getString(Constant.SHARED_PREF_DRIVER_ID, "");
        String workInstruction = sharedPref.getString(Constant.SHARED_PREF_WORK_INSTRUCTION, "");

        if (!operatorID.isEmpty() && !driverID.isEmpty() && !workInstruction.isEmpty()) {

            btnScanOperatorId.setTextColor(getResources().getColor(R.color.colorWhite));
            btnScanOperatorId.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            tvOperatorId.setText(operatorID);

            btnScanDriverId.setTextColor(getResources().getColor(R.color.colorWhite));
            btnScanDriverId.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            btnScanDriverId.setEnabled(true);
            tvDriverId.setText(driverID);

            btnScanWorkInstruction.setTextColor(getResources().getColor(R.color.colorWhite));
            btnScanWorkInstruction.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            btnScanWorkInstruction.setEnabled(true);

        } else if (!operatorID.isEmpty() && !driverID.isEmpty()) {

            btnScanOperatorId.setTextColor(getResources().getColor(R.color.colorWhite));
            btnScanOperatorId.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            tvOperatorId.setText(operatorID);

            btnScanDriverId.setTextColor(getResources().getColor(R.color.colorWhite));
            btnScanDriverId.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            btnScanDriverId.setEnabled(true);
            tvDriverId.setText(driverID);

            btnScanWorkInstruction.setEnabled(true);

        } else if (!operatorID.isEmpty()) {

            btnScanOperatorId.setTextColor(getResources().getColor(R.color.colorWhite));
            btnScanOperatorId.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            tvOperatorId.setText(operatorID);

            btnScanDriverId.setEnabled(true);

        }

    }

    //region Header
    /*-------- Set User Login Details --------*/
    public void setUserLoginDetails() {

        LinearLayout headerLayout = (LinearLayout) findViewById(R.id.header);
        headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);
        tv_jobID = (TextView) headerLayout.findViewById(R.id.tvOrderId);
        tv_customerName = (TextView) headerLayout.findViewById(R.id.tvCustomer);
        tv_loadingBay = (TextView) headerLayout.findViewById(R.id.tvBay);
        tv_loadingArm = (TextView) headerLayout.findViewById(R.id.tvArm);

        //retrieve shared preferences
        sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "");
        String welcomeMessage = sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "");
        String jobID = sharedPref.getString(Constant.SHARED_PREF_JOB_ID, "");
        String customerName = sharedPref.getString(Constant.SHARED_PREF_CUSTOMER_NAME, "");
        String loadingBay = sharedPref.getString(Constant.SHARED_PREF_LOADING_BAY, "");
        String loadingArm = sharedPref.getString(Constant.SHARED_PREF_LOADING_ARM, "");

        headerMessage.setText(Common.formatWelcomeMsg(welcomeMessage));
        tv_jobID.setText(jobID);
        tv_customerName.setText(customerName);
        tv_loadingBay.setText(loadingArm);
        tv_loadingArm.setText(loadingBay);
    }
    //endregion

    //region Footer
    public void setFooterMenu() {

        RelativeLayout footerLayout = (RelativeLayout) findViewById(R.id.footer);
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

        Intent intentHome = new Intent(context, DashboardActivity.class);
        finish();
        startActivity(intentHome);
    }

    public void btnSearchClicked() {

        Intent intentSearchJob = new Intent(context, SearchJobActivity.class);
        finish();
        startActivity(intentSearchJob);
    }

    public void btnSwitchClicked() {

        Intent intentSwitchTruckBay = new Intent(context, SwitchTruckBayActivity.class);
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
                SharedPreferences sharedPref = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
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

    public void btnScanOperatorIdClicked(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.SHARED_PREF_SCAN_VALUE, Constant.SCAN_VALUE_OPERATOR_ID);
        editor.commit();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(Constant.SCAN_MSG_PROMPT_OPERATOR_ID);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    public void btnScanDriverIdClicked(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.SHARED_PREF_SCAN_VALUE, Constant.SCAN_VALUE_DRIVER_ID);
        editor.commit();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(Constant.SCAN_MSG_PROMPT_DRIVER_ID);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    public void btnScanWorkInstructionClicked(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.SHARED_PREF_SCAN_VALUE, Constant.SCAN_VALUE_WORK_INSTRUCTION);
        editor.commit();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(Constant.SCAN_MSG_PROMPT_WORK_INSTRUCTION);
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
                String returnScanValue = sharedPref.getString(Constant.SHARED_PREF_SCAN_VALUE, "");
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(Constant.SHARED_PREF_SCAN_VALUE);
                editor.apply();

                if (returnScanValue.equals(Constant.SCAN_VALUE_OPERATOR_ID)) {

                    editor.putString(Constant.SHARED_PREF_OPERATOR_ID, scanContent).commit();

                    tvOperatorId.setText(scanContent);
                    btnScanOperatorId.setTextColor(getResources().getColor(R.color.colorWhite));
                    btnScanOperatorId.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                    btnScanDriverId.setEnabled(true);

                } else if (returnScanValue.equals(Constant.SCAN_VALUE_DRIVER_ID)) {

                    String selectedJobID = sharedPref.getString(Constant.SHARED_PREF_ORDER_ID_SELECTED, "");
                    DriverIDWSAsync task = new DriverIDWSAsync(this, selectedJobID, scanContent);
                    task.execute();

                } else if (returnScanValue.equals(Constant.SCAN_VALUE_WORK_INSTRUCTION)) {

                    String selectedJobID = sharedPref.getString(Constant.SHARED_PREF_ORDER_ID_SELECTED, "");
                    WorkInstructionWSAsync task = new WorkInstructionWSAsync(this, selectedJobID, scanContent);
                    task.execute();

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
		finish();
		Intent intent = new Intent(this, JobMainActivity.class);
		startActivity(intent);
	}
}
