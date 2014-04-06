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

	private int gameId;

	private int firstTeamScore;
	private int secondTeamScore;

	public GamesContent() {

	}

	public GamesContent(int firstTeamId, int secondTeamId, int gameId,
			String firstTeamName, String secondTeamName, int firstTeamScore,
			int secondTeamScore, Date gameDate) {

		this.setGameId(gameId);
		this.setFirstTeamId(firstTeamId);
		this.setSecondTeamId(secondTeamId);
		this.setFirstTeamName(firstTeamName);
		this.setSecondTeamName(secondTeamName);
		this.setFirstTeamScore(firstTeamScore);
		this.setSecondTeamScore(secondTeamScore);
		this.setGameDate(gameDate);
	}

	public GamesContent(int firstTeamId, int secondTeamId, int gameId,
			String firstTeamName, String secondTeamName, int firstTeamScore,
			int secondTeamScore, String gameDate) throws ParseException {

		this.setGameId(gameId);
		this.setFirstTeamId(firstTeamId);
		this.setSecondTeamId(secondTeamId);
		this.setFirstTeamName(firstTeamName);
		this.setFirstTeamScore(firstTeamScore);
		this.setSecondTeamScore(secondTeamScore);
		this.setSecondTeamName(secondTeamName);

		this.setGameDate(strToDate(gameDate));
	}

	public Date strToDate(String date) throws ParseException {

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.s");
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

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getFirstTeamScore() {
		return firstTeamScore;
	}

	public void setFirstTeamScore(int firstTeamScore) {
		this.firstTeamScore = firstTeamScore;
	}

	public int getSecondTeamScore() {
		return secondTeamScore;
	}

	public void setSecondTeamScore(int secondTeamScore) {
		this.secondTeamScore = secondTeamScore;
	}

}
