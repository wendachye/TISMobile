package com.bizconnectivity.tismobile.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.bizconnectivity.tismobile.R;
import com.bizconnectivity.tismobile.webservices.OrderIDSearchAsync;

import static com.bizconnectivity.tismobile.Common.isNetworkAvailable;
import static com.bizconnectivity.tismobile.Common.shortToast;
import static com.bizconnectivity.tismobile.Constant.MSG_ORDER_ID_REQUIRED;

public class SearchByOrderIDFragment extends Fragment {

	//region declaration
	EditText etOrderID;
	Button btnSearch;
	//endregion

	public SearchByOrderIDFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		View view = inflater.inflate(R.layout.fragment_search_by_order_id, container, false);

		etOrderID = (EditText) view.findViewById(R.id.etOrderID);
		btnSearch = (Button) view.findViewById(R.id.btnSearch);

		btnSearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (isNetworkAvailable(getContext())) {

					if (etOrderID.getText().toString().isEmpty()) {

						shortToast(getContext(), MSG_ORDER_ID_REQUIRED);

					} else {

						OrderIDSearchAsync task = new OrderIDSearchAsync(getContext(), etOrderID.getText().toString());
						task.execute();
					}
				}
			}
		});

		return view;
	}


}
