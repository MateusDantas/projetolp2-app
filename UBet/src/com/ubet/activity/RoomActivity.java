package com.ubet.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import com.ubet.R;
import com.ubet.client.RoomsApi;
import com.ubet.util.UbetAccount;

import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RoomActivity extends Activity {

	private int roomId = 0;

	private Context context;
	private Account account;

	private String adminName, roomName;

	TextView roomNameView;
	TextView adminNameView;
	TextView priceRoomView;
	TextView peopleInsideView;
	Button registerButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room);

		Intent intent = getIntent();

		if (intent == null) {
			finish();
		}

		roomNameView = (TextView) findViewById(R.id.textView_roomName);
		adminNameView = (TextView) findViewById(R.id.admin_name);
		priceRoomView = (TextView) findViewById(R.id.room_price);
		peopleInsideView = (TextView) findViewById(R.id.number_of_people);
		registerButton = (Button) findViewById(R.id.button_register);

		adminName = intent.getExtras().getString("admin_name");

		context = getApplicationContext();
		account = UbetAccount.getAccount(context);

		if (account == null) {
			finish();
		}

		roomId = intent.getExtras().getInt("room_id");
		roomName = intent.getExtras().getString("name");

		if (roomId == 0 || roomName == null) {
			finish();
		}

		showProgress();
		UserTask newTask = new UserTask();
		newTask.executeOnExecutor(Executors.newSingleThreadExecutor());

		roomNameView.setText((String) intent.getExtras().get("name"));
		adminNameView.setText("Admin: "
				+ (String) intent.getExtras().getString("admin_name"));
		priceRoomView.setText("Room price: "
				+ String.valueOf(intent.getExtras().getInt("price_room")));
		peopleInsideView.setText("People in this room: "
				+ String.valueOf(intent.getExtras().getInt("people_inside")));
		roomId = intent.getExtras().getInt("room_id");
	}

	public void showProgress() {

		final View linear = findViewById(R.id.loading_status);
		linear.setVisibility(View.VISIBLE);
		roomNameView.setVisibility(View.INVISIBLE);
		adminNameView.setVisibility(View.INVISIBLE);
		priceRoomView.setVisibility(View.INVISIBLE);
		peopleInsideView.setVisibility(View.INVISIBLE);
		registerButton.setVisibility(View.INVISIBLE);
	}

	public void hideProgress(boolean flag) {

		final View linear = findViewById(R.id.loading_status);
		if (!flag) {
			linear.setVisibility(View.INVISIBLE);
			roomNameView.setVisibility(View.VISIBLE);
			adminNameView.setVisibility(View.VISIBLE);
			priceRoomView.setVisibility(View.VISIBLE);
			peopleInsideView.setVisibility(View.VISIBLE);
			registerButton.setVisibility(View.VISIBLE);
		}
	}
	
	public void registerUser(View view) {

		final Intent intent = new Intent(this, EnterToRoom.class);
		intent.putExtra("roomid", roomId);
		intent.putExtra("admin_name", adminName);
		intent.putExtra("room_name", roomName);

		startActivity(intent);
		finish();
	}

	private void redirectToRoom() {

		final Intent intent = new Intent(this, RoomInside.class);
		intent.putExtra("roomid", roomId);
		intent.putExtra("admin_name", adminName);
		intent.putExtra("room_name", roomName);

		startActivity(intent);
		finish();
	}

	public void onUserInRoomResult(Boolean isUserInRoom) {

		
		if (!isUserInRoom) {
			hideProgress(false);
			return;
		}
		hideProgress(true);
		redirectToRoom();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.no_menu, menu);
		return true;
	}

	public class UserTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				return RoomsApi.isUserInRoom(account.name, roomId, context);
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		public void onPostExecute(final Boolean isUserInRoom) {

			onUserInRoomResult(isUserInRoom);
		}

	}
}