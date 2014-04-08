package com.ubet.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;

import com.ubet.Constants;
import com.ubet.R;
import com.ubet.client.RoomsApi;
import com.ubet.content.UsersContent;
import com.ubet.util.UbetAccount;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class RoomInside extends Activity {

	List<UsersContent> users = new ArrayList<UsersContent>();
	ListView list;
	private Menu optionsMenu;

	UsersContentAdapter arrayAdapter = null;

	private RoomInside thisActivity = this;

	private Context context;
	private Account account;
	private AccountManager accountManager;

	private int roomId;
	private String adminName, roomName;
	private int priceExtra, limExtra;

	Handler handler = new Handler();

	UsersTask userTask;
	UsersTask newTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_inside);

		context = getApplicationContext();
		accountManager = AccountManager.get(context);
		account = UbetAccount.getAccount(context);

		checkAuthenticateUser();
		getIntentResults();

		TextView roomNameView = (TextView) findViewById(R.id.room_name);
		TextView adminNameView = (TextView) findViewById(R.id.admin_name);

		roomNameView.setText(roomName);
		adminNameView.setText("Admin: " + adminName);

		list = (ListView) findViewById(R.id.listView_users_registered);
		showItens();

		this.handler.postDelayed(checkRunnable, 500L);
	}

	private void getIntentResults() {

		Intent intent = getIntent();

		if (intent.getExtras() == null) {
			killThemAll();
		}
		
		try {
			roomId = intent.getExtras().getInt("roomid");
			adminName = intent.getExtras().getString("admin_name");
			roomName = intent.getExtras().getString("room_name");
			priceExtra = intent.getExtras().getInt("price_extra");
			limExtra = intent.getExtras().getInt("lim_extra");
		} catch (NullPointerException e) {
			killThemAll();
		}

		if (roomId == 0 || adminName == null || roomName == null) {
			killThemAll();
		}
	}

	private final Runnable checkRunnable = new Runnable() {
		public void run() {

			checkAuthenticateUser();
			setRefreshActionButtonState(true);
			userTask = new UsersTask();
			userTask.executeOnExecutor(Executors.newSingleThreadExecutor());
			handler.postDelayed(this, 60000L);
		}
	};

	void killThemAll() {

		if (userTask != null)
			userTask.cancel(true);
		if (newTask != null)
			newTask.cancel(true);
		finish();
	}

	public void checkAuthenticateUser() {

		if (accountManager.getAccountsByType(Constants.ACCOUNT_TYPE).length == 0) {
			killThemAll();
			return;
		}

		if (account == null) {
			killThemAll();
			return;
		}

		String nowToken = accountManager.peekAuthToken(account,
				Constants.AUTH_TOKEN_TYPE);
		if (nowToken == null) {
			this.handler.removeCallbacks(checkRunnable);
			accountManager.removeAccount(account, null, null);
			killThemAll();
		}
	}

	private void showItens() {

		arrayAdapter = new UsersContentAdapter(context, R.layout.users_list,
				users);

		list.setAdapter(arrayAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(thisActivity, ProfileActivity.class);
				intent.putExtra("username", users.get(arg2).getName());
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		this.optionsMenu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_room_inside_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.ubet_menu_room_inside_refresh:

			setRefreshActionButtonState(true);
			newTask = new UsersTask();
			newTask.executeOnExecutor(Executors.newSingleThreadExecutor());
			return true;
		case R.id.ubet_menu_room_inside_make_bet:
			
			Intent intent = new Intent(this, BetsActivity.class);
			intent.putExtra("roomid", roomId);
			intent.putExtra("roomname", roomName);
			startActivity(intent);
			return true;
		case R.id.ubet_menu_room_inside_info:
			
			Intent newIntent = new Intent(this, RoomInfo.class);
			newIntent.putExtra("roomname", roomName);
			newIntent.putExtra("adminname", adminName);
			newIntent.putExtra("price_extra", priceExtra);
			newIntent.putExtra("lim_extra", limExtra);
			startActivity(newIntent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void updateUsers(List<UsersContent> listOfUsers) {

		Collections.sort(listOfUsers, new UsersContent.sortByScore());
		arrayAdapter.clear();
		arrayAdapter.addAll(listOfUsers);
		arrayAdapter.notifyDataSetChanged();
	}

	private void onListOfUsersComplete(List<UsersContent> listOfUsers) {

		updateUsers(listOfUsers);
		setRefreshActionButtonState(false);
	}

	public void setRefreshActionButtonState(final boolean refreshing) {
		if (optionsMenu != null) {
			final MenuItem refreshItem = optionsMenu
					.findItem(R.id.ubet_menu_room_inside_refresh);

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
		setRefreshActionButtonState(false);
	}

	public class UsersContentAdapter extends ArrayAdapter<UsersContent> {

		private Context context;

		public UsersContentAdapter(Context context, int textViewResourceId,
				List<UsersContent> items) {
			super(context, textViewResourceId, items);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.users_list, null);
			}

			UsersContent item = getItem(position);
			if (item != null) {
				TextView itemViewUserName = (TextView) view
						.findViewById(R.id.user_name);
				TextView itemViewUserScore = (TextView) view
						.findViewById(R.id.user_score);
				if (itemViewUserName != null) {
					itemViewUserName.setText(item.getName());
				}
				if (itemViewUserScore != null) {
					itemViewUserScore.setText("Score: "
							+ String.valueOf(item.getScore()));
				}
			}
			return view;
		}
	}

	public class UsersTask extends AsyncTask<Void, Void, List<UsersContent>> {

		@Override
		protected List<UsersContent> doInBackground(Void... params) {

			try {

				List<UsersContent> listOfUsers = RoomsApi.getUsersInRoom(
						roomId, context);
				return listOfUsers;
			} catch (Exception e) {

				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(final List<UsersContent> listOfUsers) {

			if (listOfUsers == null) {
				onProcessFailed();
				return;
			}

			onListOfUsersComplete(listOfUsers);
		}
	}

}
