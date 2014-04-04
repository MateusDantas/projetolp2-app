package com.ubet.client;

import java.io.InputStream;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.ubet.util.UbetAccount;

import android.accounts.Account;
import android.content.Context;

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

}
