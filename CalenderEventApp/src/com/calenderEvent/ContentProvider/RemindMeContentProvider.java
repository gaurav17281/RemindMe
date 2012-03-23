package com.calenderEvent.ContentProvider;

import java.util.Arrays;
import java.util.HashSet;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.calenderEvent.Database.RemindMeDatabaseHelper;
import com.calenderEvent.Database.RemindMeTable;

public class RemindMeContentProvider extends ContentProvider {

	// database
	private RemindMeDatabaseHelper database;

	// Used for the UriMacher
	private static final int REMINDME = 10;
	private static final int REMINDME_ID = 20;

	private static final String AUTHORITY = "com.calenderEvent.ContentProvider";

	private static final String BASE_PATH = "remindme";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/remindmedir";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/remindme";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, REMINDME);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", REMINDME_ID);
	}

	@Override
	public boolean onCreate() {
		database = new RemindMeDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		// Uisng SQLiteQueryBuilder instead of query() method
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

		// Check if the caller has requested a column which does not exists
		checkColumns(projection);

		// Set the table
		queryBuilder.setTables(RemindMeTable.TABLE_REMINDME);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case REMINDME:
			break;
		case REMINDME_ID:
			// Adding the ID to the original query
			queryBuilder.appendWhere(RemindMeTable.COLUMN_ID + "="
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}

		SQLiteDatabase db = database.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		// Make sure that potential listeners are getting notified
		cursor.setNotificationUri(getContext().getContentResolver(), uri);

		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		long id = 0;
		switch (uriType) {
		case REMINDME:
			id = sqlDB.insert(RemindMeTable.TABLE_REMINDME, null, values);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsDeleted = 0;
		switch (uriType) {
		case REMINDME:
			rowsDeleted = sqlDB.delete(RemindMeTable.TABLE_REMINDME, selection,
					selectionArgs);
			break;
		case REMINDME_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(RemindMeTable.TABLE_REMINDME,
						RemindMeTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(RemindMeTable.TABLE_REMINDME,
						RemindMeTable.COLUMN_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = database.getWritableDatabase();
		int rowsUpdated = 0;
		switch (uriType) {
		case REMINDME:
			rowsUpdated = sqlDB.update(RemindMeTable.TABLE_REMINDME, values,
					selection, selectionArgs);
			break;
		case REMINDME_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(RemindMeTable.TABLE_REMINDME,
						values, RemindMeTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsUpdated = sqlDB.update(RemindMeTable.TABLE_REMINDME,
						values, RemindMeTable.COLUMN_ID + "=" + id + " and "
								+ selection, selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException("Unknown URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	private void checkColumns(String[] projection) {
		String[] available = { RemindMeTable.COLUMN_CATEGORY,
				RemindMeTable.COLUMN_SUMMARY, RemindMeTable.COLUMN_DESCRIPTION,
				RemindMeTable.COLUMN_ID, RemindMeTable.COLUMN_CATEGORY,
				RemindMeTable.COLUMN_PRIORTY, RemindMeTable.COLUMN_DATE,
				RemindMeTable.COLUMN_TIME };
		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));
			// Check if all columns which are requested are available
			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException(
						"Unknown columns in projection");
			}
		}
	}

}
