package com.cboxgames.idonia.backend;

import com.cboxgames.idonia.backend.commons.*;
import com.cboxgames.idonia.backend.commons.db.accessory.AccessoryDBSQL;
import com.cboxgames.idonia.backend.commons.db.user.UserDBSQL;
import com.cboxgames.idonia.backend.commons.db.usercharacter.UserCharacterDBSQL;
import com.cboxgames.idonia.backend.commons.db.usercharacteraccessory.UserCharacterAccessoryDBSQL;
import static com.cboxgames.idonia.backend.UserCharacterAccessoryHttpServlet.RequestType.*;

import com.cboxgames.utils.idonia.types.*;
import com.cboxgames.utils.idonia.types.Accessory.AccessoryWrapper;
import com.cboxgames.utils.idonia.types.Accessory.AccessoryStatsUpdateWrapper;
import com.cboxgames.utils.idonia.types.Accessory.WeaponUpdate;
import com.cboxgames.utils.idonia.types.Character;
import com.cboxgames.utils.idonia.types.Character.CharacterAccessoryBuy;
import com.cboxgames.utils.idonia.types.Character.UpdateStats;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Alamgir
 * Date: 12/11/11
 * Time: 5:51 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings("serial")
public class UserCharacterAccessoryHttpServlet extends HttpServlet {

	private SqlDataSource _sql_data_source;
	private UserDBSQL _user_db_sql;
	private AccessoryDBSQL _acc_db_sql;
    private UserCharacterDBSQL _uc_db_sql;
    private UserCharacterAccessoryDBSQL _uca_db_sql;
    private ObjectMapper _mapper;

	@Override
	public void init() {
		
        _sql_data_source = new SqlDataSource();
        _mapper = new ObjectMapper();
        _mapper.getSerializationConfig().setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL); // no more null-valued properties
        
        try {
        	_user_db_sql = new UserDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _uc_db_sql = new UserCharacterDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _uca_db_sql = new UserCharacterAccessoryDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
            _acc_db_sql = new AccessoryDBSQL(_sql_data_source.get_data_source(), this.getServletContext());
        } catch (SQLException e) {
        	e.printStackTrace();
        }
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		// URI: /user_character_accessories/details
    	String uri_str = request.getRequestURI();
       	if (UriToArgv.verifyUrl(uri_str, "user_character_accessories", "details") == false) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}
       	
       	//TODO: DESABLED BECAUSE IT WILL DESTROY US.
       	//ResponseTools.prepareResponseJson(response, _mapper, _uca_db_sql.getUserCharacterAccessoryDetails(), Constants.SC_OK);
	}

    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "user_character_accessories");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

		RequestTypeUserId ret = setPutRequestType(uta);
		RequestType req_type = ret.getRequestType();
       	int acc_id = ret.getAccId();

       	if (req_type == UCART_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

       	switch (req_type) {
            case UCART_STATS: {
                InputStream stream = request.getInputStream();
                UpdateStats new_acc_stats = _mapper.readValue(stream, AccessoryStatsUpdateWrapper.class).user_character_accessory;
                Accessory acc = _uca_db_sql.getUserCharacterAccessoryByID(acc_id);
                int current_stats_total = acc.strength + acc.agility + acc.intelligence + acc.vitality + acc.will;
                int new_stats_total = new_acc_stats.strength + new_acc_stats.agility + new_acc_stats.intelligence + new_acc_stats.vitality + new_acc_stats.will; 
                int delta = new_stats_total - current_stats_total;
                if (acc.stats < delta) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.INCORRECT_STATS, Constants.SC_BAD_REQUEST);
                    return;
                }
                acc.stats -= delta;
                
                acc.strength = new_acc_stats.strength;
                acc.agility = new_acc_stats.agility;
                acc.intelligence = new_acc_stats.intelligence;
                acc.vitality = new_acc_stats.vitality;
                acc.will = new_acc_stats.will;

                if (_uca_db_sql.updateUserCharacterAccessory(acc)) {
                    ResponseTools.prepareResponseJson(response, _mapper, null, Constants.SC_OK);
                }
                else {
                    response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                }
                break;
            }
	   		case UCART_CHANGE: {
                InputStream stream = request.getInputStream();
                WeaponUpdate update_info = _mapper.readValue(stream, WeaponUpdate.class);

                HttpSession session = request.getSession();
                int user_id = (Integer) session.getAttribute("user_id");

                Accessory current_weapon = _uca_db_sql.getUserCharacterAccessoryByID(update_info.current_weapon_id);
                Character user_character = _uc_db_sql.getUserCharacterByID(current_weapon.user_character_id).character;
                Accessory new_weapon = _acc_db_sql.getAccessory(update_info.new_weapon_id);

                // Get user info and carry out checks
                User user = _user_db_sql.getUserOnlyByID(user_id);

                if (!update_info.change) {
                    if (user.money < new_weapon.cost) {
                        ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.INSUFFICIENT_MONEY,
            					Constants.SC_BAD_REQUEST);
                        return;
                        
                    }
                    if (user_character.level < Constants.TIER1_TO_TIER2_WEAPON_UPGRADE_MIN_LEVEL && new_weapon.tier == 2) {
                        response.setStatus(Constants.SC_BAD_REQUEST);
                        return;
                        
                    }
                    if (user_character.level < Constants.TIER2_TO_TIER3_WEAPON_UPGRADE_MIN_LEVEL && new_weapon.tier == 3) {
                        response.setStatus(Constants.SC_BAD_REQUEST);
                        return;
                    }
                    
                    user.money -= new_weapon.cost;
                    if (_user_db_sql.updateUserOneField(user.money, "money", user_id) == false) {
                    	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    	return;
                    }
                    
                    current_weapon.strength += new_weapon.strength - current_weapon.strength;
                    current_weapon.vitality += new_weapon.vitality - current_weapon.vitality;
                    current_weapon.agility += new_weapon.agility - current_weapon.agility;
                    current_weapon.intelligence += new_weapon.intelligence - current_weapon.intelligence;
                    current_weapon.will += new_weapon.will - current_weapon.will;
                    current_weapon.armor += new_weapon.armor - current_weapon.armor;
                    current_weapon.dodge += new_weapon.dodge - current_weapon.dodge;
                    current_weapon.physical_crit += new_weapon.physical_crit - current_weapon.physical_crit;
                    current_weapon.spell_crit += new_weapon.spell_crit - current_weapon.spell_crit;
                    current_weapon.accessory_id = new_weapon.id;
                    current_weapon.tier = new_weapon.tier;
                    if ( _uca_db_sql.updateUserCharacterAccessory(current_weapon)) {   
                        ResponseTools.prepareResponseJson(response, _mapper, current_weapon, Constants.SC_OK);
                    }
                    else {
                    	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    }
                }
                else {
                    Accessory new_character_weapon = new Accessory();
                    new_character_weapon.user_id = user_id;
                    new_character_weapon.accessory_id = new_weapon.id;
                    new_character_weapon.user_character_id = 0;
                    new_character_weapon.strength = new_weapon.strength;
                    new_character_weapon.vitality = new_weapon.vitality;
                    new_character_weapon.intelligence = new_weapon.intelligence;
                    new_character_weapon.will = new_weapon.will;
                    new_character_weapon.agility = new_weapon.agility;
                    new_character_weapon.dodge = new_weapon.dodge;
                    new_character_weapon.armor = new_weapon.armor;
                    new_character_weapon.physical_crit = new_weapon.physical_crit;
                    new_character_weapon.spell_crit = new_weapon.spell_crit;
                    new_character_weapon.max_experience = 120;
                    new_character_weapon.stats = user_character.level;
                    new_character_weapon.accessory_type = new_weapon.accessory_type;
                    new_character_weapon.level_requirement = new_weapon.level_requirement;

                    user_character.weapon_swap = false;
                    List<Character> user_character_list = new ArrayList<Character>();
                    user_character_list.add(user_character);
                    _uc_db_sql.saveUserCharacters(user_character_list);

                    if (_uca_db_sql.createUserCharacterAccessory(new_character_weapon, user_id) != -1) {
                        ResponseTools.prepareResponseJson(response, _mapper, new_character_weapon, Constants.SC_OK);
                    }
                    else {
                    	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    }
                }
                break;
	   		}
	   		default: {
                response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                break;
	   		}
	   	}
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "user_character_accessories");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

		RequestTypeUserId ret = setPostRequestType(uta);
		RequestType req_type = ret.getRequestType();

       	if (req_type == UCART_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

       	switch (req_type) {
	   		case ACC_BUY: {
                //cost, level, and stats
                InputStream stream = request.getInputStream();
                Accessory acc = _mapper.readValue(stream, AccessoryWrapper.class).accessory;

                HttpSession session = request.getSession();
                int user_id = (Integer) session.getAttribute("user_id");

                acc.damage_high = acc.damage_low = acc.tier = acc.user_character_id = 0;
                acc.accessory_id = acc.id;

                User user = _user_db_sql.getUserOnlyByID(user_id);

                if (user.money < acc.cost) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.INSUFFICIENT_MONEY,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                
                List<Accessory> inventory = _uca_db_sql.getUserCharacterAccessoryInventory(user_id);
                if (inventory.size() == user.inventory_spots ) {
                	ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.ACCESSORY_INVENTORY_FULL,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                
                user.money -= acc.cost;
                if (_user_db_sql.updateUserOneField(user.money, "money", user_id) == false) {
                	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
                	return;
                }
                
                int created_id = _uca_db_sql.createUserCharacterAccessory(acc, user_id);
                if (created_id == -1) {
                	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                	return;
                }
        		
        		ResponseTools.prepareResponseJson(response, _mapper, created_id, Constants.SC_OK);
                CBoxLoggerSyslog.log("buy_accessory", "user_id","accessory_type","gold_spent",user_id,"Accessory",acc.cost);
                break;
	   		}
            case WA_BUY: {
                InputStream stream = request.getInputStream();
                CharacterAccessoryBuy buy_request = _mapper.readValue(stream, CharacterAccessoryBuy.class);
                int buy_acc_id = buy_request.non_accessory_id;

                HttpSession session = request.getSession();
                int user_id = (Integer) session.getAttribute("user_id");

//                User user = _user_db_sql.getUserOnlyByID(user_id);
                User user = _user_db_sql.getUserWithAccessoryByID(user_id, buy_request.user_character_id);
                if ( ((user.user_characters == null) || (user.user_characters.length <= 0)) && (buy_request.user_character_id != 0)) {
                	String message = String.format(ResponseMessages.BAD_USER_CHAR_ID, buy_request.user_character_id);
                	ResponseTools.prepareResponseJson(response, _mapper, message, Constants.SC_BAD_REQUEST);
        			return;
                }
                
                Accessory new_equipment = _acc_db_sql.getAccessory(buy_acc_id);
                if (new_equipment.premium) {
                    if (user.tokens < new_equipment.cost) {
                        ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.INSUFFICIENT_TOKENS,
                                                          Constants.SC_BAD_REQUEST);
                        return;
                    }
                }
                else if (user.money < new_equipment.cost) {
                    ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.INSUFFICIENT_MONEY_TO_BUY_ACCESSORY,
        					Constants.SC_BAD_REQUEST);
                    return;
                }
                
                Character user_char = null;
                if (buy_request.user_character_id != 0) {
                    user_char = user.user_characters[0];
                    if (user_char.character_id != new_equipment.character_id) {
                        ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.ACCESSORY_NOT_BELONG_TO_CHARACTER,
                                                          Constants.SC_BAD_REQUEST);
                        return;
                    }
                }
                
//                List<Accessory> inventory = _uca_db_sql.getUserCharacterAccessoryInventory(user_id);
//                if (inventory.size() == user.inventory_spots ) {
                if (user.user_character_accessories_inventory != null) {
        			if (user.user_character_accessories_inventory.length >= user.inventory_spots) {
        				ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.ACCESSORY_INVENTORY_FULL,
        					Constants.SC_BAD_REQUEST);
        				return;
        			}
        			
        			for (Accessory acc : user.user_character_accessories_inventory) {
        				if (acc.accessory_id != buy_acc_id) continue;
        				ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.ALREADY_OWN_EQUIPMENT, Constants.SC_BAD_REQUEST);
        				return;
        			}
                }
                
                // check if the user character has own such accessory
                if (user_char != null) {
                    for (Accessory acc : user_char.user_character_accessories) {
                        if (acc.accessory_id != buy_acc_id) continue;
                        ResponseTools.prepareResponseJson(response, _mapper, ResponseMessages.ALREADY_OWN_EQUIPMENT, Constants.SC_BAD_REQUEST);
                        return;
                    }
                }

                //Analytics check for first armor purchase
                if ( (user.user_character_accessories_sold == null || user.user_character_accessories_sold.length == 0) &&
                     (user.user_character_accessories_inventory == null || user.user_character_accessories_inventory.length == 0) ) {
                    
                    CBoxLoggerSyslog.log("tutorial", "user_id", "bought_armor", user.id, new_equipment.id);
                }

 
                if (new_equipment.premium) {
                    user.tokens -= new_equipment.cost;
                    if (_user_db_sql.updateUserOneField(user.tokens, "tokens", user_id) == false) {
                        response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                        CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
                        return;
                    }
                }
                else {
                    user.money -= new_equipment.cost;
                    if (_user_db_sql.updateUserOneField(user.money, "money", user_id) == false) {
                        response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                        CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_user_update",user.id);
                        return;
                    }
                }


                Accessory new_character_weapon = new Accessory();
                new_character_weapon.user_id = user_id;
                new_character_weapon.accessory_id = new_equipment.id;
                new_character_weapon.user_character_id = 0;
                new_character_weapon.rarity = new_equipment.rarity;
                new_character_weapon.accessory_type = new_equipment.accessory_type;
                new_character_weapon.name = new_equipment.name;
                new_character_weapon.cost = new_equipment.cost;
                new_character_weapon.strength = new_equipment.strength;
                new_character_weapon.vitality = new_equipment.vitality;
                new_character_weapon.agility = new_equipment.agility;
                new_character_weapon.intelligence = new_equipment.intelligence;
                new_character_weapon.will = new_equipment.will;
                new_character_weapon.armor = new_equipment.armor;
                new_character_weapon.dodge = new_equipment.dodge;
                new_character_weapon.physical_crit = new_equipment.physical_crit;
                new_character_weapon.spell_crit = new_equipment.spell_crit;
                new_character_weapon.damage_high = new_equipment.damage_high;
                new_character_weapon.damage_low = new_equipment.damage_low;
                new_character_weapon.filenames = new_equipment.filenames;
                new_character_weapon.character_id = new_equipment.character_id;
                new_character_weapon.level_requirement = new_equipment.level_requirement;
                new_character_weapon.premium = new_equipment.premium;

                int new_character_weapon_id = _uca_db_sql.createUserCharacterAccessory(new_character_weapon, user_id);

                if ( new_character_weapon_id == -1) {
                	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                    CBoxLoggerSyslog.log("error","error_type","user_id","unsuccessful_uca_create",user.id);
                	return;
                }
               
                ResponseTools.prepareResponseJson(response, _mapper, new_character_weapon_id, Constants.SC_OK);
                CBoxLoggerSyslog.log("buy_non_accessory","user_id","gold_spent","accessory_type","accessory_name",user_id,new_equipment.cost,new_equipment.accessory_type,new_equipment.name);
            }
	   		default: {
                response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
	   			break;
	   		}
	   	}
    }


    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {

    	String uri_str = request.getRequestURI();
       	UriToArgv uta = new UriToArgv(uri_str, "user_character_accessories");
       	if (uta.getBaseIndex() >= uta.getArgv().length) {
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

		RequestTypeUserId ret = setDeleteRequestType(uta);
		RequestType req_type = ret.getRequestType();

       	if (req_type == UCART_UNDEFINED)	{
       		ResponseTools.prepareResponseJson(response, _mapper, uri_str + " is not supported by " + this.getServletName(),
					Constants.SC_BAD_REQUEST);
       		return;
       	}

       	switch (req_type) {
	   		case UCART_AID_SELL: {
	   	       	int acc_id = ret.getAccId();
                int user_id = (Integer) request.getSession().getAttribute("user_id");
                Accessory inv_acc = _uca_db_sql.getUserCharacterAccessoryByID(acc_id);
                if (inv_acc.user_character_id != 0) {
                    response.sendError(Constants.SC_BAD_REQUEST, "You cannot sell an accessory in use");
                    CBoxLoggerSyslog.log("error","error_type","accessory_id","sold_accessory_in_use",acc_id);
                    return;
                }
                int add_money = (int) (inv_acc.cost*0.3);
                int new_money = _user_db_sql.saveMoney(add_money, user_id);
                if (new_money == -1) {
                	response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                	return;
                }

                if (inv_acc.node_id != 0) {
                    if (_uca_db_sql.deleteUserCharacterAccessory(inv_acc.id) == false) {
                        response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                        CBoxLoggerSyslog.log("error","error_type","accessory_id","unsuccessful_accessory_delete",acc_id);
                        return;
                    }
                }
                else {
                    if (_uca_db_sql.sellUserCharacterAccessory(inv_acc.id) == false) {
                        response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
                        CBoxLoggerSyslog.log("error","error_type","accessory_id","unsuccessful_accessory_sold",acc_id);
                        return;
                    }
                }

 
                ResponseTools.prepareResponseJson(response, _mapper, new_money, Constants.SC_OK);
                CBoxLoggerSyslog.log("sell_accessory","user_id","gold_rewarded",user_id,add_money);
                break;
	   		}
	   		default: {
                response.setStatus(Constants.SC_INTERNAL_SERVER_ERROR);
	   			break;
	   		}
	   	}
    }


    public static enum RequestType {

    	UCART_CHANGE,
        ACC_BUY,
        WA_BUY,
    	UCART_AID_SELL,
        UCART_STATS,
    	UCART_UNDEFINED;
    
    	public static RequestTypeUserId setPutRequestType(UriToArgv uta) {

        	RequestTypeUserId rt = new RequestTypeUserId(0, UCART_UNDEFINED);
    		int indx = uta.getBaseIndex() + 1;
    		String[] argv = uta.getArgv();

           		if (indx >= argv.length) {
           			rt.setRequestType(UCART_STATS); // user_character_accessories/****
                    return rt;
    	       	}
           		if (argv[indx].equals("change")) {
    	   			rt.setRequestType(UCART_CHANGE); // PUT 1 user
    	   			return rt;
    	    	}
           		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.
        	return rt;
        }

        public static RequestTypeUserId setPostRequestType(UriToArgv uta) {

        	RequestTypeUserId rt = new RequestTypeUserId(0, UCART_UNDEFINED);
    		int indx = uta.getBaseIndex() + 1;
    		String[] argv = uta.getArgv();

           	if (indx >= argv.length) {
           		return rt; // UNDEFINED
           	}
           	if (argv[indx].equals("buy")) {
           		rt.setRequestType(ACC_BUY); // POST
           		return rt;
           	}
            if (argv[indx].equals("buy_non_accessory")) {
                rt.setRequestType(WA_BUY);
                return rt;
            }
       		// Ignore extra strings in argv[indx + 1], argv[indx + 2] if any.

        	return rt; // UNDEFINED
        }

        public static RequestTypeUserId setDeleteRequestType(UriToArgv uta) {

      		RequestTypeUserId rt = new RequestTypeUserId(0, UCART_UNDEFINED);
    		int indx = uta.getBaseIndex() + 1;
    		String[] argv = uta.getArgv();

        	try {
    	       	if (indx >= argv.length) {
    	       		return rt;
    	       	}

           		rt.setAccId(Integer.parseInt(argv[indx]));
                indx++;
                if (indx >= argv.length) {
                    return rt;
                }
                if (argv[indx].equals("sell")) {
                    rt.setRequestType(UCART_AID_SELL);
                    return rt;
                }

        	} catch (NumberFormatException e) {
        		System.err.println("Unable to parse " + argv[indx] + " as an accessory id");
        		e.printStackTrace();
    		}
        	return rt;
        }
    }

    public static class RequestTypeUserId {
    	public RequestTypeUserId(int aid, RequestType type) {
    		_acc_id = aid;
    		_req_type = type;
    	}
    	void setAccId(int id) { _acc_id = id; }
    	public int getAccId() { return _acc_id; }
    	void setRequestType(RequestType type) { _req_type = type; }
    	public RequestType getRequestType() { return _req_type; }

    	private int _acc_id;
    	private RequestType _req_type;
    }
}
