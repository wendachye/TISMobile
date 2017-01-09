package com.bizconnectivity.tismobile.Database.Contracts;

import android.provider.BaseColumns;

public class SealDetailsContract {

	// To prevent someone from accidentally instantiating the contract class,
	// make the constructor private.
	private SealDetailsContract() {

	}

	/* Inner class that defines the table contents */
	public static class SealDetails implements BaseColumns {

		public static final String TABLE_NAME = "SealDetails";
		public static final String COLUMN_JOB_ID = "JobID";
		public static final String COLUMN_SEAL_NO = "SealNo";
	}
}
