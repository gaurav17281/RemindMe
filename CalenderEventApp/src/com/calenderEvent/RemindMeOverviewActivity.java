package com.calenderEvent;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.calenderEvent.ContentProvider.RemindMeContentProvider;
import com.calenderEvent.Database.RemindMeTable;
import com.calenderEvent.controller.Controller;
import com.calenderEvent.controller.CustomCursorAdapter;
import com.calenderEvent.controller.ViewPagerAdapter;

public class RemindMeOverviewActivity extends SherlockFragmentActivity
		implements LoaderManager.LoaderCallbacks<Cursor>,
		ActionBar.OnNavigationListener {

	private static final int ACTIVITY_CREATE = 0;
	public static final int ACTIVITY_EDIT = 1;
	private static final int DELETE_ID = Menu.FIRST + 1;

	private boolean searchLoader;

	private CustomCursorAdapter adapter;
	private CustomCursorAdapter searchAdapter;
	private ViewPagerAdapter viewPagerAdapter;
	private Controller controller;
	private ListView contactView;
	private ViewPager viewPager;

	private String[] projection;
	private String[] selectionArgs;
	private String selection;
	private String order;

	// Constructor

	public RemindMeOverviewActivity() {
		controller = new Controller(this);
	}

	/* Getters and setters */

	public boolean isSearchLoader() {
		return searchLoader;
	}

	public void setSearchLoader(boolean searchLoader) {
		this.searchLoader = searchLoader;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSelection() {
		return selection;
	}

	public void setSelection(String selection) {
		this.selection = selection;
	}

	public String[] getSelectionArgs() {
		return selectionArgs;
	}

	public void setSelectionArgs(String[] selectionArgs) {
		this.selectionArgs = selectionArgs;
	}

	public String[] getProjection() {
		return projection;
	}

	public void setProjection(String[] projection) {
		this.projection = projection;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contactpicker);
		/*
		 * viewPagerAdapter = new ViewPagerAdapter(this); viewPager =
		 * (ViewPager) findViewById(R.id.pageflipper);
		 * viewPager.setAdapter(viewPagerAdapter);
		 */
		fillData(this.getWindow().getDecorView());
		setupActionBar();

	}

	private void setupActionBar() {
		getSupportActionBar().setTitle("Remind Me");
		getSupportActionBar().setDisplayShowHomeEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		Context context = getSupportActionBar().getThemedContext();
		ArrayAdapter<CharSequence> list = ArrayAdapter.createFromResource(
				context, R.array.navigation, R.layout.sherlock_spinner_item);
		list.setDropDownViewResource(R.layout.sherlock_spinner_dropdown_item);
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		getSupportActionBar().setListNavigationCallbacks(list, this);
		searchAdapter = new CustomCursorAdapter(this, R.layout.rowlayout, null,
				null, null);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(v.getId(), DELETE_ID, 0, R.string.menu_delete);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			Uri uri = Uri.parse(RemindMeContentProvider.CONTENT_URI + "/"
					+ item.getGroupId());
			getContentResolver().delete(uri, null, null);
			adapter.notifyDataSetChanged();
			//fillData(this.getWindow().getDecorView());
			return true;
		}
		return super.onContextItemSelected((android.view.MenuItem) item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		new MenuInflater(this).inflate(R.menu.listmenu, menu);
		menu.findItem(R.id.insert).setIcon(android.R.drawable.ic_input_add)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		menu.findItem(R.id.search)
				.setIcon(R.drawable.ic_search)
				.setActionView(R.layout.collapsible_edittext)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_ALWAYS
								| MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW);

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public boolean onOptionsItemSelected(
			com.actionbarsherlock.view.MenuItem item) {
		switch (item.getItemId()) {
		case R.id.insert:
			createTodo();
			return true;
		case R.id.search:
			AutoCompleteTextView textView = (AutoCompleteTextView) (item
					.getActionView()).findViewById(R.id.editSearch);
			textView.setAdapter(searchAdapter);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void createTodo() {
		Intent i = new Intent(this, RemindMeDetailActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		adapter.notifyDataSetChanged();

		//fillData(this.getWindow().getDecorView());
		super.onActivityResult(requestCode, resultCode, intent);
	}

	public Cursor searchManager(String constraint) throws SQLException {
		Uri searchUri = RemindMeContentProvider.CONTENT_URI_SEARCH;

		String[] projection = { RemindMeTable.COLUMN_ID,
				RemindMeTable.COLUMN_SUMMARY, RemindMeTable.COLUMN_CATEGORY };

		if (constraint != null) {
			constraint = constraint.trim() + "%";
		}
		String params[] = { constraint };
		if (constraint == null) {
			params = null;
		}

		try {
			Cursor cursor = getContentResolver().query(searchUri, projection,
					null, params, null);
			if (cursor != null) {
				cursor.moveToFirst();
				return cursor;
			}
		} catch (SQLException e) {
			Log.d("AutoCompleteDbAdapter", e.toString());
			throw e;
		}

		return null;

	}

	public void fillData(View v) {
		String[] from = new String[] { RemindMeTable.COLUMN_ID,
				RemindMeTable.COLUMN_SUMMARY, RemindMeTable.COLUMN_CATEGORY };
		int[] to = new int[] { R.id.label };
		setOrder(RemindMeTable.COLUMN_CATEGORY + " ASC");
		setProjection(from);
		contactView = (ListView) v.findViewById(R.id.contactview);
		getSupportLoaderManager().initLoader(0, null, this);
		adapter = new CustomCursorAdapter(this, R.layout.rowlayout, from, to,
				null);
		contactView.setAdapter(adapter);
		if (adapter.isEmpty()) {
			ViewStub stub = (ViewStub) findViewById(R.id.empty);
			contactView.setEmptyView(stub.inflate());
		}
	}

	// Creates a new loader after the initLoader () call
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader;
		cursorLoader = new CursorLoader(this,
				RemindMeContentProvider.CONTENT_URI, this.getProjection(),
				this.getSelection(), this.getSelectionArgs(), this.getOrder());
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.changeCursor(data);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.changeCursor(null);
	}

	@Override
	public boolean onNavigationItemSelected(int itemPosition, long itemId) {
		// viewPager.setCurrentItem(itemPosition,true);
		return true;
	}

}
