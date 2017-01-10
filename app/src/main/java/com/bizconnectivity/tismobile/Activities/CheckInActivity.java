package com.bizconnectivity.tismobile.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.bizconnectivity.tismobile.Classes.CheckIn;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Database.Contracts.TechnicianDetailContract;
import com.bizconnectivity.tismobile.Database.DataSources.LoadingBayDetailDataSource;
import com.bizconnectivity.tismobile.Database.DataSources.TechnicianDetailDataSource;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.WebServices.CheckInWSAsync;
import com.bizconnectivity.tismobile.WebServices.ConstantWS;
import com.bizconnectivity.tismobile.WebServices.TechnicianIDWSAsync;
import com.bizconnectivity.tismobile.WebServices.UserWSAsync;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Set;


public class CheckInActivity extends AppCompatActivity {

    Context context;
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;
    TextView headerMessage;
    Dialog exitDialog;
    Button btnScanTechnician, btnScanTruckBay;

    public TextView tvTruckBayId, tvTechnicianId;
    public SharedPreferences sharedPref;

    TechnicianDetailDataSource technicianDetailDataSource;
    LoadingBayDetailDataSource loadingBayDetailDataSource;
    CheckIn checkIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

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

        tvTechnicianId = (TextView) findViewById(R.id.tvTechnicianId);
        btnScanTechnician = (Button) findViewById(R.id.btnScanTechnician);
        btnScanTechnician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnScanTechnicianClicked(view);
            }
        });

        tvTruckBayId = (TextView) findViewById(R.id.tvTruckBayId);
        btnScanTruckBay = (Button) findViewById(R.id.btnScanTruckBay);
        btnScanTruckBay.setEnabled(false);
        btnScanTruckBay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnScanTruckBayClicked(view);
            }
        });

        String technicianID = sharedPref.getString(Constant.SHARED_PREF_TECHNICIAN_ID, "");
        Set<String> truckLoadingBayList = sharedPref.getStringSet(Constant.SHARED_PREF_TRUCK_LOADING_BAY, null);

        if (!technicianID.isEmpty() && truckLoadingBayList != null){

            btnScanTechnician.setTextColor(getResources().getColor(R.color.colorWhite));
            btnScanTechnician.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            tvTechnicianId.setText(technicianID);

            String trunkBayString = "";
            for (String truckBayID : truckLoadingBayList) {
                if (trunkBayString.isEmpty()) {
                    trunkBayString = truckBayID;
                } else {
                    trunkBayString = trunkBayString + ", " + truckBayID;
                }
            }

            tvTruckBayId.setText(trunkBayString);
            btnScanTruckBay.setTextColor(getResources().getColor(R.color.colorWhite));
            btnScanTruckBay.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            btnScanTruckBay.setEnabled(true);

        } else if (!technicianID.isEmpty()) {

            btnScanTechnician.setTextColor(getResources().getColor(R.color.colorWhite));
            btnScanTechnician.setBackgroundColor(getResources().getColor(R.color.colorGreen));
            tvTechnicianId.setText(technicianID);

            btnScanTruckBay.setEnabled(true);

        }

    }

    public void btnScanTechnicianClicked(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.SHARED_PREF_SCAN_VALUE, Constant.SCAN_VALUE_TECHNICIAN_ID);
        editor.commit();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(Constant.SCAN_MSG_PROMPT_TECHNICIAN_ID);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    public void btnScanTruckBayClicked(View view) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.SHARED_PREF_SCAN_VALUE, Constant.SCAN_VALUE_TRUCK_LOADING_BAY);
        editor.commit();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(Constant.SCAN_MSG_PROMPT_TRUCK_LOADING_BAY);
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

                if (returnScanValue.equals(Constant.SCAN_VALUE_TECHNICIAN_ID)) {

                    if (Common.isNetworkAvailable(this)) {

                        TechnicianIDWSAsync task = new TechnicianIDWSAsync(this, scanContent);
                        task.execute();

                    } else {

                        //check sqlite database
                        checkIn = new CheckIn();
                        checkIn.setTechnicianNRIC(scanContent);

                        checkTechnicialNRIC(checkIn);

                    }

                } else if (returnScanValue.equals(Constant.SCAN_VALUE_TRUCK_LOADING_BAY)) {

                    if (Common.isNetworkAvailable(this)) {

                        CheckInWSAsync task = new CheckInWSAsync(this, scanContent);
                        task.execute();

                    } else {

                        //check sqlite database
                        checkIn = new CheckIn();
                        checkIn.setLoadingBayNo(scanContent);

                        checkLoadingBayNo(checkIn);

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

    public void checkTechnicialNRIC(CheckIn checkIn) {

        technicianDetailDataSource = new TechnicianDetailDataSource(this);
        technicianDetailDataSource.open();

        String message = technicianDetailDataSource.retrieveTechnicianNRIC(checkIn);

        technicianDetailDataSource.close();

        if (message.equals(Constant.MSG_CORRECT_TECHNICIAN_NRIC)) {

            Intent intent = new Intent(this, CheckInActivity.class);
            finish();
            startActivity(intent);

        } else {

            Common.shortToast(this, Constant.ERR_MSG_INVALID_TECHNICIAN_NRIC);
        }
    }

    public void checkLoadingBayNo(CheckIn checkIn) {

        loadingBayDetailDataSource = new LoadingBayDetailDataSource(this);
        loadingBayDetailDataSource.open();

        String message = loadingBayDetailDataSource.retrieveLoadingBayNo(checkIn);

        loadingBayDetailDataSource.close();

        if (message.equals(Constant.MSG_CORRECT_LOADING_BAY_NO)) {

            Intent intent = new Intent(this, CheckInActivity.class);
            finish();
            startActivity(intent);

        } else {

            Common.shortToast(this, Constant.ERR_MSG_INVALID_TRUCK_BAY);

        }
    }

    //region Header
    /*-------- Set User Login Details --------*/
    public void setUserLoginDetails() {
        LinearLayout headerLayout = (LinearLayout) findViewById(R.id.header);
        headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);

        headerMessage.setText(Common.formatWelcomeMsg(sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "")));
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
                //v.getContext().finish();

                /*((Activity) context).finish();*/
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
}
