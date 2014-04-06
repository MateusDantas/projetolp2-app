package com.ubet.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import com.ubet.Constants;
import com.ubet.R;
import com.ubet.activity.RoomsActivity.RoomsContentAdapter;
import com.ubet.activity.RoomsActivity.RoomsTask;
import com.ubet.client.RoomsApi;
import com.ubet.client.UsersApi;
import com.ubet.content.RoomsContent;
import com.ubet.util.UbetAccount;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends Activity {

	List<RoomsContent> rooms = new ArrayList<RoomsContent>();
	ListView list;
	private Menu optionsMenu;

	RoomsContentAdapter arrayAdapter = null;

	private ProfileActivity thisActivity = this;

	private Context context;
	private Account account;
	private AccountManager accountManager;

	private String username;
	
	private TextView usernameView, ubetsView;
	
	private int userCoins;
	
	Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		Intent intent = getIntent();
		
		context = getApplicationContext();
		account = UbetAccount.getAccount(context);
		accountManager = AccountManager.get(context);
		rooms = new ArrayList<RoomsContent>();

		if (account == null) {
			finish();
		}
		checkAuthenticateUser();
		
		list = (ListView) findViewById(R.id.listView_rooms_registered);

		usernameView = (TextView) findViewById(R.id.textView_name);
		ubetsView = (TextView) findViewById(R.id.textView_ubets);
		
		userCoins = 0;
		
		username = intent.getExtras().getString("username");
		
		if (username == null) {
			finish();
		}
		
		ubetsView.setText("Coins: " + String.valueOf(userCoins));
		usernameView.setText("User: " + username);
		showItens();
		
		this.handler.postDelayed(checkRunnable, 500L);
	}

	private final Runnable checkRunnable = new Runnable() {
		public void run() {

			checkAuthenticateUser();
			setRefreshActionButtonState(true);
			RoomsTask newTask = new RoomsTask();
			UserTask userTask = new UserTask();
			userTask.executeOnExecutor(Executors.newSingleThreadExecutor());
			newTask.executeOnExecutor(Executors.newSingleThreadExecutor());
			handler.postDelayed(this, 60000L);
		}
	};

	public void checkAuthenticateUser() {

		if (account == null) {
			finish();
			return;
		}
		
		String nowToken = accountManager.peekAuthToken(account,
				Constants.AUTH_TOKEN_TYPE);
		if (nowToken == null) {
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

	private void showItens() {

		arrayAdapter = new RoomsContentAdapter(context, R.layout.rooms_list,
				rooms);

		list.setAdapter(arrayAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent intent = new Intent(thisActivity, RoomActivity.class);
				intent.putExtra("name", rooms.get(arg2).getRoomName());
				intent.putExtra("admin_name", rooms.get(arg2).getAdminName());
				intent.putExtra("room_id", rooms.get(arg2).getRoomId());
				intent.putExtra("price_room", rooms.get(arg2).getPriceRoom());
				intent.putExtra("people_inside", rooms.get(arg2)
						.getPeopleInside());
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		this.optionsMenu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_profile_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.ubet_menu_refresh:

			setRefreshActionButtonState(true);
			RoomsTask newTask = new RoomsTask();
			newTask.executeOnExecutor(Executors.newSingleThreadExecutor());
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void updateRooms(List<RoomsContent> listOfRooms) {

		arrayAdapter.clear();
		arrayAdapter.addAll(listOfRooms);
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
		setRefreshActionButtonState(false);
	}

	public class RoomsContentAdapter extends ArrayAdapter<RoomsContent> {

		private Context context;

		public RoomsContentAdapter(Context context, int textViewResourceId,
				List<RoomsContent> items) {
			super(context, textViewResourceId, items);
			this.context = context;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.rooms_list, null);
			}

			RoomsContent item = getItem(position);
			if (item != null) {
				TextView itemViewRoomName = (TextView) view
						.findViewById(R.id.room_name);
				TextView itemViewRoomAdminName = (TextView) view
						.findViewById(R.id.room_admin_name);
				if (itemViewRoomName != null) {
					itemViewRoomName.setText(item.getRoomName());
				}
				if (itemViewRoomAdminName != null) {
					itemViewRoomAdminName.setText(item.getAdminName());
				}
			}
			return view;
		}
	}

	private void onUserTaskResult(Integer coins) {
		// TODO Auto-generated method stub
		this.userCoins = coins;
		ubetsView.setText("Coins: " + String.valueOf(userCoins));
	}
	
	public class RoomsTask extends AsyncTask<Void, Void, List<RoomsContent>> {

		@Override
		protected List<RoomsContent> doInBackground(Void... params) {

			try {

				List<RoomsContent> listOfRooms = RoomsApi.getRoomsByUser(
						username, context);
				return listOfRooms;
			} catch (Exception e) {

				e.printStackTrace();
				Log.d("erro", e.getMessage().toString());
				return null;
			}
		}

		@Override
		protected void onPostExecute(final List<RoomsContent> listOfRooms) {

			if (listOfRooms == null) {
				onProcessFailed();
				return;
			}

			onListOfRoomsComplete(listOfRooms);
		}
	}
	
	public class UserTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {

			try {

				return UsersApi.getUserCoins(account, context, username);
			} catch (Exception e) {

				e.printStackTrace();
				//Log.d("erro", e.getMessage().toString());
				return 0;
			}
		}

		@Override
		protected void onPostExecute(final Integer coins) {


			onUserTaskResult(coins);
		}
		
	}
}