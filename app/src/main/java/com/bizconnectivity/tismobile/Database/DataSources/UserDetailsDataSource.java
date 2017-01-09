package com.bizconnectivity.tismobile.Database.DataSources;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.bizconnectivity.tismobile.Classes.UserDetail;
import com.bizconnectivity.tismobile.Database.Contracts.UserDetailsContract.UserDetails;
import com.bizconnectivity.tismobile.Database.DatabaseSQLHelper;

public class UserDetailsDataSource {

	private SQLiteDatabase database;
	private DatabaseSQLHelper sqlHelper;

	public void open() {

		database = sqlHelper.getWritableDatabase();
	}

	public void close() {

		sqlHelper.close();
	}

	public void insertUserDetails(UserDetail userDetail) {

		// Gets the data repository in write mode
		database = sqlHelper.getWritableDatabase();

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(UserDetails.COLUMN_USERNAME, userDetail.getUsername());
		values.put(UserDetails.COLUMN_PASSWORD, userDetail.getPassword());
		values.put(UserDetails.COLUMN_NRIC, userDetail.getNric());

		// Insert the new row, returning the primary key value of the new row
		long newRowId = database.insert(UserDetails.TABLE_NAME, null, values);

	}
}
