package com.bizconnectivity.tismobile.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.classes.CheckIn;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.classes.GHS;
import com.bizconnectivity.tismobile.classes.GHSDetail;
import com.bizconnectivity.tismobile.classes.JobDetail;
import com.bizconnectivity.tismobile.classes.PPE;
import com.bizconnectivity.tismobile.classes.PPEDetail;
import com.bizconnectivity.tismobile.classes.SealDetail;
import com.bizconnectivity.tismobile.database.datasources.GHSDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.JobDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.LoadingBayDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.PPEDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.SealDetailDataSource;
import com.bizconnectivity.tismobile.database.datasources.TechnicianDetailDataSource;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.webservices.CheckInWS;
import com.bizconnectivity.tismobile.webservices.GHSWS;
import com.bizconnectivity.tismobile.webservices.JobDetailWS;
import com.bizconnectivity.tismobile.webservices.PPEWS;
import com.bizconnectivity.tismobile.webservices.SealNoWS;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import static com.bizconnectivity.tismobile.Common.shortToast;
import static com.bizconnectivity.tismobile.Constant.ERR_MSG_INVALID_TRUCK_BAY;
import static com.bizconnectivity.tismobile.Constant.ERR_MSG_TRUCK_BAY_ALREADY_CHECKED_IN;
import static com.bizconnectivity.tismobile.Constant.SCAN_MSG_CANCEL_SCANNING;
import static com.bizconnectivity.tismobile.Constant.SCAN_MSG_INVALID_DATA_RECEIVED;
import static com.bizconnectivity.tismobile.Constant.SCAN_MSG_NO_DATA_RECEIVED;
import static com.bizconnectivity.tismobile.Constant.SCAN_MSG_PROMPT_TECHNICIAN_ID;
import static com.bizconnectivity.tismobile.Constant.SCAN_MSG_PROMPT_TRUCK_LOADING_BAY;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_TECHNICIAN_ID;
import static com.bizconnectivity.tismobile.Constant.SCAN_VALUE_TRUCK_LOADING_BAY;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOGINNAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_NAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_SCAN_VALUE;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_TECHNICIAN_ID;


public class CheckInActivity extends AppCompatActivity {

    //region declaration
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;
    TextView headerMessage, tvTruckBayId, tvTechnicianId;
    Dialog exitDialog;
    LinearLayout footerLayout;
    Button btnScanTechnician, btnScanTruckBay;
    SharedPreferences sharedPref;
    TechnicianDetailDataSource technicianDetailDataSource;
    LoadingBayDetailDataSource loadingBayDetailDataSource;
    CheckIn checkIn;
    ArrayList<String> loadingBayArrayList;
    String technicianID, trunkBayString;
    boolean returnResult;
    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);

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

        //region button technician id
        tvTechnicianId = (TextView) findViewById(R.id.tvTechnicianId);
        btnScanTechnician = (Button) findViewById(R.id.btnScanTechnician);
        btnScanTechnician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnScanTechnicianClicked();
            }
        });
        //endregion

        //region button loading bay
        tvTruckBayId = (TextView) findViewById(R.id.tvTruckBayId);
        btnScanTruckBay = (Button) findViewById(R.id.btnScanTruckBay);
        btnScanTruckBay.setEnabled(false);
        btnScanTruckBay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                btnScanTruckBayClicked();
            }
        });
        //endregion

        //region check and set technician nric & loading bay no
        technicianID = sharedPref.getString(SHARED_PREF_TECHNICIAN_ID, "");

        loadingBayDetailDataSource = new LoadingBayDetailDataSource(this);

        loadingBayDetailDataSource.open();
        loadingBayArrayList = loadingBayDetailDataSource.retrieveAllLoadingBay();
        loadingBayDetailDataSource.close();

        trunkBayString = "";

        for (int i=0; i<loadingBayArrayList.size(); i++) {

            if (trunkBayString.isEmpty()) {

                trunkBayString = loadingBayArrayList.get(i);

            } else {

                trunkBayString = trunkBayString + ", " + loadingBayArrayList.get(i);
            }

        }

        if (!technicianID.isEmpty() && loadingBayArrayList.size() > 0){

            btnScanTechnician.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            btnScanTechnician.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
            tvTechnicianId.setText(technicianID);

            btnScanTruckBay.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            btnScanTruckBay.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
            tvTruckBayId.setText(trunkBayString);
            btnScanTruckBay.setEnabled(true);

        } else if (!technicianID.isEmpty()) {

            btnScanTechnician.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            btnScanTechnician.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen));
            tvTechnicianId.setText(technicianID);

            btnScanTruckBay.setEnabled(true);

        } else {

            btnScanTechnician.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
            btnScanTechnician.getBackground().clearColorFilter();
            tvTechnicianId.setText("");

            btnScanTruckBay.setTextColor(ContextCompat.getColor(this, R.color.colorWhite));
            btnScanTruckBay.getBackground().clearColorFilter();
            tvTruckBayId.setText("");
        }
        //endregion
    }

    public void btnScanTechnicianClicked() {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_TECHNICIAN_ID).apply();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(SCAN_MSG_PROMPT_TECHNICIAN_ID);
        integrator.setBeepEnabled(true);
        integrator.initiateScan();
    }

    public void btnScanTruckBayClicked() {

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SHARED_PREF_SCAN_VALUE, SCAN_VALUE_TRUCK_LOADING_BAY).apply();

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt(SCAN_MSG_PROMPT_TRUCK_LOADING_BAY);
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

                if (returnScanValue.equals(SCAN_VALUE_TECHNICIAN_ID)) {

//                    if (Common.isNetworkAvailable(this)) {
//
//                        //check technician nric with web service
//                        TechnicianIDWSAsync task = new TechnicianIDWSAsync(this, scanContent);
//                        task.execute();
//
//                    } else {
//
//                        //check technician nric with sqlite database
//                        checkIn = new CheckIn();
//                        checkIn.setTechnicianNRIC(scanContent);
//
//                        checkTechnicianNRIC(checkIn);
//                    }

                    editor.putString(Constant.SHARED_PREF_TECHNICIAN_ID, scanContent).apply();

                    Intent intent = new Intent(this, CheckInActivity.class);
                    finish();
                    startActivity(intent);

                } else if (returnScanValue.equals(SCAN_VALUE_TRUCK_LOADING_BAY)) {

                    if (Common.isNetworkAvailable(this)) {

                        //check loading bay no with web service
                        new loadingBayAsync(scanContent).execute();

                    } else {

                        //check loading bay no with database
//                        checkIn = new CheckIn();
//                        checkIn.setLoadingBayNo(scanContent);
//
//                        checkLoadingBayNo(checkIn);
                        shortToast(this, "No internet connection.");
                    }

                } else {

                    //invalid scan type
                    shortToast(this, SCAN_MSG_INVALID_DATA_RECEIVED);
                }

            } else {

                //no data received
                shortToast(this, SCAN_MSG_NO_DATA_RECEIVED);
            }

        } else {

            // If scan data is not received (for example, if the user cancels the scan by pressing the back button),
            // we can simply output a message.
            shortToast(this, SCAN_MSG_CANCEL_SCANNING);
        }
    }
    //endregion

    public void checkTechnicianNRIC(CheckIn checkIn) {

        SharedPreferences.Editor editor = sharedPref.edit();

        technicianDetailDataSource = new TechnicianDetailDataSource(this);
        technicianDetailDataSource.open();
        returnResult = technicianDetailDataSource.retrieveTechnicianNRIC(checkIn);
        technicianDetailDataSource.close();

        if (returnResult) {

            editor.putString(SHARED_PREF_TECHNICIAN_ID, checkIn.getTechnicianNRIC()).apply();

            Intent intent = new Intent(this, CheckInActivity.class);
            finish();
            startActivity(intent);

        } else {

            editor.putString(SHARED_PREF_TECHNICIAN_ID, "").apply();

            shortToast(this, Constant.ERR_MSG_INVALID_TECHNICIAN_NRIC);

            Intent intent = new Intent(this, CheckInActivity.class);
            finish();
            startActivity(intent);
        }
    }

    public void checkLoadingBayNo(CheckIn checkIn) {

        loadingBayDetailDataSource = new LoadingBayDetailDataSource(this);
        loadingBayDetailDataSource.open();
        returnResult = loadingBayDetailDataSource.checkLoadingBayNo(checkIn);
        loadingBayDetailDataSource.close();

        if (returnResult) {

            Intent intent = new Intent(this, CheckInActivity.class);
            finish();
            startActivity(intent);

        } else {

            shortToast(this, ERR_MSG_INVALID_TRUCK_BAY);
        }
    }

    //region Header
    /*-------- Set User Login Details --------*/
    public void setUserLoginDetails() {

        LinearLayout headerLayout = (LinearLayout) findViewById(R.id.headerCheckIn);
        headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);

        headerMessage.setText(Common.formatCheckInMsg(sharedPref.getString(SHARED_PREF_LOGINNAME, "")));
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
                        return true;

                    case R.id.settingsMenuExitApp:
                        exitApplication();
                        return true;

                    case R.id.settingsMenuCheckOut:
                        Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
                        finish();
                        startActivity(intent);
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
        Button btnConfirm = (Button) exitDialog.findViewById(R.id.btnConfirm);

        // if button is clicked, close the custom dialog
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

    @Override
    public void onBackPressed() {

        exitApplication();
    }

    private class loadingBayAsync extends AsyncTask<String, Void, Void> {

        ProgressDialog progressDialog;
        String rackNo;
        boolean response;

        private loadingBayAsync(String rackNo) {

            this.rackNo = rackNo;
        }

        @Override
        protected void onPreExecute() {

            //start progress dialog
            progressDialog = ProgressDialog.show(CheckInActivity.this, "Please wait..", "Loading...", true);
        }

        @Override
        protected Void doInBackground(String... params) {

            response = CheckInWS.invokeCheckTruckRack(rackNo);

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(Void result) {

            if (response) {

                //region insert loading bay to database
                checkIn = new CheckIn();
                loadingBayDetailDataSource = new LoadingBayDetailDataSource(getApplicationContext());

                checkIn.setLoadingBayNo(rackNo);
                loadingBayDetailDataSource.open();
                returnResult = loadingBayDetailDataSource.insertLoadingBayNo(getApplicationContext(), checkIn);
                loadingBayDetailDataSource.close();

                if (!returnResult) {

                    shortToast(getApplicationContext(), ERR_MSG_TRUCK_BAY_ALREADY_CHECKED_IN);
                }
                //endregion

                //get all the job details from web service
                new jobDetailsAsync(rackNo).execute();

                //new thread for button settings
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (trunkBayString.isEmpty()) {

                            trunkBayString = rackNo;
                            tvTruckBayId.setText(trunkBayString);

                        } else {

                            trunkBayString = trunkBayString + ", " + rackNo;
                            tvTruckBayId.setText(trunkBayString);
                        }

                        btnScanTruckBay.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorWhite));
                        btnScanTruckBay.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
                        btnScanTruckBay.setEnabled(true);
                    }
                });

                //close progress dialog
                progressDialog.dismiss();

            } else {

                //close progress dialog
                progressDialog.dismiss();

                //prompt error message
                shortToast(getApplicationContext(), ERR_MSG_INVALID_TRUCK_BAY);
            }
        }
    }

    private class jobDetailsAsync extends AsyncTask<String, Void, Void> {

        String rackNo;
        ArrayList<JobDetail> jobDetailArrayList;
        JobDetailDataSource jobDetailDataSource;

        private  jobDetailsAsync(String rackNo) {

            this.rackNo = rackNo;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {

            jobDetailArrayList = new ArrayList<>();
            jobDetailArrayList = JobDetailWS.invokeRetrieveAllJobs(rackNo);

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(Void result) {

            if (jobDetailArrayList.size() > 0) {

                for (int i=0; i<jobDetailArrayList.size(); i++) {

                    //insert or update database
                    jobDetailDataSource = new JobDetailDataSource(getApplicationContext());
                    jobDetailDataSource.open();
                    jobDetailDataSource.insertOrUpdateJobDetails(jobDetailArrayList.get(i));
                    jobDetailDataSource.close();

                    //retrieve all the ppe and ghs from web service
                    new PPEGHSAsync(jobDetailArrayList.get(i).getJobID(), jobDetailArrayList.get(i).getProductName()).execute();

                    //retrieve all the seal no
                    new sealNoAsync(jobDetailArrayList.get(i).getJobID()).execute();
                }
            }
        }
    }

    private class PPEGHSAsync extends AsyncTask<String, Void, Void> {

        String jobID, productName, ppeURL, ppeName, ghsURL, ghsName;
        ArrayList<PPE> ppeArrayList;
        ArrayList<GHS> ghsArrayList;
        ArrayList<PPEDetail> ppeDetailArrayList;
        ArrayList<GHSDetail> ghsDetailArrayList;
        PPEDetail ppeDetail;
        GHSDetail ghsDetail;
        PPEDetailDataSource ppeDetailDataSource;
        GHSDetailDataSource ghsDetailDataSource;

        private PPEGHSAsync(String jobID, String productName) {

            this.jobID = jobID;
            this.productName = productName;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {

            ppeArrayList = new ArrayList<>();
            ppeArrayList = PPEWS.invokeRetrievePPEWS(productName);

            ghsArrayList = new ArrayList<>();
            ghsArrayList = GHSWS.invokeRetrieveGHSWS(productName);

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        @Override
        protected void onPostExecute(Void result) {

            //insert ppe details to database
            if (ppeArrayList.size() > 0) {

                ppeDetailArrayList = new ArrayList<>();

                for (int i = 0; i < ppeArrayList.size(); i++) {

                    ppeDetail = new PPEDetail();
                    ppeURL = ppeArrayList.get(i).getPpePictureURL();
                    ppeName = ppeURL.substring(0, ppeURL.indexOf("."));

                    ppeDetail.setJobID(jobID);

                    switch (ppeName) {

                        case "ear_protection":
                            ppeDetail.setPpeID("1");
                            break;

                        case "face_shield":
                            ppeDetail.setPpeID("2");
                            break;

                        case "foot_protection":
                            ppeDetail.setPpeID("3");
                            break;

                        case "hand_protection":
                            ppeDetail.setPpeID("4");
                            break;

                        case "head_protection":
                            ppeDetail.setPpeID("5");
                            break;

                        case "mandatory_instruction":
                            ppeDetail.setPpeID("6");
                            break;

                        case "pedestrian_route":
                            ppeDetail.setPpeID("7");
                            break;

                        case "protective_clothing":
                            ppeDetail.setPpeID("8");
                            break;

                        case "respirator":
                            ppeDetail.setPpeID("9");
                            break;

                        case "safety_glasses":
                            ppeDetail.setPpeID("10");
                            break;

                        case "safety_harness":
                            ppeDetail.setPpeID("11");
                            break;

                        default:
                            break;
                    }

                    ppeDetailArrayList.add(ppeDetail);
                }

                ppeDetailDataSource = new PPEDetailDataSource(getApplicationContext());
                ppeDetailDataSource.open();
                ppeDetailDataSource.insertOrUpdatePPE(ppeDetailArrayList);
                ppeDetailDataSource.close();
            }

            //insert ghs details into to database
            if (ghsArrayList.size() > 0) {

                ghsDetailArrayList = new ArrayList<>();

                for (int i=0; i<ghsArrayList.size(); i++) {

                    ghsDetail = new GHSDetail();
                    ghsURL = ghsArrayList.get(i).getGhsPictureURL();
                    ghsName = ghsURL.substring(0, ghsURL.indexOf("."));

                    ghsDetail.setJobID(jobID);

                    switch (ghsName) {

                        case "AcuteToxicity":
                            ghsDetail.setGhsID("1");
                            break;

                        case "AspirationToxicity":
                            ghsDetail.setGhsID("2");
                            break;

                        case "Corrosive":
                            ghsDetail.setGhsID("3");
                            break;

                        case "EnvironmentToxicity":
                            ghsDetail.setGhsID("4");
                            break;

                        case "Explosive":
                            ghsDetail.setGhsID("5");
                            break;

                        case "Flammable":
                            ghsDetail.setGhsID("6");
                            break;

                        case "GasesUnderPressure":
                            ghsDetail.setGhsID("7");
                            break;

                        case "Irritant":
                            ghsDetail.setGhsID("8");
                            break;

                        case "Oxidiser":
                            ghsDetail.setGhsID("9");
                            break;

                        default:
                            break;
                    }

                    ghsDetailArrayList.add(ghsDetail);
                }

                ghsDetailDataSource = new GHSDetailDataSource(getApplicationContext());
                ghsDetailDataSource.open();
                ghsDetailDataSource.insertOrUpdateGHS(ghsDetailArrayList);
                ghsDetailDataSource.close();
            }

        }
    }

    private class sealNoAsync extends AsyncTask<String, Void, Void> {

        String jobID;
        ArrayList<String> sealNoArrayList;
        SealDetailDataSource sealDetailDataSource;

        private sealNoAsync(String jobID) {

            this.jobID = jobID;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(String... params) {

            sealNoArrayList = new ArrayList<>();
            sealNoArrayList = SealNoWS.invokeRetrieveSealNo(jobID);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {

            if (sealNoArrayList.size() > 0) {

                sealDetailDataSource = new SealDetailDataSource(getApplicationContext());
                sealDetailDataSource.open();
                sealDetailDataSource.insertSealNo(jobID, sealNoArrayList);
                sealDetailDataSource.close();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }
}
