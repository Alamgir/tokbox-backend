package com.cboxgames.idonia.backend.test;

import java.util.ArrayList;
import java.util.List;

//import com.cboxgames.idonia.backend.test.heroku.TestAccessory;
//import com.cboxgames.idonia.backend.test.heroku.TestAccessorySkill;
//import com.cboxgames.idonia.backend.test.heroku.TestAccessorySkillEffect;
//import com.cboxgames.idonia.backend.test.heroku.TestCharacter;
//import com.cboxgames.idonia.backend.test.heroku.TestCharacterNew;
//import com.cboxgames.idonia.backend.test.heroku.TestMob;
//import com.cboxgames.idonia.backend.test.heroku.TestMobSkill;
//import com.cboxgames.idonia.backend.test.heroku.TestMobSkillEffect;
//import com.cboxgames.idonia.backend.test.heroku.TestPurchase;
//import com.cboxgames.idonia.backend.test.heroku.TestSignIn;
//import com.cboxgames.idonia.backend.test.heroku.TestSkill;
//import com.cboxgames.idonia.backend.test.heroku.TestSkillEffect;
//import com.cboxgames.idonia.backend.test.heroku.TestUser;
//import com.cboxgames.idonia.backend.test.heroku.TestUserAchievement;
//import com.cboxgames.idonia.backend.test.heroku.TestUserBanner;
//import com.cboxgames.idonia.backend.test.heroku.TestUserNode;
//import com.cboxgames.idonia.backend.test.heroku.TestNode;
//import com.cboxgames.idonia.backend.test.heroku.TestVersion;
//import com.cboxgames.idonia.backend.test.heroku.TestTutorial;
//import com.cboxgames.idonia.backend.test.heroku.TestAchievement;
//import com.cboxgames.idonia.backend.test.heroku.TestBanner;
//import com.cboxgames.idonia.backend.test.heroku.TestPlaylist;
//import com.cboxgames.idonia.backend.test.heroku.TestLeaderboard;
//import com.cboxgames.idonia.backend.test.heroku.TestSignIn;

import com.cboxgames.idonia.backend.test.tomcat.TestSignIn;
import com.cboxgames.idonia.backend.test.tomcat.TestCharacterNewServlet;
import com.cboxgames.idonia.backend.test.tomcat.TestCharacterServlet;
import com.cboxgames.idonia.backend.test.tomcat.TestUserCharacterAccessoryServlet;
import com.cboxgames.idonia.backend.test.tomcat.TestLeaderboardServlet;
import com.cboxgames.idonia.backend.test.tomcat.TestUserNodeServlet;
import com.cboxgames.idonia.backend.test.tomcat.TestUserNodePostServlet;
import com.cboxgames.idonia.backend.test.tomcat.TestUserNodeDeleteServlet;
import com.cboxgames.idonia.backend.test.tomcat.TestUserVerifyServlet;

//import com.cboxgames.idonia.backend.test.tomcat.TestUserTutorialCompleteServlet;
//import com.cboxgames.idonia.backend.test.tomcat.TestUserTutorialPostServlet;
//import com.cboxgames.idonia.backend.test.tomcat.TestUserTutorialDeleteServlet;
import com.cboxgames.idonia.backend.test.tomcat.TestPveRequestServlet;
import com.cboxgames.idonia.backend.test.tomcat.TestPvpRequestServlet;
import com.cboxgames.idonia.backend.test.tomcat.TestPurchaseServlet;
//import com.cboxgames.idonia.backend.test.tomcat.TestMobPostAllServlet;
import com.cboxgames.utils.json.JsonConverter;


public class IdoniaBackendTest {

	public static final String SERVER_ROOT = "http://savage.heroku.com";
	// public static final String TOMCAT_SERVER_ROOT = "http://127.0.0.1:8080"; // for Tomcat server
	public static final String TOMCAT_SERVER_ROOT = "http://localhost:80/IdoniaBackend"; // Same as the above
	public static final JsonConverter json_converter = JsonConverter.getInstance();
	public static final String SERVER_CONTENT_TYPE = "application/json";
	public static final String SERVER_ACCEPT_TYPE = "application/json";
	
	private static String _user_name;
	private static String _password;
	
	private static void set_user_name(String uname) { _user_name = uname; }
	public static String get_user_name() { return _user_name; }
	private static void set_password(String uname) { _password = uname; }
	public static String get_password() { return _password; }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		HttpTestRunnable callback_complete = new HttpTestRunnable() {

			@Override
			public void run() {
				System.out.println("Test " + getTestType().toString() + " completed with " + result);
			}
			
			public void stop() {}
			
		};
		
		if (args.length == 0) {
			set_user_name("alamgir");
			set_password("aaaaaa");
		}
		else if (args.length == 2) {
			set_user_name(args[0]);
			set_password(args[1]);
		}
		else {
			System.out.println("Error: Number of arguments is either 0 or 2 (username password)");
			return;
		}
		
		List<HttpTest> tomcat_tests = new ArrayList<HttpTest>();
		tomcat_tests.add(new TestUserVerifyServlet(callback_complete, 5000));
		tomcat_tests.add(new TestUserCharacterAccessoryServlet(callback_complete, 5000));
		tomcat_tests.add(new TestPvpRequestServlet(callback_complete, 5000));
		tomcat_tests.add(new TestCharacterNewServlet(callback_complete, 5000));
		tomcat_tests.add(new TestSignIn(callback_complete, 5000));
		tomcat_tests.add(new TestPurchaseServlet(callback_complete, 5000));
		tomcat_tests.add(new TestCharacterServlet(callback_complete, 5000));	
		tomcat_tests.add(new TestUserNodeServlet(callback_complete, 5000));
		tomcat_tests.add(new TestPveRequestServlet(callback_complete, 5000));		
		// tomcat_tests.add(new TestLeaderboardServlet(callback_complete, 5000));
		// tomcat_tests.add(new TestUserNodeDeleteServlet(callback_complete, 5000));
		// tomcat_tests.add(new TestUserNodePostServlet(callback_complete, 5000));

//		tomcat_tests.add(new TestMobPostAllServlet(callback_complete, 5000));

//		tomcat_tests.add(new TestUserTutorialPostServlet(callback_complete, 5000));
//		tomcat_tests.add(new TestUserTutorialCompleteServlet(callback_complete, 5000));
//		tomcat_tests.add(new TestUserTutorialDeleteServlet(callback_complete, 5000));
//		tomcat_tests.add(new TestUserBannerServlet(callback_complete, 5000));
		
	
//		heroku_tests.add(new TestSignIn(callback_complete, 5000)); // always test sign-in first
//		// heroku_tests.add(new TestTutorial(callback_complete, 5000));
//		heroku_tests.add(new TestLeaderboard(callback_complete, 5000));
//		heroku_tests.add(new TestPlaylist(callback_complete, 5000));
//		heroku_tests.add(new TestBanner(callback_complete, 5000));
//		heroku_tests.add(new TestAchievement(callback_complete, 5000));
//		heroku_tests.add(new TestTutorial(callback_complete, 5000));
//		heroku_tests.add(new TestVersion(callback_complete, 5000));
//		heroku_tests.add(new TestNode(callback_complete, 5000));
//		heroku_tests.add(new TestCharacterNew(callback_complete, 5000));
//		heroku_tests.add(new TestAccessory(callback_complete, 5000));
//		heroku_tests.add(new TestAccessorySkill(callback_complete, 5000));
//		heroku_tests.add(new TestAccessorySkillEffect(callback_complete, 5000));
//		heroku_tests.add(new TestCharacter(callback_complete, 5000));
//		heroku_tests.add(new TestMob(callback_complete, 5000));
//		heroku_tests.add(new TestMobSkill(callback_complete, 5000));
//		heroku_tests.add(new TestMobSkillEffect(callback_complete, 5000));
//		heroku_tests.add(new TestPurchase(callback_complete, 5000));
//		heroku_tests.add(new TestSkill(callback_complete, 5000));
//		heroku_tests.add(new TestSkillEffect(callback_complete, 5000));
//		heroku_tests.add(new TestUserAchievement(callback_complete, 5000));
//		heroku_tests.add(new TestUserBanner(callback_complete, 5000));
//		heroku_tests.add(new TestUserNode(callback_complete, 5000));
//		heroku_tests.add(new TestUser(callback_complete, 5000));
//		heroku_tests.add(new TestCharacterNew(callback_complete, 5000));

		
		// Run all tests on the Heroku server
		for (HttpTest test : tomcat_tests) {
			Thread t = new Thread(test);
			t.run();
		}
	}
}
