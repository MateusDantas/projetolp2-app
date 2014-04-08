package com.ubet.activity;

import java.util.concurrent.Executors;

import com.ubet.R;
import com.ubet.R.id;
import com.ubet.R.layout;
import com.ubet.R.menu;
import com.ubet.activity.RoomActivity.UserTask;
import com.ubet.util.UbetAccount;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.os.Build;

public class RoomInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_room_info);

		Intent intent = getIntent();

		if (intent.getExtras() == null) {
			finish();
		}

		TextView roomNameView = (TextView) findViewById(R.id.textView_roomName);
		TextView adminNameView = (TextView) findViewById(R.id.admin_name);
		TextView priceExtraView = (TextView) findViewById(R.id.price_extra_bet);
		TextView limExtraView = (TextView) findViewById(R.id.lim_extra_bet);

		String adminName = intent.getExtras().getString("adminname");
		int priceExtra = intent.getExtras().getInt("price_extra");
		int limExtra = intent.getExtras().getInt("lim_extra");

		Context context = getApplicationContext();

		String roomName = intent.getExtras().getString("roomname");

		if (roomName == null) {
			finish();
		}

		roomNameView.setText((String) intent.getExtras().get("roomname"));
		adminNameView.setText("Admin: "
				+ (String) intent.getExtras().getString("adminname"));
		priceExtraView.setText("Extra's Price: " + intent.getExtras().getInt("price_extra"));
		limExtraView.setText("Extra's Lim: " + intent.getExtras().getInt("lim_extra"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.room_info, menu);
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
}
