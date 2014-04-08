package com.ubet.test;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import com.ubet.activity.BetsActivity;
//import com.ubet.R;

public class BetsActivityUnitTest extends ActivityUnitTestCase<BetsActivity> {
	
	private BetsActivity activity;
	
	public BetsActivityUnitTest() {
		super(BetsActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    Intent intent = new Intent(getInstrumentation().getTargetContext(),
	        BetsActivity.class);
	    startActivity(intent, null, null);
	    activity = getActivity();
	}
	
	public void testLayout() {
		
		
		
	}
}
