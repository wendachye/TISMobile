package com.bizconnectivity.tismobile.webservices;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.bizconnectivity.tismobile.webservices.ConstantWS.NAMESPACE;
import static com.bizconnectivity.tismobile.webservices.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.tismobile.webservices.ConstantWS.WS_VALIDATE_AD_USER;

public class UserWS {

    public static boolean invokeLoginWS (String username,String password) {

        boolean returnResult = false;

        SoapObject request = new SoapObject(NAMESPACE, WS_VALIDATE_AD_USER);

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
            androidHttpTransport.call(SOAP_ACTION + WS_VALIDATE_AD_USER, envelope);

            if(envelope.bodyIn instanceof SoapFault) {

                returnResult = false;

            } else {

                SoapObject response = (SoapObject) envelope.bodyIn;
                SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

                if (responseProperty.toString().equals("True")) {

                    returnResult = true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnResult;
    }

}
