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

import com.ubet.content.GamesContent;
import com.ubet.util.UbetAccount;

public class GamesApi {

	public static List<GamesContent> gamesByRound(int round, Context context)
			throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();
		Account account = UbetAccount.getAccount(context);

		if (account == null)
			return null;

		params.put("username", account.name);
		params.put("round", String.valueOf(round));

		InputStream instream = UbetApi.ubetApiCall(UbetUrls.GET_GAMES_BY_ROUND,
				params, account, context);

		if (instream == null)
			return null;

		Document doc = Jsoup.parse(instream, "ISO-8859-15", "ubet.herokuapp.com");

		if (doc == null)
			return null;

		Elements games = doc.getElementsByClass("game");

		List<GamesContent> listOfGames = new ArrayList<GamesContent>();

		for (Element element : games) {

			int firstTeamId = Integer.valueOf(element.attr("first_team"));
			int secondTeamId = Integer.valueOf(element.attr("second_team"));
			String firstTeamName = element.attr("first_team_name");
			String secondTeamName = element.attr("second_team_name");
			String gameDate = element.attr("date");

			listOfGames.add(new GamesContent(firstTeamId, secondTeamId,
					firstTeamName, secondTeamName, gameDate));
		}

		return listOfGames;
	}
}
