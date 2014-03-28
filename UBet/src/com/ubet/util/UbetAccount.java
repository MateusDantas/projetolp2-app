package com.ubet.util;

import com.ubet.Constants;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class UbetAccount {

	public static Account getAccount(Context context) {
		
		AccountManager accountManager = AccountManager.get(context);
		Account [] accounts = accountManager.getAccountsByType(Constants.ACCOUNT_TYPE);
		
		if (accounts.length == 0)
			return null;
		
		return accounts[0];
	}
	
	public static boolean isUserLoggedIn(Context context) {
		
		if (getAccount(context) != null)
			return true;
		
		return false;
	}
}
