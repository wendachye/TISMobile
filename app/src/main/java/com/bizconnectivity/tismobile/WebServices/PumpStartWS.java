package com.bizconnectivity.tismobile.WebServices;

import android.util.Log;

import com.bizconnectivity.tismobile.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.MarshalDate;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Date;

public class PumpStartWS {

	public static boolean invokeUpdatePumpStartWS(int TimeSlotID, String UpdatedBy) {

		SoapObject request = new SoapObject(ConstantWS.NAMESPACE, ConstantWS.WS_TS_CREATE_PUMPSTART_TIME);

		// Property which holds input parameters
		PropertyInfo propertyInfoTSID = new PropertyInfo();
		// Set Name
		propertyInfoTSID.setName("TimeSlotID");
		// Set Value
		propertyInfoTSID.setValue(TimeSlotID);
		// Set dataType
		propertyInfoTSID.setType(int.class);
		// Add the property to request object
		request.addProperty(propertyInfoTSID);

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
			androidHttpTransport.call(ConstantWS.SOAP_ACTION + ConstantWS.WS_TS_CREATE_PUMPSTART_TIME, envelope);

			if (envelope.bodyIn instanceof SoapFault) {
				SoapFault response = (SoapFault) envelope.bodyIn;
				Log.d(Constant.TEXT_ERROR, Constant.TEXT_ERROR_MSG + response.toString());
			} else {
				SoapObject response = (SoapObject) envelope.bodyIn;
				SoapPrimitive responseProperty = (SoapPrimitive) response.getProperty(0);

				String result = responseProperty.toString();
				if (result.equals("true")) {
					return true;
				} else {
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(Constant.TEXT_EXCEPTION, e.getLocalizedMessage());
		}

		return false;
	}
}
