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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bizconnectivity.tismobile.database.datasources.LoadingBayDetailDataSource;
import com.bizconnectivity.tismobile.R;

import java.util.ArrayList;

import static com.bizconnectivity.tismobile.Common.formatWelcomeMsg;
import static com.bizconnectivity.tismobile.Common.shortToast;
import static com.bizconnectivity.tismobile.Constant.ERR_MSG_NO_TRUCK_BAY_CHECKED_IN;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_LOGINNAME;
import static com.bizconnectivity.tismobile.Constant.SHARED_PREF_NAME;
import static com.bizconnectivity.tismobile.Constant.TRUCK_BAY_CHECKED_OUT;

public class CheckOutActivity extends AppCompatActivity {

    //region declaration
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;
    TextView headerMessage;
    Dialog exitDialog;
    Button btnConfirm;
    LinearLayout footerLayout;
    SharedPreferences sharedPref;
    Spinner spLoadingBay;
    ArrayList<String> loadingBayArrayList;
    String message;
    //endregion

    LoadingBayDetailDataSource loadingBayDetailDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

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

        //region retrieve all loading bay
        loadingBayArrayList = new ArrayList<>();

        loadingBayDetailDataSource = new LoadingBayDetailDataSource(this);
        loadingBayDetailDataSource.open();
        loadingBayArrayList = loadingBayDetailDataSource.retrieveAllLoadingBay();
        loadingBayDetailDataSource.close();
        //endregion

        //region spinner loading bay
        spLoadingBay = (Spinner) findViewById(R.id.ddlTruckBayItem);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, loadingBayArrayList);
        spLoadingBay.setAdapter(adapter);
        //endregion

        //region button confirm
        btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (spLoadingBay.getSelectedItem() != null ) {

                    //delete selected loading bay
                    loadingBayDetailDataSource = new LoadingBayDetailDataSource(getApplicationContext());
                    loadingBayDetailDataSource.deleteSelectedLoadingBay(spLoadingBay.getSelectedItem().toString());

                    //check out message
                    message = spLoadingBay.getSelectedItem().toString() + TRUCK_BAY_CHECKED_OUT;
                    shortToast(getApplicationContext(), message);

                    Intent intent = new Intent(getApplicationContext(), CheckOutActivity.class);
                    finish();
                    startActivity(intent);

                } else {

                    shortToast(getApplicationContext(), ERR_MSG_NO_TRUCK_BAY_CHECKED_IN);
                }
            }
        });
        //endregion
    }

    //region Header
    /*-------- Set User Login Details --------*/
    public void setUserLoginDetails() {

        LinearLayout headerLayout = (LinearLayout) findViewById(R.id.headerCheckOut);
        headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);

        headerMessage.setText(formatWelcomeMsg(sharedPref.getString(SHARED_PREF_LOGINNAME, "")));
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
}
