package com.ubet.authenticator;

import com.ubet.Constants;
import com.example.ubet.R;
import com.ubet.activity.TestActivity;
import com.ubet.client.UbetApi;

import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

public class AuthenticatorActivity extends FragmentActivity {

	public static final String CONFIRM_CREDENTIALS_PARAM = "confirmCredentials";
	public static final String USERNAME_PARAM = "username";
	
	private AccountManager accountManager;
	private UserLoginTask authTask = null;
	private DialogFragment progressDialog = null;
	
	private AccountAuthenticatorResponse authResponse = null;
	private Bundle resultBundle = null;
	
	private String username;
	private String password;
	
	private TextView message;
	private EditText passwordEdit, usernameEdit;
	
	private Boolean confirmCredentials = false;
	
	
	@Override
	public void onCreate(Bundle instanceBundle) {
		
		super.onCreate(instanceBundle);
		
		authResponse = getIntent().getParcelableExtra(AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE);
		
		if (authResponse != null) {
			authResponse.onRequestContinued();
		}
		
		accountManager = AccountManager.get(this);
		
		final Intent intent = getIntent();
		
		username = intent.getStringExtra(USERNAME_PARAM);
		confirmCredentials = intent.getBooleanExtra(CONFIRM_CREDENTIALS_PARAM, false);
		
		//requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.activity_authenticator);
		
		//getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert);
		
		usernameEdit = (EditText) findViewById(R.id.username);
		passwordEdit = (EditText) findViewById(R.id.password);
		message = (TextView) findViewById(R.id.message);
		
		
		if (username != null && !TextUtils.isEmpty(username)) {
			usernameEdit.setText(username);
			passwordEdit.requestFocus();
		}
	
	}

	
	public void handleLogin(View view) {
		
		password = passwordEdit.getText().toString();
		username = usernameEdit.getText().toString();

		Log.d("LOGIN", "ENTER LOGIN");
		
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			message.setText(getMessage());
		} else {
			Log.d("LOGIN", "handling log");
			showProgress();
			authTask = new UserLoginTask();
			authTask.execute();
		}
	}

	protected void finishConfirmCredentials(String authToken) {
		
		final Account account = new Account(username, Constants.ACCOUNT_TYPE);
		accountManager.setAuthToken(account, Constants.AUTH_TOKEN_TYPE, authToken);
		final Intent intent = new Intent();
		intent.putExtra(AccountManager.KEY_BOOLEAN_RESULT, authToken != null);
		intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);
		finish();
	}
	
	protected void finishLogin(String authToken) {
		
		final Account account = new Account(username, Constants.ACCOUNT_TYPE);
		
		accountManager.setAuthToken(account, Constants.AUTH_TOKEN_TYPE, authToken);
		final Intent intent = new Intent();
		
		intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, username);
		intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, Constants.ACCOUNT_TYPE);
		intent.putExtra(AccountManager.KEY_AUTHTOKEN, authToken);
		
		setAccountAuthenticatorResult(intent.getExtras());
		setResult(RESULT_OK, intent);

		finish();
		Intent newIntent = new Intent(this, TestActivity.class);
		startActivity(newIntent);
	}
	
	private CharSequence getMessage() {
		
		if (TextUtils.isEmpty(username)) {
			return getText(R.string.missing_username);
		} 
		if (TextUtils.isEmpty(password)) {
			return getText(R.string.missing_password);
		}
		return null;
	}
	
	public void onAuthenticationResult(String authToken) {
		authTask = null;
		hideProgress();
		if (authToken != null) {
			if (!confirmCredentials) {
				finishLogin(authToken);
			} else {
				finishConfirmCredentials(authToken);
			}
		} else {
			message.setText(getText(R.string.login_fail));
		}
	}
	
	public void onAuthenticationCancel() {
		authTask = null;
		hideProgress();
	}
	
	public void cancel() {
		if (authTask != null) {
			authTask.cancel(true);
			finish();
		}
	}
	
	private void setAccountAuthenticatorResult(Bundle extras) {
		
		resultBundle = extras;
	}

	private void showProgress() {
		
		final View linear = findViewById(R.id.login_status);
		final View loginForm = findViewById(R.id.login_form);
		linear.setVisibility(View.VISIBLE);
		loginForm.setVisibility(View.INVISIBLE);
	}

	private void hideProgress() {
		
		final View linear = findViewById(R.id.login_status);
		final View loginForm = findViewById(R.id.login_form);
		linear.setVisibility(View.GONE);
		loginForm.setVisibility(View.VISIBLE);
		
	}

	public void finish() {
		if (authResponse != null) {
			if (resultBundle != null) {
				authResponse.onResult(resultBundle);
			} else {
				authResponse.onError(AccountManager.ERROR_CODE_CANCELED, "canceled");
			}
			authResponse = null;
		}
		super.finish();
		
	}

	public class UserLoginTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			
			try {
				Log.d("TUDO OK", "de boa vei");
				String authToken = UbetApi.authenticateUser(username, password);
				if (authToken == null)
					Log.d("TOKEN", "null found");
				return authToken;
			} catch (Exception e) {
				Log.d("EXCEPTION", e.getMessage().toString());
				return null;
			}
		}
		
		@Override
		protected void onPostExecute(final String authToken) {
			onAuthenticationResult(authToken);
		}
		
		@Override
		protected void onCancelled() {
			onAuthenticationCancel();
		}
		
	}
	
	public static class ProgressDialogFragment extends DialogFragment {
		
		public static ProgressDialogFragment newInstance() {
			ProgressDialogFragment fragment = new ProgressDialogFragment();
			return fragment;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle instanceSate) {
			
			final ProgressDialog dialog = new ProgressDialog(getActivity());
			dialog.setMessage(getString(R.string.ui_authenticating));
			dialog.setIndeterminate(true);
			dialog.setCancelable(true);
			dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					((AuthenticatorActivity)getActivity()).cancel();
				}
			});
			return dialog;
		}
	}

	
}
