package com.bizconnectivity.tismobile.database.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class PPEDetail extends RealmObject {

	@PrimaryKey
	private int ppeID;
	private String ppeName;
	private String ppeURL;
	@Index
	private String jobID;

	public int getPpeID() {
		return ppeID;
	}

	public void setPpeID(int ppeID) {
		this.ppeID = ppeID;
	}

	public String getPpeName() {
		return ppeName;
	}

	public void setPpeName(String ppeName) {
		this.ppeName = ppeName;
	}

	public String getPpeURL() {
		return ppeURL;
	}

	public void setPpeURL(String ppeURL) {
		this.ppeURL = ppeURL;
	}

	public String getJobID() {
		return jobID;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
}
