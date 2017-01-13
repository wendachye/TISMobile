package com.bizconnectivity.tismobile.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Database.DataSources.LoadingBayDetailDataSource;
import com.bizconnectivity.tismobile.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CheckOutActivity extends AppCompatActivity {

    //region Header and Footer
    Context context;

    //footer buttons
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;

    //TextViews
    TextView headerMessage;

    //Dialog boxes
    Dialog exitDialog;
    //endregion

    public SharedPreferences sharedPref;

    LoadingBayDetailDataSource loadingBayDetailDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

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


        setCheckOutTruckBay();
    }

    public void setCheckOutTruckBay() {

        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingBayDetailDataSource = new LoadingBayDetailDataSource(context);
                loadingBayDetailDataSource.deleteAllLoadingBay();

                SharedPreferences.Editor editor = sharedPref.edit();

                editor.remove(Constant.SHARED_PREF_JOB_ID);
                editor.remove(Constant.SHARED_PREF_CUSTOMER_NAME);
                editor.remove(Constant.SHARED_PREF_PRODUCT_NAME);
                editor.remove(Constant.SHARED_PREF_TANK_NO);
                editor.remove(Constant.SHARED_PREF_LOADING_BAY);
                editor.remove(Constant.SHARED_PREF_LOADING_ARM);
                editor.remove(Constant.SHARED_PREF_SDS_FILE_PATH);
                editor.remove(Constant.SHARED_PREF_OPERATOR_ID);
                editor.remove(Constant.SHARED_PREF_DRIVER_ID);
                editor.remove(Constant.SHARED_PREF_WORK_INSTRUCTION);
                editor.remove(Constant.SHARED_PREF_PUMP_START_TIME);
                editor.remove(Constant.SHARED_PREF_PUMP_STOP_TIME);
                editor.remove(Constant.SHARED_PREF_RACK_OUT_TIME);
                editor.remove(Constant.SHARED_PREF_JOB_STATUS);
                editor.remove(Constant.SHARED_PREF_JOB_DATE);
                editor.remove(Constant.SHARED_PREF_BATCH_CONTROLLER);
                editor.remove(Constant.SHARED_PREF_BATCH_CONTROLLER_LITRE);
                editor.remove(Constant.SCAN_VALUE_BOTTOM_SEAL1);
                editor.remove(Constant.SCAN_VALUE_BOTTOM_SEAL2);
                editor.remove(Constant.SCAN_VALUE_BOTTOM_SEAL3);
                editor.remove(Constant.SCAN_VALUE_BOTTOM_SEAL4);
                editor.apply();

            }
        });
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
}
