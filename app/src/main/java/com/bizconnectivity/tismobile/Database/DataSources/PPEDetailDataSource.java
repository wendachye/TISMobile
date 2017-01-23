package com.bizconnectivity.tismobile.database.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bizconnectivity.tismobile.classes.PPE;
import com.bizconnectivity.tismobile.classes.PPEDetail;
import com.bizconnectivity.tismobile.database.contracts.PPEDetailContract.PPEDetails;
import com.bizconnectivity.tismobile.database.DatabaseSQLHelper;
import com.bizconnectivity.tismobile.database.contracts.SealDetailContract.SealDetails;

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
			String[] selectionArgsRetrieve = { ppeDetailArrayList.get(i).getJobID(), ppeDetailArrayList.get(i).getPpeID() };

			Cursor cursor = database.query(
					PPEDetails.TABLE_NAME,                    // The table to query
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
				values.put(PPEDetails.COLUMN_PPE_ID, ppeDetailArrayList.get(i).getPpeID());

				// Which row to update, based on the title
				String selectionUpdate = PPEDetails.COLUMN_JOB_ID + " = ?";
				String[] selectionArgsUpdate = { ppeDetailArrayList.get(i).getJobID() };

				database.update(PPEDetails.TABLE_NAME, values, selectionUpdate, selectionArgsUpdate);

			} else {

				// Create a new map of values, where column names are the keys
				ContentValues values = new ContentValues();
				values.put(PPEDetails.COLUMN_JOB_ID, ppeDetailArrayList.get(i).getJobID());
				values.put(PPEDetails.COLUMN_PPE_ID, ppeDetailArrayList.get(i).getPpeID());

				// Insert the new row, returning the primary key value of the new row
				database.insert(PPEDetails.TABLE_NAME, null, values);
			}

			cursor.close();
		}
	}

	public ArrayList<PPE> retrievePPE(String jobID) {

		ArrayList<PPE> ppeArrayList = new ArrayList<>();
		PPE ppe;

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { PPEDetails.COLUMN_PPE_ID };

		// Filter results WHERE
		String selectionRetrieve = PPEDetails.COLUMN_JOB_ID + " = ?";
		String[] selectionArgsRetrieve = { jobID };

		Cursor cursor = database.query(
				PPEDetails.TABLE_NAME,                    // The table to query
				projection,                               // The columns to return
				selectionRetrieve,                        // The columns for the WHERE clause
				selectionArgsRetrieve,                    // The values for the WHERE clause
				null,                                     // Group the rows
				null,                                     // Filter by row groups
				null                                      // The sort order
		);

		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				ppe = new PPE();

				ppe.setPpePictureURL(cursor.getString(cursor.getColumnIndexOrThrow(PPEDetails.COLUMN_PPE_ID)));

				ppeArrayList.add(ppe);
			}
		}

		return ppeArrayList;
	}
}
