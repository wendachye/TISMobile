package com.bizconnectivity.tismobile.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bizconnectivity.tismobile.database.contracts.GHSDetailContract.GHSDetails;
import com.bizconnectivity.tismobile.database.contracts.JobDetailContract.JobDetails;
import com.bizconnectivity.tismobile.database.contracts.LoadingBayDetailContract.LoadingBayDetails;
import com.bizconnectivity.tismobile.database.contracts.PPEContract.PPE;
import com.bizconnectivity.tismobile.database.contracts.GHSContract.GHS;
import com.bizconnectivity.tismobile.database.contracts.PPEDetailContract.PPEDetails;
import com.bizconnectivity.tismobile.database.contracts.SealDetailContract.SealDetails;
import com.bizconnectivity.tismobile.database.contracts.TechnicianDetailContract.TechnicianDetails;
import com.bizconnectivity.tismobile.database.contracts.UserDetailContract.UserDetails;

public class DatabaseSQLHelper extends SQLiteOpenHelper{

	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "TISMobile.db";

	private static final String SQL_CREATE_USER_DETAILS =
			"CREATE TABLE " +
					UserDetails.TABLE_NAME + " (" +
					UserDetails.COLUMN_USERNAME + " TEXT PRIMARY KEY, " +
					UserDetails.COLUMN_PASSWORD + " TEXT)";

	private static final String SQL_DELETE_USER_DETAILS =
			"DROP TABLE IF EXISTS " +
					UserDetails.TABLE_NAME;

	private static final String SQL_CREATE_TECHNICIAN_DETAILS =
			"CREATE TABLE " +
					TechnicianDetails.TABLE_NAME + " (" +
					TechnicianDetails.COLUMN_TECHNICIAN_ID + " TEXT PRIMARY KEY)";

	private static final String SQL_DELETE_TECHNICIAN_DETAILS =
			"DROP TABLE IF EXISTS " +
					TechnicianDetails.TABLE_NAME;

	private static final String SQL_CREATE_LOADING_BAY_DETAILS =
			"CREATE TABLE " +
					LoadingBayDetails.TABLE_NAME + " (" +
					LoadingBayDetails.COLUMN_LOADING_BAY_NO + " TEXT PRIMARY KEY)";

	private static final String SQL_DELETE_LOADING_BAY_DETAILS =
			"DROP TABLE IF EXISTS " +
					LoadingBayDetails.TABLE_NAME;

	private static final  String SQL_CREATE_JOB_DETAILS =
			"CREATE TABLE " +
					JobDetails.TABLE_NAME + " (" +
					JobDetails.COLUMN_JOB_ID + " TEXT PRIMARY KEY, " +
					JobDetails.COLUMN_CUSTOMER_NAME + " TEXT, " +
					JobDetails.COLUMN_PRODUCT_NAME + " TEXT, " +
					JobDetails.COLUMN_TANK_NO + " TEXT, " +
					JobDetails.COLUMN_TRUCK_LOADING_BAY_NO + " TEXT, " +
					JobDetails.COLUMN_LOADING_ARM_NO + " TEXT, " +
					JobDetails.COLUMN_SDS_FILE_PATH + " TEXT, " +
					JobDetails.COLUMN_OPERATOR_ID + " TEXT, " +
					JobDetails.COLUMN_DRIVER_ID + " TEXT, " +
					JobDetails.COLUMN_WORK_INSTRUCTION + " TEXT, " +
					JobDetails.COLUMN_PUMP_START_TIME + " TEXT, " +
					JobDetails.COLUMN_PUMP_STOP_TIME + " TEXT, " +
					JobDetails.COLUMN_RACK_OUT_TIME + " TEXT, " +
					JobDetails.COLUMN_JOB_STATUS + " TEXT NOT NULL DEFAULT 'Pending', " +
					JobDetails.COLUMN_JOB_DATE + " TEXT)";

	private static final String SQL_DELETE_JOB_DETAILS =
			"DROP TABLE IF EXISTS " +
					JobDetails.TABLE_NAME;

	private static final String SQL_CREATE_SEAL_DETAILS =
			"CREATE TABLE " +
					SealDetails.TABLE_NAME + " (" +
					SealDetails.COLUMN_JOB_ID + " TEXT, " +
					SealDetails.COLUMN_SEAL_NO + " TEXT)";

	private static final String SQL_DELETE_SEAL_DETAILS =
			"DROP TABLE IF EXISTS " +
					SealDetails.TABLE_NAME;

	private static final String SQL_CREATE_PPE_DETAILS =
			"CREATE TABLE " +
					PPEDetails.TABLE_NAME + " (" +
					PPEDetails.COLUMN_JOB_ID + " TEXT, " +
					PPEDetails.COLUMN_PPE_ID + " TEXT)";

	private static final String SQL_DELETE_PPE_DETAILS =
			"DROP TABLE IF EXISTS " +
					PPEDetails.TABLE_NAME;

	private static final String SQL_CREATE_GHS_DETAILS =
			"CREATE TABLE " +
					GHSDetails.TABLE_NAME + " (" +
					GHSDetails.COLUMN_JOB_ID + " TEXT, " +
					GHSDetails.COLUMN_GHS_ID + " TEXT)";

	private static final String SQL_DELETE_GHS_DETAILS =
			"DROP TABLE IF EXISTS " +
					GHSDetails.TABLE_NAME;

	private static final String SQL_CREATE_PPE =
			"CREATE TABLE " +
					PPE.TABLE_NAME + " (" +
					PPE.COLUMN_PPE_ID + " TEXT, " +
					PPE.COLUMN_PPE_NAME + " TEXT)";

	private static final String SQL_CREATE_GHS =
			"CREATE TABLE " +
					GHS.TABLE_NAME + " (" +
					GHS.COLUMN_GHS_ID + " TEXT, " +
					GHS.COLUMN_GHS_NAME + " TEXT)";

	//region insert ppe query

	private static final String SQL_INSERT_PPE1 =
			"INSERT INTO " +
					PPE.TABLE_NAME + " (" +
					PPE.COLUMN_PPE_ID + ", " +
					PPE.COLUMN_PPE_NAME + ") VALUES " +
					"('1', 'ear_protection')";

	private static final String SQL_INSERT_PPE2 =
			"INSERT INTO " +
					PPE.TABLE_NAME + " (" +
					PPE.COLUMN_PPE_ID + ", " +
					PPE.COLUMN_PPE_NAME + ") VALUES " +
					"('2', 'face_shield')";

	private static final String SQL_INSERT_PPE3 =
			"INSERT INTO " +
					PPE.TABLE_NAME + " (" +
					PPE.COLUMN_PPE_ID + ", " +
					PPE.COLUMN_PPE_NAME + ") VALUES " +
					"('3', 'foot_protection')";

	private static final String SQL_INSERT_PPE4 =
			"INSERT INTO " +
					PPE.TABLE_NAME + " (" +
					PPE.COLUMN_PPE_ID + ", " +
					PPE.COLUMN_PPE_NAME + ") VALUES " +
					"('4', 'hand_protection')";

	private static final String SQL_INSERT_PPE5 =
			"INSERT INTO " +
					PPE.TABLE_NAME + " (" +
					PPE.COLUMN_PPE_ID + ", " +
					PPE.COLUMN_PPE_NAME + ") VALUES " +
					"('5', 'head_protection')";

	private static final String SQL_INSERT_PPE6 =
			"INSERT INTO " +
					PPE.TABLE_NAME + " (" +
					PPE.COLUMN_PPE_ID + ", " +
					PPE.COLUMN_PPE_NAME + ") VALUES " +
					"('6', 'mandatory_instruction')";

	private static final String SQL_INSERT_PPE7 =
			"INSERT INTO " +
					PPE.TABLE_NAME + " (" +
					PPE.COLUMN_PPE_ID + ", " +
					PPE.COLUMN_PPE_NAME + ") VALUES " +
					"('7', 'pedestrian_route')";

	private static final String SQL_INSERT_PPE8 =
			"INSERT INTO " +
					PPE.TABLE_NAME + " (" +
					PPE.COLUMN_PPE_ID + ", " +
					PPE.COLUMN_PPE_NAME + ") VALUES " +
					"('8', 'protective_clothing')";

	private static final String SQL_INSERT_PPE9 =
			"INSERT INTO " +
					PPE.TABLE_NAME + " (" +
					PPE.COLUMN_PPE_ID + ", " +
					PPE.COLUMN_PPE_NAME + ") VALUES " +
					"('9', 'respirator')";

	private static final String SQL_INSERT_PPE10 =
			"INSERT INTO " +
					PPE.TABLE_NAME + " (" +
					PPE.COLUMN_PPE_ID + ", " +
					PPE.COLUMN_PPE_NAME + ") VALUES " +
					"('10', 'safety_glasses')";

	private static final String SQL_INSERT_PPE11 =
			"INSERT INTO " +
					PPE.TABLE_NAME + " (" +
					PPE.COLUMN_PPE_ID + ", " +
					PPE.COLUMN_PPE_NAME + ") VALUES " +
					"('11', 'safety_harness')";

	//endregion

	//region insert ghs query

	private static final String SQL_INSERT_GHS1 =
			"INSERT INTO " +
					GHS.TABLE_NAME + " (" +
					GHS.COLUMN_GHS_ID + ", " +
					GHS.COLUMN_GHS_NAME + ") VALUES " +
					"('1', 'acute_toxicity')";

	private static final String SQL_INSERT_GHS2 =
			"INSERT INTO " +
					GHS.TABLE_NAME + " (" +
					GHS.COLUMN_GHS_ID + ", " +
					GHS.COLUMN_GHS_NAME + ") VALUES " +
					"('2', 'aspiration_toxicity')";

	private static final String SQL_INSERT_GHS3 =
			"INSERT INTO " +
					GHS.TABLE_NAME + " (" +
					GHS.COLUMN_GHS_ID + ", " +
					GHS.COLUMN_GHS_NAME + ") VALUES " +
					"('3', 'corrosive')";

	private static final String SQL_INSERT_GHS4 =
			"INSERT INTO " +
					GHS.TABLE_NAME + " (" +
					GHS.COLUMN_GHS_ID + ", " +
					GHS.COLUMN_GHS_NAME + ") VALUES " +
					"('4', 'environment_toxicity')";

	private static final String SQL_INSERT_GHS5 =
			"INSERT INTO " +
					GHS.TABLE_NAME + " (" +
					GHS.COLUMN_GHS_ID + ", " +
					GHS.COLUMN_GHS_NAME + ") VALUES " +
					"('5', 'explosive')";

	private static final String SQL_INSERT_GHS6 =
			"INSERT INTO " +
					GHS.TABLE_NAME + " (" +
					GHS.COLUMN_GHS_ID + ", " +
					GHS.COLUMN_GHS_NAME + ") VALUES " +
					"('6', 'flammable')";

	private static final String SQL_INSERT_GHS7 =
			"INSERT INTO " +
					GHS.TABLE_NAME + " (" +
					GHS.COLUMN_GHS_ID + ", " +
					GHS.COLUMN_GHS_NAME + ") VALUES " +
					"('7', 'gases_under_pressure')";

	private static final String SQL_INSERT_GHS8 =
			"INSERT INTO " +
					GHS.TABLE_NAME + " (" +
					GHS.COLUMN_GHS_ID + ", " +
					GHS.COLUMN_GHS_NAME + ") VALUES " +
					"('8', 'irritant')";

	private static final String SQL_INSERT_GHS9 =
			"INSERT INTO " +
					GHS.TABLE_NAME + " (" +
					GHS.COLUMN_GHS_ID + ", " +
					GHS.COLUMN_GHS_NAME + ") VALUES " +
					"('9', 'oxidiser')";

	//endregion

	public DatabaseSQLHelper(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(SQL_CREATE_USER_DETAILS);
		db.execSQL(SQL_CREATE_TECHNICIAN_DETAILS);
		db.execSQL(SQL_CREATE_LOADING_BAY_DETAILS);
		db.execSQL(SQL_CREATE_JOB_DETAILS);
		db.execSQL(SQL_CREATE_SEAL_DETAILS);
		db.execSQL(SQL_CREATE_PPE_DETAILS);
		db.execSQL(SQL_CREATE_GHS_DETAILS);

		db.execSQL(SQL_CREATE_PPE);
		db.execSQL(SQL_CREATE_GHS);

		//region insert ppe

		db.execSQL(SQL_INSERT_PPE1);
		db.execSQL(SQL_INSERT_PPE2);
		db.execSQL(SQL_INSERT_PPE3);
		db.execSQL(SQL_INSERT_PPE4);
		db.execSQL(SQL_INSERT_PPE5);
		db.execSQL(SQL_INSERT_PPE6);
		db.execSQL(SQL_INSERT_PPE7);
		db.execSQL(SQL_INSERT_PPE8);
		db.execSQL(SQL_INSERT_PPE9);
		db.execSQL(SQL_INSERT_PPE10);
		db.execSQL(SQL_INSERT_PPE11);

		//endregion
		//region insert ghs

		db.execSQL(SQL_INSERT_GHS1);
		db.execSQL(SQL_INSERT_GHS2);
		db.execSQL(SQL_INSERT_GHS3);
		db.execSQL(SQL_INSERT_GHS4);
		db.execSQL(SQL_INSERT_GHS5);
		db.execSQL(SQL_INSERT_GHS6);
		db.execSQL(SQL_INSERT_GHS7);
		db.execSQL(SQL_INSERT_GHS8);
		db.execSQL(SQL_INSERT_GHS9);

		//endregion

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
		onCreate(db);
	}
}
