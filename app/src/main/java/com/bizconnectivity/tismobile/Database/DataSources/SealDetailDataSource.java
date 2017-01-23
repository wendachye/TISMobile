package com.bizconnectivity.tismobile.database.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bizconnectivity.tismobile.classes.SealDetail;
import com.bizconnectivity.tismobile.database.DatabaseSQLHelper;
import com.bizconnectivity.tismobile.database.contracts.SealDetailContract.SealDetails;

import java.util.ArrayList;

public class SealDetailDataSource {

	private SQLiteDatabase database;
	private DatabaseSQLHelper sqlHelper;

	public SealDetailDataSource(Context context) {

		sqlHelper = new DatabaseSQLHelper(context);
	}

	public void open() {

		database = sqlHelper.getWritableDatabase();
	}

	public void close() {

		sqlHelper.close();
	}

	public void insertSealDetails(String jobID, String sealNo) {

		// Create a new map of values, where column names are the keys
		ContentValues values = new ContentValues();
		values.put(SealDetails.COLUMN_JOB_ID, jobID);
		values.put(SealDetails.COLUMN_SEAL_NO, sealNo);
		values.put(SealDetails.COLUMN_SEAL_USED, "used");

		// Insert the new row, returning the primary key value of the new row
		database.insert(SealDetails.TABLE_NAME, null, values);
	}

	public void insertSealNo(String jobID, ArrayList<String> sealNoArrayList) {

		for (int i=0; i<sealNoArrayList.size(); i++) {

			// Define a projection that specifies which columns from the database
			// you will actually use after this query.
			String[] projection = { SealDetails.COLUMN_SEAL_NO };

			// Filter results WHERE
			String selectionRetrieve = SealDetails.COLUMN_JOB_ID + " = ?" + " AND " + SealDetails.COLUMN_SEAL_NO + " = ?";
			String[] selectionArgsRetrieve = { jobID, sealNoArrayList.get(i) };

			Cursor cursor = database.query(
					SealDetails.TABLE_NAME,                    // The table to query
					projection,                               // The columns to return
					selectionRetrieve,                        // The columns for the WHERE clause
					selectionArgsRetrieve,                    // The values for the WHERE clause
					null,                                     // Group the rows
					null,                                     // Filter by row groups
					null                                      // The sort order
			);

			if (cursor.getCount() == 0) {

				// Create a new map of values, where column names are the keys
				ContentValues values = new ContentValues();
				values.put(SealDetails.COLUMN_JOB_ID, jobID);
				values.put(SealDetails.COLUMN_SEAL_NO, sealNoArrayList.get(i));

				// Insert the new row, returning the primary key value of the new row
				database.insert(SealDetails.TABLE_NAME, null, values);
			}

			cursor.close();
		}
	}

	public boolean checkRetrieveSealNo(String jobID, String sealNo) {

		boolean returnResult = false;

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { SealDetails.COLUMN_SEAL_NO };

		// Filter results WHERE
		String selectionRetrieve = SealDetails.COLUMN_JOB_ID + " = ?" + " AND " + SealDetails.COLUMN_SEAL_NO + " = ?";
		String[] selectionArgsRetrieve = { jobID, sealNo };

		Cursor cursor = database.query(
				SealDetails.TABLE_NAME,                    // The table to query
				projection,                               // The columns to return
				selectionRetrieve,                        // The columns for the WHERE clause
				selectionArgsRetrieve,                    // The values for the WHERE clause
				null,                                     // Group the rows
				null,                                     // Filter by row groups
				null                                      // The sort order
		);

		if (cursor.moveToFirst()) {

			returnResult = true;
		}

		cursor.close();

		return returnResult;
	}

	public ArrayList<String> retrieveSealNo(String jobID) {

		ArrayList<String> sealNo = new ArrayList<>();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { SealDetails.COLUMN_SEAL_NO };

		// Filter results WHERE
		String selectionRetrieve = SealDetails.COLUMN_JOB_ID + " = ?" + " AND " + SealDetails.COLUMN_SEAL_USED + " =?";
		String[] selectionArgsRetrieve = { jobID, "used" };

		Cursor cursor = database.query(
				SealDetails.TABLE_NAME,                   // The table to query
				projection,                               // The columns to return
				selectionRetrieve,                        // The columns for the WHERE clause
				selectionArgsRetrieve,                    // The values for the WHERE clause
				null,                                     // Group the rows
				null,                                     // Filter by row groups
				null                                      // The sort order
		);

		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				sealNo.add(cursor.getString(cursor.getColumnIndexOrThrow(SealDetails.COLUMN_SEAL_NO)));
			}
		}

		cursor.close();

		return sealNo;
	}
}
