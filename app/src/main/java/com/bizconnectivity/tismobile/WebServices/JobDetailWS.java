package com.bizconnectivity.tismobile.webservices;

import android.util.Log;

import com.bizconnectivity.tismobile.database.models.JobDetail;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import static com.bizconnectivity.tismobile.Constant.*;
import static com.bizconnectivity.tismobile.webservices.ConstantWS.*;

public class JobDetailWS {

	public static ArrayList<JobDetail> invokeRetrieveAllJobs (String trunkRackNo) {

		ArrayList<JobDetail> jobDetailArrayList = new ArrayList<>();

		//create request
		SoapObject request = new SoapObject(NAMESPACE, WS_GET_ALL_JOB_DETAILS);

		//property which holds input parameters
		PropertyInfo propertyInfoTRN = new PropertyInfo();
		//set name
		propertyInfoTRN.setName("truckRackNo");
		//set value
		propertyInfoTRN.setValue(trunkRackNo);
		//set datatype
		propertyInfoTRN.setType(String.class);
		//add the property to request object
		request.addProperty(propertyInfoTRN);

		//create envelope
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;

		//set output SOAP object
		envelope.setOutputSoapObject(request);
		//create HTTP call object
		HttpTransportSE androidHTTPTransport = new HttpTransportSE(ConstantWS.URL);


		try {
			//invoke web service
			androidHTTPTransport.call(SOAP_ACTION + WS_GET_ALL_JOB_DETAILS, envelope);

			if(envelope.bodyIn instanceof SoapFault) {

				jobDetailArrayList.clear();

			} else {

				SoapObject response = (SoapObject) envelope.bodyIn;

				jobDetailArrayList = getElementFromJobDetail(response);
			}

		} catch (Exception e) {

			e.printStackTrace();
		}

		return jobDetailArrayList;
	}

	public static ArrayList<JobDetail> getElementFromJobDetail (SoapObject response) {

		ArrayList<JobDetail> jobDetailArrayList = new ArrayList<>();

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

				JobDetail jobDetail = new JobDetail();

				//retrieve data from dataset
				SoapObject table = (SoapObject) documentElement.getProperty(i);

				if (!table.getProperty("TimeSlotID").toString().equals("anyType{}")) {
					jobDetail.setJobID(table.getProperty("TimeSlotID").toString());
				} else {
					jobDetail.setJobID("");
				}

				if (!table.getProperty("CustomerName").toString().equals("anyType{}")) {
					jobDetail.setCustomerName(table.getProperty("CustomerName").toString());
				} else {
					jobDetail.setCustomerName("");
				}

				if (!table.getProperty("ProductName").toString().equals("anyType{}")) {
					jobDetail.setProductName(table.getProperty("ProductName").toString());
				} else {
					jobDetail.setProductName("");
				}

				if (!table.getProperty("TankNo").toString().equals("anyType{}")) {
					jobDetail.setTankNo(table.getProperty("TankNo").toString());
				} else {
					jobDetail.setTankNo("");
				}

				if (!table.getProperty("LoadingBayNo").toString().equals("anyType{}")) {
					Log.d("LOG", "aa" + table.getProperty("LoadingBayNo").toString());
					jobDetail.setLoadingBayNo(table.getProperty("LoadingBayNo").toString());
				} else {
					jobDetail.setLoadingBayNo("");
				}

				if (!table.getProperty("LoadingArm").toString().equals("anyType{}")) {
					jobDetail.setLoadingArm(table.getProperty("LoadingArm").toString());
				} else {
					jobDetail.setLoadingArm("");
				}

				if (!table.getProperty("SDSFile").toString().equals("anyType{}")) {
					jobDetail.setSdsFilePath(table.getProperty("SDSFile").toString());
				} else {
					jobDetail.setSdsFilePath("");
				}

				if (!table.getProperty("DriverID").toString().equals("anyType{}")) {
					jobDetail.setDriverID(table.getProperty("DriverID").toString());
				} else {
					jobDetail.setDriverID("");
				}

				if (!table.getProperty("OperatorID").toString().equals("anyType{}")) {
					jobDetail.setOperatorID(table.getProperty("OperatorID").toString());
				} else {
					jobDetail.setOperatorID("");
				}

				if (!table.getProperty("BookingDate").toString().equals("anyType{}")) {

					try {

						Date date = simpleDateFormat.parse(table.getProperty("BookingDate").toString());
						String bookingDate = simpleDateFormat.format(date);

						jobDetail.setJobDate(bookingDate);

					} catch (ParseException e) {
						e.printStackTrace();
					}

				} else {
					jobDetail.setJobDate("");
				}

				jobDetailArrayList.add(jobDetail);
			}
		}

		return jobDetailArrayList;
	}
}
