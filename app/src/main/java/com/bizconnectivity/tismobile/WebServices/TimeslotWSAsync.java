package com.bizconnectivity.tismobile.WebServices;

/**
 * Created by User on 11/11/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.bizconnectivity.tismobile.Classes.JobStatus;
import com.bizconnectivity.tismobile.Classes.TimeslotDetail;

import java.util.Date;
import java.util.List;

public class TimeslotWSAsync extends AsyncTask<String, Void, Void> {
    Context appContext;
    String type;
    Date date;
    int timeslotId, operatorId;
    String name, seal, pos;
    JobStatus jobStat;

    int outputCount, outputId;
    List<TimeslotDetail> detailList;
    List<String> strList;
    List<JobStatus> statList;

    public TimeslotWSAsync(Context context, String tType, int id) {
        appContext = context;
        type = tType;
        timeslotId = id;
    }

    public TimeslotWSAsync(Context context, String tType, int id, String str) {
        appContext = context;
        type = tType;
        timeslotId = id;

        if (type.equals(ConstantWS.WSTYPE_GET_NUM_INCORRECT_SEAL))
            seal = str;
        else
            name = str;
    }

    public TimeslotWSAsync(String tType, JobStatus stat) {
        type = tType;
        jobStat = stat;
    }

    public TimeslotWSAsync(Context context, String tType, String n) {
        appContext = context;
        type = tType;
        name = n;
    }

    public TimeslotWSAsync(String tType, int id, Date d, String n) {
        type = tType;
        timeslotId = id;
        date = d;
        name = n;
    }

    public TimeslotWSAsync(String tType, int slotId, int operId, String n) {
        type = tType;
        timeslotId = slotId;
        operatorId = operId;
        name = n;
    }

    public TimeslotWSAsync(String tType, String sealNo, int id, String sealPos, String UpdatedBy) {
        type = tType;
        seal = sealNo;
        timeslotId = id;
        pos = sealPos;
        name = UpdatedBy;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (type.equals(ConstantWS.WSTYPE_GET_TIMESLOT_DETAIL))
            detailList = TimeslotWS.invokeRetrieveTimeSlotWS(timeslotId);
        else if (type.equals(ConstantWS.WSTYPE_UPDATE_PUMPSTART))
            TimeslotWS.invokeUpdatePumpStartWS(timeslotId, date, name);
        else if (type.equals(ConstantWS.WSTYPE_UPDATE_PUMPSTOP))
            TimeslotWS.invokeUpdatePumpStopWS(timeslotId, date, name);
        else if (type.equals(ConstantWS.WSTYPE_GET_PRODUCT_GHS))
            strList = TimeslotWS.invokeRetrieveProductGHSWS(name);
        else if (type.equals(ConstantWS.WSTYPE_GET_PRODUCT_PPE))
            strList = TimeslotWS.invokeRetrieveProductPPEWS(name);
        else if (type.equals(ConstantWS.WSTYPE_GET_SEAL_USED))
            strList = TimeslotWS.invokeGetSealUsedWS(timeslotId);
        else if (type.equals(ConstantWS.WSTYPE_CREATE_SEAL_USED))
            TimeslotWS.invokeCreateSealUsedWS(seal, timeslotId, pos, name);
        else if (type.equals(ConstantWS.WSTYPE_GET_NUM_INCORRECT_SEAL))
            outputCount = TimeslotWS.invokeGetNumIncorrectSealWS(timeslotId, seal);
        else if (type.equals(ConstantWS.WSTYPE_RETRIEVE_JOB_STATUS))
            statList = TimeslotWS.invokeRetrieveJobStatusWS(timeslotId);
        else if (type.equals(ConstantWS.WSTYPE_UPDATE_JOB_STATUS))
            TimeslotWS.invokeUpdateJobStatusWS(jobStat);
        else if (type.equals(ConstantWS.WSTYPE_GET_OPERATOR_ID))
            outputId = TimeslotWS.invokeGetOperatorIdWS(name);
        else if (type.equals(ConstantWS.WSTYPE_CREATE_OPERATOR))
            TimeslotWS.invokeCreateOperatorWS(timeslotId, operatorId, name);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (type.equals(ConstantWS.WSTYPE_GET_TIMESLOT_DETAIL)) {
            if (appContext instanceof Activity) {
                /*TextView tvArm = (TextView) ((Activity) context).findViewById(R.id.tvArm);
                tvArm.setText("ARM " + detailList.get(0).getArmNo());
                OrderDetailActivity activity = ((OrderDetailActivity) context);
                activity.detail = detailList.get(0);*/
            }
        } else if (type.equals(ConstantWS.WSTYPE_GET_SEAL_USED)) {
            if (appContext instanceof Activity) {
                /*OrderDetailActivity activity = ((OrderDetailActivity) context);
                activity.sealUsedList = strList;*/
            }
        } else if (type.equals(ConstantWS.WSTYPE_GET_PRODUCT_GHS)) {
            if (appContext instanceof Activity) {
                /*OrderDetailActivity activity = ((OrderDetailActivity) context);
                activity.ghsList = strList;
                activity.ghsImgList.clear();
                for (int x = 0; x < strList.size(); x++) {
                    activity.ghsImgList.add(activity.loadImageFromURL(Const.GHS_FILE_LOCATION + strList.get(x), Float.valueOf(activity.btnPPERequired.getTag().toString())));
                }*/
            }
        } else if (type.equals(ConstantWS.WSTYPE_GET_PRODUCT_PPE)) {
            if (appContext instanceof Activity) {
                /*OrderDetailActivity activity = ((OrderDetailActivity) context);
                activity.ppeList = strList;
                for (int x = 0; x < strList.size(); x++) {
                    activity.ppeImgList.add(activity.loadImageFromURL(Const.PPE_FILE_LOCATION + strList.get(x), Float.valueOf(activity.btnPPERequired.getTag().toString())));
                }*/
            }
        } else if (type.equals(ConstantWS.WSTYPE_GET_NUM_INCORRECT_SEAL)) {
            if (appContext instanceof Activity) {
                /*OrderDetailActivity activity = ((OrderDetailActivity) context);
                if (outputCount == 0) {
                    String[] sealList = seal.split(",");
                    for (int x = 0; x < sealList.length; x++) {
                        InvokeWS.createSealUsed(context, sealList[x], timeslotId, "bottom", Const.loginName);
                    }

                    activity.jobStat.setIsSeal(true);
                    activity.jobStat.setStatus("Departure");
                    InvokeWS.updateJobStatus(context, activity.jobStat);

                    activity.scanDialog.dismiss();
                    ColorDrawable buttonColor = (ColorDrawable) activity.btnScanSeal.getBackground();
                    int colorId = buttonColor.getColor();
                    if (colorId == context.getResources().getColor(R.color.layout_red)) {
                        activity.btnScanSeal.setBackgroundColor(context.getResources().getColor(R.color.layout_green));

                        buttonColor = (ColorDrawable) activity.btnDeparture.getBackground();
                        colorId = buttonColor.getColor();
                        if (colorId == context.getResources().getColor(R.color.job_locked_color)) {
                            activity.btnDeparture.setBackgroundColor(context.getResources().getColor(R.color.layout_red));
                        }
                    }
                } else {
                    Toast toast = Toast.makeText(context, String.valueOf(outputCount) + " of the seal is incorrect.", Toast.LENGTH_SHORT);
                    toast.show();
                }*/
            }
        } else if (type.equals(ConstantWS.WSTYPE_RETRIEVE_JOB_STATUS)) {
            if (appContext instanceof Activity) {
               /* OrderDetailActivity activity = ((OrderDetailActivity) context);

                if (statList.size() > 0) {
                    activity.jobStat = statList.get(0);
                    activity.initJobStatus();
                }*/
            }
        } else if (type.equals(ConstantWS.WSTYPE_GET_OPERATOR_ID)) {
            if (appContext instanceof Activity) {
                /*if (outputId == 0) {
                    Toast toast = Toast.makeText(context, "Invalid Operator.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    TextView tvScanOperator = (TextView) ((Activity) context).findViewById(R.id.tvScanOperator);
                    tvScanOperator.setText("OPERATOR ID:" + name);

                    InvokeWS.createOperator(context, timeslotId, outputId, name);
                    LinearLayout btnScanOperator = (LinearLayout) ((Activity) context).findViewById(R.id.btnScanOperator);
                    LinearLayout btnScanDriver = (LinearLayout) ((Activity) context).findViewById(R.id.btnScanDriver);

                    OrderDetailActivity activity = ((OrderDetailActivity) context);

                    activity.jobStat.setIsOperator(true);
                    activity.jobStat.setOperatorId(outputId);
                    activity.jobStat.setStatus("Scan Driver");
                    InvokeWS.updateJobStatus(context, activity.jobStat);

                    activity.scanDialog.dismiss();

                    ColorDrawable buttonColor = (ColorDrawable) btnScanOperator.getBackground();
                    int colorId = buttonColor.getColor();
                    if (colorId == activity.getResources().getColor(R.color.layout_red)) {
                        btnScanOperator.setBackgroundColor(activity.getResources().getColor(R.color.layout_green));

                        buttonColor = (ColorDrawable) btnScanDriver.getBackground();
                        colorId = buttonColor.getColor();
                        if (colorId == activity.getResources().getColor(R.color.job_locked_color)) {
                            btnScanDriver.setBackgroundColor(activity.getResources().getColor(R.color.layout_red));
                        }
                    }
                }*/
            }
        }
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}
