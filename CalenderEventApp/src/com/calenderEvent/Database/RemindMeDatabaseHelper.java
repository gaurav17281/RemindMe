package com.calenderEvent.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RemindMeDatabaseHelper extends SQLiteOpenHelper {

	final Context calledActivity;
	private static final String DATABASE_NAME = "remindmetable.db";
	private static final int DATABASE_VERSION = 1;

	public RemindMeDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		calledActivity = context;
	}

	// Method is called during creation of the database
	@Override
	public void onCreate(SQLiteDatabase database) {
		RemindMeTable.onCreate(database);
	}

	// Method is called during an upgrade of the database,
	// e.g. if you increase the database version
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		RemindMeTable.onUpgrade(database, oldVersion, newVersion);
	}

	
}
