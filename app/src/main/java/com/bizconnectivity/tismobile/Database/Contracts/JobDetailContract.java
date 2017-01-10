package com.bizconnectivity.tismobile.Database.Contracts;

import android.provider.BaseColumns;

public class JobDetailContract implements BaseColumns{

	// To prevent someone from accidentally instantiating the contract class,
	// make the constructor private.
	private JobDetailContract() {

	}

	/* Inner class that defines the table contents */
	public static class JobDetails implements BaseColumns {

		public static final String TABLE_NAME = "JobDetails";
		public static final String COLUMN_JOB_ID = "JobID";
		public static final String COLUMN_CUSTOMER_NAME = "CustomerName";
		public static final String COLUMN_PRODUCT_NAME = "ProductName";
		public static final String COLUMN_TRUCK_LOADING_BAY_NO = "TruckLoadingBayNo";
		public static final String COLUMN_LOADING_ARM_NO = "LoadingArmNo";
		public static final String COLUMN_SDS_FILE_PATH = "SDSFilePath";
		public static final String COLUMN_OPERATOR_ID = "OperatorID";
		public static final String COLUMN_DRIVER_ID = "DriverID";
		public static final String COLUMN_WORK_INSTRUCTION = "WorkInstruction";
		public static final String COLUMN_PUMP_START_TIME = "PumpStartTime";
		public static final String COLUMN_PUMP_STOP_TIME = "PumpStopTime";
		public static final String COLUMN_RACK_OUT_TIME = "RackOutTime";

		public static final String COLUMN_PPE_STATUS = "PPEStatus";
		public static final String COLUMN_SDS_STATUS = "SDSStatus";
		public static final String COLUMN_SCAN_DETAILS_STATUS = "ScanDetailsStatus";
		public static final String COLUMN_SAFETY_CHECKS_STATUS = "SafetyChecksStatus";
		public static final String COLUMN_SCAN_LOADING_ARM_STATUS = "ScanLoadingArmStatus";
		public static final String COLUMN_BATCH_CONTROLLER_STATUS = "BatchControllerStatus";
		public static final String COLUMN_SCAN_SEAL_STATUS = "ScanSealStatus";

	}
}
