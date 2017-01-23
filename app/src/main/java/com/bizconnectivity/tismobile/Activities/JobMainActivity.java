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
import android.widget.TextView;

import com.bizconnectivity.tismobile.classes.GHS;
import com.bizconnectivity.tismobile.classes.PPE;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.database.datasources.GHSDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.database.datasources.LoadingBayDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.PPEDetailDataSource;
import com.bizconnectivity.tismobile.webservices.PPEWSAsync;
import com.bizconnectivity.tismobile.webservices.SDSWSAsync;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.bizconnectivity.tismobile.Common.shortToast;
import static com.bizconnectivity.tismobile.Constant.ERR_MSG_CHECK_PPE;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_ID;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_JOB_STATUS;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_NAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_PRODUCT_NAME;
import static com.bizconnectivity.tismobile.Constant.STATUS_PENDING;
import static com.bizconnectivity.tismobile.Constant.STATUS_PPE;
import static com.bizconnectivity.tismobile.Constant.STATUS_SAFETY_CHECKS;
import static com.bizconnectivity.tismobile.Constant.STATUS_SDS;
import static com.bizconnectivity.tismobile.Constant.STATUS_WORK_INSTRUCTION;
import static com.bizconnectivity.tismobile.webservices.ConstantWS.GHS_FILE_LOCATION;
import static com.bizconnectivity.tismobile.webservices.ConstantWS.PPE_FILE_LOCATION;

public class JobMainActivity extends AppCompatActivity {

    //region declaration
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;
    TextView headerMessage, tv_jobID, tv_customerName, tv_loadingBay, tv_loadingArm;
    Dialog exitDialog, scanPPEDialog, safetyChecksDialog;
    Button btnPPE, btnSDS, btnScanDetails, btnSafetyCheck;
    LinearLayout footerLayout;
    LinearLayout headerLayout;
    LoadingBayDetailDataSource loadingBayDetailDataSource;
    String jobStatus, welcomeMessage, jobID, customerName, loadingBay, loadingArm;
    ArrayList<LinearLayout> linearLayoutArrayGHS;
    ArrayList<LinearLayout> linearLayoutArrayPPE;
    PPEDetailDataSource ppeDetailDataSource;
    GHSDetailDataSource ghsDetailDataSource;
    ArrayList<GHS> ghsArrayList;
    ArrayList<PPE> ppeArrayList;
    boolean isOffline = false;
    //endregion

    JobDetailDataSource jobDetailDataSource;
    public SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_main);

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

        //region button PPE
        btnPPE = (Button) findViewById(R.id.btnScanPPE);
        btnPPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isNetworkAvailable(getApplicationContext())) {

                    isOffline = false;

                    PPEWSAsync task = new PPEWSAsync(JobMainActivity.this, isOffline, btnPPE, sharedPref.getString(SHARED_PREF_PRODUCT_NAME, ""));
                    task.execute();

                } else {

                    isOffline = true;

                    ppeDetailDataSource = new PPEDetailDataSource(getApplicationContext());
                    ppeArrayList = new ArrayList<>();
                    ppeDetailDataSource.open();
                    ppeArrayList = ppeDetailDataSource.retrievePPE(sharedPref.getString(SHARED_PREF_JOB_ID, ""));
                    ppeDetailDataSource.close();

                    ghsDetailDataSource = new GHSDetailDataSource(getApplicationContext());
                    ghsArrayList = new ArrayList<>();
                    ghsDetailDataSource.open();
                    ghsArrayList = ghsDetailDataSource.retrieveGHS(sharedPref.getString(SHARED_PREF_JOB_ID, ""));
                    ghsDetailDataSource.close();

                    ppeDialog(isOffline, ppeArrayList, ghsArrayList);
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

                if (Common.isNetworkAvailable(getApplicationContext())) {

                    btnScanDetails.setEnabled(true);

                    SDSWSAsync task = new SDSWSAsync(JobMainActivity.this, btnSDS, sharedPref.getString(SHARED_PREF_JOB_ID, ""));
                    task.execute();

                } else {

                    //update job status
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(Constant.SHARED_PREF_JOB_STATUS, Constant.STATUS_SDS).apply();

                    jobDetailDataSource = new JobDetailDataSource(getApplicationContext());
                    jobDetailDataSource.open();
                    jobDetailDataSource.updateJobStatus(sharedPref.getString(Constant.SHARED_PREF_JOB_ID, ""), Constant.STATUS_SDS);
                    jobDetailDataSource.close();

                    Intent intent = new Intent(getApplicationContext(), JobMainActivity.class);
                    finish();
                    startActivity(intent);

                    shortToast(getApplicationContext(), "No Internet Connection");
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

                Intent intent = new Intent(getApplicationContext(), ScanDetailsActivity.class);
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
        jobStatus = sharedPref.getString(SHARED_PREF_JOB_STATUS, "");

        switch (jobStatus) {

            case STATUS_PENDING:
                btnPPE.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                btnPPE.getBackground().clearColorFilter();

                btnSDS.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                btnSDS.getBackground().clearColorFilter();

                btnScanDetails.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                btnScanDetails.getBackground().clearColorFilter();

                btnSafetyCheck.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                btnSafetyCheck.getBackground().clearColorFilter();
                break;

            case STATUS_PPE:
                btnPPE.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnPPE.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));

                btnSDS.setEnabled(true);
                break;

            case STATUS_SDS:
                btnPPE.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnPPE.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));

                btnSDS.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnSDS.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                btnSDS.setEnabled(true);

                btnScanDetails.setEnabled(true);
                break;

            case STATUS_WORK_INSTRUCTION:
                btnPPE.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnPPE.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));

                btnSDS.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnSDS.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                btnSDS.setEnabled(true);

                btnScanDetails.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnScanDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                btnScanDetails.setEnabled(true);

                btnSafetyCheck.setEnabled(true);
                break;

            default:
                btnPPE.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnPPE.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));

                btnSDS.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnSDS.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));

                btnScanDetails.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnScanDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));

                btnSafetyCheck.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                btnSafetyCheck.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                break;
        }
        //endregion
    }

    public void ppeDialog(boolean isOffline, ArrayList<PPE> ppeArrayList, ArrayList<GHS> ghsArrayList) {

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

        if (isOffline) {

            //region ppe and ghs picture setup

            int totalLinearLayoutGHS = (int) Math.ceil(ghsArrayList.size() / 4.0);
            int totalLinearLayoutPPE = (int) Math.ceil(ppeArrayList.size() / 4.0);
            int imagePerRow = 4;
            int countGHS = 0;
            int countPPE = 0;
            int countLinearLayoutGHS = 0;
            int countLinearLayoutPPE = 0;
            linearLayoutArrayGHS = new ArrayList<>();
            linearLayoutArrayPPE = new ArrayList<>();

            //region ghs
            for (int i = 0; i < totalLinearLayoutGHS; i++) {

                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setGravity(Gravity.START);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutArrayGHS.add(linearLayout);

                linearLayoutGHS.addView(linearLayoutArrayGHS.get(i));
            }

            for (int j = 0; j < ghsArrayList.size(); j++) {

                ImageView image = new ImageView(this);

                switch (ghsArrayList.get(j).getGhsPictureURL()) {

                    case "1":
                        Picasso.with(this)
                                .load(R.drawable.acute_toxicity)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "2":
                        Picasso.with(this)
                                .load(R.drawable.aspiration_toxicity)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "3":
                        Picasso.with(this)
                                .load(R.drawable.corrosive)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "4":
                        Picasso.with(this)
                                .load(R.drawable.environment_toxicity)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "5":
                        Picasso.with(this)
                                .load(R.drawable.explosive)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "6":
                        Picasso.with(this)
                                .load(R.drawable.flammable)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "7":
                        Picasso.with(this)
                                .load(R.drawable.gases_under_pressure)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "8":
                        Picasso.with(this)
                                .load(R.drawable.irritant)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "9":
                        Picasso.with(this)
                                .load(R.drawable.oxidiser)
                                .resize(100, 100)
                                .into(image);
                        break;

                    default:
                        break;
                }

                linearLayoutArrayGHS.get(countLinearLayoutGHS).addView(image);

                countGHS++;

                if ((countGHS % imagePerRow) == 0) {

                    countLinearLayoutGHS++;
                }
            }
            //endregion

            //region ppe
            for (int i = 0; i < totalLinearLayoutPPE; i++) {

                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setGravity(Gravity.START);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutArrayPPE.add(linearLayout);

                linearLayoutPPE.addView(linearLayoutArrayPPE.get(i));
            }

            for (int j = 0; j < ppeArrayList.size(); j++) {

                ImageView image = new ImageView(this);

                switch (ppeArrayList.get(j).getPpePictureURL()) {

                    case "1":
                        Picasso.with(this)
                                .load(R.drawable.ear_protection)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "2":
                        Picasso.with(this)
                                .load(R.drawable.face_shield)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "3":
                        Picasso.with(this)
                                .load(R.drawable.foot_protection)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "4":
                        Picasso.with(this)
                                .load(R.drawable.hand_protection)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "5":
                        Picasso.with(this)
                                .load(R.drawable.head_protection)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "6":
                        Picasso.with(this)
                                .load(R.drawable.mandatory_instruction)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "7":
                        Picasso.with(this)
                                .load(R.drawable.pedestrian_route)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "8":
                        Picasso.with(this)
                                .load(R.drawable.protective_clothing)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "9":
                        Picasso.with(this)
                                .load(R.drawable.respirator)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "10":
                        Picasso.with(this)
                                .load(R.drawable.safety_glasses)
                                .resize(100, 100)
                                .into(image);
                        break;

                    case "11":
                        Picasso.with(this)
                                .load(R.drawable.safety_harness)
                                .resize(100, 100)
                                .into(image);
                        break;

                    default:
                        break;
                }

                linearLayoutArrayPPE.get(countLinearLayoutPPE).addView(image);

                countPPE++;

                if ((countPPE % imagePerRow) == 0) {

                    countLinearLayoutPPE++;
                }
            }
            //endregion

            //endregion

        } else {

            //region ppe and ghs picture setup

            int totalLinearLayoutGHS = (int) Math.ceil(ghsArrayList.size() / 4.0);
            int totalLinearLayoutPPE = (int) Math.ceil(ppeArrayList.size() / 4.0);
            int imagePerRow = 4;
            int countGHS = 0;
            int countPPE = 0;
            int countLinearLayoutGHS = 0;
            int countLinearLayoutPPE = 0;
            linearLayoutArrayGHS = new ArrayList<>();
            linearLayoutArrayPPE = new ArrayList<>();

            //region ghs
            for (int i = 0; i < totalLinearLayoutGHS; i++) {

                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setGravity(Gravity.START);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutArrayGHS.add(linearLayout);

                linearLayoutGHS.addView(linearLayoutArrayGHS.get(i));
            }

            for (int j = 0; j < ghsArrayList.size(); j++) {

                ImageView image = new ImageView(this);
                String ghsPictureUrl = GHS_FILE_LOCATION + ghsArrayList.get(j).getGhsPictureURL();
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
            //endregion

            //region ppe
            for (int i = 0; i < totalLinearLayoutPPE; i++) {

                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setGravity(Gravity.START);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayoutArrayPPE.add(linearLayout);

                linearLayoutPPE.addView(linearLayoutArrayPPE.get(i));
            }

            for (int j = 0; j < ppeArrayList.size(); j++) {

                ImageView image = new ImageView(this);
                String ppePictureUrl = PPE_FILE_LOCATION + ppeArrayList.get(j).getPpePictureURL();
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

            //endregion
        }

        //region button confirm
        Button btnConfirm = (Button) scanPPEDialog.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPref.edit();

                if (rbtnHandleProduct.isChecked() && rbtnHavePPE.isChecked()) {

                    //region set job status
                    editor.putString(Constant.SHARED_PREF_JOB_STATUS, STATUS_PPE).apply();

                    jobDetailDataSource = new JobDetailDataSource(getApplicationContext());
                    jobDetailDataSource.open();
                    jobDetailDataSource.updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_PPE);
                    jobDetailDataSource.close();
                    //endregion

                    scanPPEDialog.dismiss();

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);

                } else {

                    shortToast(getApplicationContext(), ERR_MSG_CHECK_PPE);
                }
            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) scanPPEDialog.findViewById(R.id.btnCancel);
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
            divider.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent));
        }
        assert scanPPEDialog.getWindow() != null;
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
        if (sharedPref.getString(SHARED_PREF_JOB_STATUS, "").equals(STATUS_SAFETY_CHECKS)) {

            rbtnWheelChocked.setChecked(true);
            rbtnBondingWire.setChecked(true);

        } else {

            rbtnWheelChocked.setChecked(false);
            rbtnBondingWire.setChecked(false);
        }
        //endregion

        //region button confirm
        Button btnConfirm = (Button) safetyChecksDialog.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPref.edit();

                if (rbtnWheelChocked.isChecked() && rbtnBondingWire.isChecked()) {

                    //region set job status
                    editor.putString(SHARED_PREF_JOB_STATUS, STATUS_SAFETY_CHECKS).apply();

                    jobDetailDataSource = new JobDetailDataSource(getApplicationContext());
                    jobDetailDataSource.open();
                    jobDetailDataSource.updateJobStatus(sharedPref.getString(SHARED_PREF_JOB_ID, ""), STATUS_SAFETY_CHECKS);
                    jobDetailDataSource.close();
                    //endregion

                    safetyChecksDialog.dismiss();

                    Intent intent = new Intent(getApplicationContext(), LoadingOperationActivity.class);
                    finish();
                    startActivity(intent);

                } else {

                    shortToast(getApplicationContext(), "Please Answer All The Question");
                }
            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) safetyChecksDialog.findViewById(R.id.btnCancel);
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
            divider.setBackgroundColor(ContextCompat.getColor(this, R.color.colorTransparent));
        }
        assert safetyChecksDialog.getWindow() != null;
        safetyChecksDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        safetyChecksDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        safetyChecksDialog.show();
    }

    //region Header
    /*-------- Set User Login Details --------*/
    public void setUserLoginDetails() {

        headerLayout = (LinearLayout) findViewById(R.id.headerJobMain);
        headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);
        tv_jobID = (TextView) headerLayout.findViewById(R.id.tvOrderId);
        tv_customerName = (TextView) headerLayout.findViewById(R.id.tvCustomer);
        tv_loadingBay = (TextView) headerLayout.findViewById(R.id.tvBay);
        tv_loadingArm = (TextView) headerLayout.findViewById(R.id.tvArm);

        //retrieve shared preferences
        welcomeMessage = sharedPref.getString(Constant.SHARED_PREF_LOGINNAME, "");
        jobID = sharedPref.getString(Constant.SHARED_PREF_JOB_ID, "");
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

        Intent intent = new Intent(this, DashboardActivity.class);
        finish();
        startActivity(intent);
    }

    public void btnSearchClicked() {

        Intent intent = new Intent(this, SearchJobActivity.class);
        finish();
        startActivity(intent);
    }

    public void btnSwitchClicked() {

        Intent intent = new Intent(this, SwitchJobActivity.class);
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
        // if button is clicked, close the custom dialog
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

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, DashboardActivity.class);
        finish();
        startActivity(intent);
    }
}
