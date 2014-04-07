package com.ubet.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
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
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RoomsActivity extends Activity {

	List<RoomsContent> rooms = new ArrayList<RoomsContent>();
	ListView list;
	RoomsActivity thisActivity = this;
	private Menu optionsMenu;

	RoomsContentAdapter arrayAdapter = null;

	private Context context;
	private Account account;
	private AccountManager accountManager;

	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerList;
	private String[] mTitles;
	List<String> menuOptions = new ArrayList<String>();
	
	Handler handler = new Handler();
	
	RoomsTask newTask;
	LogoutTask logoutTask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rooms);

		context = getApplicationContext();
		account = UbetAccount.getAccount(context);
		accountManager = AccountManager.get(context);
		rooms = new ArrayList<RoomsContent>();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.rooms_drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		mTitles = getResources().getStringArray(R.array.planets_array);
		
		if (account == null) {
			killThemAll();
		}
		
		mDrawerLayout.setScrimColor(Color.TRANSPARENT);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.rooms_drawer_list, mTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle("Rooms");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Settings");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        
		checkAuthenticateUser();
		
		list = (ListView) findViewById(R.id.listView_rooms);
		
		showRooms();

		this.handler.postDelayed(checkRunnable, 500L);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.optionsMenu = menu;
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.activity_room_actions, menu);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
       /* menu.findItem(R.id.ubet_create_room).setVisible(!drawerOpen);
        menu.findItem(R.id.ubet_menu_logout).setVisible(!drawerOpen);
        menu.findItem(R.id.ubet_menu_refresh).setVisible(!drawerOpen);*/
        
        return super.onPrepareOptionsMenu(menu);
    }
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
		
		switch (item.getItemId()) {
		case R.id.ubet_menu_refresh:

			setRefreshActionButtonState(true);
			newTask = new RoomsTask();
			newTask.executeOnExecutor(Executors.newSingleThreadExecutor());
			return true;
		case R.id.ubet_create_room:
			final Intent intent = new Intent(this, CreateRoomActivity.class);
			startActivity(intent);
			return true;
		case R.id.ubet_menu_logout:
			logoutTask = new LogoutTask();
			logoutTask.executeOnExecutor(Executors.newSingleThreadExecutor());
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
	
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        	
            selectItem(position);
        }
    }

    private void selectItem(int position) {
    
    	mDrawerList.setItemChecked(position, true);
    	if (position == 0) { //PROFILE
    		
    		Intent intent = new Intent(thisActivity, ProfileActivity.class);
    		intent.putExtra("username", account.name);
			startActivity(intent);
    		
    	} else if (position == 1) { //MY BETS
    		
    	
    	
    	} else if (position == 2) { //ranking
    		
    	
    		
    	} else if (position == 3) { //LOGOUT
    		
    		logoutTask = new LogoutTask();
			logoutTask.executeOnExecutor(Executors.newSingleThreadExecutor());
    	} 
        
        mDrawerLayout.closeDrawer(mDrawerList);
    }
	
	@Override
    public void setTitle(CharSequence title) {

        getActionBar().setTitle(title);
    }

	@Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    
	private final Runnable checkRunnable = new Runnable() {
		public void run() {

			checkAuthenticateUser();
			setRefreshActionButtonState(true);
			newTask = new RoomsTask();
			newTask.executeOnExecutor(Executors.newSingleThreadExecutor());
			handler.postDelayed(this, 60000L);
		}
	};

	void killThemAll() {
		
		if (logoutTask != null)
			logoutTask.cancel(true);
		if (newTask != null)
			newTask.cancel(true);
		finish();
	}
	
	public void checkAuthenticateUser() {

		if (accountManager.getAccountsByType(Constants.ACCOUNT_TYPE).length == 0) {
			Log.d("ola", "hmmm");
			killThemAll();
			return;
		}
		
		if (account == null) {
			Log.d("ola", "aqui");
			killThemAll();
			return;
		}
		
		String nowToken = accountManager.peekAuthToken(account,
				Constants.AUTH_TOKEN_TYPE);
		if (nowToken == null) {
			Log.d("u.", "1");
			this.handler.removeCallbacks(checkRunnable);
			accountManager.removeAccount(account, null, null);
			final Intent intent = new Intent(this, StartActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			killThemAll();
		}
	}

	private void showRooms() {

		arrayAdapter = new RoomsContentAdapter(getApplicationContext(),
				R.layout.rooms_list, rooms);

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
				intent.putExtra("people_inside", rooms.get(arg2).getPeopleInside());
				startActivity(intent);
			}
		});

	}

	// When ranking button clicked
	/** @deprecated  */
	public void goRanking(View view) {
		Intent intent = new Intent(this, RankingActivity.class);

		startActivity(intent);
	}

	// When Profile button clicked
	/** @deprecated  */
	public void goProfile(View view) {
		
		Intent intent = new Intent(this, ProfileActivity.class);
		intent.putExtra("username", account.name);
		startActivity(intent);
	}

	private void updateRooms(List<RoomsContent> listOfRooms) {

		Collections.sort(listOfRooms, new RoomsContent.sortByPeopleInside());
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

	private void onUserLogoutResult(Integer resultCode) {
		// TODO Auto-generated method stub
		if (resultCode == 1) {
			Toast.makeText(context, "Cya!", Toast.LENGTH_SHORT).show();
			if (account == null || accountManager == null) {
				checkAuthenticateUser();
				return;
			}
			accountManager.invalidateAuthToken(Constants.ACCOUNT_TYPE, accountManager.peekAuthToken(account, Constants.AUTH_TOKEN_TYPE));
			checkAuthenticateUser();
		} else {
			Toast.makeText(context, "You shall not pass!", Toast.LENGTH_SHORT)
					.show();
		}
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
				if (account == null)
					return null;
				List<RoomsContent> listOfRooms = RoomsApi.getAllRooms(
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
				onProcessFailed();
				return;
			}

			onListOfRoomsComplete(listOfRooms);
		}

	}

}
