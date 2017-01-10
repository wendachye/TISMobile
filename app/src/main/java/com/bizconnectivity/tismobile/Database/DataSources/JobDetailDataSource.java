package com.bizconnectivity.tismobile.Database.DataSources;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bizconnectivity.tismobile.Classes.JobDetail;
import com.bizconnectivity.tismobile.Database.Contracts.JobDetailContract.JobDetails;
import com.bizconnectivity.tismobile.Database.DatabaseSQLHelper;

public class JobDetailDataSource {

	private SQLiteDatabase database;
	private DatabaseSQLHelper sqlHelper;

	public JobDetailDataSource(Context context) {

		sqlHelper = new DatabaseSQLHelper(context);
	}

	public void open() {

		database = sqlHelper.getWritableDatabase();
	}

	public void close() {

		sqlHelper.close();
	}

	public void insertOrUpdateJobDetails(JobDetail jobDetail) {

		// Define a projection that specifies which columns from the database
		// you will actually use after this query.
		String[] projection = { JobDetails.COLUMN_JOB_ID };

		// Filter results WHERE
		String selectionRetrieve = JobDetails.COLUMN_JOB_ID + " = ?";
		String[] selectionArgsRetrieve = { jobDetail.getJobID() };

		Cursor cursor = database.query(
				JobDetails.TABLE_NAME,                    // The table to query
				projection,                               // The columns to return
				selectionRetrieve,                        // The columns for the WHERE clause
				selectionArgsRetrieve,                    // The values for the WHERE clause
				null,                                     // Group the rows
				null,                                     // Filter by row groups
				null                                      // The sort order
		);

		if (cursor.moveToFirst()) {

			// New value for one column
			ContentValues values = new ContentValues();
			values.put(JobDetails.COLUMN_CUSTOMER_NAME, jobDetail.getCustomerName());
			values.put(JobDetails.COLUMN_PRODUCT_NAME, jobDetail.getProductName());
			values.put(JobDetails.COLUMN_TANK_NO, jobDetail.getTankNo());
			values.put(JobDetails.COLUMN_TRUCK_LOADING_BAY_NO, jobDetail.getLoadingBayNo());
			values.put(JobDetails.COLUMN_LOADING_ARM_NO, jobDetail.getLoadingArm());
			values.put(JobDetails.COLUMN_SDS_FILE_PATH, jobDetail.getSdsFile());
			values.put(JobDetails.COLUMN_OPERATOR_ID, jobDetail.getOperatorID());
			values.put(JobDetails.COLUMN_DRIVER_ID, jobDetail.getDriverID());

			// Which row to update, based on the title
			String selectionUpdate = JobDetails.COLUMN_JOB_ID + " = ?";
			String[] selectionArgsUpdate = { jobDetail.getJobID() };

			database.update(JobDetails.TABLE_NAME, values, selectionUpdate, selectionArgsUpdate);

		} else {

			// Create a new map of values, where column names are the keys
			ContentValues values = new ContentValues();
			values.put(JobDetails.COLUMN_JOB_ID, jobDetail.getJobID());
			values.put(JobDetails.COLUMN_CUSTOMER_NAME, jobDetail.getCustomerName());
			values.put(JobDetails.COLUMN_PRODUCT_NAME, jobDetail.getProductName());
			values.put(JobDetails.COLUMN_TANK_NO, jobDetail.getTankNo());
			values.put(JobDetails.COLUMN_TRUCK_LOADING_BAY_NO, jobDetail.getLoadingBayNo());
			values.put(JobDetails.COLUMN_LOADING_ARM_NO, jobDetail.getLoadingArm());
			values.put(JobDetails.COLUMN_SDS_FILE_PATH, jobDetail.getSdsFile());
			values.put(JobDetails.COLUMN_OPERATOR_ID, jobDetail.getOperatorID());
			values.put(JobDetails.COLUMN_DRIVER_ID, jobDetail.getDriverID());

			// Insert the new row, returning the primary key value of the new row
			database.insert(JobDetails.TABLE_NAME, null, values);
		}
	}

}
