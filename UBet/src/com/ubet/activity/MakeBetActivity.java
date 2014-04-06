package com.ubet.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.Executors;

import com.ubet.R;
import com.ubet.R.id;
import com.ubet.R.layout;
import com.ubet.R.menu;
import com.ubet.client.BetsApi;
import com.ubet.client.RoomsApi;
import com.ubet.content.BetsContent;
import com.ubet.content.GamesContent;
import com.ubet.content.RoomsContent;
import com.ubet.util.UbetAccount;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.os.Build;

public class MakeBetActivity extends Activity {

	List<BetsContent> bets = new ArrayList<BetsContent>();
	ListView list;

	BetsContentAdapter arrayAdapter = null;

	private MakeBetActivity thisActivity = this;

	Context context;
	Account account;
	AccountManager accountManager;

	int round, roomId, gameId;

	String firstTeamName, secondTeamName, gameDate;

	String roomName;

	BetsTask newTask;
	MakeBetTask makeBetTask;
	ChangeBetTask changeBetTask;

	int scoreOne, scoreTwo;
	int scoreOneItem, scoreTwoItem;

	boolean isExtraBet;
	int betId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_bet);

		Intent intent = getIntent();

		context = getApplicationContext();

		account = UbetAccount.getAccount(context);

		accountManager = AccountManager.get(context);
		if (account == null)
			finish();

		if (intent.getExtras() == null) {
			Log.d("erro", "aqui");
			finish();
		}

		round = intent.getExtras().getInt("round");
		roomId = intent.getExtras().getInt("roomid");
		gameId = intent.getExtras().getInt("gameid");
		firstTeamName = intent.getExtras().getString("first_team_name");
		secondTeamName = intent.getExtras().getString("second_team_name");
		gameDate = intent.getExtras().getString("gamedate");
		roomName = intent.getExtras().getString("roomname");

		if (round == -1 || roomId == -1 || gameId == -1
				|| firstTeamName == null || secondTeamName == null
				|| gameDate == null) {
			Log.d("erro2", "aqui2");
			finish();
		}

		list = (ListView) findViewById(R.id.listView_bets);

		TextView roomNameView = (TextView) findViewById(R.id.room_name);
		roomNameView.setText("Room: " + roomName);
		TextView roundView = (TextView) findViewById(R.id.round);
		roundView.setText("Rodada: " + round);
		TextView firstTeamView = (TextView) findViewById(R.id.first_team_name);
		TextView secondTeamView = (TextView) findViewById(R.id.second_team_name);
		firstTeamView.setText(firstTeamName);
		secondTeamView.setText(secondTeamName);

		showItens();

		newTask = new BetsTask();
		newTask.executeOnExecutor(Executors.newSingleThreadExecutor());
	}

	public void makeBet(View v) {

		EditText scoreOneEdit = (EditText) findViewById(R.id.first_team_score_activity);
		EditText scoreTwoEdit = (EditText) findViewById(R.id.second_team_score_activity);

		if (arrayAdapter.getCount() == 0)
			isExtraBet = false;
		else
			isExtraBet = true;

		if (TextUtils.isEmpty(scoreOneEdit.getText())
				|| TextUtils.isEmpty(scoreTwoEdit.getText())) {
			Toast.makeText(context, "Score can not be empty!",
					Toast.LENGTH_SHORT).show();
		} else {
			scoreOne = Integer.valueOf(scoreOneEdit.getText().toString());
			scoreTwo = Integer.valueOf(scoreTwoEdit.getText().toString());
			Toast.makeText(context, "Sending...", Toast.LENGTH_SHORT).show();
			makeBetTask = new MakeBetTask();
			makeBetTask.executeOnExecutor(Executors.newSingleThreadExecutor());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.make_bet, menu);
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

	private void showItens() {

		arrayAdapter = new BetsContentAdapter(context, R.layout.bets_list, bets);

		list.setAdapter(arrayAdapter);

		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

			}
		});
	}

	private void invalidateBets() {

		for (int i = 0; i < arrayAdapter.getCount(); i++) {
			View view = arrayAdapter.getView(i, null, null);
			EditText firstTeamScoreEdit = (EditText) view
					.findViewById(R.id.first_team_score);
			EditText secondTeamScoreEdit = (EditText) view
					.findViewById(R.id.second_team_score);
			Button changeBetButton = (Button) view
					.findViewById(R.id.change_bet);
			if (firstTeamScoreEdit != null)
				firstTeamScoreEdit.setKeyListener(null);
			if (secondTeamScoreEdit != null)
				secondTeamScoreEdit.setKeyListener(null);
			if (changeBetButton != null)
				changeBetButton.setClickable(false);
		}

		EditText scoreOneEdit = (EditText) findViewById(R.id.first_team_score_activity);
		EditText scoreTwoEdit = (EditText) findViewById(R.id.second_team_score_activity);
		scoreOneEdit.setKeyListener(null);
		scoreTwoEdit.setKeyListener(null);
		Button makeBetButton = (Button) findViewById(R.id.make_bet);
		makeBetButton.setClickable(false);
	}

	protected Date strToDate(String date) {

		DateFormat formatter = new SimpleDateFormat("dd MMM yyyy HH:mm");
		Date newDate;
		try {
			newDate = formatter.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			return new Date();
		}
		return newDate;
	}

	protected Date getNowDate() {

		TimeZone timeZone = TimeZone.getTimeZone("GMT-3:00");
		Calendar c = Calendar.getInstance(timeZone);
		return c.getTime();
	}

	public class BetsContentAdapter extends ArrayAdapter<BetsContent> {

		private Context context;

		public BetsContentAdapter(Context context, int textViewResourceId,
				List<BetsContent> items) {
			super(context, textViewResourceId, items);
			this.context = context;
		}

		public void updateBet(View v) {

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.bets_list, null);
			}
			final View newView = view;
			final BetsContent item = getItem(position);
			if (item != null) {
				TextView firstTeamNameView = (TextView) view
						.findViewById(R.id.first_team_name);
				TextView secondTeamNameView = (TextView) view
						.findViewById(R.id.second_team_name);
				TextView gameDateView = (TextView) view
						.findViewById(R.id.game_date);

				EditText firstTeamScoreEdit = (EditText) view
						.findViewById(R.id.first_team_score);
				EditText secondTeamScoreEdit = (EditText) view
						.findViewById(R.id.second_team_score);

				Button changeBetButton = (Button) view
						.findViewById(R.id.change_bet);

				if (changeBetButton != null) {
					changeBetButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {

							EditText firstTeamScoreEdit = (EditText) newView
									.findViewById(R.id.first_team_score);
							EditText secondTeamScoreEdit = (EditText) newView
									.findViewById(R.id.second_team_score);
							betId = item.getBetId();
							if ((firstTeamScoreEdit != null && !firstTeamScoreEdit
									.getText().toString().equals(""))
									&& secondTeamScoreEdit != null
									&& !secondTeamScoreEdit.getText()
											.toString().equals("")) {

								scoreOneItem = Integer
										.valueOf(firstTeamScoreEdit.getText()
												.toString());
								scoreTwoItem = Integer
										.valueOf(secondTeamScoreEdit.getText()
												.toString());
							} else {
								Toast.makeText(context,
										"Could not save your bet!",
										Toast.LENGTH_SHORT).show();
								return;
							}

							changeBetTask = new ChangeBetTask();
							changeBetTask.executeOnExecutor(Executors
									.newSingleThreadExecutor());
						}

					});
				}

				if (firstTeamNameView != null) {
					firstTeamNameView.setFocusable(false);
					firstTeamNameView.setClickable(false);
					firstTeamNameView.setText(firstTeamName);
				}
				if (secondTeamNameView != null) {
					secondTeamNameView.setFocusable(false);
					secondTeamNameView.setClickable(false);
					secondTeamNameView.setText(secondTeamName);
				}
				if (gameDateView != null) {
					gameDateView.setFocusable(false);
					gameDateView.setClickable(false);
					gameDateView.setText(gameDate);
				}

				if (firstTeamScoreEdit != null) {
					firstTeamScoreEdit.setClickable(false);
					firstTeamScoreEdit.setText(String.valueOf(item
							.getScoreOne()));
				}
				if (secondTeamScoreEdit != null) {
					secondTeamScoreEdit.setClickable(false);
					secondTeamScoreEdit.setText(String.valueOf(item
							.getScoreTwo()));
				}
			}
			return view;
		}
	}

	public void onListOfBetsComplete(List<BetsContent> listOfBets) {
		// TODO Auto-generated method stub

		newTask = null;
		if (listOfBets == null)
			return;

		if (listOfBets.size() != 0) {
			Button makeBetButton = (Button) findViewById(R.id.make_bet);
			makeBetButton.setText("Make Extra Bet");
		}

		arrayAdapter.clear();
		arrayAdapter.addAll(listOfBets);
		arrayAdapter.notifyDataSetChanged();
	}

	public String getError(int pId) {

		String errorString = "erro" + String.valueOf(pId);

		String packageName = getPackageName();
		int resId = getResources().getIdentifier(errorString, "string",
				packageName);

		return getString(resId);

	}

	public void onChangeBetComplete(Integer resultCode) {

		changeBetTask = null;
		if (resultCode == null)
			return;
		if (resultCode == 7) {
			Toast.makeText(context, "Your bet has been saved!",
					Toast.LENGTH_SHORT).show();
			newTask = new BetsTask();
			newTask.executeOnExecutor(Executors.newSingleThreadExecutor());
		} else {
			Toast.makeText(context, getError(resultCode), Toast.LENGTH_SHORT)
					.show();
		}

		if (resultCode == 31) {
			invalidateBets();
		}
	}

	public void onMakeBetComplete(Integer resultCode) {

		makeBetTask = null;
		if (resultCode == null)
			return;
		if (resultCode == 7) {
			Toast.makeText(context, "Your bet has been saved!",
					Toast.LENGTH_SHORT).show();
			;
			newTask = new BetsTask();
			newTask.executeOnExecutor(Executors.newSingleThreadExecutor());
		} else {
			Toast.makeText(context, getError(resultCode), Toast.LENGTH_SHORT)
					.show();
		}

		if (resultCode == 31) {
			invalidateBets();
		}
	}

	public class ChangeBetTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {

			try {

				int result = BetsApi.changeBet(betId, scoreOneItem,
						scoreTwoItem, context);
				return result;
			} catch (Exception e) {

				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(final Integer resultCode) {

			onChangeBetComplete(resultCode);
		}
	}

	public class MakeBetTask extends AsyncTask<Void, Void, Integer> {

		@Override
		protected Integer doInBackground(Void... params) {

			try {

				int result = BetsApi.makeBet(gameId, roomId, round, scoreOne,
						scoreTwo, isExtraBet, context);
				return result;
			} catch (Exception e) {

				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(final Integer resultCode) {

			onMakeBetComplete(resultCode);
		}
	}

	public class BetsTask extends AsyncTask<Void, Void, List<BetsContent>> {

		@Override
		protected List<BetsContent> doInBackground(Void... params) {

			try {

				List<BetsContent> listOfBets = BetsApi.betsByGame(gameId,
						roomId, round, context);
				return listOfBets;
			} catch (Exception e) {

				e.printStackTrace();
				Log.d("erro", e.getMessage().toString());
				return null;
			}
		}

		@Override
		protected void onPostExecute(final List<BetsContent> listOfBets) {

			onListOfBetsComplete(listOfBets);
		}
	}

}
