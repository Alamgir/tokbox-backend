package com.cboxgames.idonia.backend.test;

// import java.io.BufferedReader;
// import java.io.IOException;
// import java.io.InputStream;
// import java.io.InputStreamReader;
// import java.io.Reader;
// import java.io.StringWriter;
// import java.io.Writer;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;

public abstract class HttpTest implements Runnable {
	
	/* Will be used to inform a listener this test is complete. */
	private HttpTestRunnable callback_finished;
	
	/* An HTTP client to perform HTTP calls.. */
	protected DefaultHttpClient http_client;
	
	/* How long before the test should timeout?  */
	protected long test_timeout;
	
	public HttpTest(HttpTestRunnable callback_finished, long test_timeout) {
		/* Create a new HTTP Client for this thread. */
		SchemeRegistry scheme_registry = new SchemeRegistry();
		scheme_registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
		ClientConnectionManager cm = new SingleClientConnManager(scheme_registry);
		http_client = new DefaultHttpClient(cm);
		http_client.setCookieStore(new BasicCookieStore());
		
		this.callback_finished = callback_finished;
	}
	
	/**
	 * Method to call when a test completes successfully.
	 */
	public void testComplete() {
		testComplete(HttpTestRunnable.RESULT_SUCCESS);
	}
	
	/**
	 * Method to call when a test completes.
	 * 
	 * @param result a string representation of the test result.
	 */
	public void testComplete(String result) {
		testComplete(result, null);
	}
	
	/**
	 * Method to call when a test fails due to an exception.
	 * 
	 * @param e an exception that occurred during the test.
	 */
	public void testComplete(Exception e) {
		testComplete(HttpTestRunnable.RESULT_EXCEPTION, e);
	}
	
	/**
	 * Method to call when a test completes.
	 * 
	 * @param result a string representation of the test result.
	 * @param e an exception that occurred during the test.
	 */
	public void testComplete(String result, Exception e) {
		// Clear all cookies when this test is complete..
		http_client.getCookieStore().clear();
		callback_finished.result = result;
		callback_finished.setTestType(this.getClass());
		callback_finished.run();
	}
}
