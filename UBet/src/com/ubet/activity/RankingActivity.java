package com.ubet.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ubet.R;
import com.ubet.client.UsersApi;
import com.ubet.content.UsersContent;
import com.ubet.util.UbetAccount;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class RankingActivity extends Activity {

	List<UsersContent> userNames = new ArrayList<UsersContent>();
	ListView listRanking;
	private Menu optionsMenu;

	UsersContentAdapter arrayAdapter = null;
	private RankingActivity thisActivity = this;

	private Context context;
	private Account account;
	private AccountManager accountManager;

	Handler handler = new Handler();
	UsersTask userTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ranking);

		context = getApplicationContext();
		accountManager = AccountManager.get(context);
		account = UbetAccount.getAccount(context);

		if (account == null)
			finish();

		userNames = new ArrayList<UsersContent>();

		listRanking = (ListView) findViewById(R.id.listView_ranking);

		showItens();
	}

	private void showItens() {

		arrayAdapter = new UsersContentAdapter(context,
				R.layout.users_list_ranking, userNames);

		listRanking.setAdapter(arrayAdapter);

		listRanking.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(thisActivity, ProfileActivity.class);
				intent.putExtra("username", userNames.get(position).getName());
				startActivity(intent);
			}
			
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		this.optionsMenu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_ranking_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}
	
	private void updateUsers(List<UsersContent> listOfUsers) {

		Collections.sort(listOfUsers, new UsersContent.sortByCoins());
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
					.findItem(R.id.ubet_menu_ranking_refresh);

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
				view = inflater.inflate(R.layout.users_list_ranking, null);
			}

			UsersContent item = getItem(position);
			if (item != null) {
				TextView itemViewUserName = (TextView) view
						.findViewById(R.id.user_name);
				TextView itemViewUserScore = (TextView) view
						.findViewById(R.id.user_coins);
				if (itemViewUserName != null) {
					itemViewUserName.setText(item.getName());
				}
				if (itemViewUserScore != null) {
					itemViewUserScore.setText("Coins: "
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

				List<UsersContent> listOfUsers = UsersApi.getAllUsers(context);
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