package com.bizconnectivity.tismobile.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.bizconnectivity.tismobile.classes.JobDetail;
import com.bizconnectivity.tismobile.classes.LoadingBayList;
import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.R;

import java.util.ArrayList;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;
	private ArrayList<LoadingBayList> loadingBayLists;

	public CustomExpandableListAdapter(Context context, ArrayList<LoadingBayList> loadingBayLists) {

		this.context = context;
		this.loadingBayLists = loadingBayLists;

	}

	@Override
	public Object getGroup(int groupPosition) {

		return this.loadingBayLists.get(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {

		return groupPosition;
	}

	@Override
	public int getGroupCount() {

		return this.loadingBayLists.size();
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

		LoadingBayList group = (LoadingBayList) getGroup(groupPosition);

		if (convertView == null) {

			LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = layoutInflater.inflate(R.layout.dashboard_truck_bay_order_list, parent, false);
		}

		TextView tvLoadingBayTitle = (TextView) convertView.findViewById(R.id.tvLoadingBayTitle);
		tvLoadingBayTitle.setTypeface(null, Typeface.BOLD);

		tvLoadingBayTitle.setText(Common.formatCheckedInTruckLoadingBay(group.getGroupTitle()));

		return convertView;
	}



	@Override
	public Object getChild(int groupPosition, int childPosition) {

		ArrayList<JobDetail> childList = this.loadingBayLists.get(groupPosition).getJobDetails();

		return childList.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {

		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {

		ArrayList<JobDetail> childList = this.loadingBayLists.get(groupPosition).getJobDetails();

		return childList.size();
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

		JobDetail jobDetail = (JobDetail) getChild(groupPosition, childPosition);

		if (convertView == null) {

			LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			convertView = layoutInflater.inflate(R.layout.dashboard_truck_bay_order_item, parent, false);
		}

		TextView tvLoadingBayOrderId = (TextView) convertView.findViewById(R.id.tvLoadingBayOrderId);
		tvLoadingBayOrderId.setText(jobDetail.getJobID());

		TextView tvLoadingBayCustomer = (TextView) convertView.findViewById(R.id.tvLoadingBayCustomer);
		tvLoadingBayCustomer.setText(jobDetail.getCustomerName());

		TextView tvLoadingBayProduct = (TextView) convertView.findViewById(R.id.tvLoadingBayProduct);
		tvLoadingBayProduct.setText(jobDetail.getProductName());

		TextView tvLoadingBayTankNo = (TextView) convertView.findViewById(R.id.tvLoadingBayTankNo);
		tvLoadingBayTankNo.setText(jobDetail.getTankNo());

		return convertView;
	}



	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
