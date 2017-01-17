package com.bizconnectivity.tismobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.webservices.CustomerNameSearchWSAsync;

import static com.bizconnectivity.tismobile.Common.isNetworkAvailable;
import static com.bizconnectivity.tismobile.Common.shortToast;
import static com.bizconnectivity.tismobile.Constant.MSG_ORDER_ID_REQUIRED;

public class SearchByCustomerNameFragment extends Fragment {

	//region declaration
	EditText etCustomerName;
	Button btnSearch;
	//endregion

	public SearchByCustomerNameFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_search_by_customer_name, container, false);

		etCustomerName = (EditText) view.findViewById(R.id.etCustomerName);
		btnSearch = (Button) view.findViewById(R.id.btnSearch);

		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (isNetworkAvailable(getContext())) {

					if (etCustomerName.getText().toString().isEmpty()) {

						shortToast(getContext(), MSG_ORDER_ID_REQUIRED);

					} else {

						CustomerNameSearchWSAsync task = new CustomerNameSearchWSAsync(getContext(), etCustomerName.getText().toString());
						task.execute();
					}
				}
			}
		});

		return view;
	}

}
