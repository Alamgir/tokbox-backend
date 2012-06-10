package com.cboxgames.idonia.backend.test.tests;

import java.util.ArrayList;
import java.util.List;

import com.cboxgames.idonia.backend.test.HelperMethods;
import com.cboxgames.idonia.backend.test.HttpTest;
import com.cboxgames.idonia.backend.test.HttpTestRunnable;
import com.cboxgames.idonia.backend.test.IdoniaBackendTest;
import com.cboxgames.idonia.backend.test.remote.RemoteData;
import com.cboxgames.idonia.backend.test.remote.ServerResponse;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.User;
import com.cboxgames.utils.idonia.types.User.UserWrapper;
import com.cboxgames.utils.idonia.types.CharacterNew;
import com.cboxgames.utils.idonia.types.CharacterNew.CharacterNewAttribute;

public class TestCharacterNew extends HttpTest {
	// "/users/{user_id}/details: characters, skills, accessories and playlist rating "; HTTP Method: GET
	
	public TestCharacterNew(HttpTestRunnable callback_finished, long test_timeout) {
		super(callback_finished, test_timeout);
	}

	@Override
	public void run() {
		try {
			RemoteData remote_data_success = new RemoteData(80);
			RemoteData remote_data_failure = new RemoteData(80);
			
			String username_success = HelperMethods.getNewusername();
			ServerResponse response_success = remote_data_success.authenticate(username_success, HelperMethods.PASSWORD_DEFAULT);
			
			String username_failure = HelperMethods.getNewusername();
			ServerResponse response_failure = remote_data_failure.authenticate(username_failure, HelperMethods.PASSWORD_DEFAULT);

			User user_success = IdoniaBackendTest.json_converter.getObject(response_success.getJsonData(), UserWrapper.class).user;
			User user_failure = IdoniaBackendTest.json_converter.getObject(response_failure.getJsonData(), UserWrapper.class).user;
			
			/*--------Test creating first 3 characters--------*/
			
			// Add first 3 characters.
			final int[] ids_new = {1, 2, 3};
			CharacterNew new_char = new CharacterNew(ids_new, true, new CharacterNewAttribute[0]);
			response_success = remote_data_success.createUserCharacters(ids_new, true);
			user_success = IdoniaBackendTest.json_converter.getObject(response_success.getJsonData(), UserWrapper.class).user;
			assert user_success != null;
			assert user_success.user_characters != null;
			assert user_success.user_characters.length == 3;
			assert containsCharacters(user_success.user_characters, ids_new);
			for (Character character : user_success.user_characters) {
				verifyCharacter(character, true);
			}
			
			/*--------Test creating the remaining character IDs--------*/
			
			// Purchase character slots first.
			response_success = remote_data_success.purchaseItem(user_success.id, "chs");
			assert response_success.getResponseCode() == 200;
			response_success = remote_data_success.purchaseItem(user_success.id, "chs");
			assert response_success.getResponseCode() == 200;
			response_success = remote_data_success.purchaseItem(user_success.id, "chs");
			assert response_success.getResponseCode() == 200;
			List<int[]> ids_success = new ArrayList<int[]>();
			ids_success.add((new int[] {4}));
			ids_success.add((new int[] {5}));
			int count = 3;
			for (int[] ids : ids_success) {
				response_success = remote_data_success.createUserCharacters(ids, true);
				assert response_success.getResponseCode() == 200;
				user_success = IdoniaBackendTest.json_converter.getObject(response_success.getJsonData(), UserWrapper.class).user;
				assert user_success.user_characters != null;
				assert user_success.user_characters.length == count;
				for (Character character : user_success.user_characters) {
					verifyCharacter(character, true);
				}
				count++;
			}
			
			// Purchase character slots first.
			response_failure = remote_data_failure.purchaseItem(user_failure.id, "chs");
			assert response_failure.getResponseCode() == 200;
			response_failure = remote_data_failure.purchaseItem(user_failure.id, "chs");
			assert response_failure.getResponseCode() == 200;
			response_failure = remote_data_failure.purchaseItem(user_failure.id, "chs");
			assert response_failure.getResponseCode() == 200;
			
			List<int[]> ids_failure = new ArrayList<int[]>();
			ids_failure.add((new int[] {1}));
			ids_failure.add((new int[] {2}));
			ids_failure.add((new int[] {3}));
			count = 3;
			for (int[] ids : ids_failure) {
				response_failure = remote_data_success.createUserCharacters(ids, true);
				assert response_failure.getResponseCode() != 200;
			}
			
			testComplete();
		} catch (Exception e) {
			// The test has failed due to an exception.
			testComplete(e);
		}
	}
	
	/**
	 * Do the provided ids exist in the list of given characters.
	 * 
	 * @param characters
	 * @param ids
	 * @return
	 */
	private boolean containsCharacters(Character[] characters, int[] ids) {
		int contains = 0;
		for (int id : ids) {
			for (Character character : characters) {
				if (character.character_id == id) {
					contains++;
					break;
				}
			}
		}
		return contains == ids.length;
	}
	
	private void verifyCharacter(Character character, boolean in_lineup) {
		assert character != null;
		assert character.in_lineup == in_lineup;
	}
}