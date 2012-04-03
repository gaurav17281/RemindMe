package com.calenderEvent.Database;

import java.util.ArrayList;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class RemindMeTable {

	// Database table
	public static final String TABLE_REMINDME = "remindme";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PRIORTY = "priority";
	public static final String COLUMN_SUMMARY = "summary";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_TIME = "time";
	public static final String COLUMN_CATEGORY = "category";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_REMINDME + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_PRIORTY
			+ " text , " + COLUMN_SUMMARY + " text not null,"
			+ COLUMN_DESCRIPTION + " text not null ," + COLUMN_DATE + " text ,"
			+ COLUMN_TIME + " text ," + COLUMN_CATEGORY + " text " + ");";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(RemindMeTable.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDME);
		onCreate(database);
	}
 
}
