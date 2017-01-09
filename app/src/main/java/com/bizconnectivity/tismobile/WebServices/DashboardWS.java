package com.bizconnectivity.tismobile.WebServices;

import android.util.Log;

import com.bizconnectivity.tismobile.Classes.Order;
import com.bizconnectivity.tismobile.Classes.OrderCount;
import com.bizconnectivity.tismobile.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DashboardWS {
    /*--------Retrieve All Order Count--------*/
    public static List<OrderCount> getElementsFromAllCountSOAP(SoapObject response) {
        //int count = 0;
        List<OrderCount> countList = new ArrayList<OrderCount>();

        int elementCount = response.getPropertyCount();
        SoapObject so1 = (SoapObject) response.getProperty(0);

        for (int i = 0; i < elementCount; i++) {
            PropertyInfo pi = new PropertyInfo();
            SoapObject nestedSO = (SoapObject) response.getProperty(i);

            SoapObject diffgram = (SoapObject) so1.getProperty("diffgram");
            SoapObject newDataSet = (SoapObject) diffgram.getProperty("DocumentElement");
            int newDataSetCount = newDataSet.getPropertyCount();

            String output = "";

            for (int j = 0; j < newDataSetCount; j++) {
                SoapObject table = (SoapObject) newDataSet.getProperty(j);

                int late = 0, today = 0, queueUp = 0, weighIn = 0;
                int pumpStart = 0, pumpStop = 0, departure = 0, weighOut = 0;

                if (!table.getProperty("LateCount").toString().equals("anyType{}"))
                    late = Integer.valueOf(table.getProperty("LateCount").toString());

                if (!table.getProperty("PendingKeyInCount").toString().equals("anyType{}"))
                    today = Integer.valueOf(table.getProperty("PendingKeyInCount").toString());

                if (!table.getProperty("KeyInCount").toString().equals("anyType{}"))
                    queueUp = Integer.valueOf(table.getProperty("KeyInCount").toString());

                if (!table.getProperty("WeightInCount").toString().equals("anyType{}"))
                    weighIn = Integer.valueOf(table.getProperty("WeightInCount").toString());

                if (!table.getProperty("PumpInCount").toString().equals("anyType{}"))
                    pumpStart = Integer.valueOf(table.getProperty("PumpInCount").toString());

                if (!table.getProperty("PumpOutCount").toString().equals("anyType{}"))
                    pumpStop = Integer.valueOf(table.getProperty("PumpOutCount").toString());

                if (!table.getProperty("WeightOutCount").toString().equals("anyType{}"))
                    weighOut = Integer.valueOf(table.getProperty("WeightOutCount").toString());

                if (!table.getProperty("KeyOutCount").toString().equals("anyType{}"))
                    departure = Integer.valueOf(table.getProperty("KeyOutCount").toString());

                countList.add(new OrderCount(late, today, queueUp, weighIn, pumpStart, pumpStop, departure, weighOut));
            }
        }

        return countList;
    }

    //Get all orders Count
    public static List<OrderCount> invokeGetAllCountWS(Date date, String rackNo) {
        String webMethName = ConstantWS.WS_ORDER_GET_ALL_COUNT;
        //int count = 0;
        List<OrderCount> count = new ArrayList<OrderCount>();
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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
                count = getElementsFromAllCountSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }
        return count;
    }

    /*--------Retrieve Order Count--------*/
    public static int getElementsFromCountSOAP(SoapObject response) {
        int count = 0;
        int elementCount = response.getPropertyCount();

        if (elementCount > 0) {
            count = Integer.valueOf(response.getProperty(0).toString());
        }

        return count;
    }

    //Get 5 min late orders (Count)
    public static int invokeGetLateCountWS(Date bookingDate, String rackNo) {
        int count = 0;

        String webMethName = ConstantWS.WS_ORDER_GET_LATE_COUNT;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("bookingDate");
        // Set Value
        datePI.setValue(bookingDate);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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

    //Get Timeslot With Pending Key In (Count)
    public static int invokeGetPendingKeyInCountWS(Date date, String rackNo) {
        int count = 0;

        String webMethName = ConstantWS.WS_ORDER_GET_PENDING_KEYIN_COUNT;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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

    //Get Timeslot With Key In (Count)
    public static int invokeGetKeyInCountWS(Date date, String rackNo) {
        int count = 0;

        String webMethName = ConstantWS.WS_ORDER_GET_KEYIN_COUNT;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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

    //Get Timeslot With Weight In (Count)
    public static int invokeGetWeighInCountWS(Date date, String rackNo) {
        int count = 0;

        String webMethName = ConstantWS.WS_ORDER_GET_WEIGHTIN_COUNT;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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

    //Get Timeslot With Pump In (Count)
    public static int invokeGetPumpInCountWS(Date date, String rackNo) {
        int count = 0;

        String webMethName = ConstantWS.WS_ORDER_GET_PUMPIN_COUNT;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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

    //Get Timeslot With Pump Out (Count)
    public static int invokeGetPumpOutCountWS(Date date, String rackNo) {
        int count = 0;

        String webMethName = ConstantWS.WS_ORDER_GET_PUMPOUT_COUNT;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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

    //Get Timeslot With Weigh Out (Count)
    public static int invokeGetWeighOutCountWS(Date date, String rackNo) {
        int count = 0;

        String webMethName = ConstantWS.WS_ORDER_GET_WEIGHTOUT_COUNT;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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

    //Get Timeslot With Key Out / Departure (Count)
    public static int invokeGetKeyOutCountWS(Date date, String rackNo) {
        int count = 0;

        String webMethName = ConstantWS.WS_ORDER_GET_KEYOUT_COUNT;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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

    /*--------Retrieve Order Data--------*/
    public static List<Order> getElementsFromOrderListSOAP(SoapObject response) {

        List<Order> orderList = new ArrayList<Order>();

        int elementCount = response.getPropertyCount();
        SoapObject so1 = (SoapObject) response.getProperty(0);

        for (int i = 0; i < elementCount; i++) {
            PropertyInfo pi = new PropertyInfo();
            SoapObject nestedSO = (SoapObject) response.getProperty(i);
            int nestedElementCount = nestedSO.getPropertyCount();

            SoapObject diffgram = (SoapObject) so1.getProperty("diffgram");
            if (diffgram.getPropertyCount() == 0)
                return orderList;

            SoapObject newDataSet = (SoapObject) diffgram.getProperty("DocumentElement");
            int newDataSetCount = newDataSet.getPropertyCount();

            String output = "";

            for (int j = 0; j < newDataSetCount; j++) {
                SoapObject table = (SoapObject) newDataSet.getProperty(j);

                int slotId = 0;
                String orderId = "", custName = "", tankNo = "", prodName = "", rackNo = "";

                if (!table.getProperty("TimeSlotID").toString().equals("anyType{}"))
                    slotId = Integer.valueOf(table.getProperty("TimeSlotID").toString());

                if (!table.getProperty("OrderID").toString().equals("anyType{}"))
                    orderId = table.getProperty("OrderID").toString();

                if (!table.getProperty("CustomerName").toString().equals("anyType{}"))
                    custName = table.getProperty("CustomerName").toString();

                if (!table.getProperty("ProductName").toString().equals("anyType{}"))
                    prodName = table.getProperty("ProductName").toString();

                if (!table.getProperty("TankNo").toString().equals("anyType{}"))
                    tankNo = table.getProperty("TankNo").toString();

                if (!table.getProperty("TruckRackNo").toString().equals("anyType{}"))
                    rackNo = table.getProperty("TruckRackNo").toString();

                orderList.add(new Order(slotId, orderId, custName, tankNo, prodName, rackNo));
            }
        }

        return orderList;
    }

    //Get 5 min late orders
    public static List<Order> invokeRetrieveLateListWS(Date bookingDate, String rackNo) {

        List<Order> orderList = new ArrayList<Order>();

        String webMethName = ConstantWS.WS_ORDER_GET_LATE;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("bookingDate");
        // Set Value
        datePI.setValue(bookingDate);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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
                orderList = getElementsFromOrderListSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return orderList;
    }

    //Get Timeslot With Pending Key In
    public static List<Order> invokeRetrievePendingKeyInListWS(Date date, String rackNo) {

        List<Order> orderList = new ArrayList<Order>();

        String webMethName = ConstantWS.WS_ORDER_GET_PENDING_KEYIN;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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
                orderList = getElementsFromOrderListSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return orderList;
    }

    //Get Timeslot With Key In
    public static List<Order> invokeRetrieveKeyInListWS(Date date, String rackNo) {

        List<Order> orderList = new ArrayList<Order>();

        String webMethName = ConstantWS.WS_ORDER_GET_KEYIN;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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
                orderList = getElementsFromOrderListSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return orderList;
    }

    //Get Timeslot With Weigh In
    public static List<Order> invokeRetrieveWeighInListWS(Date date, String rackNo) {

        List<Order> orderList = new ArrayList<Order>();

        String webMethName = ConstantWS.WS_ORDER_GET_WEIGHTIN;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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
                orderList = getElementsFromOrderListSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return orderList;
    }

    //Get Timeslot With Pump In
    public static List<Order> invokeRetrievePumpInListWS(Date date, String rackNo) {

        List<Order> orderList = new ArrayList<Order>();

        String webMethName = ConstantWS.WS_ORDER_GET_PUMPIN;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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
                orderList = getElementsFromOrderListSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return orderList;
    }

    //Get Timeslot With Pump Out
    public static List<Order> invokeRetrievePumpOutListWS(Date date, String rackNo) {

        List<Order> orderList = new ArrayList<Order>();

        String webMethName = ConstantWS.WS_ORDER_GET_PUMPOUT;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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
                orderList = getElementsFromOrderListSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return orderList;
    }

    //Get Timeslot With Weigh Out
    public static List<Order> invokeRetrieveWeighOutListWS(Date date, String rackNo) {

        List<Order> orderList = new ArrayList<Order>();

        String webMethName = ConstantWS.WS_ORDER_GET_WEIGHTOUT;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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
                orderList = getElementsFromOrderListSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }

        return orderList;
    }

    //Get Timeslot With Key Out / Departure
    public static List<Order> invokeRetrieveKeyOutListWS(Date date, String rackNo) {

        List<Order> orderList = new ArrayList<Order>();

        String webMethName = ConstantWS.WS_ORDER_GET_KEYOUT;
        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, webMethName);

        PropertyInfo datePI = new PropertyInfo();
        // Set Name
        datePI.setName("date");
        // Set Value
        datePI.setValue(date);
        // Set dataType
        datePI.setType(Date.class);
        // Add the property to request object
        request.addProperty(datePI);

        PropertyInfo rackPI = new PropertyInfo();
        // Set Name
        rackPI.setName("str");
        // Set Value
        rackPI.setValue(rackNo);
        // Set dataType
        rackPI.setType(String.class);
        // Add the property to request object
        request.addProperty(rackPI);

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
                orderList = getElementsFromOrderListSOAP(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
        }
        return orderList;
    }
}
