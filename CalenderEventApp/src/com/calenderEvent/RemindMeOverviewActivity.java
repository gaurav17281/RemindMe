package com.calenderEvent;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.calenderEvent.ContentProvider.RemindMeContentProvider;
import com.calenderEvent.Database.RemindMeTable;
import com.calenderEvent.controller.Controller;
import com.calenderEvent.controller.CustomCursorAdapter;

public class RemindMeOverviewActivity extends FragmentActivity implements
		LoaderManager.LoaderCallbacks<Cursor>, OnItemSelectedListener,
		OnQueryTextListener {

	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int DELETE_ID = Menu.FIRST + 1;
	private CustomCursorAdapter adapter;
	private Controller controller;
	private ListView contactView;
	private String searchFilter;

	public RemindMeOverviewActivity() {
		controller = new Controller(this);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactpicker);
		contactView = (ListView) findViewById(R.id.contactview);
		fillData();
		registerForContextMenu(contactView);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
					.getMenuInfo();
			Uri uri = Uri.parse(RemindMeContentProvider.CONTENT_URI + "/"
					+ info.id);
			getContentResolver().delete(uri, null, null);
			adapter.notifyDataSetChanged();
			return true;
		}
		return super.onContextItemSelected(item);
	}

	// Opens the second activity if an entry is clicked

	@Override
	public void onItemSelected(AdapterView<?> l, View v, int position, long id) {
		Intent i = new Intent(this, RemindMeDetailActivity.class);
		Uri todoUri = Uri.parse(RemindMeContentProvider.CONTENT_URI + "/" + id);
		i.putExtra(RemindMeContentProvider.CONTENT_ITEM_TYPE, todoUri);

		// Activity returns an result if called with startActivityForResult
		startActivityForResult(i, ACTIVITY_EDIT);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.listmenu, menu);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {

			menu.findItem(R.id.insert).setIcon(android.R.drawable.ic_input_add)
					.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
			SearchView sv = new SearchView(this);
			sv.setBackgroundColor(Color.WHITE);
			sv.setOnQueryTextListener(this);
			menu.findItem(R.id.search)
					.setIcon(R.drawable.ic_search_inverse)
					.setActionView(sv)
					.setShowAsAction(
							MenuItem.SHOW_AS_ACTION_ALWAYS
									| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

		}
		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			createTodo();
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	private void createTodo() {
		Intent i = new Intent(this, RemindMeDetailActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	// Called with the result of the other activity
	// requestCode was the origin request code send to the activity
	// resultCode is the return code, 0 is everything is ok
	// intend can be used to get data
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
	}

	private void fillData() {

		String[] from = new String[] { RemindMeTable.COLUMN_SUMMARY };
		int[] to = new int[] { R.id.label };
		getSupportLoaderManager().initLoader(0, null, this);
		adapter = new CustomCursorAdapter(this, R.layout.rowlayout, from, to,
				controller, null);
		contactView.setAdapter(adapter);
	}

	// Creates a new loader after the initLoader () call
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { RemindMeTable.COLUMN_ID,
				RemindMeTable.COLUMN_SUMMARY };
		CursorLoader cursorLoader;
		if (searchFilter==null)
			cursorLoader = new CursorLoader(this,
					RemindMeContentProvider.CONTENT_URI, projection, null,
					null, null);
		else
			cursorLoader = new CursorLoader(this,
					RemindMeContentProvider.CONTENT_URI, projection,
					RemindMeTable.COLUMN_DESCRIPTION,
					new String[] { searchFilter.toString() },
					RemindMeTable.COLUMN_DESCRIPTION + " COLLATE LOCALIZED ASC");
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.changeCursor(data);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// data is not available anymore, delete reference
		adapter.changeCursor(null);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onQueryTextChange(String newText) {
		if (newText != null) {
			searchFilter = !TextUtils.isEmpty(newText) ? newText : null;
			getSupportLoaderManager().restartLoader(0, null, this);
		}
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		// TODO Auto-generated method stub
		return false;
	}

}
