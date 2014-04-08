package com.ubet.activity;

import com.ubet.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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

		try {
			String roomName = intent.getExtras().getString("roomname");
			String adminName = intent.getExtras().getString("adminname");
			int priceExtra = intent.getExtras().getInt("price_extra");
			int limExtra = intent.getExtras().getInt("lim_extra");
			roomNameView.setText(roomName);
			adminNameView.setText("Admin: " + adminName);
			priceExtraView.setText("Extra's Price: " + priceExtra);
			limExtraView.setText("Extra's Lim: " + limExtra);
		} catch (NullPointerException e) {
			finish();
		}

		
	}
}
