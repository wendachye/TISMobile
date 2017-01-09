package com.bizconnectivity.tismobile.Classes;

import android.graphics.Bitmap;

public class PPEDetail {

	int jobID;
	String ppeImageURL;
	String ghsImageURL;
	Bitmap ppeImage;
	Bitmap ghsImage;

	public int getJobID() {
		return jobID;
	}

	public void setJobID(int jobID) {
		this.jobID = jobID;
	}

	public String getPpeImageURL() {
		return ppeImageURL;
	}

	public void setPpeImageURL(String ppeImageURL) {
		this.ppeImageURL = ppeImageURL;
	}

	public String getGhsImageURL() {
		return ghsImageURL;
	}

	public void setGhsImageURL(String ghsImageURL) {
		this.ghsImageURL = ghsImageURL;
	}

	public Bitmap getPpeImage() {
		return ppeImage;
	}

	public void setPpeImage(Bitmap ppeImage) {
		this.ppeImage = ppeImage;
	}

	public Bitmap getGhsImage() {
		return ghsImage;
	}

	public void setGhsImage(Bitmap ghsImage) {
		this.ghsImage = ghsImage;
	}
}
