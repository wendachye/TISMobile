package com.bizconnectivity.tismobile.database.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bizconnectivity.tismobile.classes.PPEDetail;
import com.bizconnectivity.tismobile.database.contracts.PPEDetailContract.PPEDetails;
import com.bizconnectivity.tismobile.database.DatabaseSQLHelper;

import java.util.ArrayList;

public class PPEDetailDataSource {

	private SQLiteDatabase database;
	private DatabaseSQLHelper sqlHelper;

	public PPEDetailDataSource(Context context) {

		sqlHelper = new DatabaseSQLHelper(context);
	}

	public void open() {

		database = sqlHelper.getWritableDatabase();
	}

	public void close() {

		sqlHelper.close();
	}

	public void insertOrUpdatePPE(ArrayList<PPEDetail> ppeDetailArrayList) {

		for (int i=0; i<ppeDetailArrayList.size(); i++) {

			// Define a projection that specifies which columns from the database
			// you will actually use after this query.
			String[] projection = { PPEDetails.COLUMN_PPE_ID };

			// Filter results WHERE
			String selectionRetrieve = PPEDetails.COLUMN_JOB_ID + " = ?" + " AND " + PPEDetails.COLUMN_PPE_ID + " = ?";
			String[] selectionArgsRetrieve = { ppeDetailArrayList.get(i).getJobID(), String.valueOf(ppeDetailArrayList.get(i).getPpeID())};

			Cursor cursor = database.query(
					PPEDetails.TABLE_NAME,                    // The table to query
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
				values.put(PPEDetails.COLUMN_JOB_ID, ppeDetailArrayList.get(i).getJobID());
				values.put(PPEDetails.COLUMN_PPE_ID, ppeDetailArrayList.get(i).getPpeID());

				// Insert the new row, returning the primary key value of the new row
				database.insert(PPEDetails.TABLE_NAME, null, values);
			}

		}

	}
}
