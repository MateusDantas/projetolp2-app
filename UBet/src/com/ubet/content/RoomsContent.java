package com.ubet.content;

public class RoomsContent {

	private String adminName;
	private String roomName;
	private int roomId;
	private int priceRoom;
	private int peopleInside;
	public boolean isPublicRoom;

	public RoomsContent(int roomId, String roomName, String adminName,
			int priceRoom, int peopleInside) {
		this.setAdminName(adminName);
		this.setRoomName(roomName);
		this.setRoomId(roomId);
		this.setPriceRoom(priceRoom);
		this.setPeopleInside(peopleInside);
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

	public int getPriceRoom() {
		return priceRoom;
	}

	public void setPriceRoom(int priceRoom) {
		this.priceRoom = priceRoom;
	}

	public int getPeopleInside() {
		return peopleInside;
	}

	public void setPeopleInside(int peopleInside) {
		this.peopleInside = peopleInside;
	}

}
