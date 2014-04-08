package com.ubet.test;

import android.content.Intent;
import android.widget.Button;
import android.test.ActivityUnitTestCase;
import com.ubet.activity.RegisterActivity;
import com.ubet.R;

public class RegisterActivityUnitTest extends ActivityUnitTestCase<RegisterActivity> {
	
	private int linearLayoutId;
	private int registerButtonId;
	private int usernameEditTextId;
	private int passwordEditTextId;
	private int emailEditTextId;
	private int firstNameEditTextId;
	private int secondNameEditTextId;
	
	private RegisterActivity activity;
	
	public RegisterActivityUnitTest() {
		super(RegisterActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
	    Intent intent = new Intent(getInstrumentation().getTargetContext(),
	        RegisterActivity.class);
	    startActivity(intent, null, null);
	    activity = getActivity();
	}
	
	public void testLayout() {
		linearLayoutId = R.id.linearLayout1;
		registerButtonId = R.id.register_button;
		usernameEditTextId = R.id.username;
		passwordEditTextId = R.id.password;
		emailEditTextId = R.id.email;
		firstNameEditTextId = R.id.firstName;
		secondNameEditTextId = R.id.secondName;
		
		assertNotNull(activity.findViewById(linearLayoutId));
		assertNotNull(activity.findViewById(registerButtonId));
		assertNotNull(activity.findViewById(usernameEditTextId));
		assertNotNull(activity.findViewById(passwordEditTextId));
		assertNotNull(activity.findViewById(emailEditTextId));
		assertNotNull(activity.findViewById(firstNameEditTextId));
		assertNotNull(activity.findViewById(secondNameEditTextId));
		
		Button register = (Button) activity.findViewById(registerButtonId);
		
		assertEquals("Incorrect label of button register", "Register", register.getText());
	}
}
