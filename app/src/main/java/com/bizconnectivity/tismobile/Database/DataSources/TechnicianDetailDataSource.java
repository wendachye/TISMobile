package com.bizconnectivity.tismobile.database.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bizconnectivity.tismobile.classes.CheckIn;
import com.bizconnectivity.tismobile.database.contracts.TechnicianDetailContract.TechnicianDetails;
import com.bizconnectivity.tismobile.database.DatabaseSQLHelper;

public class TechnicianDetailDataSource {

	private SQLiteDatabase database;
	private DatabaseSQLHelper sqlHelper;

	public TechnicianDetailDataSource(Context context) {

		sqlHelper = new DatabaseSQLHelper(context);
	}

	public void open() {

		database = sqlHelper.getWritableDatabase();
	}

	public void close() {

		sqlHelper.close();
	}

	public void insertTechnicianNRIC (CheckIn checkIn) {

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { TechnicianDetails.COLUMN_TECHNICIAN_ID };

		// Filter results WHERE
		String selectionRetrieve = TechnicianDetails.COLUMN_TECHNICIAN_ID + " = ?";
		String[] selectionArgsRetrieve = { checkIn.getTechnicianNRIC() };

		Cursor cursor = database.query(
				TechnicianDetails.TABLE_NAME,             // The table to query
				projection,                               // The columns to return
				selectionRetrieve,                        // The columns for the WHERE clause
				selectionArgsRetrieve,                    // The values for the WHERE clause
				null,                                     // Group the rows
				null,                                     // Filter by row groups
				null                                      // The sort order
		);

		if (cursor.getCount() > 0) {

			// Create a new map of values, where column names are the keys
			ContentValues values = new ContentValues();
			values.put(TechnicianDetails.COLUMN_TECHNICIAN_ID, checkIn.getTechnicianNRIC());

			// Insert the new row, returning the primary key value of the new row
			database.insert(TechnicianDetails.TABLE_NAME, null, values);
		}

		cursor.close();
	}

	public boolean retrieveTechnicianNRIC (CheckIn checkIn) {

		boolean returnResult = false;

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { TechnicianDetails.COLUMN_TECHNICIAN_ID };

		// Filter results WHERE
		String selection= TechnicianDetails.COLUMN_TECHNICIAN_ID + " = ?";
		String[] selectionArgs = { checkIn.getTechnicianNRIC() };

		Cursor cursor = database.query(
				TechnicianDetails.TABLE_NAME,             // The table to query
				projection,                               // The columns to return
				selection,                                // The columns for the WHERE clause
				selectionArgs,                            // The values for the WHERE clause
				null,                                     // Group the rows
				null,                                     // Filter by row groups
				null                                      // The sort order
		);

		if (cursor.getCount() > 0) {

			returnResult = true;
		}

		cursor.close();

		return returnResult;
	}

}
