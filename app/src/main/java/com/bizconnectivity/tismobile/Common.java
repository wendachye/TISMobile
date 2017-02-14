package com.bizconnectivity.tismobile;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.bizconnectivity.tismobile.database.models.JobDetail;

import io.realm.Realm;

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

    public static String loadingBayString(String first, String next){

    	return first + ", " + next;
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

    public static void updateJobStatus(final String jobID, final String status) {

        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                JobDetail jobDetail = realm.where(JobDetail.class).equalTo("jobID", jobID).findFirst();
                jobDetail.setJobStatus(status);

                realm.copyToRealmOrUpdate(jobDetail);
            }
        });
    }
}
