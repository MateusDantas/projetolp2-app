package com.ubet.sync;

import java.util.ArrayList;
import java.util.List;

import com.ubet.Constants;
import com.ubet.client.RoomsApi;
import com.ubet.content.RoomsContent;
import com.ubet.manager.RoomsManager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.text.TextUtils;

public class RoomSyncAdapter extends AbstractThreadedSyncAdapter {

	private final Context context;
	private final AccountManager accountManager;
	
	public RoomSyncAdapter(Context context, boolean autoInitialize) {
		super(context, autoInitialize);
		this.context = context;
		accountManager = AccountManager.get(context);
	}
	
	@Override
	public void onPerformSync(Account account, Bundle extras, String authority,
			ContentProviderClient provider, SyncResult syncResult) {
		// TODO Auto-generated method stub
		
		try {
			UploadRooms(account, syncResult);
		} catch (Exception e) {
			syncResult.stats.numIoExceptions++;
		}
	}

	private void UploadRooms(Account account, SyncResult syncResult) {
		// TODO Auto-generated method stub
		
		long lastUpdate = getServerSyncMarker(account);
		final String username = account.name;
		
		final long lastUpdateServer = RoomsApi.lastUpdate(username, context);
		
		if (lastUpdateServer > lastUpdate) {
			
			List<RoomsContent> listOfRooms = new ArrayList<RoomsContent>();
			try {
				listOfRooms = RoomsApi.getRoomsByUser(username, context);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return;
			}
			
			int newEntries = 0;
			
			for (RoomsContent room : listOfRooms) {
				if (!RoomsManager.existRoom(room.getRoomId(), context)) {
					RoomsManager.addRoom(room, username, context);
					newEntries++;
				}
			}
			
			setServerSyncMarker(account, lastUpdateServer);
			
			syncResult.stats.numEntries += newEntries;
		}
		
	}

	private long getServerSyncMarker(Account account) {
		// TODO Auto-generated method stub
		String lastTimeMarker = accountManager.getUserData(account, Constants.SYNC_TIME_ROOM_KEY);
		if (!TextUtils.isEmpty(lastTimeMarker)) {
			return Long.parseLong(lastTimeMarker);
		}
		return 0L;
	}
	
	private void setServerSyncMarker(Account account, long lastTime) {
		accountManager.setUserData(account, Constants.SYNC_TIME_ROOM_KEY, Long.toString(lastTime));
	}

}
