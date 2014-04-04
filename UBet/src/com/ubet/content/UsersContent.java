package com.ubet.content;

public class UsersContent {

	private String name;
	private int coins;
	private int score;
	
	public UsersContent(String username, int coins, int score) {
		this.setName(username);
		this.setCoins(coins);
		this.setScore(score);
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}
}
