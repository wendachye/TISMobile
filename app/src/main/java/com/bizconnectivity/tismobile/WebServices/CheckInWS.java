package com.bizconnectivity.tismobile.webservices;


import android.util.Log;

import com.bizconnectivity.tismobile.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import static com.bizconnectivity.tismobile.webservices.ConstantWS.NAMESPACE;
import static com.bizconnectivity.tismobile.webservices.ConstantWS.SOAP_ACTION;
import static com.bizconnectivity.tismobile.webservices.ConstantWS.URL;
import static com.bizconnectivity.tismobile.webservices.ConstantWS.WS_CHECK_TRUCK_RACK_EXISTS;

public class CheckInWS {

	public static boolean invokeCheckTruckRack(String rackNo) {

		boolean result = false;

		SoapObject request = new SoapObject(NAMESPACE, WS_CHECK_TRUCK_RACK_EXISTS);

		PropertyInfo propertyInfo = new PropertyInfo();
		// Set Name
		propertyInfo.setName("rackNo");
		// Set Value
		propertyInfo.setValue(rackNo);
		// Set dataType
		propertyInfo.setType(String.class);
		// Add the property to request object
		request.addProperty(propertyInfo);

		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

		try {
			androidHttpTransport.call(SOAP_ACTION + WS_CHECK_TRUCK_RACK_EXISTS, envelope);

			if (envelope.bodyIn instanceof SoapFault) {

				result = false;

			} else {

				SoapObject response = (SoapObject) envelope.bodyIn;

				if (response.getProperty(0).toString().equals("true")) {

					result = true;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return result;
	}

}
