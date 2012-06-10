package com.cboxgames.idonia.backend.test.tomcat;

import com.cboxgames.idonia.backend.test.HttpTest;
import com.cboxgames.idonia.backend.test.HttpTestRunnable;
import com.cboxgames.idonia.backend.test.IdoniaBackendTest;
import com.cboxgames.idonia.backend.test.remote.RemoteData;
import com.cboxgames.idonia.backend.test.remote.ServerResponse;
import com.cboxgames.utils.idonia.types.CharacterNew.CharacterNewAttribute;
import com.cboxgames.utils.idonia.types.CharacterNew.CharacterNewWrapper;
import com.cboxgames.utils.idonia.types.User.UserWrapper;
import com.cboxgames.utils.idonia.types.CharacterNew;

public class TestCharacterNewServlet extends HttpTest {
	// "/users/{user_id}/details: characters, skills, accessories and playlist rating "; HTTP Method: PUT
	
	public TestCharacterNewServlet(HttpTestRunnable callback_finished, long test_timeout) {
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
			
			int user_id = 43;
			final int[] ids = {1,2,3};
			CharacterNew new_char = new CharacterNew(ids, true, new CharacterNewAttribute[3]);		
			String uri = IdoniaBackendTest.TOMCAT_SERVER_ROOT + "/users/" + user_id + "/characters";
			ServerResponse serverResponse = remote_data.PUTJson(uri, new CharacterNewWrapper(new_char));
			int rc = serverResponse.getResponseCode();
			if ((rc < 200) || (rc >= 300)) {
				String status = String.format("failure with the HTTP response code of %d : %s", rc, serverResponse.getJsonData());
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