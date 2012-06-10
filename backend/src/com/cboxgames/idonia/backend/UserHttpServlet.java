package com.cboxgames.idonia.backend;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileLock;
import java.sql.SQLException;
import java.util.*;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.cboxgames.idonia.backend.commons.*;
import com.cboxgames.idonia.backend.commons.battle.PveBattle;
import com.cboxgames.idonia.backend.commons.battle.PvpBattle;
import com.cboxgames.idonia.backend.commons.db.node.NodeDBSQL;
import com.cboxgames.idonia.backend.commons.requestclasses.*;
import com.cboxgames.idonia.backend.commons.db.accessory.AccessoryDBSQL;
import com.cboxgames.idonia.backend.commons.db.character.CharacterDBSQL;
import com.cboxgames.idonia.backend.commons.db.purchase.PurchaseDBSQL;
import com.cboxgames.idonia.backend.commons.db.usercharacter.UserCharacterDBSQL;
import com.cboxgames.idonia.backend.commons.db.node.UserNodeDBSQL;
import com.cboxgames.idonia.backend.commons.db.usercharacteraccessory.UserCharacterAccessoryDBSQL;
import com.cboxgames.idonia.backend.commons.db.battle.BattleDBSQL;
import com.cboxgames.idonia.backend.commons.db.playlist.PlaylistDBSQL;
import com.cboxgames.idonia.backend.commons.db.achievement.UserAchievementDBSQL;

import com.cboxgames.utils.idonia.types.*;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.Character.*;
import com.cboxgames.utils.idonia.types.CharacterNew.*;
import com.cboxgames.utils.idonia.types.Purchase.GoldMinePurchase;
import com.cboxgames.utils.idonia.types.UserLogin;
import com.cboxgames.utils.json.messages.BattleOver;
import com.cboxgames.utils.idonia.types.User.*;
import com.cboxgames.utils.idonia.types.UserLogin.UserLoginWrapper;
import com.cboxgames.utils.json.JsonConverter;

import com.cboxgames.idonia.backend.commons.authentication.BCrypt;
import com.cboxgames.idonia.backend.commons.authentication.AuthenticateUser;
import com.sun.java.swing.plaf.windows.WindowsTableHeaderUI;
import com.sun.xml.internal.bind.v2.TODO;
import org.codehaus.jackson.map.ObjectMapper;

import com.cboxgames.idonia.backend.commons.db.user.UserDBSQL;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.skife.csv.SimpleWriter;
import org.skife.csv.WriterCallback;

import static com.cboxgames.idonia.backend.UserHttpServlet.RequestType.*;
import static com.cboxgames.idonia.backend.UserHttpServlet.RequestType.URT_OFFLINE_PVP;
import static com.cboxgames.idonia.backend.UserHttpServlet.RequestType.URT_OFFLINE_USER;

/**
 * Handle "/users/*"
 * HTTP Method: GET/PUT/POST/DELETE
 * @author Michael Chang
 *
 */
@SuppressWarnings("serial")
public class UserHttpServlet extends HttpServlet {

	private SqlDataSource _sql_data_source;
	private JsonConverter _json_converter;
	private UserDBSQL _user_db_sql;
    private UserCharacterDBSQL _uc_db_sql;
    private UserCharacterAccessoryDBSQL _uca_db_sql;
    private UserNodeDBSQL _un_db_sql;
    private CharacterDBSQL _c_db_sql;
    private AccessoryDBSQL _acc_db_sql;
    private PurchaseDBSQL _p_db_sql;
    private BattleDBSQL _b_db_sql;
    private NodeDBSQL _n_db_sql;
    private PlaylistDBSQL _pl_db_sql;
    private UserAchievementDBSQL _ua_db_sql;
	private ObjectMapper _mapper;

	@Override
	public void init() {
		
        _sql_data_source = new SqlDataSource();
        _json_converter = JsonConverter.getInstance();
        _mapper = new ObjectMapper();
        _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL); // no more null-valued properties
        _mapper.getSerializationConfig().addMixInAnnotations(User.class, HashedPwFilter.class);
        try {
        	_user_db_sql = new UserDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _uc_db_sql = new UserCharacterDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _un_db_sql = new UserNodeDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _uca_db_sql = new UserCharacterAccessoryDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _p_db_sql = new PurchaseDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _c_db_sql = new CharacterDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _acc_db_sql = new AccessoryDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _b_db_sql = new BattleDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _pl_db_sql = new PlaylistDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _ua_db_sql = new UserAchievementDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _n_db_sql = new NodeDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
		
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "users");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setGetRequestType(uta);
       	RequestType req_type = ret.getRequestType();
       	int user_id = ret.getUserId();
       	
       	if (req_type == URT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
  
       	switch (req_type) {
       		case URT_DETAILS: {
       		   	response.setContentType("application/json");
       	       	PrintWriter out = response.getWriter();
       	       	
       	       	UserWrapper[] userarray = _user_db_sql.getUserDetails();
       	       	if (userarray == null) {
       	           	out.print("[]");
       	       	} else {
       	       	   	String json = _json_converter.getJson(userarray);
       	           	out.print(json);
       	       	}
       	       	response.setStatus(Constants.SC_OK);
       	       	break;
       		}
       		case URT_TAPJOY: {
                try {
                    CBoxLoggerSyslog.log("tapjoy_url","url",request.getRequestURL().toString());
                    // Map<String, String[]> param_map = request.getParameterMap();
                    String snuid = request.getParameter("snuid");
                    int tokens_earned = Integer.parseInt(request.getParameter("currency"));
                    if (containsOnlyNumbers(snuid)) {
                        int snuid_uid = Integer.parseInt(snuid);
                        if (_user_db_sql.saveTapjoyTokensUID(tokens_earned, snuid_uid) != -1) {
                            ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_OK);
                            CBoxLoggerSyslog.log("tapjoy","user_id","snuid","tokens_earned",user_id,snuid,tokens_earned);
                            CBoxLoggerSyslog.log("token_shop","user_id","abbr","rev_earned",user_id,"etk",0.00);
                        }
                        else {
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    }
                    else {
                        if (_user_db_sql.saveTapjoyTokensIdentifier(tokens_earned, snuid) != -1) {
                            ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_OK);
                            CBoxLoggerSyslog.log("tapjoy","user_id","snuid","tokens_earned",user_id,snuid,tokens_earned);
                            CBoxLoggerSyslog.log("token_shop","user_id","abbr","rev_earned",user_id,"etk",0.00);
                        }
                        else {
                            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        }
                    }

                }
                catch (IOException e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
                catch (NumberFormatException e) {
                    e.printStackTrace();
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    CBoxLoggerSyslog.log("error","error_type","snuid_sent","tapjoy_request_failure",request.getParameter("snuid"));
                }
       			break;
       		}
       		case URT_UID_DETAILS: {
               User user = _user_db_sql.getUserByID(user_id);
               if (user != null) {
            	   Utility.verifyUserData(user, _uc_db_sql);
                   ResponseTools.prepareResponseJson(response, _mapper, new UserWrapper(user), Constants.SC_OK);
               }
               else {
                   response.sendError(Constants.SC_INTERNAL_SERVER_ERROR,
                		   "User with ID = " + user_id + " is not found.");
               }
               break;
       		}
       		case URT_GET_ACCESSORIES: {
                   break;
       		}
       		case URT_VERIFY: {
       			UserWrapper[] userarray = _user_db_sql.getUserDetails();
       	       	if (userarray != null) {
       	       		for (UserWrapper uw : userarray) {
       	       			Utility.verifyUserData(uw.user, _uc_db_sql);
       	       		}
       	       	}
       	       	ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_OK);
       	       	break;
       		}
            case URT_OFFLINE_USER: {
                HttpSession session = request.getSession();
                user_id = (Integer)session.getAttribute("user_id");

                int max_level = Integer.parseInt(request.getParameter("max_level"));
                User opponent = _user_db_sql.findRandUserByMaxLvl(max_level, user_id);

                if (opponent != null) {
                    ResponseTools.prepareResponseJson(response, _mapper, new UserWrapper(opponent), HttpServletResponse.SC_OK);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }

                break;
            }
       		default: {
       			throw new UnimplementedEnumException(req_type.toString() + " is not implemented yet");
       		}
       	}
    }
    
    public static boolean containsOnlyNumbers(String str) {
        for (int i=0;i<str.length();i++) {
            if (!java.lang.Character.isDigit(str.charAt(i)))
                return false;
        }
        return true;
    }
    
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "users");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setPutRequestType(uta);
       	RequestType req_type = ret.getRequestType();
       	int user_id = ret.getUserId();
       	
       	if (req_type == URT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
       	switch (req_type) {
       		case URT_PVP: {
                   try {
                       if (AuthenticateUser.isAdmin(request)) {
                           handlePvpRequest(request, response);
                       }
                       else {
                           response.setStatus(Constants.SC_UNAUTHORIZED);
                           return;
                       }
                   } catch (ServletException e) {
                       e.printStackTrace();
                       response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                   }
                   catch (IOException e) {
                       e.printStackTrace();
                       response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                   }
       		       break;
       		}
            case URT_OFFLINE_PVP: {
               try {
                   handleOfflinePvpRequest(request, response);
               }
               catch (IOException e) {
                   e.printStackTrace();
                   response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
               }
               break;
            }
       		case URT_CHARACTERS: {
                try {
                    InputStream stream = request.getInputStream();
                    CharacterNew u = _mapper.readValue(stream, CharacterNewWrapper.class).user;
                    CharacterNew.CharacterNewAttribute[] character_attributes = u.user_characters_attributes;
                    if (character_attributes.length <= 0) {
                    	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.MISS_CHARACTER_IDS,
            					Constants.SC_BAD_REQUEST);
                    	return;
                    }
                    
                    List<Integer> character_id_list = new ArrayList<Integer>();
                    for (CharacterNew.CharacterNewAttribute character_attribute : character_attributes) {
                        character_id_list.add(character_attribute.character_id);
                    }

                    boolean in_lineup = true;
                    User user = _user_db_sql.getUserByID(user_id);

                    if (user.user_characters != null) {
                        //You can't have more than 5 characters
                        if (user.user_characters.length >= Constants.MAX_CHARACTER_SLOTS) {
                            ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.EXCEEDS_MAX_USER_CHARACTER,
                					Constants.SC_BAD_REQUEST);
                            return;
                        }
                        
                    	if (user.user_characters.length >= 3) {
                    		if (user.character_slot == 0) {
                    			ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.NEEDS_CHARACTER_SLOT,
                    					Constants.SC_BAD_REQUEST);
                                return;
                            }
                    		
                    		user.character_slot -= character_id_list.size();
                    		
                            if (_user_db_sql.updateUserOneField(user.character_slot, "character_slot", user_id) == false) {
                            	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                                CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
                            	return;
                            }
                            
                            in_lineup = false;
                    	}
                    }
                    
                    if (_uc_db_sql.createUserCharacters(character_id_list, user, in_lineup) == false) {
                        response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                        CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_uc_create",user.id);
                        return;
                    }
                    
                    user.hashed_pw = null;
                    ResponseTools.prepareResponseJson(response, _mapper, new UserWrapper(user), Constants.SC_OK);
                }
                catch (IOException e) {
                    e.printStackTrace();
                    response.setStatus(Constants.SC_BAD_REQUEST);
                }

       			break;
       		}
	    	case URT_SMALL_BAG_OF_GOODIES: {
                if (!checkReceipt(request)) {
                    ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.UNVERIFIED_APPLE_RECEIPTT,
        					Constants.SC_BAD_REQUEST);
                    CBoxLoggerSyslog.log("cheater","user_id",user_id);
                    return;
                }

                ArrayList<String> user_mac_addresses = _user_db_sql.getUserMACAddressesByID(user_id);
                
                int tokens = _user_db_sql.saveTokens(Constants.SMALL_BAG_OF_GOODIES_TOKENS, user_id);
                if (tokens != -1) {
                	RewardResponse reward = new RewardResponse();
                    reward.tokens = tokens;
                	ResponseTools.prepareResponseJson(response, _mapper, reward, Constants.SC_OK);
                    CBoxLoggerSyslog.log("token_shop","user_id","abbr","rev_earned","mac_addresses",user_id,"sbg",7.00,user_mac_addresses);
                }
                else {
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","token_purchase","unsuccessful_token_purchase",user_id,"sbg");
                }
                break;
	    	}
	    	case URT_BOX_OF_GOODIES: {
                if (!checkReceipt(request)) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.UNVERIFIED_APPLE_RECEIPTT,
        					Constants.SC_BAD_REQUEST);
                    CBoxLoggerSyslog.log("cheater","user_id",user_id);
                    return;
                }

                ArrayList<String> user_mac_addresses = _user_db_sql.getUserMACAddressesByID(user_id);

                int tokens = _user_db_sql.saveTokens(Constants.BOX_OF_GOODIES_TOKENS, user_id);
                if (tokens != -1) {
                	RewardResponse reward = new RewardResponse();
                    reward.tokens = tokens;
                    ResponseTools.prepareResponseJson(response, _mapper, reward, Constants.SC_OK);
                    CBoxLoggerSyslog.log("token_shop","user_id","abbr","rev_earned","mac_addresses",user_id,"bog",14.00,user_mac_addresses);
                }
                else {
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","token_purchase","unsuccessful_token_purchase",user_id,"bog");
                }
                break;
	    	}
	    	case URT_GIANT_SAG_OF_GOODIES: {
                if (!checkReceipt(request)) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.UNVERIFIED_APPLE_RECEIPTT,
        					Constants.SC_BAD_REQUEST);
                    CBoxLoggerSyslog.log("cheater","user_id",user_id);
                    return;
                }

                ArrayList<String> user_mac_addresses = _user_db_sql.getUserMACAddressesByID(user_id);
                
                int tokens = _user_db_sql.saveTokens(Constants.GIANT_SACK_OF_GOODIES_TOKENS, user_id);
                if (tokens != -1) {
                	RewardResponse reward = new RewardResponse();
                    reward.tokens = tokens;
                    ResponseTools.prepareResponseJson(response, _mapper, reward, Constants.SC_OK);
                    CBoxLoggerSyslog.log("token_shop","user_id","abbr","rev_earned","mac_addresses",user_id,"gsg",35.00,user_mac_addresses);
                }
                else {
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","token_purchase","unsuccessful_token_purchase",user_id,"gsg");
                }
                break;
	    	}
	    	case URT_MASSIVE_SAG_OF_GOODIES: {
                if (!checkReceipt(request)) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.UNVERIFIED_APPLE_RECEIPTT,
        					Constants.SC_BAD_REQUEST);
                    CBoxLoggerSyslog.log("cheater","user_id",user_id);
                    return;
                }

                ArrayList<String> user_mac_addresses = _user_db_sql.getUserMACAddressesByID(user_id);
                
                int tokens = _user_db_sql.saveTokens(Constants.MASSIVE_SACK_OF_GOODIES_TOKENS, user_id);
                if (tokens != -1) {
                	RewardResponse reward = new RewardResponse();
                    reward.tokens = tokens;
                    ResponseTools.prepareResponseJson(response, _mapper, reward, Constants.SC_OK);
                    CBoxLoggerSyslog.log("token_shop","user_id","abbr","rev_earned","mac_addresses",user_id,"msg",70.00,user_mac_addresses);
                }
                else {
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","token_purchase","unsuccessful_token_purchase",user_id,"msg");
                }
	    		break;
	    	}
	    	case URT_HAND_FULL_OF_GOODIES: {
                if (!checkReceipt(request)) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.UNVERIFIED_APPLE_RECEIPTT,
        					Constants.SC_BAD_REQUEST);
                    CBoxLoggerSyslog.log("cheater","user_id",user_id);
                    return;
                }

                ArrayList<String> user_mac_addresses = _user_db_sql.getUserMACAddressesByID(user_id);
                
                int tokens = _user_db_sql.saveTokens(Constants.HANDFUL_OF_GOODIES_TOKENS, user_id);
                if (tokens != -1) {
                	RewardResponse reward = new RewardResponse();
                    reward.tokens = tokens;
                	ResponseTools.prepareResponseJson(response, _mapper, reward, Constants.SC_OK);
                    CBoxLoggerSyslog.log("token_shop","user_id","abbr","rev_earned","mac_addresses",user_id,"hfg",0.70,user_mac_addresses);
                }
                else {
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","token_purchase","unsuccessful_token_purchase",user_id,"hfg");
                }
                break;
            }
	    	case URT_MOUTH_FULL_OF_GOODIES: {
                if (!checkReceipt(request)) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.UNVERIFIED_APPLE_RECEIPTT,
        					Constants.SC_BAD_REQUEST);
                    CBoxLoggerSyslog.log("cheater","user_id",user_id);
                    return;
                }

                ArrayList<String> user_mac_addresses = _user_db_sql.getUserMACAddressesByID(user_id);
                
                int tokens = _user_db_sql.saveTokens(Constants.MOUTHFUL_OF_GOODIES_TOKENS, user_id);
                if (tokens != -1) {
                	RewardResponse reward = new RewardResponse();
                    reward.tokens = tokens;
                	ResponseTools.prepareResponseJson(response, _mapper, reward, Constants.SC_OK);
                    CBoxLoggerSyslog.log("token_shop","user_id","abbr","rev_earned","mac_addresses",user_id,"mfg",3.5,user_mac_addresses);
                }
                else {
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","token_purchase","unsuccessful_token_purchase",user_id,"mfg");
                }
                response.setStatus(200);
                break;
	    	}
	    	case URT_GOLD_MINE: {
                try {
                    InputStream stream = request.getInputStream();
                    GoldMinePurchase gom = _mapper.readValue(stream, GoldMinePurchase.class);
                    
                    User user = _user_db_sql.getUserOnlyByID(user_id);
                    
                    user.tokens -= gom.exchanged_token_amount;
                    user.money += gom.exchanged_token_amount * Constants.GOLD_TOKEN_EXCHANGE_RATE;
                    if (_user_db_sql.updateUserTwoFields(user.tokens, "tokens", user.money,
                    		"money", user_id) == false) {
                    	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                        CBoxLoggerSyslog.log("error","error_type","user_id","purchase_abbr","unsuccessful_market_purchase",user_id,"gom");
                		return;
                    }
                    RewardResponse reward_response = new RewardResponse();
                    reward_response.gold = user.money;
                    reward_response.tokens = user.tokens;

                    ResponseTools.prepareResponseJson(response, _mapper, reward_response, Constants.SC_OK);
                    CBoxLoggerSyslog.log("purchase_shop","user_id","abbr","tokens_spent",user_id,"gom",gom.exchanged_token_amount);
                }
                catch (IOException e) {
                    e.printStackTrace();
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                }
	    		break;
	    	}
	    	case URT_AMNESIA: {
                try {
                    InputStream stream = request.getInputStream();
                    int char_id = _mapper.readValue(stream, CharacterRequest.class).user_character_id;
                    int purchase_price = (int) _p_db_sql.getPurchaseByAbbr(Constants.AMN).price;
                    User user = _user_db_sql.getUserOnlyByID(user_id);
                    if (user.tokens < purchase_price) {
                        ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.NOT_ENOUGH_TOKENS,
            					Constants.SC_BAD_REQUEST);
                        return;
                    }

                    user.tokens -= purchase_price;
                    if (_user_db_sql.updateUserOneField(user.tokens, "tokens", user_id) == false) {
                    	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                        CBoxLoggerSyslog.log("error","error_type","user_id","purchase_abbr","unsuccessful_market_purchase",user_id,"amn");
                    	return;
                    }
                    
                    Character uc = _uc_db_sql.getUserCharacterByID(char_id).character;
                    Character lookup_char = _c_db_sql.getCharacterByID(uc.character_id);

                    //Account for the subtraction in stat points and give them back
                    //to the user to allocate again
                    uc.stats += uc.strength-lookup_char.strength;
                    uc.stats += uc.agility-lookup_char.agility;
                    uc.stats += uc.intelligence-lookup_char.intelligence;
                    uc.stats += uc.vitality-lookup_char.vitality;
                    uc.stats += uc.will-lookup_char.will;

                    uc.strength = lookup_char.strength;
                    uc.agility = lookup_char.agility;
                    uc.intelligence = lookup_char.intelligence;
                    uc.vitality = lookup_char.vitality;
                    uc.will = lookup_char.will;

                    List<Character> uc_list = new ArrayList<Character>();
                    uc_list.add(uc);

                    if (_uc_db_sql.saveUserCharacters(uc_list) == false) {
                    	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                        CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_uc_save",user_id);
                    	return;
                    }
                    
                    CharacterUpdateStatsWrapper new_ucs_wrapper = new CharacterUpdateStatsWrapper();
                    UpdateStats us = new_ucs_wrapper.user_character;
                    us.agility = uc.agility;
                    us.strength = uc.strength;
                    us.intelligence = uc.intelligence;
                    us.vitality = uc.vitality;
                    us.will = uc.will;
                    us.stats = uc.stats;
                    
                    ResponseTools.prepareResponseJson(response, _mapper, new_ucs_wrapper, Constants.SC_OK);
                    CBoxLoggerSyslog.log("purchase_shop","user_id","abbr","tokens_spent","user_character_id",user_id,"amn",purchase_price,char_id);                  
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
	    		break;
	    	}
	    	case URT_CHARACTER_SLOT: {
                int purchase_price = (int) _p_db_sql.getPurchaseByAbbr(Constants.CHS).price;
                User user = _user_db_sql.getUserByID(user_id);
                if (user.tokens < purchase_price) {
                    ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.NOT_ENOUGH_TOKENS,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                
                if (user.character_slot > 0) {
                    ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.USE_CHAR_SLOT_FIRST,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                
                if (user.user_characters.length == Constants.MAX_CHARACTER_SLOTS) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.MORE_TAHN_5_CHARS,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
 
                user.tokens -= purchase_price;
                user.character_slot += 1;
                
                if (_user_db_sql.updateUserTwoFields(user.tokens, "tokens", user.character_slot,
                		"character_slot", user_id) == false) {
                	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
            		return;
                }
 
                ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_OK);
                CBoxLoggerSyslog.log("purchase_shop","user_id","abbr","tokens_spent",user_id,"chs",purchase_price);
	    		break;
	    	}
	    	case URT_BREAD_STICK: {
                int purchase_price = (int) _p_db_sql.getPurchaseByAbbr(Constants.BRE).price;
                User user = _user_db_sql.getUserOnlyByID(user_id);
                if (user.tokens < purchase_price) {
                    ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.NOT_ENOUGH_TOKENS,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                if (user.bread_slice > 0) {
                    ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.USE_BREAD_FIRST,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                if (user.bread_loaf > 0) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.USE_BREAD_FIRST,
        					Constants.SC_BAD_REQUEST);
                    return;
                }

                user.tokens -= purchase_price;
                user.breadstick += Constants.BREADSTICK_TURNS;
                
                if (_user_db_sql.updateUserTwoFields(user.tokens, "tokens", user.breadstick,
                		"breadstick", user_id) == false) {
                	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
            		return;
                }
                RewardResponse reward = new RewardResponse();
                reward.breadstick = user.breadstick;
                ObjectMapper new_mapper = new ObjectMapper();
                new_mapper.getSerializationConfig().addMixInAnnotations(RewardResponse.class, RewardResponse.BreadStickFilter.class);
                ResponseTools.prepareResponseJson(response, new_mapper, reward, Constants.SC_OK);
                CBoxLoggerSyslog.log("purchase_shop","user_id","abbr","tokens_spent",user_id,"bre",purchase_price);
	    		break;
	    	}
	    	case URT_BREAD_SLICE: {
                int purchase_price = (int) _p_db_sql.getPurchaseByAbbr(Constants.BSL).price;
                User user = _user_db_sql.getUserOnlyByID(user_id);
                if (user.tokens < purchase_price) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.NOT_ENOUGH_TOKENS,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                if (user.breadstick > 0) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.USE_BREAD_FIRST,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                if (user.bread_loaf > 0) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.USE_BREAD_FIRST,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                
                user.tokens -= purchase_price;
                user.bread_slice += Constants.BREADSLICE_TURNS;
                
                if (_user_db_sql.updateUserTwoFields(user.tokens, "tokens", user.bread_slice,
                		"bread_slice", user_id) == false) {
                	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
            		return;
                }

                RewardResponse reward = new RewardResponse();
                reward.breadslice = user.bread_slice;
                ObjectMapper new_mapper = new ObjectMapper();
                new_mapper.getSerializationConfig().addMixInAnnotations(RewardResponse.class, RewardResponse.BreadSliceFilter.class);
                ResponseTools.prepareResponseJson(response, new_mapper, reward, Constants.SC_OK);
                CBoxLoggerSyslog.log("purchase_shop","user_id","abbr","tokens_spent",user_id,"bsl",purchase_price);
	    		break;
	    	}
	    	case URT_BREAD_LOAF: {
                int purchase_price = (int) _p_db_sql.getPurchaseByAbbr(Constants.BLF).price;
                User user = _user_db_sql.getUserOnlyByID(user_id);
                if (user.tokens < purchase_price) {
                    ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.NOT_ENOUGH_TOKENS,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                if (user.breadstick > 0) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.USE_BREAD_FIRST,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                if (user.bread_slice > 0) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.USE_BREAD_FIRST,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
 
                user.tokens -= purchase_price;
                user.bread_loaf += Constants.BREADLOAF_TURNS;

                if (_user_db_sql.updateUserTwoFields(user.tokens, "tokens", user.bread_loaf,
                		"bread_loaf", user_id) == false) {
                	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
            		return;
                }

                RewardResponse reward = new RewardResponse();
                reward.breadloaf = user.bread_loaf;
                ObjectMapper new_mapper = new ObjectMapper();
                new_mapper.getSerializationConfig().addMixInAnnotations(RewardResponse.class, RewardResponse.BreadLoafFilter.class);
                ResponseTools.prepareResponseJson(response, new_mapper, reward, Constants.SC_OK);
                CBoxLoggerSyslog.log("purchase_shop","user_id","abbr","tokens_spent",user_id,"blf",purchase_price);
	    		break;
	    	}
	    	case URT_FRESH_BOOTY: {
                int purchase_price = (int) _p_db_sql.getPurchaseByAbbr(Constants.FRB).price;
                User user = _user_db_sql.getUserByID(user_id);
                if (user.tokens < purchase_price) {
                    ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.NOT_ENOUGH_TOKENS,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                if (user.bread_slice > 0) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.USE_BREAD_FIRST,
        					Constants.SC_BAD_REQUEST);;
                    return;
                }
                if (user.bread_loaf > 0) {
                    ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.USE_BREAD_FIRST,
        					Constants.SC_BAD_REQUEST);
                    return;
                }

                user.tokens -= purchase_price;
                
                if (_user_db_sql.updateUserOneField(user.tokens, "tokens", user_id) == false) {
                	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
                	return;
                }
                
            	if (_un_db_sql.updateUserFreshBooty(user) == false) {
            		response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
            		return;
            	}
 
                ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_OK);
                CBoxLoggerSyslog.log("purchase_shop","user_id","abbr","tokens_spent",user_id,"frb",purchase_price);
	    		break;
	    	}
	    	case URT_REFORGE: {
                try {
                    InputStream stream = request.getInputStream();
                    int uca_id = _mapper.readValue(stream, ReforgeRequest.class).user_character_accessory_id;
                    Accessory mod_user_acc = _uca_db_sql.getUserCharacterAccessoryByID(uca_id);

                    String purchase_type = mod_user_acc.accessory_type.equals(Constants.WEAPON) ?
                        Constants.WRT : Constants.WSA;
                    int purchase_price = (int) _p_db_sql.getPurchaseByAbbr(purchase_type).price;
                    User user = _user_db_sql.getUserOnlyByID(user_id);
                    if (user.tokens < purchase_price) {
                    	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.NOT_ENOUGH_TOKENS,
            					Constants.SC_BAD_REQUEST);
                        return;
                    }

                    user.tokens -= purchase_price;
                    
                    if (_user_db_sql.updateUserOneField(user.tokens, "tokens", user_id) == false) {
                    	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                        CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
                    	return;
                    }
                    
                    // Carry out accessory reset with lookup accessory
                    Accessory lookup_accessory = _acc_db_sql.getAccessory(mod_user_acc.accessory_id);

                    mod_user_acc.stats = 0;
                    mod_user_acc.stats += (mod_user_acc.strength - lookup_accessory.strength);
                    mod_user_acc.stats += (mod_user_acc.agility - lookup_accessory.agility);
                    mod_user_acc.stats += (mod_user_acc.intelligence - lookup_accessory.intelligence);
                    mod_user_acc.stats += (mod_user_acc.vitality - lookup_accessory.vitality);
                    mod_user_acc.stats += (mod_user_acc.will - lookup_accessory.will);

                    mod_user_acc.strength = lookup_accessory.strength;
                    mod_user_acc.agility = lookup_accessory.agility;
                    mod_user_acc.intelligence = lookup_accessory.intelligence;
                    mod_user_acc.vitality = lookup_accessory.vitality;
                    mod_user_acc.will = lookup_accessory.will;

                    if (_uca_db_sql.updateUserCharacterAccessory(mod_user_acc)) {
                    	ResponseTools.prepareResponseJson(response, _mapper, mod_user_acc, Constants.SC_OK);
                    }
                    else {
                        response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    }
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
	    		break;
	    	}
	    	case URT_WEAPON: {
                int purchase_price = (int) _p_db_sql.getPurchaseByAbbr(Constants.WPN).price;
                User user = _user_db_sql.getUserByID(user_id);
                if (user.tokens < purchase_price) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.NOT_ENOUGH_TOKENS,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                
                user.tokens -= purchase_price;
                
                if (_user_db_sql.updateUserOneField(user.tokens, "tokens", user_id) == false) {
                	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                	return;
                }

                try {
                    InputStream stream = request.getInputStream();
                    int user_char_id = _mapper.readValue(stream, CharacterRequest.class).user_character_id;
                    if (_uc_db_sql.updateUserCharacterWeaponSwap(user_char_id) == false) {
                    	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                        CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
                    	return;
                    }
                    
                    ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_OK);
                }
                catch (IOException e) {
                    e.printStackTrace();
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                }

	    		break;
	    	}
	    	case URT_EXPANDED_SACK: {
                int purchase_price = (int) _p_db_sql.getPurchaseByAbbr(Constants.SCK).price;
                User user = _user_db_sql.getUserOnlyByID(user_id);
                if (user.tokens < purchase_price) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.NOT_ENOUGH_TOKENS,
        					Constants.SC_BAD_REQUEST);
                    return;
                }

                user.tokens -= purchase_price;
                user.inventory_spots += 9;
                if (_user_db_sql.updateUserTwoFields(user.tokens, "tokens", user.inventory_spots,
                		"inventory_spots", user_id) == false) {
                	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
            		return;
                }
                
                ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_OK);
                CBoxLoggerSyslog.log("purchase_shop","user_id","abbr","tokens_spent",user_id,"sck",purchase_price);
	    		break;
	    	}
            case URT_NO_RP: {
                int purchase_price = (int) _p_db_sql.getPurchaseByAbbr(Constants.NRP).price;
                User user = _user_db_sql.getUserOnlyByID(user_id);
                if (user.tokens < purchase_price) {
                    ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.NOT_ENOUGH_TOKENS,
                                                      Constants.SC_BAD_REQUEST);
                    return;
                }
                
                user.tokens -= purchase_price;
                user.no_rp = true;
                if (_user_db_sql.saveNoRP(user) == false) {
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
                    return;
                }

                ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_OK);
                CBoxLoggerSyslog.log("purchase_shop","user_id","abbr","tokens_spent",user_id,"nrp",purchase_price);
                break;
            }
            case URT_NO_ADS: {
//                TODO add No-ads code here for purchase, duplicate NO_RP code is below
//                int purchase_price = (int) _p_db_sql.getPurchaseByAbbr(Constants.NRP).price;
//                User user = _user_db_sql.getUserOnlyByID(user_id);
//                if (user.tokens < purchase_price) {
//                    ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.NOT_ENOUGH_TOKENS,
//                                                      Constants.SC_BAD_REQUEST);
//                    return;
//                }
//
//                user.tokens -= purchase_price;
//                user.no_rp = true;
//                if (_user_db_sql.saveNoRP(user) == false) {
//                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
//                    CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
//                    return;
//                }
//
//                ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_OK);
//                CBoxLoggerSyslog.log("purchase_shop","user_id","abbr","tokens_spent",user_id,"nrp",purchase_price);
//                break;
            }
            case URT_EXP_SCROLL: {
                int purchase_price = (int) _p_db_sql.getPurchaseByAbbr(Constants.EXP).price;
                InputStream request_stream = request.getInputStream();
                Map<String, Integer> data = _mapper.readValue(request_stream, Map.class);
                int request_uc_id = data.get("user_character_id");

                User user = _user_db_sql.getUserOnlyByID(user_id);
                for (Character uc : user.user_characters) {
                    if (uc.id == request_uc_id) {
                        uc.experience += Constants.EXP_SCROLL_GAIN;

                        PveBattle.levelUpUserCharacter(uc, user);
                    }
                }

                
                if (_uc_db_sql.saveUserCharactersStats(new ArrayList<Character>(Arrays.asList(user.user_characters)))) {
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }

                break;
            }
            case URT_ADD_TOKENS: {
                InputStream stream = request.getInputStream();
                int tokens = 0;
                @SuppressWarnings("unchecked")
                Map<String, Object> data = _mapper.readValue(stream, Map.class);
                if ((Integer)data.get("tokens") != 0) {
                    tokens = (Integer)data.get("tokens");
                }

                if (_user_db_sql.saveTokens(tokens, user_id) != -1) {
                    response.setStatus(HttpServletResponse.SC_OK);
                }
                else {
                    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }

                break;
            }
               case URT_ADD_MONEY: {
                   InputStream stream = request.getInputStream();
                   int money = 0;
                   @SuppressWarnings("unchecked")
                   Map<String, Object> data = _mapper.readValue(stream, Map.class);
                   if ((Integer)data.get("money") != 0) {
                       money = (Integer)data.get("money");
                   }

                   if (_user_db_sql.saveMoney(money, user_id) != -1) {
                       response.setStatus(HttpServletResponse.SC_OK);
                   }
                   else {
                       response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                   }

                   break;
               }
	    	case URT_UID_PVE: {
	    		try {
	    			handlePveRequest(request, response, user_id);
	    		} catch (IOException e) {
                    e.printStackTrace();
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                }
	    		break;
	    	}
            case URT_ACHIEVEMENT: {
                InputStream stream = request.getInputStream();
                int amount = 0;
                int character_id = 0;
                int achievement_id = 0;
                @SuppressWarnings("unchecked")
                Map<String, Object> data = _mapper.readValue(stream, Map.class);
               

                if (data.containsKey("amount")) {
                    amount = (Integer)data.get("amount");
                }
                
                if (data.containsKey("achievement_id")) {
                    achievement_id = (Integer)data.get("achievement_id");
                }

                if (data.containsKey("character_id")) {
                    character_id = (Integer)data.get("character_id");
                }
                
                UserAchievement achievement = new UserAchievement();
                achievement.character_id = character_id;
                achievement.achievement_id = achievement_id;
                achievement.complete = true;

                User user = _user_db_sql.getUserByID(user_id);

                for (UserAchievement ua : user.user_achievements) {
                    if (ua.achievement_id == achievement_id) {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("earned", true);
                        map.put("achievement_id", achievement_id);
                        ResponseTools.prepareResponseJson(response, _mapper, map, HttpServletResponse.SC_OK);
                        //response.setStatus(HttpServletResponse.SC_BAD_REQUEST, "{\"earned\":\"true\",\"achievement_id\":" + achievement_id + "}");
                        return;
                    }
                }
                
                AchievementResponse achievement_response = new AchievementResponse();
                achievement_response.achievement_id = achievement_id;

                
                if (character_id <= 5) {
                    //add stat point to character_id
                    for (Character uc : user.user_characters) {
                        if (uc.character_id == character_id) {
                            uc.stats += amount;
                            achievement_response.character = uc;
                        }
                    }
                    achievement_response.tokens = user.tokens;
                    achievement_response.gold = user.money;
                    if (_ua_db_sql.userAchievementComplete(user.id, achievement, character_id, amount)) {
                        ResponseTools.prepareResponseJson(response, _mapper, achievement_response, HttpServletResponse.SC_OK);
                    }
                    return;
                }
                else if (character_id == 6) {
                    //gold reward
                    user.money += amount;
                    
                    achievement_response.gold = user.money;   
                    achievement_response.tokens = user.tokens;
                    if (_ua_db_sql.userAchievementComplete(user.id, achievement, character_id, amount)) {
                        ResponseTools.prepareResponseJson(response, _mapper, achievement_response, HttpServletResponse.SC_OK);
                    }
                    return;
                }
                else if (character_id == 7) {
                    //token reward
                    user.tokens += amount;

                    achievement_response.tokens = user.tokens;
                    achievement_response.gold = user.money;
                    if (_ua_db_sql.userAchievementComplete(user.id, achievement, character_id, amount)) {
                        ResponseTools.prepareResponseJson(response, _mapper, achievement_response, HttpServletResponse.SC_OK);
                    }
                    return;
                }
                else if (character_id == 8) {
                    user.inventory_spots += 9;
                    _user_db_sql.saveInventorySpots(9, user.id);

                    Accessory acc = _acc_db_sql.getAccessory(794);
                    _uca_db_sql.createUserCharacterAccessory(acc, user.id);
                    
                    achievement_response.user_character_accessory_id = acc.id;
                    achievement_response.tokens = user.tokens;
                    achievement_response.gold = user.money;
                    if (_ua_db_sql.userAchievementComplete(user.id, achievement, character_id, amount)) {
                        ResponseTools.prepareResponseJson(response, _mapper, achievement_response, HttpServletResponse.SC_OK);
                    }
                    return;
                }
                else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            break;
            }
    		default: {
    			throw new UnimplementedEnumException(req_type.toString() + " is not implemented yet");
    		}

       	}
    }

    private boolean checkReceipt(HttpServletRequest request) {
        try {
            InputStream stream = request.getInputStream();
            String receipt_data = _mapper.readValue(stream, ReceiptDataRequest.class).receipt;

            URL url = new URL("https://itunes.apple.com/verifyReceipt/");
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.connect();

            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
            System.out.println("{\"receipt-data\":\""+receipt_data+"\"}");
            out.write("{\"receipt-data\":\""+receipt_data+"\"}");
            out.flush();
            out.close();

            InputStream url_stream = connection.getInputStream();
            @SuppressWarnings("unchecked")
			Map<String, Object> data = _mapper.readValue(url_stream, Map.class);
            if ((Integer)data.get("status") != 0) {
                return false;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

//    private void recordIAP(final ArrayList<String> mac_addresses, final String iap_type) {
//        String relative_web_path = "/WEB-INF/mac_addresses_iap.csv";
//        String absolute_disk_path = getServletContext().getRealPath(relative_web_path);
//        File file = new File(absolute_disk_path);
//
//        SimpleWriter.append(file, new WriterCallback() {
//            public void withWriter(SimpleWriter writer) throws Exception {
//                mac_addresses.add(iap_type);
//
//                Object[] obj_array = mac_addresses.toArray(new Object[mac_addresses.size()]);
//                writer.append(obj_array);
//            }
//        });
//    }

    private void handlePveRequest(HttpServletRequest request, HttpServletResponse response, int user_id)
    		throws IOException {	
    	
    	InputStream stream = request.getInputStream();
    	if (stream == null) return;
    	
		PveRequest pve = _mapper.readValue(stream, PveRequest.class);
		stream.close();
		
		User user = _user_db_sql.getUserByID(user_id);
		if (user == null) {
			String message = String.format(ResponseMessages.USER_WITH_ID_NOT_FOUND, user_id);
			ResponseTools.prepareResponseJson(response, _mapper, message, Constants.SC_BAD_REQUEST);
			return;
		}
		
		if ((pve.user_node_id == -1) && !pve.pve_type.equals("s")) {
			String message = String.format(ResponseMessages.UNKNOWN_PVE_TYPE, pve.pve_type);
			ResponseTools.prepareResponseJson(response, _mapper, message, Constants.SC_BAD_REQUEST);
			return;
		}
		
		Accessory reward = PveBattle.battlePVE(user, pve, _b_db_sql, _acc_db_sql, _n_db_sql);
		if ((reward != null) && (reward.id == -1)) {
			response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
			return;
		}

        _mapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);

        ResponseTools.prepareResponseJson(response, _mapper, new PveResponse(user, reward), Constants.SC_OK);
        CBoxLoggerSyslog.log("pve","user_id","pve_type",user_id,pve.pve_type);
    }
    

    private void handlePvpRequest(HttpServletRequest request, HttpServletResponse response)
    		throws IOException {

        InputStream stream = request.getInputStream();
    	if (stream == null) return;
    	
		BattleOver pvp = _mapper.readValue(stream, BattleOver.class);
		stream.close();
		
		User winner = _user_db_sql.getUserByID(pvp.winner_id);
		if (winner == null) {
			String message = String.format(ResponseMessages.USER_WITH_ID_NOT_FOUND, pvp.winner_id);
			ResponseTools.prepareResponseJson(response, _mapper, message, Constants.SC_BAD_REQUEST);
			return;
		}
		
		if ((winner.user_characters == null) || (winner.user_characters.length <= 0)) {
			String message = String.format(ResponseMessages.USER_WITH_ID_HAS_NO_CHAR, pvp.winner_id);
			ResponseTools.prepareResponseJson(response, _mapper, message, Constants.SC_BAD_REQUEST);
			return;
		}
		
		User loser = _user_db_sql.getUserByID(pvp.loser_id);
		if (loser == null) {
			String message = String.format(ResponseMessages.USER_WITH_ID_NOT_FOUND, pvp.loser_id);
			ResponseTools.prepareResponseJson(response, _mapper, message, Constants.SC_BAD_REQUEST);
			return;
		}
		
		if ((loser.user_characters == null) || (loser.user_characters.length <= 0)) {
			String message = String.format(ResponseMessages.USER_WITH_ID_HAS_NO_CHAR, pvp.loser_id);
			ResponseTools.prepareResponseJson(response, _mapper, message, Constants.SC_BAD_REQUEST);
			return;
		}
		
		Playlist playlist = _pl_db_sql.getPlaylist(pvp.playlist_id);
		if (playlist == null) {
			String message = String.format(ResponseMessages.PLAYLIST_WITH_ID_NOT_FOUND, pvp.loser_id);
			ResponseTools.prepareResponseJson(response, _mapper, message, Constants.SC_BAD_REQUEST);
			return;
		}

		
		String message = PvpBattle.battlePVP(winner, loser, playlist, pvp.battle_end_state, pvp.loser_souls_earned, _b_db_sql, pvp.gold, pvp.experience);
		if (message != null) {
			ResponseTools.prepareResponseJson(response, _mapper, message, Constants.SC_EXPECTATION_FAILED);
			return;
		}
		
		PvpResponse prw = new PvpResponse(new UserWrapper(winner), new UserWrapper(loser));
		ResponseTools.prepareResponseJson(response, _mapper, prw, Constants.SC_OK);
        CBoxLoggerSyslog.log("pvp","winner_id","loser_id","battle_end_state",winner.id,loser.id,pvp.battle_end_state);
    }

    private void handleOfflinePvpRequest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        InputStream stream = request.getInputStream();
        if (stream == null) return;

        BattleOver pvp = _mapper.readValue(stream, BattleOver.class);
        stream.close();

        User user = _user_db_sql.getUserByID(pvp.user_id);
        if (user == null) {
            String message = String.format(ResponseMessages.USER_WITH_ID_NOT_FOUND, pvp.user_id);
            ResponseTools.prepareResponseJson(response, _mapper, message, Constants.SC_BAD_REQUEST);
            return;
        }

        if ((user.user_characters == null) || (user.user_characters.length <= 0)) {
            String message = String.format(ResponseMessages.USER_WITH_ID_HAS_NO_CHAR, pvp.user_id);
            ResponseTools.prepareResponseJson(response, _mapper, message, Constants.SC_BAD_REQUEST);
            return;
        }

        Playlist playlist = _pl_db_sql.getPlaylist(pvp.playlist_id);
        if (playlist == null) {
            String message = String.format(ResponseMessages.PLAYLIST_WITH_ID_NOT_FOUND, pvp.loser_id);
            ResponseTools.prepareResponseJson(response, _mapper, message, Constants.SC_BAD_REQUEST);
            return;
        }


        String message = PvpBattle.offlineBattlePvp(user, pvp.win, pvp.battle_end_state ,playlist, _b_db_sql, pvp.gold, pvp.experience);
        if (message != null) {
            ResponseTools.prepareResponseJson(response, _mapper, message, Constants.SC_EXPECTATION_FAILED);
            return;
        }

        PvpResponse prw = new PvpResponse(new UserWrapper(user), null);
        ResponseTools.prepareResponseJson(response, _mapper, prw, Constants.SC_OK);
        CBoxLoggerSyslog.log("offline_pvp","user_id",user.id);
    }
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "users");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setPostRequestType(uta);
		RequestType req_type = ret.getRequestType();
       	
       	if (req_type == URT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

       	switch (req_type) {
            case URT_SIGNIN: {
                try {
                    //NEED TO GET USERNAME/PASSWORD FROM INPUTSTREAM
                    InputStream stream = request.getInputStream();
                    UserLogin u = _mapper.readValue(stream, UserLoginWrapper.class).user;
                    String username = u.username;
                    String password = u.password;
                    String device_token = u.device_token;
                    String request_udid = u.udid;
                    String request_mac_address = u.mac_address;

                    //Query for user here using "username"
                    User user = _user_db_sql.getUserByUsername(username);
                    boolean sign_in = true;
                    if (user != null) {
                    	if (!BCrypt.checkpw(password, user.hashed_pw)) {
                    		ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.INCORRECT_PASSWORD, Constants.SC_BAD_REQUEST);
                        	return;
                    	}
                        //Set hashed_password to null after it is checked, we don't want to send it in the json
                        user.hashed_pw = null;
                    }
                    else {
                        //Create a new user with BCrypt hashed password
                        //Use this line of code to generate password from plain text: String pw_hash = BCrypt.hashpw(plain_password, BCrypt.gensalt(12));
                        user = _user_db_sql.createUser(username, password, device_token, request_udid, request_mac_address);
                        sign_in = false; // sign_up
                    }

                    if (user == null) {
                    	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    	return;
                    }
                    if (!u.test) CBoxLoggerSyslog.log(sign_in ? "sign_in" : "sign_up","user_id",user.id);
                    if (sign_in)
                        Utility.verifyUserData(user, _uc_db_sql);
                    
                    AuthenticateUser.authenticate(request, response);
   
                    //store user_id in a session attribute
                    HttpSession session = request.getSession();
                    session.setAttribute("user_id", user.id);

                    //Set the admin status
                    if (user.admin) {
                        session.setAttribute(AuthenticateUser.USER_ADMIN_KEY, true);
                        session.setMaxInactiveInterval(-1);
                    }
                    else {
                        //A negative value for setMaxInactiveInterval causes the session to never expire
                        session.setMaxInactiveInterval(Constants.MAX_SESSION_INTERVAL_ONE_DAY);
                    }
                    
                    //If udid or mac_address is null, save the one from the call
                    if ( (user.udids != null && user.mac_addresses != null) && (!user.udids.contains(request_udid) || !user.mac_addresses.contains(request_mac_address)) ) {
                        if (u.udid != null || u.mac_address != null) {
                            _user_db_sql.saveUniqueIdentifier(user.id, u.udid, u.mac_address);
                        }
                    }

                    //session.setAttribute(AuthenticateUser.USER_ADMIN_KEY, user.admin);
                    //Look for tapjoy tokens and empty them
                    if (user.tapjoy_tokens != 0) {
                    	user.tokens += user.tapjoy_tokens;
                        //Only set Tapjoy tokens to 0 in the DB
                    	if (!_user_db_sql.updateUserTwoFields(user.tokens, "tokens", 0, "tapjoy_tokens", user.id)) {
                         	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                     		return;
                        }
                    }

                    DateTime now = new DateTime(new Date());
                    if (user.created_at != null) {
                        DateTime first_sign_in = new DateTime(user.created_at);
                        int days_after_creation = Days.daysBetween(first_sign_in, now).getDays();
                        if (days_after_creation > 0) {
                            CBoxLoggerSyslog.log("retention", "user_id", "days_after_creation", user.id, days_after_creation);
                        }
                    }
                    
                    if (user.last_sign_in_at != null) {
                        DateTime last_sign_in = new DateTime(user.last_sign_in_at);
                        int days_between_login = Days.daysBetween(last_sign_in, now).getDays();
                        if (!now.minuteOfDay().equals(last_sign_in.minuteOfDay())) {
                            CBoxLoggerSyslog.log("retention", "user_id", "days_between_login", user.id, days_between_login);
                        }
                    }

                    //Write new_user to JSON
                    _mapper.configure(SerializationConfig.Feature.AUTO_DETECT_GETTERS, false);
                    response.addHeader("Access-Control-Allow-Origin", "http://localhost");
                    ResponseTools.prepareResponseJson(response, _mapper, new UserWrapper(user), Constants.SC_OK);
                    //if (!u.test) CBoxLoggerSyslog.log("sign_in","user_id",user.id);
                } catch (IOException e) {
                    e.printStackTrace();
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                }
                catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                }
                break;
            }
       		case URT_EXIST: {
                   break;
       		}
       		default: {
       			throw new UnimplementedEnumException(req_type.toString() + " is not implemented yet");
       		}
       	}
    }
    
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	
    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "users");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
		RequestTypeUserId ret = setDeleteRequestType(uta);
		RequestType req_type = ret.getRequestType();
       	int user_id = ret.getUserId();
       	
       	if (req_type == URT_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	switch (req_type) {
	   		case URT_DESTROY: {
                if (AuthenticateUser.isAdmin(request)) {
                    if (_user_db_sql.deleteUser(user_id) == false) {
                        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                    else {
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
                else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                }

	   			break;
	   		}
	   		default: {
	   			throw new UnimplementedEnumException(req_type.toString() + " is not implemented yet");
	   		}
	   	}
    }
    
 
    
    public static enum RequestType {
    	
    	URT_DETAILS, // GET details for all users < /users/details >
    	URT_TAPJOY, // GET < /users/tapjoy >
    	URT_UID_DETAILS, // GET details for a given user_id < /users/{user_id}/details >
    	URT_GET_ACCESSORIES, // GET accessories for a given user_id < /users/{user_id}/get_accessories >
    	URT_VERIFY, // GET < /users/verify >
    	URT_UID_VERIFY, // GET < /users/verify >
        URT_OFFLINE_USER, // GET < /users/offline_user >
    	
    	URT_ACHIEVEMENT, // PUT </users/achievement>
        URT_PVP, // PUT < /users/pvp >
        URT_OFFLINE_PVP, //PUT < /users/offline_pvp >
    	URT_CHARACTERS, // PUT < /users/{user_id}/characters >
    	URT_SMALL_BAG_OF_GOODIES, // PUT < /users/{user_id}/sbg >
    	URT_BOX_OF_GOODIES, // PUT < /users/{user_id}/bog >
    	URT_GIANT_SAG_OF_GOODIES, // PUT < /users/{user_id}/gsg >
    	URT_MASSIVE_SAG_OF_GOODIES, // PUT < /users/{user_id}/msg >
    	URT_HAND_FULL_OF_GOODIES, // PUT < /users/{user_id}/hfg >
    	URT_MOUTH_FULL_OF_GOODIES, // PUT < /users/{user_id}/mfsg >
    	URT_GOLD_MINE, // PUT < /users/{user_id}/gom >
    	URT_AMNESIA, // PUT < /users/{user_id}/amn >
    	URT_CHARACTER_SLOT, // PUT < /users/{user_id}/chs >
    	URT_BREAD_STICK, // PUT < /users/{user_id}/bre >
    	URT_BREAD_SLICE, // PUT < /users/{user_id}/bsl >
    	URT_BREAD_LOAF, // PUT < /users/{user_id}/blf >
    	URT_FRESH_BOOTY, // PUT < /users/{user_id}/frb >
    	URT_REFORGE, // PUT < /users/{user_id}/wrt >
    	URT_WEAPON, // PUT < /users/{user_id}/wpn >
    	URT_EXPANDED_SACK, // PUT < /users/{user_id}/sck >
        URT_NO_RP, //PUT < /users/{user_id}/nrp >
        URT_NO_ADS, //PUT < /users/{user_id}/nad >
        URT_EXP_SCROLL, //PUT < /users/{user_id}/exp >
    	URT_UID_PVE, // PUT for a given user_id < /users/{user_id}/pve >

        URT_ADD_TOKENS, // PUT for a given user_id < /users/{user_id}/tokens>
        URT_ADD_MONEY, // PUT for a given user_id < /users/{user_id}/money>
    	
    	URT_EXIST, // POST < /users/exist >
        URT_SIGNIN, // POST < /users/sign_in >
    	
    	URT_DESTROY, // DELTE < /users/{user_id}/destroy >
    	
    	URT_UNDEFINED;
    	
    	public static RequestTypeUserId setGetRequestType(UriToArgv uta) {
    		
      		RequestTypeUserId rt = new RequestTypeUserId(0, URT_UNDEFINED);
			int indx = uta.getBaseIndex() + 1;
			String[] argv = uta.getArgv();
			
    		try {
    	       	if (indx >= argv.length) {
    	       		rt.setRequestType(URT_DETAILS);
    	       		return rt;  // GET (all users)
    	       	} 
    	       	if (argv[indx].equals("tapjoy")) {
    	       		rt.setRequestType(URT_TAPJOY);
    	       		return rt; // GET
    	       	}
    	       	if (argv[indx].equals("details")) {
    	       		rt.setRequestType(URT_DETAILS);
    	       		return rt; // GET (all users)
    	       	}
          		if (argv[indx].equals("verify")) {
        			rt.setRequestType(URT_VERIFY);
           			return rt; // GET 1 user										
    	    	}
                if (argv[indx].equals("offline_user")) {
                    rt.setRequestType(URT_OFFLINE_USER);
                    return rt; // GET 1 user										
                }
                

           		rt.setUserId(Integer.parseInt(argv[indx]));
           		
           		indx++;
           		if (indx >= argv.length) {
    	       		rt.setRequestType(URT_UID_DETAILS);
           			return rt; // GET 1 user
           		}
           		if (argv[indx].equals("details")) {
           			rt.setRequestType(URT_UID_DETAILS);
           			return rt; // GET 1 user
    	       	}
           		if (argv[indx].equals("get_accessories")) {
        			rt.setRequestType(URT_GET_ACCESSORIES);
           			return rt; // GET 1 user										
    	    	}
           		if (argv[indx].equals("verify")) {
        			rt.setRequestType(URT_UID_VERIFY);
           			return rt; // GET 1 user										
    	    	}
 
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		System.err.println("Unable to parse " + argv[indx] + " as a user id");
        		e.printStackTrace();
    		}
        	
        	return rt;
        }
    	
    	public static RequestTypeUserId setPutRequestType(UriToArgv uta) {
        	
        	RequestTypeUserId rt = new RequestTypeUserId(0, URT_UNDEFINED);
    		int indx = uta.getBaseIndex() + 1;
    		String[] argv = uta.getArgv();

        	try {
    	       	if (indx >= argv.length) {
    	       		return rt;
    	       	}
    	       	if (argv[indx].equals("pvp")) {
    	   			rt.setRequestType(URT_PVP); // PUT
    	   			return rt;
    	       	}
                if (argv[indx].equals("offline_pvp")) {
                    rt.setRequestType(URT_OFFLINE_PVP); // PUT
                    return rt;
                }
    	       	
           		rt.setUserId(Integer.parseInt(argv[indx]));
           		
           		indx++;
           		if (indx >= argv.length) {
           			return rt;
    	       	}
           		if (argv[indx].equals("characters")) {
    	   			rt.setRequestType(URT_CHARACTERS); // PUT 1 user
    	   			return rt;
    	    	}
           		if (argv[indx].equals("sbg")) {
           			rt.setRequestType(URT_SMALL_BAG_OF_GOODIES); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("bog")) {
           			rt.setRequestType(URT_BOX_OF_GOODIES); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("gsg")) {
           			rt.setRequestType(URT_GIANT_SAG_OF_GOODIES); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("msg")) {
           			rt.setRequestType(URT_MASSIVE_SAG_OF_GOODIES); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("hfg")) {
           			rt.setRequestType(URT_HAND_FULL_OF_GOODIES); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("mfg")) {
           			rt.setRequestType(URT_MOUTH_FULL_OF_GOODIES); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("gom")) {
           			rt.setRequestType(URT_GOLD_MINE); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("amn")) {
           			rt.setRequestType(URT_AMNESIA); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("chs")) {
           			rt.setRequestType(URT_CHARACTER_SLOT); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("bre")) {
           			rt.setRequestType(URT_BREAD_STICK); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("bsl")) {
           			rt.setRequestType(URT_BREAD_SLICE); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("blf")) {
           			rt.setRequestType(URT_BREAD_LOAF); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("frb")) {
           			rt.setRequestType(URT_FRESH_BOOTY); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("wrt")) {
           			rt.setRequestType(URT_REFORGE); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("wpn")) {
           			rt.setRequestType(URT_WEAPON); // PUT 1 user
           			return rt;
    	    	}
           		if (argv[indx].equals("sck")) {
           			rt.setRequestType(URT_EXPANDED_SACK); // PUT 1 user
           			return rt;
    	    	}
                if (argv[indx].equals("nrp")) {
                    rt.setRequestType(URT_NO_RP); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("nad")) {
                    rt.setRequestType(URT_NO_ADS); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("exp")) {
                    rt.setRequestType(URT_EXP_SCROLL); // PUT 1 user
                    return rt;
                }
           		if (argv[indx].equals("pve")) {
           			rt.setRequestType(URT_UID_PVE); // PUT 1 user	
           			return rt;
           		}
                if (argv[indx].equals("tokens")) {
                    rt.setRequestType(URT_ADD_TOKENS); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("money")) {
                    rt.setRequestType(URT_ADD_MONEY); // PUT 1 user
                    return rt;
                }
                if (argv[indx].equals("achievement")) {
                    rt.setRequestType(URT_ACHIEVEMENT); // PUT 1 user
                    return rt;
                }
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		System.err.println("Unable to parse " + argv[indx] + " as a user id");
        		e.printStackTrace();
    		}
        	
        	return rt;
        }
    	
        public static RequestTypeUserId setPostRequestType(UriToArgv uta) {
        	
        	RequestTypeUserId rt = new RequestTypeUserId(0, URT_UNDEFINED);
    		int indx = uta.getBaseIndex() + 1;
    		String[] argv = uta.getArgv();
    		
           	if (indx >= argv.length) {
           		return rt; // UNDEFINED
           	}
           	
           	if (argv[indx].equals("exist")) {
           		rt.setRequestType(URT_EXIST); // POST
           		return rt;
           	}

            if (argv[indx].equals("sign_in")) {
                rt.setRequestType(URT_SIGNIN); //POST
                return rt;
            }
           	
       		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
      
        	return rt; // UNDEFINED
        }
        
        public static RequestTypeUserId setDeleteRequestType(UriToArgv uta) {
    		
      		RequestTypeUserId rt = new RequestTypeUserId(0, URT_UNDEFINED);
			int indx = uta.getBaseIndex() + 1;
			String[] argv = uta.getArgv();
			
    		try {
    	       	if (indx >= argv.length) {
    	       		return rt;  // UNDEFINED
    	       	} 

           		rt.setUserId(Integer.parseInt(argv[indx]));
           		
           		indx++;
           		if (indx >= argv.length) {
           			return rt; // UNDEFINED
           		}
           		if (argv[indx].equals("destroy")) {
           			rt.setRequestType(URT_DESTROY); // DELETE
           			return rt; // GET 1 user
    	       	}
          		if (argv[indx].equals("delete")) {
           			rt.setRequestType(URT_DESTROY); // DELETE
           			return rt; // GET 1 user
    	       	}
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	} catch (NumberFormatException e) {
        		System.err.println("Unable to parse " + argv[indx] + " as a user id");
        		e.printStackTrace();
    		}
        	
        	return rt;
        }
    }
    
    public static class RequestTypeUserId {
    	public RequestTypeUserId(int uid, RequestType type) {
    		_user_id = uid;
    		_req_type = type;
    	}
    	void setUserId(int id) { _user_id = id; }
    	public int getUserId() { return _user_id; }
    	void setRequestType(RequestType type) { _req_type = type; }
    	public RequestType getRequestType() { return _req_type; }
    	
    	private int _user_id;
    	private RequestType _req_type;
    }

}
