package com.ubet.client;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.accounts.Account;
import android.content.Context;
import android.util.Log;

import com.ubet.content.BetsContent;
import com.ubet.content.GamesContent;
import com.ubet.util.UbetAccount;

public class BetsApi {

	public static int makeBet(int gameId, int roomId, int round, int scoreOne, int scoreTwo,
			boolean isExtraBet, Context context) throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();
		Account account = UbetAccount.getAccount(context);

		if (account == null) {
			Log.d("erro","erroqui");
			return -1;
		}

		params.put("round", String.valueOf(round));
		params.put("username", account.name);
		params.put("gameid", String.valueOf(gameId));
		params.put("scoreone", String.valueOf(scoreOne));
		params.put("scoretwo", String.valueOf(scoreTwo));
		params.put("roomid", String.valueOf(roomId));
		if (isExtraBet)
			params.put("isextrabet", "1");
		else
			params.put("isextrabet", "0");

		InputStream instream = UbetApi.ubetApiCall(UbetUrls.MAKE_BET,
				params, account, context);

		if (instream == null) {
			Log.d("erro","erroqui2");
			return -1;
		}

		Document doc = Jsoup.parse(instream, "ISO-8859-15",
				"ubet.herokuapp.com");

		if (doc == null) {
			Log.d("erro","erroqui3");
			return -1;
		}

		return Integer.valueOf(doc.select("#returnCode").html());
	}


	public static int changeBet(int betId, int scoreOne, int scoreTwo, Context context) throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();
		Account account = UbetAccount.getAccount(context);

		if (account == null) {
			Log.d("erro","erroqui");
			return -1;
		}

		params.put("betid", String.valueOf(betId));
		params.put("username", account.name);
		params.put("scoreone", String.valueOf(scoreOne));
		params.put("scoretwo", String.valueOf(scoreTwo));

		InputStream instream = UbetApi.ubetApiCall(UbetUrls.CHANGE_BET,
				params, account, context);

		if (instream == null) {
			return -1;
		}

		Document doc = Jsoup.parse(instream, "ISO-8859-15",
				"ubet.herokuapp.com");

		if (doc == null) {
			return -1;
		}

		return Integer.valueOf(doc.select("#returnCode").html());
	}
	
	public static List<BetsContent> betsByGame(int gameId, int roomId, int round,
			Context context) throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();
		Account account = UbetAccount.getAccount(context);

		if (account == null)
			return null;

		params.put("username", account.name);
		params.put("gameid", String.valueOf(gameId));
		params.put("roomid", String.valueOf(roomId));
		params.put("round", String.valueOf(round));

		InputStream instream = UbetApi.ubetApiCall(
				UbetUrls.GET_BETS_BY_USER_BY_GAME, params, account, context);

		if (instream == null)
			return null;

		Document doc = Jsoup.parse(instream, "ISO-8859-15",
				"ubet.herokuapp.com");

		if (doc == null)
			return null;

		Elements bets = doc.getElementsByClass("bets");

		List<BetsContent> listOfBets = new ArrayList<BetsContent>();

		for (Element element : bets) {

			int scoreOne = Integer.valueOf(element.attr("scoreone"));
			int scoreTwo = Integer.valueOf(element.attr("scoretwo"));
			int betId = Integer.valueOf(element.attr("betid"));
			
			listOfBets.add(new BetsContent(betId, gameId, scoreOne, scoreTwo));
		}

		return listOfBets;
	}
}
