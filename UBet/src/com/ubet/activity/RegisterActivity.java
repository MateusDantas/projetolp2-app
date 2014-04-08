package com.ubet.activity;

import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.ubet.R;
import com.ubet.authenticator.AuthenticatorActivity;
import com.ubet.client.UbetApi;
import com.ubet.util.UbetAccount;

import android.app.Activity;
import android.text.TextUtils;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity {

	private static final int NO_ERROR = 7;
	private static final String LOADING = "Loading...";
	String firstName, secondName, username, password, email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		redirectIfIsLoggedIn();
		
		setContentView(R.layout.activity_register);
	}

	public void redirectIfIsLoggedIn() {
		
		Context context = getApplicationContext();
		if (UbetAccount.isUserLoggedIn(context)) {
			final Intent intent = new Intent(context, RoomsActivity.class);
			startActivity(intent);
			finish();
		}
	}

	// When register button clicked
	public void register(View view) {

		EditText firstNameEdit = (EditText) findViewById(R.id.firstName);
		EditText secondNameEdit = (EditText) findViewById(R.id.secondName);
		EditText usernameEdit = (EditText) findViewById(R.id.username);
		EditText passwordEdit = (EditText) findViewById(R.id.password);
		EditText emailEdit = (EditText) findViewById(R.id.email);

		firstName = firstNameEdit.getText().toString();
		secondName = secondNameEdit.getText().toString();
		username = usernameEdit.getText().toString();
		password = passwordEdit.getText().toString();
		email = emailEdit.getText().toString();

		if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(secondName)
				|| TextUtils.isEmpty(username) || TextUtils.isEmpty(password)
				|| TextUtils.isEmpty(email)) {
			Toast.makeText(getApplicationContext(), getMessage(), Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(getApplicationContext(), LOADING, Toast.LENGTH_SHORT).show();
			RegisterTask registerUser = new RegisterTask();
			registerUser.execute();
		}
	}

	public CharSequence getMessage() {
		if (TextUtils.isEmpty(firstName)) {
			return getText(R.string.missing_firstname);
		}
		if (TextUtils.isEmpty(secondName)) {
			return getText(R.string.missing_secondname);
		}
		if (TextUtils.isEmpty(email)) {
			return getText(R.string.missing_email);
		}
		if (TextUtils.isEmpty(username)) {
			return getText(R.string.missing_username);
		}
		if (TextUtils.isEmpty(password)) {
			return getText(R.string.missing_password);
		}
		
		return null;
	}

	public CharSequence getError(int pId) {
		String errorString = "erro" + String.valueOf(pId);
		
		String packageName = getPackageName();
	    int resId = getResources()
	            .getIdentifier(errorString, "string", packageName);
	    
		return getString(resId);
	}
	
	public void onRegisterResult(int resultCode) {

		if (resultCode == NO_ERROR) {
			Intent intent = new Intent(this, AuthenticatorActivity.class);
			startActivity(intent);
		} else {
			Toast.makeText(getApplicationContext(), getError(resultCode), Toast.LENGTH_SHORT).show();
		}
	}

	public class RegisterTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {

			try {
				InputStream instream = UbetApi.ubetRegister(firstName,
						secondName, email, username, password, 0);
				if (instream == null)
					return 0;
				Document doc = Jsoup.parse(instream, "UTF-8",
						"http://ubet.herokuapp.com");
				int resultCode = Integer.valueOf(doc.select("div#returnCode")
						.html());

				return resultCode;
			} catch (Exception e) {

				e.printStackTrace();
				return 0;
			}
		}

		@Override
		protected void onPostExecute(final Integer resultCode) {

			onRegisterResult(resultCode);
		}

	}
}
