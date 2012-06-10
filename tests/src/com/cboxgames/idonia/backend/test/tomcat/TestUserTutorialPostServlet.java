package com.cboxgames.idonia.backend.test.tomcat;

import com.cboxgames.idonia.backend.test.HttpTest;
import com.cboxgames.idonia.backend.test.HttpTestRunnable;
import com.cboxgames.idonia.backend.test.IdoniaBackendTest;
import com.cboxgames.idonia.backend.test.remote.RemoteData;
import com.cboxgames.idonia.backend.test.remote.ServerResponse;
import com.cboxgames.utils.idonia.types.UserTutorial.UserTutorialWrapper;

public class TestUserTutorialPostServlet extends HttpTest {
	// "/users/{user_id}/details: characters, skills, accessories and playlist rating "; HTTP Method: GET
	
	public TestUserTutorialPostServlet(HttpTestRunnable callback_finished, long test_timeout) {
		super(callback_finished, test_timeout);
	}

	@Override
	public void run() {
		try {
			RemoteData remote_data = new RemoteData(80);
//			ServerResponse response_login = remote_data.authenticate(IdoniaBackendTest.get_user_name(), IdoniaBackendTest.get_password());
//
//			UserWrapper uw = IdoniaBackendTest.json_converter.getObject(response_login.getJsonData(), UserWrapper.class);
//			
//			assert uw != null : "Returned User object from login was null.  Our model is probably" +
//					"missing some fields.";
//			
			// Add a new user tutorial
			UserTutorialWrapper unw = new UserTutorialWrapper();
			unw.user_tutorial.user_id = 1;
			String url = String.format("%s/user_tutorials/%d/create", IdoniaBackendTest.TOMCAT_SERVER_ROOT, unw.user_tutorial.user_id);
			ServerResponse serverResponse = remote_data.POSTJson(url, unw);
			
			int rc = serverResponse.getResponseCode();
			if ((rc < 200) || (rc >= 300)) {
				String status = String.format("failure with the HTTP response code of %d", rc);
				testComplete(status);
			}
			else testComplete();
		} catch (Exception e) {
			// The test has failed due to an exception.
			testComplete(e);
		}
	}
}