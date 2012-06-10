package com.cboxgames.idonia.backend.test.heroku;

import com.cboxgames.idonia.backend.test.HttpTest;
import com.cboxgames.idonia.backend.test.IdoniaBackendTest;
import com.cboxgames.idonia.backend.test.HttpTestRunnable;
import com.cboxgames.idonia.backend.test.remote.RemoteData;
import com.cboxgames.idonia.backend.test.remote.ServerResponse;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.User.UserWrapper;

public class TestSignIn extends HttpTest {
	
	public TestSignIn(HttpTestRunnable callback_finished, long test_timeout) {
		super(callback_finished, test_timeout);
	}

	@Override
	public void run() {
		try {
			RemoteData remote_data = new RemoteData(80);
			ServerResponse response = remote_data.authenticate(IdoniaBackendTest.get_user_name(), IdoniaBackendTest.get_password());

			UserWrapper uw = IdoniaBackendTest.json_converter.getObject(response.getJsonData(), UserWrapper.class);
			
			assert uw != null : "Returned User object from login was null.  Our model is probably" +
					"missing some fields.";
			
			// Do some checking here.
			
			User u = uw.user;
			assert u.username.equals(IdoniaBackendTest.get_user_name()) : "Invalid username";
			
			testComplete();
		} catch (Exception e) {
			// The test has failed due to an exception.
			testComplete(e);
		}
	}
}
