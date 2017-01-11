package com.bizconnectivity.tismobile.WebServices;

import android.util.Log;

import com.bizconnectivity.tismobile.Common;
import com.bizconnectivity.tismobile.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class UserWS {

    public static boolean getElementsFromSuccessSOAP(SoapObject response){

        boolean success = false;
        int elementCount = response.getPropertyCount();

        if(elementCount > 0)
        {
            success = Boolean.valueOf(response.getProperty(0).toString());
        }

        return success;
    }

    public static boolean invokeLoginWS (String username,String password) {

        boolean result = false;

        SoapObject request = new SoapObject(ConstantWS.NAMESPACE, ConstantWS.WS_VALIDATE_AD_USER);

        PropertyInfo usernamePI = new PropertyInfo();
        // Set Name
        usernamePI.setName("Username");
        // Set Value
        usernamePI.setValue(username);
        // Set dataType
        usernamePI.setType(String.class);
        // Add the property to request object
        request.addProperty(usernamePI);

        PropertyInfo passwordPI = new PropertyInfo();
        // Set Name
        passwordPI.setName("Password");
        // Set Value
        passwordPI.setValue(password);
        // Set dataType
        passwordPI.setType(String.class);
        // Add the property to request object
        request.addProperty(passwordPI);

        // Create envelope
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;
        // Set output SOAP object
        envelope.setOutputSoapObject(request);
        // Create HTTP call object
        HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

        try {
            androidHttpTransport.call(ConstantWS.SOAP_ACTION + ConstantWS.WS_VALIDATE_AD_USER, envelope);

            if(envelope.bodyIn instanceof SoapFault) {

                SoapFault response = (SoapFault)envelope.bodyIn;

            } else {

                SoapObject response = (SoapObject) envelope.bodyIn;
                SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

                if (responseProperty.equals(true)) {

                    result = true;

                } else {

                    result = false;
                }
//                success = getElementsFromSuccessSOAP(response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
