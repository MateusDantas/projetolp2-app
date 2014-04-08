package com.ubet.test;

import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.test.ActivityUnitTestCase;
import com.ubet.activity.StartActivity;
import com.ubet.R;

public class StartActivityUnitTest extends ActivityUnitTestCase<StartActivity> {
	
	private int registerButtonId;
	private int loginButtonId;
	private StartActivity activity;
	
	public StartActivityUnitTest() {
		super(StartActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    Intent intent = new Intent(getInstrumentation().getTargetContext(),
	        StartActivity.class);
	    startActivity(intent, null, null);
	    activity = getActivity();
	}
	
	public void testLayout() {
		registerButtonId = R.id.start_register_button;
		loginButtonId = R.id.start_login_button;
		assertNotNull(activity.findViewById(loginButtonId));
		assertNotNull(activity.findViewById(registerButtonId));
		Button login = (Button) activity.findViewById(loginButtonId);
		Button register = (Button) activity.findViewById(registerButtonId);
		assertEquals("Incorrect label of button login", "Login", login.getText());
		assertEquals("Incorrect label of button register", "Register", register.getText());
	}
	
	public void testClickRegisterButton() {
		registerButtonId = R.id.start_register_button;
		Button register = (Button) activity.findViewById(registerButtonId);
		assertNotNull("Register Button was null", register);
		
		register.performClick();
		Intent triggeredIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", triggeredIntent);
		Context context = getInstrumentation().getTargetContext();
		String activityName = triggeredIntent.resolveActivity(context.getPackageManager()).getClassName();
		assertEquals("Wrong activity intent", "com.ubet.activity.RegisterActivity", activityName);
	}
	
	public void testClickLoginButton() {
		loginButtonId = R.id.start_login_button;
		Button login = (Button) activity.findViewById(loginButtonId);
		assertNotNull("Login Button was null", login);
		
		login.performClick();
		Intent triggeredIntent = getStartedActivityIntent();
		assertNotNull("Intent was null", triggeredIntent);
		Context context = getInstrumentation().getTargetContext();
		String activityName = triggeredIntent.resolveActivity(context.getPackageManager()).getClassName();
		assertEquals("Wrong activity intent", "com.ubet.authenticator.AuthenticatorActivity", activityName);
		
	}
}
