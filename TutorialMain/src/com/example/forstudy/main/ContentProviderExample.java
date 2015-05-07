package com.example.forstudy.main;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

// Should enroll provider at Manifest file
public class ContentProviderExample extends ContentProvider {

	static final Uri CONTENT_URI = Uri.parse("content://com.example.forstudy.main");
	private final String DB_NAME = "sampleDB.db", TABLE_NAME = "sampleTable";
	SQLiteDatabase mDB;
	
	@Override
	public boolean onCreate() {
		dbHelper helper = new dbHelper(getContext());
		mDB = helper.getReadableDatabase();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		String sql = "SELECT * FROM " + TABLE_NAME + ";";
		Cursor result = mDB.rawQuery(sql, null);
		
		return result;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		return 0;
	}
	
	class dbHelper extends SQLiteOpenHelper {

		public dbHelper(Context context) {
			super(context, DB_NAME, null, 1);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, test_column TEXT NOT NULL)";
	        db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		    onCreate(db);
		}
		
	}
}