package com.calenderEvent;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ListView;

import com.calenderEvent.controller.Controller;
import com.calenderEvent.controller.CustomCursorAdapter;

public class ContactPicker extends Activity {
	Controller controller;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactpicker);

		String[] from = {};
		int[] to = {};

		CustomCursorAdapter adapter = new CustomCursorAdapter(this,
				R.layout.rowlayout, from, to, controller, getContacts());
		ListView contactView = (ListView) findViewById(R.id.contactview);
		contactView.setAdapter(adapter);
	}

	private Cursor getContacts() {
		// Run query
		/*Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME,ContactsContract.Contacts._ID };
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '"
				+ ("1") + "'";
		String[] selectionArgs = null;
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME
				+ " COLLATE LOCALIZED ASC";

		return managedQuery(uri, projection, selection, selectionArgs,
				sortOrder);

	*/
	     ContentResolver cr = getContentResolver();
	     return cr.query(ContactsContract.Contacts.CONTENT_URI,
	                null, null, null, null);
	
	}

}