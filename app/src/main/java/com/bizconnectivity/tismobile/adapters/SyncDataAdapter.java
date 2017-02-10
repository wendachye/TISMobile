package com.bizconnectivity.tismobile.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.database.models.JobDetail;

import java.util.List;

public class SyncDataAdapter extends RecyclerView.Adapter<SyncDataAdapter.ViewHolder>{

	private List<JobDetail> items;
	private int itemLayout;
	AdapterCallBack adapterCallBack;

	public SyncDataAdapter(List<JobDetail> items, int itemLayout, AdapterCallBack adapterCallBack) {

		this.items = items;
		this.itemLayout =itemLayout;
		this.adapterCallBack = adapterCallBack;
	}

	@Override
	public SyncDataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);

		return new SyncDataAdapter.ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

		holder.tvLoadingBayOrderId.setText(items.get(position).getJobID());
		holder.tvLoadingBayCustomer.setText(items.get(position).getCustomerName());
		holder.tvLoadingBayProduct.setText(items.get(position).getProductName());
		holder.tvLoadingBayTankNo.setText(items.get(position).getTankNo());
	}

	@Override
	public int getItemCount() {

		return items.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		TextView tvLoadingBayOrderId, tvLoadingBayCustomer, tvLoadingBayProduct, tvLoadingBayTankNo;

		private ViewHolder(View itemView) {

			super(itemView);

			tvLoadingBayOrderId = (TextView) itemView.findViewById(R.id.text_job_id);
			tvLoadingBayCustomer = (TextView) itemView.findViewById(R.id.text_customer_name);
			tvLoadingBayProduct = (TextView) itemView.findViewById(R.id.text_product_name);
			tvLoadingBayTankNo = (TextView) itemView.findViewById(R.id.text_tank_no);

			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {

		}
	}

	public interface AdapterCallBack {

		void adapterOnClick(int adapterPosition);
	}
}
