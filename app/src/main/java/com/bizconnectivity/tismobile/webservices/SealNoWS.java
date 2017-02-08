package com.bizconnectivity.tismobile.webservices;

import com.bizconnectivity.tismobile.database.models.SealDetail;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class SealNoWS {

	public static ArrayList<SealDetail> invokeRetrieveSealNo(String timeslotID) {

		ArrayList<SealDetail> sealNoArrayList = new ArrayList<>();

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
			androidHttpTransport.call(ConstantWS.SOAP_ACTION + ConstantWS.WS_TS_GET_SEAL_NO, envelope);

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

	public static ArrayList<SealDetail> getElementFromSealNo(SoapObject response){

		ArrayList<SealDetail> sealNoArrayList = new ArrayList<>();

		//get the desired property
		SoapObject soapObject = (SoapObject) response.getProperty(0);

		//retrieve diffgram from property
		SoapObject diffGram = (SoapObject) soapObject.getProperty("diffgram");

		if (!soapObject.getProperty("diffgram").toString().equals("anyType{}")) {

			//retrieve document element from diffgram
			SoapObject documentElement = (SoapObject) diffGram.getProperty("DocumentElement");

			//get the number of dataset
			int newDataSetCount = documentElement.getPropertyCount();

			for (int i = 0; i < newDataSetCount; i++) {

				SealDetail sealDetail = new SealDetail();

				//retrieve data from dataset
				SoapObject table = (SoapObject) documentElement.getProperty(i);

				if (!table.getProperty("SealNo").toString().equals("anyType{}")) {

					sealDetail.setSealNo(table.getProperty("SealNo").toString());

				} else {

					sealDetail.setSealNo("");
				}

				sealNoArrayList.add(sealDetail);
			}
		}

		return sealNoArrayList;
	}
}
