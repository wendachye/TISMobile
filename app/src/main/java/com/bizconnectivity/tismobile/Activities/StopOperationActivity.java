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
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.SealDetailDataSource;
import com.bizconnectivity.tismobile.webservices.AddSealWSAsync;
import com.bizconnectivity.tismobile.webservices.CheckSealWSAsync;
import com.bizconnectivity.tismobile.webservices.DepartureWSAsync;
import com.bizconnectivity.tismobile.webservices.PumpStopWSAsync;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import static com.bizconnectivity.tismobile.Common.isNetworkAvailable;
import static com.bizconnectivity.tismobile.Common.shortToast;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_BOTTOM_SEAL1;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_BOTTOM_SEAL2;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_BOTTOM_SEAL3;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_BOTTOM_SEAL4;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_BATCH_CONTROLLER;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_BATCH_CONTROLLER_LITRE;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_CUSTOMER_NAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_DRIVER_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_DATE;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_STATUS;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOADING_ARM;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOADING_BAY;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_OPERATOR_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_PRODUCT_NAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_PUMP_START_TIME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_PUMP_STOP_TIME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_RACK_OUT_TIME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_SDS_FILE_PATH;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_TANK_NO;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_WORK_INSTRUCTION;
import static com.bizconnectivity.tismobile.Constant.STATUS_PUMP_START;
import static com.bizconnectivity.tismobile.Constant.STATUS_PUMP_STOP;
import static com.bizconnectivity.tismobile.Constant.STATUS_SCAN_SEAL;
import static com.bizconnectivity.tismobile.Constant.calendar;
import static com.bizconnectivity.tismobile.Constant.simpleDateFormat2;

public class StopOperationActivity extends AppCompatActivity {

    //region declaration
    Context context;
    LinearLayout footerLayout;
    LinearLayout headerLayout;
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;
    TextView headerMessage, tvPumpStop, tv_jobID, tv_customerName, tv_loadingBay, tv_loadingArm;
    Dialog exitDialog, pumpStopDialog, scanSealDialog, departureDialog;
    Button btnPumpStop, btnScanSeal, btnDeparture;
    SharedPreferences sharedPref;
    JobDetailDataSource jobDetailDataSource;
    SealDetailDataSource sealDetailDataSource;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_operation);

        context = this;
        sharedPref = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //region Header and Footer
        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        /*-------- Set User Login Details --------*/
        setUserLoginDetails();

        /*-------- footer buttons --------*/
        setFooterMenu();
        //endregion

        //region button pump stop
        tvPumpStop = (TextView) findViewById(R.id.tvPumpStop);
        btnPumpStop = (Button) findViewById(R.id.btnPumpStop);
        btnPumpStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPumpStopClicked();
            }
        });
        //endregion

        //region button scan seal
        btnScanSeal = (Button) findViewById(R.id.btnScanSeal);
        btnScanSeal.setEnabled(false);
        btnScanSeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnScanSealClicked();
            }
        });
        //endregion

        //region button departure
        btnDeparture = (Button) findViewById(R.id.btnDeparture);
        btnDeparture.setEnabled(false);
        btnDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnDepartureClicked();
            }
        });
        //endregion

        //region status settings

        //retrieve job status from shared preferences
        String jobStatus = sharedPref.getString(SHARED_PREF_JOB_STATUS, "");
        String pumpStopTime = sharedPref.getString(SHARED_PREF_PUMP_STOP_TIME, "");

        switch (jobStatus) {

            case STATUS_PUMP_START:
                btnPumpStop.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                btnPumpStop.getBackground().clearColorFilter();
                tvPumpStop.setText("");

                btnScanSeal.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                btnScanSeal.getBackground().clearColorFilter();

                btnDeparture.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                btnDeparture.getBackground().clearColorFilter();
                break;

            case STATUS_PUMP_STOP:
                btnPumpStop.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnPumpStop.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                btnPumpStop.setEnabled(false);
                tvPumpStop.setText(pumpStopTime);

                btnScanSeal.setEnabled(true);
                break;

            case STATUS_SCAN_SEAL:
                btnPumpStop.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnPumpStop.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                btnPumpStop.setEnabled(false);
                tvPumpStop.setText(pumpStopTime);

                btnScanSeal.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnScanSeal.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                btnScanSeal.setEnabled(true);

                btnDeparture.setEnabled(true);
                break;

            default:

                break;
        }

        //endregion
    }

    //region Header
    /*-------- Set User Login Details --------*/
    public void setUserLoginDetails() {

        headerLayout = (LinearLayout) findViewById(R.id.headerSO);
        headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);
        tv_jobID = (TextView) headerLayout.findViewById(R.id.tvOrderId);
        tv_customerName = (TextView) headerLayout.findViewById(R.id.tvCustomer);
        tv_loadingBay = (TextView) headerLayout.findViewById(R.id.tvBay);
        tv_loadingArm = (TextView) headerLayout.findViewById(R.id.tvArm);

        //retrieve shared preferences
        String welcomeMessage = sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "");
        String jobID = sharedPref.getString(SHARED_PREF_JOB_ID, "");
        String customerName = sharedPref.getString(SHARED_PREF_CUSTOMER_NAME, "");
        String loadingBay = sharedPref.getString(SHARED_PREF_LOADING_BAY, "");
        String loadingArm = sharedPref.getString(SHARED_PREF_LOADING_ARM, "");

        //set text
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

        Intent intentSwitchTruckBay = new Intent(context, SwitchJobActivity.class);
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
                editor.apply();

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
            divider.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent));
        }
        assert exitDialog.getWindow() != null;
        exitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        exitDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        exitDialog.show();
    }
    //endregion

    public void btnPumpStopClicked() {

        if (pumpStopDialog != null && pumpStopDialog.isShowing())
            return;

        pumpStopDialog = new Dialog(this);
        pumpStopDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        pumpStopDialog.setContentView(R.layout.dialog_pump_stop);

        //region button confirm
        Button btnConfirm = (Button) pumpStopDialog.findViewById(R.id.btnConfirm);
        // if button is clicked, close the custom dialog
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

				String jobID = sharedPref.getString(SHARED_PREF_JOB_ID, "");
				String loginName = sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "");

                if (isNetworkAvailable(context)) {

                    PumpStopWSAsync task = new PumpStopWSAsync(context, pumpStopDialog, jobID, loginName);
                    task.execute();

                } else {

                    jobDetailDataSource = new JobDetailDataSource(getApplicationContext());
                    jobDetailDataSource.open();
                    jobDetailDataSource.updatePumpStop(jobID);
                    jobDetailDataSource.updateJobStatus(sharedPref.getString(Constant.SHARED_PREF_JOB_ID, ""), STATUS_PUMP_STOP);
                    jobDetailDataSource.close();

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(SHARED_PREF_PUMP_STOP_TIME, simpleDateFormat2.format(calendar.getTime()));
                    editor.putString(SHARED_PREF_JOB_STATUS, STATUS_PUMP_STOP);
                    editor.apply();

                    pumpStopDialog.dismiss();

                    Intent intent = new Intent(getApplicationContext(), StopOperationActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) pumpStopDialog.findViewById(R.id.btnCancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pumpStopDialog.dismiss();
            }
        });
        //endregion

        int dividerId = pumpStopDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = pumpStopDialog.findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent));
        }
        assert pumpStopDialog.getWindow() != null;
        pumpStopDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pumpStopDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pumpStopDialog.show();
    }

    public void btnScanSealClicked() {

        SharedPreferences.Editor editor = sharedPref.edit();

        if (sharedPref.getString(SCAN_VALUE_BOTTOM_SEAL1, "").isEmpty()) {

            editor.putString(Constant.SHARED_PREF_SCAN_VALUE, SCAN_VALUE_BOTTOM_SEAL1).apply();

            IntentIntegrator integrator = new IntentIntegrator(this);
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt(Constant.SCAN_MSG_PROMPT_SCAN_BOTTOM_SEAL);
            integrator.setBeepEnabled(true);
            integrator.initiateScan();

        } else {

            scanSealDialog();
        }
    }

    public void scanSealDialog() {

        if (scanSealDialog != null && scanSealDialog.isShowing())
            return;

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
	    Button btnConfirm = (Button) scanSealDialog.findViewById(R.id.btnConfirm);
        // if button is clicked, close the custom dialog
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

	            String jobID = sharedPref.getString(SHARED_PREF_JOB_ID, "");
	            String updatedBy = sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "");
	            String sealPos = "bottom";
	            int totalCount = countSeal.size();

                if (isNetworkAvailable(getApplicationContext())) {

                    AddSealWSAsync task = new AddSealWSAsync(context, scanSealDialog, totalCount, countSeal, jobID, sealPos, updatedBy);
                    task.execute();

                } else {

                    for (int i=0; i<countSeal.size(); i++) {

                        sealDetailDataSource = new SealDetailDataSource(getApplicationContext());
                        sealDetailDataSource.open();
                        sealDetailDataSource.insertSealDetails(jobID, countSeal.get(i));
                        sealDetailDataSource.close();

                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString(SHARED_PREF_JOB_STATUS, STATUS_SCAN_SEAL).apply();

                        jobDetailDataSource = new JobDetailDataSource(context);
                        jobDetailDataSource.open();
                        jobDetailDataSource.updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_SCAN_SEAL);
                        jobDetailDataSource.close();

                        scanSealDialog.dismiss();

                        Intent intent = new Intent(getApplicationContext(), StopOperationActivity.class);
                        finish();
                        startActivity(intent);
                    }
                }

            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) scanSealDialog.findViewById(R.id.btnCancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

            editor.putString(Constant.SHARED_PREF_SCAN_VALUE, SCAN_VALUE_BOTTOM_SEAL2).apply();

        } else if (seal3.isEmpty()) {

            editor.putString(Constant.SHARED_PREF_SCAN_VALUE, SCAN_VALUE_BOTTOM_SEAL3).apply();

        } else if (seal4.isEmpty()) {

            editor.putString(Constant.SHARED_PREF_SCAN_VALUE, SCAN_VALUE_BOTTOM_SEAL4).apply();
        }

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(Constant.SCAN_MSG_PROMPT_SCAN_BOTTOM_SEAL);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    public void btnDepartureClicked() {

        if (departureDialog != null && departureDialog.isShowing())
            return;

        departureDialog = new Dialog(this);
        departureDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        departureDialog.setContentView(R.layout.dialog_departure);

        //region button confirm
        Button btnConfirm = (Button) departureDialog.findViewById(R.id.btnConfirm);
        // if button is clicked, close the custom dialog
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

	            String jobID = sharedPref.getString(SHARED_PREF_JOB_ID, "");
	            String updatedBy = sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "");

                if (isNetworkAvailable(getApplicationContext())) {

                    DepartureWSAsync task = new DepartureWSAsync(context, departureDialog, jobID, updatedBy);
                    task.execute();

                } else {

                    jobDetailDataSource = new JobDetailDataSource(getApplicationContext());
                    jobDetailDataSource.open();
                    jobDetailDataSource.updateDepartureTime(jobID);
                    jobDetailDataSource.close();

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

                    departureDialog.dismiss();

                    shortToast(getApplicationContext(), "Data stored in local due to no internet connection, please sync the data after the internet available.");

                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) departureDialog.findViewById(R.id.btnCancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                departureDialog.dismiss();
            }
        });
        //endregion

        int dividerId = departureDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = departureDialog.findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent));
        }
        assert departureDialog.getWindow() != null;
        departureDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        departureDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        departureDialog.show();
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

                if (returnScanValue.equals(SCAN_VALUE_BOTTOM_SEAL1)) {

                    if (isNetworkAvailable(this)) {

                        CheckSealWSAsync task = new CheckSealWSAsync(this, sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent);
                        task.execute();

                    } else {

                        sealDetailDataSource = new SealDetailDataSource(this);
                        sealDetailDataSource.open();
                        sealDetailDataSource.checkRetrieveSealNo(sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent);
                        sealDetailDataSource.close();
                    }

                } else if (returnScanValue.equals(SCAN_VALUE_BOTTOM_SEAL2)) {

                    if (isNetworkAvailable(this)) {

                        CheckSealWSAsync task = new CheckSealWSAsync(this, sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent);
                        task.execute();

                    } else {

                        sealDetailDataSource = new SealDetailDataSource(this);
                        sealDetailDataSource.open();
                        boolean response = sealDetailDataSource.checkRetrieveSealNo(sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent);
                        sealDetailDataSource.close();

                        if (response) {


                        } else {


                        }
                    }

                } else if (returnScanValue.equals(SCAN_VALUE_BOTTOM_SEAL3)) {

                    if (isNetworkAvailable(this)) {

                        CheckSealWSAsync task = new CheckSealWSAsync(this, sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent);
                        task.execute();

                    } else {

                        sealDetailDataSource = new SealDetailDataSource(this);
                        sealDetailDataSource.open();
                        sealDetailDataSource.checkRetrieveSealNo(sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent);
                        sealDetailDataSource.close();
                    }

                } else if (returnScanValue.equals(SCAN_VALUE_BOTTOM_SEAL4)) {

                    if (isNetworkAvailable(this)) {

                        CheckSealWSAsync task = new CheckSealWSAsync(this, sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent);
                        task.execute();

                    } else {

                        sealDetailDataSource = new SealDetailDataSource(this);
                        sealDetailDataSource.open();
                        sealDetailDataSource.checkRetrieveSealNo(sharedPref.getString(SHARED_PREF_JOB_ID, ""), scanContent);
                        sealDetailDataSource.close();
                    }
                }
                else {

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
