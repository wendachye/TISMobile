package com.bizconnectivity.tismobile.webservices;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class DepartureWS {

	public static boolean invokeAddDepartureWS(String TimeSlotID, String rackOutTime, String UpdatedBy) {

		boolean returnResult = false;

		SoapObject request = new SoapObject(ConstantWS.NAMESPACE, ConstantWS.WS_TS_CREATE_DEPARTURE_TIME);

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
		PropertyInfo propertyInfoROT = new PropertyInfo();
		// Set Name
		propertyInfoROT.setName("rackOutTime");
		// Set Value
		propertyInfoROT.setValue(rackOutTime);
		// Set dataType
		propertyInfoROT.setType(String.class);
		// Add the property to request object
		request.addProperty(propertyInfoROT);

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
			androidHttpTransport.call(ConstantWS.SOAP_ACTION + ConstantWS.WS_TS_CREATE_DEPARTURE_TIME, envelope);

			if (envelope.bodyIn instanceof SoapFault) {

				SoapFault response = (SoapFault) envelope.bodyIn;

			} else {

				SoapObject response = (SoapObject) envelope.bodyIn;
				SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

				if (responseProperty.toString().equals("true")) {

					returnResult =  true;
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return returnResult;
	}
}
