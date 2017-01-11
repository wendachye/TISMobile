package com.bizconnectivity.tismobile.Database.DataSources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.bizconnectivity.tismobile.Classes.CheckIn;
import com.bizconnectivity.tismobile.Constant;
import com.bizconnectivity.tismobile.Database.Contracts.LoadingBayDetailContract.LoadingBayDetails;
import com.bizconnectivity.tismobile.Database.DatabaseSQLHelper;

import java.util.ArrayList;

public class LoadingBayDetailDataSource {

	private SQLiteDatabase database;
	private DatabaseSQLHelper sqlHelper;

	public LoadingBayDetailDataSource(Context context) {

		sqlHelper = new DatabaseSQLHelper(context);
	}

	public void open() {

		database = sqlHelper.getWritableDatabase();
	}

	public void close() {

		sqlHelper.close();
	}

	public void insertLoadingBayNo (CheckIn checkIn) {

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { LoadingBayDetails.COLUMN_LOADING_BAY_NO };

		// Filter results WHERE
		String selectionRetrieve = LoadingBayDetails.COLUMN_LOADING_BAY_NO + " = ?";
		String[] selectionArgsRetrieve = { checkIn.getLoadingBayNo() };

		Cursor cursor = database.query(
				LoadingBayDetails.TABLE_NAME,             // The table to query
				projection,                               // The columns to return
				selectionRetrieve,                        // The columns for the WHERE clause
				selectionArgsRetrieve,                    // The values for the WHERE clause
				null,                                     // Group the rows
				null,                                     // Filter by row groups
				null                                      // The sort order
		);

		if (cursor.getCount() > 0) {


		} else {

			// Create a new map of values, where column names are the keys
			ContentValues values = new ContentValues();
			values.put(LoadingBayDetails.COLUMN_LOADING_BAY_NO, checkIn.getLoadingBayNo());

			// Insert the new row, returning the primary key value of the new row
			database.insert(LoadingBayDetails.TABLE_NAME, null, values);
		}

		cursor.close();
	}

	public String retrieveLoadingBay (CheckIn checkIn) {

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { LoadingBayDetails.COLUMN_LOADING_BAY_NO };

		// Filter results WHERE
		String selection= LoadingBayDetails.COLUMN_LOADING_BAY_NO + " = ?";
		String[] selectionArgs = { checkIn.getLoadingBayNo() };

		Cursor cursor = database.query(
				LoadingBayDetails.TABLE_NAME,             // The table to query
				projection,                               // The columns to return
				selection,                                // The columns for the WHERE clause
				selectionArgs,                            // The values for the WHERE clause
				null,                                     // Group the rows
				null,                                     // Filter by row groups
				null                                      // The sort order
		);

		if (cursor.getCount() > 0) {

			cursor.close();

			return Constant.MSG_CORRECT_LOADING_BAY_NO;

		} else {

			cursor.close();

			return Constant.ERR_MSG_INVALID_TRUCK_BAY;

		}
	}

	public ArrayList<String> retrieveAllLoadingBay () {

		ArrayList<String> loadingBayArrayList = new ArrayList<>();

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { LoadingBayDetails.COLUMN_LOADING_BAY_NO };

		Cursor cursor = database.query(
				LoadingBayDetails.TABLE_NAME,             // The table to query
				projection,                               // The columns to return
				null,                                     // The columns for the WHERE clause
				null,                                     // The values for the WHERE clause
				null,                                     // Group the rows
				null,                                     // Filter by row groups
				null                                      // The sort order
		);

		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				String truckLoadingBay = cursor.getString(cursor.getColumnIndex(LoadingBayDetails.COLUMN_LOADING_BAY_NO));

				loadingBayArrayList.add(truckLoadingBay);

			}

			return loadingBayArrayList;

		} else {

			return loadingBayArrayList;
		}
	}

}