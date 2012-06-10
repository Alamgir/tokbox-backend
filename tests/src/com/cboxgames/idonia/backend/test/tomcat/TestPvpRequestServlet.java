package com.cboxgames.idonia.backend.test.tomcat;

import com.cboxgames.idonia.backend.test.HttpTest;
import com.cboxgames.idonia.backend.test.HttpTestRunnable;
import com.cboxgames.idonia.backend.test.IdoniaBackendTest;
import com.cboxgames.idonia.backend.test.remote.RemoteData;
import com.cboxgames.idonia.backend.test.remote.ServerResponse;
import com.cboxgames.utils.json.messages.BattleOver;
import com.cboxgames.utils.idonia.types.User.UserWrapper;


public class TestPvpRequestServlet extends HttpTest {
	// "/users/{user_id}/pve  HTTP Method: PUT
	
	public TestPvpRequestServlet(HttpTestRunnable callback_finished, long test_timeout) {
		super(callback_finished, test_timeout);
	}

	@Override
	public void run() {
		try {
			RemoteData remote_data = new RemoteData(80);
			ServerResponse response_login = remote_data.tomcat_authenticate(IdoniaBackendTest.get_user_name(), IdoniaBackendTest.get_password());

			UserWrapper uw = IdoniaBackendTest.json_converter.getObject(response_login.getJsonData(), UserWrapper.class);
			
			assert uw != null : "Returned User object from login was null.  Our model is probably" +
					"missing some fields.";
			int winner_id = 40, loser_id = 41;

			BattleOver pvp = new BattleOver();
			pvp.winner_id = winner_id;
			pvp.loser_id = loser_id;
			pvp.battle_end_state = 0;
			pvp.playlist_id = 1;
			
			ServerResponse serverResponse = remote_data.PUTJson(IdoniaBackendTest.TOMCAT_SERVER_ROOT + "/users/pvp", pvp);
			
			int rc = serverResponse.getResponseCode();
			if ((rc < 200) || (rc >= 300)) {
				String status = String.format("failure with the HTTP response code of %d", rc);
				testComplete(status);
			}
			else {
				System.out.println(serverResponse.getJsonData());
				testComplete();
			}
		} catch (Exception e) {
			// The test has failed due to an exception.
			testComplete(e);
		}
	}
}