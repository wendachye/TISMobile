package com.bizconnectivity.tismobile.database.contracts;

import android.provider.BaseColumns;

public class PPEContract {

	// To prevent someone from accidentally instantiating the contract class,
	// make the constructor private.
	private PPEContract() {

	}

	/* Inner class that defines the table contents */
	public static class PPE implements BaseColumns {

		public static final String TABLE_NAME = "PPE";
		public static final String COLUMN_PPE_ID = "PPEID";
		public static final String COLUMN_PPE_NAME = "PPEName";

	}
}
