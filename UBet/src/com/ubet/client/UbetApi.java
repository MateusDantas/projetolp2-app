package com.ubet.client;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.net.Uri;

public class UbetApi {

	private static final String SCHEME = "http";
	private static final String UBET_AUTHORITY = "192.241.241.32";
	private static final int PORT = 8080;
	
	private static final AuthScope SCOPE = new AuthScope(UBET_AUTHORITY, PORT);
	
	public static String ubetAuthenticate(String username, String password) {
		
		HttpResponse response;
		
		Uri.Builder builder = new Uri.Builder();
		builder.scheme(SCHEME);
		builder.authority(UBET_AUTHORITY);
		builder.appendEncodedPath(UbetUrls.LOGIN_USER_URL);
		
		Uri uri = builder.build();
		
		HttpGet request = new HttpGet(String.valueOf(uri));
		
		DefaultHttpClient client = (DefaultHttpClient)HttpClientFactory.getSafeClient();
		
		CredentialsProvider provider = client.getCredentialsProvider();
		Credentials credentials = new UsernamePasswordCredentials(username, password);
		provider.setCredentials(SCOPE, credentials);
		
		try {
			response = client.execute(request);
			
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED) {
				
				HttpEntity entity = response.getEntity();
				InputStream
			}
		}
		
	}
	
}
