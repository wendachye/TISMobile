package com.bizconnectivity.tismobile.Database.Contracts;

import android.provider.BaseColumns;

public class UserDetailsContract {

	// To prevent someone from accidentally instantiating the contract class,
	// make the constructor private.
	private UserDetailsContract() {

	}

	/* Inner class that defines the table contents */
	public static class UserDetails implements BaseColumns {

		public static final String TABLE_NAME = "UserDetails";
		public static final String COLUMN_USERNAME = "Username";
		public static final String COLUMN_PASSWORD = "Password";
		public static final String COLUMN_NRIC = "NRIC";

	}

}
