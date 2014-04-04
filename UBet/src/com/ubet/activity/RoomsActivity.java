package com.ubet.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.http.auth.AuthenticationException;

import com.ubet.Constants;
import com.ubet.R;
import com.ubet.authenticator.AuthenticatorActivity;
import com.ubet.client.RoomsApi;
import com.ubet.client.UsersApi;
import com.ubet.content.RoomsContent;
import com.ubet.util.UbetAccount;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Toast;

public class RoomsActivity extends Activity {

	List<String> rooms = new ArrayList<String>();
	ListView list;
	RoomsActivity thisActivity = this;
	private Menu optionsMenu;

	ArrayAdapter<String> arrayAdapter = null;

	private Context context;
	private Account account;
	private AccountManager accountManager;

	Handler handler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rooms);

		context = getApplicationContext();
		account = UbetAccount.getAccount(context);
		accountManager = AccountManager.get(context);
		rooms = new ArrayList<String>();

		list = (ListView) findViewById(R.id.listView_rooms);

		showRooms();

		setRefreshActionButtonState(true);
		RoomsTask newTask = new RoomsTask();
		newTask.executeOnExecutor(Executors.newSingleThreadExecutor());

		this.handler.postDelayed(checkRunnable, 60000L);
	}

	private final Runnable checkRunnable = new Runnable() {
		public void run() {

			checkAuthenticateUser();
			setRefreshActionButtonState(true);
			RoomsTask newTask = new RoomsTask();
			newTask.executeOnExecutor(Executors.newSingleThreadExecutor());
			handler.postDelayed(this, 60000L);
		}
	};

	public void checkAuthenticateUser() {

		AccountManager accountManager = AccountManager.get(context);
		String nowToken = accountManager.peekAuthToken(account,
				Constants.AUTH_TOKEN_TYPE);
		if (nowToken == null) {
			Log.d("u.","1");
			this.handler.removeCallbacks(checkRunnable);
			accountManager.removeAccount(account, null, null);
			final Intent intent = new Intent(this, StartActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}

	private void showRooms() {

		arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.rooms_list, rooms);

		list.setAdapter(arrayAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(thisActivity, RoomActivity.class);
				intent.putExtra("name", rooms.get(arg2));
				startActivity(intent);
			}
		});

	}

	// When ranking button clicked
	public void goRanking(View view) {
		Intent intent = new Intent(this, RankingActivity.class);

		startActivity(intent);
	}

	// When Profile button clicked
	public void goProfile(View view) {
		Intent intent = new Intent(this, ProfileActivity.class);

		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.optionsMenu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_room_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.ubet_menu_refresh:
			
			setRefreshActionButtonState(true);
			RoomsTask newTask = new RoomsTask();
			newTask.executeOnExecutor(Executors.newSingleThreadExecutor());
			return true;
		case R.id.ubet_create_room:
			final Intent intent = new Intent(this, CreateRoomActivity.class);
			startActivity(intent);
			return true;
		case R.id.ubet_menu_logout:
			LogoutTask logoutTask = new LogoutTask();
			logoutTask.executeOnExecutor(Executors.newSingleThreadExecutor());
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void updateRooms(List<RoomsContent> listOfRooms) {

		arrayAdapter.clear();

		List<String> newRooms = new ArrayList<String>();
		for (RoomsContent room : listOfRooms) {
			Log.d("sala", room.getRoomName());
			newRooms.add(room.getRoomName());
		}

		arrayAdapter.addAll(newRooms);
		arrayAdapter.notifyDataSetChanged();
	}

	private void onListOfRoomsComplete(List<RoomsContent> listOfRooms) {

		updateRooms(listOfRooms);
		setRefreshActionButtonState(false);
	}

	public void setRefreshActionButtonState(final boolean refreshing) {
		if (optionsMenu != null) {
			final MenuItem refreshItem = optionsMenu
					.findItem(R.id.ubet_menu_refresh);

			if (refreshItem != null) {
				if (refreshing) {
					refreshItem
							.setActionView(R.layout.android_refresh_progress);
				} else {
					refreshItem.setActionView(null);
				}
			}
		}
	}

	private void onProcessFailed() {
		// TODO Auto-generated method stub

		checkAuthenticateUser();

	}

	private void onUserLogoutResult(Integer resultCode) {
		// TODO Auto-generated method stub
		if (resultCode == 1) {
			Toast.makeText(context, "Cya!", Toast.LENGTH_SHORT).show();
			this.handler.removeCallbacks(checkRunnable);
			accountManager.removeAccount(account, null, null);
			final Intent intent = new Intent(this, StartActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		} else {
			Toast.makeText(context, "You shall not pass!", Toast.LENGTH_SHORT)
					.show();
		}
	}

	public class LogoutTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {

				return UsersApi.logoutUser(account, context);
			} catch (Exception e) {

				return 26;
			}
		}

		@Override
		protected void onPostExecute(final Integer resultCode) {

			onUserLogoutResult(resultCode);
		}

	}

	public class RoomsTask extends AsyncTask<Void, Void, List<RoomsContent>> {

		@Override
		protected List<RoomsContent> doInBackground(Void... params) {

			try {
				Log.d("doing", "yeah");
				if (account == null)
					Log.d("we may have a problem, sir", "what?");
				List<RoomsContent> listOfRooms = RoomsApi.getRoomsByUser(
						account.name, context);
				return listOfRooms;
			} catch (Exception e) {

				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(final List<RoomsContent> listOfRooms) {

			if (listOfRooms == null) {
				Log.d("something went wrong", "oh yeah");
				onProcessFailed();
				return;
			}

			onListOfRoomsComplete(listOfRooms);
		}

	}

}
