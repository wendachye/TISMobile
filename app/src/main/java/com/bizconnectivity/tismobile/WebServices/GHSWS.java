package com.bizconnectivity.tismobile.webservices;

import com.bizconnectivity.tismobile.database.models.GHSDetail;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class GHSWS {

	public static ArrayList<GHSDetail> invokeRetrieveGHSWS (String productName) {

		ArrayList<GHSDetail> ghsArrayList = new ArrayList<>();

		//create request
		SoapObject request = new SoapObject(ConstantWS.NAMESPACE, ConstantWS.WS_TS_GET_PRODUCT_GHS);
		//property which holds input parameters
		PropertyInfo propertyInfo = new PropertyInfo();
		//set name
		propertyInfo.setName("productName");
		//set value
		propertyInfo.setValue(productName);
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
		HttpTransportSE androidHTTPTransport = new HttpTransportSE(ConstantWS.URL);

		try {
			//invoke web service
			androidHTTPTransport.call(ConstantWS.SOAP_ACTION + ConstantWS.WS_TS_GET_PRODUCT_GHS, envelope);

			if(envelope.bodyIn instanceof SoapFault) {

				ghsArrayList.clear();

			} else {

				SoapObject response = (SoapObject) envelope.bodyIn;
				ghsArrayList = getElementFromGHS(response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ghsArrayList;
	}

	private static ArrayList<GHSDetail> getElementFromGHS(SoapObject response) {

		ArrayList<GHSDetail> ghsArrayList = new ArrayList<>();

		//get the desired property
		SoapObject soapObject = (SoapObject) response.getProperty(0);

		//retrieve diffgram from property
		SoapObject diffGram = (SoapObject) soapObject.getProperty("diffgram");
		//retrieve document element from diffgram
		SoapObject documentElement = (SoapObject) diffGram.getProperty("DocumentElement");
		//get the number of dataset
		int newDataSetCount = documentElement.getPropertyCount();

		for (int i = 0; i < newDataSetCount; i++) {

			GHSDetail ghsDetail = new GHSDetail();

			//retrieve data from dataset
			SoapObject table = (SoapObject) documentElement.getProperty(i);

			if (!table.getProperty("GHSPicture").toString().equals("anyType{}")) {

				ghsDetail.setGhsURL(table.getProperty("GHSPicture").toString());

			} else {

				ghsDetail.setGhsURL("");
			}

			ghsArrayList.add(ghsDetail);
		}

		return ghsArrayList;
	}
}
