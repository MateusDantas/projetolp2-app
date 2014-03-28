package com.ubet.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class Room {

	public Room() {

	}

	public static final class Rooms implements BaseColumns {
		private Rooms() {
		}

		public static final Uri CONTENT_URI = Uri.parse("content://"
				+ RoomsContentProvider.AUTHORITY + "/room");
		
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.ubet.room";
		
		public static final String NOTE_ID = "_id";
	}
}
