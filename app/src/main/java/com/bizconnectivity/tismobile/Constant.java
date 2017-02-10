package com.bizconnectivity.tismobile;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constant {
    public static String KEY_ENCRYPT = "TISMOBILE";
    public static String SHARED_PREF_NAME = "SP_NAME";
    public static String SHARED_PREF_LOGIN_NAME = "SP_LOGIN_NAME";
    public static String SHARED_PREF_SCAN_VALUE = "SP_SCAN_VALUE";
    public static String SHARED_PREF_TECHNICIAN_ID = "SP_TECHNICIAN_ID";

    /*--------Job Details--------*/

    //Shared Preferences of selected job
    public static String SHARED_PREF_JOB_ID = "SP_JOB_ID";
    public static String SHARED_PREF_CUSTOMER_NAME = "SP_CUSTOMER_NAME";
    public static String SHARED_PREF_PRODUCT_NAME = "SP_PRODUCT_NAME";
    public static String SHARED_PREF_TANK_NO = "SP_TANK_NO";
    public static String SHARED_PREF_LOADING_BAY = "SP_LOADING_BAY";
    public static String SHARED_PREF_LOADING_ARM = "SP_LOADING_ARM";
    public static String SHARED_PREF_SDS_FILE_PATH = "SP_SDS_FILE_PATH";
    public static String SHARED_PREF_OPERATOR_ID = "SP_OPERATOR_ID";
    public static String SHARED_PREF_DRIVER_ID = "SP_DRIVER_ID";
    public static String SHARED_PREF_WORK_INSTRUCTION = "SP_WORK_INSTRUCTION";
    public static String SHARED_PREF_PUMP_START_TIME = "SP_PUMP_START_TIME";
    public static String SHARED_PREF_PUMP_STOP_TIME = "SP_PUMP_STOP_TIME";
    public static String SHARED_PREF_RACK_OUT_TIME = "SP_RACK_OUT_TIME";
    public static String SHARED_PREF_JOB_STATUS = "SP_JOB_STATUS";
    public static String SHARED_PREF_JOB_DATE = "SP_JOB_DATE";
    public static String SHARED_PREF_BATCH_CONTROLLER = "SP_BATCH_CONTROLLER";
    public static String SHARED_PREF_BATCH_CONTROLLER_LITRE = "SP_BATCH_CONTROLLER_LITRE";

    //Job Status
    public static final String STATUS_PENDING = "Pending";
    public static final String STATUS_PPE = "PPE Completed";
    public static final String STATUS_SDS = "SDS Completed ";
    public static final String STATUS_OPERATOR_ID = "Operator ID Completed";
    public static final String STATUS_DRIVER_ID = "Driver ID Completed";
    public static final String STATUS_WORK_INSTRUCTION = "Work Instruction Completed";
    public static final String STATUS_SAFETY_CHECKS = "Safety Checks Completed";
    public static final String STATUS_SCAN_LOADING_ARM = "Scan Loading Bay Completed";
    public static final String STATUS_BATCH_CONTROLLER = "Batch Controller Completed";
    public static final String STATUS_PUMP_START = "Pump Start Completed";
    public static final String STATUS_PUMP_STOP = "Pump Stop Completed";
    public static final String STATUS_SCAN_SEAL = "Scan Seal Completed";

    /*--------Testing Login User--------*/
    public static String TEST_USERNAME = "test";
    public static String TEST_PASSWORD = "test";

    /*--------Error Messages--------*/

    //Login
    public static String ERR_MSG_LOGIN_INCORRECT = "Incorrect Username or Password.";
    public static String ERR_MSG_USERNAME_REQUIRED = "Username is required.";
    public static String ERR_MSG_PASSWORD_REQUIRED = "Password is required.";

    //CheckIn
    public static String ERR_MSG_INVALID_TECHNICIAN_NRIC = "Invalid Technician NRIC";
    public static String ERR_MSG_TRUCK_BAY_ALREADY_CHECKED_IN = "Truck loading bay was already checked in before.";
    public static String ERR_MSG_INVALID_TRUCK_BAY = "Invalid Truck Loading Bay.";

    //Check Out
    public static String ERR_MSG_NO_TRUCK_BAY_CHECKED_IN = "You have not check-in to any Truck Loading Bay.";
    public static String ERR_MSG_INVALID_LOADING_ARM = "Invalid Truck Rack Arm.";
    public static String ERR_MSG_INVALID_SEAL_NO = "Invalid Seal Number.";
    public static String ERR_MSG_CANNOT_ADD_SEAL = "This seal already added.";
    public static String ERR_MSG_SEAL_CANNOT_ADD = "Can not add seal.";


    public static String MSG_ORDER_ID_REQUIRED = "Order ID is required";
    public static String MSG_CUSTOMER_NAME_REQUIRED = "Customer Name is required";
    public static String MSG_ORDER_ID_NOT_FOUND = "Order ID not found.";
    public static String MSG_CUSTOMER_NAME_NOT_FOUND = "Customer Name not found.";
    public static String ERR_MSG_CHECK_PPE = "Please check the Product PPE.";

    /*--------Text Messages--------*/
    public static String LOADING_BAY_NO_CHECK_IN = "Check In";
    public static String LOADING_BAY_NO_CHECK_OUT = "Check Out";
    public static String TRUCK_BAY_CHECKED_OUT = " have been check-out.";
    public static String NO_PPE = "NO PPE";
    public static String NO_PPE_MESSAGE = "This product don't have PPE. Please contact the Safety Department.";
    public static String NO_SDS = "NO SDS";
    public static String NO_SDS_MESSAGE = "This product don't have SDS. Please contact the Safety Department.";
    public static String OK = "OK";
    public static String SAFETY_CHECKS_MESSAGE = "Please do the Safety Checks.";
    public static String BATCH_CONTROLLER_MESSAGE = "Please fill in the Batch Controller Input.";
    public static String DEPARTURE_MESSAGE = "Data stored in local due to no internet connection, please sync the data after the internet available.";
    public static String ERR_MSG_DEPARTURE = "Fail to departure, please try again.";
    public static String NO_DATA_SYNC = "No data to sync.";
    public static String NO_INTERNET = "No Internet Connection.";
    public static String SUCCESS_SYNC = "Data has been synced.";

    /*--------Barcode Scanner--------*/
    public static String SCAN_MSG_CANCEL_SCANNING = "Scanning is cancelled";
    public static String SCAN_MSG_NO_DATA_RECEIVED = "No scan data received!";
    public static String SCAN_MSG_INVALID_DATA_RECEIVED = "Invalid scan data received!";
    public static String SCAN_MSG_INVALID_DRIVER_ID_RECEIVED = "Invalid Driver ID received!";
    public static String SCAN_MSG_INVALID_WORK_INSTRUCTION_RECEIVED = "Invalid Work Instruction received!";

    public static String SCAN_VALUE_TECHNICIAN_ID = "SCAN_VALUE_TECHNICIAN_ID";
    public static String SCAN_VALUE_OPERATOR_ID = "SCAN_VALUE_OPERATOR_ID";
    public static String SCAN_VALUE_TRUCK_LOADING_BAY = "SCAN_VALUE_TRUCK_LOADING_BAY";
    public static String SCAN_VALUE_DRIVER_ID = "SCAN_VALUE_DRIVER_ID";
    public static String SCAN_VALUE_WORK_INSTRUCTION = "SCAN_VALUE_WORK_INSTRUCTION";
    public static String SCAN_VALUE_LOADING_ARM = "SCAN_VALUE_LOADING_ARM";

    public static String SCAN_VALUE_BOTTOM_SEAL1 = "SCAN_VALUE_BOTTOM_SEAL1";
    public static String SCAN_VALUE_BOTTOM_SEAL2 = "SCAN_VALUE_BOTTOM_SEAL2";
    public static String SCAN_VALUE_BOTTOM_SEAL3 = "SCAN_VALUE_BOTTOM_SEAL3";
    public static String SCAN_VALUE_BOTTOM_SEAL4 = "SCAN_VALUE_BOTTOM_SEAL4";

    public static String SCAN_MSG_PROMPT_TECHNICIAN_ID = "Please scan NRIC of Technician";
    public static String SCAN_MSG_PROMPT_OPERATOR_ID = "Please scan NRIC of Operator";
    public static String SCAN_MSG_PROMPT_TRUCK_LOADING_BAY = "Please scan Truck Loading Bay";
    public static String SCAN_MSG_PROMPT_DRIVER_ID = "Please scan Driver ID of Technician";
    public static String SCAN_MSG_PROMPT_WORK_INSTRUCTION = "Please scan Work Instruction";
    public static String SCAN_MSG_PROMPT_SCAN_LOADING_ARM = "Please scan Loading Arm";
    public static String SCAN_MSG_PROMPT_SCAN_BOTTOM_SEAL = "Please scan Bottom Seal";

    /*--------Date Format--------*/
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    public static SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("HH:mm", Locale.US);
    public static SimpleDateFormat simpleDateFormat3 = new SimpleDateFormat("HH:mm:ss", Locale.US);
}
