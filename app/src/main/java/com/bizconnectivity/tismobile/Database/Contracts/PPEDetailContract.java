package com.bizconnectivity.tismobile.Database.Contracts;

import android.provider.BaseColumns;

public class PPEDetailContract {

	// To prevent someone from accidentally instantiating the contract class,
	// make the constructor private.
	private PPEDetailContract() {

	}

	/* Inner class that defines the table contents */
	public static class PPEDetails implements BaseColumns {

		public static final String TABLE_NAME = "PPEDetails";
		public static final String COLUMN_JOB_ID = "JobID";
		public static final String COLUMN_PPE_IMAGE_NAME = "PPEImageName";

	}
}
