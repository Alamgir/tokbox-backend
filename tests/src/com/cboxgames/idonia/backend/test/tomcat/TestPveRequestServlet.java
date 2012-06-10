package com.cboxgames.idonia.backend.test.tomcat;

import com.cboxgames.idonia.backend.test.HttpTest;
import com.cboxgames.idonia.backend.test.HttpTestRunnable;
import com.cboxgames.idonia.backend.test.IdoniaBackendTest;
import com.cboxgames.idonia.backend.test.remote.RemoteData;
import com.cboxgames.idonia.backend.test.remote.ServerResponse;
import com.cboxgames.utils.idonia.types.User.UserWrapper;
import com.cboxgames.utils.idonia.types.PveRequest;


public class TestPveRequestServlet extends HttpTest {
	// "/users/{user_id}/pve  HTTP Method: PUT
	
	public TestPveRequestServlet(HttpTestRunnable callback_finished, long test_timeout) {
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
		
			int user_id = 30;
			Integer[] mob_ids = {50, 51, 51};
			Integer[] dead_user_characters = { /* 20, 31, 54 */ };
			Integer[] mob_levels = { 5, 8, 7};

			// PveRequestWrapper pve_wrapper = new PveRequestWrapper();
			PveRequest pve = new PveRequest(); // pve_wrapper.pve_request;
			pve.addPveRequest(user_id, 5, 8, "c", dead_user_characters, mob_ids, mob_levels);
			
			ServerResponse serverResponse = remote_data.PUTJson(IdoniaBackendTest.TOMCAT_SERVER_ROOT + "/users/" + user_id + "/pve",
					pve /* pve_wrapper */ );
			
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