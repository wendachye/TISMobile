package com.bizconnectivity.tismobile.Activities;

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
import android.util.Log;
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
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.WebServices.LoadingArmWSAsync;
import com.bizconnectivity.tismobile.WebServices.PumpStartWSAsync;
import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.common.StringUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class LoadingOperationActivity extends AppCompatActivity {

    Context context;
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;
    TextView headerMessage, tvScanLoadingArm, tvBatchController, tvPumpStart;
    Dialog exitDialog, batchControllerDialog, pumpStartDialog;
    Button btnScanLoadingArm, btnBatchController, btnPumpStart;

    public SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_operation);

        //region Header and Footer
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        context = this;
        sharedPref = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        /*-------- Set User Login Details --------*/
        setUserLoginDetails();

        /*-------- footer buttons --------*/
        setFooterMenu();
        //endregion

        tvScanLoadingArm = (TextView) findViewById(R.id.tvScanLoadingArm);
        tvBatchController = (TextView) findViewById(R.id.tvBatchController);
		tvPumpStart = (TextView) findViewById(R.id.tvPumpStart);

        btnScanLoadingArm = (Button) findViewById(R.id.btnScanLoadingArm);
        btnScanLoadingArm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnScanLoadingArmClicked(view);
            }
        });
        btnBatchController = (Button) findViewById(R.id.btnBatchController);
        btnBatchController.setEnabled(false);
        btnBatchController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                batchController();
            }
        });
        btnPumpStart = (Button) findViewById(R.id.btnPumpStart);
        btnPumpStart.setEnabled(false);
        btnPumpStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
				btnPumpStartClicked();
            }
        });

        String loadingArm = sharedPref.getString(Constant.SHARED_PREF_LOADING_ARM, "");
        String batchController = sharedPref.getString(Constant.SHARED_PREF_BATCH_CONTROLLER_L, "");
        String pumpStart = sharedPref.getString(Constant.SHARED_PREF_PUMP_START, "");

        if (!loadingArm.isEmpty() && !batchController.isEmpty() && !pumpStart.isEmpty()) {

            btnScanLoadingArm.setTextColor(getResources().getColor(R.color.colorWhite));
            btnScanLoadingArm.setBackgroundColor(getResources().getColor(R.color.colorGreen));

            tvBatchController.setText(batchController);
            btnBatchController.setTextColor(getResources().getColor(R.color.colorWhite));
            btnBatchController.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            btnBatchController.setEnabled(true);

			tvPumpStart.setText(pumpStart);
            btnPumpStart.setTextColor(getResources().getColor(R.color.colorWhite));
            btnPumpStart.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            btnPumpStart.setEnabled(true);

        } else if (!loadingArm.isEmpty() && !batchController.isEmpty()) {

            btnScanLoadingArm.setTextColor(getResources().getColor(R.color.colorWhite));
            btnScanLoadingArm.setBackgroundColor(getResources().getColor(R.color.colorGreen));

            tvBatchController.setText(batchController);
            btnBatchController.setTextColor(getResources().getColor(R.color.colorWhite));
            btnBatchController.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            btnBatchController.setEnabled(true);

            btnPumpStart.setEnabled(true);

        }
        else if (!loadingArm.isEmpty()){

			btnScanLoadingArm.setTextColor(getResources().getColor(R.color.colorWhite));
			btnScanLoadingArm.setBackgroundColor(getResources().getColor(R.color.colorGreen));

            btnBatchController.setEnabled(true);
        }
    }

    //region Header
    /*-------- Set User Login Details --------*/
    public void setUserLoginDetails() {
        LinearLayout headerLayout = (LinearLayout) findViewById(R.id.header);
        headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);

         String loginName = sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "");

        headerMessage.setText(Common.formatWelcomeMsg(loginName));
    }
    //endregion

    //region Footer
    public void setFooterMenu() {
        RelativeLayout footerLayout = (RelativeLayout) findViewById(R.id.footer);
        btnAlert = (ImageButton) footerLayout.findViewById(R.id.btnHome);
        btnAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnHomeClicked(view);
            }
        });

        btnSearch = (ImageButton) footerLayout.findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSearchClicked(view);
            }
        });

        btnSwitch = (ImageButton) footerLayout.findViewById(R.id.btnSwitch);
        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSwitchClicked(view);
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

    public void btnHomeClicked(View view) {
        Intent intentHome = new Intent(context, DashboardActivity.class);
        finish();
        startActivity(intentHome);
    }

    public void btnSearchClicked(View view) {
        Intent intentSearchJob = new Intent(context, SearchJobActivity.class);
        finish();
        startActivity(intentSearchJob);
    }

    public void btnSwitchClicked(View view) {
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
                System.exit(0);
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

    public void btnScanLoadingArmClicked(View view) {
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

					int timeslotID = Integer.parseInt(sharedPref.getString(Constant.SHARED_PREF_ORDER_ID_SELECTED, ""));

					LoadingArmWSAsync task = new LoadingArmWSAsync(this, timeslotID, scanContent);
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

		String litre = sharedPref.getString(Constant.SHARED_PREF_BATCH_CONTROLLER_L, "");
		String metric = sharedPref.getString(Constant.SHARED_PREF_BATCH_CONTROLLER_MT, "");

		if (!litre.isEmpty() && !metric.isEmpty()) {
			tvMetricTon.setText(metric);
			etLitre.setText(litre);
		} else {
			etLitre.setText("");
		}

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

        Button btnConfirm = (Button) batchControllerDialog.findViewById(R.id.btnConfirm);
        // if button is clicked, close the custom dialog
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!etLitre.getText().toString().isEmpty()) {

                    SharedPreferences.Editor editor = sharedPref.edit();
                    String litre = etLitre.getText().toString();
					String metric = tvMetricTon.getText().toString();
                    editor.putString(Constant.SHARED_PREF_BATCH_CONTROLLER_L, litre).commit();
					editor.putString(Constant.SHARED_PREF_BATCH_CONTROLLER_MT, metric).commit();

                    batchControllerDialog.dismiss();

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                } else {
                    Common.shortToast(getApplicationContext(), "Please fill in the Batch Controller Input");
                }

            }
        });

        Button btnCancel = (Button) batchControllerDialog.findViewById(R.id.btnCancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                batchControllerDialog.dismiss();

            }
        });

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

	    String pumpStart = sharedPref.getString(Constant.SHARED_PREF_PUMP_START, "");

	    if (pumpStart.isEmpty()) {


		    if (pumpStartDialog != null && pumpStartDialog.isShowing())
			    return;

		    pumpStartDialog = new Dialog(this);
		    pumpStartDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		    pumpStartDialog.setContentView(R.layout.dialog_pump_start);

		    Button btnConfirm = (Button) pumpStartDialog.findViewById(R.id.btnConfirm);
		    // if button is clicked, close the custom dialog
		    btnConfirm.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View view) {

				    int timeslotID = Integer.parseInt(sharedPref.getString(Constant.SHARED_PREF_ORDER_ID_SELECTED, ""));
				    String updatedBy = sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "");

				    PumpStartWSAsync task = new PumpStartWSAsync(context, timeslotID, updatedBy);
				    task.execute();

				    pumpStartDialog.dismiss();

			    }
		    });

		    Button btnCancel = (Button) pumpStartDialog.findViewById(R.id.btnCancel);
		    // if button is clicked, close the custom dialog
		    btnCancel.setOnClickListener(new View.OnClickListener() {
			    @Override
			    public void onClick(View view) {
				    pumpStartDialog.dismiss();
			    }
		    });

		    int dividerId = pumpStartDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
		    View divider = pumpStartDialog.findViewById(dividerId);
		    if (divider != null) {
			    divider.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
		    }

		    pumpStartDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		    pumpStartDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		    pumpStartDialog.show();

	    } else {

		    finish();
		    Intent intent = new Intent(this, StopOperationActivity.class);
		    startActivity(intent);

	    }
    }

	public void onBackPressed() {
		finish();
		Intent intent = new Intent(this, JobMainActivity.class);
		startActivity(intent);
	}
}
