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
	 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        
        Intent intent = getIntent();
        TextView roomName = (TextView) findViewById(R.id.textView_roomName);
        
        roomName.setText((String) intent.getExtras().get("name"));
    }
    
     
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.no_menu, menu);
        return true;
    }    
}