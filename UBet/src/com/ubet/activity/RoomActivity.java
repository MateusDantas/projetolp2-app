package com.ubet.activity;

import java.util.ArrayList;
import java.util.List;
import com.ubet.R;
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
import android.widget.TextView;
import android.widget.Toast;

public class RoomActivity extends Activity {
	
	private int roomId = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        
        Intent intent = getIntent();
        TextView roomName = (TextView) findViewById(R.id.textView_roomName);
        TextView adminName = (TextView) findViewById(R.id.admin_name);
        TextView priceRoom = (TextView) findViewById(R.id.room_price);
        TextView peopleInside = (TextView) findViewById(R.id.number_of_people);
        
        roomName.setText((String) intent.getExtras().get("name"));
        adminName.setText("Admin: " + (String) intent.getExtras().getString("admin_name"));
        priceRoom.setText("Room price: " + String.valueOf(intent.getExtras().getInt("price_room")));
        peopleInside.setText("People in this room: " + String.valueOf(intent.getExtras().getInt("people_inside")));
        roomId = intent.getExtras().getInt("room_id");
    }
    
     
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.no_menu, menu);
        return true;
    }    
}