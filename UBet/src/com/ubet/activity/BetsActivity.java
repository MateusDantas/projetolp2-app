package com.ubet.activity;

import java.util.ArrayList;
import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.ubet.R;
import com.ubet.activity.ProfileActivity.RoomsContentAdapter;
import com.ubet.client.GamesApi;
import com.ubet.client.RoomsApi;
import com.ubet.content.GamesContent;
import com.ubet.content.RoomsContent;
import com.ubet.util.UbetAccount;

public class BetsActivity extends FragmentActivity implements
		ActionBar.TabListener {

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	private BetsActivity thisActivity = this;
	
	ViewPager mViewPager;

	Context context;
	Account account;
	AccountManager accountManager;
	static int roomId = -1;
	static String roomName = "";

	public void onCreate(Bundle savedInstance) {
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_bets);

		context = getApplicationContext();
		account = UbetAccount.getAccount(context);
		accountManager = AccountManager.get(context);

		roomId = -1;

		if (account == null)
			finish();

		if (context == null)
			finish();

		Intent intent = getIntent();

		if (intent.getExtras() != null) {

			roomId = intent.getExtras().getInt("roomid");
			roomName = intent.getExtras().getString("roomname");

			if (roomName == null) {
				roomId = -1;
				roomName = "";
			}
		}

		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(
				getSupportFragmentManager(), roomId, roomName);

		final ActionBar actionBar = getActionBar();

		if (roomId == -1)
			actionBar.setTitle("Games");
		else
			actionBar.setTitle("Room: " + roomName);

		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
			actionBar.addTab(actionBar.newTab()
					.setText(mAppSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// TODO Auto-generated method stub

	}

	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		int roomId;
		String roomName;

		public AppSectionsPagerAdapter(FragmentManager fm, int roomId,
				String roomName) {
			super(fm);
			this.roomId = roomId;
			this.roomName = roomName;
		}

		@Override
		public Fragment getItem(int pos) {

			Fragment fragment = new GamesSectionFragment();
			Bundle args = new Bundle();
			args.putInt("round", pos + 1);
			args.putInt("roomid", this.roomId);
			args.putString("roomname", this.roomName);
			fragment.setArguments(args);

			return fragment;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 10;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position <= 5) {
				return "Mata-mata " + (position + 1);
			} else {
				switch (position) {
				case 6:
					return "Oitavas";
				case 7:
					return "Quartas";
				case 8:
					return "Semis";
				case 9:
					return "Final";

				default:
					return "Final";
				}

			}
		}

	}

	public static class GamesSectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		List<GamesContent> games = new ArrayList<GamesContent>();
		ListView list;
		GamesContentAdapter arrayAdapter = null;

		private GamesSectionFragment thisFragment = this;

		Context nowContext;

		int round, roomId;
		String roomName;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_bets_rounds,
					container, false);
			Bundle args = getArguments();
			round = args.getInt("round");
			roomId = args.getInt("roomid");
			roomName = args.getString("roomname");
			nowContext = getActivity();

			list = (ListView) rootView.findViewById(R.id.listView_games);

			showItens();
			if (games.size() == 0) {
				GamesTask newTask = new GamesTask();
				newTask.execute();
			}
			return rootView;
		}

		private void showItens() {

			arrayAdapter = new GamesContentAdapter(getActivity(),
					R.layout.games_list, games);

			list.setAdapter(arrayAdapter);

			list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					if (roomId >= 0) {
						Log.d("opa", "sim");
						Intent intent = new Intent(nowContext, MakeBetActivity.class);
						intent.putExtra("round", round);
						intent.putExtra("roomid", roomId);
						intent.putExtra("gameid", games.get(arg2).getGameId());
						intent.putExtra("first_team_name", games.get(arg2).getFirstTeamName());
						intent.putExtra("second_team_name", games.get(arg2).getSecondTeamName());
						intent.putExtra("gamedate", games.get(arg2).getFormattedDate());
						intent.putExtra("roomname", roomName);
						
						startActivity(intent);
					}
				}
			});
		}

		public void onListOfGamesComplete(List<GamesContent> listOfGames) {
			// TODO Auto-generated method stub
			if (listOfGames == null)
				return;

			arrayAdapter.clear();
			arrayAdapter.addAll(listOfGames);
			arrayAdapter.notifyDataSetChanged();
		}

		public class GamesTask extends
				AsyncTask<Void, Void, List<GamesContent>> {

			@Override
			protected List<GamesContent> doInBackground(Void... params) {

				try {
					Log.d("round " + String.valueOf(round), "ok");
					List<GamesContent> listOfGames = GamesApi.gamesByRound(
							round, nowContext);
					return listOfGames;
				} catch (Exception e) {

					e.printStackTrace();
					Log.d("erro", e.getMessage().toString());
					return null;
				}
			}

			@Override
			protected void onPostExecute(final List<GamesContent> listOfGames) {

				onListOfGamesComplete(listOfGames);
			}
		}

		public class GamesContentAdapter extends ArrayAdapter<GamesContent> {

			private Context context;

			int textViewResourceId;

			public GamesContentAdapter(Context context, int textViewResourceId,
					List<GamesContent> items) {
				super(context, textViewResourceId, items);
				this.context = context;
				this.textViewResourceId = textViewResourceId;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				View view = convertView;
				if (view == null) {
					LayoutInflater inflater = (LayoutInflater) context
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					view = inflater.inflate(this.textViewResourceId, null);
				}

				GamesContent item = getItem(position);
				if (item != null) {

					TextView firstTeamView = (TextView) view
							.findViewById(R.id.first_team_name);
					TextView secondTeamView = (TextView) view
							.findViewById(R.id.second_team_name);
					TextView dateView = (TextView) view
							.findViewById(R.id.game_date);

					if (firstTeamView != null)
						firstTeamView.setText(item.getFirstTeamName());

					if (secondTeamView != null)
						secondTeamView.setText(item.getSecondTeamName());

					if (dateView != null)
						dateView.setText(item.getFormattedDate());
				}
				return view;
			}
		}

	}

}
