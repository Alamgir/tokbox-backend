package com.cboxgames.idonia.backend.test.heroku;

import com.cboxgames.idonia.backend.test.HttpTest;
import com.cboxgames.idonia.backend.test.HttpTestRunnable;
import com.cboxgames.idonia.backend.test.IdoniaBackendTest;
import com.cboxgames.idonia.backend.test.remote.RemoteData;
import com.cboxgames.idonia.backend.test.remote.ServerResponse;
import com.cboxgames.utils.idonia.types.User.UserWrapper;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.UserNode;
import com.cboxgames.utils.idonia.types.UserNode.UserNodeWrapper;
import com.cboxgames.utils.idonia.types.Accessory;

public class TestUser extends HttpTest {
	// "/users/{user_id}/details: characters, skills, accessories and playlist rating "; HTTP Method: GET
	
	public TestUser(HttpTestRunnable callback_finished, long test_timeout) {
		super(callback_finished, test_timeout);
	}

	@Override
	public void run() {
		try {
			RemoteData remote_data = new RemoteData(80);
			ServerResponse response_login = remote_data.authenticate(IdoniaBackendTest.get_user_name(), IdoniaBackendTest.get_password());

			UserWrapper uw = IdoniaBackendTest.json_converter.getObject(response_login.getJsonData(), UserWrapper.class);
			
			assert uw != null : "Returned User object from login was null.  Our model is probably" +
					"missing some fields.";
			
			// String json_two = IdoniaBackendTest.json_converter.getJson(uw);
			// System.out.println(json_two);
			
			// Iterate thru user's characters
			int idx = 0;
			System.out.printf("===================== User Character ==========================\n");
			for (Character chr : uw.user.user_characters) {
				System.out.printf("---------------------User Character %d  --------------------------\n", idx++);
				String json_data = IdoniaBackendTest.json_converter.getJson(chr);
				System.out.println(json_data);
			}
			
			// Iterate thru user's character accessories inventory
			idx = 0;
			System.out.printf("===================== User Accessory ==========================\n");
			for (Accessory accs : uw.user.user_character_accessories_inventory) {
				System.out.printf("--------------------- User Accessory %d  --------------------------\n", idx++);
				String json_data = IdoniaBackendTest.json_converter.getJson(accs);
				System.out.println(json_data);
			}
			
			// Iterate thru user's nodes
			idx = 0;
			System.out.printf("===================== User Node ==========================\n");
			for (UserNode unode : uw.user.user_nodes) {
				System.out.printf("--------------------- User Node %d  --------------------------\n", idx++);
				String json_data = IdoniaBackendTest.json_converter.getJson(unode);
				System.out.println(json_data);
			}
			
			testComplete();
		} catch (Exception e) {
			// The test has failed due to an exception.
			testComplete(e);
		}
	}
}