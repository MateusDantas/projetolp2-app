package com.ubet.content;

public class RoomsContent {

	private static String adminName;
	private static String roomName;
	private static int roomId;
	
	public boolean isPublicRoom;
	
	public RoomsContent(int roomId, String roomName, String adminName) {
		RoomsContent.setAdminName(adminName);
		RoomsContent.setRoomName(roomName);
		RoomsContent.setRoomId(roomId);
		if (adminName.equals("lobby"))
			this.isPublicRoom = true;
		else
			this.isPublicRoom = false;
	}

	public static String getAdminName() {
		return adminName;
	}

	private static void setAdminName(String adminName) {
		RoomsContent.adminName = adminName;
	}

	public static String getRoomName() {
		return roomName;
	}

	private static void setRoomName(String roomName) {
		RoomsContent.roomName = roomName;
	}

	public static int getRoomId() {
		return roomId;
	}

	private static void setRoomId(int roomId) {
		RoomsContent.roomId = roomId;
	}
	
	
}
