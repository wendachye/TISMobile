package com.bizconnectivity.tismobile.webservices;

public class ConstantWS {

    public static String NAMESPACE = "http://tempuri.org/";
    public static String SOAP_ACTION = "http://tempuri.org/";

    /*--------BCPL Development Server--------*/
    //public static String URL = "http://192.168.2.91/TISMobileWS/MobileWebService.asmx";
//    public static String URL = "http://192.168.2.91/TISWS/MobileWebService.asmx";
    //public static String URL = "http://111.223.112.222:18081/TISWS/WebService.asmx";

	/*--------Live Server--------*/
    public static String URL = "http://10.129.114.48/WS_UAT/MobileWebService.asmx";

    /*-------- Mobile Web Service --------*/
    public static String WS_VALIDATE_AD_USER = "Mobile_ValidateADUser";
    public static String WS_CHECK_TECHNICIAN_NRIC = "Mobile_CheckTechnicianNRIC";
    public static String WS_CHECK_TRUCK_RACK_EXISTS = "Mobile_CheckTruckRackExists";
    public static String WS_GET_ALL_JOB_DETAILS = "Mobile_GetAllJobDetails";
	public static String WS_TS_CREATE_SEAL = "Mobile_CreateSealUsed";
    public static String WS_TS_CREATE_PUMPSTART_TIME = "Mobile_CreatePumpStartTime";
    public static String WS_TS_CREATE_PUMPSTOP_TIME = "Mobile_CreatePumpStopTime";
    public static String WS_TS_CREATE_DEPARTURE_TIME = "Mobile_CreateRackOutTime";
    public static String WS_TS_GET_TIMESLOT = "Mobile_GetTimeSlot";
    public static String WS_TS_GET_PRODUCT_GHS = "Mobile_GetProductGHS";
    public static String WS_TS_GET_PRODUCT_PPE = "Mobile_GetProductPPE";
    public static String WS_TS_GET_SDS_PDF = "Mobile_GetMSDSFile";
	public static String WS_TS_CHECK_LOADING_ARM = "Mobile_CheckValidTrackRackArm";
	public static String WS_TS_CHECK_SEAL = "Mobile_CheckValidSeal";
}
