package com.ubet.client;

import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public class HttpClientFactory {

	public static final int REQUEST_TIMEOUT = 40 * 1000; // milliseconds
	public static final int MAX_CONNECTIONS = 100;
	
	public static HttpClient getSafeClient() {
		
		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, MAX_CONNECTIONS);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		
		HttpConnectionParams.setConnectionTimeout(params, REQUEST_TIMEOUT);
		HttpConnectionParams.setSoTimeout(params, REQUEST_TIMEOUT);
		ConnManagerParams.setTimeout(params, REQUEST_TIMEOUT);
	
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 8080));
		schemeRegistry.register(new Scheme("https", PlainSocketFactory.getSocketFactory(), 443));
		
		ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);
		
		HttpClient client = new DefaultHttpClient(manager, params);
		
		return client;
	}
	
	
}
