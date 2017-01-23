package com.bizconnectivity.tismobile.webservices;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class SealNoWS {

	public static ArrayList<String> invokeRetrieveSealNo(String timeslotID) {

		ArrayList<String> sealNoArrayList = new ArrayList<>();

		//create request
		SoapObject request = new SoapObject(ConstantWS.NAMESPACE, ConstantWS.WS_TS_GET_SEAL_NO);

		//property which holds input parameters
		PropertyInfo propertyInfo = new PropertyInfo();
		//set name
		propertyInfo.setName("timeslotID");
		//set value
		propertyInfo.setValue(timeslotID);
		//set datatype
		propertyInfo.setType(String.class);
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
			androidHttpTransport.call(ConstantWS.SOAP_ACTION + ConstantWS.WS_TS_GET_PRODUCT_PPE, envelope);

			if (envelope.bodyIn instanceof SoapFault) {

				sealNoArrayList.clear();

			} else {

				SoapObject response = (SoapObject) envelope.bodyIn;
				sealNoArrayList = getElementFromSealNo(response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sealNoArrayList;
	}

	public static ArrayList<String> getElementFromSealNo(SoapObject response){

		ArrayList<String> sealNoArrayList = new ArrayList<>();

		//get the desired property
		SoapObject soapObject = (SoapObject) response.getProperty(0);

		//retrieve diffgram from property
		SoapObject diffGram = (SoapObject) soapObject.getProperty("diffgram");

		//retrieve document element from diffgram
		SoapObject documentElement = (SoapObject) diffGram.getProperty("DocumentElement");

		//get the number of dataset
		int newDataSetCount = documentElement.getPropertyCount();

		for (int i = 0; i < newDataSetCount; i++) {

			//retrieve data from dataset
			SoapObject table = (SoapObject) documentElement.getProperty(i);

			if (!table.getProperty("SealNo").toString().equals("anyType{}")) {

				sealNoArrayList.add(table.getProperty("SealNo").toString());
			}
		}

		return sealNoArrayList;
	}
}
