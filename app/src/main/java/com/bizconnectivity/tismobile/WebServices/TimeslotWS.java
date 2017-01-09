package com.bizconnectivity.tismobile.WebServices;

import android.util.Log;

import com.bizconnectivity.tismobile.Classes.JobStatus;
import com.bizconnectivity.tismobile.Classes.TimeslotDetail;
import com.bizconnectivity.tismobile.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TimeslotWS {

    //Add Pump Start to Timeslot
    public static boolean invokeUpdatePumpStartWS(int TimeSlotID, Date pumpStartTime, String UpdatedBy) {
        String webMethName = ConstantWS.WS_TS_CREATE_PUMPSTART_TIME;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        // Property which holds input parameters
        PropertyInfo slotIdPI = new PropertyInfo();
        // Set Name
        slotIdPI.setName("TimeSlotID");
        // Set Value
        slotIdPI.setValue(TimeSlotID);
        // Set dataType
        slotIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(slotIdPI);

        // Property which holds input parameters
        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("pumpStartTime");
        // Set Value
        datePI.setValue(pumpStartTime);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        // Property which holds input parameters
        PropertyInfo updatedByPI = new PropertyInfo();
        // Set Name
        updatedByPI.setName("UpdatedBy");
        // Set Value
        updatedByPI.setValue(UpdatedBy);
        // Set dataType
        updatedByPI.setType(String.class);
        // Add the property to request object
        request.addProperty(updatedByPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        MarshalDate md = new MarshalDate();
        md.register(envelope);

        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

        try {
            new MarshalDate().register(envelope);
            androidHttpTransport.call(ConstantWS.SOAP_ACTION + webMethName, envelope);

            if (envelope.bodyIn instanceof SoapFault) {
                SoapFault response = (SoapFault) envelope.bodyIn;
                Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
            } else {
                SoapObject response = (SoapObject) envelope.bodyIn;
                SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

                responseProperty.toString();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return false;
    }

    //Add Pump Stop to Timeslot
    public static boolean invokeUpdatePumpStopWS(int TimeSlotID, Date pumpStopTime, String UpdatedBy) {
        String webMethName = ConstantWS.WS_TS_CREATE_PUMPSTOP_TIME;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        // Property which holds input parameters
        PropertyInfo slotIdPI = new PropertyInfo();
        // Set Name
        slotIdPI.setName("TimeSlotID");
        // Set Value
        slotIdPI.setValue(TimeSlotID);
        // Set dataType
        slotIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(slotIdPI);

        // Property which holds input parameters
        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("pumpStopTime");
        // Set Value
        datePI.setValue(pumpStopTime);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        // Property which holds input parameters
        PropertyInfo updatedByPI = new PropertyInfo();
        // Set Name
        updatedByPI.setName("UpdatedBy");
        // Set Value
        updatedByPI.setValue(UpdatedBy);
        // Set dataType
        updatedByPI.setType(String.class);
        // Add the property to request object
        request.addProperty(updatedByPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        MarshalDate md = new MarshalDate();
        md.register(envelope);

        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

        try {
            new MarshalDate().register(envelope);
            androidHttpTransport.call(ConstantWS.SOAP_ACTION + webMethName, envelope);

            if (envelope.bodyIn instanceof SoapFault) {
                SoapFault response = (SoapFault) envelope.bodyIn;
                Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
            } else {
                SoapObject response = (SoapObject) envelope.bodyIn;
                SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

                responseProperty.toString();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return false;
    }

    /*--------Retrieve Timeslot Data--------*/
    //Get Timeslot and Order Information
    public static List<TimeslotDetail> invokeRetrieveTimeSlotWS(int timeslotId) {

        List<TimeslotDetail> timeslotList = new ArrayList<TimeslotDetail>();

        String webMethName = ConstantWS.WS_TS_GET_TIMESLOT;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo slotIdPI = new PropertyInfo();
        // Set Name
        slotIdPI.setName("timeslotId");
        // Set Value
        slotIdPI.setValue(timeslotId);
        // Set dataType
        slotIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(slotIdPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

        try {
            androidHttpTransport.call(ConstantWS.SOAP_ACTION + webMethName, envelope);

            if (envelope.bodyIn instanceof SoapFault) {
                SoapFault response = (SoapFault) envelope.bodyIn;
                Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
            } else {
                SoapObject response = (SoapObject) envelope.bodyIn;
                timeslotList = getElementsFromTimeslotDetailListSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return timeslotList;
    }

    public static List<TimeslotDetail> getElementsFromTimeslotDetailListSOAP(SoapObject response) {

        List<TimeslotDetail> timeslotList = new ArrayList<TimeslotDetail>();

        int elementCount = response.getPropertyCount();
        SoapObject so1 = (SoapObject) response.getProperty(0);

        for (int i = 0; i < elementCount; i++) {
            PropertyInfo pi = new PropertyInfo();
            SoapObject nestedSO = (SoapObject) response.getProperty(i);
            int nestedElementCount = nestedSO.getPropertyCount();

            SoapObject diffgram = (SoapObject) so1.getProperty("diffgram");
            Log.d("TABLE", diffgram.toString());
            SoapObject newDataSet = (SoapObject) diffgram.getProperty("DocumentElement");
            int newDataSetCount = newDataSet.getPropertyCount();

            String output = "";

            for (int j = 0; j < newDataSetCount; j++) {
                SoapObject table = (SoapObject) newDataSet.getProperty(j);

                int slotId = 0;
                String driverId = "", armNo = "", url = "";
                float quantity = 0;

                if (!table.getProperty("TimeSlotID").toString().equals("anyType{}"))
                    slotId = Integer.valueOf(table.getProperty("TimeSlotID").toString());

                if (!table.getProperty("IDNumber").toString().equals("anyType{}"))
                    driverId = table.getProperty("IDNumber").toString();

                if (!table.getProperty("TruckRackArmNo").toString().equals("anyType{}"))
                    armNo = table.getProperty("TruckRackArmNo").toString();

                if (table.toString().contains("Quantity") && !table.getProperty("Quantity").toString().equals("anyType{}"))
                    quantity = Float.valueOf(table.getProperty("Quantity").toString());
                else
                    quantity = 20000;

                if (table.toString().contains("MSDSfileName") && !table.getProperty("MSDSfileName").toString().equals("anyType{}"))
                    url = table.getProperty("MSDSfileName").toString();

                timeslotList.add(new TimeslotDetail(slotId, driverId, armNo, quantity, url));
            }
        }

        return timeslotList;
    }

    /*--------Retrieve Product GHS/PPE--------*/
    //Get Product GHS
    public static List<String> invokeRetrieveProductGHSWS(String productName) {

        List<String> timeslotList = new ArrayList<String>();

        String webMethName = ConstantWS.WS_TS_GET_PRODUCT_GHS;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo productNamePI = new PropertyInfo();
        // Set Name
        productNamePI.setName("productName");
        // Set Value
        productNamePI.setValue(productName);
        // Set dataType
        productNamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(productNamePI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

        try {
            androidHttpTransport.call(ConstantWS.SOAP_ACTION + webMethName, envelope);

            if (envelope.bodyIn instanceof SoapFault) {
                SoapFault response = (SoapFault) envelope.bodyIn;
                Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
            } else {
                SoapObject response = (SoapObject) envelope.bodyIn;
                timeslotList = getElementsFromProductGHSSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return timeslotList;
    }

    public static List<String> getElementsFromProductGHSSOAP(SoapObject response) {

        List<String> timeslotList = new ArrayList<String>();

        int elementCount = response.getPropertyCount();
        SoapObject so1 = (SoapObject) response.getProperty(0);

        for (int i = 0; i < elementCount; i++) {
            PropertyInfo pi = new PropertyInfo();
            SoapObject nestedSO = (SoapObject) response.getProperty(i);
            int nestedElementCount = nestedSO.getPropertyCount();

            SoapObject diffgram = (SoapObject) so1.getProperty("diffgram");

            if (!diffgram.toString().contains("DocumentElement"))
                return timeslotList;

            SoapObject newDataSet = (SoapObject) diffgram.getProperty("DocumentElement");
            int newDataSetCount = newDataSet.getPropertyCount();

            String output = "";

            for (int j = 0; j < newDataSetCount; j++) {
                SoapObject table = (SoapObject) newDataSet.getProperty(j);

                String pic = "";
                if (!table.getProperty("GHSPicture").toString().equals("anyType{}"))
                    pic = table.getProperty("GHSPicture").toString();

                timeslotList.add(pic);
            }
        }

        return timeslotList;
    }

    //Get Product PPE
    public static List<String> invokeRetrieveProductPPEWS(String productName) {

        List<String> timeslotList = new ArrayList<String>();

        String webMethName = ConstantWS.WS_TS_GET_PRODUCT_PPE;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo productNamePI = new PropertyInfo();
        // Set Name
        productNamePI.setName("productName");
        // Set Value
        productNamePI.setValue(productName);
        // Set dataType
        productNamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(productNamePI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

        try {
            androidHttpTransport.call(ConstantWS.SOAP_ACTION + webMethName, envelope);

            if (envelope.bodyIn instanceof SoapFault) {
                SoapFault response = (SoapFault) envelope.bodyIn;
                Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
            } else {
                SoapObject response = (SoapObject) envelope.bodyIn;
                timeslotList = getElementsFromProductPPESOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return timeslotList;
    }

    public static List<String> getElementsFromProductPPESOAP(SoapObject response) {

        List<String> timeslotList = new ArrayList<String>();

        int elementCount = response.getPropertyCount();
        SoapObject so1 = (SoapObject) response.getProperty(0);

        for (int i = 0; i < elementCount; i++) {
            PropertyInfo pi = new PropertyInfo();
            SoapObject nestedSO = (SoapObject) response.getProperty(i);
            int nestedElementCount = nestedSO.getPropertyCount();

            SoapObject diffgram = (SoapObject) so1.getProperty("diffgram");

            if (!diffgram.toString().contains("DocumentElement"))
                return timeslotList;

            SoapObject newDataSet = (SoapObject) diffgram.getProperty("DocumentElement");
            int newDataSetCount = newDataSet.getPropertyCount();

            String output = "";

            for (int j = 0; j < newDataSetCount; j++) {
                SoapObject table = (SoapObject) newDataSet.getProperty(j);

                String pic = "";
                if (!table.getProperty("PPEPicture").toString().equals("anyType{}"))
                    pic = table.getProperty("PPEPicture").toString();

                timeslotList.add(pic);
            }
        }

        return timeslotList;
    }

    /*--------Retrieve Seal Used--------*/
    //Create seal used
    public static boolean invokeCreateSealUsedWS(String sealNo, int TimeSlotID, String sealPos, String UpdatedBy) {
        String webMethName = ConstantWS.WS_TS_CREATE_SEAL_USED;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        // Property which holds input parameters
        PropertyInfo sealNoPI = new PropertyInfo();
        // Set Name
        sealNoPI.setName("sealNo");
        // Set Value
        sealNoPI.setValue(sealNo);
        // Set dataType
        sealNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(sealNoPI);

        // Property which holds input parameters
        PropertyInfo slotIdPI = new PropertyInfo();
        // Set Name
        slotIdPI.setName("TimeSlotID");
        // Set Value
        slotIdPI.setValue(TimeSlotID);
        // Set dataType
        slotIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(slotIdPI);

        // Property which holds input parameters
        PropertyInfo sealPosPI = new PropertyInfo();
        // Set Name
        sealPosPI.setName("sealPos");
        // Set Value
        sealPosPI.setValue(sealPos);
        // Set dataType
        sealPosPI.setType(String.class);
        // Add the property to request object
        request.addProperty(sealPosPI);

        // Property which holds input parameters
        PropertyInfo updatedByPI = new PropertyInfo();
        // Set Name
        updatedByPI.setName("UpdatedBy");
        // Set Value
        updatedByPI.setValue(UpdatedBy);
        // Set dataType
        updatedByPI.setType(String.class);
        // Add the property to request object
        request.addProperty(updatedByPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        MarshalDate md = new MarshalDate();
        md.register(envelope);

        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

        try {
            new MarshalDate().register(envelope);
            androidHttpTransport.call(ConstantWS.SOAP_ACTION + webMethName, envelope);

            if (envelope.bodyIn instanceof SoapFault) {
                SoapFault response = (SoapFault) envelope.bodyIn;
                Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
            } else {
                SoapObject response = (SoapObject) envelope.bodyIn;
                SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

                responseProperty.toString();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return false;
    }

    public static int getElementsFromCountSOAP(SoapObject response) {

        int count = 0;
        int elementCount = response.getPropertyCount();

        if (elementCount > 0) {
            count = Integer.valueOf(response.getProperty(0).toString());
        }

        return count;
    }

    //Get num incorrect seal
    public static int invokeGetNumIncorrectSealWS(int timeslotId, String allSealNo) {

        int count = 0;

        String webMethName = ConstantWS.WS_TS_GET_NUM_INCORRECT_SEAL;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo slotIdPI = new PropertyInfo();
        // Set Name
        slotIdPI.setName("timeslotId");
        // Set Value
        slotIdPI.setValue(timeslotId);
        // Set dataType
        slotIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(slotIdPI);

        PropertyInfo sealNoPI = new PropertyInfo();
        // Set Name
        sealNoPI.setName("allSealNo");
        // Set Value
        sealNoPI.setValue(allSealNo);
        // Set dataType
        sealNoPI.setType(String.class);
        // Add the property to request object
        request.addProperty(sealNoPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        MarshalDate md = new MarshalDate();
        md.register(envelope);

        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

        try {
            androidHttpTransport.call(ConstantWS.SOAP_ACTION + webMethName, envelope);

            if (envelope.bodyIn instanceof SoapFault) {
                SoapFault response = (SoapFault) envelope.bodyIn;
                Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
            } else {
                SoapObject response = (SoapObject) envelope.bodyIn;
                count = getElementsFromCountSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return count;
    }

    //Get Seal Used
    public static List<String> invokeGetSealUsedWS(int timeslotId) {

        List<String> strList = new ArrayList<String>();

        String webMethName = ConstantWS.WS_TS_GET_SEAL_USED;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo slotIdPI = new PropertyInfo();
        // Set Name
        slotIdPI.setName("timeslotId");
        // Set Value
        slotIdPI.setValue(timeslotId);
        // Set dataType
        slotIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(slotIdPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        MarshalDate md = new MarshalDate();
        md.register(envelope);

        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

        try {
            androidHttpTransport.call(ConstantWS.SOAP_ACTION + webMethName, envelope);

            if (envelope.bodyIn instanceof SoapFault) {
                SoapFault response = (SoapFault) envelope.bodyIn;
                Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
            } else {
                SoapObject response = (SoapObject) envelope.bodyIn;
                strList = getElementsFromSealUsedSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return strList;
    }

    public static List<String> getElementsFromSealUsedSOAP(SoapObject response) {

        List<String> strList = new ArrayList<String>();

        int elementCount = response.getPropertyCount();
        SoapObject so1 = (SoapObject) response.getProperty(0);

        for (int i = 0; i < elementCount; i++) {
            PropertyInfo pi = new PropertyInfo();
            SoapObject nestedSO = (SoapObject) response.getProperty(i);
            int nestedElementCount = nestedSO.getPropertyCount();

            SoapObject diffgram = (SoapObject) so1.getProperty("diffgram");
            Log.d("TABLE", diffgram.toString());

            SoapObject newDataSet = (SoapObject) diffgram.getProperty("DocumentElement");
            int newDataSetCount = newDataSet.getPropertyCount();

            String output = "";

            for (int j = 0; j < newDataSetCount; j++) {

                SoapObject table = (SoapObject) newDataSet.getProperty(j);

                String sealNo = "";

                if (!table.getProperty("SealNo").toString().equals("anyType{}"))
                    sealNo = table.getProperty("SealNo").toString();

                strList.add(sealNo);
            }
        }

        return strList;
    }

    /*--------Retrieve Job Status--------*/
    //Get Job Status
    public static List<JobStatus> invokeRetrieveJobStatusWS(int timeslotId) {

        List<JobStatus> statusList = new ArrayList<JobStatus>();

        String webMethName = ConstantWS.WS_TS_GET_JOB_STATUS;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo slotIdPI = new PropertyInfo();
        // Set Name
        slotIdPI.setName("timeslotId");
        // Set Value
        slotIdPI.setValue(timeslotId);
        // Set dataType
        slotIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(slotIdPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

        try {
            androidHttpTransport.call(ConstantWS.SOAP_ACTION + webMethName, envelope);

            if (envelope.bodyIn instanceof SoapFault) {
                SoapFault response = (SoapFault) envelope.bodyIn;
                Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
            } else {
                SoapObject response = (SoapObject) envelope.bodyIn;
                statusList = getElementsFromJobStatusSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return statusList;
    }

    public static List<JobStatus> getElementsFromJobStatusSOAP(SoapObject response) {

        List<JobStatus> statusList = new ArrayList<JobStatus>();

        int elementCount = response.getPropertyCount();
        SoapObject so1 = (SoapObject) response.getProperty(0);

        for (int i = 0; i < elementCount; i++) {
            PropertyInfo pi = new PropertyInfo();
            SoapObject nestedSO = (SoapObject) response.getProperty(i);
            int nestedElementCount = nestedSO.getPropertyCount();

            SoapObject diffgram = (SoapObject) so1.getProperty("diffgram");
            Log.d("TABLE", diffgram.toString());

            SoapObject newDataSet = (SoapObject) diffgram.getProperty("DocumentElement");
            int newDataSetCount = newDataSet.getPropertyCount();

            String output = "";

            for (int j = 0; j < newDataSetCount; j++) {
                SoapObject table = (SoapObject) newDataSet.getProperty(j);

                int timeslotId = 0, operatorID = 0;
                String status = "", OperatorIdNum = "", driverWI = "";
                boolean isPPE = false, isOperator = false, isDriver = false, isSafetyCheck = false, isLoadingArm = false, isBC = false;
                boolean isPumpStart = false, isPumpStop = false, isSeal = false, isDeparture = false;
                Date pumpStartTime = null, pumpStopTime = null, departureTime = null;

                if (!table.getProperty("TimeslotID").toString().equals("anyType{}"))
                    timeslotId = Integer.valueOf(table.getProperty("TimeslotID").toString());

                if (!table.getProperty("Status").toString().equals("anyType{}"))
                    status = table.getProperty("Status").toString();

                if (!table.getProperty("IsPPE").toString().equals("anyType{}"))
                    isPPE = Boolean.valueOf(table.getProperty("IsPPE").toString());

                if (!table.getProperty("IsOperator").toString().equals("anyType{}"))
                    isOperator = Boolean.valueOf(table.getProperty("IsOperator").toString());

                if (table.toString().contains("OperatorID") && !table.getProperty("OperatorID").toString().equals("anyType{}"))
                    operatorID = Integer.valueOf(table.getProperty("OperatorID").toString());

                if (table.toString().contains("IDNumber") && !table.getProperty("IDNumber").toString().equals("anyType{}"))
                    OperatorIdNum = table.getProperty("IDNumber").toString();

                if (!table.getProperty("IsDriver").toString().equals("anyType{}"))
                    isDriver = Boolean.valueOf(table.getProperty("IsDriver").toString());

                if (!table.getProperty("DriverWI").toString().equals("anyType{}"))
                    driverWI = table.getProperty("DriverWI").toString();

                if (!table.getProperty("IsSafetyCheck").toString().equals("anyType{}"))
                    isSafetyCheck = Boolean.valueOf(table.getProperty("IsSafetyCheck").toString());

                if (!table.getProperty("IsLoadingArm").toString().equals("anyType{}"))
                    isLoadingArm = Boolean.valueOf(table.getProperty("IsLoadingArm").toString());

                if (!table.getProperty("IsBatchController").toString().equals("anyType{}"))
                    isBC = Boolean.valueOf(table.getProperty("IsBatchController").toString());

                if (!table.getProperty("IsPumpStart").toString().equals("anyType{}"))
                    isPumpStart = Boolean.valueOf(table.getProperty("IsPumpStart").toString());

                if (table.toString().contains("PumpStartTime") && !table.getProperty("PumpStartTime").toString().equals("anyType{}")) {
                    //java.text.ParseException: Unparseable date: "2016-06-29 19:32:15.023+08:00" (at offset 4)

                    String dateString = table.getProperty("PumpStartTime").toString().replace("T", " ");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        pumpStartTime = dateFormat.parse(dateString);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (!table.getProperty("IsPumpStop").toString().equals("anyType{}"))
                    isPumpStop = Boolean.valueOf(table.getProperty("IsPumpStop").toString());

                if (table.toString().contains("PumpStopTime") && !table.getProperty("PumpStopTime").toString().equals("anyType{}")) {
                    String dateString = table.getProperty("PumpStopTime").toString().replace("T", " ");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        pumpStopTime = dateFormat.parse(dateString);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (!table.getProperty("IsSeal").toString().equals("anyType{}"))
                    isSeal = Boolean.valueOf(table.getProperty("IsSeal").toString());

                if (!table.getProperty("IsDeparture").toString().equals("anyType{}"))
                    isDeparture = Boolean.valueOf(table.getProperty("IsDeparture").toString());

                if (table.toString().contains("DepartureTime") && !table.getProperty("DepartureTime").toString().equals("anyType{}")) {
                    String dateString = table.getProperty("DepartureTime").toString().replace("T", " ");
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    try {
                        departureTime = dateFormat.parse(dateString);
                    } catch (ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                statusList.add(new JobStatus(timeslotId, status, isPPE, isOperator, operatorID, OperatorIdNum,
                        isDriver, driverWI, isSafetyCheck, isLoadingArm, isBC,
                        isPumpStart, pumpStartTime, isPumpStop, pumpStopTime,
                        isSeal, isDeparture, departureTime));
            }
        }

        return statusList;
    }

    //Update Job Status
    public static boolean invokeUpdateJobStatusWS(JobStatus job) {
        String webMethName = ConstantWS.WS_TS_UPDATE_JOB_STATUS;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        // Property which holds input parameters
        PropertyInfo slotIdPI = new PropertyInfo();
        // Set Name
        slotIdPI.setName("timeslotId");
        // Set Value
        slotIdPI.setValue(job.getTimeslotId());
        // Set dataType
        slotIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(slotIdPI);

        // Property which holds input parameters
        PropertyInfo statPI = new PropertyInfo();
        // Set Name
        statPI.setName("status");
        // Set Value
        statPI.setValue(job.getStatus());
        // Set dataType
        statPI.setType(String.class);
        // Add the property to request object
        request.addProperty(statPI);

        // Property which holds input parameters
        PropertyInfo isPPEPI = new PropertyInfo();
        // Set Name
        isPPEPI.setName("isPPE");
        // Set Value
        isPPEPI.setValue(job.getIsPPE());
        // Set dataType
        isPPEPI.setType(Boolean.class);
        // Add the property to request object
        request.addProperty(isPPEPI);

        // Property which holds input parameters
        PropertyInfo isOperatorPI = new PropertyInfo();
        // Set Name
        isOperatorPI.setName("isOperator");
        // Set Value
        isOperatorPI.setValue(job.getIsOperator());
        // Set dataType
        isOperatorPI.setType(Boolean.class);
        // Add the property to request object
        request.addProperty(isOperatorPI);

        if (job.getOperatorID() > 0) {
            // Property which holds input parameters
            PropertyInfo operatorIdPI = new PropertyInfo();
            // Set Name
            operatorIdPI.setName("operatorID");
            // Set Value
            operatorIdPI.setValue(job.getOperatorID());
            // Set dataType
            operatorIdPI.setType(Integer.class);
            // Add the property to request object
            request.addProperty(operatorIdPI);
        }

        //Property which holds input parameters
        PropertyInfo isDriverPI = new PropertyInfo();
        // Set Name
        isDriverPI.setName("isDriver");
        // Set Value
        isDriverPI.setValue(job.getIsDriver());
        // Set dataType
        isDriverPI.setType(boolean.class);
        // Add the property to request object
        request.addProperty(isDriverPI);

        //Property which holds input parameters
        PropertyInfo driverWIPI = new PropertyInfo();
        // Set Name
        driverWIPI.setName("driverWI");
        // Set Value
        driverWIPI.setValue(job.getDriverWI());
        // Set dataType
        driverWIPI.setType(String.class);
        // Add the property to request object
        request.addProperty(driverWIPI);

        //Property which holds input parameters
        PropertyInfo isSafetyCheckPI = new PropertyInfo();
        // Set Name
        isSafetyCheckPI.setName("isSafetyCheck");
        // Set Value
        isSafetyCheckPI.setValue(job.getIsSafetyCheck());
        // Set dataType
        isSafetyCheckPI.setType(boolean.class);
        // Add the property to request object
        request.addProperty(isSafetyCheckPI);

        //Property which holds input parameters
        PropertyInfo isLoadingArmPI = new PropertyInfo();
        // Set Name
        isLoadingArmPI.setName("isLoadingArm");
        // Set Value
        isLoadingArmPI.setValue(job.getIsLoadingArm());
        // Set dataType
        isLoadingArmPI.setType(boolean.class);
        // Add the property to request object
        request.addProperty(isLoadingArmPI);

        //Property which holds input parameters
        PropertyInfo isBCPI = new PropertyInfo();
        // Set Name
        isBCPI.setName("isBC");
        // Set Value
        isBCPI.setValue(job.getIsBC());
        // Set dataType
        isBCPI.setType(boolean.class);
        // Add the property to request object
        request.addProperty(isBCPI);

        //Property which holds input parameters
        PropertyInfo isPumpStartPI = new PropertyInfo();
        // Set Name
        isPumpStartPI.setName("isPumpStart");
        // Set Value
        isPumpStartPI.setValue(job.getIsPumpStart());
        // Set dataType
        isPumpStartPI.setType(boolean.class);
        // Add the property to request object
        request.addProperty(isPumpStartPI);

        if (job.getPumpStartTime() != null) {
            //Property which holds input parameters
            PropertyInfo pumpStartTimePI = new PropertyInfo();
            // Set Name
            pumpStartTimePI.setName("pumpStartTime");
            // Set Value
            pumpStartTimePI.setValue(job.getPumpStartTime());
            // Set dataType
            pumpStartTimePI.setType(Date.class);
            // Add the property to request object
            request.addProperty(pumpStartTimePI);
        }

        //Property which holds input parameters
        PropertyInfo isPumpStopPI = new PropertyInfo();
        // Set Name
        isPumpStopPI.setName("isPumpStop");
        // Set Value
        isPumpStopPI.setValue(job.getIsPumpStop());
        // Set dataType
        isPumpStopPI.setType(boolean.class);
        // Add the property to request object
        request.addProperty(isPumpStopPI);

        if (job.getPumpStopTime() != null) {
            //Property which holds input parameters
            PropertyInfo pumpStopTimePI = new PropertyInfo();
            // Set Name
            pumpStopTimePI.setName("pumpStopTime");
            // Set Value
            pumpStopTimePI.setValue(job.getPumpStopTime());
            // Set dataType
            pumpStopTimePI.setType(Date.class);
            // Add the property to request object
            request.addProperty(pumpStopTimePI);
        }

        //Property which holds input parameters
        PropertyInfo isSealPI = new PropertyInfo();
        // Set Name
        isSealPI.setName("isSeal");
        // Set Value
        isSealPI.setValue(job.getIsSeal());
        // Set dataType
        isSealPI.setType(boolean.class);
        // Add the property to request object
        request.addProperty(isSealPI);

        //Property which holds input parameters
        PropertyInfo isDeparturePI = new PropertyInfo();
        // Set Name
        isDeparturePI.setName("isDeparture");
        // Set Value
        isDeparturePI.setValue(job.getIsDeparture());
        // Set dataType
        isDeparturePI.setType(boolean.class);
        // Add the property to request object
        request.addProperty(isDeparturePI);

        if (job.getDepartureTime() != null) {
            //Property which holds input parameters
            PropertyInfo departureTimePI = new PropertyInfo();
            // Set Name
            departureTimePI.setName("departureTime");
            // Set Value
            departureTimePI.setValue(job.getDepartureTime());
            // Set dataType
            departureTimePI.setType(Date.class);
            // Add the property to request object
            request.addProperty(departureTimePI);
        }

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        MarshalDate md = new MarshalDate();
        md.register(envelope);

        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

        try {
            new MarshalDate().register(envelope);
            androidHttpTransport.call(ConstantWS.SOAP_ACTION + webMethName, envelope);

            if (envelope.bodyIn instanceof SoapFault) {
                SoapFault response = (SoapFault) envelope.bodyIn;
                Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
            } else {
                SoapObject response = (SoapObject) envelope.bodyIn;
                SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

                responseProperty.toString();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return false;
    }

    /*--------Create Operator--------*/
    //Create Operator
    public static boolean invokeCreateOperatorWS(int TimeSlotID, int operatorId, String UpdatedBy) {

        String webMethName = ConstantWS.WS_TS_CREATE_OPERATOR;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        // Property which holds input parameters
        PropertyInfo slotIdPI = new PropertyInfo();
        // Set Name
        slotIdPI.setName("TimeSlotID");
        // Set Value
        slotIdPI.setValue(TimeSlotID);
        // Set dataType
        slotIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(slotIdPI);

        // Property which holds input parameters
        PropertyInfo operatorIdPI = new PropertyInfo();
        // Set Name
        operatorIdPI.setName("operatorId");
        // Set Value
        operatorIdPI.setValue(operatorId);
        // Set dataType
        operatorIdPI.setType(int.class);
        // Add the property to request object
        request.addProperty(operatorIdPI);

        // Property which holds input parameters
        PropertyInfo updatedByPI = new PropertyInfo();
        // Set Name
        updatedByPI.setName("UpdatedBy");
        // Set Value
        updatedByPI.setValue(UpdatedBy);
        // Set dataType
        updatedByPI.setType(String.class);
        // Add the property to request object
        request.addProperty(updatedByPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        MarshalDate md = new MarshalDate();
        md.register(envelope);

        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

        try {
            new MarshalDate().register(envelope);
            androidHttpTransport.call(ConstantWS.SOAP_ACTION + webMethName, envelope);

            if (envelope.bodyIn instanceof SoapFault) {
                SoapFault response = (SoapFault) envelope.bodyIn;
                Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
            } else {
                SoapObject response = (SoapObject) envelope.bodyIn;
                SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

                responseProperty.toString();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return false;
    }

    public static int getElementsFromOperatorIdSOAP(SoapObject response) {

        int count = 0;

        int elementCount = response.getPropertyCount();

        if (elementCount > 0) {
            //Log.d("TABLE", response.getProperty(0).toString());
            count = Integer.valueOf(response.getProperty(0).toString());
        }

        return count;
    }

    //Get Operator Id
    public static int invokeGetOperatorIdWS(String idNum) {

        int count = 0;

        String webMethName = ConstantWS.WS_TS_GET_OPERATOR_ID;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo idNumPI = new PropertyInfo();
        // Set Name
        idNumPI.setName("idNum");
        // Set Value
        idNumPI.setValue(idNum);
        // Set dataType
        idNumPI.setType(String.class);
        // Add the property to request object
        request.addProperty(idNumPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        MarshalDate md = new MarshalDate();
        md.register(envelope);

        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

        try {
            androidHttpTransport.call(ConstantWS.SOAP_ACTION + webMethName, envelope);

            if (envelope.bodyIn instanceof SoapFault) {
                SoapFault response = (SoapFault) envelope.bodyIn;
                Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
            } else {
                SoapObject response = (SoapObject) envelope.bodyIn;
                count = getElementsFromOperatorIdSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return count;
    }
}
