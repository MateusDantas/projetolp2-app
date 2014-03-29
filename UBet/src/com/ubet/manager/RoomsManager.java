package com.ubet.manager;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.ubet.content.RoomsContent;
import com.ubet.provider.Room.Rooms;

public class RoomsManager {
	
	public static final int PUBLIC_ROOM = 0;
	public static final int OWNER_ROOM = 1;
	public static final int PRIVATE_ROOM = 2;
	
	public static boolean existRoom(int roomId, Context context) {
		
		String selection = "ROOMID=?";
		String [] selectionArgs = new String[]{String.valueOf(roomId)};
		
		Uri roomUri = Rooms.CONTENT_URI;
		
		Cursor cursor = context.getContentResolver().query(roomUri, null, selection, selectionArgs, null);
		
		if (cursor.moveToFirst())
			return true;
		
		return false;
	}
	
	public static List<RoomsContent> getRooms(Cursor cursor) {
		
		List<RoomsContent> listOfRooms = new ArrayList<RoomsContent>();
		
		while (cursor.moveToNext()) {
			int roomId = cursor.getInt(cursor.getColumnIndex("ROOMID"));
			String roomName = cursor.getString(cursor.getColumnIndex("NAME"));
			String adminName = cursor.getString(cursor.getColumnIndex("ADMIN"));
			listOfRooms.add(new RoomsContent(roomId, roomName, adminName));
		}
		
		return listOfRooms;
	}
	
	public static List<RoomsContent> getPublicRooms(Context context) {
		
		String selection = "TIPO=" + String.valueOf(PUBLIC_ROOM);
		
		Uri roomUri = Rooms.CONTENT_URI;
		
		Cursor cursor = context.getContentResolver().query(roomUri, null, selection, null, null);
		
		return getRooms(cursor);
	}
	
	public static List<RoomsContent> getOwnerRooms(Context context) {

		String selection = "TIPO=" + String.valueOf(OWNER_ROOM);
		
		Uri roomUri = Rooms.CONTENT_URI;
		
		Cursor cursor = context.getContentResolver().query(roomUri, null, selection, null, null);
		
		return getRooms(cursor);
	}
	
	public static List<RoomsContent> getPrivateRooms(Context context) {
		
		String selection = "TIPO=" + String.valueOf(PRIVATE_ROOM);
		
		Uri roomUri = Rooms.CONTENT_URI;
		
		Cursor cursor = context.getContentResolver().query(roomUri, null, selection, null, null);
		
		return getRooms(cursor);
	}
	
	public static void addRoom(RoomsContent room, String username, Context context) {
		
		final ContentValues values = new ContentValues();
		values.put("ADMIN", room.getAdminName());
		values.put("NAME", room.getRoomName());
		values.put("ROOMID", room.getRoomId());
		int tipo;
		if (username.equals(room.getAdminName()))
			tipo = OWNER_ROOM;
		else if(username.equals("ubet"))
			tipo = PUBLIC_ROOM;
		else
			tipo = PRIVATE_ROOM;
		values.put("TIPO", tipo);
		
		context.getContentResolver().insert(Rooms.CONTENT_URI, values);
	}
}
