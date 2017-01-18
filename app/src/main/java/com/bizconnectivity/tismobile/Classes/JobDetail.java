package com.bizconnectivity.tismobile.classes;

import java.io.Serializable;

public class JobDetail implements Serializable{

	private String jobID;
	private String customerName;
	private String productName;
	private String tankNo;
	private String loadingBayNo;
	private String loadingArm;
	private String sdsFilePath;
	private String operatorID;
	private String driverID;
	private String workInstruction;
	private String pumpStartTime;
	private String pumpStopTime;
	private String rackOutTime;
	private String jobStatus;
	private String jobDate;

	public String getJobID() {
		return jobID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getTankNo() {
		return tankNo;
	}

	public void setTankNo(String tankNo) {
		this.tankNo = tankNo;
	}

	public String getLoadingBayNo() {
		return loadingBayNo;
	}

	public void setLoadingBayNo(String loadingBayNo) {
		this.loadingBayNo = loadingBayNo;
	}

	public String getLoadingArm() {
		return loadingArm;
	}

	public void setLoadingArm(String loadingArm) {
		this.loadingArm = loadingArm;
	}

	public String getSdsFilePath() {
		return sdsFilePath;
	}

	public void setSdsFilePath(String sdsFilePath) {
		this.sdsFilePath = sdsFilePath;
	}

	public String getDriverID() {
		return driverID;
	}

	public void setDriverID(String driverID) {
		this.driverID = driverID;
	}

	public String getOperatorID() {
		return operatorID;
	}

	public void setOperatorID(String operatorID) {
		this.operatorID = operatorID;
	}

	public String getWorkInstruction() {
		return workInstruction;
	}

	public void setWorkInstruction(String workInstruction) {
		this.workInstruction = workInstruction;
	}

	public String getPumpStartTime() {
		return pumpStartTime;
	}

	public void setPumpStartTime(String pumpStartTime) {
		this.pumpStartTime = pumpStartTime;
	}

	public String getPumpStopTime() {
		return pumpStopTime;
	}

	public void setPumpStopTime(String pumpStopTime) {
		this.pumpStopTime = pumpStopTime;
	}

	public String getRackOutTime() {
		return rackOutTime;
	}

	public void setRackOutTime(String rackOutTime) {
		this.rackOutTime = rackOutTime;
	}

	public String getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}

	public String getJobDate() {
		return jobDate;
	}

	public void setJobDate(String jobDate) {
		this.jobDate = jobDate;
	}

}
