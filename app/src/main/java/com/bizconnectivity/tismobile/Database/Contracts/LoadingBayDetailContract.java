package com.bizconnectivity.tismobile.database.contracts;

import android.provider.BaseColumns;

public class LoadingBayDetailContract {

	// To prevent someone from accidentally instantiating the contract class,
	// make the constructor private.
	private LoadingBayDetailContract() {

	}

	/* Inner class that defines the table contents */
	public static class LoadingBayDetails implements BaseColumns {

		public static final String TABLE_NAME = "LoadingBayDetails";
		public static final String COLUMN_LOADING_BAY_NO = "LoadingBayNo";

	}
}
