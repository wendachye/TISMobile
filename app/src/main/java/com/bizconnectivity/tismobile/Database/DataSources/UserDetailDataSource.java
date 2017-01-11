package com.bizconnectivity.tismobile.Database.DataSources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bizconnectivity.tismobile.Classes.UserDetail;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Database.Contracts.UserDetailContract.UserDetails;
import com.bizconnectivity.tismobile.Database.DatabaseSQLHelper;

public class UserDetailDataSource {

	private SQLiteDatabase database;
	private DatabaseSQLHelper sqlHelper;

	public UserDetailDataSource(Context context) {

		sqlHelper = new DatabaseSQLHelper(context);
	}

	public void open() {

		database = sqlHelper.getWritableDatabase();
	}

	public void close() {

		sqlHelper.close();
	}

	public void insertOrUpdateUserDetails(UserDetail userDetail) {

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { UserDetails.COLUMN_USERNAME };

		// Filter results WHERE
		String selectionRetrieve = UserDetails.COLUMN_USERNAME + " = ?";
		String[] selectionArgsRetrieve = { userDetail.getUsername() };

		Cursor cursor = database.query(
				UserDetails.TABLE_NAME,                   // The table to query
				projection,                               // The columns to return
				selectionRetrieve,                        // The columns for the WHERE clause
				selectionArgsRetrieve,                    // The values for the WHERE clause
				null,                                     // Group the rows
				null,                                     // Filter by row groups
				null                                      // The sort order
		);

		if (cursor.getCount() > 0) {

			// New value for one column
			ContentValues values = new ContentValues();
			values.put(UserDetails.COLUMN_PASSWORD, userDetail.getPassword());

			// Which row to update, based on the title
			String selectionUpdate = UserDetails.COLUMN_USERNAME + " = ?";
			String[] selectionArgsUpdate = { userDetail.getUsername() };

			database.update(UserDetails.TABLE_NAME, values, selectionUpdate, selectionArgsUpdate);

		} else {

			// Create a new map of values, where column names are the keys
			ContentValues values = new ContentValues();
			values.put(UserDetails.COLUMN_USERNAME, userDetail.getUsername());
			values.put(UserDetails.COLUMN_PASSWORD, userDetail.getPassword());

			// Insert the new row, returning the primary key value of the new row
			database.insert(UserDetails.TABLE_NAME, null, values);

		}

		cursor.close();

	}

	public String retrieveUserDetails(UserDetail userDetail) {

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { UserDetails.COLUMN_PASSWORD };

		// Filter results WHERE
		String selection= UserDetails.COLUMN_USERNAME + " = ?";
		String[] selectionArgs = { userDetail.getUsername() };

		Cursor cursor = database.query(
				UserDetails.TABLE_NAME,                   // The table to query
				projection,                               // The columns to return
				selection,                                // The columns for the WHERE clause
				selectionArgs,                            // The values for the WHERE clause
				null,                                     // Group the rows
				null,                                     // Filter by row groups
				null                                      // The sort order
		);

		if (cursor.getCount() > 0) {

			String password = cursor.getString(cursor.getColumnIndex(UserDetails.COLUMN_PASSWORD));

			if (password.equals(userDetail.getPassword())) {

				cursor.close();

				return Constant.MSG_LOGIN_CORRECT;

			} else {

				cursor.close();

				return Constant.ERR_MSG_INCORRECT_PASSWORD;

			}

		} else {

			cursor.close();

			return Constant.ERR_MSG_INCORRECT_USERNAME;
		}

	}
}
