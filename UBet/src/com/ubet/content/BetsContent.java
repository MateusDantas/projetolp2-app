package com.ubet.content;

public class BetsContent {

	private int gameId, betId;
	private int scoreOne, scoreTwo;
	
	public static final int LIM_BET_DATE = 30 * 60000;
	
	public BetsContent(int betId, int gameId, int scoreOne, int scoreTwo) {
		
		this.setBetId(betId);
		this.setGameId(gameId);
		this.setScoreOne(scoreOne);
		this.setScoreTwo(scoreTwo);
	}

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public int getScoreOne() {
		return scoreOne;
	}

	public void setScoreOne(int scoreOne) {
		this.scoreOne = scoreOne;
	}

	public int getScoreTwo() {
		return scoreTwo;
	}

	public void setScoreTwo(int scoreTwo) {
		this.scoreTwo = scoreTwo;
	}

	public int getBetId() {
		return betId;
	}

	public void setBetId(int betId) {
		this.betId = betId;
	}
	
	
}
