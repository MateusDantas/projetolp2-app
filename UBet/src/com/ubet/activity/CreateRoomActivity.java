package com.ubet.activity;

import com.ubet.Constants;
import com.ubet.R;
import com.ubet.client.RoomsApi;
import com.ubet.util.UbetAccount;

import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CreateRoomActivity extends ActionBarActivity {

	private static final String LOADING = "Loading...";
	public EditText nameEdit, passwordEdit, priceRoomEdit;
	public EditText priceExtraEdit, limExtraEdit;

	public TextView message;

	public String name, password;
	public String priceRoom, priceExtra, limExtra;

	private Account account;
	private Context context;

	Handler handler = new Handler();
	
	CreateRoomTask roomTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_create_room);

		context = getApplicationContext();
		account = UbetAccount.getAccount(context);

		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

		this.handler = new Handler();
		this.handler.postDelayed(checkRunnable, 30000L);
	}

	private final Runnable checkRunnable = new Runnable() {
		public void run() {
			
			checkAuthenticateUser();
			handler.postDelayed(checkRunnable, 30000L);
		}
	};
	
	void killThemAll() {
		
		if (roomTask != null)
			roomTask.cancel(true);
		finish();
	}
	
	public void checkAuthenticateUser() {
    	

		
		if (account == null) {
			killThemAll();
			return;
		}
		
    	AccountManager accountManager = AccountManager.get(context);
    	
		if (accountManager.getAccountsByType(Constants.ACCOUNT_TYPE).length == 0) {
			killThemAll();
			return;
		}
    	
    	String nowToken = accountManager.peekAuthToken(account, Constants.AUTH_TOKEN_TYPE);
    	if (nowToken == null) {
    		accountManager.removeAccount(account, null, null);
    		killThemAll();
    	}
    }
	

	public void createRoom(View view) {

	
		nameEdit = (EditText) findViewById(R.id.create_room_name);
		passwordEdit = (EditText) findViewById(R.id.create_room_password);
		priceRoomEdit = (EditText) findViewById(R.id.create_room_price_room);
		priceExtraEdit = (EditText) findViewById(R.id.create_room_price_extra);
		limExtraEdit = (EditText) findViewById(R.id.create_room_lim_extra);
		
		try {
			name = nameEdit.getText().toString();
			password = passwordEdit.getText().toString();
			priceRoom = priceRoomEdit.getText().toString();
			priceExtra = priceExtraEdit.getText().toString();
			limExtra = limExtraEdit.getText().toString();
		} catch (NullPointerException e) {
			finish();
		}

		if (!TextUtils.isEmpty(getMessage())) {
			showMessage(getMessage());
		} else {
			showMessage(LOADING);
			roomTask = new CreateRoomTask();
			roomTask.execute();
		}
	}

	private void showMessage(String text) {
		Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
	}

	public String getMessage() {

		if (TextUtils.isEmpty(name)) {
			return getString(R.string.missing_room_name);
		}

		if (TextUtils.isEmpty(password)) {
			return getString(R.string.missing_password);
		}

		if (TextUtils.isEmpty(priceRoom)) {
			return getString(R.string.missing_room_price_room);
		}

		if (TextUtils.isEmpty(priceExtra)) {
			return getString(R.string.missing_room_price_extra);
		}

		if (TextUtils.isEmpty(limExtra)) {
			return getString(R.string.missing_room_lim_extra);
		}

		return null;
	}

	public String getError(int pId) {

		String errorString = "erro" + String.valueOf(pId);

		String packageName = getPackageName();
		int resId = getResources().getIdentifier(errorString, "string",
				packageName);

		return getString(resId);

	}

	private void onCreateRoomResult(Integer resultCode) {

	if (resultCode == 7) {

			final Intent intent = new Intent(this, RoomsActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			killThemAll();
		} else {
			showMessage(getError(resultCode));
		}
	}

	public class CreateRoomTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				return RoomsApi.createRoom(name, account.name,
						Integer.valueOf(priceRoom),
						Integer.valueOf(priceExtra), Integer.valueOf(limExtra),
						password, context);

			} catch (Exception e) {

				Log.d("EXCP", e.getMessage().toString());
				return 26;
			}
		}

		@Override
		protected void onPostExecute(final Integer resultCode) {

			onCreateRoomResult(resultCode);
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
			View rootView = inflater.inflate(R.layout.fragment_create_room,
					container, false);
			return rootView;
		}
	}

}
