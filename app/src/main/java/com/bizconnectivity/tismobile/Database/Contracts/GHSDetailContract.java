package com.bizconnectivity.tismobile.database.contracts;

import android.provider.BaseColumns;

public class GHSDetailContract {

	// To prevent someone from accidentally instantiating the contract class,
	// make the constructor private.
	private GHSDetailContract() {

	}

	/* Inner class that defines the table contents */
	public static class GHSDetails implements BaseColumns {

		public static final String TABLE_NAME = "GHSDetails";
		public static final String COLUMN_JOB_ID = "JobID";
		public static final String COLUMN_GHS_ID = "GHSID";

	}
}
