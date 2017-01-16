package com.bizconnectivity.tismobile.database.DataSources;

import android.database.sqlite.SQLiteDatabase;

import com.bizconnectivity.tismobile.classes.SealDetail;
import com.bizconnectivity.tismobile.database.DatabaseSQLHelper;

public class SealDetailDataSource {

	private SQLiteDatabase database;
	private DatabaseSQLHelper sqlHelper;

	public void open() {

		database = sqlHelper.getWritableDatabase();
	}

	public void close() {

		sqlHelper.close();
	}

	public void insertSealDetails(SealDetail sealDetail) {

	}
}
