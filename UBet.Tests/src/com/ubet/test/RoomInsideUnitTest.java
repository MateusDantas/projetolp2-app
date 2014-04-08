package com.ubet.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import com.ubet.activity.RoomInside;
import com.ubet.R;

public class RoomInsideUnitTest extends ActivityUnitTestCase<RoomInside> {
	
	private int usersRegisteredListViewId;
	private int roomNameTextViewId;
	private int adminNameTextViewId;
	private int usersRegisteredTextViewId;
	
	private RoomInside activity;
	
	public RoomInsideUnitTest() {
		super(RoomInside.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    Intent intent = new Intent(getInstrumentation().getTargetContext(),
	        RoomInside.class);
	    startActivity(intent, null, null);
	    activity = getActivity();
	}
	
	public void testLayout() {
		
		usersRegisteredListViewId = R.id.listView_users_registered;
		roomNameTextViewId = R.id.room_name;
		adminNameTextViewId = R.id.admin_name;
		usersRegisteredTextViewId = R.id.users_registered;
		
		assertNotNull(activity.findViewById(roomNameTextViewId));
		assertNotNull(activity.findViewById(adminNameTextViewId));
		assertNotNull(activity.findViewById(usersRegisteredListViewId));
		assertNotNull(activity.findViewById(usersRegisteredTextViewId));
	}
	
}
