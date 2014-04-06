package com.ubet.content;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GamesContent {

	private String firstTeamName;
	private String secondTeamName;

	private int firstTeamId;
	private int secondTeamId;

	private Date gameDate;

	public GamesContent() {

	}

	public GamesContent(int firstTeamId, int secondTeamId,
			String firstTeamName, String secondTeamName, Date gameDate) {

		this.setFirstTeamId(firstTeamId);
		this.setSecondTeamId(secondTeamId);
		this.setFirstTeamName(firstTeamName);
		this.setSecondTeamName(secondTeamName);
		this.setGameDate(gameDate);
	}

	public GamesContent(int firstTeamId, int secondTeamId,
			String firstTeamName, String secondTeamName, String gameDate)
			throws ParseException {

		this.setFirstTeamId(firstTeamId);
		this.setSecondTeamId(secondTeamId);
		this.setFirstTeamName(firstTeamName);
		this.setSecondTeamName(secondTeamName);

		this.setGameDate(strToDate(gameDate));
	}

	public Date strToDate(String date) throws ParseException {

		DateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.s");
		Date newDate = formatter.parse(date);
		return newDate;
	}

	public String getFormattedDate() {
		
		String formattedDate = new SimpleDateFormat("dd MMM yyyy HH:mm")
				.format(gameDate);
		return formattedDate;
	}

	public String getFirstTeamName() {
		return firstTeamName;
	}

	public void setFirstTeamName(String firstTeamName) {
		this.firstTeamName = firstTeamName;
	}

	public String getSecondTeamName() {
		return secondTeamName;
	}

	public void setSecondTeamName(String secondTeamName) {
		this.secondTeamName = secondTeamName;
	}

	public int getFirstTeamId() {
		return firstTeamId;
	}

	public void setFirstTeamId(int firstTeamId) {
		this.firstTeamId = firstTeamId;
	}

	public int getSecondTeamId() {
		return secondTeamId;
	}

	public void setSecondTeamId(int secondTeamId) {
		this.secondTeamId = secondTeamId;
	}

	public Date getGameDate() {
		return gameDate;
	}

	public void setGameDate(Date gameDate) {
		this.gameDate = gameDate;
	}

}
