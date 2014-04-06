package com.ubet.content;

import java.util.Comparator;



public class UsersContent {

	private String name;
	private int coins;
	private int score;
	
	public UsersContent(String username, int coins, int score) {
		this.setName(username);
		this.setCoins(coins);
		this.setScore(score);
	}

	public static class sortByCoins implements Comparator<UsersContent> {
		
		@Override
		public int compare(UsersContent userOne, UsersContent userTwo) {
			return userTwo.getCoins() - userOne.getCoins();
		}
	}

	public static class sortByScore implements Comparator<UsersContent> {

		@Override
		public int compare(UsersContent userOne, UsersContent userTwo) {
			// TODO Auto-generated method stub
			return userTwo.getScore() - userOne.getScore();
		}
		
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
