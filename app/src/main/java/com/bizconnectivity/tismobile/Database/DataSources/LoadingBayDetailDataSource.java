package com.bizconnectivity.tismobile.Database.DataSources;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.bizconnectivity.tismobile.Database.DatabaseSQLHelper;

public class LoadingBayDetailDataSource {

	private SQLiteDatabase database;
	private DatabaseSQLHelper sqlHelper;

	public LoadingBayDetailDataSource(Context context) {

		sqlHelper = new DatabaseSQLHelper(context);
	}

	public void open() {

		database = sqlHelper.getWritableDatabase();
	}

	public void close() {

		sqlHelper.close();
	}
}
