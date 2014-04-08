package com.ubet.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import com.ubet.activity.RoomsActivity;
import com.ubet.R;

public class RoomsActivityUnitTest extends ActivityUnitTestCase<RoomsActivity> {
	
	private int roomsListViewId;
	private int leftDrawerId;
	private RoomsActivity activity;
	
	public RoomsActivityUnitTest() {
		super(RoomsActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    Intent intent = new Intent(getInstrumentation().getTargetContext(),
	        RoomsActivity.class);
	    startActivity(intent, null, null);
	    activity = getActivity();
	}
	
	public void testLayout() {
		roomsListViewId = R.id.listView_rooms;
		leftDrawerId = R.id.left_drawer;
		assertNotNull(activity.findViewById(roomsListViewId));
		assertNotNull(activity.findViewById(leftDrawerId));
	}
	
}
