package com.bizconnectivity.tismobile.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.DataSources.JobDetailDataSource;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.webservices.LoadingArmWSAsync;
import com.bizconnectivity.tismobile.webservices.PumpStartWSAsync;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_BATCH_CONTROLLER;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_STATUS;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOADING_ARM;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_PUMP_START_TIME;
import static com.bizconnectivity.tismobile.Constant.STATUS_BATCH_CONTROLLER;
import static com.bizconnectivity.tismobile.Constant.STATUS_PUMP_START;
import static com.bizconnectivity.tismobile.Constant.STATUS_SAFETY_CHECKS;
import static com.bizconnectivity.tismobile.Constant.STATUS_SCAN_LOADING_ARM;

public class LoadingOperationActivity extends AppCompatActivity {

    Context context;
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;
    TextView headerMessage, tvScanLoadingArm, tvBatchController, tvPumpStart, tv_jobID, tv_customerName, tv_loadingBay, tv_loadingArm;
    Dialog exitDialog, batchControllerDialog, pumpStartDialog;
    Button btnScanLoadingArm, btnBatchController, btnPumpStart;

    public SharedPreferences sharedPref;

    JobDetailDataSource jobDetailDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_operation);

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

        //region button loading arm
        tvScanLoadingArm = (TextView) findViewById(R.id.tvScanLoadingArm);
        btnScanLoadingArm = (Button) findViewById(R.id.btnScanLoadingArm);
        btnScanLoadingArm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnScanLoadingArmClicked();
            }
        });
        //endregion

        //region button batch controller
        tvBatchController = (TextView) findViewById(R.id.tvBatchController);
        btnBatchController = (Button) findViewById(R.id.btnBatchController);
        btnBatchController.setEnabled(false);
        btnBatchController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                batchController();
            }
        });
        //endregion

        //region button pump start
        tvPumpStart = (TextView) findViewById(R.id.tvPumpStart);
        btnPumpStart = (Button) findViewById(R.id.btnPumpStart);
        btnPumpStart.setEnabled(false);
        btnPumpStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				btnPumpStartClicked();
            }
        });
        //endregion

        //region status settings

        //retrieve job status from shared preferences
        String jobStatus = sharedPref.getString(SHARED_PREF_JOB_STATUS, "");
        String loadingArm = sharedPref.getString(SHARED_PREF_LOADING_ARM, "");
        String batchController = sharedPref.getString(SHARED_PREF_BATCH_CONTROLLER, "");
        String pumpStartTime = sharedPref.getString(SHARED_PREF_PUMP_START_TIME, "");

        switch (jobStatus) {

            case STATUS_SAFETY_CHECKS:
                btnScanLoadingArm.setTextColor(getResources().getColor(R.color.colorBlack));
                btnScanLoadingArm.getBackground().clearColorFilter();
                tvScanLoadingArm.setText("");

                btnBatchController.setTextColor(getResources().getColor(R.color.colorBlack));
                btnBatchController.getBackground().clearColorFilter();
                tvBatchController.setText("");

                btnPumpStart.setTextColor(getResources().getColor(R.color.colorBlack));
                btnPumpStart.getBackground().clearColorFilter();
                tvPumpStart.setText("");
                break;

            case STATUS_SCAN_LOADING_ARM:
                btnScanLoadingArm.setTextColor(getResources().getColor(R.color.colorWhite));
                btnScanLoadingArm.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                tvScanLoadingArm.setText(loadingArm);

                btnBatchController.setEnabled(true);
                break;

            case STATUS_BATCH_CONTROLLER:
                btnScanLoadingArm.setTextColor(getResources().getColor(R.color.colorWhite));
                btnScanLoadingArm.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                tvScanLoadingArm.setText(loadingArm);

                btnBatchController.setTextColor(getResources().getColor(R.color.colorWhite));
                btnBatchController.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                btnBatchController.setEnabled(true);
                tvBatchController.setText(batchController);

                btnPumpStart.setEnabled(true);
                break;

            case STATUS_PUMP_START:
                btnScanLoadingArm.setTextColor(getResources().getColor(R.color.colorWhite));
                btnScanLoadingArm.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                tvScanLoadingArm.setText(loadingArm);

                btnBatchController.setTextColor(getResources().getColor(R.color.colorWhite));
                btnBatchController.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                btnBatchController.setEnabled(true);
                tvBatchController.setText(batchController);

                btnPumpStart.setTextColor(getResources().getColor(R.color.colorWhite));
                btnPumpStart.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                btnPumpStart.setEnabled(true);
                tvPumpStart.setText(pumpStartTime);
                break;

            default:
                btnScanLoadingArm.setTextColor(getResources().getColor(R.color.colorWhite));
                btnScanLoadingArm.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                tvScanLoadingArm.setText(loadingArm);

                btnBatchController.setTextColor(getResources().getColor(R.color.colorWhite));
                btnBatchController.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                tvBatchController.setText(batchController);

                btnPumpStart.setTextColor(getResources().getColor(R.color.colorWhite));
                btnPumpStart.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                tvPumpStart.setText(pumpStartTime);
                break;
        }

        //endregion
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
        String welcomeMessage = sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "");
        String jobID = sharedPref.getString(Constant.SHARED_PREF_JOB_ID, "");
        String customerName = sharedPref.getString(Constant.SHARED_PREF_CUSTOMER_NAME, "");
        String loadingBay = sharedPref.getString(Constant.SHARED_PREF_LOADING_BAY, "");
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

    public void btnScanLoadingArmClicked() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.SHARED_PREF_SCAN_VALUE, Constant.SCAN_VALUE_LOADING_ARM);
        editor.commit();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(Constant.SCAN_MSG_PROMPT_SCAN_LOADING_ARM);
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

                if (returnScanValue.equals(Constant.SCAN_VALUE_LOADING_ARM)) {

					LoadingArmWSAsync task = new LoadingArmWSAsync(this, sharedPref.getString(Constant.SHARED_PREF_JOB_ID, ""), scanContent);
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

    public void batchController() {

        if (batchControllerDialog != null && batchControllerDialog.isShowing())
            return;

        batchControllerDialog = new Dialog(this);
        batchControllerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        batchControllerDialog.setContentView(R.layout.dialog_batch_controller);

        final TextView tvMetricTon = (TextView) batchControllerDialog.findViewById(R.id.tvMetricTon);
        final EditText etLitre = (EditText) batchControllerDialog.findViewById(R.id.etLitre);

        //region set batch controller status
        if (sharedPref.getString(SHARED_PREF_JOB_STATUS, "").equals(STATUS_BATCH_CONTROLLER)) {

            String litre = sharedPref.getString(Constant.SHARED_PREF_BATCH_CONTROLLER_LITRE, "");
            String metric = sharedPref.getString(SHARED_PREF_BATCH_CONTROLLER, "");

            tvMetricTon.setText(metric);
            etLitre.setText(litre);

        } else {

            etLitre.setText("");
        }
        //endregion

        //region edittext changed listener
        etLitre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String litre = etLitre.getText().toString();
                if (litre.length() > 0) {
                    Float metricTon = Float.parseFloat(litre) / 1000;
                    tvMetricTon.setText(metricTon.toString() + " MT");
                } else {
                    tvMetricTon.setText("Metric Ton");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //endregion

        //region button confrim
        Button btnConfirm = (Button) batchControllerDialog.findViewById(R.id.btnConfirm);
        // if button is clicked, close the custom dialog
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPref.edit();

                if (!etLitre.getText().toString().isEmpty()) {

                    //region set job status
                    String litre = etLitre.getText().toString();
					String metric = tvMetricTon.getText().toString();

                    editor.putString(Constant.SHARED_PREF_BATCH_CONTROLLER_LITRE, litre).commit();
					editor.putString(SHARED_PREF_BATCH_CONTROLLER, metric).commit();
                    editor.putString(SHARED_PREF_JOB_STATUS, STATUS_BATCH_CONTROLLER).commit();

                    jobDetailDataSource = new JobDetailDataSource(context);
                    jobDetailDataSource.open();
                    jobDetailDataSource.updateJobDetails(sharedPref.getString(Constant.SHARED_PREF_JOB_ID, ""), STATUS_BATCH_CONTROLLER);
                    jobDetailDataSource.close();
                    //endregion

                    batchControllerDialog.dismiss();

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                } else {

                    Common.shortToast(getApplicationContext(), "Please fill in the Batch Controller Input");
                }

            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) batchControllerDialog.findViewById(R.id.btnCancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                batchControllerDialog.dismiss();

            }
        });
        //endregion

        int dividerId = batchControllerDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = batchControllerDialog.findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        }

        batchControllerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        batchControllerDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        batchControllerDialog.show();
    }

    public void btnPumpStartClicked() {

	    if (sharedPref.getString(SHARED_PREF_JOB_STATUS, "").equals(STATUS_BATCH_CONTROLLER)) {

		    if (pumpStartDialog != null && pumpStartDialog.isShowing())
			    return;

		    pumpStartDialog = new Dialog(this);
		    pumpStartDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    pumpStartDialog.setContentView(R.layout.dialog_pump_start);

            //region button confrim
		    Button btnConfirm = (Button) pumpStartDialog.findViewById(R.id.btnConfirm);
		    // if button is clicked, close the custom dialog
		    btnConfirm.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View view) {

                    String jobID = sharedPref.getString(Constant.SHARED_PREF_JOB_ID, "");
                    String loginName = sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "");

                    if (Common.isNetworkAvailable(context)) {

                        PumpStartWSAsync task = new PumpStartWSAsync(context, pumpStartDialog, jobID, loginName);
                        task.execute();

                    } else {


                    }

			    }
		    });
            //endregion

            //region button cancel
		    Button btnCancel = (Button) pumpStartDialog.findViewById(R.id.btnCancel);
		    // if button is clicked, close the custom dialog
		    btnCancel.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View view) {
				    pumpStartDialog.dismiss();
			    }
		    });
            //endregion

		    int dividerId = pumpStartDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		    View divider = pumpStartDialog.findViewById(dividerId);
		    if (divider != null) {
			    divider.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
		    }

		    pumpStartDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		    pumpStartDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		    pumpStartDialog.show();

	    } else {

		    Intent intent = new Intent(this, StopOperationActivity.class);
            finish();
		    startActivity(intent);
	    }
    }

	public void onBackPressed() {

	}
}
