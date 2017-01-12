package com.bizconnectivity.tismobile.WebServices;

import android.util.Log;

import com.bizconnectivity.tismobile.Classes.GHS;
import com.bizconnectivity.tismobile.Classes.PPE;
import com.bizconnectivity.tismobile.Constant;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.ArrayList;

public class PPEWS {

	public static ArrayList<PPE> invokeRetrievePPEWS(String productName) {

		ArrayList<PPE> ppeArrayList = new ArrayList<>();

		//create request
		SoapObject request = new SoapObject(ConstantWS.NAMESPACE, ConstantWS.WS_TS_GET_PRODUCT_PPE);

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
		HttpTransportSE androidHttpTransport = new HttpTransportSE(ConstantWS.URL);

		try {
			//invole web service
			androidHttpTransport.call(ConstantWS.SOAP_ACTION + ConstantWS.WS_TS_GET_PRODUCT_PPE, envelope);

			if (envelope.bodyIn instanceof SoapFault) {

				ppeArrayList.clear();

			} else {

				SoapObject response = (SoapObject) envelope.bodyIn;
				ppeArrayList = getElementFromPPE(response);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return ppeArrayList;
	}

	public static ArrayList<PPE> getElementFromPPE(SoapObject response) {

		ArrayList<PPE> ppeArrayList = new ArrayList<>();

		//get the desired property
		SoapObject soapObject = (SoapObject) response.getProperty(0);

		//retrieve diffgram from property
		SoapObject diffGram = (SoapObject) soapObject.getProperty("diffgram");
		//retrieve document element from diffgram
		SoapObject documentElement = (SoapObject) diffGram.getProperty("DocumentElement");
		//get the number of dataset
		int newDataSetCount = documentElement.getPropertyCount();

		for (int i = 0; i < newDataSetCount; i++) {

			PPE ppe = new PPE();

			//retrieve data from dataset
			SoapObject table = (SoapObject) documentElement.getProperty(i);

			if (!table.getProperty("PPEPicture").toString().equals("anyType{}")) {
				ppe.setPpePictureURL(table.getProperty("PPEPicture").toString());
			} else {
				ppe.setPpePictureURL("");
			}

			ppeArrayList.add(ppe);
		}

		return ppeArrayList;
	}
}
