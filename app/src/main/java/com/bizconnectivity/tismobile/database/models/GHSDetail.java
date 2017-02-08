package com.bizconnectivity.tismobile.database.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class GHSDetail extends RealmObject {

	@PrimaryKey
	private int ghsID;
	private String ghsName;
	private String ghsURL;
	@Index
	private String jobID;

	public int getGhsID() {
		return ghsID;
	}

	public void setGhsID(int ghsID) {
		this.ghsID = ghsID;
	}

	public String getGhsName() {
		return ghsName;
	}

	public void setGhsName(String ghsName) {
		this.ghsName = ghsName;
	}

	public String getGhsURL() {
		return ghsURL;
	}

	public void setGhsURL(String ghsURL) {
		this.ghsURL = ghsURL;
	}

	public String getJobID() {
		return jobID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
}
