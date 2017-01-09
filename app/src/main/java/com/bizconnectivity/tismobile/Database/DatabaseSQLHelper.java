package com.bizconnectivity.tismobile.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bizconnectivity.tismobile.Database.Contracts.UserDetailsContract.UserLoginDetails;

public class DatabaseSQLHelper extends SQLiteOpenHelper{

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "TISMobile.db";

	private static final String SQL_CREATE_USER_DETAILS =
			"CREATE TABLE" + UserLoginDetails.TABLE_NAME + " (" +
			UserLoginDetails.COLUMN_USER_DETAILS_ID + " INTEGER PRIMARY KEY" +
			UserLoginDetails.COLUMN_USERNAME + " TEXT" +
			UserLoginDetails.COLUMN_PASSWORD + " TEXT" +
			UserLoginDetails.COLUMN_NRIC + " TEXT)";

	private static final String SQL_DELETE_USER_DETAILS =
			"DROP TABLE IF EXISTS " + UserLoginDetails.TABLE_NAME;


	public DatabaseSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(SQL_CREATE_USER_DETAILS);
		onCreate(db);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// This database is only a cache for online data, so its upgrade policy is
		// to simply to discard the data and start over
		db.execSQL(SQL_DELETE_USER_DETAILS);
		onCreate(db);

	}
}
