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

public class ProfileActivity extends Activity {
	 
	List<String> registeredRooms;
	ListView list;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        
        registeredRooms = new ArrayList<String>();
        
        registeredRooms.add("Public Room 1");
        
        list = (ListView) findViewById(R.id.listView_rooms_registered);
        
        showItens();
    }
    
    private void showItens() {

    	ArrayAdapter<String> adp = new ArrayAdapter<String> 
		(getBaseContext(),R.layout.rooms_list,registeredRooms);
		
    	
    	list.setAdapter(adp);		
    }
    
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.no_menu, menu);
        return true;
    }    
}