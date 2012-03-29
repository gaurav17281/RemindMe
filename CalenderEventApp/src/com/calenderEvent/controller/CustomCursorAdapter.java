package com.calenderEvent.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.CharArrayBuffer;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.calenderEvent.R;
import com.calenderEvent.RemindMeDetailActivity;
import com.calenderEvent.RemindMeOverviewActivity;
import com.calenderEvent.ContentProvider.RemindMeContentProvider;
import com.calenderEvent.Database.RemindMeTable;

public class CustomCursorAdapter extends CursorAdapter implements
		OnClickListener {

	private final Activity context;
	private final int layout;
	private static int rowPosition = 0;

	private Cursor currentCursor;

	private static final int STATE_UNKNOWN = 0;
	private static final int STATE_SECTIONED_CELL = 1;
	private static final int STATE_REGULAR_CELL = 2;

	private final CharArrayBuffer mBuffer = new CharArrayBuffer(128);
	private int[] mCellStates;

	private static class ViewHolder {
		public TextView separator;
		public TextView text;
		public ImageView image;
		public String priority;
		public int position;
		public CharArrayBuffer titleBuffer = new CharArrayBuffer(128);

	}

	public CustomCursorAdapter(Activity context, int layout, String[] from,
			int[] to, Cursor c) {
		super(context, c, true);
		this.context = context;
		this.layout = layout;
		mCellStates = c == null ? null : new int[c.getCount()];

	}

	@Override
	public void changeCursor(Cursor cursor) {
		super.changeCursor(cursor);
		this.currentCursor = cursor;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		ViewHolder viewHolder = new ViewHolder();
		View rowView = LayoutInflater.from(context).inflate(layout, parent,
				false);
		viewHolder.separator = (TextView) rowView.findViewById(R.id.separator);
		viewHolder.text = (TextView) rowView.findViewById(R.id.label);
		viewHolder.image = (ImageView) rowView.findViewById(R.id.icon);
		viewHolder.priority = cursor.getString(cursor
				.getColumnIndexOrThrow(RemindMeTable.COLUMN_CATEGORY));
		rowView.setTag(viewHolder);
		viewHolder.position = rowPosition++;
		return rowView;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		ViewHolder holder = (ViewHolder) v.getTag();
		boolean needSeparator = false;
		final int position = c.getPosition();
		
		if (position == 0) {
			needSeparator = false;
		} else {
			c.moveToPosition(position - 1);
			if (!c.getString(
					c.getColumnIndexOrThrow(RemindMeTable.COLUMN_CATEGORY))
					.equalsIgnoreCase(holder.priority))
				needSeparator = true;
		}
		c.moveToPosition(position);

		if (needSeparator) {
			holder.separator.setText(holder.priority);
			holder.separator.setVisibility(View.VISIBLE);
			holder.image.setVisibility(View.GONE);
			holder.text.setVisibility(View.GONE);
		} else {
			holder.separator.setVisibility(View.GONE);
		}

		holder.text.setText(convertToString(c));
		holder.image.setImageResource(R.drawable.ic_launcher);
		v.setId(c.getInt(c.getColumnIndex(RemindMeTable.COLUMN_ID)));
		this.context.registerForContextMenu(v);
		v.setOnClickListener(this);
	}

	
	@Override
	public String convertToString(Cursor cursor) {
		return cursor.getString(cursor
				.getColumnIndexOrThrow(RemindMeTable.COLUMN_SUMMARY));
	}

	@Override
	public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
		if (getFilterQueryProvider() != null)
			return getFilterQueryProvider().runQuery(constraint);

		return ((RemindMeOverviewActivity) context)
				.searchManager((constraint != null ? constraint.toString()
						: null));
	}

	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount();
	}

	@Override
	public int getItemViewType(int position) {
		return super.getItemViewType(position);
	}

	@Override
	public boolean isEnabled(int position) {
		return super.isEnabled(position);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent(context, RemindMeDetailActivity.class);
		Uri editUri = Uri.parse(RemindMeContentProvider.CONTENT_URI + "/"
				+ v.getId());
		i.putExtra(RemindMeContentProvider.CONTENT_ITEM_TYPE, editUri);
		context.startActivityForResult(i,
				RemindMeOverviewActivity.ACTIVITY_EDIT);
	}

}
