package com.cboxgames.idonia.backend.test.tomcat;

import com.cboxgames.idonia.backend.test.HttpTest;
import com.cboxgames.idonia.backend.test.HttpTestRunnable;
import com.cboxgames.idonia.backend.test.IdoniaBackendTest;
import com.cboxgames.idonia.backend.test.remote.RemoteData;
import com.cboxgames.idonia.backend.test.remote.ServerResponse;
import com.cboxgames.utils.idonia.types.User.UserWrapper;
import com.cboxgames.utils.idonia.types.Character.CharacterRequest;
import com.cboxgames.utils.idonia.types.ReforgeRequest;

import static com.cboxgames.idonia.backend.test.tomcat.TestPurchaseServlet.PurchaseType.*;

public class TestPurchaseServlet extends HttpTest {
	private static String _purchase_uri = "/users/"; // HTTP Method: PUT
	
	public TestPurchaseServlet(HttpTestRunnable callback_finished, long test_timeout) {
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
			
			int user_id = 36;
			Object obj = null;
			for (int type = TPS_GOLD_MINE; type < TPS_UNDEFINED; type++) {
				String uri = IdoniaBackendTest.TOMCAT_SERVER_ROOT + _purchase_uri + user_id + "/" + nameFromId(type);
				switch (type) {			
					case TPS_REFORGE: {
						continue;
//						ReforgeRequest rr = new ReforgeRequest();
//		            	rr.user_character_accessory_id = 96;
//		            	obj = rr;
//						break;
					}
		            case TPS_WEAPON: {
		            	CharacterRequest ucr = new CharacterRequest();
		            	ucr.user_character_id = 2;
		            	obj = ucr;
		            	break;
		            }
		            case TPS_EXPANDED_SACK:
					case TPS_GOLD_MINE:
					case TPS_AMNESIA:
					case TPS_CHARACTER_SLOT:
		            case TPS_BREAD_STICK:
					case TPS_BREAD_SLICE:
					case TPS_BREAD_LOAF:
					case TPS_FRESH_BOOTY:
					default:
						continue;
				}
				
				ServerResponse serverResponse = remote_data.PUTJson(uri, obj);
				int rc = serverResponse.getResponseCode();
				if ((rc < 200) || (rc >= 300)) {
					String status = String.format("failure with the HTTP response code of %d for purchase '%s'", rc, nameFromId(type));
					testComplete(status);
				}
				else {
					System.out.println(serverResponse.getJsonData());
				}
			}
			
			testComplete();
		} catch (Exception e) {
			// The test has failed due to an exception.
			testComplete(e);
		}
	}
	
//	public static final int TPS_SMALL_BAG_OF_GOODIES = 0;
//	public static final int TPS_BOX_OF_GOODIES = 1;
//	public static final int TPS_GIANT_SAG_OF_GOODIES = 2;
//	public static final int TPS_MASSIVE_SAG_OF_GOODIES = 3;
//  public static final int TPS_HAND_FULL_OF_GOODIES = 4;
//	public static final int TPS_MOUTH_FULL_OF_GOODIES = 5;	
	public static final int TPS_GOLD_MINE = 6;
	public static final int TPS_AMNESIA = 7;
	public static final int TPS_CHARACTER_SLOT = 8;
	public static final int TPS_BREAD_STICK = 9;
    public static final int TPS_BREAD_SLICE = 10;
	public static final int TPS_BREAD_LOAF = 11;
	public static final int TPS_FRESH_BOOTY = 12;
	public static final int TPS_REFORGE = 13;
    public static final int TPS_WEAPON = 14;
	public static final int TPS_EXPANDED_SACK = 15;
	public static final int TPS_UNDEFINED = 16;
	
	public static enum PurchaseType {
//    	sbg(TPS_SMALL_BAG_OF_GOODIES), // PUT < /users/{user_id}/sbg >
//    	bog(TPS_BOX_OF_GOODIES), // PUT < /users/{user_id}/bog >
//    	gsg(TPS_GIANT_SAG_OF_GOODIES), // PUT < /users/{user_id}/gsg >
//    	msg(TPS_MASSIVE_SAG_OF_GOODIES), // PUT < /users/{user_id}/msg >
//    	hfg(TPS_HAND_FULL_OF_GOODIES), // PUT < /users/{user_id}/hfg >
//    	mfsg(TPS_MOUTH_FULL_OF_GOODIES), // PUT < /users/{user_id}/mfsg >
    	gom(TPS_GOLD_MINE), // PUT < /users/{user_id}/gom >
    	amn(TPS_AMNESIA), // PUT < /users/{user_id}/amn >
    	chs(TPS_CHARACTER_SLOT), // PUT < /users/{user_id}/chs >
    	bre(TPS_BREAD_STICK), // PUT < /users/{user_id}/bre >
    	bsl(TPS_BREAD_SLICE), // PUT < /users/{user_id}/bsl >
    	blf(TPS_BREAD_LOAF), // PUT < /users/{user_id}/blf >
    	frb(TPS_FRESH_BOOTY), // PUT < /users/{user_id}/frb >
    	wrt(TPS_REFORGE), // PUT < /users/{user_id}/wrt >
    	wpn(TPS_WEAPON), // PUT < /users/{user_id}/wpn >
    	sck(TPS_EXPANDED_SACK), // PUT < /users/{user_id}/sck >
    	udf(TPS_UNDEFINED);
    	
		private int id;
		
		private PurchaseType(int id) {
			this.id = id;
		}
		
		public int getId() { return id; }
		
		public static String nameFromId(int id) {
			switch (id) {			
//				case TPS_SMALL_BAG_OF_GOODIES:
//					return sbg.name();
//				case TPS_BOX_OF_GOODIES:
//					return bog.name();
//				case TPS_GIANT_SAG_OF_GOODIES:
//					return gsg.name();
//				case TPS_MASSIVE_SAG_OF_GOODIES:
//					return msg.name();
//	            case TPS_HAND_FULL_OF_GOODIES:
//	                return hfg.name();             
//				case TPS_MOUTH_FULL_OF_GOODIES:
//					return mfsg.name();
				case TPS_GOLD_MINE:
					return gom.name();
				case TPS_AMNESIA:
					return amn.name();
				case TPS_CHARACTER_SLOT:
					return chs.name();
	            case TPS_BREAD_STICK:
	                return bre.name();
				case TPS_BREAD_SLICE:
					return bsl.name();
				case TPS_BREAD_LOAF:
					return blf.name();
				case TPS_FRESH_BOOTY:
					return frb.name();
				case TPS_REFORGE:
					return wrt.name();
	            case TPS_WEAPON:
	                return wpn.name();
	            case TPS_EXPANDED_SACK:
	                return sck.name();
				default:
					return udf.name();
			}
		}
	}
}