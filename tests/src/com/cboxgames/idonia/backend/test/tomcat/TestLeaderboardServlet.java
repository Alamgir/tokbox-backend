package com.cboxgames.idonia.backend.test.tomcat;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import com.cboxgames.idonia.backend.test.HttpTest;
import com.cboxgames.idonia.backend.test.HttpTestRunnable;
import com.cboxgames.idonia.backend.test.IdoniaBackendTest;
import com.cboxgames.idonia.backend.test.remote.RemoteData;
import com.cboxgames.utils.idonia.types.Leaderboard;
import com.cboxgames.utils.tools.General;


public class TestLeaderboardServlet extends HttpTest {
	
	private static String _user_playlist_uri = "/user_playlists/leaderboard"; // HTTP Method: GET
	
	public TestLeaderboardServlet(HttpTestRunnable callback_finished, long test_timeout) {
		super(callback_finished, test_timeout);
	}

	@Override
	public void run() {
		try {
			RemoteData remote_data = new RemoteData(80);
			// ServerResponse response_login = remote_data.tomcat_authenticate(IdoniaBackendTest.get_user_name(), IdoniaBackendTest.get_password());

			// UserWrapper uw = IdoniaBackendTest.json_converter.getObject(response_login.getJsonData(), UserWrapper.class);
			
			// assert uw != null : "Returned User object from login was null.  Our model is probably" +
			//		"missing some fields.";
			
			int user_id = 1;
			String buf = String.format("?playlist_id=1&user_id=%d", user_id);
			HttpGet get = new HttpGet(new URI(IdoniaBackendTest.TOMCAT_SERVER_ROOT + _user_playlist_uri + buf));
			get.setHeader("Accept", "application/json");
			get.setHeader("Content-Type", "application/json");
			
			HttpResponse response = http_client.execute(get);
			HttpEntity entity = response.getEntity();
			
			String data = General.inputStreamToString(entity.getContent());

			Leaderboard unw = IdoniaBackendTest.json_converter.getObject(data, Leaderboard.class);
			
			// Do some checking here.
			assert unw != null : "Returned Leaderboard object was null.  Our model is probably missing some fields.";
			
			testComplete();
		} catch (Exception e) {
			// The test has failed due to an exception.
			testComplete(e);
		}
	}
}