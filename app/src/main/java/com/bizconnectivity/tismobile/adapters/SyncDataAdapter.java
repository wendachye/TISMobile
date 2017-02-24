package com.bizconnectivity.tismobile.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.activities.SyncDataActivity;
import com.bizconnectivity.tismobile.database.models.JobDetail;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class SyncDataAdapter extends RealmRecyclerViewAdapter<JobDetail, SyncDataAdapter.MyViewHolder> {

	private final SyncDataActivity activity;

	public SyncDataAdapter(SyncDataActivity activity, OrderedRealmCollection<JobDetail> data) {

		super(activity, data, true);

		this.activity = activity;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_search_result, parent, false);

		return new MyViewHolder(itemView);
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {

		JobDetail jobDetail = getData().get(position);

		holder.data = jobDetail;
		holder.mTextViewJobID.setText(jobDetail.getJobID());
		holder.mTextViewCustomerName.setText(jobDetail.getCustomerName());
		holder.mTextViewProductName.setText(jobDetail.getProductName());
		holder.mTextViewTankNo.setText(jobDetail.getTankNo());
	}

	class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

		public TextView mTextViewJobID;
		public TextView mTextViewCustomerName;
		public TextView mTextViewProductName;
		public TextView mTextViewTankNo;
		public JobDetail data;

		public MyViewHolder(View itemView) {

			super(itemView);

			mTextViewJobID = (TextView) itemView.findViewById(R.id.text_job_id);
			mTextViewCustomerName = (TextView) itemView.findViewById(R.id.text_customer_name);
			mTextViewProductName = (TextView) itemView.findViewById(R.id.text_product_name);
			mTextViewTankNo = (TextView) itemView.findViewById(R.id.text_tank_no);

			itemView.setOnLongClickListener(this);
		}

		@Override
		public boolean onLongClick(View v) {

			return true;
		}
	}
}
