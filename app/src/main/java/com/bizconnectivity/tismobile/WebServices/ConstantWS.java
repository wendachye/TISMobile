package com.bizconnectivity.tismobile.WebServices;

public class ConstantWS {

    public static String NAMESPACE = "http://tempuri.org/";
    public static String SOAP_ACTION = "http://tempuri.org/";

    /*--------BCPL Development Server--------*/
    //public static String URL = "http://192.168.2.91/TISMobileWS/MobileWebService.asmx";
    public static String URL = "http://192.168.2.91/TISWS/MobileWebService.asmx";
    //public static String URL = "http://111.223.112.222:18081/TISWS/WebService.asmx";

	/*--------Live Server--------*/
//    public static String URL = "http://10.129.114.48/WS_UAT/MobileWebService.asmx";

    /*-------- User WS--------*/
    public static String WSTYPE_LOGIN = "Login";
    public static String WSTYPE_CHECKTRUCKRACK = "CheckTruckRack";

    public static String WS_VALIDATE_AD_USER = "Mobile_ValidateADUser";
    public static String WS_CHECK_TRUCK_RACK_EXISTS = "Mobile_CheckTruckRackExists";

    /*-------- Dashboard WS--------*/
    public static String WSTYPE_ALL_COUNT = "AllCount";
    public static String WSTYPE_LATE_COUNT = "LateCount";
    public static String WSTYPE_TODAY_COUNT = "TodayCount";
    public static String WSTYPE_QUEUEUP_COUNT = "QueueUpCount";
    public static String WSTYPE_WEIGHIN_COUNT = "WeighInCount";
    public static String WSTYPE_WEIGHOUT_COUNT = "WeighOutCount";
    public static String WSTYPE_PUMPSTART_COUNT = "PumpStartCount";
    public static String WSTYPE_DEPARTURE_COUNT = "DepartureCount";

    public static String WSTYPE_LATE_LIST = "LateList";
    public static String WSTYPE_TODAY_LIST = "TodayList";
    public static String WSTYPE_QUEUEUP_LIST = "QueueUpList";
    public static String WSTYPE_WEIGHIN_LIST = "WeighInList";
    public static String WSTYPE_WEIGHOUT_LIST = "WeighOutList";
    public static String WSTYPE_PUMPSTART_LIST = "PumpStartList";
    public static String WSTYPE_DEPARTURE_LIST = "DepartureList";

    public static String WS_ORDER_GET_ALL_COUNT = "Mobile_GetAllCount";
    public static String WS_ORDER_GET_LATE_COUNT = "Mobile_GetLateCount";
    public static String WS_ORDER_GET_PENDING_KEYIN_COUNT = "Mobile_GetPendingKeyInCount";
    public static String WS_ORDER_GET_KEYIN_COUNT = "Mobile_GetKeyInCount";
    public static String WS_ORDER_GET_WEIGHTIN_COUNT = "Mobile_GetWeightInCount";
    public static String WS_ORDER_GET_PUMPIN_COUNT = "Mobile_GetPumpInCount";
    public static String WS_ORDER_GET_PUMPOUT_COUNT = "Mobile_GetPumpOutCount";
    public static String WS_ORDER_GET_WEIGHTOUT_COUNT = "Mobile_GetWeightOutCount";
    public static String WS_ORDER_GET_KEYOUT_COUNT = "Mobile_GetKeyOutCount";

    public static String WS_ORDER_GET_LATE = "Mobile_GetLate";
    public static String WS_ORDER_GET_PENDING_KEYIN = "Mobile_GetPendingKeyIn";
    public static String WS_ORDER_GET_KEYIN = "Mobile_GetKeyIn";
    public static String WS_ORDER_GET_WEIGHTIN = "Mobile_GetWeightIn";
    public static String WS_ORDER_GET_PUMPIN = "Mobile_GetPumpIn";
    public static String WS_ORDER_GET_PUMPOUT = "Mobile_GetPumpOut";
    public static String WS_ORDER_GET_WEIGHTOUT = "Mobile_GetWeightOut";
    public static String WS_ORDER_GET_KEYOUT = "Mobile_GetKeyOut";

    /*-------- Timeslot WS--------*/
    public static String WSTYPE_GET_NUM_INCORRECT_SEAL = "GetNumIncorrectSeal";
    public static String WSTYPE_GET_TIMESLOT_DETAIL = "GetTimeslotDetail";
    public static String WSTYPE_UPDATE_PUMPSTART = "UpdatePumpStart";
    public static String WSTYPE_UPDATE_PUMPSTOP = "UpdatePumpStop";
    public static String WSTYPE_GET_PRODUCT_GHS = "GetProductGHS";
    public static String WSTYPE_GET_PRODUCT_PPE = "GetProductPPE";
    public static String WSTYPE_GET_SEAL_USED = "GetSealUsed";
    public static String WSTYPE_CREATE_SEAL_USED = "CreateSealUsed";
    public static String WSTYPE_RETRIEVE_JOB_STATUS = "RetrieveJobStatus";
    public static String WSTYPE_UPDATE_JOB_STATUS = "UpdateJobStatus";
    public static String WSTYPE_GET_OPERATOR_ID = "GetOperatorId";
    public static String WSTYPE_CREATE_OPERATOR = "CreateOperator";

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

    public static String WS_TS_CREATE_SEAL_USED = "Mobile_CreateSealUsed";
    public static String WS_TS_GET_NUM_INCORRECT_SEAL = "Mobile_GetNumIncorrectSeal";
    public static String WS_TS_GET_SEAL_USED = "Mobile_GetSealUsed";

    public static String WS_TS_GET_JOB_STATUS = "Mobile_GetJobStatus";
    public static String WS_TS_UPDATE_JOB_STATUS = "Mobile_UpdateJobStatus";

    public static String WS_TS_CREATE_OPERATOR = "Mobile_CreateOperator";
    public static String WS_TS_GET_OPERATOR_ID = "Mobile_GetOperatorId";
}
