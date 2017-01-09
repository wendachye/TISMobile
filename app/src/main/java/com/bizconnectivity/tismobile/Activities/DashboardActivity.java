package com.bizconnectivity.tismobile.Activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bizconnectivity.tismobile.Adapters.CustomOrderAdapter;
import com.bizconnectivity.tismobile.Classes.Order;
import com.bizconnectivity.tismobile.Classes.TruckBayOrderList;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.WebServices.ConstantWS;
import com.bizconnectivity.tismobile.WebServices.DashboardWSAsync;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

public class DashboardActivity extends AppCompatActivity {

    //region Header and Footer
    Context context;

    //footer buttons
    ImageButton btnAlert, btnSearch, btnSwitch, btnSettings;

    //TextViews
    TextView headerMessage;

    //Dialog boxes
    Dialog exitDialog;
    //endregion

    public boolean isBayClicked;
    public SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //region Header and Footer
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        context = this;
        sharedPref = getSharedPreferences(Constant.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        /*-------- Set User Login Details --------*/
        setUserLoginDetails();

        /*-------- Footer Buttons --------*/
        setFooterMenu();
        //endregion

        setCheckedInTruckLoadingBayDetails();
    }

    public void setCheckedInTruckLoadingBayDetails() {
        Set<String> checkedInTruckLoadingBay = sharedPref.getStringSet(Constant.SHARED_PREF_TRUCK_LOADING_BAY, null);

        if (checkedInTruckLoadingBay != null) {

            String trunkBayString = "";
            for (String truckBayID : checkedInTruckLoadingBay) {
                if (trunkBayString.isEmpty())
                    trunkBayString = truckBayID;
                else
                    trunkBayString = trunkBayString + ", " + truckBayID;
            }

            TextView tvTruckBayTitle = (TextView) findViewById(R.id.tvTruckBayTitle);
            tvTruckBayTitle.setText(Common.formatCheckedInTruckLoadingBay(trunkBayString));

            Calendar calendar = Calendar.getInstance();
            if (Constant.DEBUG)
                calendar.set(2016, Calendar.MARCH, 22, 8, 0, 0);

            if (checkedInTruckLoadingBay.size() == 0) {
                LinearLayout tvTruckBayListHeader = (LinearLayout) findViewById(R.id.tvTruckBayListHeader);
                tvTruckBayListHeader.setVisibility(LinearLayout.INVISIBLE);
            } else {
                DashboardWSAsync task = new DashboardWSAsync(context, ConstantWS.WSTYPE_TODAY_LIST, calendar.getTime(), trunkBayString);
                task.execute();
            }
        } else {
            LinearLayout tvTruckBayListHeader = (LinearLayout) findViewById(R.id.tvTruckBayListHeader);
            tvTruckBayListHeader.setVisibility(LinearLayout.INVISIBLE);
        }
    }

    public void showTruckLoadingBayDetails(List<Order> list) {

        List<ArrayList<Order>> truckBayOrderList = new ArrayList<ArrayList<Order>>();
        List<String> truckBayList = new ArrayList<String>();

        List<TruckBayOrderList> loadingBayOrderList = new ArrayList<TruckBayOrderList>();

        for (int x = 0; x < list.size(); x++) {
            if (truckBayList.contains(list.get(x).getRackNo())) {
                int i = truckBayList.indexOf(list.get(x).getRackNo());
                truckBayOrderList.get(i).add(list.get(x));
            } else {
                truckBayList.add(list.get(x).getRackNo());
                truckBayOrderList.add(new ArrayList<Order>());
                int i = truckBayList.indexOf(list.get(x).getRackNo());
                truckBayOrderList.get(i).add(list.get(x));
            }
        }

        for (int x = 0; x < truckBayOrderList.size(); x++) {
            loadingBayOrderList.add(new TruckBayOrderList(Constant.LOADING_BAY + truckBayList.get(x), truckBayOrderList.get(x)));
        }

        ListView lvLoadingBayOrder = (ListView) findViewById(R.id.lvLoadingBayOrder);
        CustomOrderAdapter adapter = new CustomOrderAdapter(context, R.layout.dashboard_truck_bay_order_list, loadingBayOrderList);
        lvLoadingBayOrder.setAdapter(adapter);
    }

    //region Header
    /*-------- Set User Login Details --------*/
    public void setUserLoginDetails() {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constant.SHARED_PREF_LOGINNAME, Constant.LOGIN_LOGINNAME);
        editor.commit();

        LinearLayout headerLayout = (LinearLayout) findViewById(R.id.header);
        headerMessage = (TextView) headerLayout.findViewById(R.id.headerMessage);

        headerMessage.setText(Common.formatWelcomeMsg(Constant.LOGIN_LOGINNAME));
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

    }

    public void btnSearchClicked(View view) {
        Intent intentSearchJob = new Intent(context, SearchJobActivity.class);
        startActivity(intentSearchJob);
    }

    public void btnSwitchClicked(View view) {
        Intent intentSwitchTruckBay = new Intent(context, SwitchTruckBayActivity.class);
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
                        startActivity(intentCheckIn);
                        return true;
                    case R.id.settingsMenuExitApp:
                        exitApplication();
                        return true;
                    case R.id.settingsMenuCheckOut:
                        Intent intentCheckOut = new Intent(context, CheckOutActivity.class);
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
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear();
                editor.commit();

                Intent intentLogin = new Intent(getApplicationContext(), LoginActivity.class);
                intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentLogin);
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onPause();
    }

    //Confirmation dialog before exiting the application
    @Override
    public void onBackPressed() {
        exitApplication();
    }
}
