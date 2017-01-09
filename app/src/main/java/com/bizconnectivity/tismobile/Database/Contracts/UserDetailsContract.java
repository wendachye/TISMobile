package com.bizconnectivity.tismobile.Database.Contracts;

import android.provider.BaseColumns;

public class UserDetailsContract {

	// To prevent someone from accidentally instantiating the contract class,
	// make the constructor private.
	private UserDetailsContract() {

	}

	/* Inner class that defines the table contents */
	public static class UserLoginDetails implements BaseColumns {
		public static final String TABLE_NAME = "UserDetails";
		public static final String COLUMN_USER_DETAILS_ID = "id";
		public static final String COLUMN_USERNAME = "username";
		public static final String COLUMN_PASSWORD = "password";
		public static final String COLUMN_NRIC = "nric";
	}

}
