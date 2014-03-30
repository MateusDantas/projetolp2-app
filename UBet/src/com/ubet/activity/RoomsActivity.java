package com.ubet.activity;

import java.util.ArrayList;
import java.util.List;
import com.example.ubet.R;
import com.ubet.authenticator.AuthenticatorActivity;
import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Toast;

public class RoomsActivity extends Activity {
	 
	List<String> rooms;
	ListView list;
	RoomsActivity thisone = this;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rooms);
        
        rooms = new ArrayList<String>();
        
        //Add static rooms, now you should see in the server what's the rooms avaliables
        //and add them to the arrayList
        rooms.add("Public Room 1");
        rooms.add("Public Room 2");
        rooms.add("Public Room 3");        
        
        list = (ListView) findViewById(R.id.listView1);
        
        //this magically show the rooms in the arraylist rooms
        //now they are clickable
        showRooms();
    }
    
    public void showRooms() {

    	ArrayAdapter<String> adp = new ArrayAdapter<String> 
		(getBaseContext(),R.layout.rooms_list,rooms);
		
    	
    	list.setAdapter(adp);
		
		// this thing makes the rooms clikable
		list.setOnItemClickListener(new OnItemClickListener() {
			 
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				/*
				Toast.makeText(getBaseContext(), rooms.get(arg2) + " clicked!",
					Toast.LENGTH_SHORT).show();
				*/
				
				Intent intent = new Intent(thisone, RoomActivity.class);
				
				intent.putExtra("name", rooms.get(arg2));
				
				startActivity(intent);
			}
		});
		
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.no_menu, menu);
        return true;
    }    
}
