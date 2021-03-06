package com.bizconnectivity.tismobile.webservices;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

public class SDSWS {

	public static String invokeRetrieveSDSWS (String timeslotID) {

		String sdsURL = "";

		//create request
		SoapObject request = new SoapObject(ConstantWS.NAMESPACE, ConstantWS.WS_TS_GET_SDS_PDF);

		//property which holds input parameters
		PropertyInfo propertyInfo = new PropertyInfo();
		//set name
		propertyInfo.setName("timeslotID");
		//set value
		propertyInfo.setValue(Integer.parseInt(timeslotID));
		//set datatype
		propertyInfo.setType(int.class);
		//add the property to request object
		request.addProperty(propertyInfo);

		//create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		//set output SOAP object
		envelope.setOutputSoapObject(request);
		//create HTTP call object
		HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

		try {
			//invoke web service
			androidHttpTransport.call(ConstantWS.SOAP_ACTION + ConstantWS.WS_TS_GET_SDS_PDF, envelope);

			if(envelope.bodyIn instanceof SoapFault) {

				sdsURL = "";

			} else {

				SoapObject response = (SoapObject) envelope.bodyIn;

				SoapObject so1 = (SoapObject) response.getProperty(0);
				//retrieve diffGram from property
				SoapObject diffGram = (SoapObject) so1.getProperty("diffgram");

				//retrieve document element from diffGram
				SoapObject newDataSet = (SoapObject) diffGram.getProperty("DocumentElement");

				//get the number of dataSet
				SoapObject table = (SoapObject) newDataSet.getProperty(0);

				if (!table.getProperty("MSDSfileName").toString().equals("anyType{}")) {

					sdsURL = table.getProperty("MSDSfileName").toString();

				} else {

					sdsURL = "";
				}
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return sdsURL;
	}
}
