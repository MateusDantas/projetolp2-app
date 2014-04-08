package com.ubet.activity;

import com.ubet.R;
import com.ubet.authenticator.AuthenticatorActivity;
import com.ubet.util.UbetAccount;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		redirectIfIsLoggedIn();
		
		setContentView(R.layout.activity_start);
	}

	public void redirectIfIsLoggedIn() {
		
		Context context = getApplicationContext();
		if (UbetAccount.isUserLoggedIn(context)) {
			final Intent intent = new Intent(context, RoomsActivity.class);
			startActivity(intent);
			finish();
		}
	}
	
	//When login button clicked
	public void goLogin(View view) {
		Intent intent = new Intent(this, AuthenticatorActivity.class);

		startActivity(intent);
	}

	//When register button clicked
	public void goRegister(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);

		startActivity(intent);
	}
}
