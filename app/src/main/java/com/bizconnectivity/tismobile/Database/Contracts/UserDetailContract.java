package com.bizconnectivity.tismobile.database.contracts;

import android.provider.BaseColumns;

public class UserDetailContract {

	// To prevent someone from accidentally instantiating the contract class,
	// make the constructor private.
	private UserDetailContract() {

	}

	/* Inner class that defines the table contents */
	public static class UserDetails implements BaseColumns {

		public static final String TABLE_NAME = "UserDetails";
		public static final String COLUMN_USERNAME = "Username";
		public static final String COLUMN_PASSWORD = "Password";

	}

}
