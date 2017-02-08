package com.bizconnectivity.tismobile.database.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class LoadingBayDetail extends RealmObject{

	@PrimaryKey
	private String loadingBayNo;
	private String status;

	public String getLoadingBayNo() {
		return loadingBayNo;
	}

	public void setLoadingBayNo(String loadingBayNo) {
		this.loadingBayNo = loadingBayNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
