package com.bizconnectivity.tismobile.webservices;

public class ConstantWS {

    public static String NAMESPACE = "http://tempuri.org/";
    public static String SOAP_ACTION = "http://tempuri.org/";

    /*--------BCPL Development Server--------*/
    public static String URL = "http://192.168.2.91/TISWS/MobileWebService.asmx";

	/*--------BCPL Development Server--------*/
    public static String SDS_FILE_LOCATION = "http://192.168.2.91/tis/Core/MSDS Files/";
    public static String GHS_FILE_LOCATION = "http://192.168.2.91/tis/Images/GHS/";
    public static String PPE_FILE_LOCATION = "http://192.168.2.91/tis/Images/PPE/";

	/*--------Live Server--------*/
//	public static String URL = "http://10.129.114.48/WS_UAT/MobileWebService.asmx";

	/*--------Live Server--------*/
//	public static String SDS_FILE_LOCATION = "http://10.129.114.48/Penjuru_UAT/Core/MSDS Files/";
//	public static String GHS_FILE_LOCATION = "http://10.129.114.48/Penjuru_UAT/Images/GHS/";
//	public static String PPE_FILE_LOCATION = "http://10.129.114.48/Penjuru_UAT/Images/PPE/";

    /*-------- Mobile Web Service --------*/
    public static String WS_VALIDATE_AD_USER = "Mobile_ValidateADUser";
    public static String WS_CHECK_TECHNICIAN_NRIC = "Mobile_CheckTechnicianNRIC";
    public static String WS_CHECK_TRUCK_RACK_EXISTS = "Mobile_CheckTruckRackExists";
    public static String WS_GET_ALL_JOB_DETAILS = "Mobile_GetAllJobDetails";
	public static String WS_TS_CREATE_SEAL = "Mobile_CreateSealUsed";
    public static String WS_TS_CREATE_PUMP_START_TIME = "Mobile_CreatePumpStartTime";
    public static String WS_TS_CREATE_PUMP_STOP_TIME = "Mobile_CreatePumpStopTime";
    public static String WS_TS_CREATE_DEPARTURE_TIME = "Mobile_CreateRackOutTime";
    public static String WS_TS_GET_TIMESLOT = "Mobile_GetTimeSlot";
    public static String WS_TS_GET_PRODUCT_GHS = "Mobile_GetProductGHS";
    public static String WS_TS_GET_PRODUCT_PPE = "Mobile_GetProductPPE";
    public static String WS_TS_GET_SDS_PDF = "Mobile_GetMSDSFile";
	public static String WS_TS_CHECK_LOADING_ARM = "Mobile_CheckValidTrackRackArm";
	public static String WS_TS_CHECK_SEAL = "Mobile_CheckValidSeal";
	public static String WS_TS_GET_JOB_DETAIL_BY_ORDER_ID = "Mobile_GetJobDetailsByTimeslotID";
	public static String WS_TS_GET_JOB_DETAIL_BY_CUSTOMER_NAME = "Mobile_GetJobDetailsByCustomerName";
	public static String WS_TS_GET_SEAL_NO = "Mobile_GetSealNo";
}
