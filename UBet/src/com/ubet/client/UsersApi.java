package com.ubet.client;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ubet.content.UsersContent;
import com.ubet.util.UbetAccount;

import android.accounts.Account;
import android.content.Context;
import android.util.Log;

public class UsersApi {

	public static int logoutUser(Account account, Context context)
			throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();

		if (account == null)
			return -1;
		
		params.put("username", account.name);

		InputStream instream = UbetApi.ubetApiCall(UbetUrls.LOGOFF_USER_URL,
				params, account, context);

		if (instream == null)
			return 0;

		Document doc = Jsoup.parse(instream, "UTF-8", "ubet.herokuapp.com");

		if (doc == null)
			return 0;

		return Integer.valueOf(doc.select("#returnCode").html());
	}

	public static int getUserCoins(Account account, Context context, String username)
			throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();

		if (account == null)
			return -1;
		
		params.put("username", account.name);
		params.put("requested_user", username);
		
		InputStream instream = UbetApi.ubetApiCall(UbetUrls.GET_COINS_USER_URL,
				params, account, context);

		if (instream == null)
			return 0;

		Document doc = Jsoup.parse(instream, "UTF-8", "ubet.herokuapp.com");

		if (doc == null)
			return 0;
		
		return Integer.valueOf(doc.select("div#coins").html());
	}
	

	public static List<UsersContent> getAllUsers(Context context)
			throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();
		Account account = UbetAccount.getAccount(context);

		if (account == null)
			return null;
		
		params.put("username", account.name);

		InputStream instream = UbetApi.ubetApiCall(UbetUrls.GET_ALL_USERS,
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

}
