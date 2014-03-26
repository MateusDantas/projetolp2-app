package com.ubet.authenticator;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class AuthenticatationService extends Service {

	private AccountAuthenticator managerAuthenticator;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return managerAuthenticator.getIBinder();
	}
	
	@Override
	public void onCreate() {
		managerAuthenticator = new AccountAuthenticator(this);
	}

}
