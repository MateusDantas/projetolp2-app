package com.ubet.activity;

import java.util.concurrent.Executors;

import com.ubet.R;
import com.ubet.R.id;
import com.ubet.R.layout;
import com.ubet.R.menu;
import com.ubet.R.string;
import com.ubet.activity.RoomActivity.UserTask;
import com.ubet.client.RoomsApi;
import com.ubet.util.UbetAccount;

import android.accounts.Account;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class EnterToRoom extends Activity {

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserRoomTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private int roomId = -1;
	private String adminName, roomName;

	private Context context;
	private Account account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_enter_to_room);

		context = getApplicationContext();
		account = UbetAccount.getAccount(context);
		Intent intent = getIntent();
		if (intent == null || account == null) {
			finish();
		}

		getIntentResults();

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});

		UserTask newTask = new UserTask();
		newTask.executeOnExecutor(Executors.newSingleThreadExecutor());
		boolean result = false;
		try {
			result = newTask.get();
		} catch (Exception e) {

			e.printStackTrace();
		}

		if (result) {
			redirectToRoom();
		}
	}

	private void getIntentResults() {

		Intent intent = getIntent();

		if (intent == null) {
			finish();
		}

		roomId = intent.getExtras().getInt("roomid");
		adminName = intent.getExtras().getString("admin_name");
		roomName = intent.getExtras().getString("room_name");

		if (roomId == 0 || adminName == null || roomName == null) {
			finish();
		}
	}
	
	private void redirectToRoom() {
		
		final Intent intent = new Intent(this, RoomInside.class);
		intent.putExtra("roomid", roomId);
		intent.putExtra("admin_name", adminName);
		intent.putExtra("room_name", roomName);
		
		startActivity(intent);
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.enter_to_room, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		mPasswordView.setError(null);

		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		}

		if (roomId == -1) {
			Toast.makeText(context, "Something went wrong! try again..",
					Toast.LENGTH_SHORT).show();
			return;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserRoomTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	public CharSequence getError(int pId) {
		String errorString = "erro" + String.valueOf(pId);

		String packageName = getPackageName();
		int resId = getResources().getIdentifier(errorString, "string",
				packageName);

		return getString(resId);
	}

	private void onRoomRegisterResult(Integer resultCode) {
		// TODO Auto-generated method stub
		mAuthTask = null;
		showProgress(false);
		if (resultCode == 7) {
			Toast.makeText(context, "Ok!", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			mPasswordView.setError(getError(resultCode));
		}
	}

	public class UserTask extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				return RoomsApi.isUserInRoom(account.name, roomId, context);
			} catch (Exception e) {
				return false;
			}
		}

	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserRoomTask extends AsyncTask<Void, Void, Integer> {
		@Override
		protected Integer doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				return RoomsApi.enterToRoom(account.name, mPassword, roomId,
						context);
			} catch (InterruptedException e) {
				return 26;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return 26;
			}

		}

		@Override
		protected void onPostExecute(final Integer resultCode) {

			onRoomRegisterResult(resultCode);
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
