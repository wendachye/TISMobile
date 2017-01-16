package com.bizconnectivity.tismobile.webservices;

import android.util.Log;

import com.bizconnectivity.tismobile.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class TechnicianIDWS {

	public static boolean invokeTechnicianIDWS(String nric)
	{
		boolean result = false;

		SoapObject request = new SoapObject(ConstantWS.NAMESPACE, ConstantWS.WS_CHECK_TECHNICIAN_NRIC);

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
			androidHttpTransport.call(ConstantWS.SOAP_ACTION + ConstantWS.WS_CHECK_TECHNICIAN_NRIC, envelope);

			if(envelope.bodyIn instanceof SoapFault)
			{
				SoapFault response = (SoapFault)envelope.bodyIn;
				Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
			}
			else
			{
				SoapObject response = (SoapObject) envelope.bodyIn;
				SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

				String responseWS = responseProperty.toString();

				if (responseWS.equals("true")) {

					result = true;

				} else {

					result = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
		}

		return result;
	}
}
