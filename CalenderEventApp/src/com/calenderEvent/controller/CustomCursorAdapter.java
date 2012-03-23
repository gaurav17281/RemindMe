package com.calenderEvent.controller;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calenderEvent.R;
import com.calenderEvent.Database.RemindMeTable;

public class CustomCursorAdapter extends SimpleCursorAdapter {
	private final Activity context;
	private String[] names;
	private final Controller controller;
	private final Cursor contentCursor;
	private final int layout;

	static class ViewHolder {
		public TextView text;
		public ImageView image;
	}

	public CustomCursorAdapter(Activity context, int layout, String[] from,
			int[] to, Controller controller, Cursor c) {
		super(context, layout, c, from, to);
		this.context = context;
		this.controller = controller;
		this.layout = layout;
		this.contentCursor = c;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {

		// Cursor c = getCursor();
		String name = "no name";
		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(layout, parent, false);
		ViewHolder viewHolder = new ViewHolder();
		int nameCol = cursor.getColumnIndex(RemindMeTable.COLUMN_SUMMARY);
		name = cursor.getString(nameCol);
		// int nameCol = c.getColumnIndex(People.NAME);

		/*
		 * if (Integer.parseInt(c.getString(c
		 * .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
		 * name = c.getString(c
		 * .getColumnIndex(ContactsContract.Data.DISPLAY_NAME));
		 * 
		 * 
		 * } /** Next set the name of the entry.
		 */
		viewHolder.text = (TextView) v.findViewById(R.id.label);
		if (viewHolder.text != null) {
			viewHolder.text.setText(name);
		}
		v.setTag(viewHolder);
		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		String name = "noname";
		/*
		 * if (Integer.parseInt(c.getString(c
		 * .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
		 * name = c.getString(c
		 * .getColumnIndex(ContactsContract.Data.DISPLAY_NAME)); }
		 */
		int nameCol = c.getColumnIndex(RemindMeTable.COLUMN_SUMMARY);
		name = c.getString(nameCol);

		ViewHolder viewHolder = new ViewHolder();
		viewHolder.text = (TextView) v.findViewById(R.id.label);
		if (viewHolder.text != null) {
			viewHolder.text.setText(name);
		}
	}

	private class OnItemClickListener implements OnClickListener {
		private int mPosition;

		OnItemClickListener(int position) {
			mPosition = position;
		}
		
		

		@Override
		public void onClick(View view) {

		}
	}

}
