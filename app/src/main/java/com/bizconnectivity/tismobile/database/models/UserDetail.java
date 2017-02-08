package com.bizconnectivity.tismobile.database.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class UserDetail extends RealmObject {

	@PrimaryKey
	private String username;
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
