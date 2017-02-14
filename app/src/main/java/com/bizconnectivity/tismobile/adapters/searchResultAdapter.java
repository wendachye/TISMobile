package com.bizconnectivity.tismobile.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.activities.JobMainActivity;
import com.bizconnectivity.tismobile.activities.LoadingOperationActivity;
import com.bizconnectivity.tismobile.activities.ScanDetailsActivity;
import com.bizconnectivity.tismobile.activities.SearchResultActivity;
import com.bizconnectivity.tismobile.activities.StopOperationActivity;
import com.bizconnectivity.tismobile.database.models.JobDetail;

import java.util.List;

import io.realm.Realm;

import static com.bizconnectivity.tismobile.Constant.*;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder>{

	//region declaration
	private Context context;
	private List<JobDetail> items;
	private int itemLayout;
	AdapterCallBack adapterCallBack;
	//endregion

	public SearchResultAdapter(Context context, List<JobDetail> items, int itemLayout, AdapterCallBack adapterCallBack) {

		this.context = context;
		this.items = items;
		this.itemLayout =itemLayout;
		this.adapterCallBack = adapterCallBack;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);

		return new ViewHolder(view);
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

		ViewHolder(View itemView) {

			super(itemView);

			tvLoadingBayOrderId = (TextView) itemView.findViewById(R.id.text_job_id);
			tvLoadingBayCustomer = (TextView) itemView.findViewById(R.id.text_customer_name);
			tvLoadingBayProduct = (TextView) itemView.findViewById(R.id.text_product_name);
			tvLoadingBayTankNo = (TextView) itemView.findViewById(R.id.text_tank_no);

			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {

			adapterCallBack.adapterOnClick(getAdapterPosition());

			//retrieve job details
			String jobID = items.get(getAdapterPosition()).getJobID();
			Realm realm = Realm.getDefaultInstance();
			JobDetail jobDetail = realm.where(JobDetail.class).equalTo("jobID", jobID).findFirst();

			//store shared preferences
			storeJobDetailSharedPref(jobDetail);

			//status of job for navigation
			statusNavigation(jobDetail.getJobStatus());
		}
	}

	private void storeJobDetailSharedPref(JobDetail jobDetail) {

		SharedPreferences sharedPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPref.edit();

		editor.putString(SHARED_PREF_JOB_ID, jobDetail.getJobID());
		editor.putString(SHARED_PREF_CUSTOMER_NAME, jobDetail.getCustomerName());
		editor.putString(SHARED_PREF_PRODUCT_NAME, jobDetail.getProductName());
		editor.putString(SHARED_PREF_TANK_NO, jobDetail.getTankNo());
		editor.putString(SHARED_PREF_LOADING_BAY, jobDetail.getLoadingBayNo());
		editor.putString(SHARED_PREF_LOADING_ARM, jobDetail.getLoadingArm());
		editor.putString(SHARED_PREF_SDS_FILE_PATH, jobDetail.getSdsFilePath());
		editor.putString(SHARED_PREF_OPERATOR_ID, jobDetail.getOperatorID());
		editor.putString(SHARED_PREF_DRIVER_ID, jobDetail.getDriverID());
		editor.putString(SHARED_PREF_WORK_INSTRUCTION, jobDetail.getWorkInstruction());
		editor.putString(SHARED_PREF_PUMP_START_TIME, jobDetail.getPumpStartTime());
		editor.putString(SHARED_PREF_PUMP_STOP_TIME, jobDetail.getPumpStopTime());
		editor.putString(SHARED_PREF_RACK_OUT_TIME, jobDetail.getRackOutTime());
		editor.putString(SHARED_PREF_JOB_STATUS, jobDetail.getJobStatus());
		editor.putString(SHARED_PREF_JOB_DATE, jobDetail.getJobDate());
		editor.apply();
	}

	private void statusNavigation(String jobStatus) {

		switch (jobStatus) {

			case STATUS_PENDING:

				Intent intent = new Intent(context, JobMainActivity.class);
				((SearchResultActivity) context).finish();
				context.startActivity(intent);
				break;

			case STATUS_PPE:

				Intent intentPPE = new Intent(context, JobMainActivity.class);
				((SearchResultActivity) context).finish();
				context.startActivity(intentPPE);
				break;

			case STATUS_SDS:

				Intent intentSDS = new Intent(context, JobMainActivity.class);
				((SearchResultActivity) context).finish();
				context.startActivity(intentSDS);
				break;

			case STATUS_OPERATOR_ID:

				Intent intentSD = new Intent(context, ScanDetailsActivity.class);
				((SearchResultActivity) context).finish();
				context.startActivity(intentSD);
				break;

			case STATUS_DRIVER_ID:

				Intent intentDI = new Intent(context, ScanDetailsActivity.class);
				((SearchResultActivity) context).finish();
				context.startActivity(intentDI);
				break;

			case STATUS_WORK_INSTRUCTION:

				Intent intentWI = new Intent(context, JobMainActivity.class);
				((SearchResultActivity) context).finish();
				context.startActivity(intentWI);
				break;

			case STATUS_SAFETY_CHECKS :

				Intent intentSC = new Intent(context, LoadingOperationActivity.class);
				((SearchResultActivity) context).finish();
				context.startActivity(intentSC);
				break;

			case STATUS_SCAN_LOADING_ARM:

				Intent intentLA = new Intent(context, LoadingOperationActivity.class);
				((SearchResultActivity) context).finish();
				context.startActivity(intentLA);
				break;

			case STATUS_BATCH_CONTROLLER:

				Intent intentBC = new Intent(context, LoadingOperationActivity.class);
				((SearchResultActivity) context).finish();
				context.startActivity(intentBC);
				break;

			case STATUS_PUMP_START:

				Intent intentPS = new Intent(context, StopOperationActivity.class);
				((SearchResultActivity) context).finish();
				context.startActivity(intentPS);
				break;

			case STATUS_PUMP_STOP:

				Intent intentPSTP = new Intent(context, StopOperationActivity.class);
				((SearchResultActivity) context).finish();
				context.startActivity(intentPSTP);
				break;

			case STATUS_SCAN_SEAL:

				Intent intentSS = new Intent(context, StopOperationActivity.class);
				((SearchResultActivity) context).finish();
				context.startActivity(intentSS);
				break;

			default:

				break;
		}
	}

	public interface AdapterCallBack {

		void adapterOnClick(int adapterPosition);
	}
}
