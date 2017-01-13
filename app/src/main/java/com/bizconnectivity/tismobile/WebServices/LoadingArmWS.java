package com.bizconnectivity.tismobile.WebServices;

import android.util.Log;

import com.bizconnectivity.tismobile.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class LoadingArmWS {

	public static boolean invokeCheckLoadingArmWS(String timeslotId, String truckRackArmNo) {

		SoapObject request = new SoapObject(ConstantWS.NAMESPACE, ConstantWS.WS_TS_CHECK_LOADING_ARM);

		// Property which holds input parameters
		PropertyInfo propertyInfoTSID = new PropertyInfo();
		// Set Name
		propertyInfoTSID.setName("timeslotId");
		// Set Value
		propertyInfoTSID.setValue(Integer.parseInt(timeslotId));
		// Set dataType
		propertyInfoTSID.setType(int.class);
		// Add the property to request object
		request.addProperty(propertyInfoTSID);

		// Property which holds input parameters
		PropertyInfo propertyInfoTRA = new PropertyInfo();
		// Set Name
		propertyInfoTRA.setName("truckRackArmNo");
		// Set Value
		propertyInfoTRA.setValue(truckRackArmNo);
		// Set dataType
		propertyInfoTRA.setType(String.class);
		// Add the property to request object
		request.addProperty(propertyInfoTRA);

		// Create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		// Set output SOAP object
		envelope.setOutputSoapObject(request);
		// Create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

		try {
			androidHttpTransport.call(ConstantWS.SOAP_ACTION + ConstantWS.WS_TS_CHECK_LOADING_ARM, envelope);

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
