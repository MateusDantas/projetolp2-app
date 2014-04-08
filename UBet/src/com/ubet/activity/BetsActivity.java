package com.ubet.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

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
import android.widget.AdapterView.OnItemClickListener;

import com.ubet.R;
import com.ubet.client.GamesApi;
import com.ubet.content.GamesContent;
import com.ubet.util.UbetAccount;

public class BetsActivity extends FragmentActivity implements
		ActionBar.TabListener {

	AppSectionsPagerAdapter mAppSectionsPagerAdapter;


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
				
		try {
			if (roomId == -1) {
				actionBar.setTitle("Games");
			} else {
				actionBar.setTitle("Room: " + roomName);
			}
			actionBar.setHomeButtonEnabled(false);
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		} catch (Exception e) {
			finish();
		}
			
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
			try {
				actionBar.addTab(actionBar.newTab()
						.setText(mAppSectionsPagerAdapter.getPageTitle(i))
						.setTabListener(this));
			} catch (Exception e) {
				finish();
			}
		}
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
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
			return 7;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			if (position <= 2) {
				return "Mata-mata " + (position + 1);
			} else {
				switch (position) {
				case 3:
					return "Oitavas";
				case 4:
					return "Quartas";
				case 5:
					return "Semis";
				case 6:
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

		GamesTask newTask;

		Context nowContext;

		int round, roomId;
		String roomName;

		View rootView;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			rootView = inflater.inflate(R.layout.fragment_bets_rounds,
					container, false);
			Bundle args = getArguments();
			round = args.getInt("round");
			roomId = args.getInt("roomid");
			roomName = args.getString("roomname");
			nowContext = getActivity();

			list = (ListView) rootView.findViewById(R.id.listView_games);

			showItens();
			if (games.size() == 0) {
				final View linear = rootView.findViewById(R.id.loading_status);
				linear.setVisibility(View.VISIBLE);
				newTask = new GamesTask();
				newTask.executeOnExecutor(Executors.newSingleThreadExecutor());
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
						Intent intent = new Intent(nowContext,
								MakeBetActivity.class);
						intent.putExtra("round", round);
						intent.putExtra("roomid", roomId);
						intent.putExtra("gameid", games.get(arg2).getGameId());
						intent.putExtra("first_team_name", games.get(arg2)
								.getFirstTeamName());
						intent.putExtra("second_team_name", games.get(arg2)
								.getSecondTeamName());
						intent.putExtra("gamedate", games.get(arg2)
								.getFormattedDate());
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

			if (rootView == null)
				return;

			final View linear = rootView.findViewById(R.id.loading_status);
			linear.setVisibility(View.INVISIBLE);

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
					view = inflater.inflate(R.layout.games_list, null);
				}

				GamesContent item = getItem(position);
				if (item != null) {

					TextView firstTeamView = (TextView) view
							.findViewById(R.id.first_team_name);
					TextView secondTeamView = (TextView) view
							.findViewById(R.id.second_team_name);
					TextView dateView = (TextView) view
							.findViewById(R.id.game_date);
					TextView scoreOneView = (TextView) view
							.findViewById(R.id.score_team_one);
					TextView scoreTwoView = (TextView) view
							.findViewById(R.id.score_team_two);

					if (firstTeamView != null)
						firstTeamView.setText(item.getFirstTeamName());

					if (secondTeamView != null)
						secondTeamView.setText(item.getSecondTeamName());

					if (dateView != null)
						dateView.setText(item.getFormattedDate());
					
					if (scoreOneView != null) {
						if (item.getFirstTeamScore() >= 0)
							scoreOneView.setText(String.valueOf(item.getFirstTeamScore()));
						else
							scoreOneView.setText("");
					}
					
					if (scoreTwoView != null) {
						if (item.getSecondTeamScore() >= 0)
							scoreTwoView.setText(String.valueOf(item.getSecondTeamScore()));
						else
							scoreTwoView.setText("");
					}
				}
				return view;
			}
		}

	}

}
