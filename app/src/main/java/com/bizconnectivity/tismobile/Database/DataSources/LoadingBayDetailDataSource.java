package com.bizconnectivity.tismobile.database.datasources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bizconnectivity.tismobile.classes.CheckIn;
import com.bizconnectivity.tismobile.database.contracts.LoadingBayDetailContract.LoadingBayDetails;
import com.bizconnectivity.tismobile.database.DatabaseSQLHelper;

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

	public boolean insertLoadingBayNo (Context context, CheckIn checkIn) {

		boolean returnResult = false;
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

		if (cursor.getCount() == 0) {

			// Create a new map of values, where column names are the keys
			ContentValues values = new ContentValues();
			values.put(LoadingBayDetails.COLUMN_LOADING_BAY_NO, checkIn.getLoadingBayNo());

			// Insert the new row, returning the primary key value of the new row
			database.insert(LoadingBayDetails.TABLE_NAME, null, values);

			returnResult = true;
		}

		cursor.close();

		return returnResult;
	}

	public boolean checkLoadingBayNo(CheckIn checkIn) {

		boolean returnResult = false;

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

			returnResult = true;
		}

		cursor.close();

		return returnResult;
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
		}

		cursor.close();

		return loadingBayArrayList;
	}

	public void deleteAllLoadingBay() {

		database = sqlHelper.getWritableDatabase();

		database.delete(LoadingBayDetails.TABLE_NAME, null, null);

		sqlHelper.close();
	}

	public void deleteSelectedLoadingBay(String loadingBayNo) {

		database = sqlHelper.getWritableDatabase();

		// Define 'where' part of query.
		String selection = LoadingBayDetails.COLUMN_LOADING_BAY_NO + " = ?";
		// Specify arguments in placeholder order.
		String[] selectionArgs = { loadingBayNo };

		database.delete(LoadingBayDetails.TABLE_NAME, selection, selectionArgs);

		sqlHelper.close();
	}

}
