package com.ubet.authenticator;

import com.example.ubet.R;
import com.ubet.Constants;
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
		accountManager = AccountManager.get(this);
		
		final Intent intent = getIntent();
		
		username = intent.getStringExtra(USERNAME_PARAM);
		confirmCredentials = intent.getBooleanExtra(CONFIRM_CREDENTIALS_PARAM, false);
		
		requestWindowFeature(Window.FEATURE_LEFT_ICON);
		setContentView(R.layout.activity_login);
		
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON, android.R.drawable.ic_dialog_alert);
		
		usernameEdit = (EditText) findViewById(R.id.username);
		passwordEdit = (EditText) findViewById(R.id.password);
		
		if (!TextUtils.isEmpty(username)) {
			usernameEdit.setText(username);
			passwordEdit.requestFocus();
		}
		
		message.setText(getMessage());
	}

	public void handleLogin(View view) {
		
		password = passwordEdit.getText().toString();
		
		if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
			message.setText(getMessage());
		} else {
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
	}
	
	private CharSequence getMessage() {
		
		if (TextUtils.isEmpty(username)) {
			return getText(R.string.login_missing_username);
		} 
		if (TextUtils.isEmpty(password)) {
			return getText(R.string.login_missing_password);
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
		
		progressDialog = ProgressDialogFragment.newInstance();
		progressDialog.show(getSupportFragmentManager(), "dialog");
	}

	private void hideProgress() {
		
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
		
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
				return UbetApi.authenticateUser(username, password);
			} catch (Exception e) {
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
