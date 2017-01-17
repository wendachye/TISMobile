package com.bizconnectivity.tismobile.database.contracts;

import android.provider.BaseColumns;

public class GHSContract {

	// To prevent someone from accidentally instantiating the contract class,
	// make the constructor private.
	private GHSContract() {

	}

	/* Inner class that defines the table contents */
	public static class GHS implements BaseColumns {

		public static final String TABLE_NAME = "GHS";
		public static final String COLUMN_GHS_ID = "GHSID";
		public static final String COLUMN_GHS_NAME = "GHSName";

	}
}
