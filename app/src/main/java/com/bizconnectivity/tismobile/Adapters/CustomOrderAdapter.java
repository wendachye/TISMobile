package com.bizconnectivity.tismobile.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bizconnectivity.tismobile.Classes.TruckBayOrderList;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Activities.DashboardActivity;
import com.bizconnectivity.tismobile.Activities.JobMainActivity;
import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.WebServices.PPEWSAsync;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class CustomOrderAdapter extends ArrayAdapter<TruckBayOrderList> {

    Context context;
    Context jobMainContext;
    List<TruckBayOrderList> orderList;
    int layoutResID;

    public CustomOrderAdapter(Context context, int layoutResourceID, List<TruckBayOrderList> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.jobMainContext = context;
        this.orderList = listItems;
        this.layoutResID = layoutResourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        TruckBayOrderHolder holder;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new TruckBayOrderHolder();

            view = inflater.inflate(layoutResID, parent, false);
            holder.tvLoadingBayTitle = (TextView) view.findViewById(R.id.tvLoadingBayTitle);
            holder.lvTruckBayOrder = (LinearLayout) view.findViewById(R.id.lvTruckBayOrder);
            view.setTag(holder);

        } else
            holder = (TruckBayOrderHolder) view.getTag();

        TruckBayOrderList dItem = (TruckBayOrderList) this.orderList.get(position);

        holder.tvLoadingBayTitle.setText(dItem.getTitle());
        CustomTruckBayOrderAdapter adapter = new CustomTruckBayOrderAdapter(getContext().getApplicationContext(), R.layout.dashboard_truck_bay_order_item, dItem.getOrderList());

        holder.lvTruckBayOrder.removeAllViews();
        for (int i = 0; i < adapter.getCount(); i++) {
            View v = adapter.getView(i, null, parent);
            holder.lvTruckBayOrder.addView(v);

            final int x = i;
            final TruckBayOrderList item = dItem;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    String orderId = String.valueOf(item.getOrderList().get(x).getTimeSlotId());
					String armNo = String.valueOf(item.getOrderList().get(x).getRackNo());
					String productName = String.valueOf(item.getOrderList().get(x).getProduct());

                    if(context instanceof DashboardActivity)
                    {
                        DashboardActivity dashboardActivity = ((DashboardActivity) context);

                        SharedPreferences.Editor editor = dashboardActivity.sharedPref.edit();

                        editor.putString(Constant.SHARED_PREF_ORDER_ID_SELECTED, orderId);
                        editor.putString(Constant.SHARED_PREF_PRODUCT_NAME_SELECTED, productName);
                        editor.putString(Constant.SHARED_PREF_ARM_NO_SELECTED, armNo);
                        editor.commit();

                        Intent intent = new Intent(context, JobMainActivity.class);
                        context.startActivity(intent);

                    }

                    /*


                    if (jobMainContext instanceof Activity) {
                        JobMainActivity jobMainActivity = ((JobMainActivity) jobMainContext);

                        Set<String> orderIdSelectedList = jobMainActivity.sharedPref.getStringSet(Constant.SHARED_PREF_ORDER_ID_SELECTED, null);

                        if (orderIdSelectedList == null) {
                            orderIdSelectedList = new HashSet<String>();
                            orderIdSelectedList.add(orderId);
                        } else {
                            //List<String> orderIdSelectedList = Arrays.asList(orderIdSelected.split("\\s*,\\s*"));

                            if (!orderIdSelectedList.contains(orderId))
                                orderIdSelectedList.add(orderId);
                        }

                        SharedPreferences.Editor editor = jobMainActivity.sharedPref.edit();
                        editor.putStringSet(Constant.SHARED_PREF_ORDER_ID_SELECTED, orderIdSelectedList);
                        editor.commit();
                    }
                    context.startActivity(jobMainIntent);*/
                }
            });
        }
        return view;
    }

    private static class TruckBayOrderHolder {
        TextView tvLoadingBayTitle;
        LinearLayout lvTruckBayOrder;
    }
}

