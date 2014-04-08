package com.ubet.content;

import java.util.Comparator;

public class RoomsContent {

	private String adminName;
	private String roomName;
	private int roomId;
	private int priceRoom;
	private int priceExtra;
	private int limExtra;
	private int peopleInside;
	public boolean isPublicRoom;

	public RoomsContent(int roomId, String roomName, String adminName,
			int priceRoom, int peopleInside, int priceExtra, int limExtra) {
		this.setAdminName(adminName);
		this.setRoomName(roomName);
		this.setRoomId(roomId);
		this.setPriceRoom(priceRoom);
		this.setPeopleInside(peopleInside);
		this.setPriceExtra(priceExtra);
		this.setLimExtra(limExtra);
		if (adminName.equals("lobby"))
			this.isPublicRoom = true;
		else
			this.isPublicRoom = false;
	}

	public static class sortByPeopleInside implements Comparator<RoomsContent> {
		
		@Override
		public int compare(RoomsContent userOne, RoomsContent userTwo) {
			return userTwo.getPeopleInside() - userOne.getPeopleInside();
		}
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

	public int getPriceExtra() {
		return priceExtra;
	}

	public void setPriceExtra(int priceExtra) {
		this.priceExtra = priceExtra;
	}

	public int getLimExtra() {
		return limExtra;
	}

	public void setLimExtra(int limExtra) {
		this.limExtra = limExtra;
	}

}
