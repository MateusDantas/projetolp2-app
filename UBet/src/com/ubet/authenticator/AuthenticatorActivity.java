package com.ubet.authenticator;

import com.ubet.Constants;
import com.example.ubet.R;
import com.ubet.activity.RoomsActivity;
import com.ubet.activity.TestActivity;
import com.ubet.client.UbetAccount;
import com.ubet.client.UbetApi;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorActivity;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class AuthenticatorActivity extends AccountAuthenticatorActivity {

	public static final String CONFIRM_CREDENTIALS_PARAM = "confirmCredentials";
	public static final String USERNAME_PARAM = "username";

	private AccountManager accountManager;
	private UserLoginTask authTask = null;

	private String username;
	private String password;

	private TextView message;
	private EditText passwordEdit, usernameEdit;

	@Override
	public void onCreate(Bundle instanceBundle) {

		super.onCreate(instanceBundle);

		redirectIfIsLoggedIn();

		accountManager = AccountManager.get(this);

		final Intent intent = getIntent();

		username = intent.getStringExtra(USERNAME_PARAM);

		setContentView(R.layout.activity_authenticator);

		usernameEdit = (EditText) findViewById(R.id.username);
		passwordEdit = (EditText) findViewById(R.id.password);
		message = (TextView) findViewById(R.id.message);

		if (username != null && !TextUtils.isEmpty(username)) {
			usernameEdit.setText(username);
			passwordEdit.requestFocus();
		}

	}

	public void handleLogin(View view) {

		password = passwordEdit.getText().toString();
		username = usernameEdit.getText().toString();

		redirectIfIsLoggedIn();

		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			message.setText(getMessage());
		} else {
			Log.d("LOGIN", "handling log");
			showProgress();
			authTask = new UserLoginTask();
			authTask.execute();
		}
	}

	protected void finishLogin(String authToken) {

		final Account account = new Account(username, Constants.ACCOUNT_TYPE);

		accountManager.addAccountExplicitly(account, null, null);
		accountManager.setAuthToken(account, Constants.AUTH_TOKEN_TYPE,
				authToken);

		final Intent intent = new Intent();

		intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
		intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
		intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);

		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);

		finish();
		// changed to rooms activity
		Intent newIntent = new Intent(this, RoomsActivity.class);
		newIntent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
	
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(newIntent);
		finish();
	}

	private CharSequence getMessage() {

		if (TextUtils.isEmpty(username)) {
			return getText(R.string.missing_username);
		}
		if (TextUtils.isEmpty(password)) {
			return getText(R.string.missing_password);
		}
		return null;
	}

	public void onAuthenticationResult(String authToken) {
		authTask = null;
		hideProgress();
		if (authToken != null) {
			finishLogin(authToken);
		} else {
			message.setText(getText(R.string.login_fail));
		}
	}

	public void onAuthenticationCancel() {
		authTask = null;
		hideProgress();
	}

	public void cancel() {
		if (authTask != null) {
			authTask.cancel(true);
			finish();
		}
	}

	public void redirectIfIsLoggedIn() {

		Context context = getApplicationContext();
		if (UbetAccount.isUserLoggedIn(context)) {
			//changed to rooms activity
			final Intent intent = new Intent(context, RoomsActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

	private void showProgress() {

		final View linear = findViewById(R.id.login_status);
		final View loginForm = findViewById(R.id.login_form);
		linear.setVisibility(View.VISIBLE);
		loginForm.setVisibility(View.INVISIBLE);
	}

	private void hideProgress() {

		final View linear = findViewById(R.id.login_status);
		final View loginForm = findViewById(R.id.login_form);
		linear.setVisibility(View.GONE);
		loginForm.setVisibility(View.VISIBLE);

	}

	public class UserLoginTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {

			try {
				Log.d("TUDO OK", "de boa vei");
				String authToken = UbetApi.authenticateUser(username, password);
				if (authToken == null)
					Log.d("TOKEN", "null found");
				return authToken;
			} catch (Exception e) {
				Log.d("EXCEPTION", e.getMessage().toString());
				return null;
			}
		}

		@Override
		protected void onPostExecute(final String authToken) {
			onAuthenticationResult(authToken);
		}

		@Override
		protected void onCancelled() {
			onAuthenticationCancel();
		}

	}

}
