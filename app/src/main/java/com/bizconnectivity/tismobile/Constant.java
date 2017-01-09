package com.bizconnectivity.tismobile;

public class Constant {
    public static String SHARED_PREF_NAME = "SP_NAME";
    public static String SHARED_PREF_LOGINNAME = "SP_LOGINNAME";
    public static String SHARED_PREF_SCAN_VALUE = "SP_SCAN_VALUE";
    public static String SHARED_PREF_TECHNICIAN_ID = "SP_TECHNICIAN_ID";
    public static String SHARED_PREF_TRUCK_LOADING_BAY = "SP_TRUCK_LOADING_BAY";
    public static String SHARED_PREF_OPERATOR_ID = "SP_OPERATOR_ID";
    public static String SHARED_PREF_DRIVER_ID = "SP_DRIVER_ID";
    public static String SHARED_PREF_WORK_INSTRUCTION = "SP_WORK_INSTRUCTION";
    public static String SHARED_PREF_LOADING_ARM = "SP_LOADING_ARM";
    public static String SHARED_PREF_ORDER_ID_SELECTED = "SP_ORDER_ID_SELECTED";
    public static String SHARED_PREF_ARM_NO_SELECTED = "SP_ARM_NO_SELECTED";
    public static String SHARED_PREF_PRODUCT_NAME_SELECTED = "SP_PRODUCT_NAME_SELECTED";
    public static String SHARED_PREF_PPE = "SP_PPE";
    public static String SHARED_PREF_PPE_PICTURE_URL = "SP_PPE_PICTURE_URL";
    public static String SHARED_PREF_SDS = "SP_SDS";
    public static String SHARED_PREF_SDS_PDF_URL = "SP_SDS_PDF_URL";
    public static String SHARED_PREF_SCAN_DETAILS = "SP_SCAN_DETAILS";
    public static String SHARED_PREF_SAFETY_CHECKS = "SP_SAFETY_CHECKS";
    public static String SHARED_PREF_BATCH_CONTROLLER_L = "SP_BATCH_CONTROLLER_T";
	public static String SHARED_PREF_BATCH_CONTROLLER_MT = "SP_BATCH_CONTROLLER_MT";
    public static String SHARED_PREF_PUMP_START = "SP_PUMP_START";
    public static String SHARED_PREF_PUMP_STOP = "SP_PUMP_STOP";
    public static String SHARED_PREF_SCAN_SEAL = "SP_SCAN_SEAL";
	public static String SHARED_PREF_ADD_SEAL_COUNT = "SP_ADD_SEAL_COUNT";

	/*--------BCPL Development Server--------*/
    public static String SDS_FILE_LOCATION = "http://192.168.2.91/tis/Core/MSDS Files/";
    public static String GHS_FILE_LOCATION = "http://192.168.2.91/tis/Images/GHS/";
    public static String PPE_FILE_LOCATION = "http://192.168.2.91/tis/Images/PPE/";
    public static boolean DEBUG = true;

    /*--------Live Server--------*/
    /*public static String SDS_FILE_LOCATION = "http://10.129.114.48/Penjuru_UAT/Core/MSDS Files/";
    public static String GHS_FILE_LOCATION = "http://10.129.114.48/Penjuru_UAT/Images/GHS/";
    public static String PPE_FILE_LOCATION = "http://10.129.114.48/Penjuru_UAT/Images/PPE/";*/

    /*--------Login--------*/
    public static String TEST_USERNAME = "test";
    public static String TEST_PASSWORD = "test";
    public static String LOGIN_LOGINNAME = "X";

    /*--------Job Order--------*/
    public static String LOADING_BAY = "LOADING BAY ";

    public static String ORDER_ID = "ORDER_ID";
    public static String TIMESLOT_ID = "TIMESLOT_ID";
    public static String CUSTOMER_NAME = "CUSTOMER_NAME";
    public static String BAY_NAME = "BAY_NAME";
    public static String ARM_NAME = "ARM_NAME";
    public static String PRODUCT_NAME = "PRODUCT_NAME";

    /*--------Error Messages--------*/
    //Login
    public static String ERR_MSG_LOGIN_INCORRECT = "Incorrect username/password.";
    public static String ERR_MSG_USERNAME_REQUIRED = "Username is required.";
    public static String ERR_MSG_PASSWORD_REQUIRED = "Password is required.";

    //CheckIn
    public static String ERR_MSG_TRUCK_BAY_ALREADY_CHECKED_IN = "Truck loading bay was already checked in before.";
    public static String ERR_MSG_INVALID_TRUCK_BAY = "Invalid truck loading bay.";
    public static String ERR_MSG_NO_ORDER_FOR_TRUCK_BAY = "Current there is no order for the checked-in bay.";

    //Check Out
    public static String ERR_MSG_NO_TRUCK_BAY_CHECKED_IN = "You have not check-in to any Truck Loading Bay.";

	public static String ERR_MSG_INVALID_LOADING_ARM = "Invalid Truck Rack Arm.";
	public static String ERR_MSG_INVALID_SEAL_NO = "Invalid Seal Number.";

	public static String ERR_MSG_CANNOT_ADD_SEAL = "Can not add the Seal";

	/*--------Text Messages--------*/
    public static String TEXT_EXCEPTION = "EXCEPTION";
    public static String TEXT_ERROR = "ERROR";
    public static String TEXT_ERROR_MSG = "ERROR MSG: ";

    //Check Out
    public static String TRUCK_BAY_CHECKED_OUT = "You have check-out from Truck Loading Bay: ";


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
}
