package com.bizconnectivity.tismobile.webservices;

import android.util.Log;

import com.bizconnectivity.tismobile.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class WorkInstructionWS {

	public static String invokeRetrieveWorkInstruction(String timeslotId, String WI) {

		String workInstruction = "";

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

			} else {
				SoapObject response = (SoapObject) envelope.bodyIn;

				SoapObject so1 = (SoapObject) response.getProperty(0);
				//retrieve diffGram from property
				SoapObject diffGram = (SoapObject) so1.getProperty("diffgram");
				//retrieve document element from diffGram
				SoapObject newDataSet = (SoapObject) diffGram.getProperty("DocumentElement");
				//get the number of dataSet
				SoapObject table = (SoapObject) newDataSet.getProperty(0);

				if (!table.getProperty("TimeSlotID").toString().equals("anyType{}")) {
					workInstruction = table.getProperty("TimeSlotID").toString();
				} else {
					workInstruction = "";
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		if (!workInstruction.isEmpty()) {

			if (workInstruction.equals(WI)) {

				return workInstruction;

			} else {

				workInstruction = "";
				return workInstruction;
			}

		} else {
			return workInstruction;
		}
	}
}
