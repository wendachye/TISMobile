package com.bizconnectivity.tismobile.Database.DataSources;

import android.database.sqlite.SQLiteDatabase;

import com.bizconnectivity.tismobile.Database.DatabaseSQLHelper;

public class GHSDetailDataSource {

	private SQLiteDatabase database;
	private DatabaseSQLHelper sqlHelper;

	public void open() {

		database = sqlHelper.getWritableDatabase();
	}

	public void close() {

		sqlHelper.close();
	}
}
