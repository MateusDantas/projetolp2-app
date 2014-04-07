package com.ubet.client;

public class UbetUrls {
	/**
	 * User's URLs
	 */
	public static final String CREATE_USER_URL = "users/createuser";
	public static final String LOGIN_USER_URL = "users/login";
	public static final String CHANGE_PASSWORD_URL = "users/changepassword";
	public static final String REFRESH_TOKEN_URL = "users/refreshtoken";
	public static final String LOGOFF_USER_URL = "users/logoff";
	public static final String GET_COINS_USER_URL = "users/getcoins";
	public static final String GET_SCORE_USER_URL = "users/getscore";
	public static final String GET_ALL_USERS = "users/getallusers";
	
	/**
	 * Room's URLs
	 */
	public static final String GET_ALL_ROOMS = "rooms/getallrooms";
	public static final String CREATE_ROOM_URL = "rooms/createroom";
	public static final String GET_ROOMS_CREATED_BY_USER_URL = "rooms/getroomscreatedbyuser";
	public static final String ENTER_TO_ROOM_URL = "rooms/entertoroom";
	public static final String GET_USERS_IN_ROOM = "rooms/usersinroom";
	public static final String GET_ROOMS_BY_USER = "rooms/roomsbyuser";
	public static final String GET_POINTS_USER_IN_ROOM_URL = "rooms/pointsuser";
	public static final String LAST_UPDATE_ROOM = "rooms/lastupdate";
	public static final String IS_USER_IN_ROOM = "rooms/isuserinroom";
	
	/**
	 * Bet's URL
	 */
	public static final String MAKE_BET = "bets/makebet";
	public static final String CHANGE_BET = "bets/changebet";
	public static final String GET_BETS_BY_USER_BY_ROOM = "bets/betsbyuserbyroom";
	public static final String GET_BETS_BY_USER_BY_GAME = "bets/betsbyuserbygame";
	public static final String GET_BETS_BY_USER = "bets/betsbyuser";
	
	/**
	 * Game's URL
	 */
	public static final String GET_GAMES_BY_ROUND = "games/getgamesbyround";
}
