package com.cboxgames.idonia.backend.test.tomcat;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.cboxgames.idonia.backend.test.HttpTest;
import com.cboxgames.idonia.backend.test.HttpTestRunnable;
import com.cboxgames.idonia.backend.test.IdoniaBackendTest;
import com.cboxgames.idonia.backend.test.remote.RemoteData;
import com.cboxgames.idonia.backend.test.remote.ServerResponse;

import com.cboxgames.utils.idonia.types.UserLogin;
import com.cboxgames.utils.idonia.types.UserLogin.UserLoginWrapper;


public class TestSignIn extends HttpTest {
	private static String _sign_in_uri = "/users/sign_in"; // HTTP Method: POST
	
	public TestSignIn(HttpTestRunnable callback_finished, long test_timeout) {
		super(callback_finished, test_timeout);
	}

	@Override
	public void run() {
		try {
			UserLoginWrapper ulw = new UserLoginWrapper();
			UserLogin user = ulw.user = new UserLogin();
			user.username = IdoniaBackendTest.get_user_name();
			user.password = IdoniaBackendTest.get_password();
			String url = IdoniaBackendTest.TOMCAT_SERVER_ROOT + _sign_in_uri;
			
			HttpPost post = new HttpPost(url);
			post.addHeader("Content-Type", IdoniaBackendTest.SERVER_CONTENT_TYPE);
			post.addHeader("Accept", IdoniaBackendTest.SERVER_ACCEPT_TYPE);
			
			String json = IdoniaBackendTest.json_converter.getJson(ulw);
			
			StringEntity body = new StringEntity(json, "UTF-8");
			post.setEntity(body);
			
			HttpResponse response = http_client.execute(post);
			
			ServerResponse serverResponse = new ServerResponse(response);
			
			int rc = serverResponse.getResponseCode();
			if ((rc < 200) || (rc >= 300)) {
				String status = String.format("failure with the HTTP response code of %d", rc);
				testComplete(status);
			}
			else {
				testComplete();
			}
		} catch (Exception e) {
			// The test has failed due to an exception.
			testComplete(e);
		}
	}
}