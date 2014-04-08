package com.ubet.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import com.ubet.activity.RoomInfo;
import com.ubet.R;

public class RoomInfoUnitTest extends ActivityUnitTestCase<RoomInfo> {
	
	private int roomNameTextViewId;
	private int adminNameTextViewId;
	private int limExtraBetTextViewId;
	private int priceExtraBetViewId;
	
	private RoomInfo activity;
	
	public RoomInfoUnitTest() {
		super(RoomInfo.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    Intent intent = new Intent(getInstrumentation().getTargetContext(),
	        RoomInfo.class);
	    startActivity(intent, null, null);
	    activity = getActivity();
	}
	
	public void testLayout() {
		
		roomNameTextViewId = R.id.textView_roomName;
		adminNameTextViewId = R.id.admin_name;
		limExtraBetTextViewId = R.id.lim_extra_bet;
		priceExtraBetViewId = R.id.price_extra_bet;
		
		
		assertNotNull(activity.findViewById(roomNameTextViewId));
		assertNotNull(activity.findViewById(adminNameTextViewId));
		assertNotNull(activity.findViewById(limExtraBetTextViewId));
		assertNotNull(activity.findViewById(priceExtraBetViewId));
	}
	
}
