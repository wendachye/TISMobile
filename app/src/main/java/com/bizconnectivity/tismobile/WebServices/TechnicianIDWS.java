package com.bizconnectivity.tismobile.webservices;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.bizconnectivity.tismobile.webservices.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.tismobile.webservices.ConstantWS.WS_CHECK_TECHNICIAN_NRIC;

public class TechnicianIDWS {

	public static boolean invokeTechnicianIDWS(String nric) {

		boolean returnResult = false;

		SoapObject request = new SoapObject(ConstantWS.NAMESPACE, WS_CHECK_TECHNICIAN_NRIC);

		PropertyInfo nricPI = new PropertyInfo();
		// Set Name
		nricPI.setName("nric");
		// Set Value
		nricPI.setValue(nric);
		// Set dataType
		nricPI.setType(String.class);
		// Add the property to request object
		request.addProperty(nricPI);

		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

		try {
			androidHttpTransport.call(SOAP_ACTION + WS_CHECK_TECHNICIAN_NRIC, envelope);

			if(envelope.bodyIn instanceof SoapFault)
			{
				returnResult = false;
			}
			else
			{
				SoapObject response = (SoapObject) envelope.bodyIn;
				SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);
				Log.d("aa", response.toString());
				String responseWS = responseProperty.toString();

				if (responseWS.equals("true")) {

					returnResult = true;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return returnResult;
	}
}
