package com.bizconnectivity.tismobile.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bizconnectivity.tismobile.Classes.Order;
import com.bizconnectivity.tismobile.R;

import java.util.List;

public class CustomTruckBayOrderAdapter extends ArrayAdapter<Order> {

    Context context;
    List<Order> scheduleList;
    int layoutResID;

    public CustomTruckBayOrderAdapter(Context context, int layoutResourceID, List<Order> listItems) {
        super(context, layoutResourceID, listItems);
        this.context = context;
        this.scheduleList = listItems;
        this.layoutResID = layoutResourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        OrderHolder holder;
        View view = convertView;

        if (view == null) {
            //LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            holder = new OrderHolder();

            view = inflater.inflate(layoutResID, parent, false);
            holder.tvLoadingBayOrderId = (TextView) view.findViewById(R.id.tvLoadingBayOrderId);
            holder.tvLoadingBayCustomer = (TextView) view.findViewById(R.id.tvLoadingBayCustomer);
            holder.tvLoadingBayTankNo = (TextView) view.findViewById(R.id.tvLoadingBayTankNo);
            holder.tvLoadingBayProduct = (TextView) view.findViewById(R.id.tvLoadingBayProduct);
            view.setTag(holder);

        } else
            holder = (OrderHolder) view.getTag();

        Order dItem = (Order) this.scheduleList.get(position);

        holder.tvLoadingBayOrderId.setText(String.valueOf(dItem.getTimeSlotId()));
        holder.tvLoadingBayCustomer.setText(dItem.getCustomer());
        holder.tvLoadingBayTankNo.setText(dItem.getTankNo());
        holder.tvLoadingBayProduct.setText(dItem.getProduct());

        if (position % 2 == 1)
            view.setBackgroundColor(Color.parseColor(view.getResources().getString(R.color.list_view_color_white)));
        else
            view.setBackgroundColor(Color.parseColor(view.getResources().getString(R.color.list_view_color_grey)));

        return view;
    }

    private static class OrderHolder {
        TextView tvLoadingBayOrderId;
        TextView tvLoadingBayCustomer;
        TextView tvLoadingBayTankNo;
        TextView tvLoadingBayProduct;
    }

}
