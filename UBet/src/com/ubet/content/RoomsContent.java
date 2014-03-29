package com.ubet.content;

public class RoomsContent {

	private static String adminName;
	private static String roomName;
	private static int roomId;
	public boolean isPublicRoom;
	
	public RoomsContent(int roomId, String roomName, String adminName) {
		this.setAdminName(adminName);
		this.setRoomName(roomName);
		this.setRoomId(roomId);
		if (adminName.equals("lobby"))
			this.isPublicRoom = true;
		else
			this.isPublicRoom = false;
	}

	public String getAdminName() {
		return adminName;
	}

	private void setAdminName(String adminName) {
		RoomsContent.adminName = adminName;
	}

	public String getRoomName() {
		return roomName;
	}

	private void setRoomName(String roomName) {
		RoomsContent.roomName = roomName;
	}

	public int getRoomId() {
		return roomId;
	}

	private void setRoomId(int roomId) {
		RoomsContent.roomId = roomId;
	}
	
	
}
