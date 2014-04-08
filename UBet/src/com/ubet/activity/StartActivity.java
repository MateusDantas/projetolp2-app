package com.ubet.activity;

import com.ubet.R;
import com.ubet.authenticator.AuthenticatorActivity;
import com.ubet.util.UbetAccount;

import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.accounts.Account;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.os.Build;

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
