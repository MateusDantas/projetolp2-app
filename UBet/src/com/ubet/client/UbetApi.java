package com.ubet.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.ubet.util.BCrypt;
import com.ubet.util.Variables;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.content.Context;
import android.net.Uri;
import android.os.OperationCanceledException;
import android.util.Log;

public abstract class UbetApi {

	private static final String SCHEME = "http";
	private static final String UBET_AUTHORITY = "ubet.herokuapp.com";


	public static String authenticateUser(String username, String password) {

		final HttpResponse response;

		Uri.Builder builder = new Uri.Builder();
		builder.scheme(SCHEME);
		builder.authority(UBET_AUTHORITY);
		builder.appendEncodedPath(UbetUrls.LOGIN_USER_URL);
		builder.appendQueryParameter("username", username);
		//password = BCrypt.hashpw(password, BCrypt.gensalt(5));
		builder.appendQueryParameter("password", password);

		Uri uri = builder.build();

		HttpGet request = new HttpGet(String.valueOf(uri));

		Log.d("URL", uri.toString());
		DefaultHttpClient client = (DefaultHttpClient) HttpClientFactory
				.getSafeClient();

		try {

			response = client.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED) {

				final HttpEntity entity = response.getEntity();
				InputStream instream = entity.getContent();

				Document doc = Jsoup.parse(instream, "UTF-8", "http://ubet.herokuapp.com");

				String authToken = doc.select("div#authToken").html();

				return authToken;
			}
			return null;
		} catch (Exception e) {
			Log.v("EXCEPTION", e.getMessage().toString());
			return null;
		}

	}

	
	public static InputStream ubetRegister(String firstName, String lastName,
			String email, String username, String password, int language)
			throws Exception {

		TreeMap<String, String> params = new TreeMap<String, String>();

		//password = BCrypt.hashpw(password, BCrypt.gensalt(5));

		params.put("firstname", firstName);
		params.put("secondname", lastName);
		params.put("email", email);
		params.put("username", username);
		params.put("password", password);
		params.put("language", String.valueOf(language));

		return ubetApiCall(UbetUrls.CREATE_USER_URL, params);
	}

	public static InputStream ubetApiCall(String url,
			TreeMap<String, String> params) throws Exception {

		Uri.Builder builder = new Uri.Builder();
		builder.scheme(SCHEME);
		builder.authority(UBET_AUTHORITY);
		builder.appendEncodedPath(url);

		for (String key : params.keySet()) {
			builder.appendQueryParameter(key, params.get(key));
		}

		String urlCall = builder.build().toString();

		HttpGet get = new HttpGet(urlCall);

		DefaultHttpClient client = (DefaultHttpClient) HttpClientFactory
				.getSafeClient();

		HttpResponse response = client.execute(get);
		int statusCode = response.getStatusLine().getStatusCode();

		if (statusCode == HttpStatus.SC_ACCEPTED) {

			HttpEntity entity = response.getEntity();

			InputStream instream = entity.getContent();

			return instream;
		} else if (statusCode == HttpStatus.SC_UNAUTHORIZED) {

			throw new AuthenticationException();
		} else {

			throw new ubetInternalError();
		}
	}

	public static InputStream ubetApiCall(String url,
			TreeMap<String, String> params, Account account, Context context)
			throws Exception {

		AccountManager manager = AccountManager.get(context);

		if (account == null)
			throw new ubetInternalError();

		String authToken = null;

		try {

			authToken = manager.blockingGetAuthToken(account,
					Variables.AUTH_TOKEN_TYPE, true);
		} catch (Exception e) {
			return null;
		}

		params.put("token", authToken);

		Uri.Builder builder = new Uri.Builder();
		builder.scheme(SCHEME);
		builder.authority(UBET_AUTHORITY);
		builder.appendEncodedPath(url);

		for (String key : params.keySet()) {
			builder.appendQueryParameter(key, params.get(key));
		}

		String urlCall = builder.build().toString();

		HttpGet get = new HttpGet(urlCall);

		DefaultHttpClient client = (DefaultHttpClient) HttpClientFactory
				.getSafeClient();

		HttpResponse response = client.execute(get);
		int statusCode = response.getStatusLine().getStatusCode();

		if (statusCode == HttpStatus.SC_ACCEPTED) {

			HttpEntity entity = response.getEntity();

			InputStream instream = entity.getContent();

			return instream;
		} else if (statusCode == HttpStatus.SC_UNAUTHORIZED) {

			manager.invalidateAuthToken(Variables.AUTH_TOKEN_TYPE, authToken);

			try {

				authToken = manager.blockingGetAuthToken(account,
						Variables.AUTH_TOKEN_TYPE, true);
			} catch (Exception e) {
				
				return null;
			}

			throw new AuthenticationException();
		} else {

			throw new ubetInternalError();
		}
	}

}
