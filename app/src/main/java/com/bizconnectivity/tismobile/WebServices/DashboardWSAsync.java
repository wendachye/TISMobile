package com.bizconnectivity.tismobile.WebServices;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bizconnectivity.tismobile.Classes.Order;
import com.bizconnectivity.tismobile.Classes.OrderCount;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Activities.DashboardActivity;
import com.bizconnectivity.tismobile.R;

import java.util.Date;
import java.util.List;


public class DashboardWSAsync extends AsyncTask<String, Void, Void> {
    Context appContext;
    String type;
    Date date;
    int timeslotId;
    String rackNo;

    int outputCount;
    List<Order> orderList;
    List<OrderCount> countList;

    public DashboardWSAsync(Context context, String tType, Date d, String rack) {
        appContext = context;
        type = tType;
        date = d;
        rackNo = rack;
    }

    public DashboardWSAsync(String tType, int id) {
        type = tType;
        timeslotId = id;
    }

    @Override
    protected Void doInBackground(String... params) {
        if (type.equals(ConstantWS.WSTYPE_ALL_COUNT))
            countList = DashboardWS.invokeGetAllCountWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_LATE_COUNT))
            outputCount = DashboardWS.invokeGetLateCountWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_TODAY_COUNT))
            outputCount = DashboardWS.invokeGetPendingKeyInCountWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_QUEUEUP_COUNT))
            outputCount = DashboardWS.invokeGetKeyInCountWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_WEIGHIN_COUNT))
            outputCount = DashboardWS.invokeGetWeighInCountWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_WEIGHOUT_COUNT))
            outputCount = DashboardWS.invokeGetWeighOutCountWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_PUMPSTART_COUNT))
            outputCount = DashboardWS.invokeGetPumpInCountWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_DEPARTURE_COUNT))
            outputCount = DashboardWS.invokeGetKeyOutCountWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_LATE_LIST))
            orderList = DashboardWS.invokeRetrieveLateListWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_TODAY_LIST))
            orderList = DashboardWS.invokeRetrievePendingKeyInListWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_QUEUEUP_LIST))
            orderList = DashboardWS.invokeRetrieveKeyInListWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_WEIGHIN_LIST))
            orderList = DashboardWS.invokeRetrieveWeighInListWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_WEIGHOUT_LIST))
            orderList = DashboardWS.invokeRetrieveWeighOutListWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_PUMPSTART_LIST))
            orderList = DashboardWS.invokeRetrievePumpInListWS(date, rackNo);
        else if (type.equals(ConstantWS.WSTYPE_DEPARTURE_LIST))
            orderList = DashboardWS.invokeRetrieveKeyOutListWS(date, rackNo);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        if (type.equals(ConstantWS.WSTYPE_TODAY_LIST)) {
            if (appContext instanceof DashboardActivity) {
                LinearLayout tvTruckBayListHeader = (LinearLayout) ((DashboardActivity) appContext).findViewById(R.id.tvTruckBayListHeader);

//                if (orderList.size() > 0) {
//                    ((DashboardActivity) context).showTruckLoadingBayDetails(orderList);
//                    tvTruckBayListHeader.setVisibility(LinearLayout.VISIBLE);
//                } else {
//                    tvTruckBayListHeader.setVisibility(LinearLayout.INVISIBLE);
//                    Toast.makeText(context, Constant.ERR_MSG_NO_ORDER_FOR_TRUCK_BAY, Toast.LENGTH_SHORT).show();
//                }
            }
        }

        /*if(type.equals(ConstantWS.WSTYPE_ALL_COUNT))
        {
            if(context instanceof Activity)
            {
                *//*LinearLayout summary;
                if(rackNo.isEmpty())
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.TerminalSummary);
                }
                else
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.BaySummary);
                }

                TextView tvLateCount = (TextView)summary.findViewById(R.id.tvLateCount);
                tvLateCount.setText(String.valueOf(countList.get(0).getLateCount()));

                TextView tvTodayCount = (TextView)summary.findViewById(R.id.tvTodayCount);
                tvTodayCount.setText(String.valueOf(countList.get(0).getTodayCount()));

                TextView tvQueueUpCount = (TextView)summary.findViewById(R.id.tvQueueUpCount);
                tvQueueUpCount.setText(String.valueOf(countList.get(0).getQueueUpCount()));

                TextView tvWeighInCount = (TextView)summary.findViewById(R.id.tvWeighInCount);
                tvWeighInCount.setText(String.valueOf(countList.get(0).getWeighInCount()));

                TextView tvWeighOutCount = (TextView)summary.findViewById(R.id.tvWeighOutCount);
                tvWeighOutCount.setText(String.valueOf(countList.get(0).getWeighOutCount()));

                TextView tvPumpStartCount = (TextView)summary.findViewById(R.id.tvPumpStartCount);
                tvPumpStartCount.setText(String.valueOf(countList.get(0).getPumpStartCount()));

                TextView tvDepartureCount = (TextView)summary.findViewById(R.id.tvDepartureCount);
                tvDepartureCount.setText(String.valueOf(countList.get(0).getDepartureCount()));*//*
            }
        }
        else if(type.equals(ConstantWS.WSTYPE_LATE_COUNT))
        {
            if(context instanceof Activity)
            {
               *//* LinearLayout summary;
                if(rackNo.isEmpty())
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.TerminalSummary);
                }
                else
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.BaySummary);
                }

                TextView tvLateCount = (TextView)summary.findViewById(R.id.tvLateCount);
                tvLateCount.setText(String.valueOf(outputCount));*//*
            }
        }
        else if(type.equals(ConstantWS.WSTYPE_TODAY_COUNT))
        {
            if(context instanceof Activity)
            {
               *//* LinearLayout summary;
                if(rackNo.isEmpty())
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.TerminalSummary);
                }
                else
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.BaySummary);
                }

                TextView tvTodayCount = (TextView)summary.findViewById(R.id.tvTodayCount);
                tvTodayCount.setText(String.valueOf(outputCount));*//*
            }
        }
        else if(type.equals(ConstantWS.WSTYPE_QUEUEUP_COUNT))
        {
            if(context instanceof Activity)
            {
                *//*LinearLayout summary;
                if(rackNo.isEmpty())
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.TerminalSummary);
                }
                else
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.BaySummary);
                }

                TextView tvQueueUpCount = (TextView)summary.findViewById(R.id.tvQueueUpCount);
                tvQueueUpCount.setText(String.valueOf(outputCount));*//*
            }
        }
        else if(type.equals(ConstantWS.WSTYPE_WEIGHIN_COUNT))
        {
            if(context instanceof Activity)
            {
                *//*LinearLayout summary;
                if(rackNo.isEmpty())
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.TerminalSummary);
                }
                else
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.BaySummary);
                }

                TextView tvWeighInCount = (TextView)summary.findViewById(R.id.tvWeighInCount);
                tvWeighInCount.setText(String.valueOf(outputCount));*//*
            }
        }
        else if(type.equals(ConstantWS.WSTYPE_WEIGHOUT_COUNT))
        {
            if(context instanceof Activity)
            {
                *//*LinearLayout summary;
                if(rackNo.isEmpty())
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.TerminalSummary);
                }
                else
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.BaySummary);
                }

                TextView tvWeighOutCount = (TextView)summary.findViewById(R.id.tvWeighOutCount);
                tvWeighOutCount.setText(String.valueOf(outputCount));*//*
            }
        }
        else if(type.equals(ConstantWS.WSTYPE_PUMPSTART_COUNT))
        {
            if(context instanceof Activity)
            {
                *//*LinearLayout summary;
                if(rackNo.isEmpty())
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.TerminalSummary);
                }
                else
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.BaySummary);
                }

                TextView tvPumpStartCount = (TextView)summary.findViewById(R.id.tvPumpStartCount);
                tvPumpStartCount.setText(String.valueOf(outputCount));*//*
            }
        }
        else if(type.equals(ConstantWS.WSTYPE_DEPARTURE_COUNT))
        {
            if(context instanceof Activity)
            {
                *//*LinearLayout summary;
                if(rackNo.isEmpty())
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.TerminalSummary);
                }
                else
                {
                    summary = (LinearLayout)((Activity)context).findViewById(R.id.BaySummary);
                }

                TextView tvDepartureCount = (TextView)summary.findViewById(R.id.tvDepartureCount);
                tvDepartureCount.setText(String.valueOf(outputCount));*//*
            }
        }

        else if(type.equals(ConstantWS.WSTYPE_LATE_LIST) || type.equals(ConstantWS.WSTYPE_TODAY_LIST)
                || type.equals(ConstantWS.WSTYPE_QUEUEUP_LIST) || type.equals(ConstantWS.WSTYPE_WEIGHIN_LIST)
                || type.equals(ConstantWS.WSTYPE_WEIGHOUT_LIST) || type.equals(ConstantWS.WSTYPE_PUMPSTART_LIST)
                || type.equals(ConstantWS.WSTYPE_DEPARTURE_LIST))
        {
            if(context instanceof DashboardActivity)
            {
                LinearLayout tvTruckBayListHeader = (LinearLayout) ((DashboardActivity) context).findViewById(R.id.tvTruckBayListHeader);

                if(orderList.size() > 0) {
                    ((DashboardActivity) context).showTruckLoadingBayDetails(orderList);
                    tvTruckBayListHeader.setVisibility(LinearLayout.VISIBLE);
                }
                else
                {
                    tvTruckBayListHeader.setVisibility(LinearLayout.INVISIBLE);
                    Toast toast = Toast.makeText(context, Constant.ERR_MSG_NO_ORDER_FOR_TRUCK_BAY, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }*/
    }

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }
}

