package com.ubet.provider;

import com.ubet.provider.Room.Rooms;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

public class RoomsContentProvider extends ContentProvider {

	private static final String DATABASE_NAME = "ubetRooms.db";
	private static final int DATABASE_VERSION = 1;
	private static final String ROOM_TABLE_NAME = "room";
	
	public static final String AUTHORITY = "com.ubet.provider.RoomsContentProvider";

	public static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase sqlDb) {
			// TODO Auto-generated method stub
			sqlDb.execSQL("CREATE TABLE " + ROOM_TABLE_NAME
					+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "ADMIN TEXT, " + "ROOMID INTEGER, " + "NAME TEXT, "
					+ "TIPO INTEGER);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase sqlDb, int oldVersion,
				int newVersion) {
			// TODO Auto-generated method stub
			sqlDb.execSQL("DROP TABLE IF EXISTS " + ROOM_TABLE_NAME);
			onCreate(sqlDb);
		}

	}

	private DatabaseHelper dbHelper;

	@Override
	public boolean onCreate() {

		dbHelper = new DatabaseHelper(getContext());
		return false;
	}
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = db.delete(ROOM_TABLE_NAME, selection, selectionArgs);
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		int count = db.update(ROOM_TABLE_NAME, values, selection, selectionArgs);
		
		getContext().getContentResolver().notifyChange(uri, null);
		
		return count;
	}
	
	@Override
	public String getType(Uri uri) {
		
		return Rooms.CONTENT_TYPE;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		long rowId = db.insert(ROOM_TABLE_NAME, null, values);
		
		if (rowId > 0) {
			Uri rowUri = ContentUris.withAppendedId(Rooms.CONTENT_URI, rowId);
			getContext().getContentResolver().notifyChange(rowUri, null);
			return rowUri;
		}
		
		throw new SQLException("Failed to insert row into " + uri);
	}


	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		
		queryBuilder.setTables(ROOM_TABLE_NAME);
		
		Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		
		return cursor;
	}
	
}
