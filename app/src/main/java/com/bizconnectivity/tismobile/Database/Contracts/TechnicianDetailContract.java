package com.bizconnectivity.tismobile.Database.Contracts;

import android.provider.BaseColumns;

public class TechnicianDetailContract {

	// To prevent someone from accidentally instantiating the contract class,
	// make the constructor private.
	private TechnicianDetailContract() {

	}

	/* Inner class that defines the table contents */
	public static class TechnicianDetails implements BaseColumns {

		public static final String TABLE_NAME = "TechnicianDetails";
		public static final String COLUMN_TECHNICIAN_ID = "TechnicianID";

	}
}
