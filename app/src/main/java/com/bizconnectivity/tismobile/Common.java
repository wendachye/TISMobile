package com.bizconnectivity.tismobile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class Common {

    public static String formatWelcomeMsg(String loginName) {

        return "WELCOME " + loginName + ".";
    }

    public static String formatCheckInMsg(String loginName) {

        return "WELCOME " + loginName + ". PLEASE ENSURE YOU ARE CHECK-IN TO YOUR TRUCK LOADING BAY.";
    }

    public static String formatCheckedInTruckLoadingBay(String truckLoadingBay) {

        return "TRUCK LOADING BAY: " + truckLoadingBay;
    }

    public static boolean isNetworkAvailable(final Context context) {

        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static void shortToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void longToast(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
