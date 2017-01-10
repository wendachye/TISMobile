package com.bizconnectivity.tismobile.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bizconnectivity.tismobile.Database.Contracts.GHSDetailContract.GHSDetails;
import com.bizconnectivity.tismobile.Database.Contracts.JobDetailContract.JobDetails;
import com.bizconnectivity.tismobile.Database.Contracts.LoadingBayDetailContract.LoadingBayDetail;
import com.bizconnectivity.tismobile.Database.Contracts.PPEDetailContract.PPEDetails;
import com.bizconnectivity.tismobile.Database.Contracts.SealDetailContract.SealDetails;
import com.bizconnectivity.tismobile.Database.Contracts.TechnicianDetailContract.TechnicianDetails;
import com.bizconnectivity.tismobile.Database.Contracts.UserDetailContract.UserDetails;

public class DatabaseSQLHelper extends SQLiteOpenHelper{

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "TISMobile.db";

	private static final String SQL_CREATE_USER_DETAILS =
			"CREATE TABLE " + UserDetails.TABLE_NAME + " (" +
					UserDetails.COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
					UserDetails.COLUMN_PASSWORD + " TEXT)";

	private static final String SQL_DELETE_USER_DETAILS =
			"DROP TABLE IF EXISTS " + UserDetails.TABLE_NAME;

	private static final String SQL_CREATE_TECHNICIAN_DETAILS =
			"CREATE TABLE" + TechnicianDetails.TABLE_NAME + " (" +
					TechnicianDetails.COLUMN_TECHNICIAN_ID + " TEXT PRIMARY KEY)";

	private static final String SQL_DELETE_TECHNICIAN_DETAILS =
			"DROP TABLE IF EXISTS " + TechnicianDetails.TABLE_NAME;

	private static final String SQL_CREATE_LOADING_BAY_DETAILS =
			"CREATE TABLE" + LoadingBayDetail.TABLE_NAME + " (" +
					LoadingBayDetail.COLUMN_LOADING_BAY_NO + " TEXT PRIMARY KEY)";

	private static final String SQL_DELETE_LOADING_BAY_DETAILS =
			"DROP TABLE IF EXISTS " + LoadingBayDetail.TABLE_NAME;

	private static final  String SQL_CREATE_JOB_DETAILS =
			"CREATE TABLE" + JobDetails.TABLE_NAME + " (" +
					JobDetails.COLUMN_JOB_ID + " INTEGER PRIMARY KEY," +
					JobDetails.COLUMN_CUSTOMER_NAME + " TEXT," +
					JobDetails.COLUMN_PRODUCT_NAME + " TEXT," +
					JobDetails.COLUMN_TRUCK_LOADING_BAY_NO + " " +
					JobDetails.COLUMN_LOADING_ARM_NO + " " +
					JobDetails.COLUMN_SDS_FILE_PATH + " TEXT," +
					JobDetails.COLUMN_OPERATOR_ID + " " +
					JobDetails.COLUMN_DRIVER_ID + " " +
					JobDetails.COLUMN_WORK_INSTRUCTION + " " +
					JobDetails.COLUMN_PUMP_START_TIME + " TEXT," +
					JobDetails.COLUMN_PUMP_STOP_TIME + " TEXT," +
					JobDetails.COLUMN_RACK_OUT_TIME + " TEXT" +
					JobDetails.COLUMN_PPE_STATUS + " TEXT" +
					JobDetails.COLUMN_SDS_STATUS + " TEXT" +
					JobDetails.COLUMN_SCAN_DETAILS_STATUS + " TEXT" +
					JobDetails.COLUMN_SAFETY_CHECKS_STATUS + " TEXT" +
					JobDetails.COLUMN_SCAN_LOADING_ARM_STATUS + " " +
					JobDetails.COLUMN_BATCH_CONTROLLER_STATUS + " TEXT" +
					JobDetails.COLUMN_SCAN_SEAL_STATUS + " )";

	private static final String SQL_DELETE_JOB_DETAILS =
			"DROP TABLE IF EXISTS " + JobDetails.TABLE_NAME;

	private static final String SQL_CREATE_SEAL_DETAILS =
			"CREATE TABLE" + SealDetails.TABLE_NAME + " (" +
					SealDetails.COLUMN_JOB_ID + " INTEGER PRIMARY KEY," +
					SealDetails.COLUMN_SEAL_NO + " TEXT)";

	private static final String SQL_DELETE_SEAL_DETAILS =
			"DROP TABLE IF EXISTS " + SealDetails.TABLE_NAME;

	private static final String SQL_CREATE_PPE_DETAILS =
			"CREATE TABLE" + PPEDetails.TABLE_NAME + " (" +
					PPEDetails.COLUMN_JOB_ID + " INTEGER PRIMARY KEY," +
					PPEDetails.COLUMN_PPE_IMAGE_NAME + " TEXT)";

	private static final String SQL_DELETE_PPE_DETAILS =
			"DROP TABLE IF EXISTS " + PPEDetails.TABLE_NAME;

	private static final String SQL_CREATE_GHS_DETAILS =
			"CREATE TABLE" + GHSDetails.TABLE_NAME + " (" +
					GHSDetails.COLUMN_JOB_ID + " INTEGER PRIMARY KEY," +
					GHSDetails.COLUMN_GHS_IMAGE_NAME + " TEXT)";

	private static final String SQL_DELETE_GHS_DETAILS =
			"DROP TABLE IF EXISTS " + GHSDetails.TABLE_NAME;


	public DatabaseSQLHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(SQL_CREATE_USER_DETAILS);
		db.execSQL(SQL_CREATE_TECHNICIAN_DETAILS);
		db.execSQL(SQL_CREATE_LOADING_BAY_DETAILS);
//		db.execSQL(SQL_CREATE_JOB_DETAILS);
//		db.execSQL(SQL_CREATE_SEAL_DETAILS);
//		db.execSQL(SQL_CREATE_PPE_DETAILS);
//		db.execSQL(SQL_CREATE_GHS_DETAILS);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		// This database is only a cache for online data, so its upgrade policy is
		// to simply to discard the data and start over
		db.execSQL(SQL_DELETE_USER_DETAILS);
		db.execSQL(SQL_DELETE_TECHNICIAN_DETAILS);
		db.execSQL(SQL_DELETE_LOADING_BAY_DETAILS);
		db.execSQL(SQL_DELETE_JOB_DETAILS);
		db.execSQL(SQL_DELETE_SEAL_DETAILS);
		db.execSQL(SQL_DELETE_PPE_DETAILS);
		db.execSQL(SQL_DELETE_GHS_DETAILS);

	}
}
