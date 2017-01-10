package com.bizconnectivity.tismobile.Database.DataSources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bizconnectivity.tismobile.Classes.CheckIn;
import com.bizconnectivity.tismobile.Database.Contracts.TechnicianDetailContract.TechnicianDetails;
import com.bizconnectivity.tismobile.Database.DatabaseSQLHelper;

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

	public void insertTechnicianDetail(CheckIn checkIn) {

		// Gets the data repository in write mode
		database = sqlHelper.getWritableDatabase();

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

		if (cursor.moveToFirst()) {

			// Create a new map of values, where column names are the keys
			ContentValues values = new ContentValues();
			values.put(TechnicianDetails.COLUMN_TECHNICIAN_ID, checkIn.getTechnicianNRIC());

			// Insert the new row, returning the primary key value of the new row
			database.insert(TechnicianDetails.TABLE_NAME, null, values);
		}

		cursor.close();
	}


}
