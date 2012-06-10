package com.cboxgames.idonia.backend.test.tests;

import com.cboxgames.idonia.backend.test.HelperMethods;
import com.cboxgames.idonia.backend.test.HttpTest;
import com.cboxgames.idonia.backend.test.IdoniaBackendTest;
import com.cboxgames.idonia.backend.test.HttpTestRunnable;
import com.cboxgames.idonia.backend.test.remote.RemoteData;
import com.cboxgames.idonia.backend.test.remote.ServerResponse;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.User.UserWrapper;
import com.cboxgames.utils.idonia.types.UserNode;
import com.cboxgames.utils.idonia.types.UserTutorial;

/**
 * Test the login flow.
 * 
 * This class will test logging in with a brand new account and with an existing account.
 * It will also test that the user is authenticated after sign up by trying to create new characters.
 * 
 * At the end of the test, it will test a DELETE operation on the user while cleaning up.
 * @author Irwin
 *
 */
public class TestSignIn extends HttpTest {
	
	private static final int GOLD_DEFAULT = 1000;
	private static final int TOKENS_DEFAULT = 10;
	private static final int NODES_DEFAULT = 40;
	
	public TestSignIn(HttpTestRunnable callback_finished, long test_timeout) {
		super(callback_finished, test_timeout);
	}

	@Override
	public void run() {
		try {
			RemoteData remote_data = new RemoteData(80);
			
			/*--------Login with a non-existing user.--------*/
			String username = HelperMethods.getNewusername();
			ServerResponse response = remote_data.authenticate(username, HelperMethods.PASSWORD_DEFAULT);
			UserWrapper uw = IdoniaBackendTest.json_converter.getObject(response.getJsonData(), UserWrapper.class);
			assert uw != null : "Returned User object from login was null.  Our model is probably" +
					"missing some fields.";
			User u = uw.user;
			assert u != null;
			assert u.bread_loaf == 0;
			assert u.bread_slice == 0;
			assert u.breadstick == 0;
			assert u.character_slot == 3;
			assert u.equipped_banner == null;
			assert u.id > 0;
			assert u.inventory_spots == 0;
			assert u.money == GOLD_DEFAULT;
			assert u.tapjoy_tokens == 0;
			assert u.tokens == TOKENS_DEFAULT;
			assert u.username.equals(username);
			assert u.user_achievements == null;
			assert u.user_character_accessories_inventory == null;
			assert u.user_characters == null;
			assert u.user_nodes != null;
			assert u.user_nodes.length == NODES_DEFAULT;
			for (UserNode node : u.user_nodes) {
				assert node.complete == false;
				assert node.user_id == u.id;
			}
			assert u.user_playlists == null;
			assert u.user_tutorials != null;
			for (UserTutorial tut : u.user_tutorials) {
				assert tut.tutorial_name != null;
				assert tut.complete == false;
			}
			
			// Perform some stuff only a logged in user is capable of doing.
			int[] ids = {1, 2, 3};
			response = remote_data.createUserCharacters(ids, true);
			
			assert response.getResponseCode() == 200;
			
			remote_data.clearCookies();
			
			/*--------Login with an existing user.--------*/
			response = remote_data.authenticate(username, HelperMethods.PASSWORD_DEFAULT);
			uw = IdoniaBackendTest.json_converter.getObject(response.getJsonData(), UserWrapper.class);
			assert uw != null : "Returned User object from login was null.  Our model is probably" +
					"missing some fields.";
			u = uw.user;
			assert u != null;
			assert u.id > 0;
			assert u.username.equals(username);
			assert u.user_nodes != null;
			assert u.user_nodes.length == NODES_DEFAULT;
			assert u.user_tutorials != null;
			
			/*--------Clean up--------*/
			assert remote_data.deleteUserAsAdmin(u.id);
			
			testComplete();
		} catch (Exception e) {
			// The test has failed due to an exception.
			testComplete(e);
		}
	}
}
