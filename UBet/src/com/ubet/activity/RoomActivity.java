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
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        
        Intent intent = getIntent();
        
        if (intent == null) {
        	finish();
        }
        
        TextView roomNameView = (TextView) findViewById(R.id.textView_roomName);
        TextView adminNameView = (TextView) findViewById(R.id.admin_name);
        TextView priceRoomView = (TextView) findViewById(R.id.room_price);
        TextView peopleInsideView = (TextView) findViewById(R.id.number_of_people);
        
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
        
        UserTask newTask = new UserTask();
        newTask.executeOnExecutor(Executors.newSingleThreadExecutor());
        boolean result = false;
        try {
			result = newTask.get();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
        
        if (result) {
        	redirectToRoom();
        }
        roomNameView.setText((String) intent.getExtras().get("name"));
        adminNameView.setText("Admin: " + (String) intent.getExtras().getString("admin_name"));
        priceRoomView.setText("Room price: " + String.valueOf(intent.getExtras().getInt("price_room")));
        peopleInsideView.setText("People in this room: " + String.valueOf(intent.getExtras().getInt("people_inside")));
        roomId = intent.getExtras().getInt("room_id");
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
    	
    }
}