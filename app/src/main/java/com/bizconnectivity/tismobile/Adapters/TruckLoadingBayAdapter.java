package com.bizconnectivity.tismobile.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TruckLoadingBayAdapter extends RecyclerView.Adapter<TruckLoadingBayAdapter.ViewHolder> {

	Context context;
	ArrayList<String> items;
	int itemLayout;

	public TruckLoadingBayAdapter(Context context, ArrayList<String> items, int itemLayout) {
		this.context = context;
		this.items = items;
		this.itemLayout =itemLayout;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

		View view = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
		return new ViewHolder(view);

	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {

	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

		public ViewHolder(View itemView) {
			super(itemView);


		}

		@Override
		public void onClick(View v) {

		}
	}
}
