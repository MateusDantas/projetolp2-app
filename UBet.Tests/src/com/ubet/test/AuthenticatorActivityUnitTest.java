package com.ubet.test;

import android.content.Intent;
import android.widget.Button;
import android.test.ActivityUnitTestCase;
import com.ubet.authenticator.AuthenticatorActivity;
import com.ubet.R;

public class AuthenticatorActivityUnitTest extends 
             ActivityUnitTestCase<AuthenticatorActivity> {

	private int usernameEditTextId;
	private int passwordEditTextId;
	private int signInButtonId;
	
	private AuthenticatorActivity activity;

	public AuthenticatorActivityUnitTest() {
		super(AuthenticatorActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    Intent intent = new Intent(getInstrumentation().getTargetContext(),
	        AuthenticatorActivity.class);
	    startActivity(intent, null, null);
	    activity = getActivity();
	}
	
	public void testLayout() {
		usernameEditTextId = R.id.username;
		passwordEditTextId = R.id.password;
		signInButtonId = R.id.sign_in_button;
		assertNotNull(activity.findViewById(usernameEditTextId));
		assertNotNull(activity.findViewById(passwordEditTextId));
		assertNotNull(activity.findViewById(signInButtonId));
		Button signIn = (Button) activity.findViewById(signInButtonId);
		assertEquals("Incorrect Label for Sign In Button", "Login", signIn.getText());
	}
	
}
