package com.ubet.activity;

import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.example.ubet.R;
import com.ubet.authenticator.AuthenticatorActivity;
import com.ubet.client.UbetApi;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;

public class RegisterActivity extends ActionBarActivity {

	String firstName, secondName, username, password, email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// When register button clicked
	public void register(View view) {
		int language = 0;
		EditText firstNameEdit = (EditText) findViewById(R.id.firstName);
		EditText secondNameEdit = (EditText) findViewById(R.id.secondName);
		EditText usernameEdit = (EditText) findViewById(R.id.username);
		EditText passwordEdit = (EditText) findViewById(R.id.password);
		EditText emailEdit = (EditText) findViewById(R.id.email);

		TextView messageView = (TextView) findViewById(R.id.message);

		firstName = firstNameEdit.getText().toString();
		secondName = secondNameEdit.getText().toString();
		username = usernameEdit.getText().toString();
		password = passwordEdit.getText().toString();
		email = emailEdit.getText().toString();

		if (TextUtils.isEmpty(firstName) || TextUtils.isEmpty(secondName)
				|| TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			messageView.setText(getMessage());
		} else {
			
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
		if (TextUtils.isEmpty(username)) {
			return getText(R.string.missing_username);
		}
		if (TextUtils.isEmpty(password)) {
			return getText(R.string.missing_password);
		}
		return null;
	}

	public void onRegisterResult(int resultCode) {
		
		TextView messageView = (TextView) findViewById(R.id.message);
		if (resultCode == 7) {
			Intent intent = new Intent(this, AuthenticatorActivity.class);
			startActivity(intent);
		} else {
			messageView.setText("Something went wrong! Try Again!");
		}
	}
	
	public class RegisterTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			
			try {
				InputStream instream = UbetApi.ubetRegister(firstName,secondName, email, username, password, 0);
				if (instream == null)
					return 0;
				Document doc = Jsoup.parse(instream, "UTF-8","http://ubet.herokuapp.com");
				int resultCode = Integer.valueOf(doc.select("div#returnCode").html());
				
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
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_register,
					container, false);
			return rootView;
		}
	}

}
