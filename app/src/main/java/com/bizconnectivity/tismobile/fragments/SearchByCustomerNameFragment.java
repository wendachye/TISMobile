package com.bizconnectivity.tismobile.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.activities.SearchResultActivity;
import com.bizconnectivity.tismobile.database.models.JobDetail;
import com.bizconnectivity.tismobile.webservices.CustomerNameSearchWS;
import com.google.gson.Gson;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bizconnectivity.tismobile.Common.*;
import static com.bizconnectivity.tismobile.Constant.*;

public class SearchByCustomerNameFragment extends Fragment {

	@BindView(R.id.edit_customer_name)
	EditText mEditTextCustomerName;

	public SearchByCustomerNameFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_search_by_customer_name, container, false);

		ButterKnife.bind(this, view);

		mEditTextCustomerName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (mEditTextCustomerName.getText().toString().isEmpty()) {

					//show error message
					shortToast(getContext(), MSG_CUSTOMER_NAME_REQUIRED);

					return true;

				} else {

					if (isNetworkAvailable(getContext())) {


						new customerNameSearchWSAsync(mEditTextCustomerName.getText().toString()).execute();

						return true;

					} else {

						return true;
					}
				}
			}
		});

		return view;
	}

	private class customerNameSearchWSAsync extends AsyncTask<String, Void, Void> {

		ArrayList<JobDetail> jobDetailArrayList;
		String customerName;
		ProgressDialog progressDialog;

		private customerNameSearchWSAsync(String customerName) {

			this.customerName = customerName;
		}

		@Override
		protected void onPreExecute() {

			//start progress dialog
			progressDialog = ProgressDialog.show(getContext(), "Please wait..", "Loading...", true);
		}

		@Override
		protected Void doInBackground(String... params) {

			jobDetailArrayList = CustomerNameSearchWS.invokeRetrieveJobDetailByCustomerName(customerName);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (jobDetailArrayList.size() > 0) {

				//convert to json
				Gson gson = new Gson();
				String json = gson.toJson(jobDetailArrayList);

				//close progress dialog
				progressDialog.dismiss();

				//navigate to search result activity
				Intent intent = new Intent(getContext(), SearchResultActivity.class);
				intent.putExtra(KEY_SEARCH, json);
				startActivity(intent);

			} else {

				//close progress dialog
				progressDialog.dismiss();

				//show error message
				shortToast(getContext(), MSG_CUSTOMER_NAME_NOT_FOUND);
			}
		}
	}
}
