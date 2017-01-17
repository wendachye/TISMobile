package com.bizconnectivity.tismobile.webservices;

import android.util.Log;

import com.bizconnectivity.tismobile.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class DriverIDWS {

	public static boolean invokeRetrieveDriverID(String timeslotId, String idNumber) {

		boolean returnResult = false;

		SoapObject request = new SoapObject(ConstantWS.NAMESPACE, ConstantWS.WS_TS_GET_TIMESLOT);

		PropertyInfo propertyInfo = new PropertyInfo();
		// Set Name
		propertyInfo.setName("timeslotId");
		// Set Value
		propertyInfo.setValue(timeslotId);
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
		HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

		try {
			androidHttpTransport.call(ConstantWS.SOAP_ACTION + ConstantWS.WS_TS_GET_TIMESLOT, envelope);

			if (envelope.bodyIn instanceof SoapFault) {
				SoapFault response = (SoapFault) envelope.bodyIn;
				Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
			} else {
				SoapObject response = (SoapObject) envelope.bodyIn;

				SoapObject so1 = (SoapObject) response.getProperty(0);
				//retrieve diffGram from property
				SoapObject diffGram = (SoapObject) so1.getProperty("diffgram");
				//retrieve document element from diffGram
				SoapObject newDataSet = (SoapObject) diffGram.getProperty("DocumentElement");
				//get the number of dataSet
				SoapObject table = (SoapObject) newDataSet.getProperty(0);

				if (!table.getProperty("IDNumber").toString().equals("anyType{}")) {

					if (idNumber.equals(table.getProperty("IDNumber").toString())) {

						returnResult = true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
		}

		return returnResult;
	}
}
