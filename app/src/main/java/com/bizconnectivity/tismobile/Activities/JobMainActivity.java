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
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.Classes.GHS;
import com.bizconnectivity.tismobile.Classes.JobDetail;
import com.bizconnectivity.tismobile.Classes.PPE;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Database.DataSources.JobDetailDataSource;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.WebServices.PPEWSAsync;
import com.bizconnectivity.tismobile.WebServices.SDSWSAsync;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.bizconnectivity.tismobile.Constant.ERR_MSG_CHECK_PPE;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_STATUS;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_PRODUCT_NAME;
import static com.bizconnectivity.tismobile.Constant.STATUS_PENDING;
import static com.bizconnectivity.tismobile.Constant.STATUS_PPE;
import static com.bizconnectivity.tismobile.Constant.STATUS_SAFETY_CHECKS;
import static com.bizconnectivity.tismobile.Constant.STATUS_SDS;
import static com.bizconnectivity.tismobile.Constant.STATUS_WORK_INSTRUCTION;

public class JobMainActivity extends AppCompatActivity {

    Context context;
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;
    TextView headerMessage, tv_jobID, tv_customerName, tv_loadingBay, tv_loadingArm;
    Dialog exitDialog, scanPPEDialog, safetyChecksDialog;
    Button btnPPE, btnSDS, btnScanDetails, btnSafetyCheck;

    JobDetailDataSource jobDetailDataSource;
    public SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_main);

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

        //region button PPE
        btnPPE = (Button) findViewById(R.id.btnScanPPE);
        btnPPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isNetworkAvailable(context)) {

                    PPEWSAsync task = new PPEWSAsync(context, btnPPE, sharedPref.getString(SHARED_PREF_PRODUCT_NAME, ""));
                    task.execute();

                } else {


                }

            }
        });
        //endregion

        //region button SDS
        btnSDS = (Button) findViewById(R.id.btnSDS);
        btnSDS.setEnabled(false);
        btnSDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isNetworkAvailable(context)) {

                    SDSWSAsync task = new SDSWSAsync(context, btnSDS, sharedPref.getString(SHARED_PREF_JOB_ID, ""));
                    task.execute();

                } else {


                }
            }
        });
        //endregion

        //region button scan details
        btnScanDetails = (Button) findViewById(R.id.btnScanDetails);
        btnScanDetails.setEnabled(false);
        btnScanDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, ScanDetailsActivity.class);
                finish();
                startActivity(intent);
            }
        });
        //endregion

        //region button safety checks onclick
        btnSafetyCheck = (Button) findViewById(R.id.btnSafetyCheck);
        btnSafetyCheck.setEnabled(false);
        btnSafetyCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                safetyChecks();
            }
        });
        //endregion

        //region status settings

        //retrieve job status from shared preferences
        String jobStatus = sharedPref.getString(Constant.SHARED_PREF_JOB_STATUS, "");

        switch (jobStatus) {

            case STATUS_PENDING:
                btnPPE.setTextColor(getResources().getColor(R.color.colorBlack));
                btnPPE.getBackground().clearColorFilter();

                btnSDS.setTextColor(getResources().getColor(R.color.colorBlack));
                btnSDS.getBackground().clearColorFilter();

                btnScanDetails.setTextColor(getResources().getColor(R.color.colorBlack));
                btnScanDetails.getBackground().clearColorFilter();

                btnSafetyCheck.setTextColor(getResources().getColor(R.color.colorBlack));
                btnSafetyCheck.getBackground().clearColorFilter();
                break;

            case STATUS_PPE:
                btnPPE.setTextColor(getResources().getColor(R.color.colorWhite));
                btnPPE.setBackgroundColor(getResources().getColor(R.color.colorGreen));

                btnSDS.setEnabled(true);
                break;

            case STATUS_SDS:
                btnPPE.setTextColor(getResources().getColor(R.color.colorWhite));
                btnPPE.setBackgroundColor(getResources().getColor(R.color.colorGreen));

                btnSDS.setTextColor(getResources().getColor(R.color.colorWhite));
                btnSDS.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                btnSDS.setEnabled(true);

                btnScanDetails.setEnabled(true);
                break;

            case STATUS_WORK_INSTRUCTION:
                btnPPE.setTextColor(getResources().getColor(R.color.colorWhite));
                btnPPE.setBackgroundColor(getResources().getColor(R.color.colorGreen));

                btnSDS.setTextColor(getResources().getColor(R.color.colorWhite));
                btnSDS.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                btnSDS.setEnabled(true);

                btnScanDetails.setTextColor(getResources().getColor(R.color.colorWhite));
                btnScanDetails.setBackgroundColor(getResources().getColor(R.color.colorGreen));
                btnScanDetails.setEnabled(true);

                btnSafetyCheck.setEnabled(true);
                break;

            default:
                btnPPE.setTextColor(getResources().getColor(R.color.colorWhite));
                btnPPE.setBackgroundColor(getResources().getColor(R.color.colorGreen));

                btnSDS.setTextColor(getResources().getColor(R.color.colorWhite));
                btnSDS.setBackgroundColor(getResources().getColor(R.color.colorGreen));

                btnScanDetails.setTextColor(getResources().getColor(R.color.colorWhite));
                btnScanDetails.setBackgroundColor(getResources().getColor(R.color.colorGreen));

                btnSafetyCheck.setTextColor(getResources().getColor(R.color.colorWhite));
                btnSafetyCheck.setBackgroundColor(getResources().getColor(R.color.colorGreen));
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

        Intent intent = new Intent(context, DashboardActivity.class);
        finish();
        startActivity(intent);
    }

    public void btnSearchClicked() {

        Intent intent = new Intent(context, SearchJobActivity.class);
        finish();
        startActivity(intent);
    }

    public void btnSwitchClicked() {

        Intent intent = new Intent(context, SwitchTruckBayActivity.class);
        finish();
        startActivity(intent);
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

    public void ppeDialog(ArrayList<PPE> ppeArrayList, ArrayList<GHS> ghsArrayList) {

        if (scanPPEDialog != null && scanPPEDialog.isShowing())
            return;

        scanPPEDialog = new Dialog(this);
        scanPPEDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        scanPPEDialog.setContentView(R.layout.dialog_driver_safety_check_ppe);

        LinearLayout linearLayoutPPE = (LinearLayout) scanPPEDialog.findViewById(R.id.linearLayoutPPE);
        LinearLayout linearLayoutGHS = (LinearLayout) scanPPEDialog.findViewById(R.id.linearLayoutGHS);
        TextView tvPPEProductName = (TextView) scanPPEDialog.findViewById(R.id.tvPPEProductName);
        final RadioButton rbtnHandleProduct = (RadioButton) scanPPEDialog.findViewById(R.id.rbtnHandleProduct);
        final RadioButton rbtnHavePPE = (RadioButton) scanPPEDialog.findViewById(R.id.rbtnHavePPE);

        //set product name
        tvPPEProductName.setText(sharedPref.getString(SHARED_PREF_PRODUCT_NAME, ""));

        //region set radio button status
        if (sharedPref.getString(SHARED_PREF_JOB_STATUS, "").equals(STATUS_PENDING)) {

            rbtnHandleProduct.setChecked(false);
            rbtnHavePPE.setChecked(false);

        } else {

            rbtnHandleProduct.setChecked(true);
            rbtnHavePPE.setChecked(true);
        }
        //endregion

        //region ppe and ghs picture setup

        int totalLinearLayoutGHS = (int) Math.ceil(ghsArrayList.size() / 4.0);
        int totalLinearLayoutPPE = (int) Math.ceil(ppeArrayList.size() / 4.0);
	    int imagePerRow = 4;
	    int countGHS = 0;
        int countPPE = 0;
        int countLinearLayoutGHS = 0;
        int countLinearLayoutPPE = 0;
        ArrayList<LinearLayout> linearLayoutArrayGHS = new ArrayList<>();
        ArrayList<LinearLayout> linearLayoutArrayPPE = new ArrayList<>();

	    for (int i=0; i<totalLinearLayoutGHS; i++) {

            LinearLayout linearLayout = new LinearLayout(this);
		    linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		    linearLayout.setGravity(Gravity.LEFT);
		    linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutArrayGHS.add(linearLayout);

            linearLayoutGHS.addView(linearLayoutArrayGHS.get(i));
	    }

	    for (int j=0; j<ghsArrayList.size(); j++) {

		    ImageView image = new ImageView(this);
		    String ghsPictureUrl = Constant.GHS_FILE_LOCATION + ghsArrayList.get(j).getGhsPictureURL();
		    Picasso.with(this)
                    .load(ghsPictureUrl)
                    .resize(100, 100)
                    .into(image);

		    linearLayoutArrayGHS.get(countLinearLayoutGHS).addView(image);

		    countGHS++;

		    if ((countGHS % imagePerRow) == 0) {

			    countLinearLayoutGHS++;
		    }

	    }

        for (int i=0; i<totalLinearLayoutPPE; i++) {

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setGravity(Gravity.LEFT);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutArrayPPE.add(linearLayout);

            linearLayoutPPE.addView(linearLayoutArrayPPE.get(i));
        }

        for (int j=0; j<ppeArrayList.size(); j++) {

            ImageView image = new ImageView(this);
            String ppePictureUrl = Constant.PPE_FILE_LOCATION + ppeArrayList.get(j).getPpePictureURL();
            Picasso.with(this)
                    .load(ppePictureUrl)
                    .resize(100, 100)
                    .into(image);

            linearLayoutArrayPPE.get(countLinearLayoutPPE).addView(image);

            countPPE++;

            if ((countPPE % imagePerRow) == 0) {

                countLinearLayoutPPE++;
            }

        }

        //endregion

        //region button confirm
        Button btnConfirm = (Button) scanPPEDialog.findViewById(R.id.btnConfirm);
        // if button is clicked, close the custom dialog
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPref.edit();

                if (rbtnHandleProduct.isChecked() && rbtnHavePPE.isChecked()) {

                    //region set job status
                    editor.putString(Constant.SHARED_PREF_JOB_STATUS, STATUS_PPE).commit();

                    jobDetailDataSource = new JobDetailDataSource(context);
                    jobDetailDataSource.open();
                    jobDetailDataSource.updateJobDetails(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_PPE);
                    jobDetailDataSource.close();
                    //endregion

                    scanPPEDialog.dismiss();

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                } else {

                    Common.shortToast(getApplicationContext(), ERR_MSG_CHECK_PPE);
                }
            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) scanPPEDialog.findViewById(R.id.btnCancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanPPEDialog.dismiss();
            }
        });
        //endregion

        int dividerId = scanPPEDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = scanPPEDialog.findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        }

        scanPPEDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        scanPPEDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        scanPPEDialog.show();
    }

    public void safetyChecks() {

        if (safetyChecksDialog != null && safetyChecksDialog.isShowing())
            return;

        safetyChecksDialog = new Dialog(this);
        safetyChecksDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        safetyChecksDialog.setContentView(R.layout.dialog_driver_safety_check_vehicle);

        final RadioButton rbtnWheelChocked = (RadioButton) safetyChecksDialog.findViewById(R.id.rbtnWheelChocked);
        final RadioButton rbtnBondingWire = (RadioButton) safetyChecksDialog.findViewById(R.id.rbtnBondingWire);

        //region set radio button status
        if (sharedPref.getString(Constant.SHARED_PREF_JOB_STATUS, "").equals(STATUS_SAFETY_CHECKS)) {

            rbtnWheelChocked.setChecked(true);
            rbtnBondingWire.setChecked(true);

        } else {

            rbtnWheelChocked.setChecked(false);
            rbtnBondingWire.setChecked(false);
        }
        //endregion

        //region button confirm
        Button btnConfirm = (Button) safetyChecksDialog.findViewById(R.id.btnConfirm);
        // if button is clicked, close the custom dialog
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPref.edit();

                if (rbtnWheelChocked.isChecked() && rbtnBondingWire.isChecked()) {

                    //region set job status
                    editor.putString(Constant.SHARED_PREF_JOB_STATUS, STATUS_SAFETY_CHECKS).commit();

                    jobDetailDataSource = new JobDetailDataSource(context);
                    jobDetailDataSource.open();
                    jobDetailDataSource.updateJobDetails(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_PPE);
                    jobDetailDataSource.close();
                    //endregion

                    safetyChecksDialog.dismiss();

                    Intent intent = new Intent(getApplicationContext(), LoadingOperationActivity.class);
                    finish();
                    startActivity(intent);

                } else {

                    Common.shortToast(getApplicationContext(), "Please Answer All The Question");
                }
            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) safetyChecksDialog.findViewById(R.id.btnCancel);
        // if button is clicked, close the custom dialog
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                safetyChecksDialog.dismiss();
            }
        });
        //endregion

        int dividerId = safetyChecksDialog.getContext().getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = safetyChecksDialog.findViewById(dividerId);
        if (divider != null) {
            divider.setBackgroundColor(getResources().getColor(R.color.colorTransparent));
        }

        safetyChecksDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        safetyChecksDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        safetyChecksDialog.show();
    }

}
