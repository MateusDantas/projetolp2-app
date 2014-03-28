package com.ubet.content;

public class UsersContent {

	private static String name;
	public UsersContent(String username) {
		UsersContent.setName(username);
	}

	public static String getName() {
		return name;
	}

	private static void setName(String name) {
		UsersContent.name = name;
	}
}
