package com.bizconnectivity.tismobile.webservices;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class PumpStartWS {

	public static boolean invokeUpdatePumpStartWS(String TimeSlotID, String pumpStartTime, String UpdatedBy) {

		boolean returnResult = false;

		SoapObject request = new SoapObject(ConstantWS.NAMESPACE, ConstantWS.WS_TS_CREATE_PUMP_START_TIME);

		// Property which holds input parameters
		PropertyInfo propertyInfoTSID = new PropertyInfo();
		// Set Name
		propertyInfoTSID.setName("TimeSlotID");
		// Set Value
		propertyInfoTSID.setValue(Integer.parseInt(TimeSlotID));
		// Set dataType
		propertyInfoTSID.setType(int.class);
		// Add the property to request object
		request.addProperty(propertyInfoTSID);

		// Property which holds input parameters
		PropertyInfo propertyInfoPST = new PropertyInfo();
		// Set Name
		propertyInfoPST.setName("pumpStartTime");
		// Set Value
		propertyInfoPST.setValue(pumpStartTime);
		// Set dataType
		propertyInfoPST.setType(String.class);
		// Add the property to request object
		request.addProperty(propertyInfoPST);

		// Property which holds input parameters
		PropertyInfo propertyInfoUB = new PropertyInfo();
		// Set Name
		propertyInfoUB.setName("UpdatedBy");
		// Set Value
		propertyInfoUB.setValue(UpdatedBy);
		// Set dataType
		propertyInfoUB.setType(String.class);
		// Add the property to request object
		request.addProperty(propertyInfoUB);

		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

		try {
			androidHttpTransport.call(ConstantWS.SOAP_ACTION + ConstantWS.WS_TS_CREATE_PUMP_START_TIME, envelope);

			if (envelope.bodyIn instanceof SoapFault) {

				returnResult = false;

			} else {

				SoapObject response = (SoapObject) envelope.bodyIn;
				SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

				String result = responseProperty.toString();

				if (result.equals("true")) {

					returnResult = true;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return returnResult;
	}
}
