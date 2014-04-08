package com.ubet.test;

import android.content.Intent;
import android.widget.Button;
import android.test.ActivityUnitTestCase;
import com.ubet.activity.CreateRoomActivity;
import com.ubet.R;

public class CreateRoomActivityUnitTest extends ActivityUnitTestCase<CreateRoomActivity> {
	
	private int createRoomNameEditTextId;
	private int createRoomExtraPriceEditTextId;
	private int createRoomPasswordEditTextId;
	private int createRoomPriceRoomEditTextId;
	private int createRoomLimExtraEditTextId;
	private int createRoomSubmitButtonId;
	
	
	CreateRoomActivity activity;
	
	public CreateRoomActivityUnitTest() {
		super(CreateRoomActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    Intent intent = new Intent(getInstrumentation().getTargetContext(),
	        CreateRoomActivity.class);
	    startActivity(intent, null, null);
	    activity = getActivity();
	}
	
	public void testLayout() {
		createRoomNameEditTextId = R.id.create_room_name;
		createRoomExtraPriceEditTextId = R.id.create_room_price_extra;
		createRoomLimExtraEditTextId = R.id.create_room_lim_extra;
		createRoomPasswordEditTextId = R.id.create_room_password;
		createRoomSubmitButtonId = R.id.create_room_submit;
		assertNotNull(activity.findViewById(createRoomNameEditTextId));
		assertNotNull(activity.findViewById(createRoomExtraPriceEditTextId));
		assertNotNull(activity.findViewById(createRoomLimExtraEditTextId));
		assertNotNull(activity.findViewById(createRoomPasswordEditTextId));
		assertNotNull(activity.findViewById(createRoomSubmitButtonId));
	}
}
