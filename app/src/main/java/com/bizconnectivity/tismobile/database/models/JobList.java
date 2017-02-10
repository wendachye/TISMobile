package com.bizconnectivity.tismobile.database.models;

import java.util.ArrayList;

public class JobList {

	private String loadingBayNo;
	private ArrayList<JobDetail> jobDetails;

	public String getLoadingBayNo() {
		return loadingBayNo;
	}

	public void setLoadingBayNo(String loadingBayNo) {
		this.loadingBayNo = loadingBayNo;
	}

	public ArrayList<JobDetail> getJobDetails() {
		return jobDetails;
	}

	public void setJobDetails(ArrayList<JobDetail> jobDetails) {
		this.jobDetails = jobDetails;
	}
}
