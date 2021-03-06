package com.ubet.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;

import com.ubet.content.RoomsContent;
import com.ubet.content.UsersContent;
import com.ubet.util.UbetAccount;
import com.ubet.util.Variables;

import android.accounts.Account;
import android.content.Context;

public class RoomsApi {

	public static List<RoomsContent> getRooms(String Url, String username,
			Context context) throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();
		Account account = UbetAccount.getAccount(context);

		if (account == null)
			return null;
		
		params.put("username", account.name);
		params.put("requested_user", username);

		InputStream instream = UbetApi.ubetApiCall(Url, params, account,
				context);

		if (instream == null)
			return null;

		Document doc = Jsoup.parse(instream, "UTF-8", "ubet.herokuapp.com");

		if (doc == null)
			return null;

		
		
		Elements rooms = doc.getElementsByClass("room");

		List<RoomsContent> listOfRooms = new ArrayList<RoomsContent>();

		for (Element element : rooms) {
			String adminName = element.attr("admin");
			String roomName = element.attr("name");
			int roomId = Integer.valueOf(element.attr("roomid"));
			int priceRoom = Integer.valueOf(element.attr("roomprice"));
			int peopleInside = Integer.valueOf(element.attr("peopleinside"));
			int priceExtra = Integer.valueOf(element.attr("priceextra"));
			int limExtra = Integer.valueOf(element.attr("limextra"));
			listOfRooms.add(new RoomsContent(roomId, roomName, adminName,
					priceRoom, peopleInside, priceExtra, limExtra));
		}

		return listOfRooms;
	}

	public static List<RoomsContent> getAllRooms(String username,
			Context context) throws Exception {

		return getRooms(UbetUrls.GET_ALL_ROOMS, username, context);
	}

	public static List<RoomsContent> getRoomsByUser(String username,
			Context context) throws Exception {

		return getRooms(UbetUrls.GET_ROOMS_BY_USER, username, context);
	}

	public static List<RoomsContent> getRoomsCreatedByUser(String username,
			Context context) throws Exception {

		return getRooms(UbetUrls.GET_ROOMS_CREATED_BY_USER_URL, username,
				context);
	}

	public static boolean isUserInRoom(String username, int roomId,
			Context context) throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();
		Account account = UbetAccount.getAccount(context);

		if (account == null)
			return false;
		
		params.put("username", account.name);
		params.put("roomid", String.valueOf(roomId));
		
		InputStream instream = UbetApi.ubetApiCall(UbetUrls.IS_USER_IN_ROOM,
				params, account, context);

		if (instream == null)
			return false;

		Document doc = Jsoup.parse(instream, "UTF-8", "ubet.herokuapp.com");
		
		if (doc == null)
			return false;
		
		return Integer.valueOf(doc.select("#returnCode").html()) == 1;
	}

	public static List<UsersContent> getUsersInRoom(int roomId, Context context)
			throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();
		Account account = UbetAccount.getAccount(context);

		if (account == null)
			return null;
		
		params.put("username", account.name);
		params.put("roomid", String.valueOf(roomId));

		InputStream instream = UbetApi.ubetApiCall(UbetUrls.GET_USERS_IN_ROOM,
				params, account, context);

		if (instream == null)
			return null;

		Document doc = Jsoup.parse(instream, "UTF-8", "ubet.herokuapp.com");

		if (doc == null)
			return null;

		Elements rooms = doc.getElementsByClass("user");

		List<UsersContent> listOfUsers = new ArrayList<UsersContent>();

		for (Element element : rooms) {
			String username = element.attr("username");
			int score = Integer.valueOf(element.attr("score"));
			int coins = Integer.valueOf(element.attr("coins"));
			listOfUsers.add(new UsersContent(username, coins, score));
		}

		return listOfUsers;
	}

	public static int getPointsByUserInRoom(String username, int roomId,
			Context context) throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();
		Account account = UbetAccount.getAccount(context);

		if (account == null)
			return -1;
		
		params.put("username", account.name);
		params.put("requested_user", username);

		InputStream instream = UbetApi.ubetApiCall(
				UbetUrls.GET_POINTS_USER_IN_ROOM_URL, params, account, context);

		if (instream == null)
			return -1;

		Document doc = Jsoup.parse(instream, "UTF-8", "ubet.herokuapp.com");

		if (doc == null)
			return -1;

		return Integer.valueOf(doc.select("#points").html());
	}

	public static int createRoom(String name, String username, int priceRoom,
			int priceExtra, int limExtra, String password, Context context)
			throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();
		Account account = UbetAccount.getAccount(context);

		if (account == null)
			return -1;
		
		params.put("name", name);
		params.put("username", username);
		params.put("price_room", String.valueOf(priceRoom));
		params.put("price_extra", String.valueOf(priceExtra));
		params.put("lim_extra", String.valueOf(limExtra));
		params.put("password", password);

		InputStream instream = UbetApi.ubetApiCall(UbetUrls.CREATE_ROOM_URL,
				params, account, context);

		if (instream == null)
			return Variables.INTERNAL_ERROR;

		Document doc = Jsoup.parse(instream, "UTF-8", "ubet.herokuapp.com");

		if (doc == null)
			return Variables.INTERNAL_ERROR;

		return Integer.valueOf(doc.select("div#returnCode").html());
	}

	public static int enterToRoom(String username, String password, int roomId,
			Context context) throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();
		Account account = UbetAccount.getAccount(context);

		if (account == null)
			return -1;
		
		params.put("username", username);
		params.put("password", password);
		params.put("roomid", String.valueOf(roomId));

		InputStream instream = UbetApi.ubetApiCall(UbetUrls.ENTER_TO_ROOM_URL,
				params, account, context);

		if (instream == null)
			return Variables.INTERNAL_ERROR;

		Document doc = Jsoup.parse(instream, "UTF-8", "ubet.herokuapp.com");

		if (doc == null)
			return Variables.INTERNAL_ERROR;

		return Integer.valueOf(doc.select("div#returnCode").html());
	}
}
