package com.bizconnectivity.tismobile.activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
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

import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.database.models.GHSDetail;
import com.bizconnectivity.tismobile.database.models.JobDetail;
import com.bizconnectivity.tismobile.database.models.LoadingBayDetail;
import com.bizconnectivity.tismobile.database.models.PPEDetail;
import com.bizconnectivity.tismobile.webservices.GHSWS;
import com.bizconnectivity.tismobile.webservices.PPEWS;
import com.bizconnectivity.tismobile.webservices.SDSWS;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.bizconnectivity.tismobile.Common.*;
import static com.bizconnectivity.tismobile.Constant.*;
import static com.bizconnectivity.tismobile.webservices.ConstantWS.*;

public class JobMainActivity extends AppCompatActivity {

    //region declaration

    //header
    @BindView(R.id.header_job_main)
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
    @BindView(R.id.button_ppe)
    Button mButtonPPE;
    @BindView(R.id.button_sds)
    Button mButtonSDS;
    @BindView(R.id.button_scan_details)
    Button mButtonScanDetails;
    @BindView(R.id.button_safety_checks)
    Button mButtonSafetyChecks;

    //footer
    @BindView(R.id.footer_job_main)
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
    LoadingBayDetail loadingBayDetail;
    PopupMenu popupMenu;
    Dialog exitDialog, scanPPEDialog, safetyChecksDialog;
    SharedPreferences sharedPref;
    boolean isActivityStarted = false;

    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_main);

        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        sharedPref = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        //region Header and Footer
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
        switch (sharedPref.getString(SHARED_PREF_JOB_STATUS, "")) {

            case STATUS_PENDING:
                mButtonPPE.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                mButtonPPE.getBackground().clearColorFilter();

                mButtonSDS.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                mButtonSDS.getBackground().clearColorFilter();

                mButtonScanDetails.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                mButtonScanDetails.getBackground().clearColorFilter();

                mButtonSafetyChecks.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
                mButtonSafetyChecks.getBackground().clearColorFilter();
                break;

            case STATUS_PPE:
                mButtonPPE.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonPPE.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));

                mButtonSDS.setEnabled(true);
                break;

            case STATUS_SDS:
                mButtonPPE.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonPPE.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));

                mButtonSDS.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonSDS.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mButtonSDS.setEnabled(true);

                mButtonScanDetails.setEnabled(true);
                break;

            case STATUS_WORK_INSTRUCTION:
                mButtonPPE.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonPPE.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));

                mButtonSDS.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonSDS.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mButtonSDS.setEnabled(true);

                mButtonScanDetails.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
                mButtonScanDetails.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
                mButtonScanDetails.setEnabled(true);

                mButtonSafetyChecks.setEnabled(true);
                break;

            default:
                break;
        }
        //endregion
    }

    @OnClick(R.id.button_ppe)
    public void btnPPE() {

        if (isNetworkAvailable(getApplicationContext())) {

            //retrieve from web service
            new PPEWSAsync(sharedPref.getString(SHARED_PREF_PRODUCT_NAME, "")).execute();

        } else {

            //retrieve from local database
            if (realm.where(PPEDetail.class).equalTo("jobID", sharedPref.getString(SHARED_PREF_JOB_ID, "")).count() > 0 ||
                    realm.where(GHSDetail.class).equalTo("jobID", sharedPref.getString(SHARED_PREF_JOB_ID, "")).count() > 0) {

                //retrieve from local database
                productPpeOfflineDialog();

            } else {

                //display alert dialog
                alertDialog(NO_PPE, NO_PPE_MESSAGE);
            }
        }
    }

    @OnClick(R.id.button_sds)
    public void btnSDS() {

        if (isNetworkAvailable(getApplicationContext())) {

            //retrieve from web service
            new SDSWSAsync(sharedPref.getString(SHARED_PREF_JOB_ID, "")).execute();

        } else {

            //update job status
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(SHARED_PREF_JOB_STATUS, STATUS_SDS).apply();

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    JobDetail jobDetail = new JobDetail();
                    jobDetail.setJobID(sharedPref.getString(SHARED_PREF_JOB_ID, ""));
                    jobDetail.setJobStatus(STATUS_SDS);

                    realm.copyToRealmOrUpdate(jobDetail);
                }
            });

            //set button sds
            mButtonSDS.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            mButtonSDS.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
            mButtonScanDetails.setEnabled(true);

            shortToast(getApplicationContext(), "No Internet Connection");
        }
    }

    @OnClick(R.id.button_scan_details)
    public void btnScanDetails() {

        Intent intent = new Intent(this, ScanDetailsActivity.class);
        isActivityStarted = true;
        startActivity(intent);
    }

    @OnClick(R.id.button_safety_checks)
    public void btnSafetyChecks() {

        safetyChecks();
    }

    //region Product PPE

    //region retrieve ppe
    private class PPEWSAsync extends AsyncTask<String, Void, Void> {

        ArrayList<PPEDetail> ppeArrayList;
        ArrayList<GHSDetail> ghsArrayList;
        String productName;
        ProgressDialog progressDialog;

        private PPEWSAsync(String productName) {

            this.productName = productName;
        }

        @Override
        protected void onPreExecute() {

            //start progress dialog
            progressDialog = ProgressDialog.show(JobMainActivity.this, "Please wait..", "Loading...", true);
        }

        @Override
        protected Void doInBackground(String... params) {

            ppeArrayList = PPEWS.invokeRetrievePPEWS(productName);
            ghsArrayList = GHSWS.invokeRetrieveGHSWS(productName);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (ppeArrayList.size() > 0 || ghsArrayList.size() > 0) {

                //close progress dialog
                progressDialog.dismiss();

                ppeDialog(ppeArrayList, ghsArrayList);

            } else {

                //close progress dialog
                progressDialog.dismiss();

                //display alert dialog
                alertDialog(NO_PPE, NO_PPE_MESSAGE);
            }
        }
    }
    //endregion

    //region product ppe dialog
    private void ppeDialog(ArrayList<PPEDetail> ppeArrayList, ArrayList<GHSDetail> ghsArrayList) {

        if (scanPPEDialog != null && scanPPEDialog.isShowing()) return;

        scanPPEDialog = new Dialog(this);
        scanPPEDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        scanPPEDialog.setContentView(R.layout.dialog_driver_safety_check_ppe);

        LinearLayout linearLayoutPPE = (LinearLayout) scanPPEDialog.findViewById(R.id.linearLayoutPPE);
        LinearLayout linearLayoutGHS = (LinearLayout) scanPPEDialog.findViewById(R.id.linearLayoutGHS);
        TextView mTextViewProductName = (TextView) scanPPEDialog.findViewById(R.id.text_product_name);
        final RadioButton mRadioButtonGHS = (RadioButton) scanPPEDialog.findViewById(R.id.radio_ghs);
        final RadioButton mRadioButtonPPE = (RadioButton) scanPPEDialog.findViewById(R.id.radio_ppe);

        //set product name
        mTextViewProductName.setText(sharedPref.getString(SHARED_PREF_PRODUCT_NAME, ""));

        //region set radio button status
        if (sharedPref.getString(SHARED_PREF_JOB_STATUS, "").equals(STATUS_PENDING)) {

            mRadioButtonGHS.setChecked(false);
            mRadioButtonPPE.setChecked(false);

        } else {

            mRadioButtonGHS.setChecked(true);
            mRadioButtonPPE.setChecked(true);
        }
        //endregion

        //region display ghs & ppe
        int imagePerRow = 4;

        //region ghs
        int totalLinearLayoutGHS = (int) Math.ceil(ghsArrayList.size() / 4.0);
        int countGHS = 0;
        int countLinearLayoutGHS = 0;
        ArrayList<LinearLayout> linearLayoutArrayGHS = new ArrayList<>();

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
            String ghsPictureUrl = GHS_FILE_LOCATION + ghsArrayList.get(j).getGhsURL();
            Picasso.with(this)
                    .load(ghsPictureUrl)
                    .resize(110, 100)
                    .into(image);

            linearLayoutArrayGHS.get(countLinearLayoutGHS).addView(image);

            countGHS++;

            if ((countGHS % imagePerRow) == 0) {

                countLinearLayoutGHS++;
            }
        }
        //endregion

        //region ppe
        int totalLinearLayoutPPE = (int) Math.ceil(ppeArrayList.size() / 4.0);
        int countPPE = 0;
        int countLinearLayoutPPE = 0;
        ArrayList<LinearLayout> linearLayoutArrayPPE = new ArrayList<>();

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
            String ppePictureUrl = PPE_FILE_LOCATION + ppeArrayList.get(j).getPpeURL();
            Picasso.with(this)
                    .load(ppePictureUrl)
                    .resize(110, 100)
                    .into(image);

            linearLayoutArrayPPE.get(countLinearLayoutPPE).addView(image);

            countPPE++;

            if ((countPPE % imagePerRow) == 0) {

                countLinearLayoutPPE++;
            }
        }
        //endregion
        //endregion

        //region button confirm
        Button btnConfirm = (Button) scanPPEDialog.findViewById(R.id.button_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPref.edit();

                if (mRadioButtonGHS.isChecked() && mRadioButtonPPE.isChecked()) {

                    //region update job status
                    editor.putString(SHARED_PREF_JOB_STATUS, STATUS_PPE).apply();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            JobDetail jobDetail = new JobDetail();
                            jobDetail.setJobID(sharedPref.getString(SHARED_PREF_JOB_ID, ""));
                            jobDetail.setJobStatus(STATUS_PPE);

                            realm.copyToRealmOrUpdate(jobDetail);
                        }
                    });
                    //endregion

                    mButtonPPE.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                    mButtonPPE.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                    mButtonSDS.setEnabled(true);

                    scanPPEDialog.dismiss();

                } else {

                    //show error message
                    shortToast(getApplicationContext(), ERR_MSG_CHECK_PPE);
                }
            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) scanPPEDialog.findViewById(R.id.button_cancel);
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
    //endregion

    //region product ppe offline dialog
    private void productPpeOfflineDialog() {

        //region initial dialog
        if (scanPPEDialog != null && scanPPEDialog.isShowing()) return;

        scanPPEDialog = new Dialog(this);
        scanPPEDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        scanPPEDialog.setContentView(R.layout.dialog_driver_safety_check_ppe);

        LinearLayout linearLayoutPPE = (LinearLayout) scanPPEDialog.findViewById(R.id.linearLayoutPPE);
        LinearLayout linearLayoutGHS = (LinearLayout) scanPPEDialog.findViewById(R.id.linearLayoutGHS);
        TextView mTextViewProductName = (TextView) scanPPEDialog.findViewById(R.id.text_product_name);
        final RadioButton mRadioButtonGHS = (RadioButton) scanPPEDialog.findViewById(R.id.radio_ghs);
        final RadioButton mRadioButtonPPE = (RadioButton) scanPPEDialog.findViewById(R.id.radio_ppe);
        //endregion

        //set product name
        mTextViewProductName.setText(sharedPref.getString(SHARED_PREF_PRODUCT_NAME, ""));

        //region set radio button status
        if (sharedPref.getString(SHARED_PREF_JOB_STATUS, "").equals(STATUS_PENDING)) {

            mRadioButtonGHS.setChecked(false);
            mRadioButtonPPE.setChecked(false);

        } else {

            mRadioButtonGHS.setChecked(true);
            mRadioButtonPPE.setChecked(true);
        }
        //endregion

        //region display ghs & ppe
        int imagePerRow = 4;

        //region ghs
        int totalLinearLayoutGHS = (int) Math.ceil(realm.where(GHSDetail.class).equalTo("jobID", SHARED_PREF_JOB_ID).count() / 4.0);
        int countGHS = 0;
        int countLinearLayoutGHS = 0;
        ArrayList<LinearLayout> linearLayoutArrayGHS = new ArrayList<>();

        for (int i=0; i<totalLinearLayoutGHS; i++) {

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setGravity(Gravity.START);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutArrayGHS.add(linearLayout);

            linearLayoutGHS.addView(linearLayoutArrayGHS.get(i));
        }

        for (GHSDetail results : realm.where(GHSDetail.class).equalTo("jobID", SHARED_PREF_JOB_ID).findAll()) {

            ImageView image = new ImageView(this);

            switch (results.getGhsName()) {

                case "AcuteToxicity":
                    Picasso.with(this)
                            .load(R.drawable.acute_toxicity)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "AspirationToxicity":
                    Picasso.with(this)
                            .load(R.drawable.aspiration_toxicity)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "Corrosive":
                    Picasso.with(this)
                            .load(R.drawable.corrosive)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "EnvironmentToxicity":
                    Picasso.with(this)
                            .load(R.drawable.environment_toxicity)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "Explosive":
                    Picasso.with(this)
                            .load(R.drawable.explosive)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "Flammable":
                    Picasso.with(this)
                            .load(R.drawable.flammable)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "GasesUnderPressure":
                    Picasso.with(this)
                            .load(R.drawable.gases_under_pressure)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "Irritant":
                    Picasso.with(this)
                            .load(R.drawable.irritant)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "Oxidiser":
                    Picasso.with(this)
                            .load(R.drawable.oxidiser)
                            .resize(110, 100)
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
        int totalLinearLayoutPPE = (int) Math.ceil(realm.where(PPEDetail.class).equalTo("jobID", SHARED_PREF_JOB_ID).count() / 4.0);
        int countPPE = 0;
        int countLinearLayoutPPE = 0;
        ArrayList<LinearLayout> linearLayoutArrayPPE = new ArrayList<>();

        for (int i = 0; i < totalLinearLayoutPPE; i++) {

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            linearLayout.setGravity(Gravity.START);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayoutArrayPPE.add(linearLayout);

            linearLayoutPPE.addView(linearLayoutArrayPPE.get(i));
        }

        for (PPEDetail results : realm.where(PPEDetail.class).equalTo("jobID", SHARED_PREF_JOB_ID).findAll()) {

            ImageView image = new ImageView(this);

            switch (results.getPpeName()) {

                case "ear_protection":
                    Picasso.with(this)
                            .load(R.drawable.ear_protection)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "face_shield":
                    Picasso.with(this)
                            .load(R.drawable.face_shield)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "foot_protection":
                    Picasso.with(this)
                            .load(R.drawable.foot_protection)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "hand_protection":
                    Picasso.with(this)
                            .load(R.drawable.hand_protection)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "head_protection":
                    Picasso.with(this)
                            .load(R.drawable.head_protection)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "mandatory_instruction":
                    Picasso.with(this)
                            .load(R.drawable.mandatory_instruction)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "pedestrian_route":
                    Picasso.with(this)
                            .load(R.drawable.pedestrian_route)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "protective_clothing":
                    Picasso.with(this)
                            .load(R.drawable.protective_clothing)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "respirator":
                    Picasso.with(this)
                            .load(R.drawable.respirator)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "safety_glasses":
                    Picasso.with(this)
                            .load(R.drawable.safety_glasses)
                            .resize(110, 100)
                            .into(image);
                    break;

                case "safety_harness":
                    Picasso.with(this)
                            .load(R.drawable.safety_harness)
                            .resize(110, 100)
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

        //region button confirm
        Button btnConfirm = (Button) scanPPEDialog.findViewById(R.id.button_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPref.edit();

                if (mRadioButtonGHS.isChecked() && mRadioButtonPPE.isChecked()) {

                    //region update job status
                    editor.putString(SHARED_PREF_JOB_STATUS, STATUS_PPE).apply();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            JobDetail jobDetail = new JobDetail();
                            jobDetail.setJobID(sharedPref.getString(SHARED_PREF_JOB_ID, ""));
                            jobDetail.setJobStatus(STATUS_PPE);

                            realm.copyToRealmOrUpdate(jobDetail);
                        }
                    });
                    //endregion

                    mButtonPPE.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                    mButtonPPE.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                    mButtonSDS.setEnabled(true);

                    scanPPEDialog.dismiss();

                } else {

                    shortToast(getApplicationContext(), ERR_MSG_CHECK_PPE);
                }
            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) scanPPEDialog.findViewById(R.id.button_cancel);
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
    //endregion

    //endregion

    //region SDS File
    private class SDSWSAsync extends AsyncTask<String, Void, Void> {

        String jobID, sdsURL;
        ProgressDialog progressDialog;

        private SDSWSAsync(String jobID) {

            this.jobID = jobID;
        }

        @Override
        protected void onPreExecute() {

            //start progress dialog
            progressDialog = ProgressDialog.show(JobMainActivity.this, "Please wait..", "Loading...", true);
        }

        @Override
        protected Void doInBackground(String... params) {

            sdsURL = SDSWS.invokeRetrieveSDSWS(jobID);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (!sdsURL.isEmpty()) {

                //update job status
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(SHARED_PREF_JOB_STATUS, STATUS_SDS).apply();

                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        JobDetail jobDetail = new JobDetail();
                        jobDetail.setJobID(sharedPref.getString(SHARED_PREF_JOB_ID, ""));
                        jobDetail.setJobStatus(STATUS_SDS);

                        realm.copyToRealmOrUpdate(jobDetail);
                    }
                });

                //set button sds
                mButtonSDS.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                mButtonSDS.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                mButtonScanDetails.setEnabled(true);

                //close progress dialog
                progressDialog.dismiss();

                //open sds file in browser
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(SDS_FILE_LOCATION + sdsURL));
                startActivity(browserIntent);

            } else {

                //close progress dialog
                progressDialog.dismiss();

                //display alert dialog
                alertDialog(NO_SDS, NO_SDS_MESSAGE);

                mButtonSDS.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                mButtonSDS.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                mButtonScanDetails.setEnabled(true);
            }
        }
    }
    //endregion

    //region Safety Checks
    public void safetyChecks() {

        if (safetyChecksDialog != null && safetyChecksDialog.isShowing()) return;

        safetyChecksDialog = new Dialog(this);
        safetyChecksDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        safetyChecksDialog.setContentView(R.layout.dialog_driver_safety_check_vehicle);

        final RadioButton mRadioButtonWheelChoked = (RadioButton) safetyChecksDialog.findViewById(R.id.rbtnWheelChocked);
        final RadioButton mRadioButtonBondingWire = (RadioButton) safetyChecksDialog.findViewById(R.id.rbtnBondingWire);

        //region set radio button status
        if (sharedPref.getString(SHARED_PREF_JOB_STATUS, "").equals(STATUS_SAFETY_CHECKS)) {

            mRadioButtonWheelChoked.setChecked(true);
            mRadioButtonBondingWire.setChecked(true);

        } else {

            mRadioButtonWheelChoked.setChecked(false);
            mRadioButtonBondingWire.setChecked(false);
        }
        //endregion

        //region button confirm
        Button btnConfirm = (Button) safetyChecksDialog.findViewById(R.id.button_confirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = sharedPref.edit();

                if (mRadioButtonWheelChoked.isChecked() && mRadioButtonBondingWire.isChecked()) {

                    //region update job status
                    editor.putString(SHARED_PREF_JOB_STATUS, STATUS_SAFETY_CHECKS).apply();

                    realm.executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {

                            JobDetail jobDetail = new JobDetail();
                            jobDetail.setJobID(sharedPref.getString(SHARED_PREF_JOB_ID, ""));
                            jobDetail.setJobStatus(STATUS_SAFETY_CHECKS);

                            realm.copyToRealmOrUpdate(jobDetail);
                        }
                    });
                    //endregion

                    //close safety checks dialog
                    safetyChecksDialog.dismiss();

                    //navigate to loading operation activity
                    Intent intent = new Intent(getApplicationContext(), LoadingOperationActivity.class);
                    isActivityStarted = true;
                    startActivity(intent);

                } else {

                    shortToast(getApplicationContext(), SAFETY_CHECKS_MESSAGE);
                }
            }
        });
        //endregion

        //region button cancel
        Button btnCancel = (Button) safetyChecksDialog.findViewById(R.id.button_cancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //close safety checks dialog
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
    //endregion

    //region alert dialog
    private void alertDialog(String title, String message) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setIcon(R.drawable.ic_alert_circle_outline);
        alertDialog.setPositiveButton(OK, null);
        alertDialog.show();
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

        // if button is clicked, close the custom dialog
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

                            loadingBayDetail = new LoadingBayDetail();
                            loadingBayDetail.setLoadingBayNo(results.getLoadingBayNo());
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

    @Override
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
