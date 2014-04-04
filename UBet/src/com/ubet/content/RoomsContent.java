package com.ubet.content;

public class RoomsContent {

	private String adminName;
	private String roomName;
	private int roomId;
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
		this.adminName = adminName;
	}

	public String getRoomName() {
		return roomName;
	}

	private void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	public int getRoomId() {
		return roomId;
	}

	private void setRoomId(int roomId) {
		this.roomId = roomId;
	}
	
	
}
