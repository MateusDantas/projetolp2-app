package com.ubet.authenticator;

import com.ubet.Constants;
import com.ubet.client.UbetApi;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AccountAuthenticator extends AbstractAccountAuthenticator {

	private final Context context;

	public AccountAuthenticator(Context context) {
		super(context);
		this.context = context;
	}
	

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
			String accountType, String authTokenType,
			String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {

		final Bundle result;
		final Intent intent;

		intent = new Intent(this.context, AuthenticatorActivity.class);
		intent.putExtra(Constants.AUTH_TOKEN_TYPE, authTokenType);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
				response);

		result = new Bundle();
		result.putParcelable(AccountManager.KEY_INTENT, intent);

		return result;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse response,
			Account account, Bundle options) throws NetworkErrorException {

		if (options == null
				|| !options.containsKey(AccountManager.KEY_PASSWORD)) {

			final Intent intent = new Intent(context,
					AuthenticatorActivity.class);
			intent.putExtra(AuthenticatorActivity.USERNAME_PARAM, account.name);
			intent.putExtra(AuthenticatorActivity.CONFIRM_CREDENTIALS_PARAM,
					true);
			intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE,
					response);

			final Bundle bundle = new Bundle();
			bundle.putParcelable(AccountManager.KEY_INTENT, intent);

			return bundle;
		}

		final String password = options.getString(AccountManager.KEY_PASSWORD);
		final Bundle bundle = new Bundle();
		bundle.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
		if (getAuthToken(account.name, password) != null)
			bundle.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, true);

		return bundle;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse response,
			String accountType) {
		
		throw new UnsupportedOperationException();
	}
	
	private String getAuthToken(String username, String password) {
		// TODO Auto-generated method stub
		try {
			return UbetApi.authenticateUser(username, password);
		} catch (Exception e){
			return null;
		}
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		
		if (!authTokenType.equals(Constants.AUTH_TOKEN_TYPE)) {
			final Bundle bundle = new Bundle();
			bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid token");
			
			return bundle;
		}
		
		final AccountManager accountManager = AccountManager.get(context);
		final String password = accountManager.getPassword(account);
		accountManager.setPassword(account, null);
		
		if (password != null) {
			final String authToken = getAuthToken(account.name, password);
			if (authToken != null) {
				accountManager.setAuthToken(account, Constants.AUTH_TOKEN_TYPE, authToken);
				
				final Bundle bundle = new Bundle();
				bundle.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
				bundle.putString(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
				bundle.putString(AccountManager.KEY_AUTHTOKEN, authToken);
				
				return bundle;
			}
		}
		
		final Intent intent = new Intent(context, AuthenticatorActivity.class);
		intent.putExtra(AuthenticatorActivity.USERNAME_PARAM, account.name);
		intent.putExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		
		final Bundle bundle = new Bundle();
		bundle.putParcelable(AccountManager.KEY_INTENT, intent);
		
		return bundle;
	}

	@Override
	public String getAuthTokenLabel(String authTokenType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		/**
		 * call activity class
		 */
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse response,
			Account account, String[] features) throws NetworkErrorException {
		final Bundle bundle = new Bundle();
		bundle.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
		return bundle;
	}
	
	

}
