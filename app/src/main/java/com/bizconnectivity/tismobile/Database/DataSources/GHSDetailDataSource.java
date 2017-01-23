package com.bizconnectivity.tismobile.database.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bizconnectivity.tismobile.classes.GHS;
import com.bizconnectivity.tismobile.classes.GHSDetail;
import com.bizconnectivity.tismobile.database.contracts.GHSDetailContract.GHSDetails;
import com.bizconnectivity.tismobile.database.DatabaseSQLHelper;

import java.util.ArrayList;

public class GHSDetailDataSource {

	private SQLiteDatabase database;
	private DatabaseSQLHelper sqlHelper;

	public GHSDetailDataSource(Context context) {

		sqlHelper = new DatabaseSQLHelper(context);
	}

	public void open() {

		database = sqlHelper.getWritableDatabase();
	}

	public void close() {

		sqlHelper.close();
	}

	public void insertOrUpdateGHS(ArrayList<GHSDetail> ghsDetailArrayList) {

		for (int i=0; i<ghsDetailArrayList.size(); i++) {

			// Define a projection that specifies which columns from the database
			// you will actually use after this query.
			String[] projection = { GHSDetails.COLUMN_GHS_ID };

			// Filter results WHERE
			String selectionRetrieve = GHSDetails.COLUMN_JOB_ID + " = ?" + " AND " + GHSDetails.COLUMN_GHS_ID + " = ?";
			String[] selectionArgsRetrieve = { ghsDetailArrayList.get(i).getJobID(), String.valueOf(ghsDetailArrayList.get(i).getGhsID())};

			Cursor cursor = database.query(
					GHSDetails.TABLE_NAME,                    // The table to query
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
				values.put(GHSDetails.COLUMN_GHS_ID, ghsDetailArrayList.get(i).getGhsID());

				// Which row to update, based on the title
				String selectionUpdate = GHSDetails.COLUMN_JOB_ID + " = ?";
				String[] selectionArgsUpdate = { ghsDetailArrayList.get(i).getJobID() };

				database.update(GHSDetails.TABLE_NAME, values, selectionUpdate, selectionArgsUpdate);

			} else {

				// Create a new map of values, where column names are the keys
				ContentValues values = new ContentValues();
				values.put(GHSDetails.COLUMN_JOB_ID, ghsDetailArrayList.get(i).getJobID());
				values.put(GHSDetails.COLUMN_GHS_ID, ghsDetailArrayList.get(i).getGhsID());

				// Insert the new row, returning the primary key value of the new row
				database.insert(GHSDetails.TABLE_NAME, null, values);
			}

			cursor.close();
		}
	}

	public ArrayList<GHS> retrieveGHS(String jobID) {

		ArrayList<GHS> ghsArrayList = new ArrayList<>();
		GHS ghs;

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { GHSDetails.COLUMN_GHS_ID };

		// Filter results WHERE
		String selectionRetrieve = GHSDetails.COLUMN_JOB_ID + " = ?";
		String[] selectionArgsRetrieve = { jobID };

		Cursor cursor = database.query(
				GHSDetails.TABLE_NAME,                    // The table to query
				projection,                               // The columns to return
				selectionRetrieve,                        // The columns for the WHERE clause
				selectionArgsRetrieve,                    // The values for the WHERE clause
				null,                                     // Group the rows
				null,                                     // Filter by row groups
				null                                      // The sort order
		);

		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				ghs = new GHS();

				ghs.setGhsPictureURL(cursor.getString(cursor.getColumnIndexOrThrow(GHSDetails.COLUMN_GHS_ID)));

				ghsArrayList.add(ghs);
			}
		}

		return ghsArrayList;
	}
}
