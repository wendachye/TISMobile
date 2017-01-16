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

public class AddSealWS {

	public static boolean invokeAddSealWS(String sealNo, String TimeSlotID, String sealPos, String UpdatedBy) {

		boolean returnResult = false;

		SoapObject request = new SoapObject(ConstantWS.NAMESPACE, ConstantWS.WS_TS_CREATE_SEAL);

		// Property which holds input parameters
		PropertyInfo propertyInfoSN = new PropertyInfo();
		// Set Name
		propertyInfoSN.setName("sealNo");
		// Set Value
		propertyInfoSN.setValue(sealNo);
		// Set dataType
		propertyInfoSN.setType(String.class);
		// Add the property to request object
		request.addProperty(propertyInfoSN);

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
		PropertyInfo propertyInfoTSP = new PropertyInfo();
		// Set Name
		propertyInfoTSP.setName("sealPos");
		// Set Value
		propertyInfoTSP.setValue(sealPos);
		// Set dataType
		propertyInfoTSP.setType(String.class);
		// Add the property to request object
		request.addProperty(propertyInfoTSP);

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
			androidHttpTransport.call(ConstantWS.SOAP_ACTION + ConstantWS.WS_TS_CREATE_SEAL, envelope);

			if (envelope.bodyIn instanceof SoapFault) {
				SoapFault response = (SoapFault) envelope.bodyIn;
				Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
			} else {
				SoapObject response = (SoapObject) envelope.bodyIn;
				SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

				String result = responseProperty.toString();
				if (result.equals("true")) {

					returnResult =  true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
		}

		return returnResult;
	}
}
