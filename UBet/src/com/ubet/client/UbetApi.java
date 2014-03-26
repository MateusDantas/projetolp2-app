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

public class UbetApi {

	private static final String SCHEME = "http";
	private static final String UBET_AUTHORITY = "ubet.herokuapp.com";
	private static final int PORT = 80;

	private static final AuthScope SCOPE = new AuthScope(UBET_AUTHORITY, PORT);

	public static InputStream ubetLogin(String username, String password)
			throws AuthenticationException, ClientProtocolException,
			IOException, ubetInternalError {

		TreeMap<String, String> params = new TreeMap<String, String>();

		params.put("username", username);
		params.put("password", password);

		return ubetApiCall(UbetUrls.LOGIN_USER_URL, params);
	}

	public static InputStream ubetApiCall(String url,
			TreeMap<String, String> params) throws ClientProtocolException,
			IOException, AuthenticationException, ubetInternalError {

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
		} catch (OperationCanceledException e) {

			e.printStackTrace();
		} catch (AuthenticatorException e) {

			e.printStackTrace();
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
			} catch (OperationCanceledException e) {

				e.printStackTrace();
			} catch (AuthenticatorException e) {

				e.printStackTrace();
			}

			throw new AuthenticationException();
		} else {

			throw new ubetInternalError();
		}
	}

}
